package org.example.service;

import java.util.Map;
import org.example.model.EmployeeRecord;

public class ReportingLineAnalyzer {
  public String analyzeReportingLines(Map<Integer, EmployeeRecord> employees) {
    StringBuilder report = new StringBuilder();

    employees.values().forEach(employee -> {
      int depth = 0;
      EmployeeRecord current = employee;
      while (current.getManagerId() != null && employees.containsKey(current.getManagerId())) {
        current = employees.get(current.getManagerId());
        depth++;
      }
      if (depth > 4) {
        report.append(String.format("%s has a reporting line that is too long by %d levels%n",
            employee.getFirstName(), depth - 4));
      }
    });

    return report.toString();
  }
}
