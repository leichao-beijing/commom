package run.cmid.common.excel.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterHead;
import run.cmid.common.excel.annotations.ExcelConverterList;
import run.cmid.common.excel.annotations.ExcelConverterSimple;
import run.cmid.common.excel.annotations.Index;
import run.cmid.common.excel.annotations.Method;
import run.cmid.common.excel.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

@ToString
@Getter
@Setter
@ExcelConverterHead(maxWrongCount = 1, indexes = { @Index({ "needNumber" }) })
public class DemandTable {

    @ExcelConverter(values = { "需求序号" }, methods = { @Method(values = { "\\" }, exceptionType = ValueType.NUMBER),
            @Method(values = { "001" }, model = ExcelReadType.NO_EQUALS) })
    private String needNumber;// 需求序号

    @ExcelConverter(values = { "需求名称" })
    private String needName;// 需求名称

    @ExcelConverter(values = { "送审总工作量" }, model = ExcelReadType.INCLUDE)
    private Double auditTotalPerson;// 送审总工作量

    @ExcelConverter(values = { "送审Cosmic评估工作量" }, model = ExcelReadType.INCLUDE)
    private Double auditCosmicPerson;// 送审Cosmic评估工作量

    @ExcelConverterList(value = { @ExcelConverterSimple(value = { "K1" }) ,@ExcelConverterSimple(value = { "K21" },checkNull = true)})
    private ArrayList<String> list;

}