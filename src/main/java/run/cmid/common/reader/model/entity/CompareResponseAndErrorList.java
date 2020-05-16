package run.cmid.common.reader.model.entity;

import java.util.List;

import lombok.Getter;
import run.cmid.common.compare.model.CompareResponse;
import run.cmid.common.compare.model.LocationTagError;

/**
 * 
 * @author leichao
 */
@Getter
public class CompareResponseAndErrorList<S1, D1, EX extends Throwable> {
    public CompareResponseAndErrorList(List<CompareResponse<S1, D1>> list, List<LocationTagError<S1, EX>> errorList) {
        this.list = list;
        this.errorList = errorList;
    }

    List<CompareResponse<S1, D1>> list;
    List<LocationTagError<S1, EX>> errorList;
}
