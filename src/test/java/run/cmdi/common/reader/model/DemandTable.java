package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.Index;
import run.cmdi.common.reader.model.eumns.FindModel;

@ToString
@Getter
@Setter
@ConverterHead(maxWrongCount = 1, indexes = {@Index({"needNumber"})})
public class DemandTable {

    @FindColumn(value = {"需求序号"}, checkColumn = true)
    private String needNumber;// 需求序号

    @FindColumn(value = {"需求名称"}, checkColumn = true)
    private String needName;// 需求名称

    @FindColumn(value = {"送审总工作量"}, model = FindModel.INCLUDE, checkColumn = true)
    private Double auditTotalPerson;// 送审总工作量

    @FindColumn(value = {"送审COSMIC评估工作量"}, model = FindModel.INCLUDE, checkColumn = true)
    private Double auditCosmicPerson;// 送审Cosmic评估工作量

    @FindColumn(value = {"非COSMIC评估工作量"}, model = FindModel.INCLUDE, checkColumn = true)
    private Double nonCosmic;// 非COSMIC评估工作量

    @FindColumn(value = {"送审COSMIC功能点数量"}, model = FindModel.INCLUDE, checkColumn = true)
    private Double cosmicFunction;// 送审COSMIC功能点数量

    @FindColumn(value = {"外协人员工作内容"}, checkColumn = true)
    private String externalWork;// 外协人员工作内容

    @FindColumn(value = {"外协人员工作人天数"}, checkColumn = true)
    private Double externalWorkDay;

    @FindColumn(value = {"自有人员工作内容"}, checkColumn = true)
    private String work;// 自有人员工作内容

    @FindColumn(value = {"自有人员工作人天数"}, checkColumn = true)
    private Double workDay;


}