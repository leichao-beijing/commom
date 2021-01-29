package run.cmdi.common.convert;

import cn.hutool.core.text.csv.CsvData;
import cn.hutool.core.text.csv.CsvUtil;
import cn.hutool.core.text.csv.CsvWriter;
import org.mozilla.universalchardet.UniversalDetector;

import java.io.*;
import java.util.*;
import java.util.stream.IntStream;

public class WriterCsv implements WriterEntity<String> {
    public WriterCsv(InputStream is) throws IOException {
        this.encoded = UniversalDetector.detectCharset(is);
        if (encoded == null)
            throw new IOException("");
        is.reset();
        InputStreamReader isr = new InputStreamReader(is, encoded);
        BufferedReader reader = new BufferedReader(isr);
        CsvData csvData = CsvUtil.getReader().read(reader);
        csvData.getRows().forEach(v -> add(v.getRawList()));
    }

    public WriterCsv(String encoded) {
        this.encoded = encoded;
    }

    private static Comparator<Integer> COMPARATOR = new Comparator<Integer>() {
        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    };
    private final String encoded;
    private Map<Integer, Map<Integer, String>> storage = new TreeMap<>(COMPARATOR);

    @Override
    public void add(int rownum, int column, Object value, String s) {
        Map<Integer, String> row = storage.get(rownum);
        if (row == null)
            storage.put(rownum, row = new TreeMap<>(COMPARATOR));
        if (value == null)
            row.put(column, "");
        else
            row.put(column, value.toString());
    }

    private void add(int row, List<String> list) {
        TreeMap map = new TreeMap<>(COMPARATOR);
        IntStream.range(0, list.size()).forEach(i -> map.put(i, list.get(i)));
        storage.put(row, map);
    }

    private void add(List<String> list) {
        TreeMap map = new TreeMap<>(COMPARATOR);
        IntStream.range(0, list.size()).forEach(i -> {
            String value = list.get(i);
            map.put(i, value == null ? "" : value);
        });
        storage.put(storage.size(), map);
    }

    private String[] toValues(Map<Integer, String> map) {
        List<String> list = new ArrayList<>();
        Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
        int i = 0;
        while (it.hasNext()) {
            Map.Entry<Integer, String> next = it.next();
            Integer index = next.getKey();
            String value = next.getValue();
            for (; index > i; i++)
                list.add("");
            list.add(value);
            i++;
        }
        return list.toArray(new String[list.size()]);
    }

    private CsvWriter writer;

    @Override
    public void save(OutputStream os) throws IOException {
        OutputStreamWriter osw = new OutputStreamWriter(os, this.encoded);
        this.writer = CsvUtil.getWriter(osw);
        int line = 0;
        Iterator<Map.Entry<Integer, Map<Integer, String>>> it = storage.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Map<Integer, String>> next = it.next();
            Integer index = next.getKey();
            for (; index > line; line++) writer.writeLine();
            writer.write(toValues(next.getValue()));
            line++;

        }
    }

    @Override
    public void close() throws IOException {
        if (writer != null) {
            writer.flush();
            writer.close();
        }
    }
}

