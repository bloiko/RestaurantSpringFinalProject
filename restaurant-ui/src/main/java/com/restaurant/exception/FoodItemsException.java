package com.restaurant.exception;

public class FoodItemsException extends Exception{
    public FoodItemsException() {
        super();
    }

    public FoodItemsException(String message) {
        super(message);
    }

    public FoodItemsException(String message, Throwable cause) {
        super(message, cause);
    }
}
