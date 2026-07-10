package enigma;

import static enigma.EnigmaException.*;

/** Superclass that represents a rotor in the enigma machine.
 *  @author Ree
 */
class Rotor {

    /** A rotor named NAME whose permutation is given by PERM. */
    Rotor(String name, Permutation perm) {
        _name = name;
        _permutation = perm;
        _settingRee = 0;
    }

    /** Return my name. */
    String name() {
        return _name;
    }

    /** Return my alphabet. */
    Alphabet alphabet() {
        return _permutation.alphabet();
    }

    /** Return my permutation. */
    Permutation permutation() {
        return _permutation;
    }

    /** Return the size of my alphabet. */
    int size() {
        return _permutation.size();
    }

    /** Return true iff I have a ratchet and can move. */
    boolean rotates() {
        return false;
    }

    /** Return true iff I reflect. */
    boolean reflecting() {
        return false;
    }

    /** Return my current setting. */
    int setting() {
        return _settingRee;
    }

    /** Set setting() to POSN.  */
    void set(int posn) {
        //while (posn >= size()) {
        //    posn -= size();
        //}
        _settingRee = posn;
    }

    /** Set setting() to character CPOSN. */
    void set(char cposn) {
        _settingRee = alphabet().toInt(cposn);
    }

    /** Return the conversion of P (an integer in the range 0..size()-1)
     *  according to my permutation. */
    int convertForward(int p) {
        int forward = _permutation.permute(p + _settingRee) - _settingRee;
        //if (forward < setting()) {
        //    forward = forward + this.size();
        //}
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(forward));
        }
        forward = _permutation.wrap(forward);
        return forward;
    }

    /** Return the conversion of E (an integer in the range 0..size()-1)
     *  according to the inverse of my permutation. */
    int convertBackward(int e) {
        int out = _permutation.invert(e + setting()) - _settingRee;
        //if ((out < setting())) {
        //    out = out + size();
        //}
        if (Main.verbose()) {
            System.err.printf("%c -> ", alphabet().toChar(out));
        }
        out = _permutation.wrap(out);
        return out;
    }

    /** Returns the positions of the notches, as a string giving the letters
     *  on the ring at which they occur. */
    String notches() {
        return "";
    }

    /** Returns true iff I am positioned to allow the rotor to my left
     *  to advance. */
    boolean atNotch() {

        for (int i = 0; i < notches().length(); i++) {
            int set = _permutation.wrap(setting());
            if (alphabet().toInt(notches().charAt(i)) == set) {
                return true;
            }
        }
        return false;
    }
    //if (notches().charAt(i).indexOf(alphabet().toChar(setting())) >= 0) {
    //return true;

    /** Advance me one position, if possible. By default, does nothing. */
    void advance() {
    }

    @Override
    public String toString() {
        return "Rotor " + _name;
    }

    /** My name. */
    private final String _name;

    /** The permutation implemented by this rotor in its 0 position. */
    private Permutation _permutation;

    private int _settingRee;

    // FIXME: ADDITIONAL FIELDS HERE, AS NEEDED

}
