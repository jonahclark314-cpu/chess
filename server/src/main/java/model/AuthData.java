package model;

import java.util.UUID;

public class AuthData {
    String authToken;
    String username;

    public AuthData (String username) {
        this.username = username;
        this.authToken = generateToken();
    }

    public String getAuthToken() {
        return this.authToken;
    }

    public static String generateToken() {
        return UUID.randomUUID().toString();
    }

}
