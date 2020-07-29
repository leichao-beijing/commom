package run.cmdi.common.reader.core;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.model.FieldDetail;
import run.cmdi.common.reader.model.eumns.FieldDetailType;
import run.cmdi.common.reader.model.to.ExcelHeadModel;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.poi.core.SheetUtils;
import run.cmdi.common.reader.exception.ConverterExcelException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
    private final Map<String, FieldDetail> map;
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
        this(workbookInfo, null, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Class<T> clazz) {
        this(workbookInfo, sheetName, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Map<String, FieldDetail> map,
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

    private static <T> Map<String, FieldDetail> createFieldDetail(Class<T> classes) {
        ConverterHead head = classes.getAnnotation(ConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ConverterHead not enable");
        isIndexMethod(head.indexes(), classes);// 验证index内值是否存在于对象中。
        ExcelHeadModel headModel = new ExcelHeadModel(head);
        return ConverterFieldDetail.toMap(classes, headModel, null);
    }

    private void createHead(Map<String, FieldDetail> map, Sheet sheet, int rownum) {
        int i = 0;
        Iterator<Map.Entry<String, FieldDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, FieldDetail> next = it.next();
            FieldDetail value = next.getValue();
            Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
            cell.setCellValue(value.getName());
            value.setPosition(Integer.valueOf(i));

            if (value.getFormat() != null) {//创建样式Map
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(value.getFormat().value()));
                cellStyleMap.put(value.getFieldName(), cellStyle);
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
        String str = null;
        int column = 0;
        Row row = SheetUtils.getCreateRow(sheet, rownum);
        Iterator<Map.Entry<String, FieldDetail>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, FieldDetail> next = it.next();
            FieldDetail tt = next.getValue();
            Object value = ReflectUtil.invoke(t, ("get" + StrUtil.upperFirst(tt.getFieldName())));
            if (value == null) {
                column++;
                continue;
            }
            Cell cell = SheetUtils.getCreateCell(row, column);
            CellStyle cellStyle = cellStyleMap.get(tt.getFieldName());
            if (cellStyle != null)
                cell.setCellStyle(cellStyle);
            if (tt.getType() == FieldDetailType.LIST) {
                @SuppressWarnings("unchecked")
                List<Object> list = (List<Object>) value;
                try {
                    Object string = list.get(tt.getIndex());
                    if (StringUtils.isEmpty(string)) {
                        column++;
                        continue;
                    }
                    try {
                        cell.setCellValue((Double) converterRegistry.convert(Double.class, string));
                    } catch (NumberFormatException e) {
                        cell.setCellValue(string.toString());
                    }
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                }
                column++;
                continue;
            }
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
            }
            if (value.getClass().equals(Date.class) || value.getClass().equals(Timestamp.class)) {

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
