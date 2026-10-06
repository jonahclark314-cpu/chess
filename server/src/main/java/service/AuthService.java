package service;
import dataaccess.*;

import java.util.UUID;

/**
 * This is the Auth Service class. This handles all logic for logging in clients and logging them out.
 */
public class AuthService {
    // This is the DAO that this Service is tied to:
    AuthDAO authDAO;

    // Constructor
    public AuthService () {
        this.authDAO = new MemoryAuthDAO();
    }

    /**
     * This is what tells the DAO to clear the db.
     */
    public void clear() {
        this.authDAO.clear();
    }

    /**
     * This is the way users can log in.
     * @param username - username of the client.
     * @return - returns the loginResult which contains the authToken.
     */
    public LoginResult createAuth(String username) {
        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String authToken = this.authDAO.createAuth(username,generateAuthToken());
        return new LoginResult(username,authToken);
    }

    /**
     * This is the function that talks to the DAO to delete the auth data for that person (logging them out).
     * @param authToken - Auth token that the person is using.
     * @throws UnauthorizedException - If the person is not already logged in, they cannot log out!
     */
    public void logOut(String authToken) throws UnauthorizedException {
        verifyLoggedIn(authToken);
        this.authDAO.deleteAuth(authToken);
    }

    /**
     * This is the method that makes sure that someone is logged in. It makes sure that their authToken is currently valid.
     * @param authToken - This is the string for their authentication token.
     * @throws UnauthorizedException - If someone is not logged in, they will be marked unauthorized.
     */
    public void verifyLoggedIn (String authToken) throws UnauthorizedException {
        String username = this.authDAO.getAuth(authToken);

        if (username.isEmpty()) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    /**
     * This is the method that gets the username of a user just from their authToken.
     * @param authToken - String authentication token.
     * @return - Returns the username of the person logged in with that.
     * @throws BadRequestException - If the authToken is not valid in any way it will be marked as a bad request.
     */
    public String getUserUsername (String authToken) throws BadRequestException {
        if (authToken == null) {
            throw new BadRequestException("Error: bad request");
        }
        if (authToken.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        String username = this.authDAO.getAuth(authToken);
        if (username.isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        return username;
    }

    /**
     * This is a helper function that generates the authentication token.
     * @return - randomized authentication token
     */
    private static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }



}

