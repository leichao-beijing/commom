package run.cmdi.common.reader.core;

import run.cmdi.common.io.StringUtils;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.validator.annotations.support.*;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.utils.RegexReplace;
import run.cmdi.common.validator.eumns.ValidatorErrorType;
import run.cmdi.common.validator.FieldValidatorUtils;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.util.*;
import java.util.regex.Pattern;

public class MatchValidator {
    /**
     * @param regex
     */
    public static boolean validationRegex(String value, String regex, Map<String, ValueFieldName> dataMap) {
        if (regex.trim().equals(""))
            throw new ValidatorException(ValidatorErrorType.REGEX_EMPTY);
        regex = RegexReplace.replaceValue(dataMap, regex);
        boolean s = Pattern.compile(regex).matcher(value).matches();
        return s;
    }

    public static List<String> validatorFieldRequire(ValueFieldName valueFieldName, List<FieldRequireEntity> fieldMatches, Map<String, ValueFieldName> dataMap) {
        if (fieldMatches == null || fieldMatches.size() == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (FieldRequireEntity require : fieldMatches) {
            if (!require.getMessage().equals(""))
                mgs = require.getMessage();
            try {
                if (FieldValidatorUtils.mode(valueFieldName, require.getValue(), require, dataMap)) {
                    mgs = (mgs != null) ? (mgs) : FieldValidatorUtils.headMessage(valueFieldName) + " " + FieldValidatorUtils.message(require.getMode(), require.getValue(), true);
                    list.add(mgs);
                }
            } catch (ValidatorException e) {

            }
        }
        return list;
    }

    public static void validatorSize(Object value, FieldValidationEntity fieldValidationEntity) {
        if (fieldValidationEntity.getMin() == -1 && fieldValidationEntity.getMax() == -1) return;
        if (StringUtils.isEmpty(value)) return;
        int size = -1;
        boolean type = true;
        if (value.getClass().isAssignableFrom(Collection.class)) {
            size = ((Collection) value).size();
            type = true;
        } else if (!value.getClass().isAssignableFrom(Date.class)) {
            size = value.toString().length();
            type = false;
        }
        if (size == -1) return;
        if ((fieldValidationEntity.getMin() != -1 && fieldValidationEntity.getMax() != -1) && fieldValidationEntity.getMax() < fieldValidationEntity.getMin()) {
            //配置错误 max 必须大于 min
        }
        boolean state = true;
        if (fieldValidationEntity.getMin() != -1 && fieldValidationEntity.getMin() > size) {
            state = false;
        }
        if (fieldValidationEntity.getMax() != -1 && fieldValidationEntity.getMax() < size) {
            state = false;
        }
        if (!state) {
            String msg = null;
            if (fieldValidationEntity.getMin() != -1)
                msg = "最小:" + fieldValidationEntity.getMin();
            if (fieldValidationEntity.getMax() != -1)
                msg = (msg != null) ? msg + " 最大:" + fieldValidationEntity.getMax() : " 最大:" + fieldValidationEntity.getMax();
            if (type)
                throw new ValidatorException(ValidatorErrorType.SIZE_ERROR, "内容数量必须在<" + msg + ">的范围内");
            else
                throw new ValidatorException(ValidatorErrorType.LENGTH_ERROR, "长度数量必须在<" + msg + ">的范围内");
        }
    }

    public static void validatorFieldCompares(ValueFieldName valueFieldName, FieldValidationEntity fieldValidationEntity, Map<String, ValueFieldName> dataMap, List<String> list) {
        if (fieldValidationEntity.getFieldCompareEntities() == null || fieldValidationEntity.getFieldCompareEntities().size() == 0)
            return;
        int count = 0;
        String msg;
        Object value = dataMap.get(valueFieldName.getFieldName());
        if (list == null)
            list = new ArrayList<>();
        for (FieldCompareEntity compareField : fieldValidationEntity.getFieldCompareEntities()) {
            ValueFieldName desValueFieldName = dataMap.get(compareField.getFieldName());
            if (!FieldValidatorUtils.compare(valueFieldName.getValue(), desValueFieldName.getValue(), compareField, dataMap)) {
                if (!compareField.getMessage().equals(""))
                    msg = "<" + valueFieldName + ">的值 不满足：" + compareField.getMessage();
                else
                    msg = validationMessages(valueFieldName, desValueFieldName, compareField, false);
                count++;
                list.add(msg);
            }
        }
        if (count != 0)
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, list.toString());
    }

    private static String validationMessages(ValueFieldName srcValueFieldName, ValueFieldName desValueFieldName, FieldCompareEntity compareField, boolean state) {
        if (!compareField.getMessage().equals(""))
            return compareField.getMessage();
        String mgs;
        if (state)
            mgs = " 满足 ";
        else mgs = " 不满足 ";
        return srcValueFieldName + " 与 " + desValueFieldName + " " + mgs + compareField.getMode() + " 的条件";
    }


    public static void validatorMatch(ValueFieldName valueFieldName, FieldValidationEntity fieldValidationEntity, Map<String, ValueFieldName> dataMap, List<String> list) {
        if (fieldValidationEntity.isThrowState())
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, fieldValidationEntity.getMessage());
        if (list == null)
            list = new ArrayList<>();
        //Object value = dataMap.get(fieldNameValue.getFieldName());
        if (StringUtils.isBlack(valueFieldName.getFieldName())) {
            throw new ValidatorException(ValidatorErrorType.ON_EMPTY, fieldValidationEntity.getKeyName() + " " + ValidatorErrorType.ON_EMPTY.getTypeName());
        }
        if (fieldValidationEntity.getMode() == ValidationType.REGEX) {
            if (!validationRegex(valueFieldName.getValue().toString(), fieldValidationEntity.getRegex(), dataMap)) {
                throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, "不满足：" + (!fieldValidationEntity.getMessage().equals("") ? fieldValidationEntity.getMessage() : "正则验证 " + fieldValidationEntity.getRegex()));
            }
            return;
        }//none 比较配置跳过
        if (fieldValidationEntity.getValue().length == 0 && fieldValidationEntity.getMode() == ValidationType.NO_EMPTY) {
            return;
        } else if (fieldValidationEntity.getValue().length == 0 && !(ValidationType.noValidationValue(fieldValidationEntity.getMode()))) {
            throw new ValidatorException(ValidatorErrorType.COMPARE_IS_EMPTY, valueFieldName + " " + ConverterErrorType.COMPARE_IS_EMPTY.getTypeName());
        }

        if (!FieldValidatorUtils.mode(valueFieldName.getValue(), fieldValidationEntity.getValue(), fieldValidationEntity, dataMap)) {
            if (!fieldValidationEntity.getMessage().equals(""))
                list.add("<" + valueFieldName + ">的值,不满足：" + fieldValidationEntity.getMessage());
            else {
                list.add(FieldValidatorUtils.headMessage(valueFieldName) + " " + FieldValidatorUtils.message(fieldValidationEntity.getMode(), fieldValidationEntity.getValue(), false));
            }
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, list.toString());
        }
    }

}
