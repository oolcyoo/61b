package blocks;

import java.util.ArrayList;
import java.util.Random;

import static blocks.Utils.*;

/** A creator of random Blocks puzzles.
 *  @author Ree
 */
class PuzzleGenerator implements PuzzleSource {

    /** A new PuzzleGenerator whose random-number source is seeded
     *  with SEED. */
    PuzzleGenerator(long seed) {
        _random = new Random(seed);
    }

    /* By default, the convention is that overriding methods have the same
     * comment as on the method they override. See PuzzleSource. */
    @Override
    public boolean deal(Model model, int handSize) {
        assert handSize > 0;
        model.clearHand();
        int i = 0;
        ArrayList dealpieces = new ArrayList();
        while (handSize != i) {
            int rand = _random.nextInt(PIECES.length);
            if (!dealpieces.contains(rand)) {
                i += 1;
                model.deal(PIECES[rand]);
                dealpieces.add(_random.nextInt(rand));
            }
        }
        return true;
    }

    @Override
    public void setSeed(long seed) {
        _random.setSeed(seed);
    }

    /** My PNRG. */
    private Random _random;

    /** Pieces available to be dealt to hand. */
    static final Piece[] PIECES = {
        new Piece("*** *** ***"),
        new Piece("** **"),
        new Piece("*"),

        new Piece("** ** **"),
        new Piece("*** ***"),

        new Piece("**"),
        new Piece("* *"),
        new Piece("***"),
        new Piece("* * *"),

        new Piece("** *."),
        new Piece("** .*"),
        new Piece("*. **"),
        new Piece(".* **"),

        new Piece("** *. **"),
        new Piece("** .* **"),
        new Piece("*.* ***"),
        new Piece("*** *.*"),

        new Piece("*.. *** *.."),
        new Piece("..* *** ..*"),
        new Piece(".*. .*. ***"),
        new Piece("*** .*. .*."),

        new Piece("*** ..* ..*"),
        new Piece("..* ..* ***"),
        new Piece("*** *.. *.."),
        new Piece("*.. *.. ***"),

        new Piece("** *. *."),
        new Piece("** .* .*"),
        new Piece("*.. ***"),
        new Piece("*** *.."),

        new Piece("*. .*"),
        new Piece(".* *."),

        new Piece("*.. .*. ..*"),
        new Piece("..* .*. *.."),

        new Piece("*.* .*. *.*"),
        new Piece(".*. *.* .*."),

        new Piece(".*. *** .*."),

        new Piece(".** **."),
        new Piece("**. .**"),
        new Piece("*. ** .*"),
        new Piece(".* ** *.")
    };

}
