import java.util.Objects;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter {

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this.colName1 = colName1;
        this.colName2 = colName2;
        this.col1 = input.colNameToIndex(colName1);
        this.col2 = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep() {
        if (Objects.equals(candidateNext().getValue(col1), candidateNext().getValue(col2))){
            return true;
        }
        return false;
    }

    protected String colName1;
    protected String colName2;
    private int col1;
    private int col2;
}
