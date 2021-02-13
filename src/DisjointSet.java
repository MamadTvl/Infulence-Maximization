public class DisjointSet {
    protected int Data;
    private int Rank;
    private DisjointSet p;


    DisjointSet(int data){
        Data = data;
        Rank = 0;
        p = this;
    }


    public static void Union(DisjointSet x, DisjointSet y){
        Link(Findset(x), Findset(y));
    }

    private static void Link(DisjointSet x, DisjointSet y){
        if (x.Rank > y.Rank)
            y.p = x;
        else {
            x.p = y;
            if (x.Rank == y.Rank)
                y.Rank ++;
        }
    }

    public static DisjointSet Findset(DisjointSet x){
        if (x != x.p)
            x.p = Findset(x.p);
        return x.p;
    }

}
