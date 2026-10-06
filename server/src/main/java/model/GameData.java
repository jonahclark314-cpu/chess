package model;

import chess.*;

/**
 * This is the class we will use to represent Game Data objects.
 */
public class GameData {
    // Define variables we want to store for each game:
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    /**
     * This is the constructor for the game
     * @param name - need a game name
     * @param gameID - need a pre generated gameID.
     */
    public GameData(String name, int gameID) {
        this.gameName = name;
        this.gameID = gameID;
        // Set the two players as null to begin with.
        this.whiteUsername = null;
        this.blackUsername = null;
        // Create the fresh new chess game!
        this.game = new ChessGame();
    }

    /**
     * This is the function that returns the game ID
     * @return - int representing the specific game.
     */
    public int getGameID () {
        return this.gameID;
    }

    /**
     * Gets the user playing as the white.
     * @return - username of person playing as white
     */
    public String getWhiteUsername() {
        return this.whiteUsername;
    }

    /**
     * This is how we can change the player.
     * @param username - username of the player.
     */
    public void setWhiteUsername (String username) {
        this.whiteUsername = username;
    }

    /**
     * This is how we can change the player.
     * @param username - username of the player.
     */
    public void setBlackUsername (String username) {
        this.blackUsername = username;
    }


    /**
     * Gets the user playing as the black.
     * @return - username of person playing as black.
     */
    public String getBlackUsername () {
        return this.blackUsername;
    }



}
