package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {
    private final int row;
    private final int col;

    /**
     * this is where we instantiate the position on the board. You have to pass in row and column.
     * @param row counts vertically starting at 1 from the bottom of the board.
     * @param col counts horizontally left to right starting at 1.
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;

    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() {
        return row;
    }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() {
        return col;
    }

    /**
     * This is where I overide the tostring method to make things more readable when printing out the chess position.
     * @return is just what it is printing. should look like this: {1, 2}
     */
    @Override
    public String toString() {
        return "{" + row +
                ", " + col +
                '}';
    }

    /**
     * This is where I override the equals method so that two chess positions can be equivalent if they are the
     * same location, but not necessarily the same object.
     * @param o   the reference object with which to compare.
     * @return this returns true or false if the position the object represents is the same or different
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
