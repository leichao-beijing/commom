package run.cmid.common.excel.model.to;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.excel.annotations.ExcelConverterHead;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class ExcelHeadModel {

    public ExcelHeadModel() {
    }

    public ExcelHeadModel(ExcelConverterHead head) {
        if (head == null)
            return;
        maxWorngCount = head.maxWrongCount();
        skipNoAnnotationField = head.skipNoAnnotationField();
        bookTagName=head.bookTagName();
    }

    private int maxWorngCount = 3;

    /**
     * 开始后跳过没有ExcelConverter的Field字段。默认false
     */
    private boolean skipNoAnnotationField = false;
    private String bookTagName;

}
