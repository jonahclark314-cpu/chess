package chess;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //Here is where I make the grid for the chessPieces to be.
    private final ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {

    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow() - 1][position.getColumn() - 1] = piece;
    }

    /**
     * Removes piece from a given locaiton on the board.
     *
     * @param position The given position in which we need to remove a piece. It is allowed to be null.
     */
    public void removePiece(ChessPosition position) {
        squares[position.getRow() - 1][position.getColumn() - 1] = null;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow() - 1][position.getColumn() - 1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //add the pieces using a method from this Class. (addPiece shown above).
        addPiece(new ChessPosition(2, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(2, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(1, 1), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 8), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(1, 2), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 7), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(1, 3), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 6), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(1, 5), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(1, 4), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));

        // Here I add all the black pieces to their correct positions using addPiece method of this class above
        addPiece(new ChessPosition(7, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(7, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
        addPiece(new ChessPosition(8, 1), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 8), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
        addPiece(new ChessPosition(8, 2), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 7), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
        addPiece(new ChessPosition(8, 3), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 6), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
        addPiece(new ChessPosition(8, 5), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
        addPiece(new ChessPosition(8, 4), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
    }


    /**
     * This is where I make the overide equals method.
     *
     * @param o the reference object with which to compare.
     * @return returns true if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    /**
     * This is where I do the override hash code.
     *
     * @return returns integer
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }


    private String format(Object square) {
        if (square == null) {
            return "   ";
        } else {
            return square.toString();
        }
    }


    /**
     * this is where I override the to string method so that it becomes more readable.
     *
     * @return returns string to print
     */
    @Override
    public String toString() {
        StringBuilder totalString = new StringBuilder("\n");
        ArrayList<String> colHeader = new ArrayList<>(List.of(" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "));
        ArrayList<String> rowHeader = new ArrayList<>(List.of(" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));

        for (int i = 9; i >= 0; i--) {
            if (i == 9 || i == 0) {
                totalString.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
                for (int j = 0; j <= 9; j++) {
//                    totalString.append("|");
                    if (j != 0 && j != 9) {
                        totalString.append(colHeader.get(j - 1));
                    } else {
                        totalString.append("   ");
                    }
                }
                totalString.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            } else {
                for (int j = 0; j <= 9; j++) {
//                    totalString.append("|");
                    if (j != 0 && j != 9) {
                        if ((j + i) % 2 == 0) {
                            totalString.append(EscapeSequences.SET_BG_COLOR_BLACK);
                        } else {
                            totalString.append(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                        totalString.append(format(squares[i - 1][j - 1]));
                        totalString.append(EscapeSequences.RESET_BG_COLOR);
                    } else {
                        totalString.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
                        totalString.append(rowHeader.get(i - 1));
                        totalString.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
                    }

                }
            }
            totalString.append("\n");
        }
        return totalString.toString();
    }

    public String show(String color) {
        if (Objects.equals(color, "WHITE")) {
            return toString();
        } else if (Objects.equals(color, "BLACK")) {
            return blackToString();
        } else {
            return "Please enter in the color you are observing from!";
        }

    }

    private String blackToString() {
        StringBuilder totalString = new StringBuilder("\n");
        ArrayList<String> colHeader = new ArrayList<>(List.of(" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "));
        ArrayList<String> rowHeader = new ArrayList<>(List.of(" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "));

        for (int i = 0; i <= 9; i++) {
            if (i == 9 || i == 0) {
                totalString.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
                for (int j = 9; j >= 0; j--) {
//                    totalString.append("|");
                    if (j != 0 && j != 9) {
                        totalString.append(colHeader.get(j - 1));
                    } else {
                        totalString.append("   ");
                    }
                }
                totalString.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
            } else {
                for (int j = 9; j >= 0; j--) {
//                    totalString.append("|");
                    if (j != 0 && j != 9) {
                        if ((j + i) % 2 == 0) {
                            totalString.append(EscapeSequences.SET_BG_COLOR_BLACK);
                        } else {
                            totalString.append(EscapeSequences.SET_BG_COLOR_WHITE);
                        }
                        totalString.append(format(squares[i - 1][j - 1]));
                        totalString.append(EscapeSequences.RESET_BG_COLOR);
                    } else {
                        totalString.append(EscapeSequences.SET_BG_COLOR_DARK_GREY + EscapeSequences.SET_TEXT_COLOR_WHITE);
                        totalString.append(rowHeader.get(i - 1));
                        totalString.append(EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR);
                    }

                }
            }
            totalString.append("\n");
        }
        return totalString.toString();

    }


}


