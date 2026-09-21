import java.util.*;

public class Permutations {

    public static void main(String[] args) {
        int[] arr1 = {1, 2, 3};
        int[] arr2 = {1, 2, 3};

        System.out.println("--- Recursive Permutations ---");
        permuteRecursive(arr1, 0);

        System.out.println("\n--- Non-Recursive Permutations ---");
        permuteNonRecursive(arr2);
    }

    // 1. Recursive (Backtracking)
    static void permuteRecursive(int[] arr, int index) {
        if (index == arr.length) {
            System.out.println(Arrays.toString(arr));
            return;
        }

        for (int i = index; i < arr.length; i++) {
            swap(arr, index, i);
            permuteRecursive(arr, index + 1);
            swap(arr, index, i); // Backtrack
        }
    }

    // 2. Non-Recursive (Lexicographical / Narayana Pandita's)
    static void permuteNonRecursive(int[] arr) {
        Arrays.sort(arr);
        System.out.println(Arrays.toString(arr));

        while (true) {
            // Step A: Find largest index i such that arr[i] < arr[i + 1]
            int i = arr.length - 2;
            while (i >= 0 && arr[i] >= arr[i + 1]) {
                i--;
            }

            if (i < 0) break; // All permutations generated

            // Step B: Find largest index j such that arr[j] > arr[i]
            int j = arr.length - 1;
            while (arr[j] <= arr[i]) {
                j--;
            }

            // Step C: Swap arr[i] and arr[j]
            swap(arr, i, j);

            // Step D: Reverse subarray from i + 1 to end
            reverse(arr, i + 1, arr.length - 1);

            System.out.println(Arrays.toString(arr));
        }
    }

    static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static void reverse(int[] arr, int start, int end) {
        while (start < end) {
            swap(arr, start, end);
            start++;
            end--;
        }
    }
}