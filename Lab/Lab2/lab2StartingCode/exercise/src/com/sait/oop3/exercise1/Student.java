package com.sait.oop3.exercise1;

import java.util.Comparator;

public class Student implements Comparable<Student>, Comparator<Student> {
    private String name;
    private int age;

    // Constructor
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Student() {
        
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    // compareTo method for Comparable (compare by name)
    @Override
    public int compareTo(Student other) {
        return this.name.compareTo(other.name);
    }

    @Override
    public int compare(Student s1, Student s2) {
        if (s1.getAge() != s2.getAge()) {
            return Integer.compare(s1.getAge(), s2.getAge());
        } else {
            return s1.getName().compareTo(s2.getName());
        }
    }

    // toString for easy printing
    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + "}";
    }

    public String toJson() {
        return "{ \"name\": \"" + name + "\", \"age\": " + age + " }";
    }
}
