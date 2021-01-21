package run.cmdi.common.convert.plugs;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvRow;
import run.cmdi.common.convert.ConvertPage;

import java.util.ArrayList;
import java.util.List;

public class ConvertPageCsv implements ConvertPage<Object> {
    public ConvertPageCsv(CsvData csvData) {
        this.csvData = csvData;
    }

    private CsvData csvData;

    @Override
    public List<Object> getValues(Integer index) {
        if (index == null)
            throw new NullPointerException("index is null");
        List rawList = csvData.getRow(index).getRawList();
        return rawList;
    }

    @Override
    public List<List<Object>> getAll() {
        List list = new ArrayList();
        csvData.getRows().forEach(value ->
                list.add(value)
        );
        return list;
    }

    @Override
    public int size() {
        return csvData.getRowCount();
    }
}
