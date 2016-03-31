package root;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class SupportGraph {
    HashMap<Pair, Integer> pairSupport;
    ArrayList<Integer> support;

    public SupportGraph(CallGraph cg) {
        pairSupport = new HashMap<Pair, Integer>(20000);
        support = new ArrayList<Integer>(cg.translationTable.size());
        while(support.size() < cg.translationTable.size()) {
            support.add(0);
        }

        for (HashMap.Entry<Integer, HashSet<Integer>> hashedFnc : cg.graph.entrySet()) {
            HashSet<Integer> children = hashedFnc.getValue();
            Integer[] childrenArray = new Integer[children.size()];
            childrenArray = children.toArray(childrenArray);

            boolean[] exists = new boolean[cg.translationTable.size()];

            for (int i = 0; i < childrenArray.length; i++) {
                exists[childrenArray[i]] = true;

                for (int j = i + 1; j < childrenArray.length; j++) {
                    Pair p = new Pair(childrenArray[i], childrenArray[j]);
                    if (pairSupport.containsKey(p)) {
                        Integer count = pairSupport.get(p);
                        pairSupport.put(p, count + 1);
                    } else {
                        pairSupport.put(p, 1);
                    }
                }
            }

            for (int i = 0; i < exists.length; i++) {
                if (exists[i]) {
                    support.set(i, support.get(i) + 1);
                }
            }
        }

        System.out.println("done");
    }
}
