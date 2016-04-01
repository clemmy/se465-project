import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class SupportGraph {
    public HashMap<Integer, HashMap<Integer, Integer>> pairSupport;
    public ArrayList<Integer> support; // arraylist of hashsets

    public SupportGraph(CallGraph cg) {
        pairSupport = new HashMap<Integer, HashMap<Integer, Integer>>(20000);
        support = new ArrayList<Integer>(cg.translationTable.size());
        while(support.size() < cg.translationTable.size()) {
            support.add(0);
        }

        for (Map.Entry<Integer, HashSet<Integer>> hashedFnc : cg.graph.entrySet()) {
            HashSet<Integer> children = hashedFnc.getValue();
            Integer[] childrenArray = new Integer[children.size()];
            childrenArray = children.toArray(childrenArray);

            boolean[] exists = new boolean[cg.translationTable.size()];

            for (int i = 0; i < childrenArray.length; i++) {
                exists[childrenArray[i]] = true;

                for (int j = i + 1; j < childrenArray.length; j++) {

                    int first = Math.min(childrenArray[i], childrenArray[j]);
                    int second = Math.max(childrenArray[i], childrenArray[j]);

                    if (pairSupport.containsKey(first)) {
                        HashMap<Integer, Integer> secondHash = pairSupport.get(first);
                        if (secondHash.containsKey(second)) {
                            Integer count = secondHash.get(second);
                            secondHash.put(second, count + 1);
                        } else {
                            secondHash.put(second, 1);
                        }
                    } else {
                        HashMap<Integer, Integer> secondHash = new HashMap<Integer, Integer>();
                        secondHash.put(second, 1);
                        pairSupport.put(first, secondHash);
                    }

                    if (pairSupport.containsKey(second)) {
                        HashMap<Integer, Integer> secondHash = pairSupport.get(second);
                        if (secondHash.containsKey(first)) {
                            Integer count = secondHash.get(first);
                            secondHash.put(first, count + 1);
                        } else {
                            secondHash.put(first, 1);
                        }
                    } else {
                        HashMap<Integer, Integer> secondHash = new HashMap<Integer, Integer>();
                        secondHash.put(first, 1);
                        pairSupport.put(second, secondHash);
                    }
                }
            }

            for (int i = 0; i < exists.length; i++) {
                if (exists[i]) {
                    support.set(i, support.get(i) + 1);
                }
            }
        }
    }
}
