package com.DTISE.ShelfMasterBE.common.exceptions;

public class MutationTypeNotFoundException extends RuntimeException {
    public MutationTypeNotFoundException(String message) {
        super(message);
    }
}
