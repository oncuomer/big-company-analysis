package org.example.repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.model.EmployeeRecord;

public class EmployeeRepository {
  public Map<Integer, EmployeeRecord> createEmployeesMap(String filePath) throws IOException {
    List<String> dataLines = Files.readAllLines(Path.of(filePath));
    Map<Integer, EmployeeRecord> employees = new HashMap<>();

    if (dataLines.size() > 1001) {
      throw new IllegalArgumentException("Input file contains more than 1000 employee records.");
    }

    for (int i = 1; i < dataLines.size(); i++) {
      EmployeeRecord employee = parseEmployeeRecord(dataLines.get(i), i);
      if (employee != null) {
        employees.put(employee.getId(), employee);
      }
    }

    linkSubordinates(employees);
    return employees;
  }

  private EmployeeRecord parseEmployeeRecord(String line, int lineIndex) {
    String[] columns = line.trim().split(",", -1);
    if (columns.length != 5) {
      throw new IllegalArgumentException("Incorrect data format on line " + (lineIndex + 1));
    }

    try {
      int id = Integer.parseInt(columns[0]);
      String firstName = columns[1];
      String lastName = columns[2];
      double salary = Double.parseDouble(columns[3]);
      Integer managerId = columns[4].isEmpty() ? null : Integer.parseInt(columns[4]);

      return new EmployeeRecord(id, firstName, lastName, salary, managerId);
    } catch (NumberFormatException e) {
      throw new IllegalArgumentException("Error parsing number on line " + (lineIndex + 1) + ": " + e.getMessage());
    }
  }

  private void linkSubordinates(Map<Integer, EmployeeRecord> employees) {
    employees.values().forEach(emp -> {
      if (emp.getManagerId() != null) {
        EmployeeRecord manager = employees.get(emp.getManagerId());
        if (manager != null) {
          manager.addSubordinate(emp);
        } else {
          System.err.println("Warning: No manager with ID " + emp.getManagerId() + " found for employee " + emp.getId());
        }
      }
    });
  }
}
