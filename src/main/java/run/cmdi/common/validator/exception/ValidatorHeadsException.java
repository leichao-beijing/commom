package run.cmdi.common.validator.exception;

import lombok.Getter;

import java.util.List;

public class ValidatorHeadsException extends RuntimeException {
    public ValidatorHeadsException(List<List<String>> list) {
        super("必选头信息缺失 "+list.toString());
        this.list = list;
    }
    @Getter
    private final List list;
}
