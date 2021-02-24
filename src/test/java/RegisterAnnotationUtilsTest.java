import org.junit.Test;
import run.cmdi.common.register.RegisterAnnotationUtils;
import run.cmdi.common.reader.model.FieldInfoAbstract;
import run.cmdi.common.reader.model.Produce;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public class RegisterAnnotationUtilsTest {
    @Test
    public void test() throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Map<String, FieldInfoAbstract> map = RegisterAnnotationUtils.build(Produce.class, FieldInfoAbstract.class);
        System.err.println(map);
    }
}
