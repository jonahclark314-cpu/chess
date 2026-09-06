package chess;

import java.util.Objects;

/**
 * Represents moving a chess piece on a chessboard
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessMove {

    // Here is where I will create variables for each of the things that need to be tracked for each Chess Move
    private final ChessPosition startPosition;
    private final ChessPosition endPosition;
    private final ChessPiece.PieceType PieceType;

    /**
     * This is the instantiation of this class.
     * @param startPosition where the piece is coming from
     * @param endPosition where the class is going to
     * @param promotionPiece if it is a pawn, and it has progressed accross the board and reaches the other side, it can be promoted to a Queen, Rook, Knight, or Bishop. This variable is null unless it is a pawn in this situation.
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.PieceType = promotionPiece;
    }



    /**
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * @return ChessPosition of ending location
     */
    public ChessPosition getEndPosition() {
        return endPosition;
    }

    /**
     * Gets the type of piece to promote a pawn to if pawn promotion is part of this
     * chess move
     *
     * @return Type of piece to promote a pawn to, or null if no promotion
     */
    public ChessPiece.PieceType getPromotionPiece() {
        return PieceType;
    }

    /**
     * This is the override of the to string method so that when printing chess moves it is more readable.
     * @return it will return something that looks like this: {{1,2} to {8,2}} (this is a rook going from the bottom of the board to the top).
     */
    @Override
    public String toString() {
        return "{" + startPosition +
                " to " + endPosition +
                '}';
    }

    /**
     * This is the override method of equals. It makes it so that you can compare moves and decide that they are equivalent even if they are different objects.
     * @param o   the reference object with which to compare.
     * @return it will return true or false. True if each component of the move is the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;
        return Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition) && PieceType == chessMove.PieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, PieceType);
    }
}
