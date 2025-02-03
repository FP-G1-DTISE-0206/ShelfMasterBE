package com.DTISE.ShelfMasterBE.common.exceptions;

public class DuplicateProductNameException extends RuntimeException {
  public DuplicateProductNameException(String message) {
    super(message);
  }
}
