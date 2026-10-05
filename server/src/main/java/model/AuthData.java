package model;

import java.util.UUID;

public class AuthData {
    private final String authToken;
    private final String username;

    public AuthData (String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public String getUsername() {
        return this.username;
    }
}
