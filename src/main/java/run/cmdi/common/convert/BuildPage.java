package run.cmdi.common.convert;

import java.io.InputStream;
import java.util.List;

public interface BuildPage<T>   {
    ConvertPage<T> buildPage();

    ConvertPage<T> buildPage(String pageName);

    ConvertPage<T> buildPage(Integer pageIndex);

    List<ConvertPage> buildPageList();

    int size();
}
