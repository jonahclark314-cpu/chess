package dataaccess;

import java.util.ArrayList;

import model.*;

public class MemoryAuthDAO implements AuthDAO{
    ArrayList<AuthData> listOfAuthData;


    public MemoryAuthDAO () {
        this.listOfAuthData = new ArrayList<>();
    }


    @Override
    public void clear() {
        this.listOfAuthData.clear();
    }

    @Override
    public String createAuth(String username, String authToken) {
        AuthData auth = new AuthData(username, authToken);
        this.listOfAuthData.add(auth);
        return auth.authToken();
    }

    @Override
    public String getAuth(String authToken) {
        for (AuthData user : this.listOfAuthData) {
            if (user.authToken().equals(authToken)) {
                return user.username();
            }
        }
        return "";
    }

    @Override
    public void deleteAuth(String authToken) {
        this.listOfAuthData.removeIf(user -> user.authToken().equals(authToken));
    }
}
