package service;

/**
 * This is the class that represents the request to set a color.
 */
public class SetColorGameRequest {
    // Variables that are stored in this request:
    private final int gameID;
    private final String playerColor;
    private String username;

    // Constructor:
    public SetColorGameRequest (int gameID, String playerColor, String username) {
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.username = username;
    }

    /**
     * This returns the game ID.
     * @return int game ID.
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * this returns the player color requested.
     * @return - String color.
     */
    public String getPlayerColor () {
        return this.playerColor;
    }

    /**
     * this sets the username to a given value
     * @param username - User's username.
     */
    public void setUsername (String username) {
        this.username = username;
    }

    /**
     * This returns the username that is stored.
     * @return - string username.
     */
    public String getUsername () {
        return this.username;
    }

}
