package run.cmdi.common.validator.core.type;

public interface Convert<VALUE> {
    VALUE convert(Object value);

    /**
     * 验证类型是否支持
     */
    boolean isSupport(Class clazz);
}
