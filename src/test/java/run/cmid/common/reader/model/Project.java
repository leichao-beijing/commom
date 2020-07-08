package run.cmid.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jdk.jfr.Name;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.*;
import run.cmid.common.reader.enums.Cooperate;
import run.cmid.common.reader.enums.CooperateContent;
import run.cmid.common.reader.enums.TableState;
import run.cmid.common.reader.enums.TagState;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;
import run.cmid.common.validator.annotations.FieldName;
import run.cmid.common.validator.annotations.FiledValidator;

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
        @Index({"cooperateType", "cooperateProvider", "misIdPartyA", "insideProjectId"})})
@ToString
public class Project {

    @FindColumn(value = "序号")
    @FieldName("序号")
    @FiledValidator(value = "1", model = ExcelRead.NO_EQUALS)
    private Long id;

    //@FindColumn(value = "生产部门", checkColumn = true, matches = {@Match(require={@FiledRequire(fieldName = "id",value = "2")},check = true)})
    @FindColumn(value = "生产部门", checkColumn = true)
    @Name("生产部门")
    private String production;

    @FindColumn(value = "MIS号", checkColumn = true)
    @Name("MIS号")
    private String misIdPartyA;

    @FindColumn(value = "甲方项目名称", checkColumn = true)
    @Name("甲方项目名称")
    private String projectNamePartyA;

    @FindColumn(value = "建设单位", checkColumn = true)
    @Name("建设单位")
    private String provider;

    @FindColumn(value = "建设方项目经理", checkColumn = true)
    @Name("建设方项目经理")
    private String headPartyA;

    @FindColumn(value = "项目类型", checkColumn = true)
    @Name("项目类型")
    private String projectType;

    @FindColumn(value = "工程类别", checkColumn = true)
    //, matches = {@Match(model = ExcelRead.EQUALS, value = {"2G宏蜂窝主设备", "4G宏蜂窝主设备", "5G宏蜂窝主设备", "2G微蜂窝主设备", "4G微蜂窝主设备", "5G微蜂窝主设备", "宏蜂窝配套", "微蜂窝配套",
    //            "微基站", "分布式皮站", "WLAN", "Femto", "NB宏蜂窝", "NB微蜂窝", "宏蜂窝扩容1", "微蜂窝扩容", "5G锚点", "可研费", "宏蜂窝大修", "微蜂窝大修"})}
    @Name("工程类别")
    private String engineeringType;

    @FindColumn(value = "内部立项编号", checkColumn = true)
    @Name("内部立项编号")
    private String insideProjectId;

    @FindColumn(value = "内部立项名称", checkColumn = true)
    @Name("内部立项名称")
    private String insideProjectName;

    @FindColumn(value = "项目负责人", checkColumn = true)
    @Name("项目负责人")
    private String head;

    @FindColumn(value = "立项(计划)总投资", checkColumn = true, model = FindModel.INCLUDE)
    @Name("立项(计划)总投资")
    private Double costTotal;

    @FindColumn(value = "建设规模", checkColumn = true)
    @Name("建设规模")
    private String size;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "最终设计批复通过日期")
    @Name("最终设计批复通过日期")
    private Date finalAdoptTime;

    @FindColumn(value = "是否修正")
    @Name("是否修正")
    private String replaceState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "修正设计批复通过日期")
    private Date replaceAdoptTime;

    @FindColumn(value = "勘察设计费", checkColumn = true, model = FindModel.INCLUDE)
    private Double checkDesignerCost;

    @FindColumn(value = "合作类型", checkColumn = true)
    private Cooperate cooperateType;

    @FindColumn(value = "合作单位")
    // matches = {
    //            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"工时定额", "单项目合作", "多项目合作"})}),
    //            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = "自有")}, value = "/", converterException = false),
    //    }
    private String cooperateProvider;

    @FindColumn(value = "合作内容", checkColumn = true)
    private CooperateContent cooperateContent;

    @FindColumn(value = "合作工作量", model = FindModel.INCLUDE)

    private Double cooperateCost;

    @FindColumn(value = "合作单位入围折扣")
    private Double discount;

    @FindColumn(value = "合作工作量比例")
    private Double cooperateWorkRatio;

    @FindColumn(value = "合作成本占比")
    private Double cooperateCostRatio;

    @FindColumn(value = "合作工时")
    private Double cooperateTime;

    @FindColumn(value = "合作成本预估")
    private Double cooperateCostAssess;

    @FindColumn(value = "工作量完成年度", checkColumn = true)
    private int workYear;

    @FindColumn(value = "项目是否已确认核销", checkColumn = true)
    private String destructionState;

    @FindColumn(value = "订单状态")
    private TableState tableState = TableState.NORMAL;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "更新时间")
    private Date uptime;

    @FindColumn(value = "错误信息")
    private String errorMessage;

    @FindColumn(value = "计算当年产值年份", checkColumn = true)
    private int produceYear;

    @FindColumn(value = "标签", checkColumn = true)
    private TagState tag;

    @FindColumns({@FindColumn("我院合同编号"), @FindColumn("是否已工作量确认"), @FindColumn("工作量确认内部立项号"),
            @FindColumn("工作量确认金额(元)"), @FindColumn("是否已结算"), @FindColumn("结算金额(元)"),
            @FindColumn("结算合作单位"), @FindColumn("工作量确认编号")})
    private ArrayList<String> list;

    @FindColumn(value = "备注")
    private String other;

    @FindColumn(value = "上年度末累计完成产值", model = FindModel.INCLUDE)
    private Double lastYearCost;

    /**
     * 相同MisId And insideProjectId(内部立项编号)下的
     */
    @FindColumn(value = "当年产值合计", model = FindModel.INCLUDE)
    private Double yearCost;

    @FindColumn(value = "当年完成比例")
    private Double ratio;

    @FindColumn(value = "历年累计完成比例")
    private Double historyRatio;

    @FindColumn(value = "当年单项目合作成本")
    private Double countCost;

    public Project() {
    }

}
