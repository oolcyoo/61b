import java.util.LinkedList;

/**
 * Scheme-like pairs that can be used to form a list of integers.
 *
 * @author P. N. Hilfinger; updated by Linda Deng (1/26/2022)
 */
public class IntDList {

    /**
     * First and last nodes of list.
     */
    protected DNode _front, _back;

    /**
     * An empty list.
     */
    public IntDList() {
        _front = _back = null;
    }

    /**
     * @param values the ints to be placed in the IntDList.
     */
    public IntDList(Integer... values) {
        _front = _back = null;
        for (int val : values) {
            insertBack(val);
        }
    }

    /**
     * @return The first value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getFront() {
        return _front._val;
    }

    /**
     * @return The last value in this list.
     * Throws a NullPointerException if the list is empty.
     */
    public int getBack() {
        return _back._val;
    }

    /**
     * @return The number of elements in this list.
     */
    public int size() {
        // TODO: Implement this method and return correct value
        int count = 0;
        DNode first = _front;
        while (first != null) {
            count ++;
            first = first._next;
        }
        return count;
    }

    /**
     * @param index index of node to return,
     *          where index = 0 returns the first node,
     *          index = 1 returns the second node, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The node at index index
     */
    private DNode getNode(int index) {
        // TODO: Implement this method and return correct node
        DNode now = _front;
        while (index != 0){
            now = now._next;
            index -= 1;
        }
        return now;
    }

    /**
     * @param index index of element to return,
     *          where index = 0 returns the first element,
     *          index = 1 returns the second element,and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return The integer value at index index
     */
    public int get(int index) {
        // TODO: Implement this method (Hint: use `getNode`)

        return getNode(index)._val;
    }

    /**
     * @param d value to be inserted in the front
     */
    public void insertFront(int d) {
        // TODO: Implement this method

        DNode new_front = new DNode(d);
        if (_front == null){
            _back = new_front;
            _front = new_front;
        } else if (new_front != null){
            new_front._next = _front;
            _front._prev = new_front;
            _front = new_front;
        } else {
            _front = new_front;
            _back = new_front;
        }
    }

    /**
     * @param d value to be inserted in the back
     */
    public void insertBack(int d) {
        // TODO: Implement this method
        DNode new_back = new DNode(d);
        if (_back == null){
            _back = new_back;
            _front = new_back;

        } else if (new_back != null) {
            new_back._prev = _back;
            _back._next = new_back;
            _back = new_back;
        } else {
            _back = new_back;
            _front = new_back;
        }
    }

    /**
     * @param d     value to be inserted
     * @param index index at which the value should be inserted
     *              where index = 0 inserts at the front,
     *              index = 1 inserts at the second position, and so onh.
     *              You can assume index will always be a valid index,
     *              i.e 0 <= index <= size.
     */
    public void insertAtIndex(int d, int index) {
        // TODO: Implement this method
        if (index == 0){
            insertFront(d);
        } else if (index == size()){//else if (getNode(index) == _back){
            insertBack(d);
        } else {
            DNode new_node = new DNode(d);
            DNode behind = getNode(index);
            DNode ahead = getNode(index-1);
            behind._prev = new_node;
            ahead._next = new_node;
            new_node._prev = ahead;
            new_node._next = behind;
        }
    }

    /**
     * Removes the first item in the IntDList and returns it.
     * Assume `deleteFront` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteFront() {
        // TODO: Implement this method and return correct value
        int value = _front._val;
        if (_front == _back) {
            _front = null;
            _back = null;
            return value;
        } else {
            _front._next._prev = null;
            _front = _front._next;
            return value;
        }
    }

    /**
     * Removes the last item in the IntDList and returns it.
     * Assume `deleteBack` is never called on an empty IntDList.
     *
     * @return the item that was deleted
     */
    public int deleteBack() {
        // TODO: Implement this method and return correct value
        int value = _back._val;
        if (_front == _back) {
            _front = null;
            _back = null;
            return value;
        } else {
            _back._prev._next = null;
            _back = _back._prev;
            return value;
        }
    }

    /**
     * @param index index of element to be deleted,
     *          where index = 0 returns the first element,
     *          index = 1 will delete the second element, and so on.
     *          You can assume index will always be a valid index,
     *              i.e 0 <= index < size.
     * @return the item that was deleted
     */
    public int deleteAtIndex(int index) {
        // TODO: Implement this method and return correct value
        if (index == 0){
            return deleteFront();
        } else if (index == size()-1){//else if (getNode(index) == _back){
            return deleteBack();
        } else {
            DNode at_index = getNode(index);
            DNode ahead = getNode(index-1);
            DNode behind = getNode(index+1);
            ahead._next = behind;
            behind._prev = ahead;
            return at_index._val;
        }
    }

    /**
     * @return a string representation of the IntDList in the form
     * [] (empty list) or [1, 2], etc.
     * Hint:
     * String a = "a";
     * a += "b";
     * System.out.println(a); //prints ab
     */
    public String toString() {
        // TODO: Implement this method to return correct value
        if (size() == 0) {
            return "[]";
        }
        String str = "[";
        DNode curr = _front;
        for (; curr._next != null; curr = curr._next) {
            str += curr._val + ", ";
        }
        str += curr._val +"]";
        return str;
        }
    }

    /**
     * DNode is a "static nested class", because we're only using it inside
     * IntDList, so there's no need to put it outside (and "pollute the
     * namespace" with it. This is also referred to as encapsulation.
     * Look it up for more information!
     */
    class DNode {
        /** Previous DNode. */
        protected DNode _prev;
        /** Next DNode. */
        protected DNode _next;
        /** Value contained in DNode. */
        protected int _val;

        /**
         * @param val the int to be placed in DNode.
         */
        protected DNode(int val) {
            this(null, val, null);
        }

        /**
         * @param prev previous DNode.
         * @param val  value to be stored in DNode.
         * @param next next DNode.
         */
        protected DNode(DNode prev, int val, DNode next) {
            _prev = prev;
            _val = val;
            _next = next;
        }
    }

