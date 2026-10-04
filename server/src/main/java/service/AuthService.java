package service;
import dataaccess.*;
import io.javalin.http.Context;


public class AuthService {
    AuthDAO authDAO;

    public AuthService () {
        this.authDAO = new MemoryAuthDAO();
    }

    public void clear() {
        this.authDAO.clear();
    }

    public LoginResult createAuth(String username) {
        String authToken = this.authDAO.createAuth(username);
        return new LoginResult(username,authToken);
    }
}

