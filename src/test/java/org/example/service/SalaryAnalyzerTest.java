package org.example.service;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;
import org.example.model.Employee;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SalaryAnalyzerTest {

  private SalaryAnalyzer analyzer;
  private Map<Integer, Employee> employees;

  @BeforeEach
  void setup() {
    employees = new HashMap<>();
    analyzer = new SalaryAnalyzer();
  }

  @Test
  void testUnderPaidManager() {
    // given
    Employee manager = new Employee(1, "Alice", "Smith", 40000, null);
    Employee subordinate1 = new Employee(2, "Bob", "Jones", 30000, 1);
    Employee subordinate2 = new Employee(3, "Charlie", "Brown", 40000, 1);

    manager.addSubordinate(subordinate1);
    manager.addSubordinate(subordinate2);

    employees.put(1, manager);
    employees.put(2, subordinate1);
    employees.put(3, subordinate2);
    String expectedOutput = "1|Alice Smith earns less than they should by 2000.00\r\n";

    // when
    String output = analyzer.analyzeSalaries(employees);

    // then
    assertEquals(expectedOutput, output);
  }

  @Test
  void testOverPaidManager() {
    // given
    Employee manager = new Employee(1, "Alice", "Smith", 55000, null);
    Employee subordinate1 = new Employee(2, "Bob", "Jones", 30000, 1);
    Employee subordinate2 = new Employee(3, "Charlie", "Brown", 40000, 1);

    manager.addSubordinate(subordinate1);
    manager.addSubordinate(subordinate2);

    employees.put(1, manager);
    employees.put(2, subordinate1);
    employees.put(3, subordinate2);

    String expectedOutput = "1|Alice Smith earns more than they should by 2500.00\r\n";

    // when
    String output = analyzer.analyzeSalaries(employees);

    // then
    assertEquals(expectedOutput, output);
  }
}