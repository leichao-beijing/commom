package run.cmid.common.excel.model.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import run.cmid.common.excel.model.FieldDetail;

/**
 * @param <T> 读取原型的数据类型泛型
 * @author leichao
 */
@Getter
@Setter
@ToString
public class FieldDetailError<T> {

    public FieldDetailError(FieldDetail t, String errorMessage) {
        this.t = t;
        this.errorMessage = errorMessage;
    }

    private final String errorMessage;
    private final FieldDetail t;
}
