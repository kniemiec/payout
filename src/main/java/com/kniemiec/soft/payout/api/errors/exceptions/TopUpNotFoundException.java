package com.kniemiec.soft.payout.api.errors.exceptions;

public class TopUpNotFoundException extends RuntimeException {
    String message;
    public TopUpNotFoundException(String s) {
        super(s);
        this.message = s;
    }
}
