package service;

import org.junit.jupiter.api.*;
import passoff.server.TestServerFacade;
import server.Server;
import dataaccess.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class JonahUserServiceTests {

    private static TestServerFacade serverFacade;
    private static Server server;
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


        //one user already logged in
    }

    @Test
    @Order(1)
    @DisplayName("Can create user")
    public void canCreateUser() {
        RegisterRequest request1 = new RegisterRequest("User", "password", "email1@mail.com");

        Assertions.assertDoesNotThrow(() -> userService.createUser(request1),
                "createUser should execute successfully for a new user without throwing exceptions.");


        Assertions.assertEquals(1,userService.getLenUsers());


    }

    @Test
    @Order(1)
    @DisplayName("Cannot create user if missing the Username, Password, or Email.")
    public void canCreateUser_MissingPassEmailUserName() {
        RegisterRequest request1 = new RegisterRequest("", "password", "email1@mail.com");
        Assertions.assertThrows(BadRequestException.class,() -> userService.createUser(request1));

        RegisterRequest request2 = new RegisterRequest("User", "", "email1@mail.com");
        Assertions.assertThrows(BadRequestException.class,() -> userService.createUser(request2));

        RegisterRequest request3 = new RegisterRequest("User", "password", "");
        Assertions.assertThrows(BadRequestException.class,() -> userService.createUser(request3));


    }

    @Test
    @Order(2)
    @DisplayName("Duplicate User Request")
    public void registerDuplicateUser() {
        RegisterRequest request1 = new RegisterRequest("DuplicateUser", "password", "email1@mail.com");


        userService.createUser(request1);
        RegisterRequest request2 = new RegisterRequest("DuplicateUser", "differentPassword", "email2@mail.com");

        Assertions.assertThrows(AlreadyTakenException.class, () -> userService.createUser(request2));
    }

    @Test
    @Order(3)
    @DisplayName("Clear works")
    public void testIfClearWorks() {
        RegisterRequest request = new RegisterRequest("Username", "password", "email1@mail.com");
        userService.createUser(request);

        userService.clear();


        Assertions.assertEquals(0, userService.getLenUsers());

    }


    @Test
    @Order(4)
    @DisplayName("count users")
    public void testGetLenUsers() {
        Assertions.assertEquals(0, userService.getLenUsers());

        RegisterRequest request1 = new RegisterRequest("Username1", "password", "email1@mail.com");
        RegisterRequest request2 = new RegisterRequest("Username2", "password", "email1@mail.com");

        userService.createUser(request1);
        userService.createUser(request2);
        Assertions.assertEquals(2, userService.getLenUsers());

        userService.clear();

        Assertions.assertEquals(0, userService.getLenUsers());

    }

    @Test
    @Order(5)
    @DisplayName("Verify user")
    public void VerifyUser() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email1@mail.com");

        userService.createUser(registerRequest);

        LoginRequest request = new LoginRequest("username", "password");

        Assertions.assertDoesNotThrow(() -> userService.verifyUser(request),
                "verifyUser should execute successfully for a valid user without throwing exceptions.");

    }

    @Test
    @Order(6)
    @DisplayName("Verify user: Doesnt work if the username is empty.")
    public void VerifyUser_EmptyUser() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email1@mail.com");

        userService.createUser(registerRequest);
        LoginRequest request = new LoginRequest("", "password");

        Assertions.assertThrows(BadRequestException.class,() -> userService.verifyUser(request), "username is empty.");

    }

    @Test
    @Order(7)
    @DisplayName("Verify user: Doesnt work if the Password is empty.")
    public void VerifyUser_EmptyPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email1@mail.com");

        userService.createUser(registerRequest);
        LoginRequest request = new LoginRequest("username", "");

        Assertions.assertThrows(BadRequestException.class,() -> userService.verifyUser(request), "password is empty.");

    }

    @Test
    @Order(8)
    @DisplayName("Verify user: Doesnt work if the Password is wrong.")
    public void VerifyUser_WrongPass() {
        RegisterRequest registerRequest = new RegisterRequest("username", "password", "email1@mail.com");

        userService.createUser(registerRequest);
        LoginRequest request = new LoginRequest("username", "passwordWRONG");

        Assertions.assertThrows(UnauthorizedException.class,() -> userService.verifyUser(request), "Password is wrong.");

    }

    @Test
    @Order(9)
    @DisplayName("Verify user: Doesnt work if user doesn't exist.")
    public void VerifyUser_BadUser() {
        LoginRequest request = new LoginRequest("username", "password");

        Assertions.assertThrows(UnauthorizedException.class,() -> userService.verifyUser(request));

    }
}