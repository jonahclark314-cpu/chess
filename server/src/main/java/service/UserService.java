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
        if (registerRequest.getEmail().isEmpty() || registerRequest.getPassword().isEmpty() || registerRequest.getUsername().isEmpty() || registerRequest.getPassword() == null || registerRequest.getEmail() == null || registerRequest.getUsername() == null) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(registerRequest.getUsername());

        if (user == null) {
            userDAO.createUser(registerRequest);
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }

    public void verifyUser(LoginRequest login) throws UnauthorizedException, BadRequestException {
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty() || login.getPassword() == null || login.getUsername() == null) {
            throw new BadRequestException("Error: bad request");
        }
        UserData user = this.userDAO.getUser(login.getUsername());
        if (user == null){
            throw new UnauthorizedException("Error: unauthorized. Username is incorrect");
        }
        if (!user.getPassword().equals(login.getPassword())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void clear() {
        this.userDAO.clear();
    }
}
