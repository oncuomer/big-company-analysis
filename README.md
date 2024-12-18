# Big Company Analysis Project

## Overview

This project provides a toolkit for analyzing employee data within large organizations. It includes
functionalities to parse employee data from CSV files, analyze
salaries, and determine the reporting lines of each employee up to the CEO.

## Features

- **Employee Data Parsing**: Read and parse data from a CSV file to create a detailed map of the
  organization's employees.
- **Salary Analysis**: Calculates the average salary of all direct subordinates and specifically
  analyzes managerial salaries to ensure compliance with defined salary standards. The application
  will report any manager whose salary:
    - Is less than 20% above the average salary of their direct subordinates.
    - Exceeds 50% above the average salary of their direct subordinates.
- **Reporting Line Trace**: Determine and display the reporting hierarchy for any given employee.

## Structure

The project consists of several key components organized into packages:

1. **Model**
    - `Employee` - Represents a single employee's data.

2. **Repository**
    - `EmployeeRepository` - Handles parsing of the CSV file and storage of employee records.

3. **Analyzer**
    - `ReportingLineAnalyzer` - Analyzes and traces reporting lines within the organization to
      ensure they are not excessively long.
    - `SalaryAnalyzer` -Handles the evaluation of managers' salaries against their team's average.
      It identifies managers who are paid significantly more or less than their team.

4. **Main Application**
    - `BigCompanyAnalysis` - The starting point of the program, bringing together all components and
      executes `EmployeeAnalyzerRunner`

## Getting Started

To run this project, you will need to:

1. Ensure you have Java (version 17 or later) installed on your computer.
2. Download or clone this repository to your local machine.
3. Place the CSV file containing employee data in a known directory.
4. Update the `filePath` variable in `BigCompanyAnalysis.java` with the path to your CSV file.

## Running the Program

Once you've set up the environment. Navigate to the directory containing the project using a
terminal or command line interface.

#### 1. Ensure you have Maven installed on your system. You can verify this by running the following

command in your terminal or command line interface:

```bash
mvn -v
```

#### 2. Compile the project and run tests

Execute the following command to clean the target/ directory, compile the source code into
target/classes, and run any tests configured in the project:

```bash
mvn clean package
```

#### 3. Use Maven to start the application by running

This application requires a CSV file with employee data to run. You can either specify a path to
your custom CSV file or use the default CSV file provided with the application.

##### Using a Custom CSV File

To specify your own CSV file, add the `-Dexec.args` option followed by the file path when you launch
the application. Here is how you can do it:

```bash
mvn exec:java -D"exec.args"="src/main/resources/employees.csv" 
```

##### Using the Default CSV File

If you do not specify a custom file, the application will automatically use the default CSV file
located at src/main/resources/employees.csv.
To run the application using the default file, use the following command:

```bash
mvn exec:java
```