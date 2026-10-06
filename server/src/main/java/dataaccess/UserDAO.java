package dataaccess;
import model.*;
import service.RegisterRequest;

/**
 * This is the Data Access Object interface for all User data.
 */
public interface UserDAO {
    // Here are all the methods that this interface will use.
    void clear();
    UserData getUser(String username);
    void createUser(RegisterRequest userInfo);
    int getLenUsers();
}
