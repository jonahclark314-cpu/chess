package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    // Here is where I set up variables that will be used in this class. The Piece Type (Rook, Queen, etc.),
    // color (black or white), and if the piece has moved that game yet or not.
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;
    private boolean hasMoved;

    // This is the ChessPiece constructor.
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
        this.hasMoved = false;
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
     * This is the helper function for pieceMoves. What this does is checks if there is a piece where you are trying to go, if there IS, then
     * check if it is black or white. If it is your same color you cannot go there, if it is different color than you, you
     * can go there (by capturing the piece)
     * It will create the move and add it to the growing list that will be returned in pieceMoves
     * @param board this is the current state of the board. it allows us to know if there is a piece where you want to go
     * @param ourList this is the list that pieceMoves is creating. This function will add to it
     * @param myPosition Your current position. It is required to input for creating chess moves.
     * @param newRow The row you want to go to.
     * @param newCol The column you want to go to.
     * @param myColor the color of your piece. This helps compare against the color of the piece that is in the space you might go.
     * @return returns TRUE if there are no pieces in that place. returns FALSE if there is a piece there (black or white).
     * This helps pieceMmoves track if a rook/bishop/queen can continue going past a piece.
     */
    public boolean checkIfPeiceThereAndGo(ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, int newRow,
                                          int newCol, ChessGame.TeamColor myColor) {
        // Start by making sure where you want to go is a valid place to go.
        if (newRow >= 1 && newRow <= 8 && newCol >= 1 && newCol <= 8){

            //If it is a valid place to go, We will set up some starting variables.
            boolean noPeiceHere = true; //becomes false if there is a piece in the ChessPosition: {newRow, newCol}.
            boolean canGo = true; //this becomes false if your color is equal to the color of the piece in the new location.
            var possibleLoc = new ChessPosition(newRow,newCol); // Create the ChessPosition object that represents the new location.
            ChessPiece peiceThere = board.getPiece(possibleLoc);

            if (peiceThere != null){ //If there is a piece in the new location
                ChessGame.TeamColor colorThere = peiceThere.getTeamColor();
                noPeiceHere=false;

                if (myColor == colorThere) {//Check if you are the same color as them
                    canGo = false;
                }
            }

            if (canGo) {
                //If you are a pawn that is promoting to the end of the board,
                // make sure that you say you can become a Bishop, Queen, Rook, or Knight.
                if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN && ((myColor == ChessGame.TeamColor.WHITE &&
                        myPosition.getRow() == 7 && newRow == 8) || (myColor == ChessGame.TeamColor.BLACK &&
                        myPosition.getRow() == 2 && newRow == 1))){
                    var possibleMove = new ChessMove(myPosition,possibleLoc,PieceType.BISHOP);
                    ourList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition,possibleLoc,PieceType.QUEEN);
                    ourList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition,possibleLoc,PieceType.ROOK);
                    ourList.add(possibleMove);
                    possibleMove = new ChessMove(myPosition,possibleLoc,PieceType.KNIGHT);
                    ourList.add(possibleMove);

                // If you are NOT A pawn that is being promoted, then lets just make a new ChessMove and add it to our list.
                } else {
                    var possibleMove = new ChessMove(myPosition,possibleLoc,null);
                    ourList.add(possibleMove);
                }
            }

            return noPeiceHere; //See what this function returns in the notes above.

        } else { // this is if the new location is off the board.
            return false;
        }
    }

    /**
     * This function just makes it easier to check if the location in question has another chess piece there.
     * @param board current state of the board. Pass in a ChessBoard object.
     * @param row The row you are inquiring about.
     * @param col the Column you are inquiring about.
     * @return true if there IS a piece in the space. false if there ISN'T a piece there.
     */
    public boolean checkIfSpotEmpty (ChessBoard board, int row, int col) {
        ChessPosition toCheck = new ChessPosition(row,col);
        return board.getPiece(toCheck) == null;
    }

    /**
     * This function is called when a piece is moved. It just helps make sure that you track when a
     * piece has first moved (for castling and en Passant).
     */
    public void setHasMoved() {
        this.hasMoved = true;
    }

    /**
     * This function just returns the private variable hasMoved.
     * @return Simply returns true if the piece has already moved, and false if it has not yet.
     */
    public boolean getHasMoved() {
        return this.hasMoved;
    }




    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @param board The ChessBoard object that shows where current pieces are.
     * @param myPosition Where the piece is that you are wondering about.
     * @return Collection of valid moves of that piece.
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        //Set up Variables that will be used no matter what type of piece you are working with.
        ChessPiece piece = board.getPiece(myPosition); //retrieve the piece object at myPosition
        ChessGame.TeamColor myColor = piece.getTeamColor(); //Find out what color we are.
        Collection<ChessMove> ourList = new ArrayList<>(); //Set up list we will return.
        int currentRow = myPosition.getRow();
        int currentCol = myPosition.getColumn();

        //If the piece is a Bishop or a Queen, this allows it to move diagonally until it hits something.
        if (piece.getPieceType() == PieceType.BISHOP) {
            BishopPiece newBishop = new BishopPiece(myColor);
            newBishop.bishopMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        }

        //Lets Kings move in any direction by 1 square.
        if (piece.getPieceType() == PieceType.KING) {
            KingPiece newKing = new KingPiece(myColor);
            if (this.getHasMoved()){
                newKing.setHasMoved();
            }
            newKing.kingMoves(board,ourList,myPosition,myColor);
        }

        //Lets knights move in the L shape.
        if (piece.getPieceType() == PieceType.KNIGHT) {
            KnightPiece newKnight = new KnightPiece(myColor);
            newKnight.knightMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        }

        //This part gives all functionality to pawns.
        if (piece.getPieceType() == PieceType.PAWN) {
            PawnPiece newPawn = new PawnPiece(myColor);
            //Let's define the move rules if the pawn is white first. The pawn will only move UP (row getting bigger)
            newPawn.pawnMoves(currentRow,currentCol,board,ourList,myPosition,myColor);

        }

        //If the piece is a Rook or a Queen, this allows it to move straight until it hits something.
        if (piece.getPieceType() == PieceType.ROOK) {
            RookPiece newRook = new RookPiece(myColor);
            if (this.getHasMoved()){
                newRook.setHasMoved();
            }
            newRook.rookMoves(currentRow,currentCol,board,ourList,myPosition,myColor);
        }

        if (piece.getPieceType() == PieceType.QUEEN) {
            QueenPiece newQueen = new QueenPiece(myColor);
            newQueen.queenMoves(currentRow,currentCol,board,ourList,myPosition,myColor);

        }

        // We will return Collection of valid moves of that piece.
        return ourList;
    }


    /**
     * This is where we make an override toString method that makes the ChessPiece output more readable.
     * @return ChessPiece{ROOK} or whatever it really is.
     */
    @Override
    public String toString() {
        if (getTeamColor()== ChessGame.TeamColor.BLACK) {
            if (getPieceType() == PieceType.PAWN) {
                return "p";
            } else if (getPieceType() == PieceType.ROOK){
                return "r";
            }else if (getPieceType() == PieceType.KNIGHT) {
                return "n";
            }else if (getPieceType()==PieceType.BISHOP){
                return"b";
            }else if (getPieceType()==PieceType.QUEEN) {
                return "q";
            } else if (getPieceType() == PieceType.KING){
                return "k";
            } else {
                return ".";
            }
        }
        if (getTeamColor()== ChessGame.TeamColor.WHITE) {
            if (getPieceType() == PieceType.PAWN) {
                return "P";
            } else if (getPieceType() == PieceType.ROOK){
                return "R";
            }else if (getPieceType() == PieceType.KNIGHT) {
                return "N";
            }else if (getPieceType()==PieceType.BISHOP){
                return"B";
            }else if (getPieceType()==PieceType.QUEEN) {
                return "Q";
            } else if (getPieceType() == PieceType.KING){
                return "K";
            } else {
                return ".";
            }
        }
        return ".";
    }

    /**
     * This is where I make the Override equals function so that we can compare two pieces and make sure they are equivalent.
     * @param o   the reference object with which to compare.
     * @return true if they are equivalent pieces, false if not.
     */
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }

}

