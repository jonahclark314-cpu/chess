package service;
import dataaccess.*;
import io.javalin.http.Context;

public class GameService {
    GameDAO gameDAO;

    public GameService () {
        this.gameDAO = new MemoryGameDAO();
    }

    public void clear() {
        this.gameDAO.clear();
    }
}
