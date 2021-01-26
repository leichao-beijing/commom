package run.cmdi.common.compare;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;

import run.cmdi.common.compare.model.CompareResponse;
import run.cmdi.common.compare.model.LocationTag;
import run.cmdi.common.compare.model.LocationTagError;
import run.cmdi.common.compare.model.CompareState;
import run.cmdi.common.compare.model.QepeatResponse;
import run.cmdi.common.reader.model.FieldDetailOld;
import run.cmdi.common.reader.model.entity.CompareResponseAndErrorList;

/**
 * @author leichao
 * @date 2019年12月3日--上午9:05:49
 */
public class Compares {
    /**
     * 将list内重复数据和重复数据的对象位置索引返回list
     */
    public static <S, X1 extends Throwable> List<QepeatResponse> repeatData(List<S> s1, Function<S, String> fun)
            throws X1 {
        List<QepeatResponse> qepeatList = new ArrayList<QepeatResponse>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        int size = s1.size();
        for (int i = 0; i < size; i++) {
            S s = s1.get(i);
            if (s == null)
                continue;
            String key = fun.apply(s);
            Integer value = map.get(key);
            if (value == null)
                map.put(key, i);
            else
                qepeatList.add(new QepeatResponse(value, i));
        }
        return qepeatList;
    }

    /**
     * 将list内重复数据和重复数据的对象位置索引返回list。重复数据的对象也写入List中，但QepeatResponse.qepeatIndex=-1
     */
    public static <S> List<QepeatResponse> repeatDataAll(List<S> s1, Function<S, String> fun
    ) {
        List<QepeatResponse> qepeatList = new ArrayList<QepeatResponse>();
        Map<String, Integer> map = new HashMap<String, Integer>();
        int size = s1.size();
        for (int i = 0; i < size; i++) {
            S s = s1.get(i);
            if (s == null)
                continue;
            String key = fun.apply(s);
            if (key == null)
                continue;
            Integer value = map.get(key);
            if (value == null) {
                map.put(key, i);
            } else
                qepeatList.add(new QepeatResponse(value, i));
        }
        map.clear();
        Set<Integer> set = new HashSet<Integer>();
        for (QepeatResponse qepeat : qepeatList) {
            if (!set.contains(qepeat.getQepeatIndex()))
                set.add(qepeat.getQepeatIndex());
        }
        for (Integer integer : set) {
            qepeatList.add(new QepeatResponse(-1, integer));
        }
        return qepeatList;
    }

    /**
     * 包含关系回调函数返回 S1 对象List
     *
     * @param <S1>       原始数据类型
     * @param <D1>       目标数据类型
     * @param s1         需匹配的原始数据
     * @param d1         需匹配的目标数据
     * @param biFunction 判断回调true加入队列
     * @return 返回 S1 对象List
     */
    public static <S1, D1> List<CompareResponse<S1, D1>> toLists(List<S1> s1, List<D1> d1,
                                                                 BiFunction<LocationTag<S1>, LocationTag<D1>, Boolean> biFunction) {
        return toLists(s1, d1, biFunction, null);
    }

    /**
     * 返回匹配成功的S1数据类型的list 位置信息由list所有索引决定
     *
     * @param <S1>              原始数据类型
     * @param <D1>              目标数据类型
     * @param <X1>              自定义异常回调
     * @param srcList           原始数据类型
     * @param desList           目标数据类型
     * @param biFunction        true添加结果队列 false 跳过
     * @param exceptionSupplier src与des进行匹配时，当src未能匹配到对应的des数据时。触发该回调函数 返回异常处理函数
     * @return 返回匹配成功的S1数据
     * @throws <X1> 自定义返回异常
     */
    public static <S1, D1, X1 extends Throwable> List<CompareResponse<S1, D1>> toLists(List<S1> srcList,
                                                                                       List<D1> desList, BiFunction<LocationTag<S1>, LocationTag<D1>, Boolean> biFunction,
                                                                                       Function<CompareState<S1>, X1> exceptionSupplier) throws X1 {
        LinkedList<CompareResponse<S1, D1>> list = new LinkedList<CompareResponse<S1, D1>>();
        LocationTag<S1> srcTag = null;
        LocationTag<D1> desTag = null;
        List<CompareState<D1>> desCompareStateList = toCompareState(desList);
        List<CompareState<S1>> srcCompareStateList = toCompareState(srcList);
        CompareState<S1> secCompareState;
        CompareState<D1> desCompareState;
        for (int i = 0; i < srcCompareStateList.size(); i++) {
            secCompareState = srcCompareStateList.get(i);
            S1 src = secCompareState.getValue();
            srcTag = new LocationTag<S1>(i, src);
            for (int a = 0; a < desCompareStateList.size(); a++) {
                desCompareState = desCompareStateList.get(a);
                if (desCompareState.isState())
                    continue;
                D1 d = desCompareState.getValue();
                desTag = new LocationTag<D1>(a, d);
                if (biFunction.apply(srcTag, desTag)) {
                    list.add(new CompareResponse<S1, D1>(i, a, src, d));
                    desCompareStateList.get(a).setState(true);
                    secCompareState.setState(true);
                    break;
                }
            }
            if (exceptionSupplier != null && !secCompareState.isState()) {
                X1 ex = exceptionSupplier.apply(secCompareState);
                if (ex != null)
                    throw ex;
            }
        }
        return list;
    }

