import java.util.Scanner;

public class ChainMultiplication {

    static long[][] M;
    static int[][] P;

    static long minMul(int[] d, int n) {

        for (int i = 0; i < n; i++) {
            M[i][i] = 0;
        }

        for (int diagonal = 1; diagonal <= n - 1; diagonal++) {

            for (int i = 0; i <= n - diagonal - 1; i++) {

                int j = i + diagonal;

                M[i][j] =
                        M[i][i]
                        + M[i + 1][j]
                        + (long) d[i] * d[i + 1] * d[j + 1];

                P[i][j - 1] = i;

                for (int k = i + 1; k < j; k++) {

                    long cost =
                            M[i][k]
                            + M[k + 1][j]
                            + (long) d[i] * d[k + 1] * d[j + 1];


                    if (cost < M[i][j]) {

                        M[i][j] = cost;
                        P[i][j - 1] = k;
                    }
                }
            }
        }

        System.out.println("\nMatrix M");

        for (int i = 0; i < n; i++) {

            for (int j = 0; j < n; j++) {

                if (j < i) {
                    System.out.print("-\t");
                }
                else {
                    System.out.print(M[i][j] + "\t");
                }
            }

            System.out.println();
        }

        System.out.println("\nMatrix P");

        for (int i = 0; i < n - 1; i++) {

            for (int j = 0; j < n - 1; j++) {

                if (j < i) {
                    System.out.print("-\t");
                }
                else {
                    System.out.print((P[i][j] + 1) + "\t");
                }
            }

            System.out.println();
        }

        return M[0][n - 1];
    }

    static void order(int i, int j) {

        if (i == j) {
            System.out.print("A" + (i+1));
        }

        else {

            int k = P[i][j - 1];

            System.out.print("(");

            order(i, k);

            order(k + 1, j);

            System.out.print(")");
        }
    }

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.print("Enter number of matrices: ");
        int n = sc.nextInt();

        int[] d = new int[n + 1];

        System.out.println("Enter " + (n + 1) + " dimensions:");

        for (int i = 0; i <= n; i++) {
            d[i] = sc.nextInt();
        }


        M = new long[n][n];

        P = new int[n - 1][n - 1];

        long minCost = minMul(d, n);

        System.out.println("\n");
        System.out.println("RESULT");
        System.out.println("-------------------------------------------");

        System.out.println(
                "Minimum number of multiplications: "
                + minCost
        );

        System.out.print("Optimal multiplication order: ");

        order(0, n - 1);

        System.out.println();

        sc.close();
    }
}