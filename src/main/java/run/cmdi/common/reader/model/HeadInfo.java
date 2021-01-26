package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.core.ReaderPage;
import run.cmdi.common.reader.exception.ConverterException;

import java.util.Map;

/**
 * @author leichao
 */
@Getter
@Setter
public class HeadInfo<PAGE, UNIT> implements Comparable<HeadInfo> {
    public HeadInfo(Map<String, FieldDetailOld> map, ReaderPage<PAGE, UNIT> readerPage) {
        this.map = map;
        size = map.size();
        this.readerPage = readerPage;
    }

    public HeadInfo(ConverterException ex) {
        this.ex = ex;
    }

    private ReaderPage<PAGE, UNIT> readerPage;
    private ConverterException ex;
    private int size;
    private Map<String, FieldDetailOld> map;

    public int compareTo(HeadInfo headInfo) {
        if (this.size < headInfo.getSize()) {
            return -1;
        }
        return 0;
    }

    public Integer getTag(String fieldName) {
        FieldDetailOld fieldDetailOld = getCompareResponse(fieldName);
        if (fieldDetailOld == null)
            return null;
        if (fieldDetailOld.getPosition() == -1)
            return null;
        return fieldDetailOld.getPosition();
    }

    public FieldDetailOld getCompareResponse(String fieldName) {

        return map.get(fieldName);
    }

}