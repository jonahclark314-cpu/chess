package dataaccess;

import model.GameData;

import java.util.ArrayList;

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
    public boolean cantUseGameID(int gameID) {
        return false;
    }

    @Override
    public void createGame(String gameName, int gameID) {

    }

    @Override
    public void setWhiteColor(String username, int gameID) {

    }

    @Override
    public void setBlackColor(String username, int gameID) {

    }
}
