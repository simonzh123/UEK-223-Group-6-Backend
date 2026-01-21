package com.example.demo.core.exception;

import java.util.NoSuchElementException;
import java.util.UUID;

public class NoSuchListEntryException extends NoSuchElementException {
    public NoSuchListEntryException(UUID id) {
        super("No list entry found with corresponding ID: " + id);
    }
}
