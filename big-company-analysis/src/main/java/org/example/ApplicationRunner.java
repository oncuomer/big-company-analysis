package org.example;

import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;

public class ApplicationRunner {
  private EmployeeRepository repository;
  private SalaryAnalyzer salaryAnalyzer;
  private ReportingLineAnalyzer lineAnalyzer;

  public ApplicationRunner(
      EmployeeRepository repository,
      SalaryAnalyzer salaryAnalyzer,
      ReportingLineAnalyzer lineAnalyzer) {
    this.repository = repository;
    this.salaryAnalyzer = salaryAnalyzer;
    this.lineAnalyzer = lineAnalyzer;
  }

  public void run(String filePath) {
    try {
      var employees = repository.createEmployeesMap(filePath);

      String salaryReport = salaryAnalyzer.analyzeSalaries(employees);
      System.out.println(salaryReport);

      String lineReport = lineAnalyzer.analyzeReportingLines(employees);
      System.out.println(lineReport);

    } catch (Exception e) {
      System.err.println("Error processing employee data: " + e.getMessage());
      e.printStackTrace();
    }
  }
}