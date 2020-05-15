package run.cmid.common.excel.core;

import java.util.List;

public interface BookResources {
    ReadBook book(int i);
    ReadBook book(String tag);
    List<ReadBook> bookList();
}
