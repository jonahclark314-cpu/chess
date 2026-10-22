package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLGameDAO implements GameDAO {
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE games")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }
    }

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
                        ChessGame gameObject = new Gson().fromJson(rs.getString("game"),ChessGame.class);
                        gameData.setGameObject(gameObject);
                        ourList.add(gameData);
                    }
                    return ourList;
                } catch (Exception e) {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }

    }

    @Override
    public GameData getGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT gameID, gameName, whiteUsername, blackUsername, game FROM games WHERE gameID=?")) {
                preparedStatement.setString(1, Integer.toString(gameID));
                try (var rs = preparedStatement.executeQuery()) {
                    GameData gameData = new GameData(rs.getString("gameName"), rs.getInt("gameID"));
                    gameData.setWhiteUsername(rs.getString("whiteUsername"));
                    gameData.setBlackUsername(rs.getString("blackUsername"));
                    ChessGame gameObject = new Gson().fromJson(rs.getString("game"),ChessGame.class);
                    gameData.setGameObject(gameObject);
                    return gameData;
                } catch (Exception e) {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }

    }

    @Override
    public void createGame(String gameName, int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO games (gameID, gameName, game) VALUES(?, ?, ?)")) {
                String stringGameID = Integer.toString(gameID);
                preparedStatement.setString(1, stringGameID);
                preparedStatement.setString(2, gameName);
                ChessGame game = new ChessGame();
                var json = new Gson().toJson(game);
                preparedStatement.setString(3, json);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create game", e);
        }
    }

    @Override
    public void updateGame(GameData game) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("UPDATE games SET gameName=?, whiteUsername=?, blackUsername=?, game=? WHERE gameID=?")) {
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
            throw new DataAccessException("failed to update game", e);
        }
    }

}
