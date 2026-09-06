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

    private boolean checkIfPeiceThereAndGo(ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, int newRow, int newCol, ChessGame.TeamColor myColor) {
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8){
            boolean noPeiceHere = true;
            boolean canGo = true;
            var possibleLoc = new ChessPosition(newRow,newCol);
            ChessPiece peiceThere = board.getPiece(possibleLoc);
            if (peiceThere != null){
                ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                noPeiceHere=false;
                if (myColor == colorThere) {
                    canGo = false;
                }
            }
            if (canGo) {
                var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                ourList.add(possibleMove);
            }

            return noPeiceHere;
        } else {
            return false;
        }


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
                        UpLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newUpCol,myColor);
                    }

                    if (newDownCol >= 1 && newDownCol <=8 && DownLeft) {
                        DownLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newDownCol,myColor);
                    }
                }

                if (newRightRow >= 1 && newRightRow <=8) {
                    if (newUpCol >= 1 && newUpCol <=8 && UpRight) {
                        UpRight = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,newUpCol,myColor);
                    }
                    if (newDownCol >= 1 && newDownCol <=8 && DownRight) {
                        DownRight = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,newDownCol,myColor);

                    }
                }
            }
        }

        if (piece.getPieceType() == PieceType.KING) {
            for (int i=0; i<3; i++) {
                for (int j=0; j<3; j++) {
                    int newRow = myPosition.getRow() + i - 1;
                    int newCol = myPosition.getColumn() + j - 1;
                    if (newRow <=8 && newRow >=1 && newCol <=8 && newCol >=1 && !(i == 1 && j == 1)) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,newRow,newCol,myColor);
                    }
                }
            }
        }

        if (piece.getPieceType() == PieceType.KNIGHT) {
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+2,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol-2,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+2,currentCol+1,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+2,currentCol-1,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+2,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol-2,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-2,currentCol+1,myColor);
            checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-2,currentCol-1,myColor);
        }

        if (piece.getPieceType() == PieceType.PAWN) {
            if (myColor == ChessGame.TeamColor.WHITE) {
                if (currentRow == 2) {
                    boolean nooneInFront;
                    nooneInFront = checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                    if (nooneInFront) {
                        checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+2,currentCol,myColor);
                    }
                } else {
                    checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                }

                if (myPosition.getColumn() != 1) {
                    ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
                    ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);
                    if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+1,myColor);
                    }
                }

                if (myPosition.getColumn() != 8) {
                    ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
                    ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);
                    if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+1,myColor);
                    }

                }

            } else { // team color is BLACK
                if (currentRow == 2) {
                    boolean nooneInFront;
                    nooneInFront = checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                    if (nooneInFront) {
                        checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+2,currentCol,myColor);
                    }
                } else {
                    checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                }

                if (myPosition.getColumn() != 1) {
                    ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
                    ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);
                    if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+1,myColor);
                    }
                }

                if (myPosition.getColumn() != 8) {
                    ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
                    ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);
                    if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+1,myColor);
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
