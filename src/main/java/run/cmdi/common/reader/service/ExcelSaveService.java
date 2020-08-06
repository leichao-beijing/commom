package run.cmdi.common.reader.service;

import lombok.Getter;
import org.apache.poi.hssf.usermodel.HSSFFormulaEvaluator;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFFormulaEvaluator;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import run.cmdi.common.plugin.PluginAnnotation;
import run.cmdi.common.reader.core.ConvertDataToSheetCell;
import run.cmdi.common.reader.core.ConvertDataToWorkbook;
import run.cmdi.common.reader.core.WorkbookInfo;
import run.cmdi.common.reader.support.SupportConverterDate;
import run.cmdi.common.reader.support.SupportFormatDate;
import run.cmdi.common.reader.support.SupportJsonValue;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;

public class ExcelSaveService implements WorkbookInfo {
    @Getter
    private Workbook workbook;
    private final Map<String, ConvertDataToWorkbook> map = new HashMap<String, ConvertDataToWorkbook>();
    private boolean state = true;
    private InputStream is;
    @Getter
    private final Map<String, PluginAnnotation> plugins = new HashMap<>() {{
        SupportConverterDate converterDate = new SupportConverterDate();
        SupportFormatDate formatDate = new SupportFormatDate();
        SupportJsonValue jsonValue = new SupportJsonValue();

        put(converterDate.getName(), converterDate);
        put(formatDate.getName(), formatDate);
        put(jsonValue.getName(), jsonValue);
    }};

    public ExcelSaveService addPlugin(PluginAnnotation plugin) {
        plugins.put(plugin.getName(), plugin);
        return this;
    }

    public ExcelSaveService() {
    }

    public ExcelSaveService(InputStream is) {
        this.is = is;
    }

    /**
     * @param state false HSSF  Excel97-2003;true XSSF Excel2007
     */
    public ExcelSaveService(boolean state) {
        this.state = state;
    }

    /**
     * @param sheetName
     * @param t
     */
    public <T> ConvertDataToWorkbook buildConvert(String sheetName, Class<T> t) {
        ConvertDataToWorkbook convert = new ConvertDataToWorkbook(this, sheetName, t);
        if (map.get(sheetName) == null)
            map.put(sheetName, convert);
        else throw new NullPointerException("override sheetName");
        return convert;
    }
    /**
     * 自动计算表格内公式
     */
    public void autoComputeWork() {
        if (workbook instanceof HSSFWorkbook)
            HSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);
        if (workbook instanceof XSSFWorkbook)
            XSSFFormulaEvaluator.evaluateAllFormulaCells(workbook);

    }
    /**
     * 位置
     */
    public <T> ConvertDataToSheetCell buildConvert(Class<T> t) {
        return new ConvertDataToSheetCell<T>(t);
    }

    /**
     * @param sheetName
     * @param t
     * @param exceptFieldName 输出表格除外的数据对象FieldName
     */
    public <T> ConvertDataToWorkbook buildConvert(String sheetName, Class<T> t, Set<String> exceptFieldName) {
        ConvertDataToWorkbook convert = buildConvert(sheetName, t);
        exceptFieldName.forEach((val) -> {
            convert.getMap().remove(val);
        });
        return convert;
    }

    @Override
    public void save(OutputStream out) throws IOException {
        boolean state = false;
        Iterator<Map.Entry<String, ConvertDataToWorkbook>> it = map.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, ConvertDataToWorkbook> next = it.next();
            ConvertDataToWorkbook convertDataToWorkbook = next.getValue();
            if (convertDataToWorkbook.isState()) {
                state = true;
                break;
            }
        }
        if (state) {
            workbook.write(out);
        } else
            throw new IOException("没有添加数据无法进行保存");
    }

    @Override
    public void close() {
        try {
            if (workbook != null)
                workbook.close();
        } catch (IOException e) {
            return;
        }
    }

    @Override
    public Workbook createWorkbook() throws IOException {
        if (workbook == null)
            if (is != null)
                workbook = WorkbookFactory.create(is);
            else
                workbook = WorkbookFactory.create(state);
        return workbook;
    }

}