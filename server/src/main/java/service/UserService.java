package service;
import dataaccess.*;
import io.javalin.http.Context;
import model.*;

public class UserService {
    UserDAO userDAO;

    public UserService () {
        this.userDAO = new MemoryUserDAO();
    }


    public void createUser (RegisterRequest registerRequest) throws AlreadyTakenException {
        if (registerRequest.getEmail() == null || registerRequest.getPassword() == null || registerRequest.getUsername() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(registerRequest.getUsername());

        if (user == null) {
            userDAO.createUser(registerRequest);
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }


    public void clear() {
        this.userDAO.clear();
    }
}
