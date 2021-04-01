package run.cmdi.common.reader.core;

import lombok.Getter;
import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.FindFieldInfos;
import run.cmdi.common.validator.exception.ValidatorHeadsException;

import java.util.ArrayList;
import java.util.List;

/**
 * 通过对象class检索Excel内对应sheet
 *
 * @author leichao
 */
@Getter
public class EntityBuildings<T> {
    @Getter
    private final Class<T> clazz;
    @Getter
    private final ReaderPoiConfig readerPoiConfig;
    @Getter
    private final ClazzBuildInfo clazzBuildInfo;

    public EntityBuildings(Class<T> clazz) {
        this(clazz, new ReaderPoiConfig());
    }

    /**
     * @param clazz
     */
    public EntityBuildings(Class<T> clazz, ReaderPoiConfig readerPoiConfig) {
        this.clazz = clazz;
        this.readerPoiConfig = readerPoiConfig;
        this.clazzBuildInfo = ClazzBuildInfo.build(clazz);
    }

    public EntityResultBuildConvert find(ReaderFactory resource, int readHeadRownum)
            throws ConverterException {
        FindFieldInfos filedInfos = new FindResource(clazzBuildInfo).find(resource, readHeadRownum);
        List<List<String>> list = new ArrayList<>();
        filedInfos.getConfig().getMap().forEach((index, info) -> {
            if (info.getAddress() == null && info.isCheckColumn()) {
                list.add(info.getValues());
            }
        });

        if (!list.isEmpty())
            throw new ValidatorHeadsException(list);
        return new EntityResultBuildConvert(clazz, filedInfos);
    }
}
