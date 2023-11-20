package com.monk.exception;

public class IdNotFoundException extends MonkException{
    public IdNotFoundException() {
        super("Id not found");
    }
}
