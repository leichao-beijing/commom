package run.cmdi.common.reader.exception;

import lombok.Getter;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConverterExceptionUtils {
    public static ConverterExceptionUtils build() {
        return new ConverterExceptionUtils();
    }

    public void addError(String fieldName, ConverterErrorType errorType) {
        errorMap.put(fieldName, errorType.getTypeName());
        errorSet.add(errorType);
    }

    public static ConverterExceptionUtils build(String key, ConverterErrorType errorType) {
        ConverterExceptionUtils exUtils = build();
        exUtils.addError(key, errorType);
        return exUtils;
    }

    @Getter
    private Set<ConverterErrorType> errorSet = EnumSet.noneOf(ConverterErrorType.class);
    @Getter
    private Map<String, String> errorMap = new HashMap<>();

    public ConverterException exception() {
        return ConverterException.build(errorSet, errorMap);
    }

    public void throwException() throws ConverterException {
        if (!errorMap.isEmpty())
            throw ConverterException.build(errorSet, errorMap);
    }
}
