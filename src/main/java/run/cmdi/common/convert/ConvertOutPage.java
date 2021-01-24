package run.cmdi.common.convert;

import run.cmdi.common.compare.model.DataArray;

import java.util.List;

/**
 * @param <OUT> 输出数据类型
 *              // * @param <INT> 输入数据类型
 */
public interface ConvertOutPage<OUT> {
    OUT getValues(Integer index);

    List<OUT> getAll();

    int size();

}
