package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {

        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece piece = board.getPiece(myPosition);
        ChessGame.TeamColor myColor = piece.getTeamColor();
        Collection<ChessMove> ourList = new ArrayList<>();
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();


        if (piece.getPieceType() == PieceType.BISHOP) {
            boolean UpLeft = true;
            boolean UpRight = true;
            boolean DownLeft = true;
            boolean DownRight = true;

            for (int i = 1; i < 8 ;i++) {
                int newLeftRow = currentRow - i;
                int newRightRow = currentRow + i;
                int newUpCol = currentCol + i;
                int newDownCol = currentCol - i;

                if (newLeftRow >= 1 && newLeftRow <=8) {
                    if (newUpCol >= 1 && newUpCol <=8 && UpLeft) {
                        boolean canGo = true;
                        var possibleLoc = new ChessPosition(newLeftRow,newUpCol);
                        ChessPiece peiceThere = board.getPiece(possibleLoc);
                        if (peiceThere != null){
                            ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                            UpLeft = false;
                            if (myColor == colorThere) {
                                canGo = false;
                            }
                        }
                        if (canGo) {
                            var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                            ourList.add(possibleMove);
                        }
                    }
                    if (newDownCol >= 1 && newDownCol <=8 && DownLeft) {
                        boolean canGo = true;
                        var possibleLoc = new ChessPosition(newLeftRow,newDownCol);
                        ChessPiece peiceThere = board.getPiece(possibleLoc);
                        if (peiceThere != null){
                            ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                            DownLeft = false;
                            if (myColor == colorThere) {
                                canGo = false;
                            }
                        }
                        if (canGo) {
                            var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                            ourList.add(possibleMove);
                        }
                    }
                }

                if (newRightRow >= 1 && newRightRow <=8) {
                    if (newUpCol >= 1 && newUpCol <=8 && UpRight) {
                        boolean canGo = true;
                        var possibleLoc = new ChessPosition(newRightRow,newUpCol);
                        ChessPiece peiceThere = board.getPiece(possibleLoc);
                        if (peiceThere != null){
                            ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                            UpRight = false;
                            if (myColor == colorThere) {
                                canGo = false;
                            }
                        }
                        if (canGo) {
                            var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                            ourList.add(possibleMove);
                        }
                    }
                    if (newDownCol >= 1 && newDownCol <=8 && DownRight) {
                        boolean canGo = true;
                        var possibleLoc = new ChessPosition(newRightRow,newDownCol);
                        ChessPiece peiceThere = board.getPiece(possibleLoc);
                        if (peiceThere != null){
                            ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                            DownRight = false;
                            if (myColor == colorThere) {
                                canGo = false;
                            }
                        }
                        if (canGo) {
                            var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                            ourList.add(possibleMove);
                        }
                    }
                }
            }
        }

        return ourList;
    }




    @Override
    public String toString() {
        return "ChessPiece{" + type + '}';
    }

}
