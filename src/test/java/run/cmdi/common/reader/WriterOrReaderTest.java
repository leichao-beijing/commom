package run.cmdi.common.reader;

import org.junit.Test;
import run.cmdi.common.convert.WriterCsv;
import run.cmdi.common.convert.WriterEntity;
import run.cmdi.common.convert.WriterPoi;

import java.io.FileOutputStream;
import java.io.IOException;

public class WriterOrReaderTest {
    @Test
    public void writerPoi() throws IOException {
        WriterPoi writer = new WriterPoi(true, "test");
        writer.add(1, 1, "11", null);
        writer.add(1, 2, "12", null);
        writer.add(1, 4, "14", null);

        writer.add(5, 1, "51", null);
        writer.add(5, 2, "52", null);
        writer.add(5, 4, "54", null);

        writer.add(3, 1, "31", null);
        writer.add(3, 2, "32", null);
        writer.add(3, 4, "34", null);
        writer.save(new FileOutputStream("C:\\Users\\leichao\\Desktop\\1.xlsx"));
    }

    @Test
    public void writerCsv() throws IOException {
        WriterEntity writer = new WriterCsv("utf-8");
        writer.add(1, 1, "11", null);
        writer.add(1, 2, "12", null);
        writer.add(1, 4, "14", null);

        writer.add(5, 1, "51", null);
        writer.add(5, 2, "52", null);
        writer.add(5, 4, "54", null);

        writer.add(3, 1, "31", null);
        writer.add(3, 2, "32", null);
        writer.add(3, 4, "34", null);
        writer.save(new FileOutputStream("C:\\Users\\leichao\\Desktop\\1.csv"));
    }
}
