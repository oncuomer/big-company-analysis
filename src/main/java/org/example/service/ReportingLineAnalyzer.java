package org.example.service;

import java.util.Map;
import org.example.model.Employee;

public class ReportingLineAnalyzer {


  /**
   * The maximum allowed depth for a reporting line.
   */
  private static final int MAXIMUM_DEPTH = 4;

  /**
   * Checks each employee's reporting line to see if it's too long. This method creates a report
   * that lists employees whose number of reporting levels exceeds the allowed limit of 4. For each
   * such employee, the report includes their ID, first name, last name, and how many levels they
   * are over the limit.
   *
   * @param employees A map with employee IDs as keys and Employee objects as values, representing
   *                  all employees in the organization.
   * @return A string listing all employees with reporting lines that are too long, and by how many
   * levels they exceed the limit.
   */
  public String analyzeReportingLines(Map<Integer, Employee> employees) {
    StringBuilder report = new StringBuilder();

    employees.values().forEach(employee -> {
      int depth = calculateReportingLineDepth(employee, employees);
      if (depth > MAXIMUM_DEPTH) {
        appendReport(report, employee, depth - 4);
      }
    });

    return report.toString();
  }

  private int calculateReportingLineDepth(Employee employee, Map<Integer, Employee> employees) {
    int depth = 0;
    Employee current = employee;

    while (current.getManagerId() != null && employees.containsKey(current.getManagerId())) {
      current = employees.get(current.getManagerId());
      depth++;
    }
    return depth;
  }

  private void appendReport(StringBuilder report, Employee employee, int excessDepth) {
    report.append(String.format("%d|%s %s has a reporting line that is too long by %d levels%n",
        employee.getId(),
        employee.getFirstName(),
        employee.getLastName(),
        excessDepth));
  }
}
