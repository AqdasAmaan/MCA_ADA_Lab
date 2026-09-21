import java.util.*;

public class SpanningTrees {
    static int V;
    static int[][] adj;
    static List<int[]> edges = new ArrayList<>();
    static List<List<int[]>> allSpanningTrees = new ArrayList<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the number of vertices: ");
        V = sc.nextInt();

        adj = new int[V][V];

        System.out.println("Enter the adjacency matrix (" + V + "x" + V + "):");
        for (int i = 0; i < V; i++) {
            for (int j = 0; j < V; j++) {
                adj[i][j] = sc.nextInt();
                if (i < j && adj[i][j] > 0) {
                    for (int k = 0; k < adj[i][j]; k++) {
                        edges.add(new int[]{i, j});
                    }
                }
            }
        }

        findST(0, new ArrayList<>(), new int[V]);

        System.out.println("Total Spanning Trees: " + allSpanningTrees.size());
        for (int i = 0; i < allSpanningTrees.size(); i++) {
            System.out.print("Tree " + (i + 1) + ": ");
            for (int[] edge : allSpanningTrees.get(i)) {
                System.out.print("(" + edge[0] + "-" + edge[1] + ") ");
            }
            System.out.println();
        }
    }

    static void findST(int index, List<int[]> currentEdges, int[] parent) {
        if (currentEdges.size() == V - 1) {
            if (isConnected(currentEdges)) {
                allSpanningTrees.add(new ArrayList<>(currentEdges));
            }
            return;
        }

        if (index == edges.size()) return;

        currentEdges.add(edges.get(index));
        findST(index + 1, currentEdges, parent);
        currentEdges.remove(currentEdges.size() - 1);

        findST(index + 1, currentEdges, parent);
    }

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
                return false;
            }
        }
        return components == 1;
    }

    static int find(int[] parent, int i) {
        if (parent[i] == i) return i;
        return parent[i] = find(parent, parent[i]);
    }
}