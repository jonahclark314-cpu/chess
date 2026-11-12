package server.websocket;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import server.Server;
import service.AuthService;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();
    private final AuthService authService;
    private final GameService gameService;


    public WebSocketHandler(AuthService authService, GameService gameService) {
        this.authService = authService;
        this.gameService = gameService;

    }

    @Override
    public void handleConnect(WsConnectContext ctx) {
        System.out.println("Websocket connected");
        ctx.enableAutomaticPings();
    }

    @Override
    public void handleMessage(WsMessageContext ctx) {
        try {
            UserGameCommand action = new Gson().fromJson(ctx.message(), UserGameCommand.class);
            switch (action.GetCommandType()) {
                case UserGameCommand.CommandType.CONNECT -> enter(action, ctx.session);
                case UserGameCommand.CommandType.LEAVE -> leave(action, ctx.session);
                case UserGameCommand.CommandType.RESIGN -> resign(action, ctx.session);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(action, ctx.session);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void enter(UserGameCommand action, Session session) throws IOException {
        try {
            String username = authService.getUserUsername(action.getAuthToken());
            GameData gameData = gameService.getGame(action.getGameID());
            String whiteUser = gameData.getWhiteUsername();
            String blackUser = gameData.getBlackUsername();
            String message;
            if (username.equals(whiteUser)) {
                message = String.format("%s entered the game as White!", username);
            } else if (username.equals(blackUser)) {
                message = String.format("%s entered the game as Black!", username);
            } else {
                message = String.format("%s entered the game as an Observer!", username);
            }

            connections.add(session, gameData.getGameID(), username);
            var notification = ServerMessage.notification(message);
            connections.send(session, ServerMessage.loadGame(gameData.getGame()));
            connections.broadcast(session, notification);
        } catch (Exception e) {
            connections.send(session, ServerMessage.error("Something is wrong. You cant enter the websocket."));
        }
    }

    private void leave(UserGameCommand action, Session session) throws IOException {
        try {

            String authToken = action.getAuthToken();
            String username = authService.getUserUsername(authToken);

            GameData ourGame = gameService.getGame(action.getGameID());
            var message = String.format("%s left the game", username);
            var notification = ServerMessage.notification(message);
            gameService.leaveGame(action.getGameID(), username);
            connections.broadcast(session, notification);
            connections.send(session, ServerMessage.notification("You left the game!"));
            connections.remove(session);
        } catch (Exception e) {
            connections.send(session, ServerMessage.error("Something is wrong. You cant leave the game."));
        }
    }


    private void resign(UserGameCommand action, Session session) throws IOException {
        String username = authService.getUserUsername(action.getAuthToken());
        var message = String.format("%s resigned the game", username);
        var notification = ServerMessage.notification(message);
        connections.broadcast(session, notification);
    }

    private void makeMove(UserGameCommand action, Session session) throws IOException {
        String username = authService.getUserUsername(action.getAuthToken());
        var message = String.format("%s moved a piece", username);
        var notification = ServerMessage.notification(message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }


}