package chess;

import java.util.Collection;

public class QueenPiece extends ChessPiece{

    QueenPiece (ChessGame.TeamColor pieceColor) {
        super(pieceColor, PieceType.QUEEN);
    }


    public void queenMoves (int currentRow, int currentCol, ChessBoard board, Collection<ChessMove> ourList,
                            ChessPosition myPosition, ChessGame.TeamColor myColor) {
        RookPiece newRook = new RookPiece(myColor);
        BishopPiece newBishop = new BishopPiece(myColor);
        newRook.rookMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        newBishop.bishopMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
    }

}
