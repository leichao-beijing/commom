package run.cmdi.common.poi.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReaderPoiConfig {
//    //是否对合并单元格进行解析读取。默认不进行读取
//    private boolean cellRangeState = false;
    //默认从信息头后一行进行数据读取
    private boolean startRow = true;
//    //数据读取默认起始行
//    private int startRowNum = 0;
    //对验证出现问题行数据进行跳过，不进行实例化.不生成结果对象
    private boolean skipErrorResult = false;
}
