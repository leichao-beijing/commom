package run.cmdi.common.number;

import run.cmdi.common.validator.eumns.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class NumberFormat {

    /**
     * 四舍五入
     *
     * @param value
     * @param digit 保留位数，不能小于1
     */
    public static BigDecimal rounding(BigDecimal value, int digit) {
        if (digit < 1)
            throw new NumberFormatException("digit not less 1");
        return value.setScale(digit, RoundingMode.HALF_UP);
    }

    /**
     * 保留digit位小数，末尾为0时，补1
     *
     * @param value
     * @param digit
     */
    public static BigDecimal roundingEndZeroToOne(BigDecimal value, int digit) {
        value = rounding(value, digit);
        BigDecimal tmp = value.add(BigDecimal.ONE);
        for (int i = 0; i < digit; i++) {
            value = value.multiply(BigDecimal.TEN);
        }
        if (value.remainder(BigDecimal.TEN).intValue() == 0) {
            value = value.add(BigDecimal.ONE);
            for (int i = 0; i < digit; i++) {
                value = value.divide(BigDecimal.TEN);
            }
            return value;
        }
        return tmp;
    }
}
