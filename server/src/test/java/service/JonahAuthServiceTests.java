package service;

import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import server.*;
import dataaccess.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JonahAuthServiceTests {

    private static TestServerFacade serverFacade;
    private static Server server;
    private static AuthService authService;
    private static UserService userService;
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
        serverFacade.clear();
        userService = new UserService();
        authService = new AuthService();

        //one user already logged in
    }

    @Test
    @Order(1)
    @DisplayName("can get username from auth token")
    public void getUsernameFromAuthToken() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        LoginResult authData = authService.createAuth(targetUsername);
        String authToken = authData.authToken();

        String username = authService.getUserUsername(authToken);
        Assertions.assertEquals(targetUsername, username);
    }

    @Test
    @Order(2)
    @DisplayName("Get Username fails if authToken is wrong")
    public void getUsernameFromWrongAuthToken() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        authService.createAuth(targetUsername);

        Assertions.assertThrows(BadRequestException.class, () -> authService.getUserUsername("authToken"));
    }


    @Test
    @Order(3)
    @DisplayName("Get Username fails if authToken is empty")
    public void getUsernameFromEmptyAuthToken() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        authService.createAuth(targetUsername);

        Assertions.assertThrows(BadRequestException.class, () -> authService.getUserUsername(""));
    }


    @Test
    @Order(4)
    @DisplayName("Verify Logged In")
    public void verifyLoggedIn() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        String authToken = authService.createAuth(targetUsername).authToken();

        Assertions.assertDoesNotThrow(() -> authService.verifyLoggedIn(authToken), "verifyLoggedIn should execute successfully for a valid user without throwing exceptions.");
    }

    @Test
    @Order(5)
    @DisplayName("Verify Logged In: Error if no authToken")
    public void verifyLoggedIn_NoAuthToken() {
        Assertions.assertThrows(UnauthorizedException.class, () -> authService.verifyLoggedIn(""));

        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

//        authService.createAuth(targetUsername).getAuthToken();
        Assertions.assertThrows(UnauthorizedException.class, () -> authService.verifyLoggedIn("notTheRightAuthToken"));

    }

    @Test
    @Order(6)
    @DisplayName("Verify Logged Out")
    public void verifyLoggedOut() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        String authToken = authService.createAuth(targetUsername).authToken();

        Assertions.assertDoesNotThrow(() -> authService.verifyLoggedIn(authToken), "verifyLoggedIn should execute successfully for a valid user without throwing exceptions.");

        Assertions.assertDoesNotThrow(() -> authService.logOut(authToken), "verifyLoggedIn should execute successfully for a valid user without throwing exceptions.");


    }

    @Test
    @Order(7)
    @DisplayName("Verify Logged Out: Error if not logged in")
    public void verifyLoggedOut_Error() {
        String targetUsername = "jonahClark";


        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        String authToken = authService.createAuth(targetUsername).authToken();

        Assertions.assertDoesNotThrow(() -> authService.verifyLoggedIn(authToken), "verifyLoggedIn should execute successfully for a valid user without throwing exceptions.");

        Assertions.assertDoesNotThrow(() -> authService.logOut(authToken), "verifyLoggedIn should execute successfully for a valid user without throwing exceptions.");

        Assertions.assertThrows(UnauthorizedException.class, () -> authService.verifyLoggedIn(authToken));

    }

    @Test
    @Order(8)
    @DisplayName("can Create Auth from a username")
    public void verifyCreateAuth() {
        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        Assertions.assertInstanceOf(LoginResult.class,authService.createAuth(targetUsername));

    }

    @Test
    @Order(9)
    @DisplayName("Error Create Auth empty username")
    public void verifyCreateAuth_Error() {

        String targetUsername = "jonahClark";

        RegisterRequest registerRequest = new RegisterRequest(targetUsername, "password", "email1@mail.com");
        userService.createUser(registerRequest);

        Assertions.assertThrows(BadRequestException.class, () -> authService.createAuth(""));
    }


    @Test
    @Order(9)
    @DisplayName("Error Create Auth empty username: 2")
    public void verifyCreateAuth_Error2() {
        RegisterRequest registerRequest = new RegisterRequest(null, "password", "email1@mail.com");

        Assertions.assertEquals("", registerRequest.username());
    }


}