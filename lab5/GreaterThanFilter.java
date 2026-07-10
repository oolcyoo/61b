import java.util.Comparator;
import java.util.Objects;

/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter {

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        this.colname = colName;
        this.ref = ref;
        this.in = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(in).compareTo(ref) >= 0){
            return true;
        }
        return false;
    }

    protected String colname;
    protected String ref;
    protected int in;

}
