package run.cmdi.common.convert;

public interface WriterEntity<INFO> extends SaveInterface {
    void add(int rownum, int column, Object value, INFO info);

    /**
     * 依照size+1填充内容
     */
    void add(int column, Object value, INFO info);

    Integer getCount();
}
