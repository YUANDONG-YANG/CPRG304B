package com.sait.oop3.exercise3;

import java.util.Random;
import java.util.Arrays;
import java.util.Scanner;
import java.util.HashSet;
import java.util.Set;

public class AppDriver {

    public static final int SIZE = 100;
    public static final int UPPER_BOUND = 1000;

    public static void main(String[] args) {

        Random rand = new Random();
        Integer[] original = new Integer[SIZE];
        for (int i = 0; i < SIZE; i++) {
            original[i] = rand.nextInt(UPPER_BOUND);
        }

        System.out.println("Original array:");
        System.out.println(Arrays.toString(original));

        Scanner scanner = new Scanner(System.in);
        Set<Character> tried = new HashSet<>();

        while (tried.size() < 4) {
            System.out.print("\nEnter sorting choice (b=Bubble, i=Insertion, s=Selection, q=QuickSort): ");
            char choice = Character.toLowerCase(scanner.nextLine().charAt(0));

            if (tried.contains(choice)) {
                System.out.println("You have already tried this algorithm. Please choose another one.");
                continue;
            }

            Integer[] nums = Arrays.copyOf(original, original.length);

            switch (choice) {
                case 'b':
                    bubbleSort(nums);
                    System.out.println("\nSorted array using Bubble Sort:");
                    break;
                case 'i':
                    insertionSort(nums);
                    System.out.println("\nSorted array using Insertion Sort:");
                    break;
                case 's':
                    selectionSort(nums);
                    System.out.println("\nSorted array using Selection Sort:");
                    break;
                case 'q':
                    quickSort(nums, 0, nums.length - 1);
                    System.out.println("\nSorted array using QuickSort:");
                    break;
                default:
                    System.out.println("Invalid input. Use b, i, s, or q.");
                    continue;
            }

            System.out.println(Arrays.toString(nums));
            tried.add(choice);

            Set<Character> remaining = new HashSet<>(Arrays.asList('b', 'i', 's', 'q'));
            remaining.removeAll(tried);
            if (!remaining.isEmpty()) {
                System.out.print("Algorithms remaining to try: ");
                for (char c : remaining) {
                    System.out.print(c + " ");
                }
                System.out.println();
            } else {
                System.out.println("\nAll algorithms have been tried. Program finished.");
            }
        }

        scanner.close();
    }

    // Bubble Sort
    public static void bubbleSort(Integer[] arr) {
        int n = arr.length;
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
    }

    // Insertion Sort
    public static void insertionSort(Integer[] arr) {
        for (int i = 1; i < arr.length; i++) {
            int key = arr[i];
            int j = i - 1;
            while (j >= 0 && arr[j] > key) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = key;
        }
    }

    // Selection Sort
    public static void selectionSort(Integer[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            int minIndex = i;
            for (int j = i + 1; j < arr.length; j++)
                if (arr[j] < arr[minIndex])
                    minIndex = j;

            int temp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = temp;
        }
    }

    // QuickSort
    public static void quickSort(Integer[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);
            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    private static int partition(Integer[] arr, int low, int high) {
        int pivot = arr[high];
        int i = low - 1;
        for (int j = low; j < high; j++) {
            if (arr[j] <= pivot) {
                i++;
                int temp = arr[i];
                arr[i] = arr[j];
                arr[j] = temp;
            }
        }
        int temp = arr[i + 1];
        arr[i + 1] = arr[high];
        arr[high] = temp;
        return i + 1;
    }
}
