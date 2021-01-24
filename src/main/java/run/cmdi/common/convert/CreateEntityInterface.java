package run.cmdi.common.convert;

import java.io.Closeable;
import java.io.IOException;

public interface CreateEntityInterface extends Closeable {
    void save() throws IOException;
}