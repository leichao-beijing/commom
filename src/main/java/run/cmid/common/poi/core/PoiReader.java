package run.cmid.common.poi.core;

import cn.hutool.core.io.IoUtil;
import lombok.Getter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import run.cmid.common.poi.model.ReaderPoiConfig;
import run.cmid.common.reader.core.BookResources;
import run.cmid.common.reader.core.ReaderPage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class PoiReader extends StylePalette implements BookResources {
    @Getter
    private final Workbook workbook;
    private final ReaderPoiConfig readerPoiConfig;
    private final File outFile;

    public static PoiReader build(InputStream is,String password, ReaderPoiConfig readerPoiConfig,  File outFile) throws IOException {
        Workbook workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is), password);
        return new PoiReader(workbook, readerPoiConfig, outFile);
    }

    public static PoiReader build(Workbook workbook, ReaderPoiConfig readerPoiConfig, File outFile) {
        return new PoiReader(workbook, readerPoiConfig, outFile);
    }


    private PoiReader(Workbook workbook, ReaderPoiConfig readerPoiConfig, File outFile) {
        super(workbook);
        this.workbook = workbook;
        this.readerPoiConfig = readerPoiConfig;
        this.outFile = outFile;
    }

    @Override
    public ReaderPage book(int index) {
        Sheet sheet = workbook.getSheetAt(index);
        return new SheetReaderPage(sheet, "index:" + index, readerPoiConfig);
    }

    @Override
    public ReaderPage book(String tag) {
        Sheet sheet = workbook.getSheet(tag);
        return new SheetReaderPage(sheet, tag, readerPoiConfig);
    }

    @Override
    public List<ReaderPage> bookList() {
        Iterator<Sheet> it = workbook.sheetIterator();
        List<ReaderPage> list = new ArrayList<ReaderPage>();
        while (it.hasNext())
            list.add(new SheetReaderPage(it.next(), null, readerPoiConfig));
        return list;
    }

    /**
     * 文件保存至outFile内。关闭workbook和FileOutputStream
     */
    public void saveAndClose() throws IOException {
        FileOutputStream fos = new FileOutputStream(outFile);
        workbook.write(fos);
        workbook.close();
        fos.close();
    }

    /**
     * 文件保存至file内。关闭workbook和FileOutputStream
     *
     * @param file
     */
    public void saveAndClose(File file) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        workbook.write(fos);
        workbook.close();
        fos.close();
    }
}
