package org.example.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import org.example.model.EmployeeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

class EmployeeRepositoryTest {

  @InjectMocks
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
        "Id,firstName,lastName,salary,managerId",
        "1,John,Doe,55000.00,2",
        "2,Jane,Smith,65000.00,"));

    // when
    Map<Integer, EmployeeRecord> result = employeeRepository.createEmployeesMap(
        tempFile.toString());

    // then
    EmployeeRecord employeeJohn = result.get(1);
    assertNotNull(employeeJohn);
    assertEquals(1, employeeJohn.getId());
    assertEquals("John", employeeJohn.getFirstName());
    assertEquals("Doe", employeeJohn.getLastName());
    assertEquals(55000.00, employeeJohn.getSalary());
    assertEquals(Integer.valueOf(2), employeeJohn.getManagerId());

    EmployeeRecord employeeJane = result.get(2);
    assertNotNull(employeeJane);
    assertEquals(2, employeeJane.getId());
    assertEquals("Jane", employeeJane.getFirstName());
    assertEquals("Smith", employeeJane.getLastName());
    assertEquals(65000.00, employeeJane.getSalary());
    assertNull(employeeJane.getManagerId());
  }

  @Test
  void testCreateEmployeesMap_WithParsingError() throws IOException {
    // given
    Path tempFile = Files.createTempFile("test-invalid-format", ".csv");
    Files.write(tempFile, Arrays.asList("Id,firstName,lastName,salary,managerId",
        "xyz,John,Doe,55000.00,2")); // Invalid ID

    // when
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        employeeRepository.createEmployeesMap(tempFile.toString()));

    // then
    assertEquals("Error parsing number on line 2: For input string: \"xyz\""
        , exception.getMessage());

    Files.deleteIfExists(tempFile);
  }

  @Test
  void testCreateEmployeesMapWithDataFormatError() throws IOException {
    // given
    Path tempFile = Files.createTempFile("test-invalid-format", ".csv");
    Files.write(tempFile, Arrays.asList("Id,firstName,lastName,salary,managerId,invalidField",
        "xyz,John,Doe,55000.00,2, invalidField"));  // Invalid Format

    // when
    Exception exception = assertThrows(IllegalArgumentException.class, () ->
        employeeRepository.createEmployeesMap(tempFile.toString()));

    // then
    assertEquals("Incorrect data format on line 2", exception.getMessage());

    Files.deleteIfExists(tempFile);
  }
}