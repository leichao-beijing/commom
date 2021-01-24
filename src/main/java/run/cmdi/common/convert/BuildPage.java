package run.cmdi.common.convert;

import java.util.List;

public interface BuildPage<T>   {
    ConvertOutPage<T> buildPage();

    ConvertOutPage<T> buildPage(String pageName);

    ConvertOutPage<T> buildPage(Integer pageIndex);

    List<ConvertOutPage> buildPageList();

    int size();
}
