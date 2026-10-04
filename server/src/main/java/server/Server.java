package server;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
import dataaccess.UnauthorizedException;
import service.*;
import io.javalin.*;
import com.google.gson.Gson;


import io.javalin.http.Context;

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
                .delete("/session",this::logOut);


        // Register your endpoints and exception handlers here.

    }

    private void logOut (Context ctx) {
        try {
            this.authService.logOut(ctx.header("authorization"));
            ctx.status(200);
            ctx.result("{}");
        } catch (UnauthorizedException e) {
            ctx.status(401);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        }
    }

    private void logIn(Context ctx) {
        try {
            LoginRequest request = serializer.fromJson(ctx.body (), LoginRequest.class);
            this.userService.verifyUser(request);
            LoginResult auth = this.authService.createAuth(request.getUsername());
            ctx.status(200);
            ctx.result(serializer.toJson(auth));
        } catch (UnauthorizedException e){
            ctx.status(401);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        }
    }

    private void register (Context ctx) {
        try {
            RegisterRequest request = serializer.fromJson(ctx.body(),RegisterRequest.class);
            this.userService.createUser(request);
            LoginResult auth = this.authService.createAuth(request.getUsername());
            ctx.status(200);
            ctx.result(serializer.toJson(auth));

        } catch (AlreadyTakenException e){
            ctx.status(403);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        } catch (BadRequestException e) {
            ctx.status(400);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        } catch (Exception e){
            ctx.status(500);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        }

    }



    private void clearDb(Context ctx) {
        try {
            this.authService.clear();
            this.gameService.clear();
            this.userService.clear();
            ctx.status(200);
            ctx.result("{}");
        } catch (Exception e) {
            ctx.status(500);
            ctx.result(serializer.toJson(new ErrorResult(e.getMessage())));
        }
    }


    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
