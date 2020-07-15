package run.cmid.common.reader.model;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.model.eumns.FindModel;

@Getter
@Setter
public class MatchModel {
    private String string;
    private Integer position = -1;
    private String matchValue;
}
