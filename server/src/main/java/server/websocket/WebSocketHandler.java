package server.websocket;

import chess.ChessGame;
import chess.ChessMove;
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
import org.jetbrains.annotations.NotNull;
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
            String message = getMessage(gameData, username);

            connections.add(session, gameData.getGameID(), username);
            var notification = ServerMessage.notification(message);
            connections.send(session, ServerMessage.loadGame(gameData.getGame()));
            connections.broadcast(session, notification);
        } catch (Exception e) {
            connections.send(session, ServerMessage.error("ERROR: something is wrong. You cant enter the websocket."));
        }
    }

    @NotNull
    private static String getMessage(GameData gameData, String username) {
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
        return message;
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
//            connections.send(session, ServerMessage.notification("You left the game!"));
            connections.remove(session);
        } catch (Exception e) {
            connections.send(session, ServerMessage.error("ERROR: something is wrong. You cant leave the game."));
        }
    }


    private void resign(UserGameCommand action, Session session) throws IOException {
        try {
            String username = authService.getUserUsername(action.getAuthToken());
            int gameID = action.getGameID();
            gameService.resign(gameID, username);
            var message = String.format("%s resigned the game", username);
            var notification = ServerMessage.notification(message);
            connections.send(session, ServerMessage.notification("You resigned the game"));
            connections.broadcast(session, notification);
        } catch (Exception e) {
            connections.send(session, ServerMessage.error(e.getMessage()));
        }
    }

    private void makeMove(UserGameCommand action, Session session) throws IOException {
        try {
            String username = authService.getUserUsername(action.getAuthToken());
            int gameID = action.getGameID();
            ChessMove move = action.getMove();
            ChessGame game = gameService.makeMove(gameID, username, move);
            var loadedGame = ServerMessage.loadGame(game);
            connections.send(session, loadedGame);
            connections.broadcast(session, loadedGame);
            var message = String.format("%s played the move %s", username, move);
            var notification = ServerMessage.notification(message);
            connections.broadcast(session, notification);

            ChessGame.TeamColor oponentColor = game.getTeamTurn();
            if (game.isInCheck(oponentColor)) {
                var state = ServerMessage.notification(oponentColor + " Is in check!");
                connections.broadcast(session, state);
                connections.send(session, state);
            }
            if (game.isInCheckmate(oponentColor)) {
                var state = ServerMessage.notification(oponentColor + " Is in checkmate! The Game is over!");
                gameService.setGameOver(gameID);
                connections.broadcast(session, state);
                connections.send(session, state);
            }
            if (game.isInStalemate(oponentColor)) {
                var state = ServerMessage.notification(oponentColor + " Is in stalemate! The Game is over!");
                gameService.setGameOver(gameID);
                connections.broadcast(session, state);
                connections.send(session, state);
            }
        } catch (Exception e) {
            connections.send(session, ServerMessage.error(e.getMessage()));
        }
    }


}