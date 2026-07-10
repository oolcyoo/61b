/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter {

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        this.colName = colName;
        this.subStr = subStr;
        this.col = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        if (candidateNext().getValue(col).contains(subStr)){
            return true;
        }
        return false;
    }

    protected String colName;
    protected String subStr;
    protected int col;
}
