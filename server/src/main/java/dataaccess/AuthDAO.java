package dataaccess;

public interface AuthDAO {

    void clear();
    String createAuth(String username);
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
