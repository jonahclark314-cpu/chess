package dataaccess;

import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;
import service.RegisterRequest;

import java.sql.SQLException;
import java.util.ArrayList;


@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JonahDataAccessTests {
    private static AuthDAO authDAO;
    private static UserDAO userDAO;
    private static GameDAO gameDAO;

    // ### TESTING SETUP/CLEANUP ###

    @AfterAll

    @BeforeAll
    public static void init() {
    }

    @BeforeEach
    public void setup() {
        authDAO = new SQLAuthDAO();
        userDAO = new SQLUserDAO();
        gameDAO = new SQLGameDAO();
        userDAO.clear();
        authDAO.clear();
        gameDAO.clear();
    }

    @Test
    @Order(1)
    @DisplayName("can clear Databases.")
    public void checkAllClearWorks() {
        Assertions.assertDoesNotThrow(() -> authDAO.clear(), "Clearing the database should not cause any problems");
        Assertions.assertDoesNotThrow(() -> gameDAO.clear(), "Clearing the database should not cause any problems");
        Assertions.assertDoesNotThrow(() -> userDAO.clear(), "Clearing the database should not cause any problems");
    }


    @Test
    @Order(2)
    @DisplayName("can create Auth and get Auth.")
    public void canCreateGetAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("username", "AuthToken123"));
        Assertions.assertDoesNotThrow(() -> authDAO.getAuth("AuthToken123"));
        Assertions.assertEquals("username", authDAO.getAuth("AuthToken123"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth(null, "AT123"));
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth("username2", null));
        Assertions.assertEquals("", authDAO.getAuth("IncorrectToken"));
    }


    @Test
    @Order(3)
    @DisplayName("can delete Auth.")
    public void canDeleteAuth() {
        Assertions.assertDoesNotThrow(() -> authDAO.createAuth("username", "AuthToken123"));
        Assertions.assertDoesNotThrow(() -> authDAO.deleteAuth("AuthToken123"));
    }

    @Test
    @Order(3)
    @DisplayName("Auth Errors.")
    public void authErrors() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE auths";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                Assertions.assertThrows(DataAccessException.class, () -> authDAO.clear());
                Assertions.assertThrows(DataAccessException.class, () -> authDAO.createAuth("nonexistant", "NotGoingToWork"));
                Assertions.assertThrows(DataAccessException.class, () -> authDAO.deleteAuth("someToken"));

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        } finally {
            DatabaseManager.configureDatabase();
        }
    }

    @Test
    @Order(4)
    @DisplayName("Game Errors.")
    public void gameErrors() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE games";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                Assertions.assertThrows(DataAccessException.class, () -> gameDAO.clear());
                Assertions.assertThrows(DataAccessException.class, () -> gameDAO.listGames());
                Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGame("gameName", 123));
                GameData gameData = new GameData("gameName", 123);
                Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(gameData));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        } finally {
            DatabaseManager.configureDatabase();
        }
    }

    @Test
    @Order(4)
    @DisplayName("can createGame.")
    public void canCreateGame() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1", 123));
    }

    @Test
    @Order(5)
    @DisplayName("can get Game.")
    public void canGetGame() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1", 123));
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(123));
        GameData gameData = gameDAO.getGame(123);
        Assertions.assertEquals("game1", gameData.getGameName());
        Assertions.assertNull(gameDAO.getGame(124));
    }

    @Test
    @Order(6)
    @DisplayName("can update Game.")
    public void canUpdateGame() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1", 123));
        Assertions.assertDoesNotThrow(() -> gameDAO.getGame(123));
        GameData gameData = gameDAO.getGame(123);
        gameData.setWhiteUsername("randomUser");
        Assertions.assertDoesNotThrow(() -> gameDAO.updateGame(gameData));
        GameData gameData2 = gameDAO.getGame(123);
        Assertions.assertEquals("randomUser", gameData2.getWhiteUsername());
    }

    @Test
    @Order(7)
    @DisplayName("can list Games.")
    public void canListGame() {
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game1", 123));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game2", 124));
        Assertions.assertDoesNotThrow(() -> gameDAO.createGame("game3", 125));

        Assertions.assertDoesNotThrow(() -> gameDAO.listGames());
        ArrayList<GameData> ourList = gameDAO.listGames();
        Assertions.assertEquals(3, ourList.size());

        gameDAO.clear();

        Assertions.assertDoesNotThrow(() -> gameDAO.listGames());
        ArrayList<GameData> ourList2 = gameDAO.listGames();
        Assertions.assertEquals(0, ourList2.size());
    }

    @Test
    @Order(8)
    @DisplayName("can create User.")
    public void canCreateUser() {
        RegisterRequest request1 = new RegisterRequest("username1", "password1", "email1@gmail.com");
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(request1));

        RegisterRequest request2 = new RegisterRequest("username1", "password1", "email1@gmail.com");
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(request2));

    }

    @Test
    @Order(8)
    @DisplayName("User Errors.")
    public void userErrors() {
        try (var conn = DatabaseManager.getConnection()) {
            String statement = "DROP TABLE users";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.executeUpdate();
                RegisterRequest request2 = new RegisterRequest("username1", "password1", "email1@gmail.com");
                Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(request2));
                Assertions.assertThrows(DataAccessException.class, () -> userDAO.clear());
                Assertions.assertThrows(DataAccessException.class, () -> userDAO.getLenUsers());
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to create user", e);
        } finally {
            DatabaseManager.configureDatabase();
        }
    }

    @Test
    @Order(9)
    @DisplayName("can get User.")
    public void canGetUser() {
        Assertions.assertEquals(0, userDAO.getLenUsers());
        RegisterRequest request1 = new RegisterRequest("username1", "password1", "email1@gmail.com");
        Assertions.assertDoesNotThrow(() -> userDAO.createUser(request1));
        Assertions.assertDoesNotThrow(() -> userDAO.getUser("username1"));
        UserData userData = userDAO.getUser("username1");
        Assertions.assertEquals("password1", userData.password());

        Assertions.assertEquals(1, userDAO.getLenUsers());

        Assertions.assertDoesNotThrow(() -> userDAO.getUser("username_NONEXISTANT"));
        UserData nonExistantUserData = userDAO.getUser("username_NONEXISTANT");
        Assertions.assertNull(nonExistantUserData);
    }


}
