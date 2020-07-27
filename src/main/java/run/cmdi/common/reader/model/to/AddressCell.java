package run.cmdi.common.reader.model.to;

import lombok.Getter;

/**
 * @author leichao
 */
@Getter
public class AddressCell<R> {

    public AddressCell(int rownum, int column, R value) {
        this.rownum = rownum;
        this.column = column;
        this.value = value;
    }

    private final int rownum;
    private final int column;
    private final R value;
}
