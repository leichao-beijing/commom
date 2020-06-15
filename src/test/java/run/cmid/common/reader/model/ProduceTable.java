package run.cmid.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.annotations.*;
import run.cmid.common.reader.enums.*;
import run.cmid.common.reader.enums.Project;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;

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
    @FindColumn(value = "序号")
    private Long id;

    @FindColumn(value = "工程分类", checkColumn = true)
    private String engineeringSort;

    @FindColumn(value = "项目类型", enumGetValueMethodName = "getTypeName", checkColumn = true)
    private Project projectType;

    @FindColumn(value = "需求号", checkColumn = true, matches = {@Match(value = {"TDL"}, model = ExcelRead.NO_INCLUDE)})
    private String demandId;

    @FindColumn(value = "站名", checkColumn = true)
    private String siteName;

    @FindColumn(value = "建设单位", checkColumn = true)
    private String provider;

    @FindColumn(value = "项目负责人", checkColumn = true)
    private String head;

    @FindColumn(value = "设计人员", checkColumn = true)
    private String designer;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "勘察完成", checkColumn = true, model = FindModel.INCLUDE)
    private Date checkDoneDate;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "设计完成", checkColumn = true, model = FindModel.INCLUDE)
    private Date designerDoneDate;

    @FindColumn(value = "勘察费", checkColumn = true, model = FindModel.INCLUDE)
    private Double checkCost;

    @FindColumn(value = "设计费", checkColumn = true, model = FindModel.INCLUDE)
    private Double designerCost;

    @FindColumn(value = "勘察设计费", checkColumn = true, model = FindModel.INCLUDE)
    private Double checkDesignerCost;

    @FindColumn(value = "合作类型", enumGetValueMethodName = "getTypeName", checkColumn = true)
    private Cooperate cooperateType;

    @FindColumn(value = "合作单位", checkColumn = true, matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = "自有")}, value = "/")
    })
    private String cooperateProvider;

    @FindColumn(value = "合作内容", enumGetValueMethodName = "getTypeName", checkColumn = true)
    private CooperateContent cooperateContent;

    @FindColumn(value = "天线数量", model = FindModel.INCLUDE)
    private Double aerialValue;

    @FindColumn(value = "PRRU数量", model = FindModel.INCLUDE)
    private Double prruValue;

    @FindColumn(value = "合作成本预估", model = FindModel.INCLUDE)
    private Double cooperateCost;

    @FindColumn(value = "需求是否已确认核销", checkColumn = true)
    private String demandConfirmation;

    @FindColumn(value = "内部立项编号", checkColumn = true)
    private String insideProjectId;

    @FindColumn(value = "内部立项名称", checkColumn = true)
    private String insideProjectName;

    @FindColumn(value = "甲方项目mis号", checkColumn = true)
    private String misIdPartyA;

    @FindColumn(value = "所入甲方项目名称", checkColumn = true)
    private String projectNamePartyA;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "产值上报时间", checkColumn = true)
    private Date productionReportDate;

    @FindColumn(value = "标签", enumGetValueMethodName = "getTypeName", checkColumn = true)
    private TagState tag;

    @FindColumn(value = "备注1")
    private String other;

    @FindColumn(value = "订单状态", enumGetValueMethodName = "getTypeName")
    private TableState tableState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "更新时间")
    private Date uptime;

    @FindColumn(value = "错误信息")
    private String errorMessage;


    @FindColumns({@FindColumn(value = "合同编号"), @FindColumn(value = "是否已计提"), @FindColumn(value = "计提内部立项编号"),
            @FindColumn(value = "计提内部立项名称"), @FindColumn(value = "计提合作单位"), @FindColumn(value = "结算勘察设计费"),
            @FindColumn(value = "计提金额(元)"), @FindColumn(value = "是否已结算"), @FindColumn(value = "确认单编号"), @FindColumn(value = "备注2"),
            @FindColumn(value = "是否自有或多项目转为单项目"), @FindColumn(value = "是否在14年8月已核对"), @FindColumn(value = "是否漏报产值"),
            @FindColumn(value = "签字标签"), @FindColumn(value = "原勘察设计费（元）"), @FindColumn(value = "原自有产值"),
            @FindColumn(value = "原预估合作费")})
    private ArrayList<String> list;

    public ProduceTable() {
    }
}
