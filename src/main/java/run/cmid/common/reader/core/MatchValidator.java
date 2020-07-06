package run.cmid.common.reader.core;

import run.cmid.common.compare.model.DataArray;
import run.cmid.common.io.StringUtils;
import run.cmid.common.validator.annotations.ValidationFiled;
import run.cmid.common.reader.exception.ValidatorException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.eumns.ConverterErrorType;
import run.cmid.common.reader.model.eumns.ExcelRead;
import run.cmid.common.validator.FiledValidator;
import run.cmid.common.validator.model.CompareFiled;
import run.cmid.common.validator.model.MatchesValidation;
import run.cmid.common.validator.model.Require;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

public class MatchValidator {
    /**
     * @param regex
     * @param value == "" 不验证
     */
    public static boolean validationRegex(String regex, String value) {
        if (regex.equals("")) return true;
        return Pattern.compile(regex).matcher(value).matches();
    }

    public static List<String> validatorFiledRequire(Require[] filedMatches, RowInfo rowInfo) {
        if (filedMatches.length == 0)
            return null;
        List<String> list = new ArrayList<>();
        String mgs = null;
        for (Require filedMatch : filedMatches) {
            Object value = rowInfo.getData().get(filedMatch.getFieldName());

            if (filedMatch.getModel() == ExcelRead.NONE) {
                //ExcelRead.NONE 对正则进行匹配
                if (validationRegex(filedMatch.getRegex(), value.toString())) {
                    list.add("满足：" + (!filedMatch.getMessage().equals("") ? filedMatch.getMessage() : "正则验证 " + filedMatch.getRegex()));
                }
                continue;//none时，该条配置无效
            }
            if (!filedMatch.getMessage().equals(""))
                mgs = filedMatch.getMessage();

            if (FiledValidator.mode(value, filedMatch.getValue(), filedMatch.getModel())) {
                mgs = (mgs != null) ? ("满足：" + mgs) : FiledValidator.headMessage(filedMatch.getName(), value) + " " + FiledValidator.message(filedMatch.getModel(), filedMatch.getValue(), true);
                list.add(mgs);
            }
        }
        return list;
    }

    public static void validatorSize(DataArray<Object, MatchesValidation> value) {
        MatchesValidation info = value.getInfo();
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


    public static void validatorFiledCompares(DataArray<Object, FieldDetail> value, CompareFiled[] filedCompares, RowInfo rowInfo, String frontMassages) {
        if (filedCompares.length == 0)
            return;
        List<String> error = new ArrayList<>();
        String msg;
        for (CompareFiled compareFiled : filedCompares) {
            Object data = rowInfo.getData().get(compareFiled.getFieldName());
            if (!FiledValidator.compare(value.getValue(), data, compareFiled.getMode())) {
                if (!compareFiled.getMessage().equals(""))
                    msg = frontMassages + " " + compareFiled.getMessage();
                else
                    msg = frontMassages + " " + value.getInfo().getMatchValue() + "：" + value.getValue() + " 与 " + compareFiled.getName() +
                            "：" + data + " 不满足 " + compareFiled.getMode() + " 的条件";
                error.add(msg);
            }
        }
        if (error.size() != 0) {
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, error.toString());
        }
        return;
    }

    public static void validatorMatch(DataArray<Object, FieldDetail> value, ValidationFiled validationFiled, RowInfo rowInfo, String frontMassages) {
        if (StringUtils.isBlack(value.getValue())) {
            throw new ValidatorException(ConverterErrorType.ON_EMPTY, value.getInfo().getMatchValue() + " " + ConverterErrorType.ON_EMPTY.getTypeName());
        }
        if (validationFiled.model() == ExcelRead.NONE) {
            if (!validationRegex(validationFiled.regex(), value.getValue().toString())) {
                throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, "不满足：" + (!validationFiled.message().equals("") ? validationFiled.message() : "正则验证 " + validationFiled.regex()));
            }
            return;
        }//none 比较配置跳过
        if (validationFiled.value().length == 0 && validationFiled.model() == ExcelRead.EXISTS) {
            return;
        } else if (validationFiled.value().length == 0 && !(validationFiled.model() == ExcelRead.EXISTS || validationFiled.model() == ExcelRead.EMPTY)) {
            throw new ValidatorException(ConverterErrorType.COMPARE_IS_EMPTY, value.getInfo().getMatchValue() + " " + ConverterErrorType.COMPARE_IS_EMPTY.getTypeName());
        }

        String mgs = null;
        if (!validationFiled.message().equals(""))
            mgs = frontMassages + " 不满足：" + validationFiled.message();
        if (!FiledValidator.mode(value.getValue(), validationFiled.value(), validationFiled.model()))
            throw new ValidatorException(ConverterErrorType.VALIDATOR_ERROR, (mgs != null) ? mgs : frontMassages + " " + FiledValidator.headMessage(value.getInfo().getMatchValue(), value.getValue()) + " " + FiledValidator.message(validationFiled.model(), validationFiled.value(), false));

    }

}
