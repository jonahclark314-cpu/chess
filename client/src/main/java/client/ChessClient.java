package client;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

import exception.ResponseException;
import server.ServerFacade;
import model.*;
import ui.EscapeSequences;

public class ChessClient {
    private State state = State.SIGNEDOUT;
    private final ServerFacade server;
    private String playerColor = "";

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
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
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result + EscapeSequences.SET_TEXT_COLOR_BLACK);
            } catch (Throwable e) {
                var msg = e.toString();
                System.out.print(msg);
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
            if (state == State.SIGNEDOUT) {
                return switch (cmd) {
                    case "login" -> signIn(params);
                    case "register" -> register(params);
                    default -> help();
                };
            } else if (state == State.SIGNEDIN) {
                return switch (cmd) {
                    case "create" -> createGame(params);
                    case "list" -> listGames();
                    case "join" -> joinGame(params);
                    case "observe" -> observeGame(params);
                    case "logout" -> logout();
                    default -> help();
                };
            } else { // the state is INGAME.
                return switch (cmd) {
                    case "move" -> move(params);
                    case "leave" -> leave();
                    case "quit" -> "quit";
                    default -> help();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
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
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <userName> <password>");

    }

    public String listGames() throws ResponseException {
        try {
            assertSignedIn();
            ListGamesResult allGames = server.listGames();
            ArrayList<GameData> games = allGames.games();
            StringBuilder toScreen = new StringBuilder();
            toScreen.append("Here are all of the games being played right now:\n");
            int count = 1;
            for (GameData game : games) {
                String name = game.getGameName();
                String nextLine = "   - " + game.getGameID() + ": " + name + "   White Player: "
                        + game.getWhiteUsername() + "   Black Player: " + game.getBlackUsername() + "\n";
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

    public String joinGame(String... params) throws ResponseException {
        if (params.length == 2) {
            try {
                assertSignedIn();
                var output = server.joinGame(Integer.parseInt(params[0]), params[1]);
                state = State.INGAME;
                playerColor = params[1];
                var board = server.getBoard();
                return output + board.show(playerColor);
            } catch (ResponseException e) {
                if (e.code() == ResponseException.Code.Unauthorized) {
                    return "You are not logged in!";
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
                return "Cannot create game. We are not sure why.";
            }
        }
        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <ID> [WHITE/BLACK]");
    }


    public String observeGame(String... params) throws ResponseException {
        try {
            assertSignedIn();
            server.setUserInGame(Integer.parseInt(params[0]));
            state = State.INGAME;
            var board = server.getBoard();
            return board.show("WHITE");
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
        assertSignedIn();
        return "We have not implemented moves yet. Thanks for the patience";
    }

    public String leave() throws ResponseException {
        assertSignedIn();
        state = State.SIGNEDIN;
        playerColor = "";
        server.leaveGame();
        return "leave()";
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
                    - create
                    - list
                    - join
                    - observe
                    - logout
                    - help
                    - quit
                    """;
        }
        if (state == State.INGAME) {
            try {
                var board = server.getBoard();
                if (Objects.equals(playerColor, "BLACK")) {
                    return """
                            - move
                            - leave
                            - quit
                            """ + board.show("BLACK");
                } else {
                    return """
                            - move
                            - leave
                            - quit
                            """ + board.show("WHITE");
                }

            } catch (Exception e) {
                return """
                        - move
                        - leave
                        - quit
                        """;
            }
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


}