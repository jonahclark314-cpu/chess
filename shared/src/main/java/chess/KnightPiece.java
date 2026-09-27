package chess;

import java.util.Collection;

public class KnightPiece extends ChessPiece{

    KnightPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KNIGHT);
    }

    /**
     * This is the helper function to get all of the possible knight moves.
     * @param currentRow - integer, row of the piece in question.
     * @param currentCol - integer, column of the piece in question.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    public void knightMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList,
                             ChessPosition myPosition, ChessGame.TeamColor myColor) {
        //Check all 8 possible move directions one at a time.
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+2,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol-2,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+2,currentCol+1,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+2,currentCol-1,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+2,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol-2,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-2,currentCol+1,myColor);
        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-2,currentCol-1,myColor);

    }

}
