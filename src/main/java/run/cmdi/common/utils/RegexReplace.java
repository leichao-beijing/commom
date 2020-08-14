package run.cmdi.common.utils;

import cn.hutool.core.util.ReUtil;
import run.cmdi.common.validator.plugins.ValueFieldName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RegexReplace {
    private static String REGEX_STRING = "\\{\\{[A-Za-z0-9]+\\}\\}*";
    private static String TAG_1 = "{{";
    private static String TAG_2 = "}}";

    public static List<String> getValue(String value) {
        return ReUtil.findAll(REGEX_STRING, value, 0, new ArrayList<>());
    }

    /**
     * {{src}}***{{src}} 替换为 des***des
     *
     * @param map key==src value=des
     * @param str
     */
    public static String replaceValue(Map<String, ValueFieldName> map, String str) {
        List<String> list = getValue(str);
        if (list.size() == 0) return str;
        for (String srcValue : list) {
            String src = srcValue.replace(TAG_1, "").replace(TAG_2, "");
            ValueFieldName des = map.get(src);
            if (des.getValue() != null) {
                str = str.replace(srcValue, des.getValue().toString());
            }
        }
        return str;
    }


}
