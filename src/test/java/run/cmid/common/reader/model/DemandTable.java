package run.cmid.common.reader.model;

import java.util.ArrayList;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.ConverterProperty;
import run.cmid.common.reader.annotations.ConverterHead;
import run.cmid.common.reader.annotations.Index;
import run.cmid.common.reader.annotations.Method;
import run.cmid.common.reader.model.eumns.ExcelReadType;
import run.cmid.common.validator.eumns.ValueType;

@ToString
@Getter
@Setter
@ConverterHead(maxWrongCount = 1,indexes = { @Index({ "needNumber" }) })
public class DemandTable {

    @ConverterProperty(value = { "需求序号" }, methods = {
            //@Method(compareValue = "\\", exceptionType = ValueType.NUMBER,model = ExcelReadType.NO_EQUALS),
            //@Method(compareValue = { "001" }, model = ExcelReadType.NO_EQUALS),
            //@Method(value = "003",fieldName = "needName",  compareValue= { "E报表需求" }, model = ExcelReadType.NO_EQUALS),
            //@Method(value = "006",fieldName = "needName",compareValue = {}, model = ExcelReadType.NO_EQUALS,check = true),
            @Method(check = true),
    })
    private String needNumber;// 需求序号

    @ConverterProperty(value = { "需求名称" })
    private String needName;// 需求名称

    @ConverterProperty(value = { "送审总工作量" }, model = ExcelReadType.INCLUDE)
    private Double auditTotalPerson;// 送审总工作量

    //@ConverterProperty(value = { "送审Cosmic评估工作量" }, model = ExcelReadType.INCLUDE)
    //private Double auditCosmicPerson;// 送审Cosmic评估工作量


    //private ArrayList<String> list;

    @ConverterProperty(value = { "时间" }, model = ExcelReadType.INCLUDE)
    private Date date;

    @ConverterProperty(value = { "value1" })
    private String value1;
    @ConverterProperty(value = { "value2" })
    private String value2;
}