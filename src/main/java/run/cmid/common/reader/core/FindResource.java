package run.cmid.common.reader.core;

import run.cmid.common.compare.Compares;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTag;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.exception.ConverterExcelConfigException;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.exception.ConverterException;
import run.cmid.common.reader.exception.ConverterExceptionUtils;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.entity.CompareResponseAndErrorList;
import run.cmid.common.reader.model.eumns.*;
import run.cmid.common.reader.model.to.ExcelHeadModel;

import java.util.*;

/**
 * 使用java对象内field从excel头文件中提取与java对象对应的内容
 *
 * @author leichao
 */
public class FindResource<RESOURCES, PAGE, UNIT> {
    private EntityBuildings entityBuildings;
    //private final Map<String, FieldDetail> map;
    private int readHeadRownum = 0;
    private final ExcelHeadModel headModel;
    /**
     * 系统允许最大的无法找到列的数量，超出该数量时将抛出异常
     */
    private final int wrongCount;

    public FindResource(EntityBuildings entityBuildings, ExcelHeadModel headModel) {
        this.entityBuildings = entityBuildings;
        this.wrongCount = headModel.getMaxWrongCount();
        this.headModel = headModel;
    }

    /**
     * @param entityBuildings
     * @param readHeadRownum  获取匹配头文件的行数，默认值 0 行。
     */
    public FindResource(EntityBuildings entityBuildings, ExcelHeadModel headModel, int readHeadRownum) {
        this(entityBuildings, headModel);
        this.readHeadRownum = readHeadRownum;
    }

    public HeadInfo find(BookPage<RESOURCES, PAGE, UNIT> bookResources) throws ConverterException {
        return find(bookResources, headModel.getBookTagName());
    }


    @SuppressWarnings("rawtypes")
    public HeadInfo find(BookPage<RESOURCES, PAGE, UNIT> bookResources, String name) throws ConverterException {
        List<HeadInfo> list = new ArrayList<HeadInfo>();
        @SuppressWarnings("unchecked")
        List<ReaderPage<PAGE, UNIT>> resource = bookResources.bookList();
        if (!name.equals(""))
            list.add(find(bookResources.book(name)));
        else
            for (ReaderPage readerPage : resource) {
                try {
                    list.add(find(readerPage));
                } catch (Exception e) {
                    e.printStackTrace();
                    //ConverterExcelException
                }
            }
        Collections.sort(list);
        if (list.size() == 0) {
            ConverterExceptionUtils.build(ConverterErrorType.NOT_FINDS_SHEET.getTypeName(), ConverterErrorType.NOT_FINDS_SHEET).throwException();
        }
        HeadInfo model = list.get(list.size() - 1);
        if (model.getEx() != null) {
            throw model.getEx();
        }
        int size = model.getSize();
        if (size < wrongCount)
            ConverterExceptionUtils.build("最少匹配到" + wrongCount + "列数据，当前匹配到了" + size, ConverterErrorType.FIND_FIELD_COUNT_WRONG).throwException();
        model.getReaderPage().info(model);
        return model;
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public HeadInfo<PAGE, UNIT> find(ReaderPage<PAGE, UNIT> readerPage) {
        Map map = entityBuildings.converterFieldDetailToMap();
        try {
            List tmp = readerPage.readRowList(readHeadRownum);
            matchCompare(map, tmp);//TODO 头信息匹配
            return new HeadInfo(map, readerPage);
        } catch (ConverterException e) {
            return new HeadInfo(e);
        }
    }

    private static boolean matchCompare(FieldDetail detail, List<Object> des) {
        if (detail.getModel() == FindModel.EQUALS) {
            for (String valueValue : detail.getValues()) {
                for (int i = 0; i < des.size(); i++) {
                    if (des.get(i) == null)
                        continue;
                    boolean bool = valueValue.equals(des.get(i).toString());
                    if (bool) {//TODO setPosition
                        detail.setPosition(i);
                        detail.setMatchValue(des.get(i).toString());
                        return true;
                    }
                }
            }
        }
        if (detail.getModel() == FindModel.INCLUDE) {
            for (String valueValue : detail.getValues()) {
                for (int i = 0; i < des.size(); i++) {
                    boolean bool = (des.get(i).toString().indexOf(valueValue) != -1);
                    if (bool) {
                        detail.setPosition(i);
                        detail.setMatchValue(des.get(i).toString());
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 对数据进行匹配
     */
    public static void matchCompare(
            Map<String, FieldDetail> map, List<Object> des) throws ConverterException {
        List<String> nullList = new ArrayList<>();
        map.forEach((key, value) -> {
            if (value.getType() == FieldDetailType.LIST)
                value.getOtherDetails().forEach((va) -> {
                    matchCompare(va, des);
                });
            else if (!matchCompare(value, des))
                nullList.add(key);
        });
        ConverterExceptionUtils utilsException = ConverterExceptionUtils.build();

        nullList.forEach((key) -> {
            FieldDetail f = map.get(key);
            if (f.isCheckColumn())
                utilsException.addError(f.getName(), ConverterErrorType.NOT_FIND_CHECK_COLUMN);
            map.remove(key);
        });

        utilsException.throwException();
        List<Map.Entry<String, FieldDetail>> list = new ArrayList<Map.Entry<String, FieldDetail>>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<String, FieldDetail>>() {
            //升序排序
            public int compare(Map.Entry<String, FieldDetail> o1,
                               Map.Entry<String, FieldDetail> o2) {
                return o1.getValue().getPosition().compareTo(o2.getValue().getPosition());
            }
        });
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
