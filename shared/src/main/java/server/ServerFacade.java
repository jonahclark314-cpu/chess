package server;

import chess.*;
import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.net.*;
import java.net.http.*;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.Objects;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;
    private String authentication = "";
    private String personalUsername = "";
    private int currentGameID = 0;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public ServerFacade(int port) {
        this("http://localhost:" + port);
    }

    public String signIn(String username, String password) throws ResponseException {
        LoginRequest loginRequest = new LoginRequest(username, password);
        var request = buildRequest("POST", "/session", loginRequest);
        var response = sendRequest(request);
        AuthData authData = handleResponse(response, AuthData.class);
        assert authData != null;
        authentication = authData.authToken();
        personalUsername = authData.username();
        return authData.username();
    }

    public String register(String username, String password, String email) throws ResponseException {
        RegisterRequest registerRequest = new RegisterRequest(username, password, email);
        var request = buildRequest("POST", "/user", registerRequest);
        var response = sendRequest(request);
        AuthData authData = handleResponse(response, AuthData.class);
        assert authData != null;
        authentication = authData.authToken();
        personalUsername = authData.username();
        return authData.username();
    }


    public void logout() throws ResponseException {
        var request = buildRequest("DELETE", "/session", null);
        var response = sendRequest(request);
        handleResponse(response, Object.class);
    }

    public ListGamesResult listGames() throws ResponseException {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        return handleResponse(response, ListGamesResult.class);
    }

    public String createGame(String gameName) throws ResponseException {
        CreateGameRequest gameRequest = new CreateGameRequest(gameName);
        var request = buildRequest("POST", "/game", gameRequest);
        var response = sendRequest(request);
        handleResponse(response, Object.class);
        return "Created a game called:" + gameName;
    }

    public ChessBoard getBoard() throws ResponseException {
        var request = buildRequest("GET", "/game", null);
        var response = sendRequest(request);
        ArrayList<GameData> games = Objects.requireNonNull(handleResponse(response, ListGamesResult.class)).games();
        GameData ourGame = null;
        for (GameData game : games) {
            if (game.getGameID() == currentGameID) {
                ourGame = game;
            }
        }
        if (ourGame == null) {
            return null;
        } else {
            return ourGame.getGame().getBoard();
        }
    }

    public String joinGame(int gameID, String colorPlayer) throws ResponseException {
        SetColorGameRequest gameRequest = new SetColorGameRequest(gameID, colorPlayer, personalUsername);
        var request = buildRequest("PUT", "/game", gameRequest);
        var response = sendRequest(request);
        handleResponse(response, Object.class);
        currentGameID = gameID;
        return "Joined the game numbered:" + gameID + " as " + colorPlayer;
    }

//    public String doMove(ChessMove move) {
//        if (currentGameID == 0) {
//            return "You are just an observer in this game.";
//        }
//        var request = buildRequest("PUT", "/game", gameRequest);
//        var response = sendRequest(request);
//        handleResponse(response, Object.class);
//        return "Joined the game numbered:" + gameID + " as " + colorPlayer;
//
//    }


    public void leaveGame() {
        currentGameID = 0;
    }


//    public Pet addPet(Pet pet) throws ResponseException {
//        var request = buildRequest("POST", "/pet", pet);
//        var response = sendRequest(request);
//        return handleResponse(response, Pet.class);
//    }


//    public PetList listPets() throws ResponseException {
//        var request = buildRequest("GET", "/pet", null);
//        var response = sendRequest(request);
//        return handleResponse(response, PetList.class);
//    }

    private HttpRequest buildRequest(String method, String path, Object body) {
        var request = HttpRequest.newBuilder()
                .uri(URI.create(serverUrl + path))
                .method(method, makeRequestBody(body));
        if (!Objects.equals(authentication, "")) {
            request.setHeader("authorization", authentication);
        }
        if (body != null) {
            request.setHeader("Content-Type", "application/json");
        }
        return request.build();
    }

    private BodyPublisher makeRequestBody(Object request) {
        if (request != null) {
            return BodyPublishers.ofString(new Gson().toJson(request));
        } else {
            return BodyPublishers.noBody();
        }
    }

    private HttpResponse<String> sendRequest(HttpRequest request) throws ResponseException {
        try {
            return client.send(request, BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new ResponseException(ResponseException.Code.ServerError, ex.getMessage());
        }
    }

    private <T> T handleResponse(HttpResponse<String> response, Class<T> responseClass) throws ResponseException {
        var status = response.statusCode();
        if (!isSuccessful(status)) {
            var body = response.body();
            if (body != null) {
                throw ResponseException.fromJson(body, status);
            }

            throw new ResponseException(ResponseException.fromHttpStatusCode(status), "other failure: " + status);
        }

        if (responseClass != null) {
            return new Gson().fromJson(response.body(), responseClass);
        }

        return null;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    public void setUserInGame(int gameID) {
        currentGameID = gameID;
    }
}