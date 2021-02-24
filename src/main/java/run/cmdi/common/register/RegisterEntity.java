package run.cmdi.common.register;

import java.io.InputStream;

public interface RegisterEntity<INT> {
    /**
     * 将会自动全部转换为小写,并录入registerKey用
     */
    String getRegisterName();

    boolean isSupport(INT value);
}
