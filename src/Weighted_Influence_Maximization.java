import java.util.HashMap;

class Weighted_Influence_Maximization {

    Weighted_Influence_Maximization(Graph graph){
        DisjointSet[] disjointSet = new DisjointSet[graph.nodes.size()];
        for (int i = 0; i < disjointSet.length; i++) {
            disjointSet[i] = new DisjointSet(Integer.parseInt(String.valueOf(graph.nodes.get(i).label)));
        }
        for (int i = 0; i < graph.nodes.size() ; i++) {
            if (graph.nodes.get(i).Following == 0) {
                Influence_Maximization.PublishGraph(graph, i);
                System.out.println(graph.nodes.get(i).label + "---> Following : 0, Activated : " + graph.Activated);
            }
            if (graph.nodes.get(i).Follower == 0 && graph.nodes.get(i).Following != 0) {
                Influence_Maximization.PublishGraph(graph, i);
                System.out.println(graph.nodes.get(i).label + "---> Follower : 0, Activated : " + graph.Activated);
            }
        }
        int first = 0, second = 0, p = 0;
        double Max = -1;

        for (int i = 0; i < graph.nodes.size() ; ++i) {
            if (graph.nodes.get(i).Status == 2)
                continue;
            for (int j = i + 1; j < graph.nodes.size(); ++j) {
                if (graph.nodes.get(j).Status == 2 || graph.nodes.get(j).Following == 0)
                    continue;
                PublishResult temp_1 = Influence_Maximization.Publish_Activated(graph, i, new HashMap<>());
                PublishResult temp_2 = Influence_Maximization.Publish_Activated(graph, j, temp_1.Map);
                double X;
                int cost = graph.nodes.get(i).Follower + graph.nodes.get(j).Follower;
                if (cost == 0)
                    X = temp_1.ActivatedNodes + temp_2.ActivatedNodes;
                else
                    X = (double) (temp_1.ActivatedNodes + temp_2.ActivatedNodes) / (cost);
                if (X > Max) {
                    Max = X;
                    first = i;
                    second = j;
                    System.out.println("X : " + Max + " " + graph.nodes.get(first).label + " " + graph.nodes.get(second).label);
                    System.out.println("------------------------------------------");
                }
                p = temp_1.ActivatedNodes + temp_2.ActivatedNodes + graph.Activated;
                if (graph.nodes.size() > 1000)
                    if (p >= (double) 8 / 10 * graph.nodes.size())
                        break;
            }
            System.out.println(i + " Followers : " + graph.nodes.get(i).Follower + " Active : " + p + "max =" + Max);
            if (graph.nodes.size() > 1000)
                if (p >= (double) 8 / 10 * graph.nodes.size())
                    break;
        }
        DisjointSet.Union(disjointSet[second], disjointSet[first]);
        Influence_Maximization.PublishGraph(graph, first);
        Influence_Maximization.PublishGraph(graph, second);

        for (int i = 0; i < graph.nodes.size() ; i++) {
            if (graph.nodes.get(i).Following == 0)
                DisjointSet.Union(disjointSet[i], disjointSet[first]);
            if (graph.nodes.get(i).Follower == 0 && graph.nodes.get(i).Following != 0) {
                DisjointSet.Union(disjointSet[i], disjointSet[first]);
            }
        }
        int[] status_2 = new int[graph.nodes.size()];
        int cnt = 0;
        while (!Influence_Maximization.Check(graph)){
            Max = -1;
            int tmp;
            for (int i = 0; i < graph.nodes.size() ; i++) {
                if (graph.nodes.get(i).Status > 1)
                    continue;
                tmp = graph.nodes.get(i).Status;
                PublishResult temp_1 = Influence_Maximization.Publish(graph, i);
                int cost = graph.nodes.get(i).Follower;
                double X;
                if (cost == 0)
                    X = temp_1.ActivatedNodes;
                else
                    X = (double) (temp_1.ActivatedNodes)/(cost);
                if (X > Max){
                    Max = X;
                    second = i;
                    for (int j = 0; j < temp_1.SnapShot.nodes.size(); j++) {
                        status_2[j] = temp_1.SnapShot.nodes.get(j).Status;
                    }
                }
                while (!temp_1.Status.isEmpty())
                    graph.nodes.get(temp_1.Status.remove()).Status--;
                graph.nodes.get(i).Status = tmp;
            }
            DisjointSet.Union(disjointSet[second], disjointSet[first]);
            for (int i = 0; i < status_2.length; i++)
                graph.nodes.get(i).Status = status_2[i];
            Influence_Maximization.PublishGraph(graph, second);
            cnt++;
            int y = cnt + graph.Activated;
            System.out.println(cnt + " Node : " + second + " Activated : " + graph.Activated + "Max : " + Max + "Follower : " + graph.nodes.get(second).Follower );
        }
        Answer(disjointSet, first,graph);
    }

    private void Answer(DisjointSet[] set, int first, Graph g) {
        int total = 0, sum = 0;
        for (int i = 0; i < set.length; i++) {
            if (DisjointSet.Findset(set[i]) == set[first]) {
                System.out.print(set[i].Data);
                System.out.println(" Cost : " + g.nodes.get(i).Follower);
                total += g.nodes.get(i).Follower;
                sum++;
            }
        }
        System.out.println("Total Cost : " + total);
        System.out.println("Sum : " + sum);
    }

}
