package run.cmdi.common.convert;

/**
 * @param <INT> 需要转换的单位泛型
 */
public abstract class ReaderFactoryAbstract<INT, TYPE extends ReaderPageInterface<INT>> extends RegisterAbstract<INT, TYPE> {
    public ReaderFactoryAbstract(INT value) {
        super(value);
    }
}
