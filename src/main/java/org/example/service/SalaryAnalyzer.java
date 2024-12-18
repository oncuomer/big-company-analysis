package org.example.service;

import java.util.Map;
import org.example.model.Employee;

public class SalaryAnalyzer {

  /**
   * The minimum percentage difference between a manager's salary and the average salary of their
   * subordinates considered acceptable.
   */
  private static final double MIN_PERCENTAGE_DIFF = 1.20;
  /**
   * The maximum percentage difference between a manager's salary and the average salary of their
   * subordinates considered acceptable.
   */
  private static final double MAX_PERCENTAGE_DIFF = 1.50;


  /**
   * Analyzes salaries of managers to compare their salary against the calculated salary range based
   * on their subordinates average salaries. Generates a report indicating which managers are
   * earning significantly less or more than expected.
   *
   * @param employees A map of employee IDs to Employee objects. Each employee object contains data
   *                  about their salary and any subordinates.
   * @return A string report listing managers who earn too much or too little compared to their
   * subordinates' average salary.
   */
  public String analyzeSalaries(Map<Integer, Employee> employees) {
    StringBuilder report = new StringBuilder();
    employees.values().stream()
        .filter(manager -> !manager.getSubordinates().isEmpty())
        .forEach(manager -> evaluateManagerSalary(manager, report));
    return report.toString();
  }

  private void evaluateManagerSalary(Employee manager, StringBuilder report) {
    double avgSalary = calculateAverageSubordinateSalary(manager);
    double minSalary = avgSalary * MIN_PERCENTAGE_DIFF;
    double maxSalary = avgSalary * MAX_PERCENTAGE_DIFF;

    generateReportLine(manager, minSalary, maxSalary, report);
  }

  private void generateReportLine(Employee manager, double minSalary, double maxSalary, StringBuilder report) {
    double salary = manager.getSalary();

    if (salary < minSalary) {
      report.append(String.format("%d|%s %s earns less than they should by %.2f%n",
          manager.getId(), manager.getFirstName(), manager.getLastName(), minSalary - salary));
    }
    if (salary > maxSalary) {
      report.append(String.format("%d|%s %s earns more than they should by %.2f%n",
          manager.getId(), manager.getFirstName(), manager.getLastName(), salary - maxSalary));
    }
  }

  private double calculateAverageSubordinateSalary(Employee manager) {
    return manager.getSubordinates().stream()
        .mapToDouble(Employee::getSalary)
        .average()
        .orElse(0);
  }
}
