package run.cmid.common.reader.model.to;

import org.apache.poi.ss.usermodel.BorderStyle;

import lombok.Getter;
import lombok.Setter;

/**
 * 
 * @author leichao
 * @date 2019年10月28日--下午3:46:56
 */
@Getter
@Setter
public class BorderMode {

    private BorderStyle left;
    private BorderStyle top;
    private BorderStyle bottom;
    private BorderStyle right;

    private int width;

}
