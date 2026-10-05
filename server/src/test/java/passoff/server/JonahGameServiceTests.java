package passoff.server;

import chess.ChessGame;
import org.junit.jupiter.api.*;
import passoff.model.*;
import server.Server;
import service.RegisterRequest;
import service.*;
import server.*;
import model.*;
import dataaccess.*;
import java.net.HttpURLConnection;
import java.util.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JonahGameServiceTests {

    private static TestUser existingUser;
    private static TestUser newUser;
    private static TestCreateRequest createRequest;
    private static TestServerFacade serverFacade;
    private static Server server;
    private String existingAuth;
    private static AuthService authService;
    private static UserService userService;
    private static GameService gameService;
    // ### TESTING SETUP/CLEANUP ###

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeAll
    public static void init() {
        userService = new UserService();
        gameService = new GameService();
        authService = new AuthService();
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
        existingUser = new TestUser("ExistingUser", "existingUserPassword", "eu@mail.com");
        newUser = new TestUser("NewUser", "newUserPassword", "nu@mail.com");
        createRequest = new TestCreateRequest("testGame");
    }

    @BeforeEach
    public void setup() {
        serverFacade.clear();
        userService.clear();
        authService.clear();
        gameService.clear();
        //one user already logged in
        TestAuthResult regResult = serverFacade.register(existingUser);
        existingAuth = regResult.getAuthToken();
    }

    @Test
    @Order(1)
    @DisplayName("Can change color")
    public void setColorCorrect() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertDoesNotThrow(()->{gameService.setColorForGame(setColorRequest);});

        GameData game = gameService.getGame(gameID);
        Assertions.assertEquals("User", game.getWhiteUsername());


        SetColorGameRequest setColorRequest2 = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertDoesNotThrow(()->{gameService.setColorForGame(setColorRequest2);});

        GameData game2 = gameService.getGame(gameID);
        Assertions.assertEquals("User", game2.getBlackUsername());
    }


    @Test
    @Order(2)
    @DisplayName("Error when mess up change color")
    public void setColorCorrect_Errors() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        Assertions.assertThrows(BadRequestException.class, ()->{gameService.setColorForGame(null);});

        SetColorGameRequest setColorRequest = new SetColorGameRequest(gameID,"","User");
        Assertions.assertThrows(BadRequestException.class, ()->{gameService.setColorForGame(setColorRequest);});

        SetColorGameRequest setColorRequest2 = new SetColorGameRequest(0,"WHITE","User");
        Assertions.assertThrows(BadRequestException.class, ()->{gameService.setColorForGame(setColorRequest2);});

        SetColorGameRequest setColorRequest3 = new SetColorGameRequest(gameID,"BLUE","User");
        Assertions.assertThrows(BadRequestException.class, ()->{gameService.setColorForGame(setColorRequest3);});

        SetColorGameRequest setColorRequestCorrect = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertDoesNotThrow(()->{gameService.setColorForGame(setColorRequestCorrect);});

        SetColorGameRequest setColorRequestCorrect2 = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertThrows(AlreadyTakenException.class, ()->{gameService.setColorForGame(setColorRequestCorrect2);});

        SetColorGameRequest setColorRequestCorrect3 = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertDoesNotThrow(()->{gameService.setColorForGame(setColorRequestCorrect3);});

        SetColorGameRequest setColorRequestCorrect4 = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertThrows(AlreadyTakenException.class, ()->{gameService.setColorForGame(setColorRequestCorrect4);});
    }

    @Test
    @Order(3)
    @DisplayName("Can get Game")
    public void canGetGame() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        Assertions.assertEquals(gameID,gameService.getGame(gameID).getGameID());
    }

    @Test
    @Order(4)
    @DisplayName("Error when tries to get nonexistant Game")
    public void canGetGame_Error() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        Assertions.assertThrows(BadRequestException.class, () -> {gameService.getGame(123);});
    }

    @Test
    @Order(5)
    @DisplayName("can create game")
    public void canCreateGame() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");

        int length = gameService.listGames().size();
        Assertions.assertEquals(0,length);

        Assertions.assertDoesNotThrow(() -> {gameService.createGame(gameRequest);});
        int length2 = gameService.listGames().size();
        Assertions.assertEquals(1,length2);


    }

    @Test
    @Order(6)
    @DisplayName("Errors for create game")
    public void canCreateGame_Error() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest(null);
        Assertions.assertThrows(BadRequestException.class, () -> {gameService.createGame(gameRequest);});
    }


    @Test
    @Order(7)
    @DisplayName("can get list")
    public void canGetListOfGames() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        LoginResult loggedIn = authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");

        Assertions.assertInstanceOf(ArrayList.class,gameService.listGames());
        int length = gameService.listGames().size();
        Assertions.assertEquals(0,length);

        gameService.createGame(gameRequest);

        Assertions.assertInstanceOf(ArrayList.class,gameService.listGames());
        int length2 = gameService.listGames().size();
        Assertions.assertEquals(1,length2);


    }




}
