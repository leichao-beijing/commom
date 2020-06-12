package run.cmid.common.reader.core;

import run.cmid.common.compare.model.DataArray;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.annotations.FiledCompare;
import run.cmid.common.reader.annotations.FiledMatches;
import run.cmid.common.reader.annotations.Match;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.validator.FiledValidator;

import java.util.ArrayList;
import java.util.List;

public class MatchValidator {

    public static List<String> validatorFiledMatches(FiledMatches[] filedMatches, RowInfo rowInfo) {
        if (filedMatches.length == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (FiledMatches filedMatch : filedMatches) {
            DataArray<Object, FieldDetail> value = rowInfo.getData().get(filedMatch.fieldName());
            if (!filedMatch.message().equals(""))
                mgs = filedMatch.message();

            if (FiledValidator.mode(value.getValue(), filedMatch.value(), filedMatch.model())) {
                mgs = (mgs != null) ? ("满足：" + mgs) : FiledValidator.headMessage(value.getInfo().getMatchValue(), value.getValue()) + " " + FiledValidator.message(filedMatch.model(), filedMatch.value(), true);
                list.add(mgs);
            }
        }
        return list;
    }

    public static void validatorFiledCompares(DataArray<Object, FieldDetail> value, FiledCompare[] filedCompares, RowInfo rowInfo, String frontMassages) {
        if (filedCompares.length == 0)
            return;
        List<String> error = new ArrayList<>();
        String msg;
        for (FiledCompare filedCompare : filedCompares) {
            DataArray<Object, FieldDetail> src = rowInfo.getData().get(filedCompare.fieldName());
            if (!FiledValidator.compare(value.getValue(), src, filedCompare.mode())) {
                if (!filedCompare.message().equals(""))
                    msg = frontMassages + " " + filedCompare.message();
                else
                    msg = frontMassages + " " + value.getInfo().getMatchValue() + "：" + value.getValue() + " 与 " + src.getInfo().getMatchValue() +
                            "：" + src.getValue() + " 不满足 " + filedCompare.mode() + " 的条件";
                error.add(msg);
            }
        }
        if (error.size() != 0) {
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, error.toString());
        }
        return;
    }

    public static void validatorMatch(DataArray<Object, FieldDetail> value, Match match, RowInfo rowInfo, String frontMassages) {
        if (StringUtils.isBlack(value)) {
            throw new ValidatorException(ConverterErrorType.EMPTY);
        }
        if (match.value().length == 0 && match.model() == ExcelRead.EXISTS) {
            return;
        } else if (match.value().length == 0 && match.model() != ExcelRead.EXISTS) {
            throw new ValidatorException(ConverterErrorType.COMPARE_IS_EMPTY);
        }

        String mgs = null;
        if (!match.message().equals(""))
            mgs = frontMassages + " 不满足：" + match.message();
        if (!FiledValidator.mode(value.getValue(), match.value(), match.model()))
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, (mgs != null) ? mgs : frontMassages + " " + FiledValidator.headMessage(value.getInfo().getMatchValue(), value.getValue()) + " " + FiledValidator.message(match.model(), match.value(), false));

    }

}
