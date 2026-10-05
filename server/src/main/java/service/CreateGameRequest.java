package service;

import model.GameData;

import java.util.ArrayList;

public class CreateGameRequest {
    String gameName;

    public CreateGameRequest (String gameName) {
        this.gameName = gameName;
    }

    public String getGameName() {
        if (this.gameName == null) {
            return "";
        }
        return gameName;
    }
}
