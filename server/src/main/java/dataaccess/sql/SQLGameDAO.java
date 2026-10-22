package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLGameDAO implements GameDAO {

    /**
     * This is what clears this portion of the db.
     */
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is what lists all the Games that we have stored.
     *
     * @return a list of the games.
     */
    @Override
    public ArrayList<GameData> listGames() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games")) {

                try (var rs = preparedStatement.executeQuery()) {
                    ArrayList<GameData> ourList = new ArrayList<>();
                    while (rs.next()) {
                        GameData gameData = new GameData(rs.getString("gameName"), rs.getInt("gameID"));
                        gameData.setWhiteUsername(rs.getString("whiteUsername"));
                        gameData.setBlackUsername(rs.getString("blackUsername"));
                        ChessGame gameObject = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                        gameData.setGameObject(gameObject);
                        ourList.add(gameData);
                    }
                    return ourList;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }

    }

    /**
     * This is what can get a specific game based off of the game id.
     *
     * @param gameID - integer. It represents the unique game.
     * @return returns the game object or null if there is no such game object.
     */
    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games WHERE gameID=?")
            ) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    GameData gameData = new GameData(rs.getString("gameName"), rs.getInt("gameID"));
                    gameData.setWhiteUsername(rs.getString("whiteUsername"));
                    gameData.setBlackUsername(rs.getString("blackUsername"));
                    ChessGame gameObject = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    gameData.setGameObject(gameObject);
                    return gameData;
                } catch (Exception e) {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }

    }

    /**
     * This is what is used to create a new game.
     *
     * @param gameName - name of the game
     * @param gameID   - pre-generated integer representing the specific game.
     */
    @Override
    public void createGame(String gameName, int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "INSERT INTO games (gameID, gameName, game) VALUES(?, ?, ?)")
            ) {
                String stringGameID = Integer.toString(gameID);
                preparedStatement.setString(1, stringGameID);
                preparedStatement.setString(2, gameName);
                ChessGame game = new ChessGame();
                var json = new Gson().toJson(game);
                preparedStatement.setString(3, json);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create game", e);
        }
    }

    /**
     * This is the method that we use to update the game.
     *
     * @param game - new game object that you want saved in the db.
     */
    @Override
    public void updateGame(GameData game) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement(
                    "UPDATE games SET gameName=?, whiteUsername=?, blackUsername=?, game=? WHERE gameID=?")
            ) {
                preparedStatement.setString(1, game.getGameName());
                preparedStatement.setString(2, game.getWhiteUsername());
                preparedStatement.setString(3, game.getBlackUsername());

                ChessGame actualGameObject = game.getGame();
                var json = new Gson().toJson(actualGameObject);
                preparedStatement.setString(4, json);

                preparedStatement.setString(5, Integer.toString(game.getGameID()));

                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to update game", e);
        }
    }

}
