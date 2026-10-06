package dataaccess;
import model.*;
import service.RegisterRequest;

import java.util.ArrayList;

/**
 * this is the implementation of UserDAO that I am using until I get the db up and running.
 */
public class MemoryUserDAO implements UserDAO{
    ArrayList<UserData> listOfUserData;

    /**
     * Constructor.
     */
    public MemoryUserDAO () {
        this.listOfUserData = new ArrayList<>();
    }

    /**
     * This is what clears this portion of the db.
     */
    @Override
    public void clear() {
        this.listOfUserData.clear();
    }

    /**
     * This is what gets a specific users data from their username.
     * @param username - string username.
     * @return - returns the person object or null if there is no such username in use.
     */
    @Override
    public UserData getUser(String username) {
        for (UserData person : this.listOfUserData) {
            if (username.equals(person.username())) {
                return person;
            }
        }
        return null;
    }

    /**
     * This is what is used during registration of a new user.
     * @param userInfo - This is the RegisterRequest object passed in by the Service.
     */
    @Override
    public void createUser(RegisterRequest userInfo) {
        UserData newUser = new UserData(userInfo.username(), userInfo.password(), userInfo.email());
        listOfUserData.add(newUser);
    }

    /**
     * This is the way we can test how many users are in our database. This is only
     * used for testing purposes.
     * @return - number of users in the db.
     */
    @Override
    public int getLenUsers() {
        return listOfUserData.size();
    }

}
