package run.cmdi.common.reader.core;

import lombok.Getter;
import run.cmdi.common.convert.ClazzBuildInfo;
import run.cmdi.common.convert.ReaderFactory;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.FindFieldInfos;

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
        return new EntityResultBuildConvert(clazz, filedInfos);
    }
}
