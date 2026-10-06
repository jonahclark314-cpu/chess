package service;

/**
 * This is the record class for the registration requests. Clients will give the server this object.
 * @param username - the username of the user.
 * @param password - the password of the user.
 * @param email - the email address of the user.
 */
public record RegisterRequest(String username, String password, String email) {

    //This is necessary so that code written in the Service classes works.
    @Override
    public String username() {
        if (this.username == null) {
            return "";
        }
        return username;
    }

    //This is necessary so that code written in the Service classes works.
    @Override
    public String password() {
        if (this.password == null) {
            return "";
        }
        return password;
    }
}
