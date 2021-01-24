package run.cmdi.common.reader.core;

import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ConvertOutPage;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.convert.ReaderPageInterface;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.exception.ConverterExceptionUtils;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;

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

    public FieldInfos find(ReaderFactory readerFactory, int readHeadRownum) throws ConverterException {
        return find(readerFactory, clazzBuildInfo.getBookTagName(), readHeadRownum);
    }

    public FieldInfos find(ReaderFactory readerFactory, String name, int readHeadRownum) throws ConverterException {
        List<FieldInfos> list = new ArrayList<>();
        ReaderPageInterface page = readerFactory.buildAnalysis();
        List<FieldInfo> infoList = clazzBuildInfo.getConfig().getList();
        if (!name.equals(""))
            list.add(find(page.buildPage(name), readHeadRownum, infoList));
        else {
            List<ConvertOutPage> values = page.buildPageList();
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

        FieldInfos infos = list.remove(list.size() - 1);
        list.clear();
//        if (infos.getException() != null) {
//            throw infos.getException();
//        }
        int size = infos.getMap().size();
        if (size < clazzBuildInfo.getMaxWrongCount())
            ConverterExceptionUtils.build("最少匹配到" + clazzBuildInfo.getMaxWrongCount() + "列数据，当前匹配到了" + size, ConverterErrorType.FIND_FIELD_COUNT_WRONG).throwException();
        infos.setHeadInfo(clazzBuildInfo);
        return infos;
    }

    public FieldInfos find(ConvertOutPage<List> page, int readHeadRownum, List<FieldInfo> infoList) {
        Map<Integer, FieldInfo> resultInfo = new HashMap<>();
        FieldInfos infos = new FieldInfos(resultInfo, page, clazzBuildInfo.getConfig(), readHeadRownum);

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
