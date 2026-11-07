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
        String username = facade.register("player1", "password", "p1@email.com");
        Assertions.assertEquals("player1", username);
        ListGamesResult games = facade.listGames();
        Assertions.assertTrue(games.games().isEmpty());
    }

    @Test
    void registerDuplicateUsername() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.register("player1", "password", "p1@email.com"));
        Assertions.assertEquals(ResponseException.Code.AlreadyTaken, ex.code());
    }

    @Test
    void signInSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        ServerFacade again = new ServerFacade(port);
        Assertions.assertEquals("player1", again.signIn("player1", "password"));
        Assertions.assertNotNull(again.listGames());
    }

    @Test
    void signInWrongPassword() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        ServerFacade again = new ServerFacade(port);
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> again.signIn("player1", "nope"));
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void createAndListGame() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertEquals("Created a game called:fun", facade.createGame("fun"));
        var games = facade.listGames().games();
        Assertions.assertEquals(1, games.size());
        Assertions.assertEquals("fun", games.get(0).getGameName());
    }

    @Test
    void listGamesWithoutLogin() {
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.listGames());
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void logoutThenListFails() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.logout();
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.logout());
        Assertions.assertEquals(ResponseException.Code.Unauthorized, ex.code());
    }

    @Test
    void joinGameSuccess() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("fun");
        int gameID = facade.listGames().games().get(0).getGameID();
        String result = facade.joinGame(gameID, "WHITE");
        Assertions.assertTrue(result.contains("WHITE"));
        Assertions.assertNotNull(facade.getBoard());
    }

    @Test
    void joinBadColor() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("fun");
        int gameID = facade.listGames().games().get(0).getGameID();
        ResponseException ex = Assertions.assertThrows(ResponseException.class,
                () -> facade.joinGame(gameID, "BLUE"));
        Assertions.assertEquals(ResponseException.Code.BadRequest, ex.code());
    }

    @Test
    void getBoardBeforeJoining() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        Assertions.assertNull(facade.getBoard());
    }

    @Test
    void leaveGameClearsBoard() throws Exception {
        facade.register("player1", "password", "p1@email.com");
        facade.createGame("fun");
        int gameID = facade.listGames().games().get(0).getGameID();
        facade.setUserInGame(gameID);
        Assertions.assertNotNull(facade.getBoard());
        facade.leaveGame();
        Assertions.assertNull(facade.getBoard());
    }


}
