package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.CellPosition;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.validator.annotations.FieldName;

@ConverterHead(maxWrongCount = 1)
@Getter
@Setter
@ToString
public class ToExcelModel {
    public ToExcelModel(String address, String message) {
        this.address = address;
        this.message = message;
    }

    @CellPosition("A1")
    @FieldName("问题位置")
    @FindColumn(value = "问题位置")
    private String address;

    @CellPosition("A2")
    @FieldName("详细信息")
    @FindColumn(value = "详细信息")
    private String message;

    @CellPosition("A3")
    @FieldName("这是除外信息")
    @FindColumn(value = "这是除外信息")
    private String messageExcept;
}
