package model;

import java.util.UUID;

public class AuthData {
    private final String authToken;
    private final String username;

    public AuthData (String username) {
        this.username = username;
        this.authToken = generateToken();
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public String getUsername() {
        return this.username;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
