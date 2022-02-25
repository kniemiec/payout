package com.kniemiec.soft.payout.error;

public class ReviewDataException extends RuntimeException {
    private String message;
    public ReviewDataException(String s) {
        super(s);
        this.message=s;
    }
}
