package dataaccess;

import model.GameData;

import java.util.ArrayList;

/**
 * This is the Game Data Access Object Interface.
 */
public interface GameDAO {
    //Here are all the methods I will use in this DAO.
    void clear();
    ArrayList<GameData> listGames();
    GameData getGame(int gameID);
    void createGame(String gameName, int gameID);
    void updateGame (GameData game);
}
