package run.cmdi.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.plugin.PluginMainOne;
import run.cmdi.common.poi.core.SheetUtils;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.pligins.ConvertPlugin;
import run.cmdi.common.reader.pligins.ConvertPluginList;
import run.cmdi.common.reader.pligins.ValuePosition;
import run.cmdi.common.utils.ReflectLcUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.sql.Timestamp;
import java.util.*;

/**
 * @author leichao
 */

public class ConvertDataToWorkbook<T, PAGE, UNIT> extends EntityBuildings<T> implements DataConvertInterface<T> {

    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    //private final ArrayList<String> headNames = new ArrayList<String>();
    private final WorkbookInfo workbookInfo;
    private final HashMap<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
    private int rownum = 0;
    private Sheet sheet;
    @Getter
    private final Map<String, PluginMainOne<ConvertPlugin<T>>> map;
    @Getter
    private boolean headState = false;
    @Getter
    private boolean state = false;
    private String sheetName;

    /**
     * 通过添加fieldName将指定对象从转换中移除
     */
    public ConvertDataToWorkbook<T, PAGE, UNIT> exceptFieldName(String... fieldName) {
        for (int i = 0; i < fieldName.length; i++) {
            map.remove(fieldName[i]);
        }
        return this;
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, Class<T> clazz)
            throws IOException, ConverterExcelException {
        this(workbookInfo, null, ConvertPlugin.buildMap(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Class<T> clazz) {
        this(workbookInfo, sheetName, ConvertPlugin.buildMap(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Map<String, PluginMainOne<ConvertPlugin<T>>> map,
                                 Class<T> clazz) {
        super(clazz, new ReaderPoiConfig());
        this.workbookInfo = workbookInfo;
        this.sheetName = sheetName;
        this.map = map;
        // headAndFieldDataList();
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Map<String, PluginMainOne<ConvertPlugin<T>>> map,
                                 Class<T> clazz, ReaderPoiConfig readerPoiConfig) {
        super(clazz, readerPoiConfig);
        this.workbookInfo = workbookInfo;
        this.sheetName = sheetName;
        this.map = map;
        // headAndFieldDataList();
    }

    private void createSheet() {
        if (!headState) {
            try {
                sheet = workbookInfo.createWorkbook().createSheet(sheetName);
            } catch (IOException e) {
                throw new NullPointerException(e.getMessage());
            }
            createHead(map, sheet, rownum);
            rownum++;
        }
        headState = true;
    }

    private void createHead(Map<String, PluginMainOne<ConvertPlugin<T>>> map, Sheet sheet, int rownum) {
        int i = 0;
        Iterator<Map.Entry<String, PluginMainOne<ConvertPlugin<T>>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, PluginMainOne<ConvertPlugin<T>>> next = it.next();
            PluginMainOne<ConvertPlugin<T>> value = next.getValue();
            ConvertPlugin cpm = value.getBody();
            if (cpm instanceof ConvertPluginList) {
                List<ValuePosition> v = ((ConvertPluginList) cpm).getList();
                for (ValuePosition valuePosition : v) {
                    Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
                    cell.setCellValue(valuePosition.getValue());
                    valuePosition.setPosition(Integer.valueOf(i));
                    i++;
                }
            } else {
                Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
                cell.setCellValue(cpm.getValue());
                cpm.setPosition(Integer.valueOf(i));
                i++;
            }
            if (cpm.getFormat() != null) {//创建样式Map
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(cpm.getFormat()));
                cellStyleMap.put(cpm.getFieldName(), cellStyle);
            }
        }
        return;
    }

//
//    public void addObject(Object object) {
//        if (!object.getClass().equals(getClazz())) {
//            throw new NullPointerException("[" + getClazz() + " ]to[ " + object.getClass() + "] is error");
//        }
//        @SuppressWarnings("unchecked")
//        T t = (T) object;
//        add(t);
//    }

    @Override
    public void add(T t) {
        createSheet();
        state = true;
        String str;
        int column = 0;
        Row row = SheetUtils.getCreateRow(sheet, rownum);
        Iterator<Map.Entry<String, PluginMainOne<ConvertPlugin<T>>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, PluginMainOne<ConvertPlugin<T>>> next = it.next();
            PluginMainOne<ConvertPlugin<T>> main = next.getValue();
            ConvertPlugin<T> tt = main.getBody();

            Object value = ReflectUtil.invoke(t, ReflectLcUtils.methodGetString(tt.getFieldName()));
            if (value == null) {
                column++;
                continue;
            }
            if (tt instanceof ConvertPluginList) {
                List<ValuePosition> v = ((ConvertPluginList<T>) tt).getList();
                if (value instanceof List) {
                    List values = (List) value;
                    for (ValuePosition valuePosition : v) {
                        Cell cell = SheetUtils.getCreateCell(row, column);
                        try {
                            putValue(values.get(valuePosition.getIndex()), cell, tt, main.getPlugins());
                        } catch (Exception e) {

                        }
                        column++;
                    }
                }
            } else {
                Cell cell = SheetUtils.getCreateCell(row, column);
                putValue(value, cell, tt, main.getPlugins());
                column++;
            }

        }
        rownum++;
    }

    private void putValue(Object value, Cell cell, ConvertPlugin<T> tt, List<String> list) {
        if (value == null)
            return;
        CellStyle cellStyle = cellStyleMap.get(tt.getFieldName());
        if (cellStyle != null)
            cell.setCellStyle(cellStyle);
        if (list != null) {
            int i = 0;
            int skip = 0;
            for (String plugin : list) {
                PluginAnnotation pluginMain = workbookInfo.getPlugins().get(plugin);
                if (pluginMain == null)
                    continue;
                i++;
                Annotation ann = tt.getField().getAnnotation(pluginMain.getAnnotation());
                if (!pluginMain.plugin(value, cell, ann))
                    skip++;
            }
            if (i != 0 && skip == i)//执行成功后将跳过默认转换器
                return;
        }
        defaultConvert(value, cell, tt);
    }


    private void defaultConvert(Object value, Cell cell, ConvertPlugin<T> tt) {
        String str;
        if (value.getClass().isEnum() && !tt.getEnumFieldName().equals("")) {
            str = ReflectUtil.invoke(value, ("get" + StrUtil.upperFirst(tt.getEnumFieldName())))
                    .toString();
            cell.setCellValue(str);
            return;
        }
        if (value.getClass().equals(Double.class) || value.getClass().equals(double.class)) {
            cell.setCellValue((Double) converterRegistry.convert(Double.class, value));
            return;
        } else if (value.getClass().equals(Date.class) || value.getClass().equals(Timestamp.class)) {
            cell.setCellValue((Date) converterRegistry.convert(Date.class, value));
            return;
        }
        str = converterRegistry.convert(String.class, value);
        cell.setCellValue(str);
    }


    @Override
    public void addAll(List<T> ts) {
        createSheet();
        for (T t : ts) {
            add(t);
            rownum++;
        }
    }

    @Override
    public void save(File file) throws IOException {
        FileOutputStream out = new FileOutputStream(file);
        sheet.getWorkbook().write(out);
        out.close();
    }

    @Override
    public void addEmptyLine() {
        if (rownum != 0)
            rownum++;
    }
}
