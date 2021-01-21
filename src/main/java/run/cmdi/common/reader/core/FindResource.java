package run.cmdi.common.reader.core;

import run.cmdi.common.convert.ConvertPage;
import run.cmdi.common.convert.InputStreamConvert;
import run.cmdi.common.convert.TypeAnalysis;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.exception.ConverterExceptionUtils;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.to.ExcelHeadModel;

import java.util.*;

/**
 * 使用java对象内field从excel头文件中提取与java对象对应的内容
 *
 * @author leichao
 */
public class FindResource<RESOURCES, PAGE, UNIT> {
    private EntityBuildings entityBuildings;
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

    public FieldInfos find(InputStreamConvert inputStreamConvert) throws ConverterException {
        return find(inputStreamConvert, headModel.getBookTagName());
    }

    public FieldInfos find(InputStreamConvert inputStreamConvert, String name) throws ConverterException {
        List<FieldInfos> list = new ArrayList<>();
        TypeAnalysis page = inputStreamConvert.buildAnalysis();
        List<FieldInfo> infoList = entityBuildings.getClazzConvert().getConfig().getList();
        if (!name.equals(""))
            list.add(find(page.buildPage(name), readHeadRownum, infoList));
        else {
            List<ConvertPage> values = page.buildPageList();
            values.forEach(val -> list.add(find(val, readHeadRownum, infoList)));
        }
        if (list.size() == 0) {
            ConverterExceptionUtils.build(ConverterErrorType.NOT_FINDS_SHEET.getTypeName(), ConverterErrorType.NOT_FINDS_SHEET).throwException();
        }
        Collections.sort(list, new Comparator<FieldInfos>() {
            @Override
            public int compare(FieldInfos o1, FieldInfos o2) {
                if (o1.getMap().size() < o2.getMap().size()) {
                    return -1;
                }
                return 0;
            }
        });
        FieldInfos infos = list.get(list.size() - 1);
        if (infos.getException() != null) {
            throw infos.getException();
        }
        int size = infos.getMap().size();
        if (size < wrongCount)
            ConverterExceptionUtils.build("最少匹配到" + wrongCount + "列数据，当前匹配到了" + size, ConverterErrorType.FIND_FIELD_COUNT_WRONG).throwException();
        return infos;
    }

    public FieldInfos find(ConvertPage page, int readHeadRownum, List<FieldInfo> infoList) {
        Map<Integer, FieldInfo> resultInfo = new HashMap<>();
        FieldInfos infos = new FieldInfos(resultInfo, page, entityBuildings.getClazzConvert().getConfig(), readHeadRownum);
        List list = page.getValues(readHeadRownum);
        for (int i = 0; i < list.size(); i++) {
            Object val = list.get(i);
            if (val == null)
                continue;
            Integer index = Integer.valueOf(i);
            infoList.forEach((info) -> {
                if (info.isState())
                    return;
                if (info.match(val.toString())) {
                    info.setState(true);
                    info.setIndex(index);
                    resultInfo.put(index, info);
                }
            });
        }
        return infos;
    }
}
