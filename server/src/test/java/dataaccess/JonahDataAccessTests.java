package dataaccess;

import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import model.GameData;
import org.junit.jupiter.api.*;

import java.lang.reflect.Array;
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
        Assertions.assertEquals("randomUser", gameData.getWhiteUsername());
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


}
