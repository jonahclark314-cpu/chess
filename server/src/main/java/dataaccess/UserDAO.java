package dataaccess;
import model.*;
import service.RegisterRequest;

public interface UserDAO {

    public void clear();
    public UserData getUser(String username);
    public void createUser(RegisterRequest userInfo);
    public int getLenUsers();
}
