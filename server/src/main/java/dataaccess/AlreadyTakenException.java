package dataaccess;

/**
 * This class is an exception class. It is used for when a username/color is already taken.
 */
public class AlreadyTakenException extends RuntimeException {
    public AlreadyTakenException(String message) {
        super(message);
    }
}
