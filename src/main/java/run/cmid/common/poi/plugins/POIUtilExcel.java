package run.cmid.common.poi.plugins;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import cn.hutool.core.io.IoUtil;
import lombok.Setter;

@Deprecated
public class POIUtilExcel {
    @Setter
    private InputStream is;
    private Workbook workbook;

    public Cell runCell(Cell cell) {
        FormulaEvaluator evaluator = workbook.getCreationHelper().createFormulaEvaluator();
        return evaluator.evaluateInCell(cell);
    }

    /**
     * 自动计算工作簿中公式。
     */
    public void autoComputeWork() {
        if (workbook instanceof HSSFWorkbook)
            HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        if (workbook instanceof XSSFWorkbook)
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);

    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public File getFile() {
        return file;
    }

    public void save() throws FileNotFoundException, IOException {
        if (file == null)
            return;
        save(file);
    }

    public void close() {
        IoUtil.close(workbook);
        IoUtil.close(is);
    }

    public void save(File file) throws FileNotFoundException, IOException {
        workbook.getCreationHelper().createFormulaEvaluator().evaluateAll();
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        workbook.write(fileOutputStream);
        IoUtil.close(fileOutputStream);
    }

    public void save(Path path) throws IOException {
        save(path.toFile());
    }

    private File file;

//    public POIUtilExcel(File file) {
//        this.file = file;
//        try {
//            this.is = new FileInputStream(file);
//            this.workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is), null);
//        } catch (IOException e) {
//            IoUtil.close(this.is);
//        }
//    }

    public POIUtilExcel(InputStream is) throws IOException {
        this.is = is;
        this.workbook = WorkbookFactory.create(IoUtil.toMarkSupportStream(is), null);
    }

    /**
     * 创建sheet，当sheet存在时，删除原sheet继续创建
     */
    public Sheet createSheetRemove(String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            int index = workbook.getSheetIndex(sheetName);
            workbook.removeSheetAt(index);
        }
        return workbook.createSheet(sheetName);
    }

    /**
     * 创建sheet，当sheet存在时，删除原sheet继续创建
     */
    public static Sheet createSheetRemove(Workbook workbook, String sheetName) {
        Sheet sheet = workbook.getSheet(sheetName);
        if (sheet != null) {
            if (sheet.getPhysicalNumberOfRows() == 0)
                return sheet;
            int size = sheet.getPhysicalNumberOfRows();
            for (int i = size - 1; i >= 0; i--) {
                Row row = sheet.getRow(i);
                if (row == null)
                    continue;
                sheet.removeRow(row);
            }
            return sheet;
        }
        return workbook.createSheet(sheetName);
    }

    /**
     * 获取此工作簿所有Sheet表
     *
     * @return sheet表列表
     */
    public List<Sheet> getSheets() {
        final int totalSheet = getSheetCount();
        final List<Sheet> result = new ArrayList<>(totalSheet);
        for (int i = 0; i < totalSheet; i++) {
            result.add(this.workbook.getSheetAt(i));
        }
        return result;
    }

    /**
     * 返回工作簿表格数
     *
     * @return 工作簿表格数
     */
    public int getSheetCount() {
        return this.workbook.getNumberOfSheets();
    }
}
