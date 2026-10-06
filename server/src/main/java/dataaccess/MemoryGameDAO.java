package dataaccess;

import model.*;

import java.util.ArrayList;

/**
 * This is the implementation I use while I do not have access to the db.
 */
public class MemoryGameDAO implements GameDAO{

    ArrayList<GameData> listOfGameData;

    /**
     * This is the implementation of the class
     */
    public MemoryGameDAO() {
        this.listOfGameData = new ArrayList<>();
    }

    /**
     * This is what clears this portion of the db.
     */
    @Override
    public void clear() {
        this.listOfGameData.clear();
    }

    /**
     * This is what lists all the Games that we have stored.
     * @return a list of the games.
     */
    @Override
    public ArrayList<GameData> listGames() {
        return listOfGameData;
    }

    /**
     * This is what can get a specific game based off of the game id.
     * @param gameID - integer. It represents the unique game.
     * @return returns the game object or null if there is no such game object.
     */
    @Override
    public GameData getGame(int gameID) {
        for (GameData game: this.listOfGameData) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    /**
     * This is what is used to create a new game.
     * @param gameName - name of the game
     * @param gameID - pre-generated integer representing the specific game.
     */
    @Override
    public void createGame(String gameName, int gameID) {
        GameData newGame = new GameData(gameName, gameID);
        listOfGameData.add(newGame);
    }

    /**
     * This is the method that we use to update the game.
     * @param game - new game object that you want saved in the db.
     */
    @Override
    public void updateGame(GameData game) {
        for (GameData currentGame : this.listOfGameData) {
            if (currentGame.getGameID() == game.getGameID()) {
                listOfGameData.remove(currentGame);
                listOfGameData.add(game);
            }
        }
    }


}
