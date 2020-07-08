package run.cmid.common.reader.core;

import run.cmid.common.io.StringUtils;
import run.cmid.common.validator.exception.ValidatorException;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.validator.FiledValidatorUtils;
import run.cmid.common.validator.model.CompareField;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.Require;

import java.util.*;
import java.util.regex.Pattern;

public class MatchValidator {
    /**
     * @param regex
     * @param value
     */
    public static boolean validationRegex(String regex, String value) {
        if (regex.trim().equals(""))
            throw new ValidatorException(ConverterErrorType.REGEX_EMPTY);
       boolean s= Pattern.compile(regex).matcher(value).matches();
        return s;
    }

    public static List<String> validatorFiledRequire(List<Require> filedMatches, Map<String, Object> dataMap) {
        if (filedMatches == null || filedMatches.size() == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (Require filedMatch : filedMatches) {
            Object value = dataMap.get(filedMatch.getFieldName());
            if (filedMatch.getModel() == ExcelRead.REGEX) {
                //ExcelRead.NONE 对正则进行匹配
                if (validationRegex(filedMatch.getRegex(), value.toString())) {
                    list.add("满足：" + (!filedMatch.getMessage().equals("") ? filedMatch.getMessage() : "正则验证 " + filedMatch.getRegex()));
                }
                continue;//none时，该条配置无效
            }
            if (!filedMatch.getMessage().equals(""))
                mgs = filedMatch.getMessage();

            if (FiledValidatorUtils.mode(value, filedMatch.getValue(), filedMatch.getModel())) {
                mgs = (mgs != null) ? ("满足：" + mgs) : FiledValidatorUtils.headMessage(filedMatch.getName(), value) + " " + FiledValidatorUtils.message(filedMatch.getModel(), filedMatch.getValue(), true);
                list.add(mgs);
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
                throw new ValidatorException(ConverterErrorType.SIZE_ERROR, "内容数量必须在<" + msg + ">的范围内");
            else
                throw new ValidatorException(ConverterErrorType.LENGTH_ERROR, "长度数量必须在<" + msg + ">的范围内");
        }
    }


    public static void validatorFiledCompares(MatchesValidation matchesValidation, List<CompareField> filedCompares, Map<String, Object> dataMap, String frontMassages) {
        if (filedCompares == null || filedCompares.size() == 0)
            return;
        List<String> error = new ArrayList<>();
        String msg;
        Object value = dataMap.get(matchesValidation.getFieldName());
        for (CompareField compareField : filedCompares) {
            Object data = dataMap.get(compareField.getFieldName());
            if (!FiledValidatorUtils.compare(value, data, compareField.getMode())) {
                if (!compareField.getMessage().equals(""))
                    msg = frontMassages + " " + compareField.getMessage();
                else
                    msg = frontMassages + " " + matchesValidation.getName() + "：" + value + " 与 " + compareField.getName() +
                            "：" + data + " 不满足 " + compareField.getMode() + " 的条件";
                error.add(msg);
            }
        }
        if (error.size() != 0) {
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, error.toString());
        }
        return;
    }

    public static void validatorMatch(MatchesValidation matchesValidation, Map<String, Object> dataMap, String frontMassages) {
        Object value = dataMap.get(matchesValidation.getFieldName());
        if (StringUtils.isBlack(value)) {
            throw new ValidatorException(ConverterErrorType.ON_EMPTY, matchesValidation.getName() + " " + ConverterErrorType.ON_EMPTY.getTypeName());
        }
        if (matchesValidation.getModel() == ExcelRead.REGEX) {
            if (!validationRegex(matchesValidation.getRegex(), value.toString())) {
                throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, "不满足：" + (!matchesValidation.getMessage().equals("") ? matchesValidation.getMessage() : "正则验证 " + matchesValidation.getRegex()));
            }
            return;
        }//none 比较配置跳过
        if (matchesValidation.getValue().length == 0 && matchesValidation.getModel() == ExcelRead.EXISTS) {
            return;
        } else if (matchesValidation.getValue().length == 0 && !(matchesValidation.getModel() == ExcelRead.EXISTS
                || matchesValidation.getModel() == ExcelRead.EMPTY)) {
            throw new ValidatorException(ConverterErrorType.COMPARE_IS_EMPTY, matchesValidation.getName() + " " + ConverterErrorType.COMPARE_IS_EMPTY.getTypeName());
        }

        String mgs = null;
        if (!matchesValidation.getMessage().equals(""))
            mgs = frontMassages + " 不满足：" + matchesValidation.getMessage();
        if (!FiledValidatorUtils.mode(value, matchesValidation.getValue(), matchesValidation.getModel()))
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, (mgs != null) ? mgs : frontMassages + " " + FiledValidatorUtils.headMessage(matchesValidation.getName(), value) + " " + FiledValidatorUtils.message(matchesValidation.getModel(), matchesValidation.getValue(), false));

    }

}
