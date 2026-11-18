package client;

import java.util.*;

import chess.*;
import client.websocket.NotificationHandler;
import exception.ResponseException;
import server.ServerFacade;
import model.*;
import websocket.messages.ServerMessage;

public class ChessClient implements NotificationHandler {
    private State state = State.SIGNEDOUT;
    private final ServerFacade server;
    private String playerColor = "";
    private final ArrayList<Integer> listGames = new ArrayList<>();
    private ChessGame game;
    boolean confirmResign = false;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl, this);
    }

    public void run() {
        System.out.println(EscapeSequences.BLACK_BISHOP + " Welcome to this chess app! Sign in to start.");
        System.out.print(help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printPrompt();
            String line = scanner.nextLine();

            try {
                result = eval(line);
                if (result == null) {
                    result = "";
                }
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result + EscapeSequences.SET_TEXT_COLOR_BLACK);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print("Something went wrong. Type help to see the commands.");
            }
        }
        System.out.println();
    }


    private void printPrompt() {
        String yourState;
        if (state == State.INGAME) {
            yourState = "[IN GAME]";
        } else if (state == State.SIGNEDIN) {
            yourState = "[SIGNED IN]";
        } else {
            yourState = "[CURRENTLY SIGNED OUT]";
        }
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + EscapeSequences.RESET_BG_COLOR + yourState
                + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }


    public String eval(String input) {
        try {
            String[] tokens = input.split(" ");
            String upperCmd = (tokens.length > 0) ? tokens[0] : "help";
            String cmd = upperCmd.toLowerCase();
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if (!cmd.equals("resign")) {
                confirmResign = false;
            }
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "login" -> signIn(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            } else if (state == State.SIGNEDIN) {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout();
                    case "quit" -> "quit";
                    default -> help();
                };
            } else { // the state is INGAME.
                return switch (cmd) {
                    case "move" -> move(params);
                    case "highlight" -> highlight(params);
                    case "leave" -> leave();
                    case "resign" -> resign();
                    case "redraw" -> redraw();
                    case "quit" -> "quit";
                    default -> help();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String resign() throws ResponseException {
        if (!confirmResign) {
            confirmResign = true;
            return "Type resign again to forfeit, or any other command to cancel.";
        }
        confirmResign = false;
        server.resign();
        return "You resigned. The game is over.";

    }

    private String highlight(String[] params) {
        try {
            if (params.length != 1) {
                return "Please input the square you want to inquire about";
            }
            if (game == null) {
                return "The board has not loaded yet. Try again soon.";
            }
            if (params[0].length() != 2) {
                return "please use a valid square notation (ex: e2, a8, d5, etc.)";
            } else {
                if (Integer.parseInt(String.valueOf(params[0].charAt(1))) <= 0 || Integer.parseInt(String.valueOf(params[0].charAt(1))) >= 9) {
                    return "Please stay in the bounds of the board.";
                }
            }
            ChessPosition start = new ChessPosition(params[0]); // "e2" -> row 2, col 5
            ChessPiece piece = game.getBoard().getPiece(start);
            if (piece == null) {
                return "There is no piece on " + params[0];
            }
            Collection<ChessMove> moves = game.validMoves(start);
            // One square can appear several times when a pawn promotes
            // (queen, rook, bishop, knight). A set keeps each square once.
            Set<ChessPosition> ends = new HashSet<>();
            for (ChessMove move : moves) {
                ends.add(move.getEndPosition());
            }
            String color = playerColor.isEmpty() ? "WHITE" : playerColor;
            return game.getBoard().show(color, start, ends);
        } catch (Exception e) {
            return "Please format the request correctly. Use the help menu";
        }
    }

    public String createGame(String... params) throws ResponseException {
        if (params.length == 1) {
            try {
                assertSignedIn();
                return server.createGame(params[0]);
            } catch (ResponseException e) {
                if (e.code() == ResponseException.Code.Unauthorized) {
                    return "You are not logged in!";
                }
                if (e.code() == ResponseException.Code.ServerError) {
                    return "Something went wrong... Maybe the Server isn't up?";
                }
                if (e.code() == ResponseException.Code.BadRequest) {
                    return "That was a bad request. Use the help menu.";
                }
                return "Cannot create game. We are not sure why.";
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: create <gameName>");

    }

    public String listGames() throws ResponseException {
        try {
            assertSignedIn();
            ListGamesResult allGames = server.listGames();
            ArrayList<GameData> games = allGames.games();
            StringBuilder toScreen = new StringBuilder();
            toScreen.append("Here are all of the games being played right now:\n");
            int count = 1;
            listGames.clear();
            for (GameData game : games) {
                String nextLine = getNextLine(game, count);
                listGames.add(game.getGameID());
                toScreen.append(nextLine);
                count += 1;
            }
            if (count == 1) {
                return "There are no games so far! Feel free to make one!";
            }

            return toScreen.toString();
        } catch (ResponseException e) {
            if (e.code() == ResponseException.Code.Unauthorized) {
                return "You are not logged in!";
            }
            if (e.code() == ResponseException.Code.ServerError) {
                return "Something went wrong... Maybe the Server isn't up?";
            }
            if (e.code() == ResponseException.Code.BadRequest) {
                return "That was a bad request. Use the help menu.";
            }
            return "Cannot list the games. We are not sure why.";
        }
    }

    private static String getNextLine(GameData game, int count) {
        String name = game.getGameName();
        String blackUser;
        String whiteUser;
        if (game.getBlackUsername() == null) {
            blackUser = "NOT TAKEN";
        } else {
            blackUser = game.getBlackUsername();
        }
        if (game.getWhiteUsername() == null) {
            whiteUser = "NOT TAKEN";
        } else {
            whiteUser = game.getWhiteUsername();
        }
        String nextLine = "   - " + count + ": " + name + "   White Player: "
                + whiteUser + "   Black Player: " + blackUser + "\n";
        return nextLine;
    }

    private String redraw() {
        if (game == null) {
            return "The board has not loaded yet.";
        }
        String color = playerColor.isEmpty() ? "WHITE" : playerColor;
        return game.getBoard().show(color, null, null);
    }


    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                assertSignedIn();
                int gameToJoin = listGames.get((Integer.parseInt(params[0])) - 1);
                var output = server.joinGame(gameToJoin, params[1].toUpperCase(), params[0]);
                state = State.INGAME;
                playerColor = params[1].toUpperCase();
                var board = server.getBoard();
                return output; // + board.show(playerColor, null, null);
            } catch (ResponseException e) {
                if (e.code() == ResponseException.Code.Unauthorized) {
                    return "You are not logged in! Log in to join games";
                }
                if (e.code() == ResponseException.Code.ServerError) {
                    return "Something went wrong... Maybe the Server isn't up?";
                }
                if (e.code() == ResponseException.Code.AlreadyTaken) {
                    return "That color is already taken!";
                }
                if (e.code() == ResponseException.Code.BadRequest) {
                    return "That was a bad request. Use the help menu.";
                }
                return "Cannot join game. We are not sure why.";
            } catch (NumberFormatException e) {
                return "Please enter the game ID you want to join!";
            } catch (IndexOutOfBoundsException e) {
                return "Please choose a game listed when you run list";
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> [WHITE/BLACK]");
    }


    public String observeGame(String... params) throws ResponseException {
        try {
            if (params.length == 1) {
                assertSignedIn();
                int gameToJoin = listGames.get((Integer.parseInt(params[0])) - 1);
                server.setUserInGame(gameToJoin);
                server.connect();
                state = State.INGAME;
                var board = server.getBoard();
                return board.show("WHITE", null, null);
            } else {
                return "Please use this format: \n" +
                        "\tobserve <game number> " +
                        "\tEx: observe 3 ";

            }
        } catch (ResponseException e) {
            if (e.code() == ResponseException.Code.Unauthorized) {
                return "You are not logged in! Please log in to observe games.";
            }
            if (e.code() == ResponseException.Code.ServerError) {
                return "Something went wrong. Maybe the Server isn't up.";
            }
            if (e.code() == ResponseException.Code.BadRequest) {
                return "That was a bad request. Use the help menu. You should use observe with the integer game ID";
            }
            return "Cannot observe game. We are not sure why.";
        } catch (IndexOutOfBoundsException e) {
            return "Please choose a game listed when you run list";
        } catch (NumberFormatException e) {
            return "Please format the request correctly. Use the help menu.";
        }

    }

    public String logout() throws ResponseException {
        try {
            assertSignedIn();
            server.logout();
            state = State.SIGNEDOUT;
            return "You are signed out. Come again soon!";
        } catch (ResponseException e) {
            if (e.code() == ResponseException.Code.Unauthorized) {
                return "You are not logged in! So you can't log out!";
            }
            if (e.code() == ResponseException.Code.ServerError) {
                return "Something went wrong... Maybe the Server isn't up?";
            }
            if (e.code() == ResponseException.Code.BadRequest) {
                return "That was a bad request. Use the help menu.";
            }
            return "Cannot log you out. We are not sure why.";
        }

    }

    public String move(String... params) throws ResponseException {
        try {
            assertSignedIn();
            if (params.length == 2) {
                server.move(params[0], params[1], null);

            } else if (params.length == 3) {
                server.move(params[0], params[1], params[2]);
            } else {
                return "Please use this format: \n" +
                        "\tmove <square1> <square2> <promotionPiece> " +
                        "\tEx: move e7 e8 queen (promoting a pawn to a queen)";

            }
        } catch (InvalidMoveException e) {
            return e.getMessage();
        } catch (Exception e) {
            return "Please format the request correctly. Use the help menu";
        }
        return "";
    }

    public String leave() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDIN;
        playerColor = "";
        server.leaveGame();
        return "You left the game.";
    }


    public String signIn(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                String username = server.signIn(params[0], params[1]);
                if (username == null) {
                    return "You entered the wrong Username or password";
                }
                state = State.SIGNEDIN;
                return String.format("You signed in as %s.", username);
            } catch (ResponseException e) {
                if (e.code() == ResponseException.Code.Unauthorized) {
                    return "You entered the wrong Username or password";
                }
                if (e.code() == ResponseException.Code.ServerError) {
                    return "Something went wrong... Maybe the Server isn't up?";
                }
                if (e.code() == ResponseException.Code.BadRequest) {
                    return "That was a bad request. Use the help menu.";
                }
                return "Cannot log you in. We are not sure why.";
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <userName> <password>");
    }

    public String register(String... params) throws ResponseException {
        if (params.length == 3) {
            try {
                String username = server.register(params[0], params[1], params[2]);
                if (username == null) {
                    return "That Username is already taken.";
                }
                state = State.SIGNEDIN;
                return String.format("You signed in as %s.", username);
            } catch (ResponseException e) {
                if (e.code() == ResponseException.Code.AlreadyTaken) {
                    return "That username is already taken!";
                }
                if (e.code() == ResponseException.Code.ServerError) {
                    return "Something went wrong... Maybe the Server isn't up?";
                }
                if (e.code() == ResponseException.Code.BadRequest) {
                    return "That was a bad request. Use the help menu.";
                }
                return "Cannot register, we are not sure why.";
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <userName> <password> <email>");
    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - login <Username> <Password>
                    - register <Username> <Password> <Email>
                    - help
                    - quit
                    """;
        }
        if (state == State.SIGNEDIN) {
            return """
                    - create <GameName>
                    - list
                    - join <number beside Game> <Color you will join as>
                    - observe <number beside Game>
                    - logout
                    - help
                    - quit
                    """;
        }
        if (state == State.INGAME) {
            return """
                    - move <square1> <square2> <promotionPiece> Ex: move e7 e8 queen (promoting a pawn to a queen)
                    - leave
                    - redraw
                    - resign
                    - highlight <square>
                    - help
                    - quit
                    """;
        }
        return """
                dawg something is wrong. you should check it out!
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }


    @Override
    public void notify(ServerMessage notification) {
        switch (notification.getServerMessageType()) {
            case LOAD_GAME -> {
                game = notification.getGame();
                String color = "";
                if (playerColor.isEmpty()) {
                    color = "WHITE";
                } else {
                    color = playerColor;
                }
                System.out.println(notification.getGame().getBoard().show(color, null, null));
            }
            case NOTIFICATION -> System.out.println(notification.getMessage());
            case ERROR -> System.out.println(notification.getErrorMessage());
        }
        printPrompt();

    }
}