package run.cmdi.common.reader.model.entity;

import lombok.Getter;
import org.apache.poi.ss.util.CellAddress;
import run.cmdi.common.io.TypeName;
import run.cmdi.common.reader.exception.ConverterExcelException;
import run.cmdi.common.validator.model.ValidatorFieldException;

import java.util.HashSet;
import java.util.Set;

/**
 * @author leichao
 */
@Getter
public class CellAddressAndMessage {
    public CellAddressAndMessage(int row, int column, TypeName ex, String message) {
        this.row = row;
        this.column = column;
        add(ex, message);
    }
//
//    public CellAddressAndMessage(int row, int column, ValidatorFieldException ex) {
//        this.row = row;
//        this.column = column;
////        this.ex = ex.getType();
//        if (ex.getMessage() != null)
//            add(ex.getMessage());
//        else add(ex.getType().getTypeName());
//    }
//
//    public CellAddressAndMessage(int row, int column, ConverterExcelException ex) {
//        this.row = row;
//        this.column = column;
//        //this.ex = ex.getType();
//        if (ex.getMessage() != null)
//            //this.message = ex.getMessage();
//            add(ex.getMessage());
//        else add(ex.getType().getTypeName());
//        // this.message = ex.getType().getTypeName();
//
//    }

    public void add(String messages) {
        set.add(messages);
    }

    public void add(TypeName ex, String messages) {
        setEx.add(ex.getTypeName());
        set.add(messages);
    }

    private final Set<String> set = new HashSet<>();
    private final Set<String> setEx = new HashSet<>();
    private final int row;
    private final int column;
//    private final TypeName ex;
//    private String message;
}
