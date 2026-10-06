package service;

public record RegisterRequest(String username, String password, String email) {


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

}
