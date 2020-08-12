import java.util.Scanner; // Needed for the Scanner class 
import java.util.Iterator;

public class NetworkAnalysis {

    private static int option_number;                                   // Create a Scanner object to read input.
    private static Scanner keyboard = new Scanner(System.in);
    private static Graph graph;
    public static void main(String[] args) {

        if (args.length == 1) {
            graph = new Graph(args[0]);

            // Create a do while loop to control the program
            do{
                // Ask the user to enter number 1-8
                System.out.println("Please choose an option:");
                System.out.println("\t1) Find the lowest latency path between two vertices.\n" +
                                "\t2) Is the network copper only?.\n" +
                                "\t3) Would the network remain connected if two verices were to fail?\n" + 
                                "\t4) Exit");
                option_number = Integer.parseInt(keyboard.nextLine()); 

                switch (option_number)
                {
                    case 1: 
                        lowestLatency();
                        break;
                    case 2:
                        copperConnected();
                        break;
                    case 3:
                        failedVertices();
                        break;
                    case 4: 
                        break;
                    default:
                        System.out.println("Invalid Entry!!!");         // To make sure user enters valid input
                        break;
                }
            }while (option_number != 4);
        }
        else {
            System.out.println("There must be one argument.");
        }
    }
    private static void lowestLatency() {
        int from, to;
        do {
            System.out.print("Please enter the starting vertex: ");
            from = Integer.parseInt(keyboard.nextLine());

            if (from < 0 || from > (graph.V() - 1) ) {
                System.out.println("The entered value does not lie within the range of vertices.");
            }
        }while ((from < 0) || from > (graph.V() - 1));

        do {
            System.out.print("Please enter the ending vertex: ");
            to = Integer.parseInt(keyboard.nextLine());

            if (to < 0 || to > (graph.V() - 1) ) {
                System.out.println("The entered value does not lie within the range of vertices.");
            }
        }while (to < 0 || to > graph.V() - 1);

         // compute shortest paths
         DijkstraSP sp = new DijkstraSP(graph, from);

        System.out.println();

        if (sp.hasPathTo(to)) {
            System.out.print("Shortest Path: ");
            for (Edge e : sp.pathTo(to)) {
                System.out.print( e + " ");
            }

            System.out.println();
            System.out.println("Total Latency: " + sp.distTo(to)*1000000000 + " ns");
            System.out.println("Available Bandwidth: " + sp.bandwidth(to) + " Mbps");
            System.out.println();
            
        }
        else {
            System.out.println("There does not exist a path from " + from + " to " + to);
            System.out.println();
        }


    }

    private static void copperConnected() {

        boolean copper = false;

        for (int i = 0; i < graph.V(); i++) {

            copper = false;

            Iterator<Edge> it = graph.adj(i).iterator();

            //iterate through each edge in the linked list 
            while (it.hasNext()) {
                Edge e = it.next();
                if (e.isCopper()) {
                    copper = true;
                }

            }
            //exit loop
            if (!copper) {
                i = graph.V();
            }

        }

        if (copper) {
            System.out.println("This network is copper connected.");
        }
        else {
            System.out.println("This network is not copper connected.");
        }

    }
   
    private static void failedVertices() {
        CC cc = new CC(graph);

        if (cc.count() > 1) {
            System.out.println("The graph will not remain connected if any two vertices were to fail.");
        }
        else {
            System.out.println("The graph will still remain connected if any two verties were to fail.");
        }

    }

}




