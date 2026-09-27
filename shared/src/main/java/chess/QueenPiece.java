package chess;

import java.util.Collection;

public class QueenPiece extends ChessPiece{

    QueenPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.QUEEN);
    }


    public void queenMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        rookMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        bishopMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
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
    private void rookMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
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

    /**
     * This is the helper function to get all of the possible bishop moves.
     * @param currentRow - integer, row of the piece in question.
     * @param currentCol - integer, column of the piece in question.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    private void bishopMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        // These boolean variables help us track if we have run into a piece in this direction yet.
        boolean upLeft = true;
        boolean upRight = true;
        boolean downLeft = true;
        boolean downRight = true;

        //Loop 8 times, try to move the piece that many squares diagonally in each direction.
        for (int i = 1; i < 8 ;i++) {
            // Calculate the new row and column positions in each direction.
            int newLeftRow = currentRow - i;
            int newRightRow = currentRow + i;
            int newUpCol = currentCol + i;
            int newDownCol = currentCol - i;

            // If you are still on the board, AND you haven't run into another piece yet, Add the new position to the list using the method checkIfPeiceThereAndGo.
            if (newLeftRow >= 1 && newLeftRow <=8) {
                if (newUpCol >= 1 && newUpCol <=8 && upLeft) {
                    upLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newUpCol,myColor);
                }

                if (newDownCol >= 1 && newDownCol <=8 && downLeft) {
                    downLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newDownCol,myColor);
                }
            }

            // If you are still on the board, AND you haven't run into another piece yet, Add the new position to the list using the method checkIfPeiceThereAndGo.
            if (newRightRow >= 1 && newRightRow <=8) {
                if (newUpCol >= 1 && newUpCol <=8 && upRight) {
                    upRight = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,newUpCol,myColor);
                }
                if (newDownCol >= 1 && newDownCol <=8 && downRight) {
                    downRight = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,newDownCol,myColor);

                }
            }
        }
    }

}
