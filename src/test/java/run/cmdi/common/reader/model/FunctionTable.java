package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmdi.common.reader.annotations.ConverterHead;
import run.cmdi.common.reader.annotations.FindColumn;
import run.cmdi.common.reader.model.eumns.FindModel;

@ToString
@Getter
@Setter
@ConverterHead
public class FunctionTable {
    //
    @FindColumn(value = {"客户需求"}, checkColumn = true)
    private String needName;

    @FindColumn(value = {"功能用户"}, checkColumn = true)
    private String functionUser;// 功能用户

    @FindColumn(value = {"功能用户需求"}, checkColumn = true)
    private String userNeed;// 功能用户需求

    @FindColumn(value = {"触发事件"}, checkColumn = true)
    private String touchEvent;// 触发事件

    @FindColumn(value = {"功能过程"}, checkColumn = true)
    private String functionProcessDescription;// 功能过程

    @FindColumn(value = {"子过程描述"}, checkColumn = true)
    private String subProcessDescription;// 子过程描述

    @FindColumn(value = {"数据移动类型"}, checkColumn = true)
    private String mobileType;// 数据移动类型

    @FindColumn(value = {"数据组"}, checkColumn = true)
    private String dataArray;// 数据组

    @FindColumn(value = {"数据属性"}, checkColumn = true)
    private String dataAttribute;// 数据属性

    @FindColumn(value = {"cfp", "CFP"}, checkColumn = true)
    private String cfp;// CFP

    @FindColumn(value = {"人工审核"}, model = FindModel.INCLUDE)
    private String tagValue;
}
