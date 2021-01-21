package run.cmdi.common.convert;

/**
 * @param <INT> 需要转换的单位泛型
 */
public abstract class ConvertAbstract<INT, TYPE extends TypeAnalysis<INT>> extends RegisterAbstract<INT, TYPE> {

    public ConvertAbstract(INT value) {
        super(value);
    }
}
