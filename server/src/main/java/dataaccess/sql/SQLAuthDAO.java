package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;

import java.sql.SQLException;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLAuthDAO implements AuthDAO {

    /**
     * clears all the data in this database.
     */
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This logs in a person and returns their authToken
     *
     * @param username  - the username of the user.
     * @param authToken - a pre-generated authoring.
     * @return AuthToken
     */
    @Override
    public String createAuth(String username, String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO auths (username,authToken) VALUES(?, ?)")) {
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, authToken);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: failed to create user", e);
            }
            return authToken;
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is the way you can check if someone is logged in.
     *
     * @param authToken - this is the authToken string.
     * @return returns the username if they are logged in, and empty string otherwise.
     */
    @Override
    public String getAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username FROM auths WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    return rs.getString("username");
                } catch (Exception e) {
                    return "";
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is how you can log someone out.
     *
     * @param authToken - the auth token that they are logged in through.
     */
    @Override
    public void deleteAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to delete the authToken", e);
        }
    }
}
