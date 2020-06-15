package run.cmid.common.reader.core;

import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.entity.CompareResponseAndErrorList;
import run.cmid.common.reader.model.eumns.ConfigError;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;
import run.cmid.common.reader.model.to.ExcelHeadModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 使用java对象内field从excel头文件中提取与java对象对应的内容
 *
 * @author leichao
 */
public class FindResource<RESOURCES, PAGE, UNIT> {

    private final List<LocationTag<FieldDetail>> findList;
    private int readHeadRownum = 0;
    private final ExcelHeadModel headModel;
    /**
     * 系统允许最大的无法找到列的数量，超出该数量时将抛出异常
     */
    private final int wrongCount;

    public FindResource(List<FieldDetail> findList, ExcelHeadModel headModel) {
        this.findList = toLocationTag(findList);
        this.wrongCount = headModel.getMaxWrongCount();
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

    public HeadInfo find(BookPage<RESOURCES, PAGE, UNIT> bookResources) throws ConverterExcelException {
        return find(bookResources, headModel.getBookTagName());
    }


    @SuppressWarnings("rawtypes")
    public HeadInfo find(BookPage<RESOURCES, PAGE, UNIT> bookResources, String name) throws ConverterExcelException {
        List<HeadInfo> list = new ArrayList<HeadInfo>();
        @SuppressWarnings("unchecked")
        List<ReaderPage<PAGE, UNIT>> resource = bookResources.bookList();
        //List<LocationTag<String>> vales = null;
        //List<Object> tmp = null;
        if (!name.equals(""))
            list.add(find(bookResources.book(name)));
        else
            for (ReaderPage readerPage : resource) {
                try {
                    list.add(find(readerPage));
                } catch ( Exception e) {
                    e.printStackTrace();
                    //ConverterExcelException
                }
            }
        Collections.sort(list);
        if (list.size() == 0) {
            throw new ConverterExcelException(ConverterErrorType.NOT_FINDS_SHEET);
        }
        HeadInfo model = list.get(list.size() - 1);
        if (model.getEx() != null) {
            throw model.getEx();
        }
        int size = model.getResponse().getList().size();
        if (size < wrongCount)
            throw new ConverterExcelException(ConverterErrorType.FIND_FIELD_COUNT_WRONG)
                    .setMessage("最少匹配到" + wrongCount + "列数据，当前匹配到了" + size);
        model.getReaderPage().info(model);
        return model;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HeadInfo<PAGE, UNIT> find(ReaderPage<PAGE, UNIT> readerPage) throws ConverterExcelException {
        try {
            List<LocationTag<String>> vales = new ArrayList<>();
            List tmp = readerPage.readRowList(readHeadRownum);
            if (tmp == null)
                throw new ConverterExcelException(ConverterErrorType.HEAD_IS_EMPTY, "读取行号：" + readHeadRownum);
            for (int i = 0; i < tmp.size(); i++) {
                vales.add(new LocationTag(i, (tmp.get(i)==null)?"":tmp.get(i).toString()));
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
                    if (e.getValue().getValue().isCheckColumn()) {
                        return new ConverterExcelException(ConverterErrorType.NOT_FIND_CHECK_COLUMN)
                                .setMessage(e.getValue().getValue().getValues().toString());
                    }
                    return null;
                });
        return list;
    }

    private static Boolean getBoolean(LocationTag<String> d, FieldDetail detail) {
        CompareResponse<String, String> response = null;
        if (detail.getModel() == FindModel.EQUALS ) {
            response = Compares.toList(d.getValue(), d.getPosition().intValue(), detail.getValues(),
                    (tag, value) -> {
                        if (d.getValue().equals(value)) {
                            detail.setColumn(d.getPosition());
                            detail.setMatchValue(value);
                            return true;
                        }
                        return false;
                    });
        }
        if (detail.getModel() == FindModel.INCLUDE) {
            response = Compares.toList(d.getValue(), d.getPosition().intValue(), detail.getValues(),
                    (tag, value) -> {
                        if (d.getValue().indexOf(value) != -1) {
                            detail.setColumn(d.getPosition());
                            detail.setMatchValue(d.getValue());
                            return true;
                        }
                        return false;
                    });
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
