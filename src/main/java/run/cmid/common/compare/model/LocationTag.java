package run.cmid.common.compare.model;

import java.util.HashSet;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @param <R> 匹配到Des的数据类型
 * @author leichao
 */
@Getter
@Setter
@ToString
public class LocationTag<R> extends Tag {
    public LocationTag(int rownum, R value) {
        super(rownum);
        this.value = value;
    }
    //存在本行内，filed为空的对象
    private HashSet<String> setFiledNull = new HashSet<String>();
    private final R value;
}
