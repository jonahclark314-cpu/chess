package model;

import chess.*;


public class GameData {
    private final int gameID;
    private String whiteUsername;
    private String blackUsername;
    private final String gameName;
    private final ChessGame game;

    public GameData(String name, int gameID) {
        this.gameName = name;
        this.gameID = gameID;
        this.whiteUsername = null;
        this.blackUsername = null;
        this.game = new ChessGame();
    }

    public int getGameID () {
        return this.gameID;
    }

    public String getWhiteUsername() {
        return this.whiteUsername;
    }

    public void setWhiteUsername (String username) {
        this.whiteUsername = username;
    }

    public void setBlackUsername (String username) {
        this.blackUsername = username;
    }

    public String getBlackUsername () {
        return this.blackUsername;
    }

    public String getGameName() {
        return gameName;
    }

    public ChessGame getGame() {
        return this.game;
    }


}
