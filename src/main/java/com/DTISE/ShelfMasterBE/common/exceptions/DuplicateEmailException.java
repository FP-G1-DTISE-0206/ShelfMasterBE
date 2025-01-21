package com.DTISE.ShelfMasterBE.common.exceptions;

public class DuplicateEmailException extends RuntimeException {
    public DuplicateEmailException(String msg) {
        super(msg);
    }
}
