package io.nwdaf.eventsubscription.controller.http;

public class MissingContentLengthException extends Exception {
    public MissingContentLengthException(String message, Throwable cause) {
        super(message, cause);
    }
}
