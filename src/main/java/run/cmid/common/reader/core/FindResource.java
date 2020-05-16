package run.cmid.common.reader.core;

import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.entity.CompareResponseAndErrorList;
import run.cmid.common.reader.model.eumns.ConfigErrorType;
import run.cmid.common.reader.model.eumns.ExcelExceptionType;
import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.reader.model.to.ExcelHeadModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用java对象内fiel从excel头文件中提取与java对象对应的内容
 *
 * @author leichao
 */
public class FindResource<T> {

    private final List<LocationTag<FieldDetail>> findList;
    private int readHeadRownum = 0;
    private final ExcelHeadModel headModel;
    /**
     * 系统允许最大的无法找到列的数量，超出该数量时将抛出异常
     */
    private final int wrongCount;

    public FindResource(List<FieldDetail> findList, ExcelHeadModel headModel) {
        this.findList = toLocationTag(findList);
        this.wrongCount = headModel.getMaxWorngCount();
        this.headModel = headModel;
    }

    /**
     * @param findList       ConverterFieldDetail.toList(classes.class)
     * @param readHeadRownum 获取匹配头文件的行数，默认值 0 行。
     */
    public FindResource(List<FieldDetail> findList, ExcelHeadModel headModel, int readHeadRownum) {
        this(findList, headModel);
        this.readHeadRownum = readHeadRownum;
    }

    public HeadInfo find(BookResources bookResources) throws ConverterExcelException {
        return find(bookResources, headModel.getBookTagName());
    }


    public HeadInfo find(BookResources bookResources, String name) throws ConverterExcelException {
        List<HeadInfo> list = new ArrayList<HeadInfo>();
        List<ReaderPage> resource = bookResources.bookList();
        List<LocationTag<String>> vales = null;
        List<Object> tmp = null;
        if (!name.equals(""))
            list.add(find(bookResources.book(name)));
        else
            for (ReaderPage readerPage : resource) {
                list.add(find(readerPage));
            }
        Collections.sort(list);
        if (list.size() == 0) {
            throw new ConverterExcelException(ExcelExceptionType.NOT_FINDS_SHEET);
        }
        HeadInfo model = list.get(list.size() - 1);
        if (model.getEx() != null) {
            throw model.getEx();
        }
        int size = model.getResponse().getList().size();
        if (size < wrongCount)
            throw new ConverterExcelException(ExcelExceptionType.FIND_FIELD_COUNT_WRONG)
                    .setMessage("最少匹配到" + wrongCount + "列数据，当前匹配到了" + size);
        model.getReaderPage().info(model);
        return model;
    }

    public HeadInfo find(ReaderPage readerPage) throws ConverterExcelException {
        try {
            List<LocationTag<String>> vales = new ArrayList<>();
            List tmp = readerPage.readRowList(readHeadRownum);
            if(tmp==null)
                throw new ConverterExcelException(ExcelExceptionType.HEAD_IS_EMPTY,"读取行号："+readHeadRownum);
            for (int i = 0; i < tmp.size(); i++) {
                vales.add(new LocationTag(i, tmp.get(i).toString()));
            }
            CompareResponseAndErrorList<FieldDetail, String, ConverterExcelException> response = matchCompare(findList, vales);
            return new HeadInfo(response, readerPage);
        } catch (ConverterExcelException e) {
            return new HeadInfo(e);
        }
    }


    /**
     * 对数据进行匹配
     */
    public static CompareResponseAndErrorList<FieldDetail, String, ConverterExcelException> matchCompare(
            List<LocationTag<FieldDetail>> src, List<LocationTag<String>> des) throws ConverterExcelException {
        CompareResponseAndErrorList<FieldDetail, String, ConverterExcelException> list = Compares
                .toListsLocationTagToErrorList(src, des, (s, d) -> {
                    FieldDetail detail = s.getValue();
                    return getBoolean(d, detail);
                }, (e) -> {
                    if (e.getValue().getValue().isNullCheck()) {
                        return new ConverterExcelException(ExcelExceptionType.NOT_FIND_CHECK_COLUMN)
                                .setMessage(e.getValue().getValue().getValues().toString());
                    }
                    return null;
                });
        return list;
    }

    private static Boolean getBoolean(LocationTag<String> d, FieldDetail detail) {
        CompareResponse<String, String> response = null;
        if (detail.getModel() == ExcelReadType.EQUALS) {
            response = Compares.toList(d.getValue(), d.getColumn().intValue(), detail.getValues(),
                    (tag, value) -> {
                        if (d.getValue().equals(value)) {
                            detail.setColumn(d.getColumn());
                            return true;
                        }
                        return false;
                    });
        }
        if (detail.getModel() == ExcelReadType.INCLUDE) {
            response = Compares.toList(d.getValue(), d.getColumn().intValue(), detail.getValues(),
                    (tag, value) -> {
                        if (d.getValue().indexOf(value) != -1) {
                            detail.setColumn(d.getColumn());
                            return true;
                        }
                        return false;
                    });
        }
        if (detail.getModel() == ExcelReadType.NO_EQUALS || detail.getModel() == ExcelReadType.NO_INCLUDE) {
            throw new ConverterExcelConfigException(ConfigErrorType.NO_SUPPORT_ENUM_YYPE).setMessage("SUPPORT ExcelReadType.EQUALS or ExcelReadType.INCLUDE");
        }
        if (response != null) {
            return true;
        }
        return false;
    }


    /**
     * 将 List<FieldDetail<T>> 重构成带坐标的List<LocationTag<FieldDetail<T>>> 数据
     */
    private List<LocationTag<FieldDetail>> toLocationTag(List<FieldDetail> findList) {
        List<LocationTag<FieldDetail>> list = new ArrayList<>();
        for (int i = 0; i < findList.size(); i++) {
            list.add(new LocationTag<FieldDetail>(i, findList.get(i)));
        }
        return list;
    }
}
