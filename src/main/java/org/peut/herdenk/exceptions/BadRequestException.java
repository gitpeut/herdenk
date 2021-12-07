package org.peut.herdenk.exceptions;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super( "{ \"message\": \"" + message + "\"}");
    }
    public BadRequestException(Exception e) {
        super( e.getMessage() );
    }
    public BadRequestException() {
        super("Bad request.");
    }

}
