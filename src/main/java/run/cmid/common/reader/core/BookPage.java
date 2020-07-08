package run.cmid.common.reader.core;

import run.cmid.common.poi.core.PageClone;

import java.util.List;

public interface BookPage<RESOURCES, PAGE, UNIT> {
    RESOURCES getResources();

    ReaderPage<PAGE, UNIT> book(int i);

    ReaderPage<PAGE, UNIT> book(String tag);

    ReaderPage<PAGE, UNIT> book(PAGE page);

    List<ReaderPage<PAGE, UNIT>> bookList();


}