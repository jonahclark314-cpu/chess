package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    //Here is where I make the grid for the chessPieces to be.
    ChessPiece[][] squares = new ChessPiece[8][8];
    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {

        //create pieces for all the white pieces.
        ChessPiece wPawn1 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn2 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn3 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn4 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn5 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn6 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn7 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wPawn8 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
        ChessPiece wRook1 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK);
        ChessPiece wRook2 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK);
        ChessPiece wKnight1 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT);
        ChessPiece wKnight2 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT);
        ChessPiece wBishop1 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP);
        ChessPiece wBishop2 = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP);
        ChessPiece wKing = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING);
        ChessPiece wQueen = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN);

        //describe what positions those white peices should belong.
        ChessPosition wPawn1Pos = new ChessPosition(2,1);
        ChessPosition wPawn2Pos = new ChessPosition(2,2);
        ChessPosition wPawn3Pos = new ChessPosition(2,3);
        ChessPosition wPawn4Pos = new ChessPosition(2,4);
        ChessPosition wPawn5Pos = new ChessPosition(2,5);
        ChessPosition wPawn6Pos = new ChessPosition(2,6);
        ChessPosition wPawn7Pos = new ChessPosition(2,7);
        ChessPosition wPawn8Pos = new ChessPosition(2,8);
        ChessPosition wRook1Pos = new ChessPosition(1,1);
        ChessPosition wRook2Pos = new ChessPosition(1,8);
        ChessPosition wKnight1Pos = new ChessPosition(1,2);
        ChessPosition wKnight2Pos = new ChessPosition(1,7);
        ChessPosition wBishop1Pos = new ChessPosition(1,3);
        ChessPosition wBishop2Pos = new ChessPosition(1,6);
        ChessPosition wKingPos = new ChessPosition(1,5);
        ChessPosition wQueenPos = new ChessPosition(1,4);

        //add the pieces using a method from this Class. (addPiece shown above).
        addPiece(wPawn1Pos, wPawn1);
        addPiece(wPawn2Pos, wPawn2);
        addPiece(wPawn3Pos, wPawn3);
        addPiece(wPawn4Pos, wPawn4);
        addPiece(wPawn5Pos, wPawn5);
        addPiece(wPawn6Pos, wPawn6);
        addPiece(wPawn7Pos, wPawn7);
        addPiece(wPawn8Pos, wPawn8);
        addPiece(wRook1Pos, wRook1);
        addPiece(wRook2Pos, wRook2);
        addPiece(wKnight1Pos, wKnight1);
        addPiece(wKnight2Pos, wKnight2);
        addPiece(wBishop1Pos, wBishop1);
        addPiece(wBishop2Pos, wBishop2);
        addPiece(wKingPos, wKing);
        addPiece(wQueenPos, wQueen);

        // Here is where I made the chess pieces for all the black pieces.
        ChessPiece bPawn1 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn2 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn3 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn4 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn5 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn6 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn7 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bPawn8 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
        ChessPiece bRook1 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK);
        ChessPiece bRook2 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK);
        ChessPiece bKnight1 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT);
        ChessPiece bKnight2 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT);
        ChessPiece bBishop1 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP);
        ChessPiece bBishop2 = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP);
        ChessPiece bKing = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING);
        ChessPiece bQueen = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN);

        //here I made chess positions for where all the black pieces should belong.
        ChessPosition bPawn1Pos = new ChessPosition(7,1);
        ChessPosition bPawn2Pos = new ChessPosition(7,2);
        ChessPosition bPawn3Pos = new ChessPosition(7,3);
        ChessPosition bPawn4Pos = new ChessPosition(7,4);
        ChessPosition bPawn5Pos = new ChessPosition(7,5);
        ChessPosition bPawn6Pos = new ChessPosition(7,6);
        ChessPosition bPawn7Pos = new ChessPosition(7,7);
        ChessPosition bPawn8Pos = new ChessPosition(7,8);
        ChessPosition bRook1Pos = new ChessPosition(8,1);
        ChessPosition bRook2Pos = new ChessPosition(8,8);
        ChessPosition bKnight1Pos = new ChessPosition(8,2);
        ChessPosition bKnight2Pos = new ChessPosition(8,7);
        ChessPosition bBishop1Pos = new ChessPosition(8,3);
        ChessPosition bBishop2Pos = new ChessPosition(8,6);
        ChessPosition bKingPos = new ChessPosition(8,5);
        ChessPosition bQueenPos = new ChessPosition(8,4);

        // Here I add all the black pieces to their correct positions using addPiece method of this class above
        addPiece(bPawn1Pos, bPawn1);
        addPiece(bPawn2Pos, bPawn2);
        addPiece(bPawn3Pos, bPawn3);
        addPiece(bPawn4Pos, bPawn4);
        addPiece(bPawn5Pos, bPawn5);
        addPiece(bPawn6Pos, bPawn6);
        addPiece(bPawn7Pos, bPawn7);
        addPiece(bPawn8Pos, bPawn8);
        addPiece(bRook1Pos, bRook1);
        addPiece(bRook2Pos, bRook2);
        addPiece(bKnight1Pos, bKnight1);
        addPiece(bKnight2Pos, bKnight2);
        addPiece(bBishop1Pos, bBishop1);
        addPiece(bBishop2Pos, bBishop2);
        addPiece(bKingPos, bKing);
        addPiece(bQueenPos, bQueen);
    }


    /**
     * This is where I make the overide equals method.
     * @param o   the reference object with which to compare.
     * @return
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
     * @return
     */
    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    /**
     * this is where I override the to string method so that it becomes more readable.
     * @return
     */
    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
