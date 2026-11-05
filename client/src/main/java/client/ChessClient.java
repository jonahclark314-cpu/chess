package client;

import java.util.Arrays;
import java.util.Scanner;

import exception.ResponseException;
import server.ServerFacade;
import com.google.gson.Gson;
import model.*;
import ui.EscapeSequences;

public class ChessClient {
    private String visitorName = null;
    private State state = State.SIGNEDOUT;
    private final ServerFacade server;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
    }

    public void run() {
        System.out.println(EscapeSequences.BLACK_BISHOP + " Welcome to the pet store. Sign in to start.");
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
        System.out.print("\n" + EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
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
                    case "quit" -> "quit";
                    default -> help();
                };
            }

        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    public String createGame(String... params) throws ResponseException {
        return "createGame()";
    }

    public String listGames(String... params) throws ResponseException {
        return "listGames()";
    }

    public String joinGame(String... params) throws ResponseException {
        return "joinGame()";
    }

    public String observeGame(String... params) throws ResponseException {
        return "observeGame()";
    }

    public String logout(String... params) throws ResponseException {
        return "logout()";
    }

    public String move(String... params) throws ResponseException {
        return "move()";
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

//
//    public String listPets() throws ResponseException {
//        assertSignedIn();
//        PetList pets = server.listPets();
//        var result = new StringBuilder();
//        var gson = new Gson();
//        for (Pet pet : pets) {
//            result.append(gson.toJson(pet)).append('\n');
//        }
//        return result.toString();
//    }

//    public String adoptPet(String... params) throws ResponseException {
//        assertSignedIn();
//        if (params.length == 1) {
//            try {
//                int id = Integer.parseInt(params[0]);
//                Pet pet = getPet(id);
//                if (pet != null) {
//                    server.deletePet(id);
//                    return String.format("%s says %s", pet.name(), pet.sound());
//                }
//            } catch (NumberFormatException ignored) {
//            }
//        }
//        throw new ResponseException(ResponseException.Code.ClientError, "Expected: <pet id>");
//    }

//    public String adoptAllPets() throws ResponseException {
//        assertSignedIn();
//        var buffer = new StringBuilder();
//        for (Pet pet : server.listPets()) {
//            buffer.append(String.format("%s says %s%n", pet.name(), pet.sound()));
//        }
//
//        server.deleteAllPets();
//        return buffer.toString();
//    }

//    public String signOut() throws ResponseException {
//        assertSignedIn();
//        server.leavePetShop(visitorName);
//        state = State.SIGNEDOUT;
//        return String.format("%s left the shop", visitorName);
//    }

//    private Pet getPet(int id) throws ResponseException {
//        for (Pet pet : server.listPets()) {
//            if (pet.id() == id) {
//                return pet;
//            }
//        }
//        return null;
//    }

    public String help() {
        if (state == State.SIGNEDOUT) {
            return """
                    - signIn <yourname>
                    - quit
                    """;
        }
        return """
                - list
                - adopt <pet id>
                - rescue <name> <CAT|DOG|FROG|FISH>
                - adoptAll
                - signOut
                - quit
                """;
    }

    private void assertSignedIn() throws ResponseException {
        if (state == State.SIGNEDOUT) {
            throw new ResponseException(ResponseException.Code.ClientError, "You must sign in");
        }
    }


}