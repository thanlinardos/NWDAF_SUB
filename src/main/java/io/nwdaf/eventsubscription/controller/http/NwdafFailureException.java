package io.nwdaf.eventsubscription.controller.http;

import lombok.Getter;

@Getter
public class NwdafFailureException extends Throwable {
    private final String causeString;
    public NwdafFailureException(String msg, Throwable cause, String causeString) {
        super(msg, cause);
        this.causeString = causeString;
    }
}
