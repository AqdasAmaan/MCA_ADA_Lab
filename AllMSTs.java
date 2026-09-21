import java.util.*;

public class AllMSTs {
    static int V, minWeight = Integer.MAX_VALUE;
    static int[][] adj;
    static List<List<int[]>> allMSTs = new ArrayList<>(); 
    

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        V = sc.nextInt();
        adj = new int[V][V];

        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                adj[i][j] = sc.nextInt();
            }
        }

        // Start evaluating from row 0, col 1
        findMSTs(0, 1, new ArrayList<>(), 0);

        System.out.println("Min Weight: " + minWeight);
        System.out.println("Total MSTs: " + allMSTs.size());
        for (int i = 0; i < allMSTs.size(); i++) {
            System.out.print("MST " + (i + 1) + ": ");
            for (int[] e : allMSTs.get(i)) {
                System.out.print("(" + e[0] + "-" + e[1] + " w:" + e[2] + ") ");
            }
            System.out.println();
        }
    }

    static void findMSTs(int r, int c, List<int[]> current, int currentWeight) {
        // Pruning: if we've already exceeded minWeight, abort this branch
        if (currentWeight > minWeight) return;

        // Base Case: Valid Spanning Tree found
        if (current.size() == V - 1) {
            if (isConnected(current)) {
                if (currentWeight < minWeight) {
                    minWeight = currentWeight;
                    allMSTs.clear(); // Discard heavier trees
                }
                if (currentWeight == minWeight) {
                    allMSTs.add(new ArrayList<>(current));
                }
            }
            return;
        }

        // Boundary Check (PREVENTS CRASH): Stop if row or col exceeds matrix bounds
        if (r >= V - 1 || c >= V) return;

        // Calculate next matrix coordinates (upper triangle traversal)
        int nextR = r;
        int nextC = c + 1;
        if (nextC >= V) {
            nextR = r + 1;
            nextC = nextR + 1;
        }

        // Include edge path
        if (adj[r][c] > 0) {
            current.add(new int[]{r, c, adj[r][c]});
            findMSTs(nextR, nextC, current, currentWeight + adj[r][c]);
            current.remove(current.size() - 1); // backtrack
        }

        // Exclude edge path
        findMSTs(nextR, nextC, current, currentWeight);
    }

    // Kruskal's connectivity check using Union-Find
    static boolean isConnected(List<int[]> treeEdges) {
        int[] parent = new int[V];
        for (int i = 0; i < V; i++) parent[i] = i;

        int components = V;
        for (int[] e : treeEdges) {
            int rootU = find(parent, e[0]);
            int rootV = find(parent, e[1]);
            if (rootU != rootV) {
                parent[rootU] = rootV;
                components--;
            } else {
                return false; // Cycle detected
            }
        }
        return components == 1;
    }

    static int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent, parent[i]);
    }
}