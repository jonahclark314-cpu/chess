package server;
import dataaccess.AlreadyTakenException;
import dataaccess.BadRequestException;
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
                .post("/user",this::register);


        // Register your endpoints and exception handlers here.

    }



    private void register (Context ctx) {

        RegisterRequest request = serializer.fromJson(ctx.body(),RegisterRequest.class);
        try {
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
        }

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
