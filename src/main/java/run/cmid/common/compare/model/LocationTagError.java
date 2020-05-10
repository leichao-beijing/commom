package run.cmid.common.compare.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @param <EX> Throwable
 * @param <S>  匹配到的Src数据类型
 * @author leichao
 */
@Getter
@Setter
@ToString(callSuper = true)
public class LocationTagError<S, EX extends Throwable> {
    public LocationTagError(LocationTag<S> s1, EX ex) {
        this.s1 = s1;
        this.ex = ex;
    }

    private final EX ex;
    private final LocationTag<S> s1;
}
