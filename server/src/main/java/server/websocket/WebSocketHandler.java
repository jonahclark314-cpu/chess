package server.websocket;

import com.google.gson.Gson;
import exception.ResponseException;
import io.javalin.websocket.WsCloseContext;
import io.javalin.websocket.WsCloseHandler;
import io.javalin.websocket.WsConnectContext;
import io.javalin.websocket.WsConnectHandler;
import io.javalin.websocket.WsMessageContext;
import io.javalin.websocket.WsMessageHandler;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;

public class WebSocketHandler implements WsConnectHandler, WsMessageHandler, WsCloseHandler {

    private final ConnectionManager connections = new ConnectionManager();

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
                case UserGameCommand.CommandType.CONNECT -> enter(action.getAuthToken(), ctx.session);
                case UserGameCommand.CommandType.LEAVE -> leave(action.getAuthToken(), ctx.session);
                case UserGameCommand.CommandType.RESIGN -> resign(action.getAuthToken(), ctx.session);
                case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(action.getAuthToken(), ctx.session);

            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public void handleClose(WsCloseContext ctx) {
        System.out.println("Websocket closed");
    }

    private void enter(String authToken, Session session) throws IOException {
        connections.add(session);
        var message = String.format("%s entered the game", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(session, notification);
    }

    private void leave(String authToken, Session session) throws IOException {
        var message = String.format("%s left the game", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    private void resign(String authToken, Session session) throws IOException {
        var message = String.format("%s resigned the game", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(session, notification);
        connections.remove(session);
    }

    private void makeMove(String authToken, Session session) throws IOException {
        var message = String.format("%s moved a piece", authToken);
        var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, message);
        connections.broadcast(session, notification);
        var notification2 = new ServerMessage(ServerMessage.ServerMessageType.NOTIFICATION, message);
        connections.broadcast(session, notification2);
        connections.remove(session);
    }


}