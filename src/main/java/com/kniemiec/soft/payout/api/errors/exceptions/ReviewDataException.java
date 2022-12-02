package com.kniemiec.soft.payout.api.errors.exceptions;

public class ReviewDataException extends RuntimeException {
    private String message;
    public ReviewDataException(String s) {
        super(s);
        this.message=s;
    }
}
