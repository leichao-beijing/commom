package run.cmid.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import run.cmid.common.utils.model.OverflowNode;

/**
 * 溢出检测工具
 * 
 * @author leichao
 * @date 2020-05-01 10:37:02
 */
public class OverflowUtils<T> {
    @Getter
    private Map<T, OverflowNode<T>> map = new HashMap<T, OverflowNode<T>>();

    public void add(T path, List<T> subs) {
        OverflowNode<T> master = map.get(path);
        if (master != null && master.getSubs() != null)
            throw new StackOverflowError(path + " Overflow");
        else if (master == null) {
            master = new OverflowNode<T>(path, null, subs);
            map.put(path, master);
        } else
            master.setSubs(subs);
        for (T string : subs) {
            if (path.equals(string))
                throw new StackOverflowError(master.getPath() + " and " + string + " is loop");
            if (master.getParents() != null && master.getParents().contains(string))
                throw new StackOverflowError(master.getPath() + " and " + string + " is loop");
            OverflowNode<T> value = map.get(string);
            if (value == null) {
                value = new OverflowNode<T>(string, master.getParents(), null);
                value.add(path);
                map.put(string, value);
            } else {
                value.add(path);
                value.add(master.getParents());
            }
        }
    }
}
