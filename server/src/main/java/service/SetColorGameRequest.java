package service;

public class SetColorGameRequest {
    private final int gameID;
    private final String playerColor;
    private String username;


    public SetColorGameRequest (int gameID, String playerColor, String username) {
        this.playerColor = playerColor;
        this.gameID = gameID;
    }

    public int getGameID() {
        return this.gameID;
    }

    public String getPlayerColor () {
        return this.playerColor;
    }

    public void setUsername (String username) {
        this.username = username;
    }
    public String getUsername () {
        return this.username;
    }

}
