package run.cmdi.common.utils;

import run.cmdi.common.poi.model.ReaderPoiConfig;

import java.io.InputStream;

public class ReaderUtils<T> {
    private Class<T> clazz;
    private InputStream is;
    private ReaderPoiConfig config;

    public ReaderUtils(Class<T> clazz, InputStream is) {
        this.clazz = clazz;
        this.is = is;
        this.config = new ReaderPoiConfig();
    }

    public ReaderUtils(Class<T> clazz, InputStream is, ReaderPoiConfig config) {
        this.clazz = clazz;
        this.is = is;
        this.config = config;
    }

//    public EntityBuild<T, Sheet, Cell> build() throws IOException, ConverterException {
//        return build(0);
//    }

//    public EntityBuild<T, Sheet, Cell> build(int readRow) throws IOException, ConverterException {
//        ExcelEntityBuildings<T> ee = new ExcelEntityBuildings<T>(clazz, config);
//        return ee.find(PoiReader.build(is, config), readRow);
//    }


//    public EntityResults<T, Sheet, Cell> result(int readRow) throws IOException, ConverterException {
//        return build(readRow).build();
//    }

//    public EntityResults<T, Sheet, Cell> result() throws IOException, ConverterException {
//        return result(0);
//    }

    public static InputStream getResourceAsStream(String str) {
        return ClassLoader.getSystemResourceAsStream(str);
    }
}
