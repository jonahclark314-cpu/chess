package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.UserDAO;
import model.UserData;
import service.RegisterRequest;

import java.sql.SQLException;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLUserDAO implements UserDAO {

    /**
     * This is what clears this portion of the db.
     */
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is what gets a specific users data from their username.
     *
     * @param username - string username.
     * @return - returns the person object or null if there is no such username in use.
     */
    @Override
    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT username, password, email FROM users WHERE username=?")) {
                preparedStatement.setString(1, username);
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                } catch (SQLException e) {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is what is used during registration of a new user.
     *
     * @param userInfo - This is the RegisterRequest object passed in by the Service.
     */
    @Override
    public void createUser(RegisterRequest userInfo) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("INSERT INTO users (username, password, email) VALUES(?, ?, ?)")) {
                preparedStatement.setString(1, userInfo.username());
                preparedStatement.setString(2, userInfo.password());
                preparedStatement.setString(3, userInfo.email());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    /**
     * This is the way we can test how many users are in our database. This is only
     * used for testing purposes.
     *
     * @return - number of users in the db.
     */
    @Override
    public int getLenUsers() {
        System.out.println("calling: getLenUsers()");
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    return rs.getInt("COUNT(*)");
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }
}
