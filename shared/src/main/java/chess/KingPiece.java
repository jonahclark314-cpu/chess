package chess;

import java.util.Collection;

public class KingPiece extends ChessPiece{

    KingPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.KING);
    }

    /**
     * This is the helper function to get all of the possible king moves.
     * @param board - current board setup
     * @param ourList - the list to add to
     * @param myPosition - my current position
     * @param myColor - the color of the piece.
     */
    public void kingMoves (ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, ChessGame.TeamColor myColor) {
        //Let it move by one square in every direction
        for (int i=0; i<3; i++) {
            for (int j=0; j<3; j++) {
                int newRow = myPosition.getRow() + i - 1;
                int newCol = myPosition.getColumn() + j - 1;
                if (newRow <=8 && newRow >=1 && newCol <=8 && newCol >=1 && !(i == 1 && j == 1)) { //Make sure that it DID move and that it is still on the board.
                    checkIfPeiceThereAndGo(board,ourList,myPosition,newRow,newCol,myColor);
                }
            }
        }

        ChessPiece piece = board.getPiece(myPosition);
        // Here is where we implement the castling. First we check here if the king has moved yet.
        if (!this.getHasMoved()) {
            if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) { // If the King is Black.
                // We are going to define where the Black rooks SHOULD be if they haven't moved yet.
                ChessPosition rook1 = new ChessPosition(8,1);
                ChessPosition rook2 = new ChessPosition(8,8);

                // If the rook on the LEFT side of the board has not moved yet AND all the spaces between the King and the Rook are empty, you can allow Castling on that side
                if (board.getPiece(rook1) != null && board.getPiece(rook1).getPieceType() == PieceType.ROOK && !board.getPiece(rook1).getHasMoved() && board.getPiece(new ChessPosition(8,2)) == null && board.getPiece(new ChessPosition(8,3)) == null && board.getPiece(new ChessPosition(8,4)) == null) {
                    checkIfPeiceThereAndGo(board,ourList,myPosition,8,3,myColor);
                }

                // If the rook on the LEFT side of the board has not moved yet AND all the spaces between the King and the Rook are empty, you can allow Castling on that side
                if (board.getPiece(rook2) != null && board.getPiece(rook2).getPieceType() == PieceType.ROOK && !board.getPiece(rook2).getHasMoved() && board.getPiece(new ChessPosition(8,7)) == null && board.getPiece(new ChessPosition(8,6)) == null) {
                    checkIfPeiceThereAndGo(board,ourList,myPosition,8,7,myColor);
                }

            } else { // If the King is White.
                // We are going to define where the White rooks SHOULD be if they haven't moved yet.
                ChessPosition rook1 = new ChessPosition(1,1);
                ChessPosition rook2 = new ChessPosition(1,8);

                // If the rook on the LEFT side of the board has not moved yet AND all the spaces between the King and the Rook are empty, you can allow Castling on that side
                if (board.getPiece(rook1) != null && board.getPiece(rook1).getPieceType() == PieceType.ROOK && !board.getPiece(rook1).getHasMoved() && board.getPiece(new ChessPosition(1,2)) == null && board.getPiece(new ChessPosition(1,3)) == null && board.getPiece(new ChessPosition(1,4)) == null) {
                    checkIfPeiceThereAndGo(board,ourList,myPosition,1,3,myColor);
                }

                // If the rook on the LEFT side of the board has not moved yet AND all the spaces between the King and the Rook are empty, you can allow Castling on that side
                if (board.getPiece(rook2) != null && board.getPiece(rook2).getPieceType() == PieceType.ROOK && !board.getPiece(rook2).getHasMoved() && board.getPiece(new ChessPosition(1,7)) == null && board.getPiece(new ChessPosition(1,6)) == null) {
                    checkIfPeiceThereAndGo(board,ourList,myPosition,1,7,myColor);
                }

            }
        }

    }


}
