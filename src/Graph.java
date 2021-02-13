import java.util.*;

class Node {
    public String label;
    public int Following = 0, Follower = 0;
    public int Status;
    public int index;
    public ArrayList<Node> Followings = new ArrayList<>(), Followers = new ArrayList<>();

    Node(String l) {
        this.label = l;
    }
    public void setStatus() {
        Status = 0;
    }
}

public class Graph {

    public ArrayList<Node> nodes = new ArrayList<>();
    public int Activated = 0;

    // Status -> not-Seen : 0, Seen : 1, Publish : 2
    private int size;

    public void setStatus(){
        for (int i = 0; i < nodes.size(); i++) {
            nodes.get(i).setStatus();
        }
    }

    public void setIndex(){
        for (int i = 0; i < nodes.size() ; i++) {
            nodes.get(i).index = i;
        }
    }

    public int indexOfLabel(String label){
        for (int i = 0; i < nodes.size() ; i++) {
            if (label.equals(nodes.get(i).label))
                return i;
        }
        return -1;
    }

    public void Sort(boolean b){
        if (b) {
            nodes.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o2.Follower - o1.Follower;
                }
            });
        }
        else {
            nodes.sort(new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {
                    return o1.Follower - o2.Follower;
                }
            });
        }
    }


    public static Graph Copy(Graph g, int[] status) {
        Graph L = new Graph();
        L.nodes = g.nodes;
        L.size = g.size;
        for (int i = 0; i < status.length; i++) {
            L.nodes.get(i).Status = status[i];
        }
        return L;
    }

    public static Graph Copy(Graph g) {
        Graph L = new Graph();
        L.nodes = g.nodes;
        L.size = g.size;
        for (int i = 0; i < g.nodes.size(); i++) {
            L.nodes.get(i).Status = g.nodes.get(i).Status;
        }
        return L;
    }


    public void addNode(Node n) {
        nodes.add(n);
    }


    public void connectNode(Node start, Node end) {
        int startIndex = nodes.indexOf(start);
        int endIndex = nodes.indexOf(end);

        nodes.get(startIndex).Following++;
        nodes.get(startIndex).Followings.add(nodes.get(endIndex));
        nodes.get(endIndex).Follower++;
        nodes.get(endIndex).Followers.add(nodes.get(startIndex));
    }



    public static void printNode(Node n){
        System.out.println(n.label +
                " " +
                "status : " + n.Status);
//        System.out.print("Followings : ");
//        for (int i = 0; i < n.Followings.size() ; i++) {
//            System.out.print(n.Followings.get(i).label);
//        }
//        System.out.println();
//        System.out.print("Followers : ");
//        for (int i = 0; i < n.Followers.size() ; i++) {
//            System.out.print(n.Followers.get(i).label + " ");
//        }
        System.out.println("----------------------------------------------------");

    }


}