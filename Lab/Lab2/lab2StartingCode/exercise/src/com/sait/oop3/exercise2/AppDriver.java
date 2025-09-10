package com.sait.oop3.exercise2;

import java.util.Random;
import java.util.Scanner;

public class AppDriver {

    public static final int SIZE = 100;
    public static final int UPPER_BOUND = 10;

    // Static method to perform binary search
    public static int binarySearch(Integer[] arr, int target) {
        int left = 0;
        int right = arr.length - 1;
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (arr[mid] == target) {
                return mid; // Found target
            } else if (arr[mid] < target) {
                left = mid + 1; // Search right half
            } else {
                right = mid - 1; // Search left half
            }
        }
        return -1; // Not found
    }

    public static void main(String[] args) {
        Integer[] nums = new Integer[SIZE];
        Random rand = new Random();
        int randnum = rand.nextInt(UPPER_BOUND);
        nums[0] = randnum;
        for (int i = 1; i < SIZE; i++) {
            randnum = rand.nextInt(UPPER_BOUND);
            nums[i] = nums[i - 1] + randnum;
            System.out.println(nums[i]);
        }
        // Display the array
        System.out.println("Sorted array:");
        for (int n : nums) {
            System.out.print(n + " ");
        }
        System.out.println();
        // Prompt user for target
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nEnter an integer to search for: ");
        int target = scanner.nextInt();
        // Call binary search
        int index = binarySearch(nums, target);
        if (index != -1) {
            System.out.println("Target " + target + " found at index " + index + ".");
        } else {
            System.out.println("Target " + target + " not found in the array.");
        }
        scanner.close();
    }

}
