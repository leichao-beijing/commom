package run.cmdi.common.convert;

public interface WriterEntityInterface<T> extends SaveInterface {
    void add(T t);

    void add(int column, T t);

    void replace(int row, int column, T t);

    void replace(int row, T t);
}