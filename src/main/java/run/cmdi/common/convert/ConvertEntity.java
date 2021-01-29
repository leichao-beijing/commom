package run.cmdi.common.convert;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import java.io.*;

public class ConvertEntity<T> {

    public ConvertEntity(Class<T> clazz) {
    }

    private WriterEntityInterface writer() {
        //new HashMap<>();
        return null;
    }
}

class WriterConvert<T> {
    public WriterConvert(Class<T> clazz) {
    }

    private WriterEntityInterface writer(WriterEntity create) {
        //new HashMap<>();
        return null;
    }
}


class WriterCollect {
    WriterEntity toPoi(Sheet sheet) {
        return null;
    }

    WriterEntity toPoi(Workbook workbook, String sheetName) {
        return null;
    }

    WriterEntity toCsv() {
        return null;
    }

    WriterEntity toCsv(InputStream is) {
        return null;
    }
}




