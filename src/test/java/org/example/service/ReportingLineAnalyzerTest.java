package org.example.service;

import static org.junit.jupiter.api.Assertions.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.example.model.EmployeeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

class ReportingLineAnalyzerTest {

  private ReportingLineAnalyzer analyzer;
  private Map<Integer, EmployeeRecord> employees;

  @BeforeEach
  void setup() {
    employees = new HashMap<>();
    // Setting up a sample hierarchy where Employee (ID 1) is at the top
    employees.put(1, new EmployeeRecord(1, "Alice", "Smith", 90000, null));
    employees.put(2, new EmployeeRecord(2, "Bob", "Jones", 80000, 1));
    employees.put(3, new EmployeeRecord(3, "Charlie", "Brown", 70000, 2));
    employees.put(4, new EmployeeRecord(4, "David", "Wilson", 60000, 3));
    employees.put(5, new EmployeeRecord(5, "Eve", "Black", 50000, 4));
    employees.put(6, new EmployeeRecord(6, "Fiona", "White", 40000, 5));
    // Fiona's reporting line depth = 5

    analyzer = new ReportingLineAnalyzer();
  }

  @Test
  void testAnalyzeReportingLines() {
    String expectedOutput = "Fiona has a reporting line that is too long by 1 levels\r\n";
    String output = analyzer.analyzeReportingLines(employees);
    assertEquals(expectedOutput, output);
  }
}