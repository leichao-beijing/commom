package run.cmid.common.reader.exception;

import lombok.Getter;
import run.cmid.common.reader.model.eumns.ConverterErrorType;

import java.util.Map;
import java.util.Set;

public class ConverterException extends Exception {

    public static ConverterException build(Set<ConverterErrorType> errorSet, Map<String, String> errorMap) {
        return new ConverterException(errorSet, errorMap);
    }


    private ConverterException(Set<ConverterErrorType> errorSet, Map<String, String> errorMap) {
        super(errorSet.toString());
        this.errorSet = errorSet;
        this.errorMap = errorMap;
    }

    @Getter
    private Set<ConverterErrorType> errorSet;
    @Getter
    private Map<String, String> errorMap;
}
