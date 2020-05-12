package run.cmid.common.excel.core;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import cn.hutool.core.convert.ConverterRegistry;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import run.cmid.common.excel.DataConvertInterface;
import run.cmid.common.excel.annotations.ExcelConverterHead;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.eumns.FieldDetailType;
import run.cmid.common.excel.model.to.ExcelHeadModel;
import run.cmid.common.excel.plugins.SheetUtils;

/**
 * @author leichao
 */

public class ConvertDataToWorkbook<T> extends ExcelBuildings<T> implements DataConvertInterface<T> {

    private final ConverterRegistry converterRegistry = ConverterRegistry.getInstance();
    private final ArrayList<String> headNames = new ArrayList<String>();
    private final WorkbookInfo workbookInfo;
    private final HashMap<String, CellStyle> cellStyleMap = new HashMap<String, CellStyle>();
    private int rownum = 0;
    private Sheet sheet;

    @Getter
    private boolean headState = false;
    @Getter
    private boolean state = false;

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, Class<T> clazz)
            throws IOException, ConverterExcelException {
        this(workbookInfo, null, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, Class<T> clazz) throws IOException {
        this(workbookInfo, sheetName, createFieldDetail(clazz), clazz);
    }

    public ConvertDataToWorkbook(WorkbookInfo workbookInfo, String sheetName, List<FieldDetail<T>> list,
                                 Class<T> clazz) {
        super(clazz);
        this.workbookInfo = workbookInfo;
        if (sheetName != null)
            setSheetName(sheetName);
        headAndFieldDataList();
    }

    private void createSheet() {
        if (!headState) {
            try {
                workbookInfo.createFile();
                sheet = workbookInfo.createWorkbook().createSheet(getSheetName());
            } catch (IOException e) {
                throw new NullPointerException(e.getMessage());
            }

            createHead();
            createCellStyle(sheet);
        }
        headState = true;
    }

    private void createCellStyle(Sheet sheet) {
        for (FieldDetail<T> tt : getList()) {
            if (tt.getJsonFormat() != null) {
                CellStyle cellStyle = sheet.getWorkbook().createCellStyle();
                cellStyle.setDataFormat(sheet.getWorkbook().createDataFormat().getFormat(tt.getJsonFormat().pattern()));
                cellStyleMap.put(tt.getFieldName(), cellStyle);
            }
        }
    }

    private static <T> List<FieldDetail<T>> createFieldDetail(Class<T> classes) {
        ExcelConverterHead head = classes.getAnnotation(ExcelConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ExcelConverterHead not enable");
        isIndexMethod(head.indexes(), classes);// 验证index内值是否存在于对象中。
        ExcelHeadModel headModel = new ExcelHeadModel(head);
        return ConverterFieldDetail.toList(classes, headModel);
    }

    /**
     * 分析FieldDetail<T> 获取 head和Field 名称List
     */
    private void headAndFieldDataList() {
        for (FieldDetail<T> t : getList()) {
            headNames.add(t.getMatchValue());
        }
    }

    private void createHead() {
        for (int i = 0; i < headNames.size(); i++) {
            Cell cell = SheetUtils.getCreateCell(sheet, rownum, i);
            cell.setCellValue(headNames.get(i));
        }
        rownum++;
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
        for (FieldDetail<T> tt : getList()) {
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
                List<String> list = (List<String>) value;
                try {
                    String string = list.get(tt.getIndex());
                    try {
                        cell.setCellValue((Double) converterRegistry.convert(Double.class, string));
                    } catch (NumberFormatException e) {
                        cell.setCellValue(string);
                    }
                } catch (IndexOutOfBoundsException e) {
                    // TODO: handle exception
                }
                column++;
                continue;
            }
            if (value.getClass().isEnum() && !tt.getEnumTypeNameFiledValue().equals("")) {
                str = ReflectUtil.invoke(value, ("get" + StrUtil.upperFirst(tt.getEnumTypeNameFiledValue())))
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
