package io.nwdaf.eventsubscription.controller.http;

import lombok.Getter;

@Getter
public class PayloadTooLargeException extends Exception {

    private Long size;

    public PayloadTooLargeException(String message, Throwable cause, Long size) {
        super(message, cause);
        this.size = size;
    }


}
