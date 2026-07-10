/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import java.util.ArrayList;
import java.util.Random;

import static ataxx.PieceColor.*;
import static java.lang.Math.min;
import static java.lang.Math.max;

/** A Player that computes its own moves.
 *  @author Ree
 */
class AI extends Player {

    /** Maximum minimax search depth before going to static evaluation. */
    private static final int MAX_DEPTH = 10;
    /** A position magnitude indicating a win (for red if positive, blue
     *  if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;

    /** A new AI for GAME that will play MYCOLOR. SEED is used to initialize
     *  a random-number generator for use in move computations.  Identical
     *  seeds produce identical behaviour. */
    AI(Game game, PieceColor myColor, long seed) {
        super(game, myColor);
        _random = new Random(seed);
    }

    @Override
    boolean isAuto() {
        return true;
    }

    @Override
    String getMove() {
        if (!getBoard().canMove(myColor())) {
            game().reportMove(Move.pass(), myColor());
            return "-";
        }
        Main.startTiming();
        Move move = findMove();
        Main.endTiming();
        game().reportMove(move, myColor());
        return move.toString();
    }

    /** Return a move for me from the current position, assuming there
     *  is a move. */
    private Move findMove() {
        Board b = new Board(getBoard());
        _lastFoundMove = null;
        if (myColor() == RED) {
            minMax(b, MAX_DEPTH, true, 1, -INFTY, INFTY);
        } else {
            minMax(b, MAX_DEPTH, true, -1, -INFTY, INFTY);
        }
        return _lastFoundMove;
    }

    /** The move found by the last call to the findMove method
     *  above. */
    private Move _lastFoundMove;

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int minMax(Board board, int depth, boolean saveMove, int sense,
                       int alpha, int beta) {
        /* We use WINNING_VALUE + depth as the winning value so as to favor
         * wins that happen sooner rather than later (depth is larger the
         * fewer moves have been made. */
        if (depth == 0 || board.getWinner() != null) {
            return staticScore(board, WINNING_VALUE + depth);
        }

        Move best;
        best = null;
        int bestScore;
        bestScore = 0;

        ArrayList<Move> niuBee = moveCollection(board, myColor());
        for (Move M: niuBee) {
            board.makeMove(M);
            bestScore = minMax(board, depth - 1,
                    false, -sense, alpha, beta);
            if (sense == 1) {
                if (!M.isPass()) {
                    if (bestScore > alpha) {
                        alpha = max(alpha, bestScore);
                        best = M;
                    }
                }
            } else {
                if (!M.isPass()) {
                    if (beta > bestScore) {
                        beta = min(beta, bestScore);
                        best = M;
                    }
                }
            }
            board.undo();
        }

        if (saveMove) {
            _lastFoundMove = best;
        }
        return bestScore;
    }

    /** Return a heuristic value for BOARD.  This value is +- WINNINGVALUE in
     *  won positions, and 0 for ties. */
    private int staticScore(Board board, int winningValue) {
        PieceColor winner = board.getWinner();
        if (winner != null) {
            return switch (winner) {
            case RED -> winningValue;
            case BLUE -> -winningValue;
            default -> 0;
            };
        }
        for (char i = 'a'; i <= 'g'; i++) {
            for (char j = '1'; j <= '7'; j++) {
                PieceColor self = board.get(i, j);
                PieceColor opp = self.opposite();
                if (self == RED) {
                    winningValue += 1;
                    winningValue -= vulnable(i, j, self, opp);
                } else if (self == BLUE) {
                    winningValue -= 1;
                    winningValue += vulnable(i, j, self, opp);
                }
            }
        }
        return winningValue;
    }

    /** Return a @param vulnable vulunerability level of the.
     *  current
     *  @param i
     *  @param opp
     *  @param j
     *  @param self
     *  situation of the current player. */
    private int vulnable(char i, char j, PieceColor self, PieceColor opp) {
        int easyAttack = 0;
        for (int k = -3; k <= 3; k++) {
            for (int l = -3; l <= 3; l++) {
                char newC = (char) (i + k);
                char newR = (char) (j + l);
                PieceColor theColor = getBoard().get(newC, newR);
                if (theColor == EMPTY) {
                    easyAttack += 1;
                } else if (theColor == self || theColor == BLOCKED) {
                    easyAttack -= 1;
                } else if (theColor == opp) {
                    easyAttack += 1;
                }
            }
        }
        return easyAttack;
    }

    /** To store the moves from theMoves.
     *  That's nice.
     *   @return.
     *  @param board
     *  @param color*/
    private ArrayList<Move> moveCollection(Board board, PieceColor color) {
        ArrayList<Move> collection = new ArrayList<>();
        for (char i = 'a'; i <= 'g'; i++) {
            for (char j = '1'; j <= '7'; j++) {
                if (board.get(i, j) == color) {
                    ArrayList<Move> putIn = theMoves(board, i, j);
                    collection.addAll(putIn);
                }
            }
        }
        return collection;
    }

    private ArrayList<Move> theMoves(Board board, char c, char r) {
        ArrayList<Move> collection = new ArrayList<>();
        for (int k = -2; k <= 2; k++) {
            for (int l = -2; l <= 2; l++) {
                char newC = (char) (c + k);
                char newR = (char) (r + l);
                Move moveNow = Move.move(c, r, newC, newR);
                if (board.legalMove(moveNow)) {
                    collection.add(moveNow);
                }
            }
        }
        return collection;
    }
    /** Pseudo-random number generator for move computation. */
    private Random _random = new Random();
}

















