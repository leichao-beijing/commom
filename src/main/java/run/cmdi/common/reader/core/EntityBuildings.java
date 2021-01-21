package run.cmdi.common.reader.core;

import lombok.Getter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import run.cmdi.common.convert.ClazzConvert;
import run.cmdi.common.convert.InputStreamConvert;
import run.cmdi.common.poi.model.ReaderPoiConfig;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.exception.ConverterException;
import run.cmdi.common.reader.model.to.ExcelHeadModel;

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
    private final ExcelHeadModel headModel;
    @Getter
    private final ReaderPoiConfig readerPoiConfig;
    @Getter
    private final ClazzConvert clazzConvert;

    /**
     * @param clazz
     */
    public EntityBuildings(Class<T> clazz, ReaderPoiConfig readerPoiConfig) {
        this.clazz = clazz;
        ConverterHead head = clazz.getAnnotation(ConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ConverterHead not enable");
        this.headModel = new ExcelHeadModel(head);
        this.readerPoiConfig = readerPoiConfig;
        this.clazzConvert = new ClazzConvert(clazz);
    }

    public EntityResultBuildConvert<T, Sheet, Cell> find(InputStreamConvert resource, int readHeadRownum)
            throws ConverterException {
        FieldInfos filedInfos = new FindResource(this, headModel, readHeadRownum).find(resource);
        return new EntityResultBuildConvert<T, Sheet, Cell>(clazz, filedInfos, readerPoiConfig);
    }
}
