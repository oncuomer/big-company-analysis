package org.example.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.exception.EmployeeValidationException;
import org.example.exception.FileReaderException;
import org.example.exception.FileValidationException;
import org.example.model.Employee;

/**
 * Responsible for loading and processing employee data from a CSV file.
 */
public class EmployeeRepository {

  private static final String DELIMETER = ",";
  private static final List<String> HEADERS = Arrays.asList("Id", "firstName", "lastName", "salary",
      "managerId");
  private static final int COLUMN_COUNT = 5;

  private static final int MAXIMUM_ROW_COUNT = 1001;

  /**
   * Creates a map of employee IDs to Employee objects by reading data from a CSV file. Validates
   * that the file contains correct headers and the right number of rows. Ensures only one Employee
   * lacks a managerId (CEO).
   *
   * @param filePath the path to the CSV file containing employee data
   * @return a map linking employee IDs to Employee objects
   * @throws FileReaderException         if issues with file access occur or file format is not as
   * @throws FileValidationException     if file format is incorrect such as wrong headers,
   *                                     incorrect number of columns in any row, more than allowed
   *                                     rows, or multiple/no CEOs. expected
   * @throws EmployeeValidationException if data validation fails (e.g., incorrect data format,
   *                                     multiple CEOs)
   */
  public Map<Integer, Employee> createEmployeesMap(String filePath) {
    List<String> dataLines = readEmployeeFile(filePath);
    validateFileConditions(dataLines);

    Map<Integer, Employee> employees = processDataLines(dataLines);
    linkSubordinates(employees);

    return employees;
  }

  private List<String> readEmployeeFile(String filePath) {
    List<String> dataLines;
    try {
      dataLines = Files.readAllLines(Paths.get(filePath));
    } catch (IOException e) {
      throw new FileReaderException("Failed to read the employee file: " + e.getMessage());
    }
    return dataLines;
  }

  private void validateFileConditions(List<String> dataLines) {
    if (dataLines.isEmpty()) {
      throw new FileReaderException(
          "The file is empty. Please provide a file with at least headers.");
    }

    validateHeaders(dataLines.get(0));

    if (dataLines.size() > MAXIMUM_ROW_COUNT) {
      throw new FileValidationException("Input file contains more than 1000 employee records.");
    }
  }

  private void validateHeaders(String headerLine) {
    List<String> actualHeaders = Arrays.asList(headerLine.trim().split(DELIMETER, -1));
    if (!HEADERS.equals(actualHeaders)) {
      throw new FileValidationException(
          "Header line is incorrect. Expected: " + String.join(",", HEADERS));
    }
  }

  private Employee parseEmployeeRecord(String line, int lineIndex) {
    String[] columns = line.trim().split(DELIMETER, -1);
    if (columns.length != COLUMN_COUNT) {
      throw new FileValidationException("Incorrect column count on line " + (lineIndex + 1) +
          ". Expected " + COLUMN_COUNT + " columns, but found " + columns.length + " columns.");
    }

    try {
      int id = Integer.parseInt(columns[0]);
      String firstName = columns[1];
      String lastName = columns[2];
      double salary = Double.parseDouble(columns[3]);
      Integer managerId = columns[4].isEmpty() ? null : Integer.parseInt(columns[4]);

      return new Employee(id, firstName, lastName, salary, managerId);
    } catch (NumberFormatException e) {
      throw new EmployeeValidationException(
          "Error parsing number on line " + (lineIndex + 1) + ": " + e.getMessage());
    }
  }

  private Map<Integer, Employee> processDataLines(List<String> dataLines) {
    Map<Integer, Employee> employees = new HashMap<>();
    boolean ceoFound = false;

    for (int i = 1; i < dataLines.size(); i++) {
      Employee employee = parseEmployeeRecord(dataLines.get(i), i);
      if (employee.getManagerId() == null) {
        if (ceoFound) {
          throw new FileValidationException(
              "Multiple CEOs found. Only one CEO without a managerId is allowed.");
        }
        ceoFound = true;
      }
      employees.put(employee.getId(), employee);
    }

    if (!ceoFound) {
      throw new FileValidationException(
          "No CEO found. There must be exactly one employee without a managerId.");
    }

    return employees;
  }

  private void linkSubordinates(Map<Integer, Employee> employees) {
    employees.values().forEach(emp -> {
      if (emp.getManagerId() != null) {
        Employee manager = employees.get(emp.getManagerId());
        if (manager != null) {
          manager.addSubordinate(emp);
        } else {
          throw new EmployeeValidationException(
              "No manager with ID " + emp.getManagerId() + " found for employee " + emp.getId());
        }
      }
    });
  }
}
