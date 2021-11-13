package org.peut.herdenk.exceptions;


public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super( "{ \"message\": \"" + message + "\"}");
    }
    public DuplicateException() {
        super("Bad request.");
    }

}
