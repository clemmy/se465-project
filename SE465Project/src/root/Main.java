package root;

public class Main {
    public static void main(String[] args) {
        System.out.println("running");

        CallGraph cg = new CallGraph(args[0]);
        cg.debugCallGraph();
        SupportGraph sg = new SupportGraph(cg);
    }
}
