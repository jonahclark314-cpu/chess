package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    // Here are the variables that ChessGame will keep track of.
    ChessBoard board;
    TeamColor currentTurn;
    private ChessMove lastMove;

    //Here is the initializer for the class. It automatically puts White as going first, and it sets up the board.
    public ChessGame() {
        this.currentTurn = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
        this.lastMove = null;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * @return returns the last move that was done in the game.
     */
    public ChessMove getLastMove() {
        return this.lastMove;
    }

    /**
     * Stores the most recent move in the class for future reference.
     * @param move - this is the move that will be stored.
     */
    public void setLastMove(ChessMove move) {
        this.lastMove = move;
    }


    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        System.out.println(this.board);
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        // Start be retrieving all the possible moves from the piece at the start position. This is done within the ChessPiece class.
        Collection<ChessMove> possibleMoves = this.board.getPiece(startPosition).pieceMoves(this.board,startPosition);

        //Set up a new array. We will check which moves are valid, and add the valid moves to this array
        Collection<ChessMove> actualPossibleMoves = new ArrayList<>();

        // Loop through all of the possible moves
        for (ChessMove move : possibleMoves) {
            // If the move is a Castle move, treat is specially. We need to check if the king is in check in any of the positons along the way.
            if (move.isACastle(this.board)) {
                if (!isInCheck(this.board.getPiece(startPosition).getTeamColor()) && notInCheckAfterMove(move, this.board.getPiece(startPosition).getTeamColor())) {

                    ChessPosition newEndPosition = new ChessPosition(startPosition.getRow(),(startPosition.getColumn() + move.getEndPosition().getColumn())/2);
                    ChessMove middleMove = new ChessMove (startPosition, newEndPosition, null);
                    if (notInCheckAfterMove(middleMove,this.board.getPiece(startPosition).getTeamColor())) {
                        actualPossibleMoves.add(move);
                    }

                }
            }
            // If the move IS en passant, check if the team will be in check after the move.
            else if (move.isAEnPassant(this.board, getLastMove())) {
                if (notInCheckAfterMove(move, this.board.getPiece(startPosition).getTeamColor())) {
                    actualPossibleMoves.add(move);
                }
            }
            // If the move LOOKS like it is trying to be en passant but breaks some rule, do nothign.
            else if (move.looksLikeShouldBeEnPassant(this.board)) {
                continue;
            }
            // If it is any other move, simply check if the move results in a check, if not, add it to the list.
            else if (notInCheckAfterMove(move, this.board.getPiece(startPosition).getTeamColor())) {
                actualPossibleMoves.add(move);
            }
        }
        return actualPossibleMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        // If there is a piece at the start position, lets check if it is in the valid moves Collection.
        boolean moveIsValid = false;
        if (this.board.getPiece(move.getStartPosition()) != null) {
            Collection<ChessMove> moves = validMoves(move.getStartPosition());

            // This just checks if the given move is somewhere in the valid moves Collection.
            for (ChessMove possibleMove : moves) {
                if (possibleMove.equals(move)) {
                    moveIsValid = true;
                    break;
                }
            }

        } else { // This is if there is NO PIECE at the start location.
            System.out.println("THROW INVALID MOVE EXCEPTION");
            throw new InvalidMoveException("This move was not valid!");
        }

        // If the move is out of turn, don't allow move.
        if (this.board.getPiece(move.getStartPosition()).getTeamColor() != getTeamTurn()){
            moveIsValid = false;
        }

        // If the move is not valid, throw an exception.
        if (!moveIsValid) {
            System.out.println("THROW INVALID MOVE EXCEPTION");
            throw new InvalidMoveException("This move was not valid!");
        }
        // If the move IS valid
        else {
            ChessPiece piece = this.board.getPiece(move.getStartPosition());
            ChessPiece originalPiece = this.board.getPiece(move.getStartPosition());

            // If the move is a Castle, we need to move BOTH the rook AND the king.
            if (move.isACastle(this.board)) {
                // If is the LEFT side castling.
                if (move.getEndPosition().getColumn()==3) {
                    ChessPosition rook = new ChessPosition(move.getStartPosition().getRow(),1);
                    ChessPosition rookNewPosition = new ChessPosition(move.getStartPosition().getRow(),4);
                    ChessPiece rookPiece = this.board.getPiece(rook);
                    this.board.removePiece(move.getStartPosition());
                    this.board.removePiece(move.getEndPosition());
                    this.board.addPiece(move.getEndPosition(), piece);
                    this.board.removePiece(rook);
                    this.board.addPiece(rookNewPosition,rookPiece);
                }

                // If it is the RIGHT side castling.
                if (move.getEndPosition().getColumn() == 7) {
                    ChessPosition rook = new ChessPosition(move.getStartPosition().getRow(),8);
                    ChessPosition rookNewPosition = new ChessPosition(move.getStartPosition().getRow(),6);
                    ChessPiece rookPiece = this.board.getPiece(rook);
                    this.board.removePiece(move.getStartPosition());
                    this.board.removePiece(move.getEndPosition());
                    this.board.addPiece(move.getEndPosition(), piece);
                    this.board.removePiece(rook);
                    this.board.addPiece(rookNewPosition,rookPiece);
                }

                // Set next turn
                if (piece.getTeamColor() == TeamColor.BLACK) {
                    setTeamTurn(TeamColor.WHITE);
                } else {
                    setTeamTurn(TeamColor.BLACK);
                }

                // Mark the pieces as having been moved and put into memory what this last move was.
                piece.setHasMoved();
                setLastMove(move);
            }
            // If it is en passant, move the pawn and take the other pawn.
            else if (move.isAEnPassant(this.board,getLastMove())){
                ChessPosition enPassantPawn = lastMove.getEndPosition();
                this.board.removePiece(move.getStartPosition());
                this.board.addPiece(move.getEndPosition(), piece);
                this.board.removePiece(enPassantPawn);

                // Set next turn
                if (piece.getTeamColor() == TeamColor.BLACK) {
                    setTeamTurn(TeamColor.WHITE);
                } else {
                    setTeamTurn(TeamColor.BLACK);
                }

                // Mark the pieces as having been moved and put into memory what this last move was.
                piece.setHasMoved();
                setLastMove(move);
            }

            // If it is any other normal move.
            else {

                // Change the piece if it is a promoted pawn
                if (move.getPromotionPiece() != null) {
                    piece = new ChessPiece(originalPiece.getTeamColor(),move.getPromotionPiece());
                }

                // Save the original piece so we can revert the board if necessary.
                ChessPiece oldPiece = this.board.getPiece(move.getEndPosition());

                // move the piece.
                this.board.removePiece(move.getStartPosition());
                this.board.removePiece(move.getEndPosition());
                this.board.addPiece(move.getEndPosition(), piece);
                boolean resultsInCheck = isInCheck(piece.getTeamColor());

                // if the move results in check, undo the move
                if (resultsInCheck) {
                    System.out.println("THROW INVALID MOVE EXCEPTION");
                    this.board.removePiece(move.getStartPosition());
                    this.board.removePiece(move.getEndPosition());
                    this.board.addPiece(move.getStartPosition(), originalPiece);
                    this.board.addPiece(move.getEndPosition(), oldPiece);
                    throw new InvalidMoveException("This move was not valid!");
                }

                // If the move does not result in check, it is valid, so keep move.
                else {
                    // set the next team as the next turn.
                    if (piece.getTeamColor() == TeamColor.BLACK) {
                        setTeamTurn(TeamColor.WHITE);
                    } else {
                        setTeamTurn(TeamColor.BLACK);
                    }

                    // save the move as the last move, and mark the moved piece as having moved.
                    piece.setHasMoved();
                    setLastMove(move);
                }
            }

        }

    }


    /**
     * This determines the location of the king of the specified color
     * This is a helper function to check if the team is in check.
     *
     * @param teamColor This is the color of the team
     * @return it will return the positon of the king.
     */
    public ChessPosition findOurKing (TeamColor teamColor) {

        // loop through every location in the board.
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentCheck = new ChessPosition(i,j);

                // If it is the correct king color, then this is the correct location.
                if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == teamColor && board.getPiece(currentCheck).getPieceType() == ChessPiece.PieceType.KING) {
                    return currentCheck;
                }
            }
        }

        // This should never execute. The king of both colors should ALWAYS be present.
        return new ChessPosition(1,1);
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        // Start with looking if the Black king is in check.
        if (teamColor == TeamColor.BLACK) {
            ChessPosition kingLocation = findOurKing(teamColor);

            // Loop through all of the pieces on the board. If it is white, AND if it can move to take the king, then it is in check
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i,j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == TeamColor.WHITE){
                        Collection<ChessMove> ourList = this.board.getPiece(currentCheck).pieceMoves(this.board,currentCheck);
                        for (ChessMove move : ourList) {
                            ChessPosition endPosition = move.getEndPosition();
                            if (kingLocation.equals(endPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }

            // If none of the white pieces can attack the king, it is not in check.
            return false;

        }

        // Now we will check if the White king is in check.
        else {
            ChessPosition kingLocation = findOurKing(teamColor);

            // Loop through all of the pieces on the board. If it is black, AND if it can move to take the king, then it is in check
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i,j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == TeamColor.BLACK){
                        Collection<ChessMove> ourList = this.board.getPiece(currentCheck).pieceMoves(this.board,currentCheck);
                        for (ChessMove move : ourList) {
                            ChessPosition endPosition = move.getEndPosition();
                            if (kingLocation.equals(endPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }

            // If none of the white pieces can attack the king, it is not in check.
            return false;
        }
    }

    /**
     * This moves the piece, checks if it is in check after the move, and replaces the piece where it was.
     * This simply helps you check if a move is valid.
     * @param move - the move in question. this is the move that will be executed and undone.
     * @param teamColor - current team that is moving.
     * @return will return true if the move does not result in check, false otherwise.
     */
    public boolean notInCheckAfterMove(ChessMove move,TeamColor teamColor) {

        // Save the piece how it was, move it.
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        ChessPiece oldPiece = this.board.getPiece(move.getEndPosition());
        this.board.removePiece(move.getStartPosition());
        this.board.removePiece(move.getEndPosition());
        this.board.addPiece(move.getEndPosition(),piece);

        // Check if that team is in check.
        boolean isInCheckStill = isInCheck(teamColor);

        // Move the piece back to where it was.
        this.board.removePiece(move.getStartPosition());
        this.board.removePiece(move.getEndPosition());
        this.board.addPiece(move.getStartPosition(),piece);
        this.board.addPiece(move.getEndPosition(),oldPiece);

        // Return the answer we discovered.
        return !isInCheckStill;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {

        // Check if the team is in check.
        if (isInCheck(teamColor)) {

            // Loop through every piece that the team has.
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i, j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == teamColor) {

                        // Loop through all of the possible moves for that piece.
                        Collection<ChessMove> ourList = this.board.getPiece(currentCheck).pieceMoves(this.board,currentCheck);
                        for (ChessMove move : ourList) {

                            // If that results in escaping check, then it is NOT Checkmate.
                            if (notInCheckAfterMove(move, teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }

            // If there are no moves that the team can make, it IS checkmate.
            return true;
        }

        // If the team isn't even in check, it is not checkmate.
        else {
            return false;
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // To be in stalemate, the team cannot be in check currently.
        if (!isInCheck(teamColor)) {

            // Loop through all of the pieces of the team.
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i, j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == teamColor) {

                        // Loop through all of the moves of the given piece.
                        Collection<ChessMove> ourList = this.board.getPiece(currentCheck).pieceMoves(this.board,currentCheck);
                        for (ChessMove move : ourList) {

                            // If it can move literally anywhere, it is NOT stalemate.
                            if (notInCheckAfterMove(move, teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
            // If no pieces can move anywhere, it is stalemate.
            return true;
        }

        // If they are in check, it is not stalemate.
        else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.board;
    }


    /**
     * Overrides the equals method.
     * @param o   the reference object with which to compare.
     * @return return true if the two objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTurn == chessGame.currentTurn;
    }

    /**
     * Overrides the hashCode method
     *
     * @return return the integer representing the hashCode.
     */
    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }
}
