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
public class SheetModel<T> implements Comparable<SheetModel<T>> {
    public SheetModel(CompareResponseAndErrorList<FieldDetail<T>, String,ConverterExcelException> response, Sheet sheet) {
        this.response = response;
        this.sheet = sheet;
        size = response.getList().size();
    }

    public SheetModel(ConverterExcelException ex) {
        this.ex = ex;
    }

    private ConverterExcelException ex;
    private Sheet sheet;
    private int size;
    private CompareResponseAndErrorList<FieldDetail<T>, String,ConverterExcelException> response;

    public int compareTo(SheetModel<T> sheetModel) {
        if (this.size < sheetModel.getSize()) {
            return -1;
        }
        return 0;
    }

    public Integer getTag(String fieldName) {
        CompareResponse<FieldDetail<T>, String> compareResponse = getCompareResponse(fieldName);
        if (compareResponse == null)
            return null;
        return compareResponse.getSrcIndex();
    }

    public CompareResponse<FieldDetail<T>, String> getCompareResponse(String fieldName) {
        if (response.getList() == null)
            return null;
        for (CompareResponse<FieldDetail<T>, String> compareResponse : response.getList()) {
            if (compareResponse.getSrcData().getFieldName().equals(fieldName))
                return compareResponse;
        }
        return null;
    }

}