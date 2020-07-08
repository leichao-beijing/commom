package run.cmid.common.validator.core;

import run.cmid.common.reader.core.MatchValidator;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.utils.ReflectLcUtils;
import run.cmid.common.utils.SpotPath;
import run.cmid.common.validator.ResultObjectInterface;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.validator.exception.ValidatorFieldsException;
import run.cmid.common.validator.model.MachModelInfo;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.*;

public class ValidatorResultObject<T> implements ResultObjectInterface<T, MatchesValidation, MachModelInfo> {
    @Override
    public void update(T t, Map<SpotPath, MatchesValidation> map) {

    }

    @Override
    public void addInfo(T t, List<MachModelInfo> list, Map<SpotPath, MatchesValidation> map) {
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
        Map<SpotPath, MatchesValidation> map = machModelInfo.getMap();
        Map<String, Object> context = machModelInfo.getMapContext();
        Iterator<Map.Entry<SpotPath, MatchesValidation>> it = map.entrySet().iterator();
        List<ValidatorFieldException> err = new ArrayList<>();
        while (it.hasNext()) {
            Map.Entry<SpotPath, MatchesValidation> next = it.next();
            //SpotPath key = next.getKey();
            MatchesValidation value = next.getValue();
            try {
                List<String> list = MatchValidator.validatorFiledRequire(value.getRequires(), context);
                if (list != null && list.size() == 0) {
                    continue;
                }
                MatchValidator.validatorFiledCompares(value, value.getCompareFields(), context, (list == null) ? "" : list.toString());
                MatchValidator.validatorMatch(value, context, (list == null) ? "" : list.toString());
                MatchValidator.validatorSize(value, context);
            } catch (ValidatorException e) {
                if (!value.isCheck() && e.getType() == ConverterErrorType.ON_EMPTY)
                    break;//check==false 且  ConverterErrorType.EMPTY 时，忽略empty异常
                err.add(new ValidatorFieldException(e, value.getName(),value.getFieldName()));
            }
        }
        return err;
    }
}
