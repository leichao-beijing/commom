package run.cmid.common.compare.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @param <D> 匹配到Des数据类型
 * @param <S> 匹配到的Src数据类型
 * @author leichao
 */
@Getter
@Setter
@ToString(callSuper = true)
public class CompareResponse<S, D> {
    public CompareResponse(int srcIndex, int desIndex, S srcData, D desData) {
        this.desData = desData;
        this.srcData = srcData;
        this.srcIndex = srcIndex;
        this.desIndex = desIndex;
    }

    private final int srcIndex;
    private final int desIndex;
    private final S srcData;
    private final D desData;
}
