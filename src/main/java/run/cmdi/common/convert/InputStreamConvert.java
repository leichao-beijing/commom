package run.cmdi.common.convert;

import run.cmdi.common.convert.plugs.TypeAnalysisCsv;
import run.cmdi.common.convert.plugs.TypeAnalysisWorkBook;

import java.io.InputStream;

public final class InputStreamConvert extends ConvertAbstract<InputStream, TypeAnalysis<InputStream>> {
    /**
     * 注册默认的转换器
     */
    private void defaultRegister() {
        register(TypeAnalysisWorkBook.class);//poi注入
        register(TypeAnalysisCsv.class);//csv注入
    }

    public InputStreamConvert(InputStream is, String tagName) {
        super(is);
        defaultRegister();
        if ((this.analysis = getRegister(tagName)) == null)
            throw new NullPointerException("no register tagName:" + tagName + " TypeAnalysis");
    }

    public InputStreamConvert(InputStream is) {
        super(is);
        defaultRegister();
        if ((this.analysis = getRegister()) == null)
            throw new NullPointerException("no register TypeAnalysis");
    }

    public final TypeAnalysis buildAnalysis() {
        return analysis;
    }

    private TypeAnalysis analysis;
//
//    public ConvertPage buildPage() {
//        return analysis.buildPage();
//    }
//
//    public ConvertPage buildPage(String pageName) {
//        return analysis.buildPage(pageName);
//    }
//
//    public ConvertPage buildPage(Integer pageIndex) {
//        return analysis.buildPage(pageIndex);
//    }
//
//    public <T> ConvertPage<T> buildPage(Class<T> t) {
//        return null;
//    }
//
//    public <T> ConvertPage<T> buildPage(String pageName, Class<T> t) {
//        ConvertPage page = buildPage(pageName);
//        return null;
//    }
//
//    public int size() {
//        return analysis.size();
//    }
//
//    public List<ConvertPage> buildPageList() {
//        return analysis.buildPageList();
//    }
}
//work <=> csv <=> list<object>
//work,csv =>list<list<string>> => list<object>
//list<object> ==> sheet,csv