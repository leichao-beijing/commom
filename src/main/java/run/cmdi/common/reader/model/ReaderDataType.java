package run.cmdi.common.reader.model;

import run.cmdi.common.io.TypeName;

public enum ReaderDataType implements TypeName {
    HSSF("Excel97-2003"),
    XSSF("Excel2007"),
    CSV("csv"),
    LINE("line"),
    ;

    ReaderDataType(String typeName) {
        this.typeName = typeName;
    }

    String typeName;

    @Override
    public String getTypeName() {
        return typeName;
    }
}
