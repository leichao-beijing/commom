package run.cmdi.common.utils;

import lombok.Getter;
import run.cmdi.common.io.StringUtils;

/**
 * @author leichao
 * @date 2020-04-23 02:49:19
 */

public class SpotPath {
    public SpotPath(String path) {
        this.path = path;
        if (path.lastIndexOf(StringUtils.SPOT) == 1) {
            this.name = path;
        } else
            this.name = path.subSequence(path.lastIndexOf(StringUtils.SPOT) + 1, path.length()).toString();
    }

    @Getter
    private String path;
    @Getter
    private String name;

    public SpotPath getParent() {
        try {
            return new SpotPath(path.subSequence(0, path.lastIndexOf(StringUtils.SPOT)).toString());
        } catch (StringIndexOutOfBoundsException e) {
            return null;
        }
    }

    public String[] getPaths() {
        return path.split(StringUtils.SPOT_SPLIT);
    }

    /**
     * 创建一个和parent拥有相同父类的对象
     */
    public SpotPath careSomeParentSpotPath(String value) {
        if (getParent() == null)
            return new SpotPath(value);
        return getParent().createSub(value);
    }

    public SpotPath createSub(String subString) {
        if (subString.indexOf(StringUtils.SPOT) != -1)
            throw new NullPointerException("no exists" + StringUtils.SPOT);
        return new SpotPath(path + StringUtils.SPOT + subString);
    }

    @Override
    public String toString() {
        if (super.toString() == null)
            return super.toString();
        return path;
    }

    @Override
    public int hashCode() {
        return path.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof SpotPath) {
            SpotPath spotPath = (SpotPath) obj;
            return spotPath.getPath().equals(path);
        }
        return super.equals(obj);
    }
}
