package org.peut.herdenk.exceptions;


public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message) {
        super(message);
    }
    public FileNotFoundException() {
        super("Bad request.");
    }

}
