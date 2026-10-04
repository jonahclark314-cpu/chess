package dataaccess;
import model.*;
import org.eclipse.jetty.server.Authentication;
import service.RegisterRequest;

import java.util.ArrayList;
import java.util.Objects;


public class MemoryUserDAO implements UserDAO{
    ArrayList<UserData> listOfUserData;

    public MemoryUserDAO () {
        this.listOfUserData = new ArrayList<>();
    }

    @Override
    public void clear() {
        this.listOfUserData.clear();
    }

    @Override
    public UserData getUser(String username) {
        for (UserData person : this.listOfUserData) {
            if (username.equals(person.getUsername())) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void createUser(RegisterRequest userInfo) {
        UserData newUser = new UserData(userInfo.getUsername(), userInfo.getPassword(), userInfo.getEmail());
        listOfUserData.add(newUser);
    }

}
