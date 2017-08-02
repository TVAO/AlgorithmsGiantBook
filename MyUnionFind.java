import java.util.HashMap;

/*
Compilation: javac MyUnionFind.java
Execution: java MyUnionFind < input.txt
Dependencies: StdIn.java, StdOut.java
Using weighted quick-union (without path compression)
 */

public class MyUnionFind {
    
    private int[] parentID;    // id[i] = parent of i
    private int[] subTreeSize;    // subTreeSize[i] = number of objects in subtree rooted at i
    private int count;   // number of components

    // Assignment related variables
    private HashMap<Integer,Integer> connected; // Elements which have been connected.                                            
    private int total; // Total elements       //Size is equal to amount of unique unions made.
    private boolean isNonIsolated;
    private boolean isGiant;
    private boolean isConnected;

    /**
     * Initializes an empty union-find data structure with N isolated components 0 through N-1.
     * @throws java.lang.IllegalArgumentException if N < 0
     * @param N the number of objects
     */
    public MyUnionFind(int N) {
        count = N;
        parentID = new int[N];
        subTreeSize = new int[N];
        for (int i = 0; i < N; i++) {
            parentID[i] = i;
            subTreeSize[i] = 1;
        }

        // Assignment related
        total = N; // save total 
        connected = new HashMap<Integer,Integer>();
    }

    // Assignment related methods checking whether event exists 
    public boolean isNonIsolated() {
        return isNonIsolated;
    }

    public boolean isGiant() {
        return isGiant;
    }

    /**
     * Checks whether the event is connected
     * If the component counter equals one, the component contains all elements 
     * @return true if all elements are connected 
     */
    public boolean isConnected() {
        if (count == 1) 
        isConnected = true; 
        return isConnected;
    }

    /**
     * Returns the number of components.
     * @return the number of components (between 1 and N)
     */
    public int count() {
        return count;
    }

    /**
     * Returns the component parentIDentifier for the component containing site p.
     * @param p the integer representing one site
     * @return the component identifier for the component containing site p
     * @throws java.lang.IndexOutOfBoundsException unless 0 <= p < N
     */
    public int find(int p) {
        while (p != parentID[p])
            p = parentID[p];
        return p;
    }

    /**
     * Are the two sites p and q in the same component?
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @return true if the two sites p and q
     *    are in the same component, and false otherwise
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public boolean connected(int p, int q) {
        return find(p) == find(q);
    }

  
    /**
     * Merges the component containing site p with the component
     * containing site q.
     * @param p the integer representing one site
     * @param q the integer representing the other site
     * @throws java.lang.IndexOutOfBoundsException unless both 0 <= p < N and 0 <= q < N
     */
    public void union(int p, int q) {
        int rootP = find(p);
        int rootQ = find(q);
        if (rootP == rootQ) return; // Breaks if already connected 

        // make smaller root point to larger one
        if   (subTreeSize[rootP] < subTreeSize[rootQ]) { parentID[rootP] = rootQ; subTreeSize[rootQ] += subTreeSize[rootP]; }
        else                         { parentID[rootQ] = rootP; subTreeSize[rootP] += subTreeSize[rootQ]; }
        count--;

        // 1 is used as a boolean indicator, e.g. telling p and q are now connected 
        connected.put(p,1);
        connected.put(q,1); 

        if(!isNonIsolated) {
            if(connected.size() >= total) { // All elemenents are in a union 
                isNonIsolated = true;
            }
        }

        if(!isGiant) {
            if (subTreeSize[rootP] >= total / 2) { // I // More than half elements are in same component (root) 
                isGiant = true;
            }
        }


    }


    /**
     * Reads in a sequence of pairs of integers (between 0 and N-1) from standard input, 
     * where each integer represents some object;
     * if the objects are in different components, merge the two components
     * and print the pair to standard output.
     */
    public static void main(String[] args) {
        int N = StdIn.readInt();
        MyUnionFind uf = new MyUnionFind(N);

        double s1 = System.currentTimeMillis();

        while (!StdIn.isEmpty()) {
            int p = StdIn.readInt();
            int q = StdIn.readInt();
            if (uf.connected(p, q)) continue;
            uf.union(p, q);
            StdOut.println(p + " " + q);
        }
        double s2 = System.currentTimeMillis();

        StdOut.println(uf.count() + " components");
        StdOut.println("Runtime: " + ((s2-s1)/1000) + " seconds.");
    }

}
