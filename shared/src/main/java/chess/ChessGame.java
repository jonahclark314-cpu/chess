package chess;

import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    ChessBoard board;

    TeamColor currentTurn;

    public ChessGame() {
        currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }



    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
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
        return this.board.getPiece(startPosition).pieceMoves(this.board,startPosition);
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }


    /**
     * This determines the location of the king of the specified color
     *
     * @param teamColor This is the color of the team
     * @return it will return the positon of the king.
     */
    public ChessPosition findOurKing (TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition currentCheck = new ChessPosition(i,j);
                if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == teamColor && board.getPiece(currentCheck).getPieceType() == ChessPiece.PieceType.KING) {
                    return currentCheck;
                }
            }
        }
        return new ChessPosition(1,1);
    }


    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {

        if (teamColor == TeamColor.BLACK) {
            ChessPosition kingLocation = findOurKing(teamColor);

            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i,j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == TeamColor.WHITE){
                        Collection<ChessMove> ourList = validMoves(currentCheck);
                        for (ChessMove move : ourList) {
                            ChessPosition endPosition = move.getEndPosition();
                            if (kingLocation.equals(endPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;

        }
        else {
            ChessPosition kingLocation = findOurKing(teamColor);

            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i,j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == TeamColor.BLACK){
                        Collection<ChessMove> ourList = validMoves(currentCheck);
                        for (ChessMove move : ourList) {
                            ChessPosition endPosition = move.getEndPosition();
                            if (kingLocation.equals(endPosition)) {
                                return true;
                            }
                        }
                    }
                }
            }
            return false;
        }

    }

    public boolean inCheckAfterMove(ChessMove move,TeamColor teamColor) {
        ChessPiece piece = this.board.getPiece(move.getStartPosition());
        ChessPiece oldPiece = this.board.getPiece(move.getEndPosition());
        this.board.removePiece(move.getStartPosition());
        this.board.removePiece(move.getEndPosition());
        this.board.addPiece(move.getEndPosition(),piece);
        boolean isInCheckStill = isInCheck(teamColor);


        this.board.removePiece(move.getStartPosition());
        this.board.removePiece(move.getEndPosition());
        this.board.addPiece(move.getStartPosition(),piece);
        this.board.addPiece(move.getEndPosition(),oldPiece);

        return isInCheckStill;
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i = 1; i <= 8; i++) {
                for (int j = 1; j <= 8; j++) {
                    ChessPosition currentCheck = new ChessPosition(i, j);
                    if (board.getPiece(currentCheck) != null && board.getPiece(currentCheck).getTeamColor() == teamColor) {
                        Collection<ChessMove> ourList = validMoves(currentCheck);
                        for (ChessMove move : ourList) {
                            if (!inCheckAfterMove(move, teamColor)) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
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
        throw new RuntimeException("Not implemented");
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
}
