package enigma;
import java.util.ArrayList;
import java.util.regex.*;
import static enigma.EnigmaException.*;

/** Represents a permutation of a range of integers starting at 0 corresponding
 *  to the characters of an alphabet.
 *  @author
 */
class Permutation {

    /** Set this Permutation to that specified by CYCLES, a string in the
     *  form "(cccc) (cc) ..." where the c's are characters in ALPHABET, which
     *  is interpreted as a permutation in cycle notation.  Characters in the
     *  alphabet that are not included in any cycle map to themselves.
     *  Whitespace is ignored. */

    ArrayList<String> store = new ArrayList<>();

    Permutation(String cycles, Alphabet alphabet) {

        Matcher goThrou = Pattern.compile("\\(([^)]+)\\)").matcher(cycles);
        _alphabet = alphabet;
        while (goThrou.find()) {
            store.add(goThrou.group(1));
        }

        int F = 0;
        int B = 0;
        for (int i = 0; i < cycles.length(); i++) {
            if (cycles.charAt(i) == '(') {
                F += 1;
            }
            if (cycles.charAt(i) == ')') {
                B += 1;
            }
        }
        if (F != B) {
            throw error("Wrong P");
        }
    }

    /** Add the cycle c0->c1->...->cm->c0 to the permutation, where CYCLE is
     *  c0c1...cm. */
    private void addCycle(String cycle) {
        // FIXME
    }

    /** Return the value of P modulo the size of this permutation. */
    final int wrap(int p) {
        int r = p % size();
        if (r < 0) {
            r += size();
        }
        return r;
    }

    /** Returns the size of the alphabet I permute. */
    int size() {
        return alphabet().size();
    }

    /** Return the result of applying this permutation to P modulo the
     *  alphabet size. */

    int permute(int p) {
        p = wrap(p);
        char ch = _alphabet.toChar(p);
        for (int i = 0; i < store.size(); i++) {
            for (int j = 0; j < store.get(i).length(); j++) {
                if (store.get(i).charAt(j) == ch) {
                    int in = (j + 1 + store.get(i).length()) % store.get(i).length();
                    char d = store.get(i).charAt(in);
                    return _alphabet.toInt(d);
                }
            }
        }
        return p;
    }

    /** Return the result of applying the inverse of this permutation
     *  to  C modulo the alphabet size. */
    int invert(int c) {
        c = wrap(c);
        char ch = _alphabet.toChar(c);
        for (int i = 0; i < store.size(); i++) {
            for (int j = 0; j < store.get(i).length(); j++) {
                if (store.get(i).charAt(j) == ch) {
                    int in = (j - 1 + store.get(i).length()) % store.get(i).length();
                    char d = store.get(i).charAt(in);
                    return _alphabet.toInt(d);
                }
            }
        }
        return c;
    }

    /** Return the result of applying this permutation to the index of P
     *  in ALPHABET, and converting the result to a character of ALPHABET. */
    char permute(char p) {
        int c = permute(_alphabet.toInt(p));
        return _alphabet.toChar(c);
    }

    /** Return the result of applying the inverse of this permutation to C. */
    char invert(char c) {
        int p = invert(_alphabet.toInt(c));
        return _alphabet.toChar(p);
    }

    /** Return the alphabet used to initialize this Permutation. */
    Alphabet alphabet() {
        return _alphabet;
    }

    /** Return true iff this permutation is a derangement (i.e., a
     *  permutation for which no value maps to itself). */
    boolean derangement() {
        for (int i = 0; i < store.size(); i++) {
            if (store.get(i).length() == 1) {
                return false;
            }
        }
        return true;
    }

    /** Alphabet of this permutation. */
    private Alphabet _alphabet;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
