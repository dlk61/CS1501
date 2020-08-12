public class Edge { 
    private final int v;
    private final int w;
    private final double latency;
    private final int bandwidth;
    private final String cable;

    public Edge(int v, int w, String cable, double latency, int bandwidth) {
        this.v = v;
        this.w = w;
        this.cable = cable;
        this.latency = latency;
        this.bandwidth = bandwidth;
    }

    
    //  Returns the source vertex of edge.
    public int from() {
        return v;
    }

    
    //Returns the destination vertex of the edge.

    public int to() {
        return w;
    }

    //checks to see if cable is copper 
    public boolean isCopper() {
        return (cable.equals("copper"));
    }
   
     //Returns the latency of the edge.
  
    public double latency() {
        return latency;
    }

    //returns the bandwidth of edge
    public int bandwidth() {
        return bandwidth;
    }

   
    // Returns a string representation of the edge.
    
    public String toString() {
        return v + "->" + w;
    }
    
}