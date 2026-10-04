package service;

public class LoginRequest {
    private final String username;
    private final String password;
    public LoginRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
    }


    public String getUsername () {
        return username;
    }

    public String getPassword () {
        return password;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

