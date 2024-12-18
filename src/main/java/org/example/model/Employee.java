package org.example.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employee of BigCompany.
 */
public class Employee {

  private final int id;
  private final String firstName;
  private final String lastName;
  private final double salary;
  private final Integer managerId;
  private List<Employee> subordinates;

  public Employee(int id, String firstName, String lastName, double salary, Integer managerId) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.salary = salary;
    this.managerId = managerId;
    this.subordinates = new ArrayList<>();
  }

  public void addSubordinate(Employee subordinate) {
    if (!this.subordinates.contains(subordinate)) {
      this.subordinates.add(subordinate);
    }
  }

  public int getId() {
    return id;
  }

  public String getFirstName() {
    return firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public double getSalary() {
    return salary;
  }

  public Integer getManagerId() {
    return managerId;
  }

  public List<Employee> getSubordinates() {
    return subordinates;
  }
}
