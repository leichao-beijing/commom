package run.cmid.common.excel.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.excel.annotations.ExcelConverter;
import run.cmid.common.excel.annotations.ExcelConverterHead;
import run.cmid.common.excel.annotations.TableName;
import run.cmid.common.excel.model.eumns.ExcelReadType;

@ToString
@Getter
@Setter
@ExcelConverterHead
public class DemandTable {

    @TableName(values = {"需求序号"})
    @ExcelConverter(range = {"001"},check = true)
    private String needNumber;// 需求序号
    @TableName(values = {"需求名称"})
    private String needName;// 需求名称
    @TableName(values = {"送审总工作量"},model = ExcelReadType.INCLUDE)
    private Double auditTotalPerson;// 送审总工作量
    @TableName(values = {"送审Cosmic评估工作量"},model = ExcelReadType.INCLUDE)

    private Double auditCosmicPerson;// 送审Cosmic评估工作量

}