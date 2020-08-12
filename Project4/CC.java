import java.util.Iterator;

public class CC {
    private boolean[] marked;   // marked[v] = has vertex v been marked?
    private int count;          // number of connected component

    /**
     * Computes the connected components of the undirected graph {@code G}.
     *
     * @param G the undirected graph
     */
    public CC(Graph G) {
        marked = new boolean[G.V()];
        
        for (int i = 0; i < G.V() - 1; i++) {
            for (int j = i + 1; j < G.V(); j++) {     //remove from graph 
                 count = 0;
                for (int k = 0; k < G.V(); k++) {      //iterate over array of booleans
                    if ((k == i) || (k == j)) {
                        
                        marked[k] = true;
                    }
                    else {
                        marked[k] = false;
                    }
                }
                for (int v = 0; v < G.V(); v++) {
                    if (!marked[v]) {
                        dfs(G, v);
                        count++;
                    }
        
                    
                }
                if (count > 1) {
                    i = G.V();
                    j = G.V();
                }

            }
        }
    }
    // depth-first search for a Graph
    private void dfs(Graph G, int v) {
        marked[v] = true;
 
        Iterator<Edge> it = G.adj(v).iterator();
        while (it.hasNext()) {
            Edge e = it.next();
            if (!marked[e.to()]) {
                dfs(G, e.to());
            }
        } 
    }

    // return the count
    public int count() {
        return count;
    }

  

  
}