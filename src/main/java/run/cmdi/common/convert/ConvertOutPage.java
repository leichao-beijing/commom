package run.cmdi.common.convert;

import java.util.List;

/**
 * @param <OUT> 输出数据类型
 */
public interface ConvertOutPage<OUT> {
    OUT getValues(Integer index);

    List<OUT> getAll();

    int size();

}
