package service;

public class LoginRequest {
    private final String username;
    private final String password;
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getUsername () {
        if (this.username == null) {
            return "";
        }
        return username;
    }

    public String getPassword () {
        if (this.password == null) {
            return "";
        }
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

