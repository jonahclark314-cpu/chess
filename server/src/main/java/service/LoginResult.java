package service;

public class LoginResult {
    private final String username;
    private final String authToken;
    public LoginResult (String username, String authToken) {
        this.username = username;
        this.authToken = authToken;
    }


    public String getUsername() {
        return this.username;
    }
    public String getAuthToken() {
        return this.authToken;
    }
}
