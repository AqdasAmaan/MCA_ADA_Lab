import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class MatrixMultiplication {

    /*
     * ============================================================
     * 1. DIRECT METHOD
     * ============================================================
     *
     * Standard O(n^3) matrix multiplication:
     *
     * C[i][j] = Σ A[i][k] * B[k][j]
     */
    public static int[][] directMultiply(int[][] A, int[][] B) {

        int n = A.length;
        int[][] C = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int k = 0; k < n; k++) {

                for (int j = 0; j < n; j++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }

        return C;
    }


    /*
     * ============================================================
     * 2. DIVIDE & CONQUER
     * ============================================================
     *
     * Divide each matrix into four submatrices:
     *
     * A = [A11 A12]
     *     [A21 A22]
     *
     * B = [B11 B12]
     *     [B21 B22]
     *
     * Then:
     *
     * C11 = A11B11 + A12B21
     * C12 = A11B12 + A12B22
     * C21 = A21B11 + A22B21
     * C22 = A21B12 + A22B22
     */
    public static int[][] divideAndConquerMultiply(int[][] A, int[][] B) {

        int n = A.length;

        // Base case
        if (n == 1) {
            return new int[][]{
                    {A[0][0] * B[0][0]}
            };
        }

        int newSize = n / 2;

        // Divide A
        int[][] A11 = new int[newSize][newSize];
        int[][] A12 = new int[newSize][newSize];
        int[][] A21 = new int[newSize][newSize];
        int[][] A22 = new int[newSize][newSize];

        // Divide B
        int[][] B11 = new int[newSize][newSize];
        int[][] B12 = new int[newSize][newSize];
        int[][] B21 = new int[newSize][newSize];
        int[][] B22 = new int[newSize][newSize];

        splitMatrix(A, A11, 0, 0);
        splitMatrix(A, A12, 0, newSize);
        splitMatrix(A, A21, newSize, 0);
        splitMatrix(A, A22, newSize, newSize);

        splitMatrix(B, B11, 0, 0);
        splitMatrix(B, B12, 0, newSize);
        splitMatrix(B, B21, newSize, 0);
        splitMatrix(B, B22, newSize, newSize);

        // Recursive multiplications
        int[][] M1 = divideAndConquerMultiply(A11, B11);
        int[][] M2 = divideAndConquerMultiply(A12, B21);
        int[][] M3 = divideAndConquerMultiply(A11, B12);
        int[][] M4 = divideAndConquerMultiply(A12, B22);
        int[][] M5 = divideAndConquerMultiply(A21, B11);
        int[][] M6 = divideAndConquerMultiply(A22, B21);
        int[][] M7 = divideAndConquerMultiply(A21, B12);
        int[][] M8 = divideAndConquerMultiply(A22, B22);

        // Calculate C submatrices
        int[][] C11 = add(M1, M2);
        int[][] C12 = add(M3, M4);
        int[][] C21 = add(M5, M6);
        int[][] C22 = add(M7, M8);

        // Combine them
        return combineMatrices(C11, C12, C21, C22);
    }


    /*
     * ============================================================
     * 3. STRASSEN'S METHOD
     * ============================================================
     *
     * Strassen reduces the number of recursive multiplications
     * from 8 to 7.
     *
     * P1 = (A11 + A22)(B11 + B22)
     * P2 = (A21 + A22)B11
     * P3 = A11(B12 - B22)
     * P4 = A22(B21 - B11)
     * P5 = (A11 + A12)B22
     * P6 = (A21 - A11)(B11 + B12)
     * P7 = (A12 - A22)(B21 + B22)
     *
     * C11 = P1 + P4 - P5 + P7
     * C12 = P3 + P5
     * C21 = P2 + P4
     * C22 = P1 - P2 + P3 + P6
     */
    public static int[][] strassenMultiply(int[][] A, int[][] B) {

        int n = A.length;

        // Base case
        if (n == 1) {
            return new int[][]{
                    {A[0][0] * B[0][0]}
            };
        }

        int newSize = n / 2;

        // Divide A
        int[][] A11 = new int[newSize][newSize];
        int[][] A12 = new int[newSize][newSize];
        int[][] A21 = new int[newSize][newSize];
        int[][] A22 = new int[newSize][newSize];

        // Divide B
        int[][] B11 = new int[newSize][newSize];
        int[][] B12 = new int[newSize][newSize];
        int[][] B21 = new int[newSize][newSize];
        int[][] B22 = new int[newSize][newSize];

        splitMatrix(A, A11, 0, 0);
        splitMatrix(A, A12, 0, newSize);
        splitMatrix(A, A21, newSize, 0);
        splitMatrix(A, A22, newSize, newSize);

        splitMatrix(B, B11, 0, 0);
        splitMatrix(B, B12, 0, newSize);
        splitMatrix(B, B21, newSize, 0);
        splitMatrix(B, B22, newSize, newSize);

        // Strassen's seven products

        int[][] P1 = strassenMultiply(
                add(A11, A22),
                add(B11, B22)
        );

        int[][] P2 = strassenMultiply(
                add(A21, A22),
                B11
        );

        int[][] P3 = strassenMultiply(
                A11,
                subtract(B12, B22)
        );

        int[][] P4 = strassenMultiply(
                A22,
                subtract(B21, B11)
        );

        int[][] P5 = strassenMultiply(
                add(A11, A12),
                B22
        );

        int[][] P6 = strassenMultiply(
                subtract(A21, A11),
                add(B11, B12)
        );

        int[][] P7 = strassenMultiply(
                subtract(A12, A22),
                add(B21, B22)
        );

        // C11 = P1 + P4 - P5 + P7
        int[][] C11 = add(
                subtract(
                        add(P1, P4),
                        P5
                ),
                P7
        );

        // C12 = P3 + P5
        int[][] C12 = add(P3, P5);

        // C21 = P2 + P4
        int[][] C21 = add(P2, P4);

        // C22 = P1 - P2 + P3 + P6
        int[][] C22 = add(
                add(
                        subtract(P1, P2),
                        P3
                ),
                P6
        );

        return combineMatrices(C11, C12, C21, C22);
    }


    /*
     * ============================================================
     * MATRIX UTILITY METHODS
     * ============================================================
     */

    // Add two matrices
    public static int[][] add(int[][] A, int[][] B) {

        int n = A.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] + B[i][j];
            }
        }

        return result;
    }


    // Subtract B from A
    public static int[][] subtract(int[][] A, int[][] B) {

        int n = A.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = A[i][j] - B[i][j];
            }
        }

        return result;
    }


    // Split a matrix
    public static void splitMatrix(
            int[][] source,
            int[][] destination,
            int rowOffset,
            int colOffset
    ) {

        int size = destination.length;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                destination[i][j] =
                        source[i + rowOffset][j + colOffset];
            }
        }
    }


    // Combine four submatrices
    public static int[][] combineMatrices(
            int[][] C11,
            int[][] C12,
            int[][] C21,
            int[][] C22
    ) {

        int n = C11.length;
        int[][] result = new int[n * 2][n * 2];

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                result[i][j] = C11[i][j];
                result[i][j + n] = C12[i][j];

                result[i + n][j] = C21[i][j];
                result[i + n][j + n] = C22[i][j];
            }
        }

        return result;
    }


    /*
     * ============================================================
     * EXECUTION TIME METHODS
     * ============================================================
     *
     * System.nanoTime() gives nanosecond-resolution timestamps.
     *
     * Returned value = elapsed time in nanoseconds.
     */

    public static long measureDirect(int[][] A, int[][] B) {

        long start = System.nanoTime();

        int[][] result = directMultiply(A, B);

        long end = System.nanoTime();

        // Prevent the JVM from treating the computation as unused
        consumeResult(result);

        return end - start;
    }


    public static long measureDivideAndConquer(int[][] A, int[][] B) {

        long start = System.nanoTime();

        int[][] result = divideAndConquerMultiply(A, B);

        long end = System.nanoTime();

        consumeResult(result);

        return end - start;
    }


    public static long measureStrassen(int[][] A, int[][] B) {

        long start = System.nanoTime();

        int[][] result = strassenMultiply(A, B);

        long end = System.nanoTime();

        consumeResult(result);

        return end - start;
    }


    /*
     * Simple method to make sure the calculated matrix
     * is actually used.
     */
    private static void consumeResult(int[][] matrix) {

        long checksum = 0;

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                checksum += matrix[i][j];
            }
        }

        // Use checksum so JIT cannot easily remove the work
        if (checksum == Long.MIN_VALUE) {
            System.out.println("Impossible value");
        }
    }


    /*
     * ============================================================
     * MATRIX GENERATION
     * ============================================================
     */

    public static int[][] generateMatrix(int n, Random random) {

        int[][] matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                // Random values from 0 to 9
                matrix[i][j] = random.nextInt(10);
            }
        }

        return matrix;
    }


    /*
     * ============================================================
     * CSV EXPORT
     * ============================================================
     *
     * Creates:
     *
     * matrix_multiplication_times.csv
     *
     * in the current working directory.
     */

    public static void exportToCSV(
            int[] sizes,
            long[] directTimes,
            long[] divideConquerTimes,
            long[] strassenTimes
    ) {

        String fileName = "matrix_multiplication_times.csv";

        try (FileWriter writer = new FileWriter(fileName)) {

            // Header
            writer.write(
                    "Matrix Size,Direct Method (ms),Divide & Conquer (ms),Strassen (ms)\n"
            );

            for (int i = 0; i < sizes.length; i++) {

                double directMs = directTimes[i] / 1_000_000.0;
                double divideMs = divideConquerTimes[i] / 1_000_000.0;
                double strassenMs = strassenTimes[i] / 1_000_000.0;

                writer.write(
                        sizes[i] + "," +
                        directMs + "," +
                        divideMs + "," +
                        strassenMs + "\n"
                );
            }

            System.out.println(
                    "\nCSV successfully exported to: " +
                    new java.io.File(fileName).getAbsolutePath()
            );

        } catch (IOException e) {

            System.out.println(
                    "Error while writing CSV: " +
                    e.getMessage()
            );
        }
    }


    /*
     * ============================================================
     * VERIFY RESULTS
     * ============================================================
     */

    public static boolean matricesEqual(int[][] A, int[][] B) {

        if (A.length != B.length) {
            return false;
        }

        for (int i = 0; i < A.length; i++) {

            for (int j = 0; j < A[i].length; j++) {

                if (A[i][j] != B[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }


    /*
     * ============================================================
     * MAIN
     * ============================================================
     */

    public static void main(String[] args) {

        int[] sizes = {32, 64, 128, 256, 512};

        long[] directTimes = new long[sizes.length];
        long[] divideConquerTimes = new long[sizes.length];
        long[] strassenTimes = new long[sizes.length];

        Random random = new Random(42);

        System.out.println(
                "Matrix Multiplication Performance Comparison"
        );

        System.out.println(
                "============================================================"
        );

        System.out.printf(
                "%-10s %-20s %-25s %-20s%n",
                "Size",
                "Direct (ms)",
                "Divide & Conquer (ms)",
                "Strassen (ms)"
        );

        System.out.println(
                "------------------------------------------------------------"
        );


        for (int i = 0; i < sizes.length; i++) {

            int n = sizes[i];

            System.out.println(
                    "Processing n = " + n + " ..."
            );

            // Generate matrices
            int[][] A = generateMatrix(n, random);
            int[][] B = generateMatrix(n, random);


            /*
             * Run Direct Method
             */
            long directTime = measureDirect(A, B);


            /*
             * Run Divide & Conquer
             */
            long divideTime = measureDivideAndConquer(A, B);


            /*
             * Run Strassen
             */
            long strassenTime = measureStrassen(A, B);


            // Store times
            directTimes[i] = directTime;
            divideConquerTimes[i] = divideTime;
            strassenTimes[i] = strassenTime;


            // Optional correctness check
            int[][] directResult = directMultiply(A, B);

            int[][] divideResult =
                    divideAndConquerMultiply(A, B);

            int[][] strassenResult =
                    strassenMultiply(A, B);

            boolean divideCorrect =
                    matricesEqual(directResult, divideResult);

            boolean strassenCorrect =
                    matricesEqual(directResult, strassenResult);


            // Convert to milliseconds
            double directMs =
                    directTime / 1_000_000.0;

            double divideMs =
                    divideTime / 1_000_000.0;

            double strassenMs =
                    strassenTime / 1_000_000.0;


            System.out.printf(
                    "%-10d %-20.3f %-25.3f %-20.3f%n",
                    n,
                    directMs,
                    divideMs,
                    strassenMs
            );

            System.out.println(
                    "Correctness: Divide & Conquer = " +
                    divideCorrect +
                    ", Strassen = " +
                    strassenCorrect
            );

            System.out.println();
        }


        /*
         * Export all measured execution times to CSV
         */
        exportToCSV(
                sizes,
                directTimes,
                divideConquerTimes,
                strassenTimes
        );
    }
}