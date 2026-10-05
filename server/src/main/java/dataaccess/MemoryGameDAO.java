package dataaccess;

import model.*;

import java.util.ArrayList;

public class MemoryGameDAO implements GameDAO{

    ArrayList<GameData> listOfGameData;


    public MemoryGameDAO() {
        this.listOfGameData = new ArrayList<>();
    }

    @Override
    public void clear() {
        this.listOfGameData.clear();
    }

    @Override
    public ArrayList<GameData> listGames() {
        return listOfGameData;
    }

    @Override
    public GameData getGame(int gameID) {
        for (GameData game: this.listOfGameData) {
            if (game.getGameID() == gameID) {
                return game;
            }
        }
        return null;
    }

    @Override
    public boolean cantUseGameID(int gameID) {
        for (GameData game : this.listOfGameData) {
            if (game.getGameID() == gameID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void createGame(String gameName, int gameID) {
        GameData newGame = new GameData(gameName, gameID);
        listOfGameData.add(newGame);
    }

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
