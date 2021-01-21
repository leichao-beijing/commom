package run.cmdi.common.convert;

import java.util.List;

public class TypeAnalysisClass implements TypeAnalysis<Class> {
    @Override
    public void close() {

    }

    @Override
    public ConvertPage buildPage() {
        return null;
    }

    @Override
    public ConvertPage buildPage(String pageName) {
        return null;
    }

    @Override
    public ConvertPage buildPage(Integer pageIndex) {
        return null;
    }

    @Override
    public List<ConvertPage> buildPageList() {
        return null;
    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public String getRegisterName() {
        return "class";
    }

    @Override
    public boolean isSupport(Class value) {
        return false;
    }
}
