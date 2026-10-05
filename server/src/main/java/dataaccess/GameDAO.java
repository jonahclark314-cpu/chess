package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {

    public void clear();
    public ArrayList<GameData> listGames();
    public GameData getGame(int gameID);
    public boolean cantUseGameID(int gameID);
    public void createGame(String gameName, int gameID);
    public void setWhiteColor(String username, int gameID);
    public void setBlackColor(String username, int gameID);

}
