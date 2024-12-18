package org.example;

import static org.example.Constants.VALID_HEADERS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.example.exception.EmployeeValidationException;
import org.example.repository.EmployeeRepository;
import org.example.service.ReportingLineAnalyzer;
import org.example.service.SalaryAnalyzer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class EmployeeAnalyzerRunnerTest {

  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private final PrintStream originalErr = System.err;

  private EmployeeAnalyzerRunner employeeAnalyzerRunner;
  private EmployeeRepository repository;
  private SalaryAnalyzer salaryAnalyzer;
  private ReportingLineAnalyzer lineAnalyzer;

  @BeforeEach
  void setUp() {
    System.setOut(new PrintStream(outContent));
    System.setErr(new PrintStream(errContent));

    repository = new EmployeeRepository();
    salaryAnalyzer = new SalaryAnalyzer();
    lineAnalyzer = new ReportingLineAnalyzer();
    employeeAnalyzerRunner = new EmployeeAnalyzerRunner(repository, salaryAnalyzer, lineAnalyzer);
  }

  @AfterEach
  public void restoreStreams() {
    System.setOut(originalOut);
    System.setErr(originalErr);
  }

  @Test
  void testRunMethod() throws Exception {
    // given
    Path tempFile = Files.createTempFile("employees", ".csv");
    String filePath = tempFile.toString();
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,Jane,Smith,70000,",
        "2,John,Doe,55000,1",
        "3,Martin,Chekov,40000,2",
        "4,Bob,Ronstad,30000,3",
        "5,Alice,Hasacat,20000,4",
        "6,Brett,Hardleaf,20000,5"
    ));

    // when
    employeeAnalyzerRunner.run(filePath);

    // then
    String output = outContent.toString();

    List<String> lines = extractLogLines(output);

    assertTrue(lines.size() > 7);
    assertEquals("===== RUNNING BIG COMPANY ANALYZE =====", lines.get(0));
    assertEquals(String.format("----- Reading employees from CSV file: %s -----", filePath),
        lines.get(1));
    assertEquals("Found 6 employees", lines.get(2));
    assertEquals("----- Generating Salary report with salary violations -----", lines.get(3));
    assertEquals("5|Alice Hasacat earns less than they should by 4000.00", lines.get(4));
    assertEquals("----- Generating excessive reporting lines -----", lines.get(5));
    assertEquals("6|Brett Hardleaf has a reporting line that is too long by 1 levels",
        lines.get(6));
    assertEquals("===== ANALYZE DONE! =====", lines.get(7));
  }

  @Test
  void testRunnerException() throws Exception {
    // given
    Path tempFile = Files.createTempFile("invalidEmployees", ".csv");
    String filePath = tempFile.toString();
    Files.write(tempFile, Arrays.asList(
        VALID_HEADERS,
        "1,Jane,Smith,70000,INVALID"
    ));

    // when
    EmployeeValidationException ex = assertThrows(EmployeeValidationException.class, () ->
        employeeAnalyzerRunner.run(filePath));

    // then
    assertEquals("Error parsing number on line 2: For input string: \"INVALID\"",
        ex.getMessage());
  }

  @Test
  void testConstructorThrowsNullPointerExceptionIfRepositoryIsNull() {
    // when
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        new EmployeeAnalyzerRunner(null, salaryAnalyzer, lineAnalyzer)
    );

    // then
    assertEquals("EmployeeRepository must not be null", ex.getMessage());
  }

  @Test
  void testConstructorThrowsNullPointerExceptionIfSalaryAnalyzerIsNull() {
    // when
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        new EmployeeAnalyzerRunner(repository, null, lineAnalyzer)
    );

    // then
    assertEquals("SalaryAnalyzer must not be null", ex.getMessage());
  }

  @Test
  void testConstructorThrowsNullPointerExceptionIfLineAnalyzerIsNull() {
    // when
    NullPointerException ex = assertThrows(NullPointerException.class, () ->
        new EmployeeAnalyzerRunner(repository, salaryAnalyzer, null)
    );

    // then
    assertEquals("ReportingLineAnalyzer must not be null", ex.getMessage());
  }

  private List<String> extractLogLines(String output) {
    return Arrays.stream(output.split("\\R"))
        .map(String::trim)
        .filter(line -> !line.isEmpty())
        .collect(Collectors.toList());
  }
}