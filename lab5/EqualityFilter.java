import java.util.Objects;

/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this.colName = colName;
        this.match = match;
        this.col = input.colNameToIndex(colName);

    }

    @Override
    protected boolean keep() {
        if (Objects.equals(candidateNext().getValue(col), match)){
            return true;
        }
        return false;
    }

    protected String colName;
    protected String match;
    protected int col;
}
