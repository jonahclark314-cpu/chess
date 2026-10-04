package dataaccess;

import java.sql.Array;
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
    public String createAuth(String username) {
        AuthData auth = new AuthData(username);
        this.listOfAuthData.add(auth);
        String authString = auth.getAuthToken();
        return authString;
    }
}
