package run.cmid.common.excel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.model.eumns.ExcelReadType;

@ToString
@Getter
@Setter
public class DemandTable {

    @ExcelConverter(value = { "需求序号" })
    private String needNumber;// 需求序号

    @ExcelConverter(value = { "需求名称" })
    private String needName;// 需求名称

    @ExcelConverter(value = { "送审总工作量" })
    private Double auditTotalPerson;// 送审总工作量

    @ExcelConverter(value = { "送审Cosmic评估工作量" },model = ExcelReadType.INCLUDE)
    private Double auditCosmicPerson;// 送审Cosmic评估工作量

    @ExcelConverter(value = { "非COSMIC评估工作量" },model = ExcelReadType.INCLUDE)
    private Double nonCosmic;// 非COSMIC评估工作量

    @ExcelConverter(value = { "送审COSMIC功能点数量" },model = ExcelReadType.INCLUDE)
    private Double cosmicFunction;// 送审COSMIC功能点数量

    @ExcelConverter(value = { "外协人员工作内容" })
    private String externalWork;// 外协人员工作内容

    @ExcelConverter(value = { "外协人员工作人天数" })
    private Double externalWorkDay;// 外协人员工作人天数
    
    @ExcelConverter(value = { "自有人员工作内容" })
    private String work;// 自有人员工作内容
    
    @ExcelConverter(value = { "外协人员工作人天数" })
    private Double workDay;// 外协人员工作人天数
}