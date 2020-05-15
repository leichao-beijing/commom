package run.cmid.common.excel.model;

import org.apache.poi.ss.usermodel.Sheet;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.excel.exception.ConverterExcelException;
import run.cmid.common.excel.model.entity.CompareResponseAndErrorList;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class HeadModel implements Comparable<HeadModel> {
    public HeadModel(CompareResponseAndErrorList<FieldDetail, String,ConverterExcelException> response) {
        this.response = response;
        size = response.getList().size();
    }

    public HeadModel(ConverterExcelException ex) {
        this.ex = ex;
    }

    private ConverterExcelException ex;
    private Sheet sheet;
    private int size;
    private CompareResponseAndErrorList<FieldDetail, String,ConverterExcelException> response;

    public int compareTo(HeadModel headModel) {
        if (this.size < headModel.getSize()) {
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