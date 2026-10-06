package service;
import dataaccess.*;
import model.*;

public class UserService {
    UserDAO userDAO;

    public UserService () {
        this.userDAO = new MemoryUserDAO();
    }


    public void createUser (RegisterRequest registerRequest) throws AlreadyTakenException, BadRequestException {
        if (registerRequest.email().isEmpty() || registerRequest.password().isEmpty() || registerRequest.username().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }

        UserData user = userDAO.getUser(registerRequest.username());

        if (user == null) {
            userDAO.createUser(registerRequest);
        } else {
            throw new AlreadyTakenException("Error: already taken");
        }
    }

    public void verifyUser(LoginRequest login) throws UnauthorizedException, BadRequestException {
        if (login.getUsername().isEmpty() || login.getPassword().isEmpty()) {
            throw new BadRequestException("Error: bad request");
        }
        UserData user = this.userDAO.getUser(login.getUsername());
        if (user == null){
            throw new UnauthorizedException("Error: unauthorized. Username is incorrect");
        }
        if (!user.password().equals(login.getPassword())) {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }

    public void clear() {
        this.userDAO.clear();
    }

    public int getLenUsers() {
        return userDAO.getLenUsers();
    }

}
