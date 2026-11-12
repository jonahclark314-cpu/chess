package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    private record Connection(Session session, int gameID, String username) {
    }

    public final ConcurrentHashMap<Session, Connection> connections = new ConcurrentHashMap<>();

    public void add(Session session, int gameID, String username) {
        connections.put(session, new Connection(session, gameID, username));
    }

    public void send(Session session, ServerMessage message) throws IOException {
        if (session.isOpen()) {
            session.getRemote().sendString(new Gson().toJson(message));
        }
    }

    public void remove(Session session) {
        connections.remove(session);
    }

    public void broadcast(Session excludeSession, ServerMessage notification) throws IOException {
        Connection originPerson = connections.get(excludeSession);
        if (originPerson == null) {
            return;
        }
        String msg = new Gson().toJson(notification);
        for (Connection c : connections.values()) {
            if (c.session.isOpen() && c.gameID == originPerson.gameID && !Objects.equals(c.username, originPerson.username)) {
                if (!c.session.equals(excludeSession)) {
                    c.session.getRemote().sendString(msg);
                }
            }
        }
    }
}