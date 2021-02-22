package run.cmdi.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.*;
import run.cmdi.common.reader.enums.*;
import run.cmdi.common.validator.annotations.FieldName;
import run.cmdi.common.validator.annotations.FieldRequire;
import run.cmdi.common.validator.annotations.FieldValidation;
import run.cmdi.common.validator.annotations.FieldValidations;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.reader.model.eumns.FindModel;

import java.util.ArrayList;
import java.util.Date;

/**
 * 项目信息表
 *
 * @author leichao
 * @date 2020-03-30 01:25:51
 */
@Getter
@Setter
@ConverterHead(maxWrongCount = 1, indexes = {
        @Index({"cooperateType", "cooperateProvider", "misIdPartyA", "insideProjectId", "destructionState", "engineeringType"})})
@ToString
public class Project {
    @FindColumn("序号")
    @FieldName("序号")
    private Long id;

    @FindColumn(value = "错误信息")
    @FieldName("错误信息")
    private String errorMessage;

    @FindColumn(value = "生产部门", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("生产部门")
    private String production;

    @FindColumn(value = "MIS号", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("MIS号")
    private String misIdPartyA;

    public String getMisIdPartyA() {
        return misIdPartyA.replace(" ", "");
    }

    @FindColumn(value = "甲方项目名称", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("甲方项目名称")
    private String projectNamePartyA;

    @FindColumn(value = "建设单位", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("建设单位")
    private String provider;

    @FindColumn(value = "建设方项目经理", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("建设方项目经理")
    private String headPartyA;

    @FindColumn(value = "项目类型", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("项目类型")
    private String projectType;

    @FindColumn(value = "工程类别", checkColumn = true)
    @FieldValidation(mode = ValidationType.EQUALS, value = {"2G宏蜂窝主设备", "4G宏蜂窝主设备", "5G宏蜂窝主设备", "2G微蜂窝主设备", "4G微蜂窝主设备", "5G微蜂窝主设备", "宏蜂窝配套", "微蜂窝配套",
            "微基站", "分布式皮站", "WLAN", "Femto", "NB宏蜂窝", "NB微蜂窝", "宏蜂窝扩容", "微蜂窝扩容", "5G锚点", "可研费", "宏蜂窝大修", "微蜂窝大修"})
    @FieldName("工程类别")
    private String engineeringType;

    @FindColumn(value = "内部立项编号", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("内部立项编号")
    private String insideProjectId;

    public String getInsideProjectId() {
        return insideProjectId.replace(" ", "");
    }

    @FindColumn(value = "内部立项名称", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("内部立项名称")
    private String insideProjectName;

    @FindColumn(value = "项目负责人", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("项目负责人")
    private String head;

    @FindColumn(value = "立项(计划)总投资", checkColumn = true, model = FindModel.INCLUDE)
    @FieldName("立项(计划)总投资")
    private Double costTotal;

    @FindColumn(value = "建设规模", checkColumn = true)
    //@FieldValidation(check = true)
    @FieldName("建设规模")
    private String size;

    @FormatDate("yyyy-MM-dd")
    @FindColumn(value = "最终设计批复通过日期")
    @FieldName("最终设计批复通过日期")
    private Date finalAdoptTime;

    @FindColumn(value = "是否修正")
    @FieldName("是否修正")
    private String replaceState;

    @FormatDate("yyyy-MM-dd")
    @FindColumn(value = "修正设计批复通过日期")
    @FieldName("修正设计批复通过日期")
    private Date replaceAdoptTime;

    @FindColumn(value = "勘察设计费", checkColumn = true, model = FindModel.INCLUDE)
    @FieldValidations({
            //@FieldValidation(check = true),
            @FieldValidation(require = @FieldRequire(fieldName = "destructionState", value = "是"), value = "0", mode = ValidationType.EQUALS)
    })
    @FieldName("勘察设计费")
    private Double checkDesignerCost=0d;

    @FindColumn(value = "合作类型", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("合作类型")
    private CooperateType cooperateType;

    @FindColumn(value = "合作单位")
    @FieldValidations({
            @FieldValidation(check = true),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", mode = ValidationType.EQUALS, converterException = false)
    })
    @FieldName("合作单位")
    private String cooperateProvider;


    @FindColumn(value = "合作内容")
    @FieldValidations({
            @FieldValidation(check = true),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", mode = ValidationType.EQUALS, converterException = false)
    })
    @FieldName("合作内容")
    private CooperateContent cooperateContent;

    @FindColumn(value = "合作工作量", model = FindModel.INCLUDE)
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "单项目合作"), check = true)
    })
    @FieldName("合作工作量")
    private Double cooperateCost;

    @FindColumn(value = "合作单位入围折扣")
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"}), check = true)
    })
    @FieldName("合作单位入围折扣")
    private Double discount;

    @FindColumn(value = "合作工作量比例")
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "单项目合作"), check = true)
    })
    @FieldName("合作工作量比例")
    private Double cooperateWorkRatio;

