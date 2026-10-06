package service;

/**
 * This is the response object for a new game creation. Packages it up into a something that JSON can send to client.
 * @param gameID Just stores the Game's ID.
 */
public record GameResponse(int gameID) {
}
