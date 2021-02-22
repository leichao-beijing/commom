package run.cmdi.common.reader.core;

import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ConvertOutPage;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.convert.ReaderPageInterface;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.exception.ConverterExceptionUtils;
import run.cmdi.common.reader.model.FindFieldInfo;
import run.cmdi.common.reader.model.FindFieldInfos;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.reader.model.eumns.FieldDetailType;

import java.util.*;

/**
 * 使用java对象内field从excel头文件中提取与java对象对应的内容
 *
 * @author leichao
 */
public class FindResource {
    private final ClazzBuildInfo clazzBuildInfo;

    public FindResource(ClazzBuildInfo clazzBuildInfo) {
        this.clazzBuildInfo = clazzBuildInfo;
    }

    public FindFieldInfos find(ReaderFactory readerFactory, int readHeadRownum) throws ConverterException {
        return find(readerFactory, clazzBuildInfo.getBookTagName(), readHeadRownum);
    }

    public FindFieldInfos find(ReaderFactory readerFactory, String name, int readHeadRownum) throws ConverterException {
        List<FindFieldInfos> list = new ArrayList<>();
        ReaderPageInterface page = readerFactory.buildAnalysis();
        FindFieldConfig config = clazzBuildInfo.getConfig();
        if (!name.equals(""))
            list.add(find(page.buildPage(name), readHeadRownum, config));
        else {
            List<ConvertOutPage> values = page.buildPageList();
            values.forEach(val -> list.add(find(val, readHeadRownum, config)));
        }
        if (list.size() == 0) {
            ConverterExceptionUtils.build(ConverterErrorType.NOT_FINDS_SHEET.getTypeName(), ConverterErrorType.NOT_FINDS_SHEET).throwException();
        }
        Collections.sort(list, new Comparator<FindFieldInfos>() {
            @Override
            public int compare(FindFieldInfos o1, FindFieldInfos o2) {
                if (o1.getMap().size() < o2.getMap().size()) {
                    return -1;
                }
                return 0;
            }
        });

        FindFieldInfos infos = list.remove(list.size() - 1);
        list.clear();
        int size = infos.getMap().size();
        if (size < clazzBuildInfo.getMaxWrongCount())
            ConverterExceptionUtils.build("最少匹配到" + clazzBuildInfo.getMaxWrongCount() + "列数据，当前匹配到了" + size, ConverterErrorType.FIND_FIELD_COUNT_WRONG).throwException();
        infos.setHeadInfo(clazzBuildInfo);
        return infos;
    }

    public FindFieldInfos find(ConvertOutPage<List> page, int readHeadRownum, FindFieldConfig config) {
        Map<Integer, FindFieldInfo> resultInfo = new HashMap<>();
        FindFieldInfos infos = new FindFieldInfos(resultInfo, page, clazzBuildInfo.getConfig(), readHeadRownum);
        List list = page.getValues(readHeadRownum);
        config.getMap().forEach((fieldName, info) -> {
            if (info.getType() != FieldDetailType.LIST)
                for (int i = 0; i < list.size(); i++) {
                     Object headValue = list.get(i);
                       if (info.match(headValue)) {
                        info.setAddress(i);
                        resultInfo.put(i, info);
                        break;
                    }
                }
            else {
                for (FindFieldInfo findFieldInfo : info.getList()) {
                    for (int i = 0; i < list.size(); i++) {
                        if (!findFieldInfo.match(list.get(i))) continue;
                        findFieldInfo.setAddress(i);
                        resultInfo.put(i, findFieldInfo);
                    }
                }
            }
        });
        return infos;
    }
}
