package run.cmdi.common.validator;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import run.cmdi.common.io.StringUtils;
import run.cmdi.common.reader.core.MatchValidator;
import run.cmdi.common.validator.eumns.ValidationType;
import run.cmdi.common.validator.exception.ValidatorException;
import run.cmdi.common.validator.eumns.ValidatorErrorType;

import java.math.BigDecimal;
import java.util.*;

public class FiledValidatorUtils {
    public static String headMessage(String tagName, Object value) {
        return "<" + tagName + ">的值[" + value + "]";
    }

    /**
     * @param mode
     * @param values
     * @param state  false 不满足 true 满足
     */
    public static String message(ValidationType mode, String[] values, boolean state) {
        String no = "";
        if (!state)
            no = "不";
        if (mode == ValidationType.EMPTY || mode == ValidationType.NO_EMPTY) {
            return no + "满足 " + mode.getTypeName() + " 的条件";
        }
        return "在" + Arrays.asList(values) + "内，" + no + "满足 " + mode.getTypeName() + " 的条件";
    }

    /**
     * @param value
     * @param values
     * @param regexMode
     * @param dataMap
     * @return value== NULL or "" 时不进行判断返回null  , values.length==0返回true
     */
    public static boolean mode(Object value, String[] values, RegexModeInterface regexMode, Map<String, Object> dataMap) {
        ValidationType mode = regexMode.getMode();
        List<String> list = Arrays.asList(values);
        switch (mode) {
            case NO_EMPTY:
                return !StringUtils.isEmpty(value);
            case EMPTY:
                return StringUtils.isEmpty(value);
            case EQUALS:
                for (String val : list) {
                    try {
                        if (compare(value, val, regexMode, dataMap))
                            return true;
                    } catch (ValidatorException e) {
                        if (e.getType() == ValidatorErrorType.NO_NUMBER) {
                            if (val.equals(value))
                                return true;
                        } else
                            throw e;
                    }
                }
                return false;
            case INCLUDE:
                for (String val : list) {
                    if (compare(value, val, regexMode, dataMap))
                        return true;
                }
                return false;
            case NO_EQUALS:
                for (String val : list) {
                    try {
                        if (!compare(value, val, regexMode, dataMap))
                            return false;
                    } catch (ValidatorException e) {
                        if (e.getType() == ValidatorErrorType.NO_NUMBER) {
                            if (val.equals(value))
                                return false;
                        } else
                            throw e;

                    }
                }
                return true;
            case DOUBLE:
                boolean ss = NumberUtil.isDouble(value.toString());
                return NumberUtil.isDouble(value.toString());
            case INTEGER:
                return NumberUtil.isInteger(value.toString());
            case LONG:
                return NumberUtil.isLong(value.toString());
            case NUMBER:
                return NumberUtil.isNumber(value.toString());
            case NO_INCLUDE:
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                for (String val : list) {
                    if (compare(value, val, regexMode, dataMap))
                        return false;
                }
                return true;
            case EXECUTE:
                return true;
            case REGEX:
            default:
                throw new IllegalArgumentException("Unexpected value: " + mode);
        }
    }

    /**
     * 判断val是否是数字
     *
     * @param val
     * @throws ValidatorException {@link ValidatorException}NO_NUMBER
     */
    public static void isNumber(String val) {
        if (!NumberUtil.isNumber(val))
            throw new ValidatorException(ValidatorErrorType.NO_NUMBER);
    }

    public static BigDecimal getBigDecimal(Object object) {
        if (object == null)
            throw new ValidatorException(ValidatorErrorType.ON_EMPTY);
        if (object.getClass().isAssignableFrom(Date.class)) {
            return new BigDecimal(((Date) object).getTime());
        } else {
            try {
                DateTime date = DateUtil.parse(object.toString());
                return new BigDecimal(date.getTime());
            } catch (DateException e) {
                isNumber(object.toString());
                return new BigDecimal(object.toString());
            }
        }
    }

    public static Boolean compare(Object object1, Object object2, RegexModeInterface regexMode, Map<String, Object> dataMap) {
        if (regexMode.getMode() != ValidationType.NO_EMPTY && regexMode.getMode() != ValidationType.EMPTY)
            if (StringUtils.isEmpty(object1) && regexMode.getMode() != ValidationType.EMPTY && regexMode.getMode() != ValidationType.NO_EMPTY)
                throw new ValidatorException(ValidatorErrorType.ON_EMPTY);
        switch (regexMode.getMode()) {
            case LESS_THAN:
                return NumberUtil.isLess(getBigDecimal(object1), getBigDecimal(object2));
            case LESS_THAN_OR_EQUAL:
                return NumberUtil.isLessOrEqual(getBigDecimal(object1), getBigDecimal(object2));
            case GREATER_THAN:
                return NumberUtil.isGreater(getBigDecimal(object1), getBigDecimal(object2));
            case GREATER_THAN_OR_EQUAL:
                return NumberUtil.isGreaterOrEqual(getBigDecimal(object1), getBigDecimal(object2));
            case NO_EMPTY:
                return (object1 != null && object2 != null);
            case EMPTY:
                return (object1 == null && object2 == null);
            case EQUALS:
                return object1.equals(object2);
            case NO_EQUALS:
                return !object1.equals(object2);
            case INCLUDE:
            case NO_INCLUDE:
                boolean state = object1.toString().contains(object2.toString());
                return regexMode.getMode() == ValidationType.INCLUDE ? state : !state;
            case REGEX:
                return MatchValidator.validationRegex(object1.toString(), regexMode.getRegex(), dataMap) && MatchValidator.validationRegex(object2.toString(), regexMode.getRegex(), dataMap);
        }
        return false;
    }
}
