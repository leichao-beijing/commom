package run.cmdi.common.convert;

import java.io.InputStream;

public class WriteEntity<IN> implements WriteValueInterface<IN> {

    public static void toCsv(InputStream is) {
    }

    public static void toPoi(InputStream is, String sheetName) {
    }

    @Override
    public void add(IN value) {
    //<list<object>> add<List>
    }

    @Override
    public void add(int startColumn, IN value) {

    }

    @Override
    public void replace(int row, IN value) {

    }

    @Override
    public void replace(int row, int startColumn, IN value) {

    }
}