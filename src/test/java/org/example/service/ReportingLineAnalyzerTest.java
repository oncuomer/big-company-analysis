package org.example.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.example.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ReportingLineAnalyzerTest {

  private ReportingLineAnalyzer analyzer;
  private Map<Integer, Employee> employees;

  @BeforeEach
  void setup() {
    employees = new HashMap<>();
    // Setting up a sample hierarchy where Employee (ID 1) is at the top
    employees.put(1, new Employee(1, "Alice", "Smith", 90000, null));
    employees.put(2, new Employee(2, "Bob", "Jones", 80000, 1));
    employees.put(3, new Employee(3, "Charlie", "Brown", 70000, 2));
    employees.put(4, new Employee(4, "David", "Wilson", 60000, 3));
    employees.put(5, new Employee(5, "Eve", "Black", 50000, 4));
    employees.put(6, new Employee(6, "Fiona", "White", 40000, 5));
    // Fiona's reporting line depth = 5

    analyzer = new ReportingLineAnalyzer();
  }

  @Test
  void testAnalyzeReportingLines() {
    String expectedOutput = "6|Fiona White has a reporting line that is too long by 1 levels\r\n";
    String output = analyzer.analyzeReportingLines(employees);
    assertEquals(expectedOutput, output);
  }
}
