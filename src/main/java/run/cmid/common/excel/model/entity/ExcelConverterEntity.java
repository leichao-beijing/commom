package run.cmid.common.excel.model.entity;

import java.util.Arrays;
import java.util.List;

import lombok.Getter;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.model.eumns.ExcelReadType;

/**
 * 
 * @author leichao
 */
@Getter
public class ExcelConverterEntity {
    public ExcelConverterEntity(ExcelConverter excelConverter, ExcelReadType model, String value) {
        this(excelConverter, model);
        list = Arrays.asList(value);
    }

    public ExcelConverterEntity(ExcelConverter excelConverter, ExcelReadType model, String[] values) {
        this(excelConverter, model);
        list = Arrays.asList(values);
    }

    public ExcelConverterEntity(ExcelConverter excelConverter, ExcelReadType model) {
        check = excelConverter.check();
        max = excelConverter.max();
        this.model = model;
    }

    private List<String> list;
    private boolean check = false;
    /**
     * 当读取内容为字符串时，最大字符串长度限制
     */
    private int max = 100;
    private ExcelReadType model = ExcelReadType.EQUALS;
}
