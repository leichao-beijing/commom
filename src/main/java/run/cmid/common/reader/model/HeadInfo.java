package run.cmid.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.reader.core.ReaderPage;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.entity.CompareResponseAndErrorList;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class HeadInfo<PAGE, UNIT> implements Comparable<HeadInfo> {
    public HeadInfo(CompareResponseAndErrorList<FieldDetail, String,ConverterExcelException> response, ReaderPage<PAGE, UNIT> readerPage) throws ConverterExcelException {
        this.response = response;
        size = response.getList().size();
        this.readerPage = readerPage;
    }

    public HeadInfo(ConverterExcelException ex) {
        this.ex = ex;
    }
    private ReaderPage<PAGE, UNIT>  readerPage;
    private ConverterExcelException ex;
    private int size;
    private CompareResponseAndErrorList<FieldDetail, String,ConverterExcelException> response;

    public int compareTo(HeadInfo headInfo) {
        if (this.size < headInfo.getSize()) {
            return -1;
        }
        return 0;
    }

    public Integer getTag(String fieldName) {
        CompareResponse<FieldDetail, String> compareResponse = getCompareResponse(fieldName);
        if (compareResponse == null)
            return null;
        return compareResponse.getSrcIndex();
    }

    public CompareResponse<FieldDetail, String> getCompareResponse(String fieldName) {
        if (response.getList() == null)
            return null;
        for (CompareResponse<FieldDetail, String> compareResponse : response.getList()) {
            if (compareResponse.getSrcData().getFieldName().equals(fieldName))
                return compareResponse;
        }
        return null;
    }

}