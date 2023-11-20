package com.monk.exception;

public class RecordNotFoundException extends MonkException{
    public RecordNotFoundException() {
        super("Record not found");
    }
}
