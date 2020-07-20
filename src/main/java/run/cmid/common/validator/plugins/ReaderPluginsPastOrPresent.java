package run.cmid.common.validator.plugins;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import run.cmid.common.validator.FiledValidatorUtils;
import run.cmid.common.validator.eumns.ValidatorErrorType;
import run.cmid.common.validator.exception.ValidatorException;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.PastOrPresent;
import java.util.Date;

public class ReaderPluginsPastOrPresent implements ReaderPluginsInterface<PastOrPresent> {
    @Override
    public Class<PastOrPresent> isSupport() {
        return PastOrPresent.class;
    }

    @Override
    public void validator(Object value, PastOrPresent pastOrPresent) throws ValidatorException {
        Date date;
        if (!value.getClass().isAssignableFrom(Date.class)) {
            try {
                date = DateUtil.parse(value.toString());
            }catch (DateException e){
                throw new ValidatorException(ValidatorErrorType.CUSTOM, "不是时间类型数据");
            }
        } else
            date = (Date) value;
        if (new Date().getTime() >= date.getTime())
            return;
        throw new ValidatorException(ValidatorErrorType.CUSTOM, "不满足过去时间或等于当前时间");
    }
}
