package run.cmdi.common.convert;

import java.io.IOException;
import java.io.InputStream;

public interface WriteValueInterface<IN> {
    void add(IN value);

    void add(int startColumn, IN value);

    void replace(int row, IN value);

    void replace(int row, int startColumn, IN value);
}



