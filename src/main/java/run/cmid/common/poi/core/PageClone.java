package run.cmid.common.poi.core;

public interface PageClone<RESOURCES> {
    /**
     * @param resources 需要克隆的到的资源
     * @param tag       需要克隆的 RESOURCES name
     * @param tagNew    新resources 的 tag名称
     */
    void clone(RESOURCES resources, String tag, String tagNew);
}
