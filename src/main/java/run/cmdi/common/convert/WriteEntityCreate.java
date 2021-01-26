package run.cmdi.common.convert;

import java.io.IOException;

public class WriteEntityCreate<IN> extends WriteEntity<IN> implements CreateEntityInterface {
    WriteEntityCreate(Class<IN> t) {
        super(t);
    }

    public static void toCsv(String encoding) {
    }

    public static void toPoi(boolean xssf, String sheetName) {
    }

    public static void toPoi(boolean xssf) {
    }

    @Override
    public void save() throws IOException {

    }

    @Override
    public void close() throws IOException {

    }
}