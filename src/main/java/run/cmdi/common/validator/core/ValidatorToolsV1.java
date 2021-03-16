package run.cmdi.common.validator.core;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ValidatorToolsV1<T> {
    public ValidatorToolsV1() {
    }

    private Map<String, List<FieldValidator>> map = new HashMap<>();

    public ValidatorToolsV1 addRule(FieldValidator rule) {
        List<FieldValidator> list = map.get(rule.getFiledName());
        if (list == null) map.put(rule.getFiledName(), list = new ArrayList<>());
        list.add(rule);
        return this;
    }

    public void verification(Object ob) {
    }

    public void verification(Map<String, Object> map) {
    }
}

class ss {
    public static void main(String[] args) {
        ValidatorToolsV1 tool = new ValidatorToolsV1();
        FieldValidator lei = new FieldValidator("lei", String.class).setEmptyField(false);
//        lei.addRule(RuleValueExist.exist())
//                .addRule(RuleConvert.enable())
//                .addRule(RuleValueDefault.enable("111"));


    }
}
