package org.example.service;

import java.util.Map;
import org.example.model.EmployeeRecord;

public class SalaryAnalyzer {

  public String analyzeSalaries(Map<Integer, EmployeeRecord> employees) {
    StringBuilder report = new StringBuilder();

    employees.values().forEach(manager -> {
      if (!manager.getSubordinates().isEmpty()) {
        double avgSalary = getAverageSalary(manager);
        double minSalary = avgSalary * 1.20;
        double maxSalary = avgSalary * 1.50;

        generateReportLine(report, manager, minSalary, maxSalary);
      }
    });

    return report.toString();
  }

  private void generateReportLine(
      StringBuilder report,
      EmployeeRecord manager,
      double minSalary,
      double maxSalary) {
    double salary = manager.getSalary();
    if (salary < minSalary) {
      report.append(
          String.format("%s %s earns less than they should by %.2f%n",
              manager.getFirstName(), manager.getLastName(), minSalary - salary));
    }
    if (salary > maxSalary) {
      report.append(
          String.format("%s %s earns more than they should by %.2f%n",
              manager.getFirstName(), manager.getLastName(), salary - maxSalary));
    }
  }

  private double getAverageSalary(EmployeeRecord manager) {
    return manager.getSubordinates()
        .stream()
        .mapToDouble(EmployeeRecord::getSalary)
        .average()
        .orElse(0);
  }
}
