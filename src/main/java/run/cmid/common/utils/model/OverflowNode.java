package run.cmid.common.utils.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;

/**
 * 溢出检测节点
 * 
 * @author leichao
 * @date 2020-05-01 10:36:22
 */
public class OverflowNode<T> {
    public OverflowNode(T path, Set<T> parents, List<T> subs) {
        this.path = path;
        this.subs = subs;
        if (parents != null)
            add(parents);
    }

    public void add(Set<T> parents) {
        if (parents == null)
            return;
        if (this.parents == null)
            this.parents = new HashSet<T>();
        for (T spostPath : parents) {
            this.parents.add(spostPath);
        }
    }

    public void add(T parent) {
        if (this.parents == null) {
            this.parents = new HashSet<T>();
            this.parents.add(parent);
            return;
        }
        if (parents.contains(parent))
            throw new StackOverflowError("loop");
        this.parents.add(parent);
    }

    @Getter
    private Set<T> parents;
    @Getter
    @Setter
    private List<T> subs;
    @Getter
    private T path;
}
