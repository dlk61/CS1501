import java.util.*;
import java.io.*;

public class Graph {

    private static final String NEWLINE = System.getProperty("line.separator");
    private final int V;                // number of vertices in this digraph
    private int E;                      // number of edges in this digraph
    private static Scanner inputFile;
    private Bag<Edge>[] adj;    // adj[v] = adjacency list for vertex v
    private int[] indegree;             // indegree[v] = indegree of vertex v
    private double latency;
    private boolean copperOnly = true;

    public Graph(String name) {

        try{
            File file = new File(name);
            inputFile = new Scanner(file);
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found.");
            System.exit(1);
        }
        try {
            V = Integer.parseInt(inputFile.nextLine());
            indegree = new int[V];
            adj = new Bag[V];
            for (int v = 0; v < V; v++) {
                adj[v] = new Bag<Edge>();
            }

            String[] tokens;
            int v1, v2, bandwidth, length;
            String cable;

            while (inputFile.hasNextLine()) {
                String line = inputFile.nextLine();
                tokens = line.split(" ");
                v1 = Integer.parseInt(tokens[0]);
                v2 = Integer.parseInt(tokens[1]);
                cable = tokens[2];
                if (cable.equals("optical")) {
                    copperOnly = false;
                }
                bandwidth = Integer.parseInt(tokens[3]);
                length = Integer.parseInt(tokens[4]);
                if (cable.equals("copper")) {
                    latency =  (double) length / 230000000;
                }
                else if (cable.equals("optical")) {
                    latency = (double) length / 200000000;
                }


                addEdge(new Edge(v1, v2, cable, latency, bandwidth));
                addEdge(new Edge(v2, v1, cable, latency, bandwidth));
                
            }
            inputFile.close();
        }
        catch (NoSuchElementException e) {
            throw new IllegalArgumentException("invalid input format ", e);
        }
    }


    /**
     * Returns the number of vertices in this edge-latencyed digraph.
     *
     * @return the number of vertices in this edge-latencyed digraph
     */
    public int V() {
        return V;
    }

    /**
     * Returns the number of edges in this edge-latencyed digraph.
     *
     * @return the number of edges in this edge-latencyed digraph
     */
    public int E() {
        return E;
    }

    public boolean copper() {
        return copperOnly;
    }

    // throw an IllegalArgumentException unless {@code 0 <= v < V}
    private void validateVertex(int v) {
        if (v < 0 || v >= V)
            throw new IllegalArgumentException("Switch " + v + " is not between 0 and " + (V-1));
    }

    /**
     * Adds the directed edge {@code e} to this edge-latencyed digraph.
     *
     * @param  e the edge
     * @throws IllegalArgumentException unless endpoints of edge are between {@code 0}
     *         and {@code V-1}
     */
    public void addEdge(Edge e) {
        int v = e.from();
        int w = e.to();
        validateVertex(v);
        validateVertex(w);
        adj[v].add(e);
        indegree[w]++;
        E++;
    }


    /**
     * Returns the directed edges incident from vertex {@code v}.
     *
     * @param  v the vertex
     * @return the directed edges incident from vertex {@code v} as an Iterable
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public Iterable<Edge> adj(int v) {
        validateVertex(v);
        return adj[v];
    }

    /**
     * Returns the number of directed edges incident from vertex {@code v}.
     * This is known as the <em>outdegree</em> of vertex {@code v}.
     *
     * @param  v the vertex
     * @return the outdegree of vertex {@code v}
     * @throws IllegalArgumentException unless {@code 0 <= v < V}
     */
    public int outdegree(int v) {
        validateVertex(v);
        return adj[v].size();
    }

    
    // Returns the number of directed edges incident to vertex {@code v}.
    
    public int indegree(int v) {
        validateVertex(v);
        return indegree[v];
    }

    //iterates over edges
    public Iterable<Edge> edges() {
        Bag<Edge> list = new Bag<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
                list.add(e);
            }
        }
        return list;
    } 

}