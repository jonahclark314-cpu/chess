package dataaccess;
import model.*;
import service.RegisterRequest;

public interface UserDAO {

    void clear();
    UserData getUser(String username);
    void createUser(RegisterRequest userInfo);
    int getLenUsers();
}
