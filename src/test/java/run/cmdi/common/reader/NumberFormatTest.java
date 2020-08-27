package run.cmdi.common.reader;

import org.junit.Assert;
import org.junit.Test;
import run.cmdi.common.number.NumberFormat;

import java.math.BigDecimal;

public class NumberFormatTest {
    @Test
    public void test1() {
        String str = "39.923090";
        String right = "39.923091";
        BigDecimal big = NumberFormat.roundingEndZeroToOne(new BigDecimal(str), 6);
        Assert.assertEquals(big.toString(), right);

         str = "39.923092";
         right = "39.923092";
         big = NumberFormat.roundingEndZeroToOne(new BigDecimal(str), 6);
        Assert.assertEquals(big.toString(), right);
    }
}
