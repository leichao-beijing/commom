package run.cmdi.common.convert;

public interface WriterEntity<INFO> extends SaveInterface {
    void add(int rownum, int column, Object value, INFO info);
}
