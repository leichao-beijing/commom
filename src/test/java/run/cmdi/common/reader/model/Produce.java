package run.cmdi.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.enums.*;
import run.cmdi.common.validator.annotations.*;
import run.cmdi.common.validator.eumns.ValidationType;

import javax.validation.constraints.PastOrPresent;
import java.util.Date;

@Getter
@Setter
@ConverterHead
//@Entity
//@Table(name = "produce_pre")
public class Produce {
    @FieldName("序号")
    private Long id;

    @FieldName("制式")
    @FindColumn(value = "制式", checkColumn = true)
     private NetworkMode networkMode;

    @FieldName("类型")
    @FindColumn(value = "类型", checkColumn = true)
    private SourceMode sourceMode;

    @FieldName("需求类型")
    @FindColumn(value = "需求类型", checkColumn = true)
    private DemandType demandType;

    /**
     * 工程类型为分布类型时，需在 工程号=需求号-FB-BBU序号，其他需求号-BBU序号
     */
    @FieldName("工程类型")//工程类型+制式+类型+需求类型
    private String engineeringType;

    @FieldName("项目号")
    @FindColumn(value = "项目号", checkColumn = true)
    private String projectId;

    @FieldName("需求号")
    @FindColumn(value = "需求号", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {@FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
            }, check = true),
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = "2G微蜂窝", mode = ValidationType.INCLUDE, message = "2G需求号"),
                    @FieldRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为GSM开头，字母后面有10位以内字符", regex = "^(GSM)[0-9A-Za-z]{1,10}$"),
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = "4G微蜂窝", mode = ValidationType.INCLUDE, message = "4G需求号"),
                    @FieldRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为TDL开头，字母后面有10位以内字符", regex = "^(TDL)[0-9A-Za-z]{1,10}$"),
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = "5G微蜂窝", mode = ValidationType.INCLUDE, message = "5G需求号"),
                    @FieldRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为NR开头，字母后面有10位以内字符", regex = "^(NR)[0-9A-Za-z]{1,10}$"),
    })
    private String demandId;

    @FieldName("站址站名")
    @FindColumn(value = "站址站名", checkColumn = true)
    @FieldValidation(mode = ValidationType.NO_EMPTY, check = true)
    private String addressTagName;

    @FieldName("BBU序号")
    @FindColumn(value = "BBU序号", checkColumn = true)
    private String bbuId;

    @FieldName("设备厂家")
    @FindColumn(value = "设备厂家", checkColumn = true)
    @FieldValidation(mode = ValidationType.EQUALS, value = {"H", "Z", "N", "J"}, check = true)
    private String deviceTag;

    @FieldName("基站标签")
    @FindColumn(value = "基站标签", checkColumn = true)
    @FieldValidation(mode = ValidationType.EQUALS, value = {"M", "X"})
    private String siteMode;

    @FieldName("工程号")//需求号 + (分布式 FB) +BBU序号； - 连接符号
    private String engineeringId;

    /**
     * 系统根据 方案名称 进行自动生成 分公司 + 制式 +站址站名 + 设备厂家 + 基站类型
     */
    @FieldName("方案名称")
    private String schemeName;

    /**
     * 进行自动生成 分公司 + 制式 +站址站名 +BBU站号 + 设备厂家 + 基站类型
     */
    @FieldName("站名")
    private String siteName;

    @FieldName("站号")
    @FindColumn(value = "站号", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            mode = ValidationType.REGEX, message = "10位以内数字", regex = "^\\d{1,10}$")
    private String siteId;

    @FieldName("站点所在区县")
    @FindColumn(value = "站点所在区县", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String county;

    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FindColumn(value = "区域", checkColumn = true)
    @FieldName("区域")
    private String area;

    @FindColumn(value = "站点详细地址", checkColumn = true)
    @FieldName("站点详细地址")
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String address;

    @FindColumn(value = "小区或建筑物名称", checkColumn = true)
    @FieldName("小区或建筑物名称")
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String addressName;

    @FindColumn(value = "产权单位", checkColumn = true)
    @FieldName("产权单位")
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String proprietor;

    @FindColumn(value = "楼宇数量", checkColumn = true)
    @FieldName("楼宇数量")
    @FieldValidations({
            @FieldValidation(require = {@FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, message = "数字格式", regex = "^\\d*$")
    })
    private Integer size;

    @FindColumn(value = "覆盖区域", checkColumn = true)
    @FieldName("覆盖区域")
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String coverRange;

    @FieldName("建筑面积（万平米）")
    @FindColumn(value = "建筑面积\n（万平米）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(fieldValidation = {@FieldCompare(fieldName = "coverage", message = "建筑面积应≥覆盖面积", mode = ValidationType.GREATER_THAN_OR_EQUAL)}),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String floorage;

    @FieldName("覆盖面积（万平米）")
    @FindColumn(value = "覆盖面积\n（万平米）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(fieldValidation = {@FieldCompare(fieldName = "floorage", message = "覆盖面积应≤建筑面积", mode = ValidationType.LESS_THAN_OR_EQUAL)}, mode = ValidationType.EXECUTE),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String coverage;

    @FieldName("未覆盖区域说明")
    @FindColumn(value = "未覆盖\n区域说明", checkColumn = true)
    private String uncoverRemarks;

    @FieldName("覆盖功能类型")
    @FindColumn(value = "覆盖功能类型", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"办公区", "居民区", "餐饮场所", "娱乐场所", "商场", "医院", "学校", "体育场馆", "展览中心", "宾馆饭店", "商住", "其他"})
    })
    private String abilityType;

    @FieldName("经度")
    @FindColumn(value = "经度", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{6,})?$", message = "至少保留小数点后六位")
    })//结果要求保留小数点后六位 最后一位为O时进1
    private String longitude;

    @FieldName("纬度")
    @FindColumn(value = "纬度", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{6,})?$", message = "至少保留小数点后六位")
    })//结果要求保留小数点后六位 最后一位为O时进1
    private String latitude;

    @FieldName("分公司")
    @FindColumn(value = "分公司", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"城一", "城二", "城三", "通州",
                    "顺义", "平谷", "密云", "怀柔", "延庆", "昌平", "房山", "大兴", "重点"})
    })
    private String branchOffice;

    @FieldName("网格确认人")
    @FindColumn(value = "网格\n确认人", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String gridHead;

    //根据网络确认人自动匹配对数据
    @FieldName("网格电话")
    private String gridPhone;

    //根据网络确认人自动匹配对数据
    @FieldName("需求提出人")
    private String demandInitiation;

    @FieldName("是否提交智能审核平台")
    @FindColumn(value = "是否提交\n智能审核平台", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"是", "否"})
    })
    private YesNoState submitSystemState;

    @FieldName("勘察单位")
    @FindColumn(value = "勘察单位", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})
    })
    private String checkOffice;

    @FieldName("勘察单位合作类型")
    @FindColumn(value = "勘察单位合作类型", checkColumn = true)
    private CooperateType checkOfficeCooperationType;

    @FieldName("勘察人")
    @FindColumn(value = "勘察人", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String checkHead;

    @FieldName("勘察完成日期")
    @FindColumn(value = "勘察\n完成日期", checkColumn = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private Date checkEndDate;

    @FieldName("设计单位")
    @FindColumn(value = "设计单位", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})})
    private String designOffice;

    @FieldName("设计单位合作类型")
    @FindColumn(value = "设计单位合作类型", checkColumn = true)
    private CooperateType designOfficeCooperationType;

    @FieldName("设计人")
    @FindColumn(value = "设计人", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String designHead;

    @FieldName("设计人电话")
    @FindColumn(value = "设计人电话", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, message = "11位数字", regex = "^\\d{11,11}$", check = true)
    })
    private String designPhone;

    @FieldName("设计完成日期")
    @FindColumn(value = "设计\n完成日期", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {
                    @FieldCompare(fieldName = "checkEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“勘察完成日期”")}, check = true)
    private Date designEndDate;

    @FieldName("网格审核通过日期")
    @FindColumn(value = "网格审核\n通过日期", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {
                    @FieldCompare(fieldName = "designEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“设计完成日期”")})
    private Date GridEndDate;

    @FieldName("网优审核通过日期")
    @FindColumn(value = "网优审核\n通过日期", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),}
            , fieldValidation = {
            @FieldCompare(fieldName = "GridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网格审核通过日期”")})
    private Date optimumEndDate;

    @FieldName("是否变更方案")
    @FindColumn(value = "是否变更方案", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.EQUALS, value = {"是", "否"}, check = true)
    })
    private YesNoState changeSchemeState;

    @FieldName("变更内容")
    @FindColumn(value = "变更内容", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
            @FieldRequire(fieldName = "changeSchemeState", value = "是", mode = ValidationType.EQUALS)}, check = true)
    private String changeContext;

    @FieldName("变更网格审核通过日期")
    @FindColumn(value = "变更网格审核通过日期", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {@FieldCompare(fieldName = "optimumEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网优审核通过日期”")})
    private Date changeGridEndDate;

    @FieldName("变更网优审核通过日期")
    @FindColumn(value = "变更网优审核通过日期", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {@FieldCompare(fieldName = "changeGridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“变更网格审核通过日期”")})
    private Date changeOptimumDate;

    @FieldName("计提设计单位")
    @FindColumn(value = "计提设计单位", checkColumn = true)
    private String provisionOffice;

    @FieldName("计提设计单位合作类型")
    @FindColumn(value = "计提设计单位合作类型", checkColumn = true)
    private CooperateType provisionCooperationType;

    @FieldName("计提设计人")
    @FindColumn(value = "计提设计人", checkColumn = true)
    private String provisionHead;

    @FieldName("建设网段")
    @FindColumn(value = "建设网段", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(value = {"NR-D", "LTE-E", "LTE-F", "LTE-FDD", "LTE-D", "锚点-FDD", "锚点-F", "LTE-E、F", "LTE-E、FDD", "LTE-E、D", "LTE-F、FDD", "LTE-F、D",
                    "LTE-FDD、D", "LTE-E、F、FDD", "LTE-F、FDD、D", "LTE-E、F、FDD、D", "GSM", "DCS", "NB", "WLAN"}, mode = ValidationType.EQUALS)
    })
    private String buildNetworkSegment;

    @FieldName("建设类型")
    private String buildType;

    @FieldName("建设方式")
    @FindColumn(value = "建设方式", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(value = {"Lampsite升级5G", "Lampsite", "Lightsite", "新建单支路室分", "新建双支路室分", "扩容单支路室分", "扩容双支路室分", "补点单支路室分", "补点双支路室分", "馈入单支路室分", "馈入双支路室分", "新建第二支路室分", "信源扩容",
                    "Book RRU", "京信2G分布式", "京信4G分布式", "京信2G、4G分布式", "京信4G一体化", "京信2G、4G一体化", "MDAS", "WLAN"}, mode = ValidationType.EQUALS)
    })
    private String buildMode;

    @FieldName("5G对应的4G BBU站号")
    @FindColumn(value = "5G对应的\n4G BBU站号", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.REGEX, regex = "^\\d{1,10}$", message = "必须输入10位以内数字")
    private String Buu5gTo4gSiteId;

    @FieldName("5G对应的4G BBU站名")
    @FindColumn(value = "5G对应的\n4G BBU站名", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = ""),
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.EXECUTE, check = true),
    })
    private String Buu5gTo4gSiteName;

    @FieldName("5G对应的4G BBU SN")
    @FindColumn(value = "5G对应的\n4G BBU SN", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
            @FieldRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = "")}, mode = ValidationType.REGEX, regex = "^\\d{1,20}$", message = "必须输入20位以内数字", check = true)
    private String buu5gBuildSN;

    @FieldName("5G BBU建设方式")
    @FindColumn(value = "5G BBU建设方式", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
            }, value = {"新装", "直接升级", "换框升级"}, mode = ValidationType.EQUALS),
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"5G微蜂窝"}, mode = ValidationType.EQUALS),
                    @FieldRequire(fieldName = "sourceMode", value = {"信源"}, mode = ValidationType.EQUALS),
            }, value = {"新装", "直接升级", "换框升级"}, mode = ValidationType.EQUALS, check = true),
    })//5G微蜂窝信源工程 sourceMode
    private String Buu5gBuildMode;

    @FieldName("5G组网模式")
    @FindColumn(value = "5G\n组网模式", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
            }, value = {"SA", "NSA"}, mode = ValidationType.EQUALS),
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"5G微蜂窝"}, mode = ValidationType.EQUALS),
                    @FieldRequire(fieldName = "sourceMode", value = {"信源"}, mode = ValidationType.EQUALS),
            }, value = {"SA", "NSA"}, mode = ValidationType.EQUALS, check = true)
    })
    private String networkMode5g;

    @FieldName("框架")
    @FindColumn(value = "框架", checkColumn = true)
    private String framework;

    @FieldName("施工折扣")//^[1-9][0-9]$
    @FindColumn(value = "施工折扣", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.INTEGER, message = "百分数形式，无小数点")
    private Integer constructionDiscount;

    @FieldName("勘察费（含税）（元）")//^(\-)?\d+(\.\d{2,2})$
    @FindColumn(value = "勘察费\n（含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double checkCost;

    @FieldName("设计费（含税）（元）")
    @FindColumn(value = "设计费\n（含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double designCost;

    @FieldName("施工费（不含安全生产费）（不含税）（元）")
    @FindColumn(value = "施工费\n（不含安全生产费）\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double constructionCost;

    @FieldName("安全生产费（不含税）（元）")
    @FindColumn(value = "安全生产费\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double safeProductionCost;

    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    @FieldName("设备费 （不含税）（元）")
    @FindColumn(value = "设备费\n（不含税）\n（元）", checkColumn = true)
    private Double deviceCost;

    @FieldName("建安费（不含税）（元）")
    @FindColumn(value = "建安费\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double buildSafeCost;


    @FieldName("其他费（不含税）（元）")
    @FindColumn(value = "其他费\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double otherCost;

    @FieldName("预备费（不含税）（元）")
    @FindColumn(value = "预备费\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private Double prepareCost;

    @FieldName("总投资（不含税）（元）")
    @FindColumn(value = "总投资\n（不含税）\n（元）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NUMBER, message = "数值格式")
    })
    private String investCountCost;

    @FieldName("施工厂家")
    @FindColumn(value = "施工厂家", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            value = {"华为", "工程局", "中通一局", "浙江邮电", "中移建设"}, mode = ValidationType.EQUALS, check = true)
    private String constructionOffice;

    @FieldName("施工厂家联系人")
    @FindColumn(value = "施工厂家\n联系人", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.NO_EMPTY, message = "不能为空")
    })
    private String constructionHead;

    @FieldName("施工厂家联系电话")
    @FindColumn(value = "施工厂家\n联系人电话", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d{11,11}$", message = "手机号码必须11位数字")
    })
    private String constructionPhone;

    @FieldName("监理单位")
    @FindColumn(value = "监理单位", checkColumn = true)
    private String supervisorOffice;

    @FieldName("信源设备厂家")
    @FindColumn(value = "信源设备厂家", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            value = {"诺西", "华为", "中兴", "京信", "爱立信"}, mode = ValidationType.EQUALS, check = true)
    private String sourceDeviceOffice;

    @FieldName("设备原配置")//^((([O][0-9]\+)*([O][0-9]))|[0])$
    @FindColumn(value = "设备原配置", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    private String deviceSourceConfig;

    @FieldName("设备配置")
    @FindColumn(value = "设备配置", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    private String deviceConfig;

    @FieldName("分区工具小区数量（Lampsite填写）")
    @FindColumn(value = "分区工具小区数量（Lampsite填写）", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private Integer partitionToolCount;

    @FieldName("分区数")//^\d*$
    @FindColumn(value = "分区数", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    private Integer partitionCount;

    @FieldName("分区说明")
    @FindColumn(value = "分区说明", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String partitionExplain;

    @FieldName("机房形式")
    @FindColumn(value = "机房形式", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            value = {"微蜂窝共享业主机房", "微蜂窝独立机房"}, mode = ValidationType.EQUALS, check = true)
    private String equipmentRoomMode;

    @FieldName("基站类型")
    @FindColumn(value = "基站类型", checkColumn = true)
    private String siteType;

    @FieldName("机房面积（平米）")
    @FindColumn(value = "机房面积\n（平米）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private Double equipmentRoomArea;

    @FieldName("机房高度（米）")
    @FindColumn(value = "机房高度\n（米）", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private Double equipmentRoomHeight;

    @FieldName("共站信息")
    @FindColumn(value = "共站信息", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String sitePublicInfo;

    @FieldName("BBU数量")
    @FindColumn(value = "BBU数量", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private Integer bbuCount;

    @FieldName("BBU位置")
    @FindColumn(value = "BBU位置", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String bbuAddress;

    @FieldName("RRU rHUB SW数量")
    @FindColumn(value = "RRU\nrHUB\nSW\n数量", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    private Integer RruRHubSwCount;

    @FieldName("RRU rHUB SW位置")
    @FindColumn(value = "RRU\nrHUB\nSW\n位置", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String RruRHubSwAddress;

    @FieldName("pRRU DP数量")
    @FindColumn(value = "pRRU\nDP\n数量", checkColumn = true)
    @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private Integer pRruDpCount;


    @FieldName("传统室分天线数量")
    @FindColumn(value = "传统室分天线数量", checkColumn = true)
    @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private Integer distributedSystemAerialCount;

    @FieldName("GPS同步方式")
    @FindColumn(value = "GPS同步方式", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}
            , value = {"新建GPS", "利旧GPS", "PTN同步"}, mode = ValidationType.EQUALS, check = true)
    private String gpsSynchronousMode;

    @FieldName("信源产值上报日期")//^\d{4}-\d{1,2}-\d{1,2}
    @FindColumn(value = "信源产值上报日期", checkColumn = true)
    @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    private Date sourceDeviceProduceSubmitDate;

    @FieldName("分布产值上报日期")
    @FindColumn(value = "分布产值上报日期", checkColumn = true)
    @FieldValidation(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    private String distributedProduceSubmitDate;

    @FieldName("天线点是否入户覆盖")
    @FindColumn(value = "天线点是否入户覆盖", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    private YesNoState fromIndoorState;

    @FieldName("如为居民楼是否有室分外打")
    @FindColumn(value = "如为居民楼是否有室分外打", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    private YesNoState outsideState;

    @FieldName("如无室分外打，请说明理由")
    @FindColumn(value = "如无室分外打，请说明理由", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String outsideExplain;

    @FieldName("是否模测")
    @FindColumn(value = "是否模测", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
                    @FieldRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, mode = ValidationType.EQUALS, value = {"是", "否"}, message = "是能填入 是 否", check = true),
    })
    private YesNoState testState;

    @FieldName("未模测原因")
    @FindColumn(value = "未模测原因", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
                    @FieldRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, check = true),
    })
    private String nuTestExplain;

    @FieldName("使用套管长度")
    @FindColumn(value = "使用套管长度", checkColumn = true)
    private Double pipeHeight;

    @FieldName("是否涉及整改流程（四方上站）")
    @FindColumn(value = "是否涉及整改流程（四方上站）", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    private YesNoState processState;

    @FieldName("是否建设光交箱")
    @FindColumn(value = "是否建设光交箱", checkColumn = true)
    @FieldValidation(require = {
            @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    private YesNoState buildRayBoxState;

    @FieldName("未建设原因")
    @FindColumn(value = "未建设原因", checkColumn = true)
    @FieldValidations({
            @FieldValidation(require = {
                    @FieldRequire(fieldName = "networkMode", value = {"WLAN", "微蜂窝"}, mode = ValidationType.NO_EQUALS),
                    @FieldRequire(fieldName = "buildRayBoxState", value = "否", mode = ValidationType.EQUALS),
            }, value = {"Lampsite", "Book RRU", "Femto", "分布扩容", "分布改造", "无需建设", "分布馈入", "党建箱", "Qcell", "无法建设", "Lightsite", "分布系统改造", "Lampsite升级"},
                    mode = ValidationType.EQUALS, check = true),
    })
    private String unBuildRayBoxExplain;

    @FieldName("备注")
    @FindColumn(value = "备注", checkColumn = true)
    private String remarks;
}

