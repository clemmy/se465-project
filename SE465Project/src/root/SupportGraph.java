package root;

import java.util.HashMap;
import java.util.HashSet;

public class SupportGraph {
    HashMap<Integer, HashSet<Integer>> sg;

    public SupportGraph(CallGraph cg) {
        sg = new HashMap<Integer, HashSet<Integer>>(20000);

        for (HashMap.Entry<Integer, HashSet<Integer>> hashedFnc : cg.graph.entrySet()) {
            System.out.println("Key = " + hashedFnc.getKey() + ", Value = " + hashedFnc.getValue());
        }
    }


}
