package service;

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
