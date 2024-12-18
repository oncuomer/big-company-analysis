package org.example.exception;

/**
 * Custom exception class for handling file reading errors and validations.
 */
public class FileReaderException extends RuntimeException {

  public FileReaderException(String errorMessage) {
    super(errorMessage);
  }

}