    /**
     * 返回匹配成功的S1数据类型的list 位置信息由CompareResponse.tag决定
     *
     * @param <S1>              原始数据类型
     * @param <D1>              目标数据类型
     * @param <X1>              自定义异常回调
     * @param srcList           原始数据类型
     * @param desList           目标数据类型
     * @param biFunction        return true添加结果队列 false 跳过
     * @param exceptionSupplier src与des进行匹配时，当src未能匹配到对应的des数据时。触发该回调函数 返回异常处理函数
     * @return 返回匹配成功的S1数据
     * @throws X1 自定义返回异常
     */
    public static <S1, D1, X1 extends Throwable> List<CompareResponse<S1, D1>> toListsLocationTag(
            List<LocationTag<S1>> srcList, List<LocationTag<D1>> desList,
            BiFunction<LocationTag<S1>, LocationTag<D1>, Boolean> biFunction,
            Function<CompareState<LocationTag<S1>>, X1> exceptionSupplier) throws X1 {

        LinkedList<CompareResponse<S1, D1>> list = new LinkedList<CompareResponse<S1, D1>>();

        List<CompareState<LocationTag<D1>>> desCompareStateList = toCompareState(desList);
        List<CompareState<LocationTag<S1>>> srcCompareStateList = toCompareState(srcList);

        CompareState<LocationTag<S1>> secCompareState;
        CompareState<LocationTag<D1>> desCompareState;
        for (int i = 0; i < srcCompareStateList.size(); i++) {
            secCompareState = srcCompareStateList.get(i);

            for (int a = 0; a < desCompareStateList.size(); a++) {
                desCompareState = desCompareStateList.get(a);
                if (desCompareState.isState())
                    continue;

                if (biFunction.apply(secCompareState.getValue(), desCompareStateList.get(a).getValue())) {
                    list.add(new CompareResponse<S1, D1>(secCompareState.getValue().getPosition(),
                            desCompareStateList.get(a).getValue().getPosition(), secCompareState.getValue().getValue(),
                            desCompareState.getValue().getValue()));

                    desCompareStateList.get(a).setState(true);
                    secCompareState.setState(true);
                    break;
                }
            }
            if (exceptionSupplier != null && !secCompareState.isState()) {
                X1 ex = exceptionSupplier.apply(secCompareState);
                if (ex != null)
                    throw ex;
            }
        }
        return list;
    }

