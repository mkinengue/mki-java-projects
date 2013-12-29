package fr.mkinengue.training.designpatterns.composite;

import java.util.ArrayList;
import java.util.List;


public class Employee {

    private String name;
    private double salary;
    private List<Employee> subordinates;


    // constructor
    public Employee(String name, double sal) {
        setName(name);
        setSalary(sal);
        subordinates = new ArrayList<Employee>();
    }

    public List<Employee> getSubordinates() {
        return subordinates;
    }

    public void setSubordinates(List<Employee> subordinates) {
        this.subordinates = subordinates;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public void add(Employee e) {
        subordinates.add(e);
    }

    public void remove(Employee e) {
        subordinates.remove(e);
    }
}
