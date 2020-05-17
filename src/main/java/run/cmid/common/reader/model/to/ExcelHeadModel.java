package run.cmid.common.reader.model.to;

import lombok.Getter;
import lombok.Setter;
import run.cmid.common.reader.annotations.ConverterHead;

/**
 * 
 * @author leichao
 */
@Getter
@Setter
public class ExcelHeadModel {

    public ExcelHeadModel() {
    }

    public ExcelHeadModel(ConverterHead head) {
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
