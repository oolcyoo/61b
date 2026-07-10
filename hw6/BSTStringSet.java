import java.util.*;

/**
 * Implementation of a BST based String Set.
 * @author Ree
 */
public class BSTStringSet implements StringSet, Iterable<String> {
    /** Creates a new empty set. */
    public BSTStringSet() {
        _root = null;
    }

    private int helper(String s) {
        int count = 0;
        for (int i = 0; i < s.length(); i ++) {
            count += s.indexOf(s.charAt(i));
        }
        return count;
    }

    private Node helper2(String s) {
        if (_root == null) {
            return null;
        }
        Node tem = _root;
        while (true) {
            Node aNode;
            if (s.compareTo(tem.s) < 0) {
                aNode = tem.left;
            } else if (s.compareTo(tem.s) > 0) {
                aNode = tem.right;
            } else {
                return tem;
            }
            if (aNode == null) {
                return tem;
            } else {
                tem = aNode;
            }
        }
    }

    @Override
    public void put(String s) {
        if (_root == null) {
            _root = new Node(s);
        }
        Node help = _root;
        Node tem = _root;
        while (tem != null) {
            help = tem;
            if (tem.s.compareTo(s) < 0) {
                tem = tem.right;
            } else if (tem.s.compareTo(s) > 0) {
                tem = tem.left;
            } else {
                return;
            }
        }
        if (help.s.compareTo(s) < 0) {
            help.right = new Node(s);
        } else if (help.s.compareTo(s) > 0) {
            help.left = new Node(s);
        }
    }

    @Override
    public boolean contains(String s) {
        Node help = _root;
        while (help != null) {
            if (help.s.compareTo(s) < 0) {
                help = help.right;
            } else if (help.s.compareTo(s) > 0) {
                help = help.left;
            } else {
                return true;
            }
        } return false;
    }

    @Override
    public List<String> asList() {
        ArrayList<String> list = new ArrayList<>();
        Iterator<String> goThrough = iterator();
        while (goThrough.hasNext()) {
            list.add(goThrough.next());
        }
        return list;
    }

    /** Represents a single Node of the tree. */
    private static class Node {
        /** String stored in this Node. */
        private String s;
        /** Left child of this Node. */
        private Node left;
        /** Right child of this Node. */
        private Node right;

        /** Creates a Node containing SP. */
        Node(String sp) {
            s = sp;
        }
    }

    /** An iterator over BSTs. */
    private static class BSTIterator implements Iterator<String> {
        /** Stack of nodes to be delivered.  The values to be delivered
         *  are (a) the label of the top of the stack, then (b)
         *  the labels of the right child of the top of the stack inorder,
         *  then (c) the nodes in the rest of the stack (i.e., the result
         *  of recursively applying this rule to the result of popping
         *  the stack. */
        private Stack<Node> _toDo = new Stack<>();

        /** A new iterator over the labels in NODE. */
        BSTIterator(Node node) {
            addTree(node);
        }

        @Override
        public boolean hasNext() {
            return !_toDo.empty();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }

            Node node = _toDo.pop();
            addTree(node.right);
            return node.s;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

        /** Add the relevant subtrees of the tree rooted at NODE. */
        private void addTree(Node node) {
            while (node != null) {
                _toDo.push(node);
                node = node.left;
            }
        }
    }

    @Override
    public Iterator<String> iterator() {
        return new BSTIterator(_root);
    }

    // FIXME: UNCOMMENT THE NEXT LINE FOR PART B
    //@Override
    public Iterator<String> iterator(String low, String high) {
        Iterator<String> It = iterator();
        ArrayList<String> Bounded = new ArrayList<>();
        while (It.hasNext()) {
            String theNext = It.next();
            if (theNext.compareTo(low) >= 0 && theNext.compareTo(high) <= 0) {
                Bounded.add(theNext);
            }
        } return Bounded.iterator();
    }

    /** Root node of the tree. */
    private Node _root;
}