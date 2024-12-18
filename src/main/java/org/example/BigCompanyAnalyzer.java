package org.example;

import java.util.Optional;
import org.example.exception.FileReaderException;
import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;

public class BigCompanyAnalyzer {

  public static void main(String[] args) {
    String filePath = getValidatedFilePath(args);

    EmployeeRepository repository = new EmployeeRepository();
    SalaryAnalyzer salaryAnalyzer = new SalaryAnalyzer();
    ReportingLineAnalyzer lineAnalyzer = new ReportingLineAnalyzer();

    EmployeeAnalyzerRunner runner = new EmployeeAnalyzerRunner(repository, salaryAnalyzer,
        lineAnalyzer);
    runner.run(filePath);
  }

  private static String getValidatedFilePath(String[] args) {
    return Optional.ofNullable(args)
        .filter(a -> a.length == 1)
        .map(a -> a[0])
        .filter(f -> f.endsWith(".csv"))
        .orElseThrow(
            () -> new FileReaderException("Error: You must pass exactly one .csv file."));
  }

}