package org.example;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Map;
import org.example.model.EmployeeRecord;
import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ApplicationRunnerTest {

  private static final String FILE_PATH = "mockPath/employees.csv";
  @Mock
  private EmployeeRepository employeeRepository;
  @Mock
  private SalaryAnalyzer salaryAnalyzer;
  @Mock
  private ReportingLineAnalyzer reportingLineAnalyzer;

  private ApplicationRunner runner;

  @BeforeEach
  void setUp() {
    runner = new ApplicationRunner(employeeRepository, salaryAnalyzer,
        reportingLineAnalyzer);
  }


  @Test
  void testRun() throws Exception {

    // given
    Map<Integer, EmployeeRecord> mockEmployees = mock(Map.class);
    when(employeeRepository.createEmployeesMap(any(String.class))).thenReturn(mockEmployees);
    when(salaryAnalyzer.analyzeSalaries(anyMap())).thenReturn("Fake Salary Report");
    when(reportingLineAnalyzer.analyzeReportingLines(anyMap())).thenReturn("Fake Line Report");

    ByteArrayOutputStream sysOut = new ByteArrayOutputStream();
    System.setOut(new PrintStream(sysOut));

    // when
    runner.run(FILE_PATH);

    // then
    String output = sysOut.toString();
    assertTrue(output.contains("Fake Salary Report"));
    assertTrue(output.contains("Fake Line Report"));

    verify(employeeRepository).createEmployeesMap(FILE_PATH);
    verify(salaryAnalyzer).analyzeSalaries(mockEmployees);
    verify(reportingLineAnalyzer).analyzeReportingLines(mockEmployees);
  }

  @Test
  void testRunHandlesExceptionsProperly() throws IOException {
    // given
    when(employeeRepository.createEmployeesMap(anyString())).thenThrow(
        new RuntimeException("Failed to load data"));

    ByteArrayOutputStream errContent = new ByteArrayOutputStream();
    System.setErr(new PrintStream(errContent));

    // when
    runner.run("path/to/nonexistent/file.csv");

    // then
    String expectedErrorMessage = "Error processing employee data: Failed to load data";
    assertTrue(errContent.toString().contains(expectedErrorMessage));

    verify(employeeRepository).createEmployeesMap("path/to/nonexistent/file.csv");
  }
}