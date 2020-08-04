package run.cmdi.common.validator.plugins;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.validator.eumns.ValidatorErrorType;
import run.cmdi.common.validator.exception.ValidatorException;

import javax.validation.constraints.PastOrPresent;
import java.lang.annotation.Annotation;
import java.util.Date;
import java.util.Map;

public class ReaderPluginsPastOrPresent implements PluginAnnotation<PastOrPresent, Map<String, Object>> {
    @Override
    public Class<PastOrPresent> getAnnotation() {
        return PastOrPresent.class;
    }

    @Override
    public String getDescribe() {
        return "PastOrPresent";
    }

    @Override
    public String getName() {
        return "PastOrPresent";
    }

    @Override
    public Object plugin(Object value, Map<String, Object> stringObjectMap, Annotation pastOrPresent) throws ValidatorException {
        Date date;
        if (!value.getClass().isAssignableFrom(Date.class)) {
            try {
                date = DateUtil.parse(value.toString());
            } catch (DateException e) {
                throw new ValidatorException(ValidatorErrorType.CUSTOM, "不是时间类型数据");
            }
        } else
            date = (Date) value;
        if (new Date().getTime() >= date.getTime())
            return true;
        throw new ValidatorException(ValidatorErrorType.CUSTOM, "不满足过去时间或等于当前时间");
    }

}
