package model;

/**
 * this is the record class that we use to represent Auth Data. it has:
 * @param username - String of the username
 * @param authToken - string representing the particular login session.
 */
public record AuthData(String username, String authToken) {
}
