package dataaccess;

/**
 * This is the Exception class for when the person is not logged in/ allowed to do an action.
 */
public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
