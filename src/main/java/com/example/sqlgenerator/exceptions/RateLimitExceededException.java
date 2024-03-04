package com.example.sqlgenerator.exceptions;

public class RateLimitExceededException extends RuntimeException {
    public RateLimitExceededException() {
        super("Too many requests. Rate limit exceeded.");
    }
}
