import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

class PublishResult {
    public int ActivatedNodes;
    public Graph SnapShot;
    public Queue<Integer> Status;
    public HashMap<Integer, Integer> Map;

    PublishResult(int ActivatedNodes, Graph snapShot, Queue<Integer> status, HashMap<Integer, Integer> s) {
        this.ActivatedNodes = ActivatedNodes;
        this.SnapShot = snapShot;
        this.Status = status;
        this.Map = s;

    }
}

class Influence_Maximization {

    Influence_Maximization(Graph graph) {
        DisjointSet[] disjointSet = new DisjointSet[graph.nodes.size()];
        for (int i = 0; i < disjointSet.length; i++) {
            disjointSet[i] = new DisjointSet(Integer.parseInt(String.valueOf(graph.nodes.get(i).label)));
        }
        // Following 0
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (graph.nodes.get(i).Following == 0) {
                PublishGraph(graph, i);
                System.out.println(graph.nodes.get(i).label + "---> Following : 0, Activated : " + graph.Activated);
            }
        }
        int first = 0, second = 0;
        double Max = -1;
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (graph.nodes.get(i).Status > 0)
                graph.Activated++;
        }
        // 2 first node
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (graph.nodes.get(i).Follower == 0
                    || graph.nodes.get(i).Status == 2
                    || graph.nodes.get(i).Following == 0)
                continue;
            for (int j = i + 1; j < graph.nodes.size(); j++) {
                if (graph.nodes.get(j).Follower == 0
                        || graph.nodes.get(j).Status == 2
                        || graph.nodes.get(j).Following == 0)
                    continue;

                PublishResult temp_1 = Publish_Activated(graph, i, new HashMap<>());
                PublishResult temp_2 = Publish_Activated(graph, j, temp_1.Map);
                int X = temp_1.ActivatedNodes + temp_2.ActivatedNodes;
                if (X > Max) {
                    Max = X;
                    first = i;
                    second = j;
                }
                if (graph.nodes.size() > 1000 && Max + graph.Activated >= (double) 8 / 10 * graph.nodes.size())
                    break;
            }
            System.out.println("**** first :" + first + " second : " + second +
                    "Recent Active : " + Max + " All Activated :" + graph.Activated + "*****");
            if (graph.nodes.size() > 1000 && Max + graph.Activated >= (double) 8 / 10 * graph.nodes.size())
                break;
        }
        PublishGraph(graph, first);
        PublishGraph(graph, second);
        DisjointSet.Union(disjointSet[second], disjointSet[first]);
        int cnt = 0;
        for (int i = 0; i < graph.nodes.size(); i++) {
            if (graph.nodes.get(i).Status > 0)
                cnt++;
        }
        graph.Activated = cnt;
        for (int i = 0; i < graph.nodes.size(); i++)
            if (graph.nodes.get(i).Following == 0)
                DisjointSet.Union(disjointSet[i], disjointSet[first]);


        int[] status_2 = new int[graph.nodes.size()];

        cnt = 1;
        while (!Check(graph)) {
            Max = -1;
            boolean b = true;
            int tmp;
            for (int i = 0; i < graph.nodes.size(); i++) {
                if (graph.nodes.get(i).Status > 1)
                    continue;
                tmp = graph.nodes.get(i).Status;
                PublishResult temp_1 = Publish(graph, i);
                int X = temp_1.ActivatedNodes;
                if (X > Max) {
                    Max = X;
                    second = i;
                    for (int j = 0; j < temp_1.SnapShot.nodes.size(); j++) {
                        status_2[j] = temp_1.SnapShot.nodes.get(j).Status;
                    }
                }

                while (!temp_1.Status.isEmpty())
                    graph.nodes.get(temp_1.Status.remove()).Status--;

                graph.nodes.get(i).Status = tmp;
                if (temp_1.ActivatedNodes != 0) {
                    b = false;
                }
            }

            for (int i = 0; i < status_2.length; i++)
                graph.nodes.get(i).Status = status_2[i];
            graph.Activated += Max + 1;
            DisjointSet.Union(disjointSet[second], disjointSet[first]);
            if (b) {
                int h = 0;
                for (int i = 0; i < graph.nodes.size(); i++) {
                    if (graph.nodes.get(i).Status > 0) {
                        h++;
                    }
                }
                graph.Activated = h;
            }
            for (int i = 0; i < graph.nodes.size() && b && !Check(graph); i++) {
                if (graph.nodes.get(i).Status > 1)
                    continue;
                PublishGraph(graph, i);
                DisjointSet.Union(disjointSet[i], disjointSet[first]);
                System.out.println("Node : " + graph.nodes.get(i).label + "Max : " + Max + "Follower : " + graph.nodes.get(second).Follower);
            }
            System.out.println(cnt + " Node : " + second + "Max : " + Max + "Follower : " + graph.nodes.get(second).Follower);
            cnt++;
        }
        Answer(disjointSet, first, graph);
    }

    public static boolean Check(Graph g) {
//        return g.Activated == g.nodes.size();
        for (int i = 0; i < g.nodes.size(); i++) {
            if (g.nodes.get(i).Status == 0)
                return false;
        }
        return true;
    }

    private void Answer(DisjointSet[] set, int first, Graph g) {
        int sum = 0;
        for (int i = 0; i < set.length; i++) {
            if (DisjointSet.Findset(set[i]) == set[first]) {
                System.out.println(set[i].Data);
                sum++;
            }
        }
        System.out.println("Sum : " + sum);
    }

    public static void PublishGraph(Graph graph, int x) {
        graph.Activated++;
        graph.nodes.get(x).Status = 2;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(x);
        while (!queue.isEmpty()) {
            ArrayList<Node> q = graph.nodes.get(queue.remove()).Followers;
            for (Node follower : q) {
                if (follower.Status == 0) {
                    follower.Status++;
                    graph.Activated++;
                } else if (follower.Status == 1) {
                    follower.Status++;
                    queue.add(follower.index);
                }
            }
        }
    }

    public static PublishResult Publish_Activated(Graph graph, int x, HashMap<Integer, Integer> Updated_Status) {
        HashMap<Integer, Integer> status = new HashMap<>();
        int ActivateNodes = 0;
        graph.nodes.get(x).Status = 2;
        int c = graph.nodes.get(x).Status;
        Queue<Integer> queue = new LinkedList<>();
        queue.add(x);
        HashMap<Integer, Integer> Copy = new HashMap<>();
        while (!queue.isEmpty()) {
            ArrayList<Node> followers = graph.nodes.get(queue.remove()).Followers;
            for (Node follower : followers) {
                if (Updated_Status != null && Updated_Status.containsKey(follower.index)) {
                    if (Updated_Status.get(follower.index) == 0) {
                        Copy.put(follower.index, Updated_Status.get(follower.index));
                        follower.Status++;
                        status.put(follower.index, follower.Status);
                        ActivateNodes++;
                    } else if (Updated_Status.get(follower.index) == 1) {
                        Copy.put(follower.index, Updated_Status.get(follower.index));
                        follower.Status++;
                        status.put(follower.index, follower.Status);
                        queue.add(follower.index);
                    }
                } else {
                    if (follower.Status == 0) {
                        Copy.put(follower.index, follower.Status);
                        follower.Status++;
                        status.put(follower.index, follower.Status);
                        ActivateNodes++;
                    } else if (follower.Status == 1) {
                        Copy.put(follower.index, follower.Status);
                        follower.Status++;
                        status.put(follower.index, follower.Status);
                        queue.add(follower.index);
                    }
                }
            }
        }
        for (int i : Copy.keySet()) {
            graph.nodes.get(i).Status = Copy.get(i);
        }
        graph.nodes.get(x).Status = c;
        return new PublishResult(ActivateNodes, graph, queue, status);
    }

    public static PublishResult Publish(Graph graph, int node) {
        boolean b = true;
        if (graph.nodes.get(node).Following == 0)
            b = false;
        int recently_Activated = 0;
        graph.nodes.get(node).Status = 2;
        Queue<Integer> queue = new LinkedList<>();
        Queue<Integer> status = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            ArrayList<Node> q = graph.nodes.get(queue.remove()).Followers;
            for (Node follower : q) {
                if (follower.Status == 0) {
                    follower.Status++;
                    recently_Activated++;
                    if (b)
                        status.add(follower.index);
                } else if (follower.Status == 1) {
                    follower.Status++;
                    if (b)
                        status.add(follower.index);
                    queue.add(follower.index);
                }
            }
        }
        return new PublishResult(recently_Activated, graph, status, new HashMap<>());
    }

}
