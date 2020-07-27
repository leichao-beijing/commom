package run.cmdi.common.validator;

/**
 * @author leichao
 * @date 2020-05-05 03:48:36
 */
public class FunctionDefaultModel<RETURN> implements FunctionInterface<RETURN> {

    @Override
    public RETURN value() {
        return null;
    }

}
