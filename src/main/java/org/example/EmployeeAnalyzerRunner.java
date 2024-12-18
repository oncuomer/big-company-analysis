package org.example;

import java.util.Objects;
import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;


public class EmployeeAnalyzerRunner {

  private final EmployeeRepository repository;
  private final SalaryAnalyzer salaryAnalyzer;
  private final ReportingLineAnalyzer lineAnalyzer;

  public EmployeeAnalyzerRunner(
      EmployeeRepository repository,
      SalaryAnalyzer salaryAnalyzer,
      ReportingLineAnalyzer lineAnalyzer) {
    this.repository = Objects.requireNonNull(repository,
        "EmployeeRepository must not be null");
    this.salaryAnalyzer = Objects.requireNonNull(salaryAnalyzer,
        "SalaryAnalyzer must not be null");
    this.lineAnalyzer = Objects.requireNonNull(lineAnalyzer,
        "ReportingLineAnalyzer must not be null");
  }

  /**
   * Starts the analysis of employee data from a file. This method handles the overall functioning
   * by going through several defined steps:
   * 1.It first reads employee information from a CSV file.
   * 2.It examines if there are any issues with how employees are paid and provides a report.
   * 2.it checks if any employee's reporting line (who they report to) is too long and gives a
   * report on this.
   * The method provides clear messages on the console about what it is currently
   * doing. If something goes wrong, such as an issue with reading the file or handling the data, it
   * will show an error message and stop the process, indicating where the problem happened.
   *
   * @param filePath The path to the CSV file. This should be a valid path that the program can
   *                 read.
   * @throws RuntimeException if there are issues accessing or processing the file.
   */
  public void run(String filePath) {
    try {
      System.out.println("===== RUNNING BIG COMPANY ANALYZE =====");

      System.out.printf("----- Reading employees from CSV file: %s -----%n", filePath);
      var employees = repository.createEmployeesMap(filePath);
      System.out.printf("Found %d employees %n", employees.size());

      System.out.printf("----- Generating Salary report with salary violations -----%n");
      String salaryReport = salaryAnalyzer.analyzeSalaries(employees);
      System.out.println(salaryReport);

      System.out.println("----- Generating excessive reporting lines -----");
      String lineReport = lineAnalyzer.analyzeReportingLines(employees);
      System.out.println(lineReport);
      System.out.println("===== ANALYZE DONE! =====");

    } catch (RuntimeException e) {
      System.err.println("Error processing employee data: " + e.getMessage());
      throw e;
    }
  }

}
