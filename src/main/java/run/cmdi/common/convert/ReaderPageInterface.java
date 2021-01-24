package run.cmdi.common.convert;


import java.io.Closeable;

public interface ReaderPageInterface<INT> extends RegisterEntity<INT>, BuildPage, Closeable {
}
