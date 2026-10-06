package server;
import dataaccess.*;
import model.GameData;
import service.*;
import io.javalin.*;
import com.google.gson.Gson;


import io.javalin.http.Context;

import java.util.ArrayList;

public class Server {

    private final Javalin javalin;
    private final AuthService authService;
    private final GameService gameService;
    private final UserService userService;
    Gson serializer = new Gson();



    public Server() {
        this.authService = new AuthService();
        this.gameService = new GameService();
        this.userService = new UserService();

        javalin = Javalin.create(config -> config.staticFiles.add("web"))
                .delete("/db", this::clearDb)
                .post("/user",this::register)
                .post("/session",this::logIn)
                .delete("/session",this::logOut)
                .get("/game",this::getGames)
                .post("/game",this::createGame)
                .put("/game",this::joinGame);

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


    private void joinGame (Context ctx) {
        this.authService.verifyLoggedIn(ctx.header("authorization"));
        String username = this.authService.getUserUsername(ctx.header("authorization"));
        SetColorGameRequest request = serializer.fromJson(ctx.body(), SetColorGameRequest.class);
        request.setUsername(username);
        this.gameService.setColorForGame(request);
        ctx.status(200);
        ctx.result("{}");

    }

    private void createGame (Context ctx) {
        this.authService.verifyLoggedIn(ctx.header("authorization"));
        CreateGameRequest request = serializer.fromJson(ctx.body(), CreateGameRequest.class);
        int GameId = this.gameService.createGame(request);
        ctx.status(200);
        ctx.result(serializer.toJson(new GameResponse(GameId)));
    }




        private void getGames (Context ctx) {
            this.authService.verifyLoggedIn(ctx.header("authorization"));
            ArrayList<GameData> games = this.gameService.listGames();
            ctx.status(200);
            ListGamesResult gamesResult = new ListGamesResult(games);
            ctx.result(serializer.toJson(gamesResult));

        }


    private void logOut (Context ctx) {
        this.authService.logOut(ctx.header("authorization"));
        ctx.status(200);
        ctx.result("{}");

    }

    private void logIn(Context ctx) {
        LoginRequest request = serializer.fromJson(ctx.body (), LoginRequest.class);
        this.userService.verifyUser(request);
        LoginResult auth = this.authService.createAuth(request.getUsername());
        ctx.status(200);
        ctx.result(serializer.toJson(auth));

    }

    private void register (Context ctx) {
        RegisterRequest request = serializer.fromJson(ctx.body(),RegisterRequest.class);
        this.userService.createUser(request);
        LoginResult auth = this.authService.createAuth(request.username());
        ctx.status(200);
        ctx.result(serializer.toJson(auth));
    }



    private void clearDb(Context ctx) {
        this.authService.clear();
        this.gameService.clear();
        this.userService.clear();
        ctx.status(200);
        ctx.result("{}");

    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
