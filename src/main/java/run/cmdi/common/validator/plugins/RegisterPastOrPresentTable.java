package run.cmdi.common.validator.plugins;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateUtil;
import lombok.Getter;
import lombok.Setter;
import run.cmdi.common.plugin.ConverterAnnotation;
import run.cmdi.common.validator.annotations.support.FieldNameEntity;
import run.cmdi.common.validator.annotations.support.PastOrPresentEntity;
import run.cmdi.common.validator.eumns.ValidatorErrorType;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class RegisterPastOrPresentTable implements ValidatorPlugin {
    private String fieldName;
    private String name;
    private FieldNameEntity fieldNameEntity;
    private PastOrPresentEntity pastOrPresentEntity;

    @Override
    public void instanceAnnotationInfo(Field field) {
        this.fieldName = field.getName();
        this.fieldNameEntity = ConverterAnnotation.instance(field, FieldNameEntity.class);
        this.pastOrPresentEntity = ConverterAnnotation.instance(field, PastOrPresentEntity.class);
        if (fieldNameEntity != null)
            this.name = this.fieldNameEntity.value();
        else
            this.name = this.fieldName;
        if (pastOrPresentEntity != null)
            state = true;
    }

    private boolean state = false;

    @Override
    public boolean isSupport() {
        return state;
    }

    @Override
    public List<ValidatorFieldException> validator(Map<String, ValueFieldName> context) {
        List<ValidatorFieldException> err = new ArrayList<>();
        ValueFieldName value = context.get(fieldName);
        if (value.getValue() == null)
            return err;
        try {
            Date date;
            if (!value.getValue().getClass().isAssignableFrom(Date.class)) {
                try {
                    date = DateUtil.parse(value.getValue().toString());
                } catch (DateException e) {
                    throw new ValidatorException(ValidatorErrorType.CUSTOM, value.getName() + " 不是时间类型数据");
                }
            } else
                date = (Date) value.getValue();
            if (new Date().getTime() >= date.getTime())
                return err;
            throw new ValidatorException(ValidatorErrorType.CUSTOM, value.getName() + " 不满足过去时间或等于当前时间");
        } catch (ValidatorException e) {
            err.add(new ValidatorFieldException(e, this));
        }
        return err;
    }
}
