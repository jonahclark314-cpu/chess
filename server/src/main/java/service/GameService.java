package service;
import dataaccess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

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

    public int createGame(CreateGameRequest request) throws BadRequestException {
        String name = request.gameName();
        if (name.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        boolean cont = true;
        int random = getPossibleGameID();
        while (cont) {
            if (this.gameDAO.cantUseGameID(random)) {
                random = getPossibleGameID();
            } else {
                cont = false;
            }
        }
        gameDAO.createGame(name, random);
        return random;
        }


    public static int getPossibleGameID() {
        return ThreadLocalRandom.current().nextInt(100000000, 1000000000);
    }

    public void setColorForGame(SetColorGameRequest request) throws BadRequestException, AlreadyTakenException{
        if (request == null){
            throw new BadRequestException("Error: bad request");
        }
        int gameID = request.getGameID();
        String color = request.getPlayerColor();
        String username = request.getUsername();

        GameData game = this.gameDAO.getGame(gameID);

        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (color.equals("WHITE")) {
            if (game.getWhiteUsername().isEmpty()) {
                this.gameDAO.setWhiteColor(username, gameID);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        if (color.equals("BLACK")) {
            if (game.getBlackUsername().isEmpty()) {
                this.gameDAO.setBlackColor(username, gameID);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
    }


}
