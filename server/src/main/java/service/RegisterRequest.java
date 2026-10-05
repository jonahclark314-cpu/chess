package service;

public class RegisterRequest {
    private final String username;
    private final String password;
    private final String email;
    public RegisterRequest(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
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
    public String getEmail () {
        return email;
    }

}
