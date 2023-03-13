package main.java.com.median.finder;

import java.util.Scanner;

public class Main {
    /**
     * It takes in the file and
     * @param args
     */

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter relative file path or absolute file path: ");
        String fileName = scanner.nextLine();
        DistributedMedianFinder distributedMedianFinder = new DistributedMedianFinder(fileName);
        System.out.println(distributedMedianFinder.findMedian());
    }
}