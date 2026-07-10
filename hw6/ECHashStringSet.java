
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;
/** A set of String values.
 *  @author Ree
 */
class ECHashStringSet implements StringSet {

    private LinkedList<String> [] _buckets;

    private int _nums;

    public ECHashStringSet() {
        _nums = 0;
        _buckets = new LinkedList[5];
    }

    @Override
    public void put(String s) {
        // if we've reached the load limit, we chould resize
        if ((_buckets.length != 0) && (_nums / _buckets.length > 5))  {
            resize();
        }
        // figure out which index i bucket to go to;
        int i = s.hashCode() % _buckets.length;
        if (i < 0) {
            i = i & 0x7fffffff % _buckets.length;
        }
        while (i > _buckets.length) {
            resize();
        }
        //add s to that linked list;
        if (_buckets[i] == null) {
            _buckets[i] = new LinkedList<>();
        }
        _buckets[i].add(s);
        _nums += 1;
    }

    public void resize() {
        LinkedList<String>[] original = _buckets;
        _buckets = new LinkedList[2 * _buckets.length];
        _nums = 0;
        for (LinkedList<String> every: original) {
            if (every != null) {
                for (String s : every) {
                    this.put(s);
                }
            }
        }
    }

    @Override
    public boolean contains(String s) {
        int i = s.hashCode() % _buckets.length;
        if (i < 0) {
            i = i & 0x7fffffff % _buckets.length;
        }
        if (_buckets[i] != null) {
            if (_buckets[i].contains(s)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> newA = new ArrayList<>();
        for (LinkedList<String> every: _buckets) {
            if (every != null) {
                for (String s: every) {
                    newA.add(s);
                }
            }
        } return newA;
    }
}
