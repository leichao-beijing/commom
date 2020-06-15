package run.cmid.common.reader.model;

import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.reader.annotations.*;
import run.cmid.common.reader.annotations.Index;
import run.cmid.common.reader.enums.CooperateContent;
import run.cmid.common.reader.enums.Cooperate;
import run.cmid.common.reader.enums.TableState;
import run.cmid.common.reader.enums.TagState;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.reader.model.eumns.FindModel;

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

    @FindColumn("序号")
    private Long id;

    @FindColumn(value = "生产部门", checkColumn = true, matches = {@Match(check = true, model = ExcelRead.NONE)})
    private String production;

    @FindColumn(value = "MIS号", checkColumn = true, matches = {@Match(check = true)})
    private String misIdPartyA;

    @FindColumn(value = "甲方项目名称", checkColumn = true, matches = {@Match(check = true)})
    private String projectNamePartyA;

    @FindColumn(value = "建设单位", checkColumn = true, matches = {@Match(check = true)})
    private String provider;

    @FindColumn(value = "建设方项目经理", checkColumn = true,
            matches = {
                     @Match(value = "唐亮", model = ExcelRead.NO_EQUALS),
            })
    private String headPartyA;

    @FindColumn(value = "项目类型", checkColumn = true, matches = {@Match(check = true)})
    private String projectType;

    @FindColumn(value = "工程类别", checkColumn = true, matches = {@Match(model = ExcelRead.EQUALS, value = {"2G宏蜂窝主设备", "4G宏蜂窝主设备", "5G宏蜂窝主设备", "2G微蜂窝主设备", "4G微蜂窝主设备", "5G微蜂窝主设备", "宏蜂窝配套", "微蜂窝配套",
            "微基站", "分布式皮站", "WLAN", "Femto", "NB宏蜂窝", "NB微蜂窝", "宏蜂窝扩容1", "微蜂窝扩容", "5G锚点", "可研费", "宏蜂窝大修", "微蜂窝大修"})})
    private String engineeringType;

    @FindColumn(value = "内部立项编号", checkColumn = true, matches = {@Match(check = true)})
    private String insideProjectId;

    @FindColumn(value = "内部立项名称", checkColumn = true, matches = {@Match(check = true)})
    private String insideProjectName;

    @FindColumn(value = "项目负责人", checkColumn = true, matches = {@Match(check = true)})
    private String head;

    @FindColumn(value = "立项(计划)总投资", checkColumn = true, model = FindModel.INCLUDE)
    private Double costTotal;

    @FindColumn(value = "建设规模", checkColumn = true, matches = {@Match(check = true)})
    private String size;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "最终设计批复通过日期")
    private Date finalAdoptTime;

    @FindColumn(value = "是否修正")
    private String replaceState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "修正设计批复通过日期")
    private Date replaceAdoptTime;

    @FindColumn(value = "勘察设计费", checkColumn = true, model = FindModel.INCLUDE, matches = {@Match(check = true)})
    private Double checkDesignerCost;

    @FindColumn(value = "合作类型", enumGetValueMethodName = "getTypeName", checkColumn = true, matches = {@Match(check = true)})
    private Cooperate cooperateType;

    @FindColumn(value = "合作单位", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"工时定额", "单项目合作", "多项目合作"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = "自有")}, value = "/", converterException = false),
    })
    private String cooperateProvider;

    @FindColumn(value = "合作内容", enumGetValueMethodName = "getTypeName", checkColumn = true,
            matches = {
                    @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"工时定额", "单项目合作", "多项目合作"})}),
                    @Match(require = {@FiledRequire(fieldName = "cooperateType", value = "自有")}, value = "/", converterException = false),
            })
    private CooperateContent cooperateContent;

    @FindColumn(value = "合作工作量", model = FindModel.INCLUDE, matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"单项目合作"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double cooperateCost;

    @FindColumn(value = "合作单位入围折扣", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double discount;

    @FindColumn(value = "合作工作量比例", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"单项目合作"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double cooperateWorkRatio;

    @FindColumn(value = "合作成本占比", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double cooperateCostRatio;

    @FindColumn(value = "合作工时", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"工时定额"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double cooperateTime;

    @FindColumn(value = "合作成本预估", matches = {
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}),
            @Match(require = {@FiledRequire(fieldName = "cooperateType", value = {"自有"})}, value = "/", converterException = false)
    })
    private Double cooperateCostAssess;

    @FindColumn(value = "工作量完成年度", checkColumn = true, matches = {@Match(check = true)})
    private int workYear;

    @FindColumn(value = "项目是否已确认核销", checkColumn = true, matches = {@Match(check = true)})
    private String destructionState;

    @FindColumn(value = "订单状态", enumGetValueMethodName = "getTypeName")
    private TableState tableState = TableState.NORMAL;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FindColumn(value = "更新时间")
    private Date uptime;

    @FindColumn(value = "错误信息")
    private String errorMessage;

    @FindColumn(value = "计算当年产值年份", checkColumn = true, matches = {@Match(check = true)})
    private int produceYear;

    @FindColumn(value = "标签", enumGetValueMethodName = "getTypeName", checkColumn = true, matches = {@Match(check = true)})
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
