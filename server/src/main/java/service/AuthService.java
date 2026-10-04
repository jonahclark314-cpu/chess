package service;
import dataaccess.*;


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


    public void logOut(String authToken) throws UnauthorizedException {
        verifyLoggedIn(authToken);
        this.authDAO.deleteAuth(authToken);
    }

    public void verifyLoggedIn (String authToken) throws UnauthorizedException {
        String username = this.authDAO.getAuth(authToken);

        if (username.isEmpty()) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}

