package run.cmid.common.reader.core;

import java.util.List;

public interface BookResources {
    ReaderPage book(int i);

    ReaderPage book(String tag);

    List<ReaderPage> bookList();
}
