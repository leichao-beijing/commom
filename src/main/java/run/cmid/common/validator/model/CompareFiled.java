package run.cmid.common.validator.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.eumns.CompareType;

/**
 * filedName or  filedName 数据比较
 */
@Getter
@Setter
public class CompareFiled {
    String fieldName;
    String name;
    String message = "";
    CompareType mode;
}
