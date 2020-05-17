package run.cmid.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.annotations.*;
import run.cmid.common.reader.enums.*;
import run.cmid.common.reader.model.eumns.ExcelReadType;

import java.util.ArrayList;
import java.util.Date;

/**
 * 产值表
 *
 * @author leichao
 * @date 2020-03-30 01:25:51
 */
@Getter
@Setter
@ConverterHead(maxWrongCount = 1, indexes = {@Index({"demandId", "provider", "cooperateContent"})})
public class ProduceTable {
    @ConverterProperty(value = "序号")
    private Long id;

    @ConverterProperty(value = "工程分类", checkNull = true)
    private String engineeringSort;

    @ConverterProperty(value = "项目类型", enumGetValueMethodName = "getTypeName", checkNull = true)
    private ProjectType projectType;

    @ConverterProperty(value = "需求号", checkNull = true,methods={@Method(compareValue = {"TDL"} ,model = ExcelReadType.NO_INCLUDE)})
    private String demandId;

    @ConverterProperty(value = "站名", checkNull = true)
    private String siteName;

    @ConverterProperty(value = "建设单位", checkNull = true)
    private String provider;

    @ConverterProperty(value = "项目负责人", checkNull = true)
    private String head;

    @ConverterProperty(value = "设计人员", checkNull = true)
    private String designer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "勘察完成", checkNull = true, model = ExcelReadType.INCLUDE)
    private Date chckDoneDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "设计完成", checkNull = true, model = ExcelReadType.INCLUDE)
    private Date designerDoneDate;

    @ConverterProperty(value = "勘察费", checkNull = true, model = ExcelReadType.INCLUDE)
    private Double chckCost;

    @ConverterProperty(value = "设计费", checkNull = true, model = ExcelReadType.INCLUDE)
    private Double designerCost;

    @ConverterProperty(value = "勘察设计费", checkNull = true, model = ExcelReadType.INCLUDE)
    private Double chckDesignerCost;

    @ConverterProperty(value = "合作类型", enumGetValueMethodName = "getTypeName", checkNull = true)
    private CooperateType cooperateType;

    @ConverterProperty(value = "合作单位", checkNull = true ,methods={@Method(value ="/",fieldName = "cooperateType",compareValue = {"自有"})})
    private String cooperateProvider;

    @ConverterProperty(value = "合作内容", enumGetValueMethodName = "getTypeName", checkNull = true)
    private CooperateContent cooperateContent;

    @ConverterProperty(value = "天线数量", model = ExcelReadType.INCLUDE)
    private Double aerialValue;

    @ConverterProperty(value = "PRRU数量", model = ExcelReadType.INCLUDE)
    private Double prruValue;

    @ConverterProperty(value = "合作成本预估", model = ExcelReadType.INCLUDE)
    private Double cooperateCost;

    @ConverterProperty(value = "需求是否已确认核销", checkNull = true)
    private String demandConfirmation;

    @ConverterProperty(value = "内部立项编号", checkNull = true)
    private String insideProjectId;

    @ConverterProperty(value = "内部立项名称", checkNull = true)
    private String insideProjectName;

    @ConverterProperty(value = "甲方项目mis号", checkNull = true)
    private String misIdPartyA;

    @ConverterProperty(value = "所入甲方项目名称", checkNull = true)
    private String projectNamePartyA;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "产值上报时间", checkNull = true)
    private Date productionReportDate;

    @ConverterProperty(value = "标签", enumGetValueMethodName = "getTypeName", checkNull = true)
    private TagState tag;

    @ConverterProperty(value = "备注1")
    private String other;

    @ConverterProperty(value = "订单状态", enumGetValueMethodName = "getTypeName")
    private TableState tableState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "更新时间")
    private Date uptime;

    @ConverterProperty(value = "错误信息")
    private String errorMessage;



    @ConverterPropertyList({@ConverterProperty(value = "合同编号"), @ConverterProperty(value = "是否已计提"), @ConverterProperty(value = "计提内部立项编号"),
            @ConverterProperty(value = "计提内部立项名称"), @ConverterProperty(value = "计提合作单位"), @ConverterProperty(value = "结算勘察设计费"),
            @ConverterProperty(value = "计提金额(元)"), @ConverterProperty(value = "是否已结算"), @ConverterProperty(value = "确认单编号"), @ConverterProperty(value = "备注2"),
            @ConverterProperty(value = "是否自有或多项目转为单项目"), @ConverterProperty(value = "是否在14年8月已核对"), @ConverterProperty(value = "是否漏报产值"),
            @ConverterProperty(value = "签字标签"), @ConverterProperty(value = "原勘察设计费（元）"), @ConverterProperty(value = "原自有产值"),
            @ConverterProperty(value = "原预估合作费")})
    private ArrayList<String> list;

    public ProduceTable() {
    }
}
