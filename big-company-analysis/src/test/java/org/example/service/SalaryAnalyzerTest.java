package org.example.service;


import org.example.model.EmployeeRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Map;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SalaryAnalyzerTest {

  private SalaryAnalyzer analyzer;
  private Map<Integer, EmployeeRecord> employees;

  @BeforeEach
  void setup() {
    employees = new HashMap<>();
    analyzer = new SalaryAnalyzer();
  }

  @Test
  void testUnderPaidManager() {
    // given
    EmployeeRecord manager = new EmployeeRecord(1, "Alice", "Smith", 40000, null);
    EmployeeRecord subordinate1 = new EmployeeRecord(2, "Bob", "Jones", 30000, 1);
    EmployeeRecord subordinate2 = new EmployeeRecord(3, "Charlie", "Brown", 40000, 1);

    manager.addSubordinate(subordinate1);
    manager.addSubordinate(subordinate2);

    employees.put(1, manager);
    employees.put(2, subordinate1);
    employees.put(3, subordinate2);
    String expectedOutput = "Alice Smith earns less than they should by 2000.00\r\n";

    // when
    String output = analyzer.analyzeSalaries(employees);

    // then
    assertEquals(expectedOutput, output);
  }

  @Test
  void testOverPaidManager() {
    // given
    EmployeeRecord manager = new EmployeeRecord(1, "Alice", "Smith", 55000, null);
    EmployeeRecord subordinate1 = new EmployeeRecord(2, "Bob", "Jones", 30000, 1);
    EmployeeRecord subordinate2 = new EmployeeRecord(3, "Charlie", "Brown", 40000, 1);

    manager.addSubordinate(subordinate1);
    manager.addSubordinate(subordinate2);

    employees.put(1, manager);
    employees.put(2, subordinate1);
    employees.put(3, subordinate2);

    String expectedOutput = "Alice Smith earns more than they should by 2500.00\r\n";

    // when
    String output = analyzer.analyzeSalaries(employees);

    // then
    assertEquals(expectedOutput, output);
  }
}