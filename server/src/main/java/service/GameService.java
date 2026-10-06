package service;
import dataaccess.*;
import model.GameData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This is the Game Service class. It has all the logic for interacting with the games.
 */
public class GameService {
    // Here is the DAO for the games that we will use here.
    GameDAO gameDAO;

    // Constructor.
    public GameService () {
        this.gameDAO = new MemoryGameDAO();
    }

    /**
     * this is the method that tells the DAO to delete all Games data.
     */
    public void clear() {
        this.gameDAO.clear();
    }

    /**
     * This is the method that asks the DAO for a list of all the games in the db.
     * @return list of all the games.
     */
    public ArrayList<GameData> listGames () {
        return this.gameDAO.listGames();
    }

    /**
     * This is the method that tells the DAO to create a game with a specific name. It also generates the random gameID
     * @param request - request object that contains the gameName.
     * @return - returns the gameID
     * @throws BadRequestException - if there is no game name included, mark this as a bad request.
     */
    public int createGame(CreateGameRequest request) throws BadRequestException {
        String name = request.getGameName();
        if (name.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        // Generate the random ID and tell the DAO to make the game.
        int random = getPossibleGameID();
        gameDAO.createGame(name, random);
        return random;
    }

    /**
     * This is the method that will get the GameData object that represents a particular game.
     * @param gameID - the int Game ID that represents the game of interest.
     * @return - returns the GameData object.
     * @throws BadRequestException - if the game ID is empty or not correct.
     */
    public GameData getGame (int gameID) throws BadRequestException {
        GameData game = gameDAO.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }
        return game;
    }

    /**
     * This is the helper function that will create a 9 digit integer gameID.
     * @return integer game ID.
     */
    public static int getPossibleGameID() {
        return ThreadLocalRandom.current().nextInt(100000000, 1000000000);
    }

    /**
     * This is the method that tells the DAO to put a user as one of the colors.
     * @param request - request object containing info on the game, the requested color, and the user.
     * @throws BadRequestException - If any of the info is already taken, throw an exception.
     * @throws AlreadyTakenException - If a user requests to be a color that already is taken, throw an exception.
     */
    public void setColorForGame(SetColorGameRequest request) throws BadRequestException, AlreadyTakenException{
        // Make sure that there is a request that is being passed in.
        if (request == null){
            throw new BadRequestException("Error: bad request");
        }

        int gameID = request.getGameID();
        String color = request.getPlayerColor();

        // Make sure that there is a color requested.
        if (color == null) {
            throw new BadRequestException("Error: bad request");
        }

        // Make sure that there is a username provided.
        String username = request.getUsername();
        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        // Make sure the requested game is valid.
        GameData game = this.gameDAO.getGame(gameID);
        if (game == null) {
            throw new BadRequestException("Error: bad request");
        }

        // If the requested color is WHITE, make sure it is not already taken and tell DAO to update it.
        if (color.equals("WHITE")) {
            if (game.getWhiteUsername() == null) {
                game.setWhiteUsername((username));
                this.gameDAO.updateGame(game);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        // If the requested color is BLACK, make sure it is not already taken and tell DAO to update it.
        else if (color.equals("BLACK")) {
            if (game.getBlackUsername() == null) {
                game.setBlackUsername(username);
                this.gameDAO.updateGame(game);
            } else {
                throw new AlreadyTakenException("Error: already taken");
            }
        }
        else { // If any other color is requested (such as BLUE), throw an exception.
            throw new BadRequestException("Error: bad request");
        }
    }


}
