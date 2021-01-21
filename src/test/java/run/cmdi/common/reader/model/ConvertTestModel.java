package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.annotations.FindColumns;

import java.util.Date;
import java.util.List;

@ConverterHead(maxWrongCount = 1)
@Getter
@Setter
public class ConvertTestModel {
    @FindColumn("告警开始时间")
    private Date startTime;
    @FindColumn("告警结束时间")
    private Date endTime;
    @FindColumn("告警名称")
    private String faultName;
//    @FindColumn("告警号")
//    private String faultId;
//    @FindColumn("网元名称")
//    private String siteName;

    @FindColumns({@FindColumn("网元名称"),@FindColumn("告警号")})
    private List<String> list;
}
