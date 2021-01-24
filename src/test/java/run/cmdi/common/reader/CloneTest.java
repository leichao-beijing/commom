package run.cmdi.common.reader;

import lombok.Getter;
import lombok.Setter;
import org.junit.Test;
import run.cmdi.common.utils.ObjectUtils;

public class CloneTest {
    @Test
    public void test() {
        A a = new A();
        B va = ObjectUtils.cloneParent(a, B.class);
        System.err.println("");
    }
}

@Getter
@Setter
class A {
    private boolean ssB1 = true;
    private boolean ssB2 = true;
    private String ss112 = "ddd";
    private String ss113;
}

@Getter
@Setter
class B extends A {

}
