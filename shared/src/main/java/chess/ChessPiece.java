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

    // Here is where I set up variables that will be used in this class. The Piece Type (Rook, Queen, etc.) and color (black or white).
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;

    /**
     * This is the instantiation of this class. Must provide the following parameters.
     * @param pieceColor either black or white
     * @param type Pawn, Rook, Knight, Bishop, Queen, or King.
     */
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
     * This is the helper function for pieceMoves. What this does is checks if there is a piece where you are trying to go, if there IS, then
     * check if it is black or white. If it is your same color you cannot go there, if it is different color than you, you can go there (by capturing the piece)
     * It will create the move and add it to the growing list that will be returned in pieceMoves
     * @param board this is the current state of the board. it allows us to know if there is a piece where you want to go
     * @param ourList this is the list that pieceMoves is creating. This function will add to it
     * @param myPosition Your current position. It is required to input for creating chess moves.
     * @param newRow The row you want to go to.
     * @param newCol The column you want to go to.
     * @param myColor the color of your piece. This helps compare against the color of the piece that is in the space you might go.
     * @return returns TRUE if there are no pieces in that place. returns FALSE if there is a piece there (black or white). This helps pieceMmoves track if a rook/bishop/queen can continue going past a piece.
     */
    private boolean checkIfPeiceThereAndGo(ChessBoard board, Collection<ChessMove> ourList, ChessPosition myPosition, int newRow, int newCol, ChessGame.TeamColor myColor) {
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
                //If you are a pawn that is promoting to the end of the board, make sure that you say you can become a Bishop, Queen, Rook, or Knight.
                if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN && ((myColor == ChessGame.TeamColor.WHITE && myPosition.getRow() == 7 && newRow == 8) || (myColor == ChessGame.TeamColor.BLACK && myPosition.getRow() == 2 && newRow == 1))){
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
    private boolean checkIfSpotEmpty (ChessBoard board, int row, int col) {
        ChessPosition toCheck = new ChessPosition(row,col);
        return board.getPiece(toCheck) == null;
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
        if (piece.getPieceType() == PieceType.BISHOP || piece.getPieceType() == PieceType.QUEEN) {
            // These boolean variables help us track if we have run into a piece in this direction yet.
            boolean UpLeft = true;
            boolean UpRight = true;
            boolean DownLeft = true;
            boolean DownRight = true;

            //Loop 8 times, try to move the piece that many squares diagonally in each direction.
            for (int i = 1; i < 8 ;i++) {
                // Calculate the new row and column positions in each direction.
                int newLeftRow = currentRow - i;
                int newRightRow = currentRow + i;
                int newUpCol = currentCol + i;
                int newDownCol = currentCol - i;

                // If you are still on the board, AND you haven't run into another piece yet, Add the new position to the list using the method checkIfPeiceThereAndGo.
                if (newLeftRow >= 1 && newLeftRow <=8) {
                    if (newUpCol >= 1 && newUpCol <=8 && UpLeft) {
                        UpLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newUpCol,myColor);
                    }

                    if (newDownCol >= 1 && newDownCol <=8 && DownLeft) {
                        DownLeft = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,newDownCol,myColor);
                    }
                }

                // If you are still on the board, AND you haven't run into another piece yet, Add the new position to the list using the method checkIfPeiceThereAndGo.
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

        //Lets Kings move in any direction by 1 square.
        if (piece.getPieceType() == PieceType.KING) {
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
        }

        //Lets knights move in the L shape.
        if (piece.getPieceType() == PieceType.KNIGHT) {
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

        //This part gives all functionality to pawns.
        if (piece.getPieceType() == PieceType.PAWN) {

            //Lets define the move rules if the pawn is white first. The pawn will only move UP (row getting bigger)
            if (myColor == ChessGame.TeamColor.WHITE) {
                // If you haven't moved yet:
                if (currentRow == 2) {
                    boolean nooneInFront = checkIfSpotEmpty(board, currentRow+1, currentCol); // Sees if anyone is infront of the pawn.

                    //And no one is in front of you, you can move at least forward by one.
                    if (nooneInFront) {
                        checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow+1,currentCol,myColor);
                        boolean nooneInFront2 = checkIfSpotEmpty(board, currentRow+2, currentCol); // Sees if there is anyone 2 spaces in front of the pawn.

                        //If there is no one 2 squares of you TOO then you can move 2 forward as your first move with this piece.
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
                if (myPosition.getColumn() != 1) {
                    ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()-1);
                    ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);
                    if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol-1,myColor);
                    }
                }

                // This allows you to attack diagonally forward and RIGHT if there is a Black piece there.
                if (myPosition.getColumn() != 8) {
                    ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()+1,myPosition.getColumn()+1);
                    ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);
                    if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.BLACK) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow+1,currentCol+1,myColor);
                    }

                }

            // Now lets add the functionality if team color is BLACK. These pieces will move down the board. (Row value decreasing)
            } else {
                //If the pawn hasn't moved yet,
                if (currentRow == 7) {
                    boolean nooneInFront = checkIfSpotEmpty(board, currentRow-1, currentCol);

                    //AND there is nothing right in front of it
                    if (nooneInFront) {
                        //Then we can move there
                        checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow-1,currentCol,myColor);
                        boolean nooneInFront2 = checkIfSpotEmpty(board, currentRow-2, currentCol);

                        //Additionally, if there is nothing 2 spaces in front of it, then you can move 2 squares forward on your first move.
                        if (nooneInFront2) {
                            checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow-2,currentCol,myColor);
                        }
                    }

                // If the pawn already has moved at least once, then lets do this:
                } else {
                    boolean nooneInFront = checkIfSpotEmpty(board, currentRow-1, currentCol);

                    //See if anything is directly ahead of it. If not, it can move there.
                    if (nooneInFront) {
                        checkIfPeiceThereAndGo(board, ourList, myPosition, currentRow - 1, currentCol, myColor);
                    }
                }

                // Now we will cover the diagonal attacks. Starting with attacking diagonally down and to the right.
                if (myPosition.getColumn() != 1) {
                    ChessPosition leftDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()-1);
                    ChessPiece leftDiagonalPiece = board.getPiece(leftDiagonalPos);

                    //Check if there is an enemy in that diagonal square, if so, you can take it.
                    if (leftDiagonalPiece != null && leftDiagonalPiece.getTeamColor()==ChessGame.TeamColor.WHITE) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol-1,myColor);
                    }
                }

                //Now we will impliment the other half, diagonals going down and LEFT.
                if (myPosition.getColumn() != 8) {
                    ChessPosition rightDiagonalPos = new ChessPosition(myPosition.getRow()-1,myPosition.getColumn()+1);
                    ChessPiece rightDiagonalPiece = board.getPiece(rightDiagonalPos);

                    //Check if there is an enemy in that diagonal square, if so, you can take it.
                    if (rightDiagonalPiece != null && rightDiagonalPiece.getTeamColor()==ChessGame.TeamColor.WHITE) {
                        checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow-1,currentCol+1,myColor);
                    }

                }

            }


        }

        //If the piece is a Rook or a Queen, this allows it to move straight until it hits something.
        if (piece.getPieceType() == PieceType.ROOK || piece.getPieceType() == PieceType.QUEEN) {
            // Set up these variables to track when it runs into a piece and cannot go past it. These become false when it runs into a piece.
            boolean Up = true;
            boolean Right = true;
            boolean Left = true;
            boolean Down = true;

            // count 1-7 and let's move the rook in each direction that many spaces until it runs into something.
            for (int i = 1; i < 8 ;i++) {
                int newLeftRow = currentRow - i;
                int newRightRow = currentRow + i;
                int newUpCol = currentCol + i;
                int newDownCol = currentCol - i;

                //In each of these if statements we are checking if the new location is on the board. If so, we will try to move there.
                if (newLeftRow >= 1 && newLeftRow <=8 && Left) {
                    Left = checkIfPeiceThereAndGo(board,ourList,myPosition,newLeftRow,currentCol,myColor);
                }
                if (newRightRow >= 1 && newRightRow <=8 && Right) {
                    Right = checkIfPeiceThereAndGo(board,ourList,myPosition,newRightRow,currentCol,myColor);
                }
                if (newUpCol >= 1 && newUpCol <=8 && Up) {
                    Up = checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow,newUpCol,myColor);
                }
                if (newDownCol >= 1 && newDownCol <=8 && Down) {
                    Down = checkIfPeiceThereAndGo(board,ourList,myPosition,currentRow,newDownCol,myColor);
                }
            }
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
        return "ChessPiece{" + type + '}';
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
