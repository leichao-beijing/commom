package run.cmdi.common.reader.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MatchModel {
    private String string;
    private Integer position = -1;
    private String matchValue;
}
