package dataaccess;

import java.sql.SQLException;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLAuthDAO implements AuthDAO {

    @Override
    public void clear() {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("TRUNCATE TABLE auths")) {
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: failed to create user", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

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
            } catch (SQLException e) {
                throw new DataAccessException("Error: failed to create user", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        }
    }

    @Override
    public void deleteAuth(String authToken) {
        try (var conn = DatabaseManager.getConnection()) {
            try (var preparedStatement = conn.prepareStatement("DELETE FROM auths WHERE authToken=?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException("Error: failed to create user", e);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to delete the authToken", e);
        }

    }
}
