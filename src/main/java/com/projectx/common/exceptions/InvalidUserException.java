package com.projectx.common.exceptions;

public class InvalidUserException extends RuntimeException {
    public InvalidUserException(String msg) {
        super(msg);
    }
}