    @FindColumn(value = "合作成本占比")
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"}), check = true)
    })
    @FieldName("合作成本占比")
    private Double cooperateCostRatio;

    @FindColumn(value = "合作工时")
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "工时定额"), check = true)
    })
    @FieldName("合作工时")
    private Double cooperateTime;

    @FindColumn(value = "合作成本预估", model = FindModel.INCLUDE)
    @FieldValidations({
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = "自有"), value = "/", check = true, mode = ValidationType.EQUALS, converterException = false),
            @FieldValidation(require = @FieldRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"}), check = true)
    })
    @FieldName("合作成本预估")
    private Double cooperateCostAssess;

    @FindColumn(value = "工作量完成年度", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("工作量完成年度")
    private int workYear;

    @FindColumn(value = "项目是否已确认核销", checkColumn = true)
    @FieldValidations({@FieldValidation(check = true), @FieldValidation(value = {"是", "否"}, mode = ValidationType.EQUALS)})
    @FieldName("项目是否已确认核销")
    private String destructionState;

    @FindColumn(value = "订单状态")
    @FieldName("订单状态")
    private TableState tableState = TableState.NORMAL;

    @FormatDate("yyyy-MM-dd")
    @FindColumn(value = "更新时间")
    @FieldName("更新时间")
    private Date uptime;



    @FindColumn(value = "计算当年产值年份", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("计算当年产值年份")
    private int produceYear;

    @FindColumn(value = "标签", checkColumn = true)
    @FieldValidation(check = true)
    @FieldName("标签")
    private TagState tag;

    @FindColumns({@FindColumn("我院合同编号"), @FindColumn("是否已工作量确认"), @FindColumn("工作量确认内部立项号"),
            @FindColumn("工作量确认金额(元)"), @FindColumn("是否已结算"), @FindColumn("结算金额(元)"),
            @FindColumn("结算合作单位"), @FindColumn("工作量确认编号")})
//    @FieldNameList({
//            @FieldName("我院合同编号"), @FieldName("是否已工作量确认"), @FieldName("工作量确认内部立项号"),
//            @FieldName("工作量确认金额(元)"), @FieldName("是否已结算"), @FieldName("结算金额(元)"),
//            @FieldName("结算合作单位"), @FieldName("工作量确认编号")
//    })
    private ArrayList<String> list;

    @FindColumn(value = "备注")
    @FieldName("备注")
    private String other;

    @FindColumn(value = "上年度末累计完成产值", model = FindModel.INCLUDE)
    @FieldName("上年度末累计完成产值")
    private Double lastYearCost;

    /**
     * 相同MisId And insideProjectId(内部立项编号)下的
     */
    @FindColumn(value = "当年产值合计", model = FindModel.INCLUDE)
    @FieldName("当年产值合计")
    private Double yearCost;

    @FindColumn(value = "当年完成比例")
    @FieldName("当年完成比例")
    private Double ratio;

    @FindColumn(value = "历年累计完成比例")
    @FieldName("历年累计完成比例")
    private Double historyRatio;

    @FindColumn(value = "当年单项目合作成本")
    @FieldName("当年单项目合作成本")
    private Double countCost;

    public Project() {
    }

}
