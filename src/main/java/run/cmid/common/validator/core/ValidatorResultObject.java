package run.cmid.common.validator.core;

import run.cmid.common.reader.core.MatchValidator;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.ResultObjectInterface;
import run.cmid.common.validator.eumns.ValidationType;
import run.cmid.common.validator.eumns.ValidatorErrorType;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.validator.exception.ValidatorFieldsException;
import run.cmid.common.validator.model.MachModelInfo;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.ValidatorFieldException;
import run.cmid.common.validator.plugins.ReaderPluginsInterface;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorResultObject<T> implements ResultObjectInterface<T, List<MatchesValidation>, MachModelInfo> {
    private final List<ReaderPluginsInterface> plugins;

    public ValidatorResultObject(List<ReaderPluginsInterface> plugins) {
        this.plugins = plugins;
    }

    @Override
    public void update(T t, Map<SpotPath, List<MatchesValidation>> map) {

    }

    @Override
    public void addInfo(T t, List<MachModelInfo> list, Map<SpotPath, List<MatchesValidation>> map) {
        HashMap<String, Object> result = new HashMap<>();
        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            Object computeValue = ReflectLcUtils.getObjectValue(t, field.getName());
            result.put(field.getName(), computeValue);
        }
        list.add(new MachModelInfo(result, map));
    }

    @Override
    public void compute(T t, List<MachModelInfo> list) {
        List<ValidatorFieldException> err = new ArrayList<ValidatorFieldException>();
        for (MachModelInfo machModelInfo : list) {
            err.addAll(compute(t, machModelInfo));
        }
        if (err.size() != 0)
            throw new ValidatorFieldsException(err);
    }

    public List<ValidatorFieldException> compute(T t, MachModelInfo machModelInfo) {
        Map<SpotPath, List<MatchesValidation>> map = machModelInfo.getMap();
        Map<String, Object> context = machModelInfo.getMapContext();
        Iterator<Map.Entry<SpotPath, List<MatchesValidation>>> it = map.entrySet().iterator();
        List<ValidatorFieldException> err = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<SpotPath, List<MatchesValidation>> next = it.next();
            List<MatchesValidation> values = next.getValue();
            for (MatchesValidation value : values) {
                try {
                    List<String> list = MatchValidator.validatorFiledRequire(value.getRequires(), context);
                    if (list != null && list.size() != value.getRequires().size()) {//条件全部满足才可执行下部操作
                        continue;
                    }
                    MatchValidator.validatorFiledCompares(value, value.getCompareFields(), context, list);
                    MatchValidator.validatorMatch(value, context, list);
                    MatchValidator.validatorSize(value, context);
                } catch (ValidatorException e) {
                    if (!value.isCheck() && e.getType() == ValidatorErrorType.ON_EMPTY)
                        break;//check==false 且  ConverterErrorType.EMPTY 时，忽略empty异常
                    err.add(new ValidatorFieldException(e, value.getName(), value.getFieldName()));
                }
                plugins.forEach((val) -> {
                    Object object = context.get(value.getFieldName());
                    if (object == null)
                        return;
                    try {
                        if (!value.getField().isAnnotationPresent(val.isSupport()))
                            return;
                        val.validator(object, value.getField().getAnnotation(val.isSupport()));
                    } catch (ValidatorException e) {
                        err.add(new ValidatorFieldException(e, value.getName(), value.getFieldName()));
                    }
                });
            }
        }
        return err;
    }
}
