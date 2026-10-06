package service;

/**
 * This is the request object for creating a game.
 */
public class CreateGameRequest {
    // Just stores the game name.
    String gameName;

    //Constructor
    public CreateGameRequest (String gameName) {
        this.gameName = gameName;
    }

    /**
     * function to get the name
     * @return A STRING! the gameName.
     */
    public String getGameName() {
        if (this.gameName == null) {
            return "";
        }
        return gameName;
    }
}
