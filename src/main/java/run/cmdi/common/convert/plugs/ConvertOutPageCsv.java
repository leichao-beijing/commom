package run.cmdi.common.convert.plugs;

import cn.hutool.core.text.csv.CsvData;
import run.cmdi.common.convert.ConvertOutPage;

import java.util.ArrayList;
import java.util.List;

public class ConvertOutPageCsv implements ConvertOutPage<List> {
    public ConvertOutPageCsv(CsvData csvData) {
        this.csvData = csvData;
    }

    private CsvData csvData;

    @Override
    public List getValues(Integer index) {
        if (index == null)
            throw new NullPointerException("index is null");
        List rawList = csvData.getRow(index).getRawList();
        return rawList;
    }

    @Override
    public List getAll() {
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
