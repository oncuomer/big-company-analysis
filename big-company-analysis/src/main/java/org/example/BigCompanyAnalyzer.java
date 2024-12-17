package org.example;

import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;

public class BigCompanyAnalyzer {

  public static void main(String[] args) {
    String filePath = "src/main/resources/employees.csv";

    EmployeeRepository repository = new EmployeeRepository();
    SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
    ReportingLineAnalyzer lineAnalyzer = new ReportingLineAnalyzer();

    ApplicationRunner runner = new ApplicationRunner(repository, salaryAnalyzer, lineAnalyzer);

    runner.run(filePath);
  }
}