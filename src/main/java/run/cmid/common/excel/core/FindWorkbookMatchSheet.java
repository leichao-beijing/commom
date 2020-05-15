package run.cmid.common.excel.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.excel.exception.ConverterExcelConfigException;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.FieldDetail;
import run.cmid.common.excel.model.SheetModel;
import run.cmid.common.excel.model.entity.CompareResponseAndErrorList;
import run.cmid.common.excel.model.eumns.ConfigErrorType;
import run.cmid.common.excel.model.eumns.ExcelExceptionType;
import run.cmid.common.excel.model.eumns.ExcelReadType;
import run.cmid.common.excel.model.to.ExcelHeadModel;

/**
 * 使用java对象内fiel从excel头文件中提取与java对象对应的内容
 * 
 * @author leichao
 */
public class FindWorkbookMatchSheet<T> {

    private final List<LocationTag<FieldDetail<T>>> findList;
    private int readHeadRownum = 0;

    /**
     * 允许系统内无法找到列的最大数量，超出该数量时将抛出异常
     */
    private final int wrongCount;

    public FindWorkbookMatchSheet(List<FieldDetail<T>> findList, ExcelHeadModel headModel) {
        this.findList = toLocationTag(findList);
        this.wrongCount = headModel.getMaxWorngCount();
    }

    /**
     * @param findList       ConverterFieldDetail.toList(classes.class)
     * @param readHeadRownum 获取匹配头文件的行数，默认值 0 行。
     */
    public FindWorkbookMatchSheet(List<FieldDetail<T>> findList, ExcelHeadModel headModel, int readHeadRownum) {
        this.findList = toLocationTag(findList);
        this.readHeadRownum = readHeadRownum;
        wrongCount = headModel.getMaxWorngCount();
    }

    private SheetModel<T> find(Sheet sheet) {
        try {
            CompareResponseAndErrorList<FieldDetail<T>, String, ConverterExcelException> response = matchSheetCount(
                    sheet);
            return new SheetModel<T>(response, sheet);
        } catch (ConverterExcelException e) {
            return new SheetModel<T>(e);
        }
    }

    /**
     * @param workbook
     * @param sheetName 等于""时，忽略。反之直接通过sheetName读取sheet
     * @exception ConverterExcelException
     */
    public SheetModel<T> find(Workbook workbook, String sheetName) throws ConverterExcelException {
        List<SheetModel<T>> list = new ArrayList<SheetModel<T>>();
        if (sheetName.equals("")) {
            Iterator<Sheet> it = workbook.sheetIterator();
            while (it.hasNext()) {
                Sheet sheet = it.next();
                list.add(find(sheet));
            }
        } else {
            Sheet sheet = workbook.getSheet(sheetName);
            if (sheet == null)
                throw new ConverterExcelException(ExcelExceptionType.SHEET_NAME_NO_EXISTS);
            list.add(find(sheet));
        }
        Collections.sort(list);
        if (list.size() == 0) {
            throw new ConverterExcelException(ExcelExceptionType.NOT_FINDS_SHEET);
        }
        SheetModel<T> model = list.get(list.size() - 1);
        if (model.getEx() != null) {
            throw model.getEx();
        }
        int size = model.getResponse().getList().size();
        if (model.getResponse().getList().size() < wrongCount)
            throw new ConverterExcelException(ExcelExceptionType.FIND_FIELD_COUNT_WRONG)
                    .setMessage("最少匹配到" + wrongCount + "列数据，当前匹配到了" + size);
        return model;
    }

    private CompareResponseAndErrorList<FieldDetail<T>, String, ConverterExcelException> matchSheetCount(Sheet sheet)
            throws ConverterExcelException {
        List<LocationTag<String>> list = readSheetHead(readHeadRownum, sheet);
        return matchCompare(findList, list);
    }

    /**
     * 对数据进行匹配
     */
    public static <T> CompareResponseAndErrorList<FieldDetail<T>, String, ConverterExcelException> matchCompare(
            List<LocationTag<FieldDetail<T>>> src, List<LocationTag<String>> des) throws ConverterExcelException {
        CompareResponseAndErrorList<FieldDetail<T>, String, ConverterExcelException> list = Compares
                .toListsLocationTagToErrorList(src, des, (s, d) -> {
                    CompareResponse<String, String> response = null;
                    FieldDetail<T> detail = s.getValue();
                    if (detail.getModel() == ExcelReadType.EQUALS) {
                        response = Compares.toList(d.getValue(), d.getRowmun().intValue(), detail.getValues(),
                                (tag, value) -> {
                                    if (d.getValue().equals(value)) {
                                        return true;
                                    }
                                    return false;
                                });
                    }
                    if (detail.getModel() == ExcelReadType.INCLUDE) {
                        response = Compares.toList(d.getValue(), d.getRowmun().intValue(), detail.getValues(),
                                (tag, value) -> {
                                    if (d.getValue().indexOf(value) != -1) {
                                        return true;
                                    }
                                    return false;
                                });
                    }
                    if (detail.getModel() == ExcelReadType.NO_EQUALS || detail.getModel() == ExcelReadType.NO_INCLUDE) {
                      throw  new ConverterExcelConfigException(ConfigErrorType.NO_SUPPORT_ENUM_YYPE).setMessage("SUPPORT ExcelReadType.EQUALS or ExcelReadType.INCLUDE");
                    }
                    if (response != null) {
                        return true;
                    }
                    return false;
                }, (e) -> {
                    if (e.getValue().getValue().isNullCheck()) {
                        return new ConverterExcelException(ExcelExceptionType.NOT_FIND_CHECK_COLUMN)
                                .setMessage(e.getValue().getValue().getValues().toString());
                    }
                    return null;
                });
        return list;
    }

    private List<LocationTag<String>> readSheetHead(int readHeadRownum, Sheet sheet) throws ConverterExcelException {
        Row row = sheet.getRow(readHeadRownum);
        if (row == null)
            throw new ConverterExcelException(ExcelExceptionType.READER_HEAD_EMPTY);
        Iterator<Cell> it = row.iterator();
        ArrayList<LocationTag<String>> list = new ArrayList<LocationTag<String>>();
        while (it.hasNext()) {
            Cell cell = it.next();
            list.add(new LocationTag<String>(cell.getAddress().getColumn(), cell.toString().trim()));
        }
        return list;
    }

    /**
     * 将 List<FieldDetail<T>> 重构成带坐标的List<LocationTag<FieldDetail<T>>> 数据
     */
    private List<LocationTag<FieldDetail<T>>> toLocationTag(List<FieldDetail<T>> findList) {
        List<LocationTag<FieldDetail<T>>> list = new ArrayList<LocationTag<FieldDetail<T>>>();
        for (int i = 0; i < findList.size(); i++) {
            list.add(new LocationTag<FieldDetail<T>>(i, findList.get(i)));
        }
        return list;
    }
}
