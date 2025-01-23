package com.DTISE.ShelfMasterBE.common.exceptions;

public class DuplicateCategoryNameException extends RuntimeException {
    public DuplicateCategoryNameException(String message) {
        super(message);
    }
}
