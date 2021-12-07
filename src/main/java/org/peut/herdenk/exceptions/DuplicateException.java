package org.peut.herdenk.exceptions;


public class DuplicateException extends RuntimeException {

    public DuplicateException(String message) {
        super( "{ \"message\": \"" + message + "\"}");
    }
    public DuplicateException(Exception e) {
        super( e.getMessage() );
    }
    public DuplicateException() {
        super("Bad request.");
    }

}
