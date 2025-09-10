package com.sait.oop3.exercise1;

import java.util.ArrayList;
import java.util.Collections;

public class AppDriver {

    public static void main(String[] args) {

        ArrayList<Student> studs = new ArrayList<>();
        studs.add(new Student("Smith", 34));
        studs.add(new Student("Johnson", 21));
        studs.add(new Student("Williams", 67));
        studs.add(new Student("Brown", 53));
        studs.add(new Student("Jones", 48));
        studs.add(new Student("Miller", 36));
        studs.add(new Student("Davis", 44));
        studs.add(new Student("Wilson", 52));
        studs.add(new Student("Anderson", 34));
        studs.add(new Student("Moore", 33));

        System.out.println("Before sorting:");
        printJsonList(studs);

        Collections.sort(studs);
        System.out.println("After sorting by name:");
        printJsonList(studs);

        studs.sort(new Student());
        System.out.println("After sorting by age (then name):");
        printJsonList(studs);

    }

    private static void printJsonList(ArrayList<Student> list) {
        System.out.println("[");
        for (int i = 0; i < list.size(); i++) {
            System.out.print("  " + list.get(i).toJson());
            if (i != list.size() - 1) {
                System.out.print(",");
            }
            System.out.println();
        }
        System.out.println("]");
    }
}
