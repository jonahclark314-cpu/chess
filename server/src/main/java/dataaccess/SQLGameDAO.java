package dataaccess;

import model.GameData;

import java.util.ArrayList;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLGameDAO implements GameDAO{
    @Override
    public void clear() {

    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void createGame(String gameName, int gameID) {

    }

    @Override
    public void updateGame(GameData game) {

    }

}
