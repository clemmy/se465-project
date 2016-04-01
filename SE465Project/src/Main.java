public class Main {
    public static void main(String[] args) {
        //arg0 = callgraph file path
        //arg1 = tsupport
        //arg2 = tconfidence

        CallGraph cg = new CallGraph(args[0]);
        SupportGraph sg = new SupportGraph(cg);

        int T_SUPPORT = 3;
        double T_THRESHOLD = 65;

        if (args.length > 1) {
            T_SUPPORT = Integer.parseInt(args[1]);
        }
        if (args.length > 2) {
            T_THRESHOLD = Double.parseDouble(args[2]);
        }

        cg.analyze(sg, T_SUPPORT, T_THRESHOLD);
    }
}
