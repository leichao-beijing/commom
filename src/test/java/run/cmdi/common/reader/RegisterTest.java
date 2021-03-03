package run.cmdi.common.reader;

import org.junit.Assert;
import org.junit.Test;
import run.cmdi.common.reader.register.info;
import run.cmdi.common.reader.register.testModel;
import run.cmdi.common.register.RegisterAnnotationUtils;
import run.cmdi.common.register.RegisterParameterPour;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RegisterTest {
    @Test
    public void test1() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        RegisterParameterPour pour = new RegisterParameterPour();
        pour.registerStatic("9999", Integer.valueOf(1));
        Map<String, info> map = RegisterAnnotationUtils.build(testModel.class, info.class, pour, false);
        map.forEach((key, value) -> {
            Assert.assertEquals(value.getValue1(), Integer.valueOf(1));
            Assert.assertEquals(value.getValue(), "9999");
        });
    }
}

