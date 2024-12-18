package org.example.exception;

public class EmployeeValidationException extends RuntimeException {

  public EmployeeValidationException(String errorMessage) {
    super(errorMessage);
  }
}
