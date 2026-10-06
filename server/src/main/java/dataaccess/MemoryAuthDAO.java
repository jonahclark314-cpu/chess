package dataaccess;

import java.util.ArrayList;

import model.*;

/**
 * This is the class I am using while I do not have access to the Database.
 */
public class MemoryAuthDAO implements AuthDAO{
    ArrayList<AuthData> listOfAuthData;

    /**
     * Constructor
     */
    public MemoryAuthDAO () {
        this.listOfAuthData = new ArrayList<>();
    }

    /**
     * clears all the data in this database.
     */
    @Override
    public void clear() {
        this.listOfAuthData.clear();
    }

    /**
     * This logs in a person and returns their authToken
     * @param username - the username of the user.
     * @param authToken - a pre-generated authoring.
     * @return AuthToken
     */
    @Override
    public String createAuth(String username, String authToken) {
        AuthData auth = new AuthData(username, authToken);
        this.listOfAuthData.add(auth);
        return auth.authToken();
    }

    /**
     * This is the way you can check if someone is logged in.
     * @param authToken - this is the authToken string.
     * @return returns the username if they are logged in, and empty string otherwise.
     */
    @Override
    public String getAuth(String authToken) {
        for (AuthData user : this.listOfAuthData) {
            if (user.authToken().equals(authToken)) {
                return user.username();
            }
        }
        return "";
    }

    /**
     * This is how you can log someone out.
     * @param authToken - the auth token that they are logged in through.
     */
    @Override
    public void deleteAuth(String authToken) {
        this.listOfAuthData.removeIf(user -> user.authToken().equals(authToken));
    }
}
