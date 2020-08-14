package run.cmdi.common.reader;

import org.junit.Assert;
import org.junit.Test;
import run.cmdi.common.utils.RegexReplace;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.util.HashMap;
import java.util.List;

public class RegexReplaceTest {
    private static final String FIELD_VALUE_1 = "FieldName1";
    private static final String FIELD_VALUE_2 = "fieldName2";
    private static final String TEST_REGEX_VALUE = "{{" + FIELD_VALUE_1 + "}}112121212{{" + FIELD_VALUE_2 + "}}";

    @Test
    public void RegexReplaceValueTest() {
        List<String> list = RegexReplace.getValue(TEST_REGEX_VALUE);
        Assert.assertTrue(FIELD_VALUE_1 + " no find", list.contains("{{" + FIELD_VALUE_1 + "}}"));
        Assert.assertTrue(FIELD_VALUE_2 + " no find", list.contains("{{" + FIELD_VALUE_2 + "}}"));
    }

    @Test
    public void replaceValueTest() {
        HashMap<String, ValueFieldName> map = new HashMap<>();
        map.put(FIELD_VALUE_1, ValueFieldName.build("value1", "value1", "value1"));
        map.put(FIELD_VALUE_2, ValueFieldName.build("value2", "value2", "value2"));
        String desValue = RegexReplace.replaceValue(map, TEST_REGEX_VALUE);
        Assert.assertEquals("RegexReplace.replaceValue error", desValue, "value1112121212value2");
    }
}
