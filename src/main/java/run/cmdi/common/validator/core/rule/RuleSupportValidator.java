package run.cmdi.common.validator.core.rule;

public interface RuleSupportValidator<VALUE> {
    boolean isSupport(Class clazz);

    Class<VALUE> getValueClass();
}
