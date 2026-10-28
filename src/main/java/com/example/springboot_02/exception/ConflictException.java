package com.example.springboot_02.exception;


public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
