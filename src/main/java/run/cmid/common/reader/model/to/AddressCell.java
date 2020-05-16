package run.cmid.common.reader.model.to;

import lombok.Getter;

/**
 * 
 * @author leichao
 */
@Getter
public class AddressCell<R> {

    public AddressCell(int rowumn, int colmun, R value) {
        this.rowumn = rowumn;
        this.colmun = colmun;
        this.value = value;
    }
    private final int rowumn;
    private final int colmun;
    private final R value;
}
