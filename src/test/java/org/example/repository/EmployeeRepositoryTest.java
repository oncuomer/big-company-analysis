package org.example.repository;

import static org.example.Constants.VALID_HEADERS;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import org.example.exception.EmployeeValidationException;
import org.example.exception.FileReaderException;
import org.example.exception.FileValidationException;
import org.example.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


class EmployeeRepositoryTest {

  private EmployeeRepository employeeRepository;

  @BeforeEach
  void setUp() {
    employeeRepository = new EmployeeRepository();
  }

  @Test
  void testCreateEmployeesMapValidData() throws Exception {
    // given
    Path tempFile = Files.createTempFile("employees", ".csv");
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,Jane,Smith,65000,",
        "2,John,Doe,55000,1"));

    // when
    Map<Integer, Employee> result = employeeRepository.createEmployeesMap(
        tempFile.toString());

    // then
    Employee employeeJane = result.get(1);
    assertNotNull(employeeJane);
    assertAll("Employee Jane's details",
        () -> assertEquals(1, employeeJane.getId()),
        () -> assertEquals("Jane", employeeJane.getFirstName()),
        () -> assertEquals("Smith", employeeJane.getLastName()),
        () -> assertEquals(65000.00, employeeJane.getSalary()),
        () -> assertNull(employeeJane.getManagerId())
    );

    Employee employeeJohn = result.get(2);
    assertNotNull(employeeJohn);

    assertAll("Employee John's details",
        () -> assertEquals(2, employeeJohn.getId()),
        () -> assertEquals("John", employeeJohn.getFirstName()),
        () -> assertEquals("Doe", employeeJohn.getLastName()),
        () -> assertEquals(55000.00, employeeJohn.getSalary()),
        () -> assertEquals(Integer.valueOf(1), employeeJohn.getManagerId())
    );

    assertAll("Subordinate check",
        () -> assertEquals(1, employeeJane.getSubordinates().size()),
        () -> assertTrue(employeeJane.getSubordinates().contains(employeeJohn))
    );
  }

  @Test
  void testCreateEmployeesMapWhenFileNotFound() {
    // given
    String filePath = "notExist";

    // when
    Exception exception = assertThrows(FileReaderException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("Failed to read the employee file: " + filePath
        , exception.getMessage());
  }

  @ParameterizedTest
  @ValueSource(strings = {
      VALID_HEADERS + ",invalidExtraField",
      "InvalidId,firstName,lastName,salary,managerId",
      "Id,InvalidFirstName,lastName,salary,managerId",
      "Id,firstName,InvalidLastName,salary,managerId",
      "Id,firstName,lastName,InvalidSalary,managerId",
      "Id,firstName,lastName,salary,InvalidManagerId",
      "Id,firstName,lastName"})
  void testCreateEmployeesMapWithMalformedHeaders(String headers) throws Exception {
    // given
    Path tempFile = Files.createTempFile("test-invalid-headers", ".csv");
    String filePath = tempFile.toString();
    Files.write(tempFile, Arrays.asList(headers, "xyz,John,Doe,55000.00,2"));

    // when
    FileValidationException exception = assertThrows(FileValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("Header line is incorrect. Expected: Id,firstName,lastName,salary,managerId"
        , exception.getMessage());

    Files.deleteIfExists(tempFile);
  }

  @Test
  void testCreateEmployeesMapNoCeoFound() throws Exception {
    // given
    Path tempFile = Files.createTempFile("employees", ".csv");
    String filePath = tempFile.toString();
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,John,Doe,55000.00,2",
        "2,Jane,Smith,65000.00,1"));

    // when
    FileValidationException exception = assertThrows(FileValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("No CEO found. There must be exactly one employee without a managerId."
        , exception.getMessage());
  }

  @Test
  void testCreateEmployeesMapWithMultipleCeoFound() throws Exception {
    // given
    Path tempFile = Files.createTempFile("employees", ".csv");
    String filePath = tempFile.toString();
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,John,Doe,55000.00,",
        "2,Jane,Smith,65000.00,"));

    // when
    FileValidationException exception = assertThrows(FileValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("Multiple CEOs found. Only one CEO without a managerId is allowed."
        , exception.getMessage());
  }

  @Test
  void testCreateEmployeesMapWithWrongManagerId() throws Exception {
    // given
    Path tempFile = Files.createTempFile("test-invalid-format", ".csv");
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,John,Doe,55000.00,3",
        "2,Jane,Smith,65000.00,"));
    String filePath = tempFile.toString();

    // when
    EmployeeValidationException exception = assertThrows(EmployeeValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("No manager with ID 3 found for employee 1"
        , exception.getMessage());

    Files.deleteIfExists(tempFile);
  }

  @Test
  void testCreateEmployeesMapWithParsingError() throws Exception {
    // given
    Path tempFile = Files.createTempFile("test-invalid-format", ".csv");
    Files.write(tempFile, Arrays.asList(VALID_HEADERS,
        "xyz,John,Doe,55000.00,2")); // Invalid ID
    String filePath = tempFile.toString();

    // when
    EmployeeValidationException exception = assertThrows(EmployeeValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("Error parsing number on line 2: For input string: \"xyz\""
        , exception.getMessage());

    Files.deleteIfExists(tempFile);
  }

  @Test
  void testCreateEmployeesMapWithWrongColumnCount() throws Exception {
    // given
    Path tempFile = Files.createTempFile("test-invalid-format", ".csv");
    Files.write(tempFile, Arrays.asList(VALID_HEADERS,
        "1,John,Doe,55000.00,2, XXX")); // Invalid count
    String filePath = tempFile.toString();

    // when
    FileValidationException exception = assertThrows(FileValidationException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("Incorrect column count on line 2. Expected 5 columns, but found 6 columns."
        , exception.getMessage());

    Files.deleteIfExists(tempFile);
  }

  @Test
  void testCreateEmployeesMapWithEmptyFile() throws Exception {
    // given
    Path tempFile = Files.createTempFile("test-empty-file", ".csv");
    String filePath = tempFile.toString();

    // when
    FileReaderException exception = assertThrows(FileReaderException.class, () ->
        employeeRepository.createEmployeesMap(filePath));

    // then
    assertEquals("The file is empty. Please provide a file with at least headers."
        , exception.getMessage());
  }

}