package model;

public record LoginRequest(String username, String password) {


    @Override
    public String username() {
        if (this.username == null) {
            return "";
        }
        return username;
    }

    @Override
    public String password() {
        if (this.password == null) {
            return "";
        }
        return password;
    }

    @Override
    public String toString() {
        return "model.LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

