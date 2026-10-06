package service;

import model.GameData;

import java.util.ArrayList;

/**
 * Here is the record class that stores the list of games to send back to the client.
 * @param games - list of games to store and send back to client.
 */
public record ListGamesResult(ArrayList<GameData> games) {}
