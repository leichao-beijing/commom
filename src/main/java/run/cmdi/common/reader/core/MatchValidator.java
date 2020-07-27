package run.cmdi.common.reader.core;

import run.cmdi.common.io.StringUtils;
import run.cmdi.common.reader.model.eumns.ConverterErrorType;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.model.CompareField;
import run.cmdi.common.validator.model.MatchesValidation;
import run.cmdi.common.validator.model.Require;
import run.cmdi.common.utils.RegexReplace;
import run.cmdi.common.validator.eumns.ValidatorErrorType;
import run.cmdi.common.validator.FiledValidatorUtils;

import java.util.*;
import java.util.regex.Pattern;

public class MatchValidator {
    /**
     * @param regex
     */
    public static boolean validationRegex(String value, String regex, Map<String, Object> dataMap) {
        if (regex.trim().equals(""))
            throw new ValidatorException(ValidatorErrorType.REGEX_EMPTY);
        regex = RegexReplace.replaceValue(dataMap, regex);
        boolean s = Pattern.compile(regex).matcher(value).matches();
        return s;
    }

    public static List<String> validatorFiledRequire(List<Require> filedMatches, Map<String, Object> dataMap) {
        if (filedMatches == null || filedMatches.size() == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (Require require : filedMatches) {
            Object value = dataMap.get(require.getFieldName());
            if (!require.getMessage().equals(""))
                mgs = require.getMessage();
            try {
                if (FiledValidatorUtils.mode(value, require.getValue(), require, dataMap)) {
                    mgs = (mgs != null) ? (mgs) : FiledValidatorUtils.headMessage(require.getName(), value) + " " + FiledValidatorUtils.message(require.getMode(), require.getValue(), true);
                    list.add(mgs);
                }
            } catch (ValidatorException e) {

            }
        }
        return list;
    }

    public static void validatorSize(MatchesValidation matchesValidation, Object value) {
        if (matchesValidation.getMin() == -1 && matchesValidation.getMax() == -1) return;
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
        if ((matchesValidation.getMin() != -1 && matchesValidation.getMax() != -1) && matchesValidation.getMax() < matchesValidation.getMin()) {
            //配置错误 max 必须大于 min
        }
        boolean state = true;
        if (matchesValidation.getMin() != -1 && matchesValidation.getMin() > size) {
            state = false;
        }
        if (matchesValidation.getMax() != -1 && matchesValidation.getMax() < size) {
            state = false;
        }
        if (!state) {
            String msg = null;
            if (matchesValidation.getMin() != -1)
                msg = "最小:" + matchesValidation.getMin();
            if (matchesValidation.getMax() != -1)
                msg = (msg != null) ? msg + " 最大:" + matchesValidation.getMax() : " 最大:" + matchesValidation.getMax();
            if (type)
                throw new ValidatorException(ValidatorErrorType.SIZE_ERROR, "内容数量必须在<" + msg + ">的范围内");
            else
                throw new ValidatorException(ValidatorErrorType.LENGTH_ERROR, "长度数量必须在<" + msg + ">的范围内");
        }
    }


    public static void validatorFiledCompares(MatchesValidation matchesValidation, List<CompareField> filedCompares, Map<String, Object> dataMap, List<String> list) {
        if (filedCompares == null || filedCompares.size() == 0)
            return;
        int count = 0;
        String msg;
        Object value = dataMap.get(matchesValidation.getFieldName());
        if (list == null)
            list = new ArrayList<>();
        for (CompareField compareField : filedCompares) {
            Object data = dataMap.get(compareField.getFieldName());
            if (!FiledValidatorUtils.compare(value, data, compareField, dataMap)) {
                if (!compareField.getMessage().equals(""))
                    msg = "<" + matchesValidation.getName() + ">的值 不满足：" + compareField.getMessage();
                else
                    msg = validationMessages(matchesValidation, value, data, compareField, false);
                count++;
                list.add(msg);
            }
        }
        if (count != 0)
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, list.toString());
    }

    private static String validationMessages(MatchesValidation matchesValidation, Object value, Object data, CompareField compareField, boolean state) {
        if (!compareField.getMessage().equals(""))
            return compareField.getMessage();
        String mgs;
        if (state)
            mgs = " 满足 ";
        else mgs = " 不满足 ";
        return matchesValidation.getName() + "：" + value + " 与 " + compareField.getName() +
                "：" + data + mgs + compareField.getMode() + " 的条件";
    }


    public static void validatorMatch(MatchesValidation matchesValidation, Map<String, Object> dataMap, List<String> list) {
        if (matchesValidation.isThrowState())
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, matchesValidation.getMessage());
        if (list == null)
            list = new ArrayList<>();
        Object value = dataMap.get(matchesValidation.getFieldName());
        if (StringUtils.isBlack(value)) {
            throw new ValidatorException(ValidatorErrorType.ON_EMPTY, matchesValidation.getName() + " " + ValidatorErrorType.ON_EMPTY.getTypeName());
        }
        if (matchesValidation.getMode() == ValidationType.REGEX) {
            if (!validationRegex(value.toString(), matchesValidation.getRegex(), dataMap)) {
                throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, "不满足：" + (!matchesValidation.getMessage().equals("") ? matchesValidation.getMessage() : "正则验证 " + matchesValidation.getRegex()));
            }
            return;
        }//none 比较配置跳过
        if (matchesValidation.getValue().length == 0 && matchesValidation.getMode() == ValidationType.NO_EMPTY) {
            return;
        } else if (matchesValidation.getValue().length == 0 && !(ValidationType.noValidationValue(matchesValidation.getMode()))) {
            throw new ValidatorException(ValidatorErrorType.COMPARE_IS_EMPTY, matchesValidation.getName() + " " + ConverterErrorType.COMPARE_IS_EMPTY.getTypeName());
        }

        if (!FiledValidatorUtils.mode(value, matchesValidation.getValue(), matchesValidation, dataMap)) {
            if (!matchesValidation.getMessage().equals(""))
                list.add("<" + matchesValidation.getName() + ">的值,不满足：" + matchesValidation.getMessage());
            else {
                list.add(FiledValidatorUtils.headMessage(matchesValidation.getName(), value) + " " + FiledValidatorUtils.message(matchesValidation.getMode(), matchesValidation.getValue(), false));
            }
            throw new ValidatorException(ValidatorErrorType.VALIDATOR_ERROR, list.toString());
        }
    }

}
