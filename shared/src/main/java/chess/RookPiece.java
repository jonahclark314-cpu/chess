package chess;

import java.util.Collection;

public class RookPiece extends ChessPiece{

    RookPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor,PieceType.ROOK);
    }

    /**
     * This is the helper function to get all of the possible rook moves.
     * @param currentRow - integer, row of the piece in question.
     * @param currentCol - integer, column of the piece in question.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    public void rookMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition,
                           ChessGame.TeamColor myColor) {
        // Set up these variables to track when it runs into a piece and cannot go past it. These become false when it runs into a piece.
        boolean up = true;
        boolean right = true;
        boolean left = true;
        boolean down = true;

        // count 1-7 and let's move the rook in each direction that many spaces until it runs into something.
        for (int i = 1; i < 8 ;i++) {
            int newLeftRow = currentRow - i;
            int newRightRow = currentRow + i;
            int newUpCol = currentCol + i;
            int newDownCol = currentCol - i;

            //In each of these if statements we are checking if the new location is on the board. If so, we will try to move there.
            if (newLeftRow >= 1 && newLeftRow <=8 && left) {
                left = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,currentCol,myColor);
            }
            if (newRightRow >= 1 && newRightRow <=8 && right) {
                right = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,currentCol,myColor);
            }
            if (newUpCol >= 1 && newUpCol <=8 && up) {
                up = checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow,newUpCol,myColor);
            }
            if (newDownCol >= 1 && newDownCol <=8 && down) {
                down = checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow,newDownCol,myColor);
            }
        }

    }
}
