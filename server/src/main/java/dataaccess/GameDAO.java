package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    void clear();
    ArrayList<GameData> listGames();
    GameData getGame(int gameID);
    boolean cantUseGameID(int gameID);
    void createGame(String gameName, int gameID);
    void updateGame (GameData game);
}
