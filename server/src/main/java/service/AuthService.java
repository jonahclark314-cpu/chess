package service;
import dataaccess.*;

import java.util.UUID;


public class AuthService {
    AuthDAO authDAO;

    public AuthService () {
        this.authDAO = new MemoryAuthDAO();
    }

    public void clear() {
        this.authDAO.clear();
    }

    public LoginResult createAuth(String username) {
        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String authToken = this.authDAO.createAuth(username,generateAuthToken());
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

    public String getUserUsername (String authToken) throws BadRequestException {
        if (authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String username = this.authDAO.getAuth(authToken);
        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        return username;
    }


    private static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }



}

