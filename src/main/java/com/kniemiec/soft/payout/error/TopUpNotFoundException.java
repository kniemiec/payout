package com.kniemiec.soft.payout.error;

public class TopUpNotFoundException extends RuntimeException {
    String message;
    public TopUpNotFoundException(String s) {
        super(s);
        this.message = s;
    }
}
