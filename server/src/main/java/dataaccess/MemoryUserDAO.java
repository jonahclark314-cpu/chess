package dataaccess;
import model.*;
import service.RegisterRequest;

import java.util.ArrayList;


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
            if (username.equals(person.username())) {
                return person;
            }
        }
        return null;
    }

    @Override
    public void createUser(RegisterRequest userInfo) {
        UserData newUser = new UserData(userInfo.username(), userInfo.password(), userInfo.email());
        listOfUserData.add(newUser);
    }

    @Override
    public int getLenUsers() {
        return listOfUserData.size();
    }

}
