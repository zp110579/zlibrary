package com.zee.exception;

/**
 * @author Administrator
 */
public class ZException extends RuntimeException {
    public static ZException newInstance(String message) {
        return new ZException(message);
    }

    public ZException(String errorMessage) {
        super(errorMessage);
    }
}
