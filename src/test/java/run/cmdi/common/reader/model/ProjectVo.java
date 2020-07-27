package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.model.eumns.FindModel;

@Getter
@Setter
@ToString
@ConverterHead
public class ProjectVo {
    @FindColumn(value = {"季度"})
    private String quarter;// 季度

    @FindColumn(value = {"项目名称"})
    private String projectName;// 项目名称

    @FindColumn(value = {"需求数量总计"}, model = FindModel.INCLUDE)
    private int needTotal;// 需求数量总计(个)

    @FindColumn(value = {"送审工作量总计"}, model = FindModel.INCLUDE)
    private int auditWorkloadTotal;// 送审工作量总计(人天)

    @FindColumn(value = {"送审COSMIC评估工作量"}, model = FindModel.INCLUDE)
    private int auditCosmicWorkload;// 送审COSMIC评估工作量(人天)

    @FindColumn(value = {"非COSMIC评估工作量"}, model = FindModel.INCLUDE)
    private int nonCosmicWorkload;// 非COSMIC评估工作量(人天)

    @FindColumn(value = {"送审COSMIC功能点数量总计"}, model = FindModel.INCLUDE)
    private int auditCosmicPointTotal;// 送审COSMIC功能点数量总计(个)

    @FindColumn(value = {"部门"})
    private String department;// 部门

    @FindColumn(value = {"服务提供方"})
    private String provider; // 服务提供方

}