    /**
     * 返回匹配成功的S1数据类型的list 位置信息由CompareResponse.tag决定 and 返回错误信息列表
     *
     * @param <S1>              原始数据类型
     * @param <D1>              目标数据类型
     * @param <EX>              自定义异常回调
     * @param srcList           原始数据类型
     * @param desList           目标数据类型
     * @param biFunction        return true添加结果队列 false 跳过
     * @param exceptionSupplier src与des进行匹配时，当src未能匹配到对应的des数据时。触发该回调函数 返回异常处理函数
     * @return 返回匹配成功的S1数据
     */
    public static <S1, D1, EX extends Throwable> CompareResponseAndErrorList<S1, D1, EX> toListsLocationTagToErrorList(
            List<LocationTag<S1>> srcList, List<LocationTag<D1>> desList,
            BiFunction<LocationTag<S1>, LocationTag<D1>, Boolean> biFunction,
            Function<CompareState<LocationTag<S1>>, EX> exceptionSupplier) throws EX {

        List<CompareResponse<S1, D1>> list = new LinkedList<CompareResponse<S1, D1>>();
        List<LocationTagError<S1, EX>> errorList = new LinkedList<LocationTagError<S1, EX>>();

        List<CompareState<LocationTag<D1>>> desCompareStateList = toCompareState(desList);
        List<CompareState<LocationTag<S1>>> srcCompareStateList = toCompareState(srcList);

        CompareState<LocationTag<S1>> srcCompareState;
        CompareState<LocationTag<D1>> desCompareState;
        for (int i = 0; i < srcCompareStateList.size(); i++) {
            srcCompareState = srcCompareStateList.get(i);
            if (srcCompareState.getValue().getValue().getClass().isAssignableFrom(FieldDetailOld.class)) {
                FieldDetailOld f = (FieldDetailOld) srcCompareState.getValue().getValue();
            }
            for (int a = 0; a < desCompareStateList.size(); a++) {
                desCompareState = desCompareStateList.get(a);
                if (desCompareState.isState())
                    continue;

                if (biFunction.apply(srcCompareState.getValue(), desCompareStateList.get(a).getValue())) {
                    list.add(new CompareResponse<S1, D1>(srcCompareState.getValue().getPosition(),
                            desCompareStateList.get(a).getValue().getPosition(), srcCompareState.getValue().getValue(),
                            desCompareState.getValue().getValue()));
                    desCompareStateList.get(a).setState(true);
                    srcCompareState.setState(true);
                    break;
                }
            }
            if (exceptionSupplier != null && !srcCompareState.isState()) {
                EX ex = exceptionSupplier.apply(srcCompareState);
                if (ex != null)
                    errorList.add(new LocationTagError<S1, EX>(srcCompareState.getValue(), ex));
            }
        }
        return new CompareResponseAndErrorList<S1, D1, EX>(list, errorList);
    }

    /**
     * 将数据转换成带状态标记的数据
     *
     * @param <D>    数据原始类型
     * @param desLis 数据List
     * @return 带状态标记的数据
     */
    public static <D> List<CompareState<D>> toCompareState(List<D> desLis) {
        List<CompareState<D>> list = new ArrayList<CompareState<D>>();
        desLis.forEach((v) -> {
            list.add(new CompareState<D>(v));
        });
        return list;
    }

    /**
     * 将数据转换成带状态标记的数据
     *
     * @param <S1>        原始数据类型
     * @param <D1>        目标数据类型
     * @param s1          匹配原始单数据
     * @param srcIndex    sl 数据索引为止，生成CompareResponse对象需要
     * @param deses       目标数据List LocationTag 带有位置数据的参数
     * @param desFunction 回调函数返回true时为匹配到的数据
     * @return 带状态标记的数据
     */
    public static <S1, D1> CompareResponse<S1, D1> toList(S1 s1, int srcIndex, List<D1> deses,
                                                          BiFunction<Integer, D1, Boolean> desFunction) {
        for (int i = 0; i < deses.size(); i++) {
            if (desFunction.apply(i, deses.get(i)))
                return new CompareResponse<S1, D1>(srcIndex, i, s1, deses.get(i));
        }
        return null;
    }

    /**
     * 将数据转换成带状态标记的数据 数据必须全部匹配，失败将会返回无法匹配的目标数据列表
     *
     * @param <S1>        原始数据类型
     * @param <D1>        目标数据类型
     * @param s1          匹配原始单数据
     * @param srcIndex    sl 数据索引为止，生成CompareResponse对象需要
     * @param deses       目标数据List LocationTag 带有位置数据的参数
     * @param desFunction 回调函数返回true时为匹配到的数据
     * @return 带状态标记的数据
     */
    public static <S1, D1> CompareResponse<S1, D1> toListAll(S1 s1, int srcIndex, List<D1> deses,
                                                             BiFunction<Integer, D1, Boolean> desFunction) {
        for (int i = 0; i < deses.size(); i++) {
            if (desFunction.apply(i, deses.get(i)))
                return new CompareResponse<S1, D1>(srcIndex, i, s1, deses.get(i));
        }
        return null;
    }

    /**
     * 将数据转换成带状态标记的数据
     *
     * @param <S1>        原始数据类型
     * @param <D1>        目标数据类型
     * @param s1          匹配原始单数据
     * @param deses       目标数据List LocationTag 带有位置数据的参数
     * @param desFunction 回调函数返回true时为匹配到的数据
     * @return 带状态标记的数据
     */
    public static <S1, D1> CompareResponse<S1, D1> toListLocationTag(S1 s1, int srcIndex, List<LocationTag<D1>> deses,
                                                                     BiFunction<Integer, LocationTag<D1>, Boolean> desFunction) {
        for (int i = 0; i < deses.size(); i++) {
            if (desFunction.apply(i, deses.get(i)))
                return new CompareResponse<S1, D1>(srcIndex, deses.get(i).getPosition().intValue(), s1,
                        deses.get(i).getValue());
        }
        return null;
    }
}
