package chess;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    // These are the two variables that will be stored in each instantiation of this class.
    private final int row;
    private final int col;

    /**
     * this is where we instantiate the position on the board. You have to pass in row and column.
     *
     * @param row counts vertically starting at 1 from the bottom of the board.
     * @param col counts horizontally left to right starting at 1.
     */
    public ChessPosition(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public ChessPosition(String substring) {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        int col = 0;
        for (int i = 1; i <= 8; i += 1) {
            if (Objects.equals(list.get(i - 1), String.valueOf(substring.charAt(0)))) {
                col = i;
            }
        }
        this.row = Integer.parseInt(String.valueOf(substring.charAt(1)));
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
     *
     * @return is just what it is printing. should look like this: {1, 2}
     */
    @Override
    public String toString() {
        ArrayList<String> list = new ArrayList<>();
        list.add("a");
        list.add("b");
        list.add("c");
        list.add("d");
        list.add("e");
        list.add("f");
        list.add("g");
        list.add("h");
        String column = list.get(col - 1);
        return column + row;
    }

    /**
     * This is where I override the equals method so that two chess positions can be equivalent if they are the
     * same location, but not necessarily the same object.
     *
     * @param o the reference object with which to compare.
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

