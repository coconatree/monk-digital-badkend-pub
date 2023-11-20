package com.monk.exception;

public class StorageFileNotFoundException extends MonkException {
    public StorageFileNotFoundException(long id) {
        super("Could not read file with id: " + id);
    }
}
