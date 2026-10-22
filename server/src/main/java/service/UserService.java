package service;

import dataaccess.*;
import dataaccess.sql.SQLUserDAO;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

/**
 * This is the User Service class that handles all the logic for the users. it interacts with the Data Access Object for users.
 */
public class UserService {
    // Here is the Data Access Object that this class will use.
    UserDAO userDAO;

    //Constructor.
    public UserService() {
        this.userDAO = new SQLUserDAO();
    }

    /**
     * This is the method that is used to create a new user. It tells the DAO to make the new user.
     *
     * @param registerRequest - this is the object that the Server Handler gives us. it gives us the email, username, and password.
     * @throws AlreadyTakenException - If a username is already taken, throw an exception.
     * @throws BadRequestException   - If any required information is missing, throw an exception.
     */
    public void createUser(RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException {
        // Make sure all the information is provided (Username, Password, Email).
        if (registerRequest.email().isEmpty() || registerRequest.password().isEmpty() || registerRequest.username().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        // Check if the username is already taken.
        UserData user = userDAO.getUser(registerRequest.username());
        if (user == null) {
            // If it is not taken, create the user.
            RegisterRequest hashedPasswordUser = new RegisterRequest(registerRequest.username(), BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt()), registerRequest.email());
            userDAO.createUser(hashedPasswordUser);
        } else {
            // If it IS taken, throw an exception.
            throw new AlreadyTakenException("Error: already taken");
        }
    }

    /**
     * This is the method that helps verify if login information is correct.
     *
     * @param login - LoginRequest object that has the username and password.
     * @throws UnauthorizedException - if the password is incorrect, or the user does not exist, throw an exception.
     * @throws BadRequestException   - if any login info is not provided, throw an exception.
     */
    public void verifyUser(LoginRequest login) throws UnauthorizedException, BadRequestException {
        // Check if all the required login information is provided (username and password).
        if (login.username().isEmpty() || login.password().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        // Check if the username is valid and there is a user with the username.
        System.out.println("Trying to login with the username:");
        System.out.println(login.username());
        UserData user = this.userDAO.getUser(login.username());
        if (user == null) {
            System.out.println("well that sucks.");
            throw new UnauthorizedException("Error: unauthorized. Username is incorrect");
        }

        // Check if the password is correct.
        if (!BCrypt.checkpw(login.password(), user.password())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    /**
     * This is the method that tells the DAO to clear all of the User data from the db.
     */
    public void clear() {
        this.userDAO.clear();
    }

    /**
     * This is a method used for testing. It asks the DAO to count how many users are registered.
     *
     * @return - integer number of registered users.
     */
    public int getLenUsers() {
        return userDAO.getLenUsers();
    }

}
