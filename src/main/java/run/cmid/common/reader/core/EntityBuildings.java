package run.cmid.common.reader.core;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import cn.hutool.core.util.ReflectUtil;
import lombok.Getter;
import run.cmid.common.reader.annotations.ConverterHead;
import run.cmid.common.reader.annotations.Index;
import run.cmid.common.reader.exception.ConverterExcelException;
import run.cmid.common.reader.model.FieldDetail;
import run.cmid.common.reader.model.HeadInfo;
import run.cmid.common.reader.model.to.ExcelHeadModel;
import run.cmid.common.utils.ReflectLcUtils;

/**
 * 通过对象class检索Excel内对应sheet
 *
 * @author leichao
 */
@Getter
public class EntityBuildings<T, PAGE, UNIT> {

    @Getter
    private final Class<T> clazz;
    @Getter
    private final List<FieldDetail> list;
    @Getter
    private final ExcelHeadModel headModel;
    @Getter
    private final List<List<String>> indexes;
    // @Getter
    //@Setter
    //private String sheetName;

    /**
     * @param clazz
     */
    public EntityBuildings(Class<T> clazz) {
        this.clazz = clazz;
        ConverterHead head = clazz.getAnnotation(ConverterHead.class);
        if (head == null)
            throw new NullPointerException("@ExcelConverterHead not enable");
        isIndexMethod(head.indexes(), clazz);// 验证index内值是否存在于对象中。
        indexes = getIndexList(head.indexes());
        headModel = new ExcelHeadModel(head);
        //sheetName = head.sheetName();
        List<String> values = new ArrayList<String>();
        for (List<String> index : indexes) {
            values.addAll(index);
        }
        list = ConverterFieldDetail.toList(clazz, headModel, values);
    }



    /**
     * 将指定资源进行实体化
     *
     * @param readHeadRownum 读取头的行号
     * @param resource       实现 BookResources 接口的方法。
     * @throws ConverterExcelException
     */
    public EntityBuild<T, Sheet,Cell> find(int readHeadRownum, BookPage<Workbook,Sheet,Cell> resource)
            throws ConverterExcelException {
        HeadInfo<Sheet,Cell> mode = new FindResource(list, headModel, readHeadRownum).find(resource);
        return new EntityResultBuild<T,Sheet,Cell>(clazz, mode, indexes, readHeadRownum);
    }

    protected static <T> void isIndexMethod(Index[] indexes, Class<T> clazz) {
        HashSet<String> set = new HashSet<String>();
        for (Index index : indexes) {
            String[] values = index.value();
            for (String value : values) {
                set.add(value);
            }
        }
        set.forEach((value) -> {
            if (ReflectUtil.getMethodByName(clazz, false, ReflectLcUtils.methodGetString(value)) == null)
                throw new NullPointerException("index;" + value + " Set method no find.");
        });
    }

    protected static <T> List<List<String>> getIndexList(Index[] indexes) {
        List<List<String>> lists = new ArrayList<>();
        for (Index index : indexes) {
            String[] values = index.value();
            List<String> list = new ArrayList<>();
            for (String value : values) {
                list.add(value);// "get" + ReflectParameter.upperCase(value));
            }
            if (list.size() != 0)
                lists.add(list);
            else
                list.clear();
        }
        return lists;
    }
}
