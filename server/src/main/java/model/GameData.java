package model;

import chess.*;


public class GameData {
    int gameID;
    String whiteUsername;
    String blackUsername;
    String gameName;
    ChessGame game;

    public GameData(String name, int gameID) {
        this.gameName = name;
        this.gameID = gameID;
        this.whiteUsername = "";
        this.blackUsername = "";
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
