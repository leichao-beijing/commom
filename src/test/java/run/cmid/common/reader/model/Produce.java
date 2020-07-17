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

    /**
     * 工程类型为分布类型时，需在 工程号=需求号-FB-BBU序号，其他需求号-BBU序号
     */
    @FieldName("工程类型")
    @FindColumn(value = "工程类型", checkColumn = true)
    @FiledValidator(mode = ValidationType.EQUALS, value = {"4G微蜂窝信源工程", "4G微蜂窝信源工程", "5G微蜂窝信源工程", "5G微蜂窝分布工程", "2G微蜂窝信源工程", "2G微蜂窝分布工程", "2G微蜂窝替换工程", "4G微蜂窝信源工程",
            "4G微蜂窝分布工程", "5G微蜂窝信源工程", "5G微蜂窝分布工程", "WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"})
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
                    @FiledRequire(fieldName = "engineeringType", value = "2G", mode = ValidationType.INCLUDE, message = "2G需求号"),
                    @FiledRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为GSM开头，字母后面有10位以内字符", regex = "^(GSM)[0-9A-Za-z]{1,10}$"),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = "4G", mode = ValidationType.INCLUDE, message = "4G需求号"),
                    @FiledRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为TDL开头，字母后面有10位以内字符", regex = "^(TDL)[0-9A-Za-z]{1,10}$"),
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType", value = "5G", mode = ValidationType.INCLUDE, message = "5G需求号"),
                    @FiledRequire(fieldName = "demandId", value = "暂无", mode = ValidationType.NO_EQUALS)},
                    mode = ValidationType.REGEX, message = "必须为NR开头，字母后面有10位以内字符", regex = "^(NR)[0-9A-Za-z]{1,10}$"),
    })
    private String demandId;


    @FieldName("制式")
    @FindColumn(value = "制式", checkColumn = true)
    @FiledValidator(mode = ValidationType.EQUALS, value = {"2G", "3G", "4G", "5G"})
    private String mode;

    @FieldName("站址站名")
    @FindColumn(value = "站址站名", checkColumn = true)
    @FiledValidator(mode = ValidationType.NO_EMPTY, check = true)
    private String addressTagName;

    @FieldName("BBU序号")
    @FindColumn(value = "BBU序号", checkColumn = true)
    private String BuuId;

    @FieldName("设备厂家")
    @FindColumn(value = "设备厂家", checkColumn = true)
    @FiledValidator(mode = ValidationType.EQUALS, value = {"H", "Z", "N", "J"}, check = true)
    private String deviceId;

    @FieldName("基站标签")
    @FindColumn(value = "基站标签", checkColumn = true)
    @FiledValidator(mode = ValidationType.EQUALS, value = {"M", "X"})
    private String siteMode;

    @FieldName("工程号")
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
    @FiledValidator(require = {@FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            mode = ValidationType.REGEX, message = "10位以内数字", regex = "^\\d{1,10}$")
    private String siteId;

    @FieldName("站点所在区县")
    @FindColumn(value = "站点所在区县", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String county;

    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    @FindColumn(value = "区域", checkColumn = true)
    @FieldName("区域")
    private String area;

    @FindColumn(value = "站点详细地址", checkColumn = true)
    @FieldName("站点详细地址")
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String address;

    @FindColumn(value = "小区或建筑物名称", checkColumn = true)
    @FieldName("小区或建筑物名称")
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String addressName;

    @FindColumn(value = "产权单位", checkColumn = true)
    @FieldName("产权单位")
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String proprietor;

    @FindColumn(value = "楼宇数量", checkColumn = true)
    @FieldName("楼宇数量")
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, message = "数字格式", regex = "^\\d*$")
    })
    private String size;

    @FindColumn(value = "覆盖区域", checkColumn = true)
    @FieldName("覆盖区域")
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)
    }, check = true)
    private String coverRange;

    @FieldName("建筑面积（万平米）")
    @FindColumn(value = "建筑面积\n（万平米）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "coverage", message = "建筑面积应≥覆盖面积", mode = ValidationType.GREATER_THAN_OR_EQUAL)}),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String floorage;

    @FieldName("覆盖面积（万平米）")
    @FindColumn(value = "覆盖面积\n（万平米）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(fieldValidation = {@FiledCompare(fieldName = "floorage", message = "覆盖面积应≤建筑面积", mode = ValidationType.GREATER_THAN_OR_EQUAL)}, mode = ValidationType.EXECUTE),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String coverage;

    @FieldName("未覆盖区域说明")
    @FindColumn(value = "未覆盖\n区域说明", checkColumn = true)
    private String uncoverRemarks;

    @FieldName("覆盖功能类型")
    @FindColumn(value = "覆盖功能类型", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"办公区", "居民区", "餐饮场所", "娱乐场所", "商场", "医院", "学校", "体育场馆", "展览中心", "宾馆饭店", "商住", "其他"})
    })
    private String abilityType;

    @FieldName("经度")
    @FindColumn(value = "经度", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{6,})?$", message = "至少保留小数点后六位")
    })//结果要求保留小数点后六位 最后一位为O时进1
    private String longitude;

    @FieldName("纬度")
    @FindColumn(value = "纬度", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{6,})?$", message = "至少保留小数点后六位")
    })//结果要求保留小数点后六位 最后一位为O时进1
    private String latitude;

    @FieldName("分公司")
    @FindColumn(value = "分公司", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"城一", "城二", "城三", "通州",
                    "顺义", "平谷", "密云", "怀柔", "延庆", "昌平", "房山", "大兴", "重点"})
    })
    private String branchOffice;

    @FieldName("网格确认人")
    @FindColumn(value = "网格\n确认人", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String gridHead;

    //根据网络确认人自动匹配对数据
    @FieldName("网格电话")
    private String gridPhone;

    //根据网络确认人自动匹配对数据
    @FieldName("需求\n提出人")
    private String demandInitiation;

    @FieldName("是否提交智能审核平台")
    @FindColumn(value = "是否提交\n智能审核平台", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"是", "否"})
    })
    private String submitSystemState;

    @FieldName("勘察单位")
    @FindColumn(value = "勘察单位", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})
    })
    private String checkOffice;

    @FieldName("勘察单位合作类型")
    @FindColumn(value = "勘察单位合作类型", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"自有", "工时定额", "单项目合作", "多项目合作"}, mode = ValidationType.EQUALS),
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)})
    private String checkOfficeCooperationType;

    @FieldName("勘察人")
    @FindColumn(value = "勘察人", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String checkHead;

    @FieldName("勘察完成日期")
    @FindColumn(value = "勘察\n完成日期", checkColumn = true)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private Date checkEndDate;

    @FieldName("设计单位")
    @FindColumn(value = "设计单位", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"中移北分", "锦程前方", "通畅", "成都海祥", "河北院", "湖北院"})})
    private String designOffice;

    @FieldName("设计单位合作类型")
    @FindColumn(value = "设计单位合作类型", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
            @FiledRequire(fieldName = "engineeringType", value = {"自有", "工时定额", "单项目合作", "多项目合作"}, mode = ValidationType.EQUALS)
    })
    private String designOfficeCooperationType;

    @FieldName("设计人")
    @FindColumn(value = "设计人", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true)
    private String designHead;

    @FieldName("设计人电话")
    @FindColumn(value = "设计人电话", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, message = "11位数字", regex = "^\\d{11,11}$", check = true)
    })
    private String designPhone;

    @FieldName("设计完成日期")
    @FindColumn(value = "设计\n完成日期", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {
                    @FiledCompare(fieldName = "checkEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“勘察完成日期”")}, check = true)

    private Date designEndDate;

    @FieldName("网格审核通过日期")
    @FindColumn(value = "网格审核\n通过日期", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {
                    @FiledCompare(fieldName = "designEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“设计完成日期”")})
    private Date GridEndDate;

    @FieldName("网优审核通过日期")
    @FindColumn(value = "网优审核\n通过日期", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType", value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),}
            , fieldValidation = {
            @FiledCompare(fieldName = "GridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网格审核通过日期”")})
    private String optimumEndDate;

    @FieldName("是否变更方案")
    @FindColumn(value = "是否变更方案", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.EQUALS, value = {"是", "否"}, check = true)
    })
    private String changeSchemeState;

    @FieldName("变更内容")
    @FindColumn(value = "变更内容", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
            @FiledRequire(fieldName = "changeSchemeState", value = "是", mode = ValidationType.EQUALS)}, check = true)
    private String changeContext;

    @FieldName("变更网格审核通过日期")
    @FindColumn(value = "变更网格审核通过日期", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {@FiledCompare(fieldName = "optimumEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“网优审核通过日期”")})

    private Date changeGridEndDate;

    @FieldName("变更网优审核通过日期")
    @FindColumn(value = "变更网优审核通过日期", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            fieldValidation = {@FiledCompare(fieldName = "changeGridEndDate", mode = ValidationType.GREATER_THAN_OR_EQUAL, message = "应晚于或等于“变更网格审核通过日期”")})
    private Date changeOptimumDate;

    @FieldName("计提设计单位")
    @FindColumn(value = "计提设计单位", checkColumn = true)
    private String provisionOffice;

    @FieldName("计提设计单位合作类型")
    @FindColumn(value = "计提设计单位合作类型", checkColumn = true)
    private String provisionCooperationType;

    @FieldName("计提设计人")
    @FindColumn(value = "计提设计人", checkColumn = true)
    private String provisionHead;

    @FieldName("建设网段")
    @FindColumn(value = "建设网段", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"NR-D", "LTE-E", "LTE-F", "LTE-FDD", "LTE-D", "锚点-FDD", "锚点-F", "LTE-E、F", "LTE-E、FDD", "LTE-E、D", "LTE-F、FDD", "LTE-F、D",
                    "LTE-FDD、D", "LTE-E、F、FDD", "LTE-F、FDD、D", "LTE-E、F、FDD、D", "GSM", "DCS", "NB", "WLAN"}, mode = ValidationType.EQUALS)
    })
    private String buildNetworkSegment;

    @FieldName("建设类型")
    @FindColumn(value = "建设类型", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"新建2G", "新建4G", "新建5G", "扩容4G", "扩容5G", "升级4G", "升级5G"}, mode = ValidationType.EQUALS)
    })
    private String buildType;

    @FieldName("建设方式")
    @FindColumn(value = "建设方式", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(value = {"Lampsite升级5G", "Lampsite", "Lightsite", "新建单支路室分", "新建双支路室分", "扩容单支路室分", "扩容双支路室分", "补点单支路室分", "补点双支路室分", "馈入单支路室分", "馈入双支路室分", "新建第二支路室分", "信源扩容",
                    "Book RRU", "京信2G分布式", "京信4G分布式", "京信2G、4G分布式", "京信4G一体化", "京信2G、4G一体化", "MDAS", "WLAN"}, mode = ValidationType.EQUALS)
    })
    private String buildMode;

    @FieldName("5G对应的4G BBU站号")
    @FindColumn(value = "5G对应的\n4G BBU站号", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.REGEX, regex = "^\\d{1,10}$", message = "必须输入10位以内数字")
    private String Buu5gTo4gSiteId;

    @FieldName("5G对应的4G BBU站名")
    @FindColumn(value = "5G对应的\n4G BBU站名", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = ""),
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.EXECUTE, check = true),
    })
    private String Buu5gTo4gSiteName;

    @FieldName("5G对应的4G BBU SN")
    @FindColumn(value = "5G对应的\n4G BBU SN", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
            @FiledRequire(fieldName = "Buu5gTo4gSiteId", mode = ValidationType.NO_EMPTY, value = "")}, mode = ValidationType.REGEX, regex = "^\\d{1,20}$", message = "必须输入20位以内数字", check = true)
    private String Buu5gBuildSN;

    @FieldName("5G BBU建设方式")
    @FindColumn(value = "5G BBU建设方式", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
                    value = {"新装", "直接升级", "换框升级"}, mode = ValidationType.EQUALS),
            @FiledValidator(require = {@FiledRequire(fieldName = "engineeringType", value = {"5G微蜂窝信源工程"}, mode = ValidationType.EQUALS)}, check = true),
    })
    private String Buu5gBuildMode;

    @FieldName("5G组网模式")
    @FindColumn(value = "5G\n组网模式", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
            @FiledRequire(fieldName = "engineeringType", mode = ValidationType.EQUALS, value = "5G微蜂窝信源工程")
    }, value = {"SA", "NSA"}, mode = ValidationType.EQUALS)
    private String networkMode5g;

    @FieldName("框架")
    @FindColumn(value = "框架", checkColumn = true)
    private String framework;

    @FieldName("施工折扣")//^[1-9][0-9]$
    @FindColumn(value = "施工折扣", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.INTEGER, message = "百分数形式，无小数点")
    private String constructionDiscount;

    @FieldName("勘察费（含税）（元）")//^(\-)?\d+(\.\d{2,2})$
    @FindColumn(value = "勘察费\n（含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String checkCost;

    @FieldName("设计费（含税）（元）")
    @FindColumn(value = "设计费\n（含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String designCost;

    @FieldName("施工费（不含安全生产费）（不含税）（元）")
    @FindColumn(value = "施工费\n（不含安全生产费）\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String constructionCost;

    @FieldName("安全生产费（不含税）（元）")
    @FindColumn(value = "安全生产费\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String safeProductionCost;

    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    @FieldName("设备费 （不含税）（元）")
    @FindColumn(value = "设备费\n（不含税）\n（元）", checkColumn = true)
    private String deviceCost;

    @FieldName("建安费（不含税）（元）")
    @FindColumn(value = "建安费\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String buildSafeCost;


    @FieldName("其他费（不含税）（元）")
    @FindColumn(value = "其他费\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String otherCost;

    @FieldName("预备费（不含税）（元）")
    @FindColumn(value = "预备费\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String prepareCost;

    @FieldName("总投资（不含税）（元）")
    @FindColumn(value = "总投资\n（不含税）\n（元）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.DOUBLE, message = "数值格式")
    })
    private String investCountCost;

    @FieldName("施工厂家")
    @FindColumn(value = "施工厂家", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"华为", "工程局", "中通一局", "浙江邮电", "中移建设"}, mode = ValidationType.EQUALS, check = true)
    private String constructionOffice;

    @FieldName("施工厂家联系人")
    @FindColumn(value = "施工厂家\n联系人", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.NO_EMPTY, message = "不能为空")
    })
    private String constructionHead;

    @FieldName("施工厂家联系电话")
    @FindColumn(value = "施工厂家\n联系人电话", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{11,11}$", message = "手机号码必须11位数字")
    })
    private String constructionPhone;

    @FieldName("监理单位")
    @FindColumn(value = "监理单位", checkColumn = true)
    private String supervisorOffice;

    @FieldName("信源设备厂家")
    @FindColumn(value = "信源设备厂家", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"诺西", "华为", "中兴", "京信", "爱立信"}, mode = ValidationType.EQUALS, check = true)
    private String sourceDeviceOffice;

    @FieldName("设备原配置")//^((([O][0-9]\+)*([O][0-9]))|[0])$
    @FindColumn(value = "设备原配置", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    private String deviceSourceConfig;

    @FieldName("设备配置")
    @FindColumn(value = "设备配置", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^((([O][0-9]\\+)*([O][0-9]))|[0])$", message = "每数字间用“+”分隔数字前面写字母“O”，或者写一个数字“0”")
    })
    private String deviceConfig;

    @FieldName("分区工具小区数量（Lampsite填写）")
    @FindColumn(value = "分区工具小区数量（Lampsite填写）", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private String partitionToolCount;

    @FieldName("分区数")//^\d*$
    @FindColumn(value = "分区数", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    private String partitionCount;

    @FieldName("分区说明")
    @FindColumn(value = "分区说明", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String partitionExplain;

    @FieldName("机房形式")
    @FindColumn(value = "机房形式", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"微蜂窝共享业主机房", "微蜂窝独立机房"}, mode = ValidationType.EQUALS, check = true)
    private String equipmentRoomMode;

    @FieldName("基站类型")
    @FindColumn(value = "基站类型", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"常规-独立机房", "常规-非独立机房"}, mode = ValidationType.EQUALS, check = true)
    private String siteType;

    @FieldName("机房面积（平米）")
    @FindColumn(value = "机房面积\n（平米）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String equipmentRoomModeArea;

    @FieldName("机房高度（米）")
    @FindColumn(value = "机房高度\n（米）", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d+(\\.\\d{1,})?$", message = "至少保留一位小数点或整数")
    })
    private String equipmentRoomModeHeight;

    @FieldName("共站信息")
    @FindColumn(value = "共站信息", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String sitePublicInfo;

    @FieldName("BBU数量")
    @FindColumn(value = "BBU数量", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String bbuCount;

    @FieldName("BBU位置")
    @FindColumn(value = "BBU位置", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String bbuAddress;

    @FieldName("RRU rHUB SW数量")
    @FindColumn(value = "RRU\nrHUB\nSW\n数量", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
            @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    })
    private String RruRHubSwCount;

    @FieldName("RRU rHUB SW位置")
    @FindColumn(value = "RRU\nrHUB\nSW\n位置", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String RruRHubSwAddress;

    @FieldName("pRRU DP数量")
    @FindColumn(value = "pRRU\nDP\n数量", checkColumn = true)
    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private String pRruDpCount;


    @FieldName("传统室分天线数量")
    @FindColumn(value = "传统室分天线数量", checkColumn = true)
    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d*$", message = "数值格式")
    private String distributedSystemAerialCount;

    @FieldName("GPS同步方式")
    @FindColumn(value = "GPS同步方式", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}
            , value = {"新建GPS", "利旧GPS", "PTN同步"}, mode = ValidationType.EQUALS, check = true)
    private String gpsSynchronousMode;

    @FieldName("信源产值上报日期")//^\d{4}-\d{1,2}-\d{1,2}
    @FindColumn(value = "信源产值上报日期", checkColumn = true)
    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    private String sourceDeviceProduceSubmitDate;

    @FieldName("分布产值上报日期")
    @FindColumn(value = "分布产值上报日期", checkColumn = true)
    @FiledValidator(mode = ValidationType.REGEX, regex = "^\\d{4}-\\d{1,2}-\\d{1,2}", message = "日期格式年-月-日")
    private String distributedProduceSubmitDate;

    @FieldName("天线点是否入户覆盖")
    @FindColumn(value = "天线点是否入户覆盖", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    private String fromIndoorState;

    @FieldName("如为居民楼是否有室分外打")
    @FindColumn(value = "如为居民楼是否有室分外打", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, value = {"是", "否"}, mode = ValidationType.EQUALS, check = true)
    private String outsideState;

    @FieldName("如无室分外打，请说明理由")
    @FindColumn(value = "如无室分外打，请说明理由", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)}, check = true),
    })
    private String outsideExplain;

    @FieldName("是否模测")
    @FindColumn(value = "是否模测", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, mode = ValidationType.EQUALS, value = {"是", "否"}, message = "是能填入 是 否", check = true),
    })
    private String testState;

    @FieldName("未模测原因")
    @FindColumn(value = "未模测原因", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "testState", value = "否", mode = ValidationType.EQUALS),
            }, check = true),
    })
    private String nuTestExplain;

    @FieldName("使用套管长度")
    @FindColumn(value = "使用套管长度", checkColumn = true)
    private String pipeHeight;

    @FieldName("是否涉及整改流程（四方上站）")
    @FindColumn(value = "是否涉及整改流程（四方上站）", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    private String processState;
    @FieldName("是否建设光交箱")
    @FindColumn(value = "是否建设光交箱", checkColumn = true)
    @FiledValidator(require = {
            @FiledRequire(fieldName = "engineeringType",
                    value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS)},
            value = {"否", "是"}, mode = ValidationType.EQUALS, check = true)
    private String buildRayBoxState;

    @FieldName("未建设原因")
    @FindColumn(value = "未建设原因", checkColumn = true)
    @FiledValidators({
            @FiledValidator(require = {
                    @FiledRequire(fieldName = "engineeringType",
                            value = {"WLAN信源工程", "WLAN分布工程", "微蜂窝大修理工程", "微蜂窝信源扩容工程"}, mode = ValidationType.NO_EQUALS),
                    @FiledRequire(fieldName = "buildRayBoxState", value = "否", mode = ValidationType.EQUALS),
            }, value = {"Lampsite", "Book RRU", "Femto", "分布扩容", "分布改造", "无需建设", "分布馈入", "党建箱", "Qcell", "无法建设", "Lightsite", "分布系统改造", "Lampsite升级"},
                    mode = ValidationType.EQUALS, check = true),
    })
    private String unBuildRayBoxExplain;

    @FieldName("备注")
    @FindColumn(value = "备注", checkColumn = true)
    private String remarks;
}

