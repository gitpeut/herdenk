package org.peut.herdenk.exceptions;


public class DuplicateUserException extends RuntimeException {

    public DuplicateUserException(String message) {
        super(message);
    }
    public DuplicateUserException() {
        super("Bad request.");
    }

}
