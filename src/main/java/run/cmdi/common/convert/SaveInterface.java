package run.cmdi.common.convert;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;

public interface SaveInterface extends Closeable {
    void save(OutputStream os) throws IOException;
}