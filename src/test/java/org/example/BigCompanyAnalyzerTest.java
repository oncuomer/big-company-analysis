package org.example;

import static org.junit.jupiter.api.Assertions.*;

import org.example.exception.FileReaderException;
import org.junit.jupiter.api.Test;

class BigCompanyAnalyzerTest {

  @Test
  void testWhenNoArgumentsProvidedThenThrowsException() {
    // given
    String[] args = {};

    // when
    FileReaderException exception = assertThrows(FileReaderException.class, () -> {
      BigCompanyAnalyzer.main(args);
    });

    // then
    assertEquals("Error: You must pass exactly one .csv file.", exception.getMessage());
  }

  @Test
  void whenMultipleArgumentsProvidedThenThrowsException() {
    String[] args = {"file1.csv", "file2.csv"};

    Exception exception = assertThrows(FileReaderException.class, () -> {
      BigCompanyAnalyzer.main(args);
    });

    assertEquals("Error: You must pass exactly one .csv file.", exception.getMessage());
  }

  @Test
  void whenNonCsvFileProvidedThenThrowsException() {
    String[] args = {"file.txt"};

    Exception exception = assertThrows(FileReaderException.class, () -> {
      BigCompanyAnalyzer.main(args);
    });

    assertEquals("Error: You must pass exactly one .csv file.", exception.getMessage());
  }
}