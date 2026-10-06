package server;

import chess.*;

/**
 * This is the Main server class. It is what creates and starts up the Server.
 */
public class ServerMain {
    public static void main(String[] args) {
        var port = 8080;
        Server server = new Server();
        server.run(port);
    }
}
