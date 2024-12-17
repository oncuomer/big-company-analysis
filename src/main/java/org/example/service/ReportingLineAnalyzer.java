package org.example.service;

import java.util.HashMap;
import java.util.Map;
import org.example.model.EmployeeRecord;

public class ReportingLineAnalyzer {
public String analyzeReportingLines(Map<Integer, EmployeeRecord> employees) {
    StringBuilder report = new StringBuilder();
    Map<Integer, Integer> depthCache = new HashMap<>();

    employees.values().forEach(employee -> {
      int depth = calculateReportingLineDepth(employee, employees, depthCache);
      if (depth > 4) {
        appendReport(report, employee, depth - 4);
      }
    });

    return report.toString();
  }

  private int calculateReportingLineDepth(EmployeeRecord employee,
      Map<Integer, EmployeeRecord> employees,
      Map<Integer, Integer> depthCache) {
    if (depthCache.containsKey(employee.getId())) {
      return depthCache.get(employee.getId());
    }

    int depth = 0;
    EmployeeRecord current = employee;

    while (current.getManagerId() != null && employees.containsKey(current.getManagerId())) {
      if (depthCache.containsKey(current.getManagerId())) {
        depth += depthCache.get(current.getManagerId()) + 1;
        break;
      }
      current = employees.get(current.getManagerId());
      depth++;
    }

    depthCache.put(employee.getId(), depth);
    return depth;
  }

  private void appendReport(StringBuilder report, EmployeeRecord employee, int excessDepth) {
    report.append(String.format("%s %s has a reporting line that is too long by %d levels%n",
        employee.getFirstName(), employee.getLastName(), excessDepth));
  }
}
