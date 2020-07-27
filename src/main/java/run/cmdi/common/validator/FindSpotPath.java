package run.cmdi.common.validator;

import run.cmdi.common.utils.SpotPath;

/**
 * @author leichao
 * @date 2020-05-02 10:52:52
 */
public interface FindSpotPath<T> {
    T find(SpotPath path);
}
