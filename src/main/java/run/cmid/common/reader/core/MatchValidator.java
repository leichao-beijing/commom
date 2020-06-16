package run.cmid.common.reader.core;

import run.cmid.common.compare.model.DataArray;
import run.cmid.common.io.StringUtils;
import run.cmid.common.reader.annotations.FiledCompare;
import run.cmid.common.reader.annotations.FiledRequire;
import run.cmid.common.reader.annotations.Match;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.validator.FiledValidator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

public class MatchValidator {

    public static List<String> validatorFiledRequire(FiledRequire[] filedMatches, RowInfo rowInfo) {
        if (filedMatches.length == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (FiledRequire filedMatch : filedMatches) {
            if (filedMatch.model() == ExcelRead.NONE) continue;//none时，该条配置无效
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

    public static void validatorSize(DataArray<Object, FieldDetail> value) {
        FieldDetail info = value.getInfo();
        if (info.getMin() == -1 && info.getMax() == -1) return;
        if (StringUtils.isEmpty(value.getValue())) return;
        int size = -1;
        boolean type = true;
        if (value.getValue().getClass().isAssignableFrom(Collection.class)) {
            size = ((Collection) value).size();
            type = true;
        } else if (!value.getValue().getClass().isAssignableFrom(Date.class)) {
            size = value.getValue().toString().length();
            type = false;
        }
        if (size == -1) return;
        if ((info.getMin() != -1 && info.getMax() != -1) && info.getMax() < info.getMin()) {
            //配置错误 max 必须大于 min
        }
        boolean state = true;
        if (info.getMin() != -1 && info.getMin() > size) {
            state = false;
        }
        if (info.getMax() != -1 && info.getMax() < size) {
            state = false;
        }
        if (!state) {
            String msg = null;
            if (info.getMin() != -1)
                msg = "最小:" + info.getMin();
            if (info.getMax() != -1)
                msg = (msg != null) ? msg + " 最大:" + info.getMax() : " 最大:" + info.getMax();
            if (type)
                throw new ValidatorException(ConverterErrorType.SIZE_ERROR, "内容数量必须在<" + msg + ">的范围内");
            else
                throw new ValidatorException(ConverterErrorType.LENGTH_ERROR, "长度数量必须在<" + msg + ">的范围内");
        }
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
        if (StringUtils.isBlack(value.getValue())) {
            throw new ValidatorException(ConverterErrorType.ON_EMPTY, value.getInfo().getMatchValue() + " " + ConverterErrorType.ON_EMPTY.getTypeName());
        }
        if (match.model() == ExcelRead.NONE) return;//none 比较配置跳过
        if (match.value().length == 0 && match.model() == ExcelRead.EXISTS) {
            return;
        } else if (match.value().length == 0 && !(match.model() == ExcelRead.EXISTS || match.model() == ExcelRead.EMPTY)) {
            throw new ValidatorException(ConverterErrorType.COMPARE_IS_EMPTY, value.getInfo().getMatchValue() + " " + ConverterErrorType.COMPARE_IS_EMPTY.getTypeName());
        }

        String mgs = null;
        if (!match.message().equals(""))
            mgs = frontMassages + " 不满足：" + match.message();
        if (!FiledValidator.mode(value.getValue(), match.value(), match.model()))
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, (mgs != null) ? mgs : frontMassages + " " + FiledValidator.headMessage(value.getInfo().getMatchValue(), value.getValue()) + " " + FiledValidator.message(match.model(), match.value(), false));

    }

}
