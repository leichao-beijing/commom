package run.cmid.common.validator.core;

import run.cmid.common.reader.core.MatchValidator;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.ResultObjectInterface;
import run.cmid.common.validator.eumns.ValidatorErrorType;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.validator.exception.ValidatorFieldsException;
import run.cmid.common.validator.model.*;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorResultObject<T> implements ResultObjectInterface<T, ValidationMain, MachModelInfo> {
    private final Map<String, List<ValidatorPlugin>> pluginMap;

    public ValidatorResultObject(Map<String, List<ValidatorPlugin>> pluginMap) {
        this.pluginMap = pluginMap;
    }

    @Override
    public void update(T t, Map<SpotPath, ValidationMain> map) {

    }

    @Override
    public void addInfo(T t, List<MachModelInfo> list, Map<SpotPath, ValidationMain> map) {
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
        Map<SpotPath, ValidationMain> map = machModelInfo.getMap();
        Map<String, Object> context = machModelInfo.getMapContext();
        Iterator<Map.Entry<SpotPath, ValidationMain>> it = map.entrySet().iterator();
        List<ValidatorFieldException> err = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<SpotPath, ValidationMain> next = it.next();
            ValidationMain validationMain = next.getValue();
            for (MatchesValidation value : validationMain.getValidations()) {
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
            }
            Object object = context.get(validationMain.getFieldName());
            if (object == null)
                continue;
            List<ValidatorPlugin> plugins = pluginMap.get(validationMain.getFieldName());
            if (plugins != null)
                plugins.forEach((value) -> {
                    try {
                        value.getPlugin().validator(object, context, value.getAnnotation());
                    } catch (ValidatorException e) {
                        err.add(new ValidatorFieldException(e, value.getName(), validationMain.getFieldName()));
                    }
                });
        }
        return err;
    }
}
