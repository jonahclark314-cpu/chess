package service;

import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import server.Server;
import model.*;
import dataaccess.*;
import java.util.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JonahGameServiceTests {

    private static TestServerFacade serverFacade;
    private static Server server;
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
        server = new Server();
        var port = server.run(0);

        serverFacade = new TestServerFacade("localhost", Integer.toString(port));
    }

    @BeforeEach
    public void setup() {
        userService = new UserService();
        gameService = new GameService();
        authService = new AuthService();
        serverFacade.clear();
        //one user already logged in
    }

    @Test
    @Order(1)
    @DisplayName("Can change color")
    public void setColorCorrect() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertDoesNotThrow(()-> gameService.setColorForGame(setColorRequest));

        GameData game = gameService.getGame(gameID);
        Assertions.assertEquals("User", game.getWhiteUsername());


        SetColorGameRequest setColorRequest2 = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertDoesNotThrow(()-> gameService.setColorForGame(setColorRequest2));

        GameData game2 = gameService.getGame(gameID);
        Assertions.assertEquals("User", game2.getBlackUsername());
    }


    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 1")
    public void setColorCorrectErrors1() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        gameService.createGame(gameRequest);

        Assertions.assertThrows(BadRequestException.class, ()-> gameService.setColorForGame(null));
    }

    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 2")
    public void setColorCorrectErrors2() {

        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest = new SetColorGameRequest(gameID,"","User");
        Assertions.assertThrows(BadRequestException.class, ()-> gameService.setColorForGame(setColorRequest));
    }

    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 7")
    public void setColorCorrectErrors7() {

        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest = new SetColorGameRequest(gameID,"WHITE","");
        Assertions.assertThrows(BadRequestException.class, ()-> gameService.setColorForGame(setColorRequest));
    }



    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 3")
    public void setColorCorrectErrors3() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest2 = new SetColorGameRequest(0,"WHITE","User");
        Assertions.assertThrows(BadRequestException.class, ()-> gameService.setColorForGame(setColorRequest2));
    }

    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 6")
    public void setColorCorrectErrors6() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequest3 = new SetColorGameRequest(gameID,"BLUE","User");
        Assertions.assertThrows(BadRequestException.class, ()-> gameService.setColorForGame(setColorRequest3));
    }

    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 4")
    public void setColorCorrectErrors4() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequestCorrect = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertDoesNotThrow(()-> gameService.setColorForGame(setColorRequestCorrect));

        SetColorGameRequest setColorRequestCorrect2 = new SetColorGameRequest(gameID,"BLACK","User");
        Assertions.assertThrows(AlreadyTakenException.class, ()-> gameService.setColorForGame(setColorRequestCorrect2));
    }

    @Test
    @Order(2)
    @DisplayName("Error when mess up change color 5")
    public void setColorCorrectErrors5() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        SetColorGameRequest setColorRequestCorrect3 = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertDoesNotThrow(()-> gameService.setColorForGame(setColorRequestCorrect3));

        SetColorGameRequest setColorRequestCorrect4 = new SetColorGameRequest(gameID,"WHITE","User");
        Assertions.assertThrows(AlreadyTakenException.class, ()-> gameService.setColorForGame(setColorRequestCorrect4));
    }


    @Test
    @Order(3)
    @DisplayName("Can get Game")
    public void canGetGame() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        int gameID = gameService.createGame(gameRequest);

        Assertions.assertEquals(gameID,gameService.getGame(gameID).getGameID());
    }

    @Test
    @Order(4)
    @DisplayName("Error when tries to get nonexistent Game")
    public void canGetGameError() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest("newGame");
        gameService.createGame(gameRequest);

        Assertions.assertThrows(BadRequestException.class, () -> gameService.getGame(123));
    }

    @Test
    @Order(5)
    @DisplayName("can create game")
    public void canCreateGame() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
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
    public void canCreateGameError() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
        CreateGameRequest gameRequest = new CreateGameRequest(null);
        Assertions.assertThrows(BadRequestException.class, () -> gameService.createGame(gameRequest));
    }


    @Test
    @Order(7)
    @DisplayName("can get list")
    public void canGetListOfGames() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");
        userService.createUser(request1);
        authService.createAuth("User");
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
