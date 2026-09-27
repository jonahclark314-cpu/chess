package chess;

import java.util.Collection;
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
    private final ChessPiece.PieceType pieceType;

    /**
     * This is the instantiation of this class.
     * @param startPosition where the piece is coming from
     * @param endPosition where the class is going to
     * @param promotionPiece if it is a pawn, and it has progressed accross the board and reaches the other side,
     *                       it can be promoted to a Queen, Rook, Knight, or Bishop. This variable is null unless
     *                       it is a pawn in this situation.
     */
    public ChessMove(ChessPosition startPosition, ChessPosition endPosition,
                     ChessPiece.PieceType promotionPiece) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.pieceType = promotionPiece;
    }

    /**
     * This checks if the proposed move is a castle move. It does so by seeing if the King moves 2 spaces.
     * @param board the current state of the board
     * @return returns true if the move is a castling move
     */
    public boolean isACastle (ChessBoard board) {
        // Make sure the piece moving is a king.
        if (board.getPiece(this.startPosition) != null &&
                board.getPiece(this.startPosition).getPieceType() == ChessPiece.PieceType.KING) {
            int oldColumn = startPosition.getColumn();
            int newColumn = endPosition.getColumn();

            // Check it moves 2 squares to the left or to the right.
            return ((oldColumn - newColumn) == 2) || ((oldColumn - newColumn) == -2);
        } else { // if the piece is NOT a king.
            return false;
        }
    }

    /**
     * This is a helper function to see if the move LOOKS like it should be en Passant. Does not take into account
     * turns.
     * @param board - current board set up.
     * @return returns true if the move looks like it should be en Passant. False otherwise.
     */
    public boolean looksLikeShouldBeEnPassant (ChessBoard board) {
        // Make sure it moves 1 square diagonally
        int horizontalMovement = Math.abs(this.getStartPosition().getColumn() - this.getEndPosition().getColumn());
        int verticalMovement = Math.abs(this.getStartPosition().getRow() - this.getEndPosition().getRow());

        // See if the current piece is a pawn, make sure there is no piece where it is going.
        ChessPiece currentPiece = board.getPiece(this.getStartPosition());
        ChessPiece pieceAtNewLocation = board.getPiece(this.getEndPosition());

        // Make sure that the pawn being taken by the en passant move is actually a pawn.
        ChessPosition enPassantPawnPosition = new ChessPosition(this.getStartPosition().getRow(),this.getEndPosition().getColumn());
        ChessPiece enPassantPawn = board.getPiece(enPassantPawnPosition);

        // All of the logic of the comments above is contained in this return statement.

        if (pieceAtNewLocation == null && currentPiece.getPieceType() == ChessPiece.PieceType.PAWN) {
            if (enPassantPawn != null && enPassantPawn.getPieceType() == ChessPiece.PieceType.PAWN && horizontalMovement == 1) {
                return verticalMovement == 1;
            }
        }
        return false;
    }

    /**
     * This tests if the move is ACTUALLY an en passant move. It takes everything into account including what the previous move was.
     * Compare this to looksLikeShouldBeEnPassant().
     * @param board - current board setup.
     * @param lastMove - the most recent move that was done in the game.
     * @return - returns true if it follows all the en passant rules. returns false otherwise.
     */
    public boolean isAEnPassant (ChessBoard board, ChessMove lastMove) {

        // If it is the first move of the game, it is NOT en passant.
        if (lastMove == null) {
            return false;
        }

        // Set up the logic to see if the move resembles an en passant.
        int newPawnColumnNumber = this.endPosition.getColumn();
        int oldPawnColumnNumber = this.startPosition.getColumn();

        // Use the last move end location to locate the pawn that potentially can be taken via en passant.
        int lastMoveDistance = lastMove.getStartPosition().getRow() - lastMove.getEndPosition().getRow();
        int lastMoveColumnNumber = lastMove.getStartPosition().getColumn();
        ChessPiece enPassantPawn = board.getPiece(lastMove.getEndPosition());

        // The return implements all of the logic stated above.
        if (board.getPiece(this.startPosition).getPieceType() == ChessPiece.PieceType.PAWN) {
            if (board.getPiece(this.endPosition) == null && enPassantPawn.getPieceType() == ChessPiece.PieceType.PAWN) {
                return Math.abs(lastMoveDistance) == 2 && newPawnColumnNumber == lastMoveColumnNumber &&
                        oldPawnColumnNumber != newPawnColumnNumber;
            }
        }
        return false;
    }

    /**
     * This is what returns the start position of the move.
     * @return ChessPosition of starting location
     */
    public ChessPosition getStartPosition() {
        return startPosition;
    }

    /**
     * This is what returns the end position of the move.
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
        return pieceType;
    }


    public boolean isMoveSilly  (ChessBoard board, ChessGame.TeamColor teamTurn){

        // If there is a piece at the start position, lets check if it is in the valid moves Collection.
        boolean moveIsValid = false;
        if (board.getPiece(this.getStartPosition()) != null) {
            Collection<ChessMove> moves = board.getPiece(this.getStartPosition()).pieceMoves(board,this.getStartPosition());

            // This just checks if the given move is somewhere in the valid moves Collection.
            for (ChessMove possibleMove : moves) {
                if (this.equals(possibleMove)) {
                    moveIsValid = true;
                    break;
                }
            }

        } else { // This is if there is NO PIECE at the start location.
            return true;
        }

        // If the move is out of turn, don't allow move.
        if (board.getPiece(this.getStartPosition()).getTeamColor() != teamTurn){
            moveIsValid = false;
        }

        // If the move is not valid, throw an exception.
        return !moveIsValid;
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
     * This is the override method of equals. It makes it so that you can compare moves
     * and decide that they are equivalent even if they are different objects.
     * @param o   the reference object with which to compare.
     * @return it will return true or false. True if each component of the move is the same, false otherwise.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessMove chessMove = (ChessMove) o;

        if (Objects.equals(startPosition, chessMove.startPosition) && Objects.equals(endPosition, chessMove.endPosition)) {
            return pieceType == chessMove.pieceType;
        }
        return false;
    }

    /**
     * This is a override of the hashCode method. It helps with the equals method above.
     * @return the hash code. It is an int.
     */
    @Override
    public int hashCode() {
        return Objects.hash(startPosition, endPosition, pieceType);
    }
}
