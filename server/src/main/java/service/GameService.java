package service;
import dataaccess.*;
import model.GameData;

import java.util.ArrayList;

public class GameService {
    GameDAO gameDAO;

    public GameService () {
        this.gameDAO = new MemoryGameDAO();
    }

    public void clear() {
        this.gameDAO.clear();
    }

    public ArrayList<GameData> listGames () {
        return this.gameDAO.listGames();
    }
}
