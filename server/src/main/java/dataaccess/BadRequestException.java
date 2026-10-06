package dataaccess;

/**
 * This is the exception class used when methods are used without entering the proper
 * information into them.
 */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}
