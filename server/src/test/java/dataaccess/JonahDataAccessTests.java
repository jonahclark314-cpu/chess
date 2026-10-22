package dataaccess;

import dataaccess.sql.SQLAuthDAO;
import dataaccess.sql.SQLGameDAO;
import dataaccess.sql.SQLUserDAO;
import org.junit.jupiter.api.*;


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

}
