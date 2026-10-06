package dataaccess;

import model.UserData;
import service.RegisterRequest;

/**
 * This is the implementation that I will use once I get the db up.
 */
public class SQLUserDAO implements UserDAO{
    @Override
    public void clear() {

    }

    @Override
    public UserData getUser(String username) {
        return null;
    }

    @Override
    public void createUser(RegisterRequest userInfo) {

    }

    @Override
    public int getLenUsers() {
        return 0;
    }
}
