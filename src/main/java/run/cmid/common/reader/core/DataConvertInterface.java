package run.cmid.common.reader.core;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 
 * @author leichao
 */
public interface DataConvertInterface<T> {
    void add(T t);

    void addAll(List<T> t);

    void save(File file) throws IOException;

    /**
     * 添加空白行
     */
    void addEmptyLine();

}
