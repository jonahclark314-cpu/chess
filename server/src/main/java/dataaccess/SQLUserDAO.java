package dataaccess;

import model.UserData;
import service.RegisterRequest;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLUserDAO implements UserDAO {
    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE users")) {
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }
    }

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
            throw new DataAccessException("failed to create user", e);
        }
    }

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
            throw new DataAccessException("failed to create user", e);
        }
    }


    @Override
    public int getLenUsers() {
        System.out.println("calling: getLenUsers()");
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("SELECT COUNT(*) FROM users")) {
                try (var rs = preparedStatement.executeQuery()) {
                    rs.next();
                    return rs.getInt("COUNT(*)");
                } catch (Exception e) {
                    return 0;
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("failed to create user", e);
        }
    }
}
