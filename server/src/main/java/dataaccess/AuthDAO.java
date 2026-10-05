package dataaccess;

public interface AuthDAO {

    void clear();
    String createAuth(String username, String authToken);
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
