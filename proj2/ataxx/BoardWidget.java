/* Skeleton code copyright (C) 2008, 2022 Paul N. Hilfinger and the
 * Regents of the University of California.  Do not distribute this or any
 * derivative work without permission. */

package ataxx;

import ucb.gui2.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import java.awt.event.MouseEvent;

import java.util.concurrent.ArrayBlockingQueue;

import static ataxx.PieceColor.*;
import static ataxx.Utils.*;

/** Widget for displaying an Ataxx board.
 *  @author Ree
 */
class BoardWidget extends Pad  {

    /** Length of side of one square, in pixels. */
    static final int SQDIM = 50;
    /** Number of squares on a side. */
    static final int SIDE = Board.SIDE;
    /** Radius of circle representing a piece. */
    static final int PIECE_RADIUS = 15;
    /** Dimension of a block. */
    static final int BLOCK_WIDTH = 40;

    /** Color of red pieces. */
    private static final Color RED_COLOR = Color.RED;
    /** Color of blue pieces. */
    private static final Color BLUE_COLOR = Color.BLUE;
    /** Color of painted lines. */
    private static final Color LINE_COLOR = Color.BLACK;
    /** Color of blank squares. */
    private static final Color BLANK_COLOR = Color.WHITE;
    /** Color of selected squared. */
    private static final Color SELECTED_COLOR = new Color(150, 150, 150);
    /** Color of blocks. */
    private static final Color BLOCK_COLOR = Color.BLACK;

    /** Stroke for lines. */
    private static final BasicStroke LINE_STROKE = new BasicStroke(1.0f);
    /** Stroke for blocks. */
    private static final BasicStroke BLOCK_STROKE = new BasicStroke(5.0f);

    /** A new widget sending commands resulting from mouse clicks
     *  to COMMANDQUEUE. */
    BoardWidget(ArrayBlockingQueue<String> commandQueue) {
        _commandQueue = commandQueue;
        setMouseHandler("click", this::handleClick);
        _dim = SQDIM * SIDE;
        _blockMode = false;
        setPreferredSize(_dim, _dim);
        setMinimumSize(_dim, _dim);
    }

    /** Indicate that SQ (of the form CR) is selected, or that none is
     *  selected if SQ is null. */
    void selectSquare(String sq) {
        if (sq == null) {
            _selectedCol = _selectedRow = 0;
        } else {
            _selectedCol = sq.charAt(0);
            _selectedRow = sq.charAt(1);
        }
        repaint();
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {
        g.setStroke(LINE_STROKE);
        g.setColor(BLANK_COLOR);
        g.fillRect(0, 0, _dim, _dim);
        g.setColor(LINE_COLOR);

        for (int i = 0; i < SIDE + 1; i++) {
            g.drawLine(SQDIM * i, 0, SQDIM * i, SIDE * SQDIM);
        }
        for (int i = 0; i < SIDE + 1; i++) {
            g.drawLine(0, SQDIM * i, SIDE * SQDIM, SQDIM * i);
        }

        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                drawBlock(g, i, j);
            }
        }
    }

    /** Draw a block centered at (CX, CY) on G. */
    void drawBlock(Graphics2D g, int cx, int cy) {
        int halfSq = SQDIM / 2;
        int diameter = PIECE_RADIUS * 2;
        int spaces = halfSq - PIECE_RADIUS;
        int sulx = cx * SQDIM + spaces;
        int suly = cy * SQDIM + spaces;
        char col = (char) ('a' + cx);
        char row = (char) ('7' - cy);
        if (_model.get(col, row) == RED) {
            g.setColor(RED_COLOR);
            g.fillOval(sulx, suly, diameter, diameter);
        } else if (_model.get(col, row) == BLUE) {
            g.setColor(BLUE_COLOR);
            g.fillOval(sulx, suly, diameter, diameter);
        } else if (_model.get(col, row) == BLOCKED) {
            int useful = BLOCK_WIDTH;
            int empty = (SQDIM - useful) / 2;
            int x = cx * SQDIM + empty;
            int y = cy * SQDIM + empty;
            int z = y + useful;
            int a = x + useful;

            int ax = x;
            int ay = y + useful / 2;
            int az = a;

            int thex = x + useful / 2;
            int they = y;
            int thez = z;

            g.setColor(BLOCK_COLOR);
            g.setStroke(BLOCK_STROKE);
            g.drawRect(x, y, useful, useful);
            g.drawLine(x, y, a, z);
            g.drawLine(x, z, a, y);
            g.drawLine(ax, ay, az, ay);
            g.drawLine(thex, they, thex, thez);
        } else {
            g.setColor(BLANK_COLOR);
            g.fillOval(sulx, suly, diameter, diameter);
        }
    }

    /** Clear selected block, if any, and turn off block mode. */
    void reset() {
        _selectedRow = _selectedCol = 0;
        setBlockMode(false);
    }

    /** Set block mode on iff ON. */
    void setBlockMode(boolean on) {
        _blockMode = on;
    }

    /** Issue move command indicated by mouse-click event WHERE. */
    private void handleClick(String unused, MouseEvent where) {
        int x = where.getX(), y = where.getY();
        char mouseCol, mouseRow;
        if (where.getButton() == MouseEvent.BUTTON1) {
            mouseCol = (char) (x / SQDIM + 'a');
            mouseRow = (char) ((SQDIM * SIDE - y) / SQDIM + '1');
            if (mouseCol >= 'a' && mouseCol <= 'g'
                && mouseRow >= '1' && mouseRow <= '7') {
                if (_blockMode) {
                    if (_model.get(mouseCol, mouseRow).equals(EMPTY)) {
                        String oYeah =
                                String.format("block %c%c", mouseCol, mouseRow);
                        _commandQueue.offer(oYeah);
                    }
                    _selectedCol = _selectedRow = 0;
                } else {
                    if (_selectedCol != 0) {
                        if (_model.get(_selectedCol, _selectedRow)
                                == _model.whoseMove()) {
                            if (_model.get(mouseCol, mouseRow).equals(EMPTY)) {
                                String oYea = String.format("%c%c-%c%c",
                                        _selectedCol,
                                        _selectedRow, mouseCol, mouseRow);
                                _commandQueue.offer(oYea);
                            }
                        }
                        _selectedCol = _selectedRow = 0;
                    } else {
                        _selectedCol = mouseCol;
                        _selectedRow = mouseRow;
                    }
                }
            }
        }
        repaint();
    }

    public synchronized void update(Board board) {
        _model = new Board(board);
        repaint();
    }

    /** Dimension of current drawing surface in pixels. */
    private int _dim;

    /** Model being displayed. */
    private static Board _model;

    /** Coordinates of currently selected square, or '\0' if no selection. */
    private char _selectedCol, _selectedRow;

    /** True iff in block mode. */
    private boolean _blockMode;

    /** Destination for commands derived from mouse clicks. */
    private ArrayBlockingQueue<String> _commandQueue;
}
