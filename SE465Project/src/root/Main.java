package root;

public class Main {
    public static void main(String[] args) {
        //arg0 = callgraph file path
        //arg1 = tsupport
        //arg2 = tconfidence

        CallGraph cg = new CallGraph(args[0]);
//        cg.debugCallGraph();
        SupportGraph sg = new SupportGraph(cg);

        //System.out.println(args[1] + args[2]);
        cg.analyze(sg, Integer.parseInt(args[1]), Double.parseDouble(args[2]));
    }
}
