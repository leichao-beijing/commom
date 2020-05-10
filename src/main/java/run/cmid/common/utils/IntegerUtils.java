package run.cmid.common.utils;

import java.math.BigDecimal;

/**
 *
 * @author leichao
 * @date 2020-04-15 02:29:15
 */
public class IntegerUtils {
    /**
     * object to BigDecimal
     * 
     * @throws NumberFormatException
     */
    public static BigDecimal ObjectToBigDecimal(Object object) {

        if (object instanceof Integer) {
            Integer value = (Integer) object;
            return new BigDecimal(value);
        }
        if (object instanceof Double) {
            Double value = (Double) object;
            return new BigDecimal(value);
        }
        if (object instanceof Long) {
            Long value = (Long) object;
            return new BigDecimal(value);
        }
        if (object instanceof Short) {
            Short value = (Short) object;
            return new BigDecimal(value);
        }
        if (object == int.class) {
            int value = (int) object;
            return new BigDecimal(value);
        }
        if (object == double.class) {
            double value = (double) object;
            return new BigDecimal(value);
        }
        if (object == long.class) {
            long value = (long) object;
            return new BigDecimal(value);
        }
        if (object == short.class) {
            short value = (short) object;
            return new BigDecimal(value);
        }
        if (object instanceof String) {
            String value = (String) object;
            return new BigDecimal(value);
        }
        return null;
    }
}
