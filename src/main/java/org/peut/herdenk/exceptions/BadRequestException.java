package org.peut.herdenk.exceptions;


public class NewUserException extends RuntimeException {

    public NewUserException(String message) {
        super(message);
    }
    public NewUserException() {
        super("Bad request.");
    }

}
