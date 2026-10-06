package service;

import org.jetbrains.annotations.NotNull;

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

    @NotNull
    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

