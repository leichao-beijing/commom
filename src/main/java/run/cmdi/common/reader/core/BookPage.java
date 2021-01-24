package run.cmdi.common.reader.core;

import java.util.List;

@Deprecated
public interface BookPage<RESOURCES, PAGE, UNIT> {
    RESOURCES getResources();

    ReaderPage<PAGE, UNIT> book(int i);

    ReaderPage<PAGE, UNIT> book(String tag);

    ReaderPage<PAGE, UNIT> book(PAGE page);

    List<ReaderPage<PAGE, UNIT>> bookList();


}