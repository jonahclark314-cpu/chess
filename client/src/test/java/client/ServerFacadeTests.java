package client;

import exception.ResponseException;
import model.ListGamesResult;
import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class ServerFacadeTests {

    private static Server server;
    private static int port;
    private ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        port = server.run(0);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() throws Exception {
        var request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:" + port + "/db"))
                .DELETE()
                .build();
        HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        facade = new ServerFacade(port);
    }

    @Test
    void registerSuccess() throws Exception {
        String username = facade.register("testUser", "password", "testUser@email.com");
        Assertions.assertEquals("testUser", username);
        ListGamesResult games = facade.listGames();
        Assertions.assertTrue(games.games().isEmpty());
    }

    @Test
    void registerDuplicateUsername() throws Exception {
        facade.register("player", "password", "p@email.com");
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.register("player", "password", "p@email.com"));
        Assertions.assertEquals(ResponseException.Code.AlreadyTaken, ex.code());
    }

    @Test
    void signInSuccess() throws Exception {
        facade.register("playerSignin", "password", "pS@email.com");
        ServerFacade again = new ServerFacade(port);
        Assertions.assertEquals("playerSignin", again.signIn("playerSignin", "password"));
        Assertions.assertNotNull(again.listGames());
    }

    @Test
    void signInWrongPassword() throws Exception {
        facade.register("user", "password", "u@email.com");
        ServerFacade again = new ServerFacade(port);
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> again.signIn("user", "wrongPasswordddddd"));
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void createAndListGame() throws Exception {
        facade.register("guy", "password", "g@email.com");
        Assertions.assertEquals("Created a game called:guysGame", facade.createGame("guysGame"));
        var games = facade.listGames().games();
        Assertions.assertEquals(1, games.size());
        Assertions.assertEquals("guysGame", games.getFirst().getGameName());
    }

    @Test
    void listGamesWithoutLogin() {
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.listGames());
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void logoutThenListFails() throws Exception {
        facade.register("guy", "password", "g@email.com");
        facade.logout();
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.logout());
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void joinGameSuccess() throws Exception {
        facade.register("guy", "password", "g@email.com");
        facade.createGame("guysGame");
        int gameID = facade.listGames().games().getFirst().getGameID();
        String result = facade.joinGame(gameID, "WHITE");
        Assertions.assertTrue(result.contains("WHITE"));
        Assertions.assertNotNull(facade.getBoard());
    }

    @Test
    void joinBadColor() throws Exception {
        facade.register("badGuy", "password", "bg@email.com");
        facade.createGame("badGuyGame");
        int gameID = facade.listGames().games().getFirst().getGameID();
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.joinGame(gameID, "BLUE"));
        Assertions.assertEquals(ResponseException.Code.BadRequest, ex.code());
    }

    @Test
    void getBoardBeforeJoining() throws Exception {
        facade.register("guy", "password", "g@email.com");
        Assertions.assertNull(facade.getBoard());
    }

    @Test
    void leaveGameClearsBoard() throws Exception {
        facade.register("guy", "password", "g@email.com");
        facade.createGame("guysGame");
        int gameID = facade.listGames().games().getFirst().getGameID();
        facade.setUserInGame(gameID);
        Assertions.assertNotNull(facade.getBoard());
        facade.leaveGame();
        Assertions.assertNull(facade.getBoard());
    }


}
