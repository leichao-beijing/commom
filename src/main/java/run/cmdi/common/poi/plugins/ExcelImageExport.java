package run.cmdi.common.poi.plugins;

import cn.hutool.core.io.IoUtil;
import org.apache.poi.ss.usermodel.*;
import run.cmdi.common.convert.plugs.PoiReaderConvert;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

/**
 * Excel内图片文件导出功能
 */

public class ExcelImageExport {
    public static final String EMF = "x-emf";

    public static int readExcel(Path srcPath, File outPath) throws IOException {
        FileInputStream is = new FileInputStream(srcPath.toFile());
        PoiReaderConvert convert = PoiReaderConvert.reader(is);
        Workbook workbook = convert.getWorkbook();
        int size = readWordbookAll(workbook, srcPath.getFileName().toString(), outPath);
        FileOutputStream fos = new FileOutputStream(srcPath.toFile());
        workbook.close();
        convert.close();
        fos.close();
        IoUtil.close(is);
        return size;
    }

    public static int readExcel(Workbook word, String fileName, File outPath) {
        return readWordbookAll(word, fileName, outPath);
    }

    /**
     * 获取word内，图片数量
     */
    public static int getImageCount(Workbook word) {
        Iterator<Sheet> it = word.iterator();
        int i = 0;
        while (it.hasNext()) {
            Sheet sheet = it.next();
            i = i + exportSheetImageFile(sheet, null, null);
        }
        return i;
    }

    private static int readWordbookAll(Workbook word, String fileName, File outPath) {
        Iterator<Sheet> it = word.iterator();
        int i = 0;
        while (it.hasNext()) {
            Sheet sheet = it.next();
            int c = exportSheetImageFile(sheet, fileName, outPath);
            i = c + i;
        }
        return i;
    }

    /**
     * 导出sheet内的图片到file目录内。
     *
     * @return 返回sheet内图片的数量
     **/
    private static int exportSheetImageFile(Sheet srcSheet, String fileName, File outPath) {
        Drawing<?> drawing = srcSheet.getDrawingPatriarch();
        if (drawing == null)
            return 0;
        Iterator<?> it = drawing.iterator();
        String sheetName = srcSheet.getSheetName();
        int i = 0;
        while (it.hasNext()) {
            Object obj = it.next();
            PictureData pictureData;
            if (obj instanceof ObjectData) {
                ObjectData objectData = (ObjectData) obj;
                pictureData = objectData.getPictureData();
            } else if (obj instanceof Picture) {
                Picture picture = (Picture) obj;
                pictureData = picture.getPictureData();
            } else {
                continue;
            }
            i++;
            if (outPath == null)
                continue;
            String name = Paths.get(pictureData.getMimeType()).getFileName().toString();
            if (name.toString().equals(EMF))
                name = "emf";
            byte[] data = pictureData.getData();
            File file = new File(outPath + "//" + fileName + "-" + sheetName + "-" + i + "." + name);
            try {
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(data);
                IoUtil.close(fos);
                continue;
            } catch (Exception e) {
                // log.error("输出目录：" + outPath + "，sheetName：" + sheetName + ",错误：" +
                // e.getMessage() + "。");
                i--;
            }
        }
        return i;
    }

}
