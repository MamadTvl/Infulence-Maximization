import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main{

    public static void main(String[] args) throws FileNotFoundException {
        Graph graph = getGraph(new File("src/Graph.txt"));
        graph.Sort(true);
        graph.setIndex();
        System.out.println("Question A :");
        new Influence_Maximization(graph);
//
//        graph.Sort(false);
//        graph.setIndex();
//        System.out.println("Question B :");
//        new Weighted_Influence_Maximization(graph);

    }

    private static Graph getGraph(File file) throws FileNotFoundException {
        int N, E;
//        file = new File("src/Graph.txt");
        Scanner input ;
        input = new Scanner(file);
        N = input.nextInt();
        E = input.nextInt();
        Node[] node = new Node[N];
        int j = 48;
        for (int i = 0; i < N; i++) {
            node[i] = new Node(String.valueOf(i));
            j++;
        }
        Graph graph = new Graph();
        for (int i = 0; i < N ; i++) {
            graph.addNode(node[i]);
        }
        for (int i = 0; i < E; i++)
            graph.connectNode(node[input.nextInt()], node[input.nextInt()]);
        graph.setStatus();
        input.close();
        System.out.println("File Read !");
        return graph;
    }
}
