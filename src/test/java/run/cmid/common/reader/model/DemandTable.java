package run.cmid.common.reader.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.*;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;
import run.cmid.common.validator.eumns.Value;

@ToString
@Getter
@Setter
@ConverterHead(maxWrongCount = 1, indexes = {@Index({"needNumber"})})
public class DemandTable {

    @FindColumn(value = {"需求序号"}, matches = {
            @Match(value = "\\", model = ExcelRead.NO_EQUALS),//不能等于\
            @Match(value = {"001"}, model = ExcelRead.NO_EQUALS),//不能等于 001
            //needNumber 不允许出现"\\"
            @Match(require = {
                    @FiledRequire(fieldName = "needName", value = "E报表需求")
            }, value = "003", model = ExcelRead.NO_EQUALS),//当 needName ==  E报表需求 时 需求序号 不能等于 003
            @Match(require = {
                    @FiledRequire(fieldName = "needName", value = "报表1")
            },check = true),//当 needName ==  报表1 时 需求序号 不能为空

    })
    private String needNumber;// 需求序号

    @FindColumn(value = {"需求名称"})
    private String needName;// 需求名称

    @FindColumn(value = {"送审总工作量"}, model = FindModel.INCLUDE)
    private Double auditTotalPerson;// 送审总工作量

    //@ConverterProperty(value = { "送审Cosmic评估工作量" }, model = ExcelReadType.INCLUDE)
    //private Double auditCosmicPerson;// 送审Cosmic评估工作量


    //private ArrayList<String> list;

    @FindColumn(value = {"时间"}, model = FindModel.INCLUDE)
    private Date date;

    @FindColumn(value = {"value1"})
    private String value1;
    @FindColumn(value = {"value2"})
    private String value2;
}