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
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.reader.model.FieldDetail;
import run.cmdi.common.reader.model.to.ExcelHeadModel;
import run.cmdi.common.reader.pligins.ConvertPluginModel;
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

public class ConvertDataToWorkbook<T, PAGE, UNIT> extends EntityBuildings<T, PAGE, UNIT> implements DataConvertInterface<T> {

    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    //private final ArrayList<String> headNames = new ArrayList<String>();
    private final WorkbookInfo workbookInfo;
    private final HashMap<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
    private int rownum = 0;
    private Sheet sheet;
    @Getter
    private final Map<String, PluginMainOne<ConvertPluginModel<T>>> map;
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
        this(workbookInfo, null, ConvertPluginModel.buildMap(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Class<T> clazz) {
        this(workbookInfo, sheetName, ConvertPluginModel.buildMap(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Map<String, PluginMainOne<ConvertPluginModel<T>>> map,
                                 Class<T> clazz) {
        super(clazz);
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

//    private static <T> Map<String, FieldDetail> createFieldDetail(Class<T> classes) {
//        ConverterHead head = classes.getAnnotation(ConverterHead.class);
//        if (head == null)
//            throw new NullPointerException("@ConverterHead not enable");
//        isIndexMethod(head.indexes(), classes);// 验证index内值是否存在于对象中。
//        ExcelHeadModel headModel = new ExcelHeadModel(head);
//        return ConverterFieldDetail.toMap(classes, headModel, null);
//    }

    private void createHead(Map<String, PluginMainOne<ConvertPluginModel<T>>> map, Sheet sheet, int rownum) {
        int i = 0;
        Iterator<Map.Entry<String, PluginMainOne<ConvertPluginModel<T>>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, PluginMainOne<ConvertPluginModel<T>>> next = it.next();
            PluginMainOne<ConvertPluginModel<T>> value = next.getValue();

            ConvertPluginModel cpm = value.getBody();

            Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
            cell.setCellValue(cpm.getValue());
            cpm.setPosition(Integer.valueOf(i));

            if (cpm.getFormat() != null) {//创建样式Map
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(cpm.getFormat()));
                cellStyleMap.put(cpm.getFieldName(), cellStyle);
            }
            i++;
        }
        return;
    }

    public void addObject(Object object) {
        if (!object.getClass().equals(getClazz())) {
            throw new NullPointerException("[" + getClazz() + " ]to[ " + object.getClass() + "] is error");
        }
        @SuppressWarnings("unchecked")
        T t = (T) object;
        add(t);
    }

    @Override
    public void add(T t) {
        createSheet();
        state = true;
        String str;
        int column = 0;
        Row row = SheetUtils.getCreateRow(sheet, rownum);
        Iterator<Map.Entry<String, PluginMainOne<ConvertPluginModel<T>>>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, PluginMainOne<ConvertPluginModel<T>>> next = it.next();
            PluginMainOne<ConvertPluginModel<T>> main = next.getValue();
            ConvertPluginModel<T> tt = main.getBody();
            Object value = ReflectUtil.invoke(t, ReflectLcUtils.methodGetString(tt.getFieldName()));
            if (value == null) {
                column++;
                continue;
            }
            Cell cell = SheetUtils.getCreateCell(row, column);
            if (main.getPlugins() != null) {
                boolean state = false;
                for (String plugin : main.getPlugins()) {
                    PluginAnnotation pluginMain = workbookInfo.getPlugins().get(plugin);
                    if(pluginMain==null)
                        continue;
                    Annotation ann = tt.getField().getAnnotation(pluginMain.getAnnotation());
                    pluginMain.plugin(value,cell,ann);
                }
                if (state)
                    continue;
            }

            CellStyle cellStyle = cellStyleMap.get(tt.getFieldName());
            if (cellStyle != null)
                cell.setCellStyle(cellStyle);

            if (value.getClass().isEnum() && !tt.getEnumFieldName().equals("")) {
                str = ReflectUtil.invoke(value, ("get" + StrUtil.upperFirst(tt.getEnumFieldName())))
                        .toString();
                cell.setCellValue(str);
                column++;
                continue;
            }

            if (value.getClass().equals(Double.class) || value.getClass().equals(double.class)) {
                cell.setCellValue((Double) converterRegistry.convert(Double.class, value));
                column++;
                continue;
            } else if (value.getClass().equals(Date.class) || value.getClass().equals(Timestamp.class)) {
                cell.setCellValue((Date) converterRegistry.convert(Date.class, value));
                column++;
                continue;
            }
            str = converterRegistry.convert(String.class, value);
            cell.setCellValue(str);
            column++;
        }

        rownum++;
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
