package run.cmid.common.compute.core;

import java.util.List;
import java.util.Map;

import run.cmid.common.compute.enums.LinkState;
import run.cmid.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-04-15 01:46:39
 */
public interface ComputeDetail {
    /**
     * 查找下一个fieldDetail对象属性<br>
     * 通过fieldName查找对应Get方法。无法找到时抛出空指针异常
     */
    ComputeDetail find(String fieldName);

    /**
     * 获取Object对象
     */
    Object getObject();

    Object getParentObject();

    Map<String, ComputeDetail> getMap();

    Map<String, Object> getMapContext();

    SpotPath getSpotPath();

    String getComputeValue();

    void setComputeValue(String value);

    String getComputeValueFieldName();

    LinkState getState();

    void setState(LinkState state);

    List<String> getComputeValues();

    Object getValues();

    List<String> getParentNameList();

    void clear();
}
