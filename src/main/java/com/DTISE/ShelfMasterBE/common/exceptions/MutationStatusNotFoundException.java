package com.DTISE.ShelfMasterBE.common.exceptions;

public class MutationStatusNotFoundException extends RuntimeException {
    public MutationStatusNotFoundException(String message) {
        super(message);
    }
}
