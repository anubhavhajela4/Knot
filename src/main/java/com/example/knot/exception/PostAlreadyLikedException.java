package com.example.knot.exception;

public class PostAlreadyLikedException extends RuntimeException {
    public PostAlreadyLikedException(String message) {
        super(message);
    }
}
