package run.cmid.common.io;

import java.util.HashMap;
import java.util.HashSet;

import cn.hutool.core.util.NumberUtil;
import lombok.Data;
import lombok.Getter;

/**
 * 相同的文件进行重命名
 */
@Getter
public class SameFileRename {
    public static final String LINK_TAG = "-";

    private HashMap<String, CountInt> map = new HashMap<String, CountInt>();

    private HashSet<String> fileAll = new HashSet<String>();

    public void clear() {
        map.clear();
    }

    public void add(String fileName) {
        fileAll.add(fileName);
        String name = StringUtils.getValueHead(fileName, StringUtils.SPOT);
        String suffix = StringUtils.getTagAfterValue(fileName, StringUtils.SPOT);
        String nameHead = StringUtils.getValueHead(name, LINK_TAG);
        String nameBody = StringUtils.getTagAfterValue(name, LINK_TAG);

        String value = null;
        int i = -1;
        if (NumberUtil.isNumber(nameBody)) {
            value = nameHead + StringUtils.SPOT + suffix;
            i = Integer.parseInt(nameBody);
        } else
            value = fileName;
        if (map.get(value) == null) {
            map.put(value, new CountInt(i == -1 ? 0 : i));
            return;
        }
        CountInt countInt = new CountInt(map.get(value).getCount() > i ? map.get(value).getCount() + 1 : i);
        map.remove(value);
        map.put(value, countInt);
    }

    public String renameName(String fileName) {

        String name = StringUtils.getValueHead(fileName, StringUtils.SPOT);
        String suffix = StringUtils.getTagAfterValue(fileName, StringUtils.SPOT);
        String nameHead = StringUtils.getValueHead(name, LINK_TAG);
        String nameBody = StringUtils.getTagAfterValue(name, LINK_TAG);

        String value = null;
        int i = -1;
        if (NumberUtil.isNumber(nameBody)) {
            value = nameHead + StringUtils.SPOT + suffix;
            i = Integer.parseInt(nameBody);
        } else {
            nameHead = name;
            value = fileName;
            fileAll.add(value);
        }
        if (map.get(value) == null) {
            map.put(value, new CountInt(i == -1 ? 0 : i));
            return fileName;
        }
        if (fileAll.contains(fileName)) {
            i = map.get(value).getCount();
        }

        CountInt countInt = new CountInt(map.get(value).getCount() >= i ? map.get(value).getCount() + 1 : i);
        map.remove(value);
        map.put(value, countInt);

        return nameHead + LINK_TAG + countInt.getCount() + StringUtils.SPOT + suffix;
    }
}

@Data
class CountInt {
    public CountInt(int count) {
        this.count = count;
    }

    int count;
}
