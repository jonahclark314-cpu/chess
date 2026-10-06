package service;

/**
 * This is the result record if there is an error. It helps to put an error into a usable object
 * to put into JSON.
 * @param message - the message of the error.
 */
public record ErrorResult(String message) {}
