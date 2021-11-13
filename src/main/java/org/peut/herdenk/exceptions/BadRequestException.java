package org.peut.herdenk.exceptions;


public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super( "{ \"message\": \"" + message + "\"}");
    }
    public BadRequestException() {
        super("Bad request.");
    }

}
