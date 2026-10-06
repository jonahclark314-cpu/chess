package service;

/**
 * This is the record class representing the Login result. This is what is given back to the client.
 * @param username - Username of the user.
 * @param authToken - logged in Authentication token the user must store and use.
 */
public record LoginResult(String username, String authToken) {
}
