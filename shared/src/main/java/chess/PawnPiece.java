package chess;

import java.util.Collection;

public class PawnPiece extends ChessPiece {

    PawnPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor,PieceType.PAWN);
    }


    /**
     * This is the main utility function of the class. It directs what pawns can do. It determines what color the pawn is and directs it to the
     * appropriate code to get the job done
     * @param currentRow - int, the row of the pawn.
     * @param currentCol - int, the col of the pawn.
     * @param board - the current board setup.
     * @param ourList - list of possible moves we are keeping track of.
     * @param myPosition - the position of the pawn.
     * @param myColor - the color of the pawn
     */
    public void pawnMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        if (myColor == ChessGame.TeamColor.BLACK) {
            blackPawnMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        } else {
            whitePawnMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        }
    }

    /**
     * This is the helper function to get all of the possible white pawn moves.
     * @param currentRow - integer, row of the piece in question.
     * @param currentCol - integer, column of the piece in question.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    private void whitePawnMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        if (currentRow == 2) {  // If you haven't moved yet:
            boolean nooneInFront = checkIfSpotEmpty(board, currentRow+1, currentCol); // Sees if anyone is infront of the pawn.

            //If you haven't moved yet AND no one is in front of you, you can move at least forward by one.
            if (nooneInFront) {
                checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                boolean nooneInFront2 = checkIfSpotEmpty(board, currentRow+2, currentCol); // Sees if there is anyone 2 spaces in front of the pawn.

                //If there is no one 2 squares of you AS WELL then you can move 2 forward as your first move with this piece.
                if (nooneInFront2) {
                    checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+2,currentCol,myColor);
                }
            }
            // If this is NOT The pawns first move, then lets treat it differently.
        } else {
            boolean nooneInFront = checkIfSpotEmpty(board, currentRow+1, currentCol);

            //Check if anyone is directly in front of the pawn. if not, you can move there.
            if (nooneInFront) {
                checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
            }
        }

        // This allows you to attack diagonally forward and LEFT if there is a Black piece there.
        // This also allows for en Passant.
        if (myPosition.getColumn() != 1) {
            // Check left diagonal position for normal piece capturing.
            ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
            ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);

            // if the piece diagonally is BLACK then lets mark this as a possible move.
            if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol-1,myColor);
            }

            // Check piece directly to the left for en passant capturing.
            ChessPosition leftPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1);
            ChessPiece leftPiece = board.getPiece(leftPos);

            // if the piece directly to the left is BLACK AND is a PAWN then lets mark this as a possible en passant move.
            if (leftPiece != null && leftDiagonalPiece == null && leftPiece.getPieceType() == PieceType.PAWN && leftPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol-1,myColor);
            }

        }

        // This allows you to attack diagonally forward and RIGHT if there is a Black piece there.
        // This also allows for en Passant.
        if (myPosition.getColumn() != 8) {
            // Check left diagonal position for normal piece capturing.
            ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
            ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);

            // if the piece diagonally is BLACK then lets mark this as a possible move.
            if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+1,myColor);
            }

            // Check piece directly to the right for en passant capturing.
            ChessPosition rightPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1);
            ChessPiece rightPiece = board.getPiece(rightPos);

            // if the piece directly to the right is BLACK AND is a PAWN then lets mark this as a possible en passant move.
            if (rightPiece != null && currentRow == 5 && rightDiagonalPiece == null && rightPiece.getPieceType() == PieceType.PAWN && rightPiece.getTeamColor() == ChessGame.TeamColor.BLACK) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+1,myColor);
            }

        }


    }


    /**
     * This is the helper function to get all of the possible white pawn moves.
     * @param currentRow - integer, row of the piece in question.
     * @param currentCol - integer, column of the piece in question.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    private void blackPawnMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        //If the pawn hasn't moved yet,
        if (currentRow == 7) {
            boolean nooneInFront = checkIfSpotEmpty(board, currentRow-1, currentCol);

            //Check if there is anything right in front of it
            if (nooneInFront) {
                //If there is NOTHING in front of it, it can move there
                checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow-1,currentCol,myColor);
                boolean nooneInFront2 = checkIfSpotEmpty(board, currentRow-2, currentCol);

                //Additionally, if there is nothing 2 spaces in front of it, then you can move 2 squares forward on your first move.
                if (nooneInFront2) {
                    checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow-2,currentCol,myColor);
                }
            }

            // If the pawn already has moved at least once, then lets treat it differently:
        } else {
            boolean nooneInFront = checkIfSpotEmpty(board, currentRow-1, currentCol);

            //See if anything is directly ahead of it. If not, it can move there.
            if (nooneInFront) {
                checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow - 1, currentCol, myColor);
            }
        }

        // Now we will cover the diagonal attacks. Starting with attacking diagonally down and to the right.
        // This also allows for en Passant.
        if (myPosition.getColumn() != 1) {
            // Check left diagonal position for normal piece capturing.
            ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1);
            ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);

            //Check if there is an enemy in that diagonal square, if so, you can take it.
            if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.WHITE) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol-1,myColor);
            }

            // Check piece directly to the left for en passant capturing.
            ChessPosition leftPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-1);
            ChessPiece leftPiece = board.getPiece(leftPos);

            // if the piece directly to the left is WHITE AND is a PAWN then lets mark this as a possible en passant move.
            if (leftPiece != null && currentRow == 4 && leftDiagonalPiece == null && leftPiece.getPieceType() == PieceType.PAWN && leftPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol-1,myColor);
            }

        }

        //Now we will impliment the other half, diagonals going down and LEFT.
        // This also allows for en Passant.
        if (myPosition.getColumn() != 8) {
            // Check left diagonal position for normal piece capturing.
            ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
            ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);

            //Check if there is an enemy in that diagonal square, if so, you can take it.
            if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.WHITE) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+1,myColor);
            }

            // Check piece directly to the right for en passant capturing.
            ChessPosition rightPos = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+1);
            ChessPiece rightPiece = board.getPiece(rightPos);

            // if the piece directly to the right is WHITE AND is a PAWN then lets mark this as a possible en passant move.
            if (rightPiece != null && rightDiagonalPiece == null && rightPiece.getPieceType() == PieceType.PAWN && rightPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+1,myColor);
            }


        }

    }


}
