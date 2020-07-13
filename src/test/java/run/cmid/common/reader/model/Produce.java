package run.cmid.common.reader.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.annotations.ConverterHead;
import run.cmid.common.reader.annotations.FindColumn;
import run.cmid.common.validator.annotations.*;
import run.cmid.common.validator.eumns.ValidationType;

import javax.validation.constraints.FutureOrPresent;
import java.util.Date;

@Getter
@Setter
@ConverterHead
public class Produce {
    @FieldName("序号")
    private Long id;

    @FieldName("工程类型")
    @FindColumn(value = "工程类型", checkColumn = true)
    private String engineeringType;

    @FieldName("项目号")
    @FindColumn(value = "项目号", checkColumn = true)
    private String projectId;

    @FieldName("需求类型")
    @FindColumn(value = "需求类型", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
            }, check = true, value = {"新建", "扩容", "大修"}, mode = ValidationType.EQUALS)
    })
    private String demandType;

    @FieldName("需求号")
    @FindColumn(value = "需求号", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
            }, check = true),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = "2G", mode = ValidationType.INCLUDE, message = "2G需求号")},
                    mode = ValidationType.REGEX, message = "必须为GSM开头，字母后面有10位以内字符", regex = "^(GSM)\\d{1,10}$"),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = "4G", mode = ValidationType.INCLUDE, message = "4G需求号")},
                    mode = ValidationType.REGEX, message = "必须为TDL开头，字母后面有10位以内字符", regex = "^(TDL)\\d{1,10}$"),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = "5G", mode = ValidationType.INCLUDE, message = "5G需求号")},
                    mode = ValidationType.REGEX, message = "必须为NR开头，字母后面有10位以内字符", regex = "^(NR)\\d{1,10}$"),
    })
    private String demandId;

    @FieldName("工程号")
    @FindColumn(value = "工程号", checkColumn = true)

    @FiledValidators({
            @FiledValidator(mode = ValidationType.REGEX, message = "“需求号”后面增加“-xx”，“xx”为1或2位数字", regex = "^({{demandId}})-\\d{1,2}$"),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
            }, check = true)
    })
    private String engineeringId;

    @FindColumn(value = "方案名称", checkColumn = true)
    @FieldName("方案名称")
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String schemeName;

    //系统根据 方案名称 进行自动生成
    @FieldName("站名")

    private String siteName;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, message = "10位以内数字", regex = "^\\d{1,10}$")
    })

    @FindColumn(value = "站号", checkColumn = true)
    @FieldName("站号")
    private String siteId;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FindColumn(value = "方案所在县", checkColumn = true)
    @FieldName("方案所在县")
    private String county;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FindColumn(value = "区域", checkColumn = true)
    @FieldName("区域")
    private String area;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FieldName("站点详细地址")
    private String address;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FieldName("小区或建筑物名称")
    private String addressName;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FieldName("产权单位")
    private String proprietor;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, message = "数字格式", regex = "^\\d*$")
    })
    @FieldName("楼宇数量")
    private String size;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FieldName("覆盖区域")
    private String coverRange;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "coverage", message = "建筑面积应≥覆盖面积", mode = ValidationType.GREATER_THAN_OR_EQUAL)}),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$", message = "保留两位小数")
    })
    @FieldName("建筑面积（万平米）")
    private String floorage;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "floorage", message = "覆盖面积应≤建筑面积", mode = ValidationType.GREATER_THAN_OR_EQUAL)}),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{1,2})?$", message = "保留两位小数")
    })
    @FieldName("覆盖面积（万平米）")
    private String coverage;

    @FieldName("未覆盖区域说明")
    private String uncoverRemarks;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"办公区", "居民区", "餐饮场所", "娱乐场所", "商场", "医院", "学校", "体育场馆", "展览中心", "宾馆饭店", "商住", "其他"})
    })
    @FieldName("覆盖功能类型")
    private String abilityType;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{5,5})?[1-9]$", message = "保留六位小数最后一位不为0")
    })
    @FieldName("经度")
    private String longitude;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(([1-9]{1}\\d*)|(0{1}))(\\.\\d{5,5})?[1-9]$", message = "保留六位小数最后一位不为0")
    })
    @FieldName("纬度")
    private String latitude;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"城一", "城二", "城三", "通州",
                    "顺义", "平谷", "密云", "怀柔", "延庆", "昌平", "房山", "大兴", "重点"})
    })
    @FieldName("分公司")
    private String branchOffice;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    @FieldName("网格确认人")
    private String gridHead;

    //根据网络确认人自动匹配对数据
    @FieldName("网格电话")
    private String gridPhone;

    //根据网络确认人自动匹配对数据
    @FieldName("需求提出人")
    private String demandInitiation;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"是", "否"})
    })
    @FieldName("是否提交智能审核平台")
    private String submitSystemState;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})
    })
    @FieldName("勘察单位")
    private String checkOffice;


    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = {"自有", "工时定额", "单项目合作", "多项目合作"}, mode = ValidationType.EQUALS)}, check = true)
    })
    @FieldName("勘察单位合作类型")
    private String checkOfficeCooperationType;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    @FieldName("勘察人")
    private String checkHead;

    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    @FieldName("勘察完成日期")
    private Date checkEndDate;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})})
    @FieldName("设计单位")
    private String designOffice;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = {"自有", "工时定额", "单项目合作", "多项目合作"}, mode = ValidationType.EQUALS)}, check = true)
    })
    @FieldName("设计单位合作类型")
    private String designOfficeCooperationType;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    @FieldName("设计人")
    private String designHead;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, message = "11位数字", regex = "^\\d{11,11}$", check = true)
    })
    @FieldName("设计院电话")
    private String designPhone;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "checkEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“勘察完成日期”")})
    })
    @FieldName("设计完成日期")
    private Date designEndDate;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "designEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“设计完成日期”")})
    })
    @FieldName("网格审核通过日期")
    private Date GridEndDate;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "GridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网格审核通过日期”")})
    })
    @FieldName("网优审核通过日期")
    private String optimumEndDate;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"是", "否"}, check = true)
    })
    @FieldName("是否变更方案")
    private String changeSchemeState;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "changeSchemeState", value = "是", mode = ValidationType.EQUALS)}, check = true),
    })
    @FieldName("变更内容")
    private String changeContext;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "optimumEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网优审核通过日期”")})
    })
    @FieldName("变更网格审核通过时间")
    private Date changeGridEndDate;


    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "changeGridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“变更网格审核通过日期”")})
    })
    @FieldName("变更网优审核通过时间")
    private Date changeOptimumDate;

    @FieldName("计提设计单位")
    private String provisionOffice;

    @FieldName("计提设计单位合作类型")
    private String provisionCooperationType;

    @FieldName("计提设计人")
    private String provisionHead;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"NR-D", "LTE-E", "LTE-F", "LTE-FDD", "LTE-D", "锚点-FDD", "锚点-F", "LTE-E、F", "LTE-E、FDD", "LTE-E、D", "LTE-F、FDD", "LTE-F、D",
                    "LTE-FDD、D", "LTE-E、F、FDD", "LTE-F、FDD、D", "LTE-E、F、FDD、D", "GSM", "DCS", "NB", "WLAN"}, mode = ValidationType.EQUALS)
    })
    @FieldName("建设网段")
    private String buildNetworkSegment;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"新建2G", "新建4G", "新建5G", "扩容4G", "扩容5G", "升级4G", "升级5G"}, mode = ValidationType.EQUALS)
    })
    @FieldName("建设类型")

    private String buildType;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"Lampsite", "Lightsite", "新建单支路室分", "新建双支路室分", "扩容单支路室分", "扩容双支路室分", "补点单支路室分", "补点双支路室分", "馈入单支路室分", "馈入双支路室分", "新建第二支路室分", "信源扩容",
                    "Book RRU", "京信2G分布式", "京信4G分布式", "京信2G、4G分布式", "京信4G一体化", "京信2G、4G一体化", "MDAS", "WLAN"}, mode = ValidationType.EQUALS)
    })
    @FieldName("建设方式")
    private String buildMode;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{1,10}$", message = "必须输入10位以内数字")
    })
    @FieldName("5G对应的4G BBU站号")
    private String Buu5gTo4gSiteId;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(require = {@FiledRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = "")}, check = true)
    })
    @FieldName("5G对应的4G BBU站名")
    private String Buu5gTo4gSiteName;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(require = {@FiledRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = "")}, mode = ValidationType.REGEX, regex = "^\\d{1,20}$", message = "必须输入20位以内数字", check = true)
    })
    @FieldName("5G对应的4G BBU SN")
    private String Buu5gBuildSN;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"新装", "直接升级", "换框升级"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("5G BBU建设方式")
    private String Buu5gBuildMode;


    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
            @FiledRequire(fieldName = "engineeringType", mode = ValidationType.EQUALS, value = "5G微蜂窝信源工程")
    }, value = {"SA", "NSA"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("5G组网模式")
    private String networkMode5g;

    @FieldName("框架")
    private String framework;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^[1-9][0-9]$", message = "百分数形式，无小数点", check = true)
    })
    @FieldName("施工折扣")//^[1-9][0-9]$
    private String constructionDiscount;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数", check = true)
    })
    @FieldName("勘察费（含税）（元）")//^(\-)?\d+(\.\d{2,2})$
    private String checkCost;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数", check = true)
    })
    @FieldName("设计费（含税）（元）")
    private String designCost;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数", check = true)
    })
    @FieldName("施工费（不含安全生产费）（不含税）（元）")
    private String constructionCost;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数", check = true)
    })
    @FieldName("安全生产费（不含税）（元）")
    private String safeProductionCost;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数", check = true)
    })
    @FieldName("设备费 （不含税）（元")
    private String deviceCost;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("建安费（不含税）（元）")
    private String buildSafeCost;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("其他费（不含税）（元）")
    private String otherCost;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("预备费（不含税）（元）")
    private String prepareCost;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("总投资（不含税）（元）")
    private String investCountCost;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"华为", "工程局", "中通一局", "浙江邮电", "中移建设"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("施工厂家")
    private String constructionOffice;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.NO_EMPTY, message = "不能为空")
    })
    @FieldName("施工厂家联系人")
    private String constructionHead;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{11,11}$", message = "手机号码必须11位数字")
    })
    @FieldName("施工厂家联系电话")
    private String constructionPhone;

    @FieldName("监理单位")
    private String supervisorOffice;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"诺西", "华为", "中兴", "京信", "爱立信"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("信源设备厂家")
    private String sourceDeviceOffice;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    @FieldName("设备原配置")//^((([O][0-9]\+)*([O][0-9]))|[0])$
    private String deviceSourceConfig;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    @FieldName("设备配置")
    private String deviceConfig;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    @FieldName("分区工具小区数量（Lampsite填写）")
    private String partitionToolCount;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    @FieldName("分区数")//^\d*$
    private String partitionCount;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("分区说明")
    private String partitionExplain;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"微蜂窝共享业主机房", "微蜂窝独立机房"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("机房形式")
    private String equipmentRoomMode;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"常规-独立机房", "常规-非独立机房"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("基站类型")
    private String siteType;


    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("机房面积（平米）")
    private String equipmentRoomModeArea;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^(\\-)?\\d+(\\.\\d{2,2})$", message = "保留两位小数")
    })
    @FieldName("机房高度（米）")
    private String equipmentRoomModeHeight;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("共站信息")
    private String sitePublicInfo;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("BBU数量")
    private String bbuCount;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("BBU位置")
    private String bbuAddress;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    @FieldName("RRU rHUB SW数量")
    private String RruRHubSwCount;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("RRU rHUB SW位置")
    private String RruRHubSwAddress;

    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    @FieldName("pRRU DP数量")
    private String pRruDpCount;

    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    @FieldName("传统室分天线数量")
    private String distributedSystemAerialCount;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}
            , value = {"新建GPS", "利旧GPS", "PTN同步"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("GPS同步方式")
    private String gpsSynchronousMode;

    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    @FieldName("信源产值上报日期")//^\d{4}-\d{1,2}-\d{1,2}
    private String sourceDeviceProduceSubmitDate;

    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    @FieldName("分布产值上报日期")
    private String distributedProduceSubmitDate;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("天线点是否入户覆盖")
    private String fromIndoorState;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("如为居民楼是否有室分外打")
    private String outsideState;
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    @FieldName("如无室分外打，请说明理由")
    private String outsideExplain;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, mode = ValidationType.EQUALS, value = {"是", "否"}, message = "是能填入 是 否", check = true),
    })
    @FieldName("是否模测")
    private String testState;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, check = true),
    })
    @FieldName("未模测原因")
    private String nuTestExplain;

    @FieldName("使用套管长度")
    private String pipeHeight;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("是否涉及整改流程（四方上站）")
    private String processState;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    @FieldName("是否建设光交箱")
    private String buildRayBoxState;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "buildRayBoxState", value = "否", mode = ValidationType.EQUALS),
            }, value = {"Lampsite", "Book RRU", "Femto", "分布扩容", "分布改造", "无需建设", "分布馈入", "党建箱", "Qcell", "无法建设", "Lightsite", "分布系统改造", "Lampsite升级"},
                    mode = ValidationType.EQUALS, check = true),
    })
    @FieldName("未建设原因")
    private String unBuildRayBoxExplain;

    @FieldName("备注")
    private String remarks;
}

