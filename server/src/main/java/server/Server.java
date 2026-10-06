package server;
import dataaccess.*;
import model.GameData;
import service.*;
import io.javalin.*;
import com.google.gson.Gson;


import io.javalin.http.Context;

import java.util.ArrayList;

/**
 * This is our Server class. It implements all the endpoints.
 */
public class Server {
    // Set up all the services we will use for this Server, Gson, and javalin.
    private final Javalin javalin;
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    Gson serializer = new Gson();

    /**
     * This is the constructor. It is what creates all the endpoints.
     */
    public Server() {
        // Instantiate all the services.
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();

        //Create the "listeners" for the endpoints.
        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearDb)
                .post("/user",this::register)
                .post("/session",this::logIn)
                .delete("/session",this::logOut)
                .get("/game",this::getGames)
                .post("/game",this::createGame)
                .put("/game",this::joinGame);

        // Below is where we set up what status and result to add to context with each specific type of exception.
        javalin.exception(BadRequestException.class, (e, ctx) -> {
            ctx.status(400);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        });
        javalin.exception(UnauthorizedException.class, (e, ctx) -> {
            ctx.status(401);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        });
        javalin.exception(AlreadyTakenException.class, (e, ctx) -> {
            ctx.status(403);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        });
        javalin.exception(Exception.class, (e, ctx) -> {
            ctx.status(500);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        });
    }

    /**
     * This is the endpoint implementation for joining a game.
     * @param ctx - Context object passed in.
     */
    private void joinGame (Context ctx) {
        // Make sure you are logged in.
        String authorization = ctx.header("authorization");
        this.authService.verifyLoggedIn(authorization);

        //Get the username and build an appropriate request.
        String username = this.authService.getUserUsername(authorization);
        SetColorGameRequest request = serializer.fromJson(ctx.body(), SetColorGameRequest.class);
        request.setUsername(username);

        //Make the request to set the color (and join a game).
        this.gameService.setColorForGame(request);
        ctx.status(200);
        ctx.result("{}");

    }

    /**
     * This is the endpoint implementation for creating a game.
     * @param ctx - Context object passed in.
     */
    private void createGame (Context ctx) {
        // Make sure you are logged in.
        this.authService.verifyLoggedIn(ctx.header("authorization"));

        //Create the CreateGameRequest. This is what the service will use to create the game with the appropriate name
        CreateGameRequest request = serializer.fromJson(ctx.body(), CreateGameRequest.class);

        //Make the request to create the game.
        int GameId = this.gameService.createGame(request);
        ctx.status(200);
        ctx.result(serializer.toJson(new GameResponse(GameId)));
    }

    /**
     * This is the implementation of the endpoint getGame.
     * @param ctx - Context object passed in.
     */
    private void getGames (Context ctx) {
        // Make sure you are logged in.
        this.authService.verifyLoggedIn(ctx.header("authorization"));

        // Ask the service to list all the games.
        ArrayList<GameData> games = this.gameService.listGames();
        ctx.status(200);

        // Set those games as the result of the Context object.
        ListGamesResult gamesResult = new ListGamesResult(games);
        ctx.result(serializer.toJson(gamesResult));

    }

    /**
     * This is the implementation of the endpoint of logging out.
     * @param ctx - Context object that is passed in.
     */
    private void logOut (Context ctx) {
        //Tell the authService to log out the user.
        this.authService.logOut(ctx.header("authorization"));
        ctx.status(200);
        ctx.result("{}");

    }

    /**
     * This is the implementation of the endpoint logging in.
     * @param ctx - Context object that is passed in.
     */
    private void logIn(Context ctx) {
        // Create the request object.
        LoginRequest request = serializer.fromJson(ctx.body (), LoginRequest.class);

        // Make sure the user exists.
        this.userService.verifyUser(request);

        //Log in and create the authToken.
        LoginResult auth = this.authService.createAuth(request.username());
        ctx.status(200);
        ctx.result(serializer.toJson(auth));

    }

    /**
     * This is the implementation of the register endpoint.
     * @param ctx - Context object that is passed in.
     */
    private void register (Context ctx) {
        // Create the appropriate request object and create the user.
        RegisterRequest request = serializer.fromJson(ctx.body(),RegisterRequest.class);
        this.userService.createUser(request);

        // Log the person in.
        LoginResult auth = this.authService.createAuth(request.username());
        ctx.status(200);
        ctx.result(serializer.toJson(auth));
    }


    /**
     * this is the implementation of the endpoint that clears the db. This is only used for testing.
     * @param ctx - Context object that is passed in.
     */
    private void clearDb(Context ctx) {
        // go through each of the services, and have them clear their db.
        this.authService.clear();
        this.gameService.clear();
        this.userService.clear();
        ctx.status(200);
        ctx.result("{}");

    }

    /**
     * This is what starts the javalin.
     * @param desiredPort - this is the port that will be used. normally it is 8080.
     * @return - returns the port that is started.
     */
    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    /**
     * Stops running the server.
     */
    public void stop() {
        javalin.stop();
    }
}
