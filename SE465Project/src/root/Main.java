package root;

public class Main {
    public static void main(String[] args) {
        CallGraph cg = new CallGraph(args[0]);
//        cg.debugCallGraph();
        SupportGraph sg = new SupportGraph(cg);
        cg.analyze(sg);
    }
}
