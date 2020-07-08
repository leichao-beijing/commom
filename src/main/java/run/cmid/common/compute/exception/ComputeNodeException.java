package run.cmid.common.compute.exception;

/**
 * @author leichao
 * @date 2020-04-21 10:32:29
 */
public class ComputeNodeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ComputeNodeException(String message) {
        this.message = message;
    }

    private String message;

    @Override
    public String getMessage() {
        return message;
    }
}
