package run.cmdi.common.convert;


import run.cmdi.common.register.RegisterEntity;

import java.io.Closeable;

public interface ReaderPageInterface<INT> extends RegisterEntity<INT>, BuildPage, Closeable {
}
