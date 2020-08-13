package run.cmdi.common.validator.annotations.support;

import javax.validation.Payload;
import javax.validation.constraints.PastOrPresent;
import run.cmdi.common.plugin.ConverterAnnotation;

public class PastOrPresentEntity extends ConverterAnnotation<PastOrPresent> implements PastOrPresent {
    @Override
    public void initialize(PastOrPresent pastOrPresent) {
        this.message = pastOrPresent.message();
        this.groups = pastOrPresent.groups();
    }

    private String message;

    @Override
    public String message() {
        return message;
    }

    Class[] groups = new Class[0];

    @Override
    public Class<?>[] groups() {
        return groups;
    }

    @Override
    public Class<? extends Payload>[] payload() {
        return new Class[0];
    }
}
