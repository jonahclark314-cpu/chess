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
        String name = request.getGameName();
        if (name.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        int random = getPossibleGameID();
        gameDAO.createGame(name, random);
        return random;

    }


    public GameData getGame (int gameID) throws BadRequestException {
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        return game;
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

        if (color == null) {
            throw new BadRequestException("Error: bad request");
        }

        String username = request.getUsername();

        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        GameData game = this.gameDAO.getGame(gameID);

        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (color.equals("WHITE")) {
            if (game.getWhiteUsername() == null) {
                game.setWhiteUsername((username));
                this.gameDAO.updateGame(game);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        else if (color.equals("BLACK")) {
            if (game.getBlackUsername() == null) {
                game.setBlackUsername(username);
                this.gameDAO.updateGame(game);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        else {
            throw new BadRequestException("Error: bad request");
        }
    }


}
