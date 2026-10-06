package dataaccess;

/**
 * This is the interface for the Auth Data Access Objects.
 */
public interface AuthDAO {
    // Here are all the methods I will use in this.
    void clear();
    String createAuth(String username, String authToken);
    String getAuth(String authToken);
    void deleteAuth(String authToken);
}
