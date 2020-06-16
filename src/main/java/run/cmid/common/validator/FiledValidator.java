package run.cmid.common.validator;

import cn.hutool.core.date.DateException;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.eumns.CompareType;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class FiledValidator {
    public static String headMessage(String tagName, Object value) {
        return "<" + tagName + ">的值[" + value + "]";
    }

    /**
     * @param mode
     * @param values
     * @param state  false 不满足 true 满足
     */
    public static String message(ExcelRead mode, String[] values, boolean state) {
        String no = "";
        if (!state)
            no = "不";
        if (mode == ExcelRead.EMPTY || mode == ExcelRead.EXISTS) {
            return no + "满足 " + mode.getTypeName() + " 的条件";
        }
        return "在" + Arrays.asList(values) + "内，" + no + "满足 " + mode.getTypeName() + " 的条件";
    }

    /**
     * @param value
     * @param values
     * @param mode
     * @return value== NULL or "" 时不进行判断返回null  , values.length==0返回true
     */
    public static boolean mode(Object value, String[] values, ExcelRead mode) {
        if (StringUtils.isBlack(value))
            throw new ValidatorException(ConverterErrorType.ON_EMPTY);
        else if (mode == ExcelRead.EXISTS) {
            return true;
        }
        if (mode == ExcelRead.EMPTY) {
            return false;
        }
        List<String> list = Arrays.asList(values);
        switch (mode) {
            case EQUALS:
                return list.contains(value);
            case INCLUDE:
                for (String val : list) {
                    if (value.toString().indexOf(val) != -1)
                        return true;
                }
                return false;
            case NO_EQUALS:
                return !list.contains(value);
            case NO_INCLUDE:
                for (String val : list) {
                    if (value.toString().indexOf(val) != -1)
                        return false;
                }
                return true;
            case LESS_THAN:
            case LESS_THAN_OR_EQUAL:
            case GREATER_THAN:
            case GREATER_THAN_OR_EQUAL:
                BigDecimal srcBigDecimal = getBigDecimal(value);
                for (String val : list) {
                    if (!compare(srcBigDecimal, getBigDecimal(val), mode))
                        return false;
                }
                return true;
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
            throw new ValidatorException(ConverterErrorType.NO_NUMBER);
    }

    public static boolean compare(Object src, Object des, CompareType mode) {
        return compare(getBigDecimal(src), getBigDecimal(des), mode);
    }

    public static BigDecimal getBigDecimal(Object object) {
        if (object == null)
            throw new ValidatorException(ConverterErrorType.NO_NUMBER);
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

    /**
     * 比大小
     */
    public static Boolean compare(BigDecimal number1, BigDecimal number2, ExcelRead mode) {
        switch (mode) {
            case LESS_THAN:
                return NumberUtil.isLess(number1, number2);
            case LESS_THAN_OR_EQUAL:
                return NumberUtil.isLessOrEqual(number1, number2);
            case GREATER_THAN:
                return NumberUtil.isGreater(number1, number2);
            case GREATER_THAN_OR_EQUAL:
                return NumberUtil.isGreaterOrEqual(number1, number2);
        }
        return false;
    }

    public static Boolean compare(BigDecimal number1, BigDecimal number2, CompareType mode) {
        switch (mode) {
            case EQUALS:
                return number1.equals(number2);
            case LESS_THAN:
                return NumberUtil.isLess(number1, number2);
            case LESS_THAN_OR_EQUAL:
                return NumberUtil.isLessOrEqual(number1, number2);
            case GREATER_THAN:
                return NumberUtil.isGreater(number1, number2);
            case GREATER_THAN_OR_EQUAL:
                return NumberUtil.isGreaterOrEqual(number1, number2);
            default:
                throw new IllegalArgumentException("Unexpected value: " + mode);
        }
    }
}
