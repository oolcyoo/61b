package enigma;
import java.util.HashMap;
import java.util.Collection;


import static enigma.EnigmaException.*;

/** Class that represents a complete enigma machine.
 *  @author
 */
class Machine {

    /** A new Enigma machine with alphabet ALPHA, 1 < NUMROTORS rotor slots,
     *  and 0 <= PAWLS < NUMROTORS pawls.  ALLROTORS contains all the
     *  available rotors. */

    private int _numRotors;
    private Rotor[] _rotors;
    private Permutation _plugboard;
    private final int _pawls;
    private HashMap<String, Rotor> _allRotors = new HashMap<>();




    Machine(Alphabet alpha, int numRotors, int pawls,
            Collection<Rotor> allRotors) {
        _alphabet = alpha;
        _pawls = pawls;
        _numRotors = numRotors;
        _rotors = new Rotor[numRotors];
        _allRotors = new HashMap<String, Rotor>();
        for (Rotor i: allRotors) {
            _allRotors.put(i.name(), i);
        }
    }

    /** Return the number of rotor slots I have. */
    int numRotors() {
        return _numRotors;
    }

    /** Return the number pawls (and thus rotating rotors) I have. */
    int numPawls() {
        return _pawls;
    }

    /** Return Rotor #K, where Rotor #0 is the reflector, and Rotor
     *  #(numRotors()-1) is the fast Rotor.  Modifying this Rotor has
     *  undefined results. */
    Rotor getRotor(int k) {
        return _rotors[k];
    }

    Alphabet alphabet() {
        return _alphabet;
    }

    /** Set my rotor slots to the rotors named ROTORS from my set of
     *  available rotors (ROTORS[0] names the reflector).
     *  Initially, all rotors are set at their 0 setting. */
    void insertRotors(String[] rotors) {
        for (int i = 0; i < rotors.length; i++) {
            Rotor ele = _allRotors.get(rotors[i]);
            if (ele == null) {
                throw error("We don't have that rotor.");
            }
            for (int j = 0; j != i && j < _rotors.length; j++) {
                if (rotors[i] == rotors[j]) {
                    throw error("repeated Rotor");
                }
            }
            _rotors[i] = ele;
        }
    }


    /** Set my rotors according to SETTING, which must be a string of
     *  numRotors()-1 characters in my alphabet. The first letter refers
     *  to the leftmost rotor setting (not counting the reflector).  */
    void setRotors(String setting) {
        if (setting.length() == numRotors() - 1) {
            for (int i = 0; i < setting.length(); i++) {
                _rotors[i + 1].set(setting.charAt(i));
            }
        } else {
            throw error("setting doesn't match rotors amount");
        }
    }

    /** Return the current plugboard's permutation. */
    Permutation plugboard() {
        return _plugboard;
    }

    /** Set the plugboard to PLUGBOARD. */
    void setPlugboard(Permutation plugboard) {
        _plugboard = plugboard;
    }

    /** Returns the result of converting the input character C (as an
     *  index in the range 0..alphabet size - 1), after first advancing
     *  the machine. */
    int convert(int c) {
        advanceRotors();
        if (Main.verbose()) {
            System.err.printf("[");
            for (int r = 1; r < numRotors(); r += 1) {
                System.err.printf("%c",
                        alphabet().toChar(getRotor(r).setting()));
            }
            System.err.printf("] %c -> ", alphabet().toChar(c));
        }
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(c));
        }
        c = applyRotors(c);
        c = plugboard().permute(c);
        if (Main.verbose()) {
            System.err.printf("%c%n", alphabet().toChar(c));
        }
        return c;
    }

    /** Advance all rotors to their next position. */
    private void advanceRotors() {

        if (_rotors.length == 0) {
            throw error("No Rotors");
        }

        boolean [] booleanArray = new boolean[_rotors.length];
        int fastPos = _rotors.length - 1;
        booleanArray[fastPos] = true;

        for (int i = 0; i < fastPos; i++) {
            //if (_rotors[i+1].atNotch() && _rotors[i].rotates()) {
            //    booleanArray[i] = true;
            //}
            if (_rotors[i + 1].atNotch() && _rotors[i].rotates()) {
                booleanArray[i] = true;
            }
            if (_rotors[i].atNotch() && _rotors[i - 1].rotates()) {
                booleanArray[i] = true;
                booleanArray[i - 1] = true;
            }
        }

        for (int i = 0; i < booleanArray.length; i++) {
            if (booleanArray[i]) {
                _rotors[i].advance();
            }
        }
    }

    /** Return the result of applying the rotors to the character C (as an
     *  index in the range 0..alphabet size - 1). */
    private int applyRotors(int c) {
        int current = _plugboard.wrap(c);
        for (int i = _rotors.length - 1; i >= 0; i--) {
            current = _rotors[i].convertForward(current);
        }
        for (int i = 1; i < _rotors.length; i++) {
            current = _rotors[i].convertBackward(current);
        }
        return current;
    }

    /** Returns the encoding/decoding of MSG, updating the state of
     *  the rotors accordingly. */
    String convert(String msg) {
        String message = "";
        String [] cut = msg.split(" ");
        for (String word: cut) {
            for (int i = 0; i < word.length(); i++) {
                int megInt = _alphabet.toInt(word.charAt(i));
                message += alphabet().toChar(convert(megInt));
            }
        }
        return message;
    }

    /** Common alphabet of my rotors. */
    private final Alphabet _alphabet;


    void emptyRotors(Rotor[] setting) {
        _rotors = setting;
    }

    // FIXME: ADDITIONAL FIELDS HERE, IF NEEDED.
}
