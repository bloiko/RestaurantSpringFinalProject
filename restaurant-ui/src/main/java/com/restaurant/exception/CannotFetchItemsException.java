package com.restaurant.exception;

public class CannotFetchItemsException extends FoodItemsException {
    public CannotFetchItemsException() {
        super();
    }

    public CannotFetchItemsException(String message) {
        super(message);
    }

    public CannotFetchItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}
