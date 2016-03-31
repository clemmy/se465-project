package root;

import java.util.HashMap;
import java.util.HashSet;

public class SupportGraph {
    HashMap<Pair, Integer> sg;

    public SupportGraph(CallGraph cg) {
        sg = new HashMap<Pair, Integer>(20000);

        for (HashMap.Entry<Integer, HashSet<Integer>> hashedFnc : cg.graph.entrySet()) {
            System.out.println("Key = " + hashedFnc.getKey() + ", Value = " + hashedFnc.getValue());

            // permute through pairs
            HashSet<Integer> children = hashedFnc.getValue();
            Integer[] childrenArray = new Integer[children.size()];
            childrenArray = children.toArray(childrenArray);

            for (int i = 0; i < childrenArray.length; i++) {
                for (int j = i + 1; j < childrenArray.length; j++){
                    Pair p = new Pair(childrenArray[i], childrenArray[j]);
                    if (sg.containsKey(p)) {
                        Integer count = sg.get(p);
                        count++;
                        sg.put(p, count + 1);
                    } else {
                        sg.put(p, 1);
                    }
                }
            }
        }

        System.out.println("done");
    }


}
