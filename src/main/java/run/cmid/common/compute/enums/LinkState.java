package run.cmid.common.compute.enums;

/**
 * NOT_COMPUTE 不参与计算的数据 包括String无法转换成Double的数据<br>
 * COMPUTE 带有运算的数据<br>
 * DATA 可以直接参与计算的数据<br>
 * COMPUTE_DATA 可以计算出结果的数据
 *
 * @author leichao
 * @date 2020-04-20 01:44:25
 */
public enum LinkState {
    NOT_COMPUTE, COMPUTE, DATA, COMPUTE_DATA;
}
