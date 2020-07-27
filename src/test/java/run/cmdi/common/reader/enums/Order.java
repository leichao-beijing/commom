package run.cmdi.common.reader.enums;

import lombok.Getter;
import run.cmdi.common.io.TypeName;

/**
 * @author leichao
 * @date 2020-04-04 04:17:01
 */
@Getter
public enum Order implements TypeName {

    FILE("输入的完整XLSX文件目录地址"),

    COMPUTE("更新没有计算的产值表与项目表"),

    COMPUTE_ALL("更新所有的产值表与项目表"),

    SHOW_TIME("显示数据录入时间表"),

    DOWNLOAD_DATA("根据表名与时间导出指定数据");

    // LINK("对项目表和产值表进行关联，导出报出未关联数据");

    // DOWNLOAD_DATA_ALL("导出所有数据");

    Order(String typeName) {
        this.typeName = typeName;
    }

    String typeName;
}
