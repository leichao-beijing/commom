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
    @ConverterProperty("序号")
    private Long id;

    @ConverterProperty(value = "生产部门", checkColumn = true, matches = {@Match(check = true)})
    private String production;

    @ConverterProperty(value = "MIS号", checkColumn = true, matches = {@Match(check = true)})
    private String misIdPartyA;

    @ConverterProperty(value = "甲方项目名称", checkColumn = true, matches = {@Match(check = true)})
    private String projectNamePartyA;

    @ConverterProperty(value = "建设单位", checkColumn = true, matches = {@Match(check = true)})
    private String provider;

    @ConverterProperty(value = "建设方项目经理", checkColumn = true,
            matches = {
            @Match(check = true), @Match(value = "唐亮",model = ExcelRead.NO_EQUALS,check = true),
            })
    private String headPartyA;

    @ConverterProperty(value = "项目类型", checkColumn = true, matches = {@Match(check = true)})
    private String projectType;

    @ConverterProperty(value = "工程类别", checkColumn = true, matches = {@Match(value = {"2G宏蜂窝主设备", "4G宏蜂窝主设备", "5G宏蜂窝主设备", "2G微蜂窝主设备", "4G微蜂窝主设备", "5G微蜂窝主设备", "宏蜂窝配套", "微蜂窝配套",
            "微基站", "分布式皮站", "WLAN", "Femto", "NB宏蜂窝", "NB微蜂窝", "宏蜂窝扩容", "微蜂窝扩容", "5G锚点", "可研费", "宏蜂窝大修", "微蜂窝大修"})})
    private String engineeringType;

    @ConverterProperty(value = "内部立项编号", checkColumn = true, matches = {@Match(check = true)})
    private String insideProjectId;

    @ConverterProperty(value = "内部立项名称", checkColumn = true, matches = {@Match(check = true)})
    private String insideProjectName;

    @ConverterProperty(value = "项目负责人", checkColumn = true, matches = {@Match(check = true)})
    private String head;

    @ConverterProperty(value = "立项(计划)总投资", checkColumn = true, model = ExcelRead.INCLUDE)
    private Double costTotal;

    @ConverterProperty(value = "建设规模", checkColumn = true, matches = {@Match(check = true)})
    private String size;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "最终设计批复通过日期")
    private Date finalAdoptTime;

    @ConverterProperty(value = "是否修正")
    private String replaceState;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "修正设计批复通过日期")
    private Date replaceAdoptTime;

    @ConverterProperty(value = "勘察设计费", checkColumn = true, model = ExcelRead.INCLUDE, matches = {@Match(check = true)})
    private Double checkDesignerCost;

    @ConverterProperty(value = "合作类型", enumGetValueMethodName = "getTypeName", checkColumn = true, matches = {@Match(check = true)})
    private Cooperate cooperateType;

    @ConverterProperty(value = "合作单位", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"工时定额", "单项目合作", "多项目合作"})}),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = "自有")}, value = "/", converterException = false),
    })
    private String cooperateProvider;

    @ConverterProperty(value = "合作内容", enumGetValueMethodName = "getTypeName", checkColumn = true,
            matches = {@Match(check = true),
                    @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"工时定额", "单项目合作", "多项目合作"})}),
                    @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = "自有")}, value = "/", converterException = false),
            })
    private CooperateContent cooperateContent;

    @ConverterProperty(value = "合作工作量", model = ExcelRead.INCLUDE, matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"单项目合作"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double cooperateCost;

    @ConverterProperty(value = "合作单位入围折扣", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double discount;

    @ConverterProperty(value = "合作工作量比例", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"单项目合作"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double cooperateWorkRatio;

    @ConverterProperty(value = "合作成本占比", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double cooperateCostRatio;

    @ConverterProperty(value = "合作工时", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"工时定额"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double cooperateTime;

    @ConverterProperty(value = "合作成本预估", matches = {@Match(check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"单项目合作", "工时定额"})}, check = true),
            @Match(filedMatches = {@FiledMatches(fieldName = "cooperateType", value = {"自有"})}, value = "/", check = true, converterException = false)
    })
    private Double cooperateCostAssess;

    @ConverterProperty(value = "工作量完成年度", checkColumn = true, matches = {@Match(check = true)})
    private int workYear;

    @ConverterProperty(value = "项目是否已确认核销", checkColumn = true, matches = {@Match(check = true)})
    private String destructionState;

    @ConverterProperty(value = "订单状态", enumGetValueMethodName = "getTypeName")
    private TableState tableState = TableState.NORMAL;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @ConverterProperty(value = "更新时间")
    private Date uptime;

    @ConverterProperty(value = "错误信息")
    private String errorMessage;

    @ConverterProperty(value = "计算当年产值年份", checkColumn = true, matches = {@Match(check = true)})
    private int produceYear;

    @ConverterProperty(value = "标签", enumGetValueMethodName = "getTypeName", checkColumn = true, matches = {@Match(check = true)})
    private TagState tag;

    @ConverterPropertyList({@ConverterProperty("我院合同编号"), @ConverterProperty("是否已工作量确认"), @ConverterProperty("工作量确认内部立项号"),
            @ConverterProperty("工作量确认金额(元)"), @ConverterProperty("是否已结算"), @ConverterProperty("结算金额(元)"),
            @ConverterProperty("结算合作单位"), @ConverterProperty("工作量确认编号")})
    private ArrayList<String> list;

    @ConverterProperty(value = "备注")
    private String other;

    @ConverterProperty(value = "上年度末累计完成产值", model = ExcelRead.INCLUDE)
    private Double lastYearCost;

    /**
     * 相同MisId And insideProjectId(内部立项编号)下的
     */
    @ConverterProperty(value = "当年产值合计", model = ExcelRead.INCLUDE)
    private Double yearCost;

    @ConverterProperty(value = "当年完成比例")
    private Double ratio;

    @ConverterProperty(value = "历年累计完成比例")
    private Double historyRatio;

    @ConverterProperty(value = "当年单项目合作成本")
    private Double countCost;

    public Project() {
    }

}
