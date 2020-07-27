package run.cmdi.common.compare;

import java.util.List;
import java.util.function.Function;

import run.cmdi.common.compare.model.CompareResponse;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.compare.model.CompareState;

/**
 * 传入两个List对象通过回调函数对List内容进行比较返回对应新的List集合对象
 *
 * @author leichao
 */
public class CompareList<S, D> {
    private List<S> srcList;
    private List<D> desList;

    public CompareList(List<S> srcList, List<D> desList) {
        this.srcList = srcList;
        this.desList = desList;
    }

    /**
     * 等于关系回调函数
     *
     * @param srcFun S 对象字符串回调函数
     * @param desFun D 对象字符串回调函数
     * @param <X>    自定义返回异常对象
     * @return 返回 S 对象List
     * @throws X 自定义返回异常
     */
    public <X extends Throwable> List<CompareResponse<S, D>> equalsToListToSrc(Function<LocationTag<S>, String> srcFun,
                                                                               Function<LocationTag<D>, String> desFun) throws X {
        return equalsToListToSrc(srcFun, desFun, null);
    }

    /**
     * 等于关系回调函数
     *
     * @param <X>               自定义异常
     * @param srcFun            S 对象字符串回调函数
     * @param desFun            D 对象字符串回调函数
     * @param exceptionSupplier 未匹配导数据时，可定义回调抛出异常
     * @return 返回 S 对象List
     * @throws X 自定义返回异常
     */
    public <X extends Throwable> List<CompareResponse<S, D>> equalsToListToSrc(Function<LocationTag<S>, String> srcFun,
                                                                               Function<LocationTag<D>, String> desFun, Function<CompareState<S>, X> exceptionSupplier) throws X {
        return Compares.toLists(srcList, desList, (s, d) -> {
            if (srcFun.apply(s).equals(desFun.apply(d))) {
                return true;
            }
            return false;
        }, exceptionSupplier);
    }

    /**
     * 包含关系回调函数
     *
     * @param <X>    自定义异常
     * @param srcFun S 对象字符串回调函数
     * @param desFun D 对象字符串回调函数
     * @return 返回 D 对象List
     * @throws X 自定义返回异常
     */
    public <X extends Throwable> List<CompareResponse<D, S>> equalsToListToDes(Function<LocationTag<S>, String> srcFun,
                                                                               Function<LocationTag<D>, String> desFun) throws X {
        return equalsToListToDes(srcFun, desFun, null);
    }

    /**
     * 包含关系回调函数
     *
     * @param <X>               自定义异常
     * @param srcFun            S 对象字符串回调函数
     * @param desFun            D 对象字符串回调函数
     * @param exceptionSupplier 未匹配导数据时，可定义回调抛出异常
     * @return 返回 D 对象List
     * @throws X 自定义异常
     */
    public <X extends Throwable> List<CompareResponse<D, S>> equalsToListToDes(Function<LocationTag<S>, String> srcFun,
                                                                               Function<LocationTag<D>, String> desFun, Function<CompareState<D>, X> exceptionSupplier) throws X {
        return Compares.toLists(desList, srcList, (d, s) -> {
            if (srcFun.apply(s).equals(desFun.apply(d))) {
                return true;
            }
            return false;
        }, exceptionSupplier);
    }

    /**
     * 包含关系回调函数
     *
     * @param <X>    自定义异常
     * @param srcFun S 对象字符串回调函数
     * @param desFun D 对象字符串回调函数
     * @return 返回 S 对象List
     * @throws X 自定义异常
     */
    public <X extends Throwable> List<CompareResponse<S, D>> includeToListToSrc(Function<LocationTag<S>, String> srcFun,
                                                                                Function<LocationTag<D>, String> desFun) throws X {
        return includeToListToSrc(srcFun, desFun, null);
    }

    /**
     * 包含关系回调函数
     *
     * @param <X>               自定义异常
     * @param srcFun            S 对象字符串回调函数
     * @param desFun            D 对象字符串回调函数
     * @param exceptionSupplier 未匹配导数据时，可定义回调抛出异常
     * @return 返回 S 对象List
     * @throws X 自定义异常
     */
    public <X extends Throwable> List<CompareResponse<S, D>> includeToListToSrc(Function<LocationTag<S>, String> srcFun,
                                                                                Function<LocationTag<D>, String> desFun, Function<CompareState<S>, X> exceptionSupplier) throws X {
        return Compares.toLists(srcList, desList, (s, d) -> {
            if (desFun.apply(d).indexOf(srcFun.apply(s)) != -1) {
                return true;
            }
            return false;
        }, exceptionSupplier);
    }

    /**
     * 包含关系回调函数返回 D 对象List
     *
     * @param <X>    自定义异常
     * @param srcFun S 对象字符串回调函数
     * @param desFun D 对象字符串回调函数
     * @return 返回 D 对象List
     * @throws X 自定义异常
     */
    public <X extends Throwable> List<CompareResponse<D, S>> includeToListToDes(Function<LocationTag<S>, String> srcFun,
                                                                                Function<LocationTag<D>, String> desFun) throws X {
        return includeToListToDes(srcFun, desFun, null);
    }

    /**
     * 包含关系回调函数返回 D 对象List
     *
     * @param <X>               自定义异常
     * @param srcFun            S 对象字符串回调函数
     * @param desFun            D 对象字符串回调函数
     * @param exceptionSupplier 未匹配导数据时，可定义回调抛出异常
     * @return 返回 D 对象List
     * @throws X 自定义异常
     */
    public <X extends Throwable> List<CompareResponse<D, S>> includeToListToDes(Function<LocationTag<S>, String> srcFun,
                                                                                Function<LocationTag<D>, String> desFun, Function<CompareState<D>, X> exceptionSupplier) throws X {
        return Compares.toLists(desList, srcList, (d, s) -> {
            if (desFun.apply(d).indexOf(srcFun.apply(s)) != -1) {
                return true;
            }
            return false;
        }, exceptionSupplier);
    }

}
