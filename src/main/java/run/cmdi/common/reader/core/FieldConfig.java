package run.cmdi.common.reader.core;

import lombok.Getter;
import run.cmdi.common.reader.annotations.Index;

import java.util.*;

public class FieldConfig {
    public FieldConfig(List<FieldInfo> list, Index[] indexes) {
        this.list = list;
        this.setIndex = toIndexSet(indexes);
        this.listIndex = toIndexList(indexes);
     }

    @Getter
    private final List<FieldInfo> list;

    @Getter
    private final Set<String> setIndex;
    @Getter
    private final List<List<String>> listIndex;


    private Set<String> toIndexSet(Index[] indexes) {
        Set<String> set = new HashSet<>();
        for (Index index : indexes) {
            set.addAll(Arrays.asList(index.value()));
        }
        return set;
    }

    private List<List<String>> toIndexList(Index[] indexes) {
        List<List<String>> list = new ArrayList<>();
        for (Index index : indexes) {
            list.add(new ArrayList<>() {{
                addAll(Arrays.asList(index.value()));
            }});
        }
        return list;
    }
}
