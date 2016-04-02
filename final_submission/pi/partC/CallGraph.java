import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CallGraph {

    public HashMap<Integer, HashSet<Integer>> graph;
    public ArrayList<String> translationTable;
    public HashMap<String, Integer> reverseTable;

    public CallGraph(String filename) {

        graph = new HashMap<Integer, HashSet<Integer>>(20000);
        translationTable = new ArrayList<String>();
        reverseTable = new HashMap<String, Integer>();

        try {
            String line;
            int lastFuncId = 0;

            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Call graph node for function:")) { // outer function
                    String functionName = line.split("\'")[1];

                    int id;
                    if (!translationTable.contains(functionName)) {
                        translationTable.add(functionName);
                        id = translationTable.size() - 1;
                        lastFuncId = id;
                        reverseTable.put(functionName, id);
                    } else {
                        id = reverseTable.get(functionName);
                        lastFuncId = id;
                    }

                    graph.put(id, new HashSet<Integer>());

                } else if (line.startsWith("  CS<")) { // inside function
                    String[] split = line.split("\'");

                    if (split.length == 2) { // avoid this case: CS<0x0> calls external node
                        String functionName = split[1];

                        if (!translationTable.contains(functionName)) {
                            translationTable.add(functionName);
                            int id = translationTable.size() - 1;
                            reverseTable.put(functionName, id);
                            HashSet<Integer> edges = graph.get(lastFuncId);
                            edges.add(id);
                        } else {
                            int id = reverseTable.get(functionName);
                            HashSet<Integer> edges = graph.get(lastFuncId);
                            edges.add(id);
                            graph.put(lastFuncId, edges);
                        }
                    }

                } else if (line.startsWith("Call graph node <<null function>>")) {
                    String uselessString;
                    while (br.readLine().startsWith("  CS<0x0>")) {}
                } else {
                }
            }
        } catch (Exception e) {

        }
    }

    public void expand(int passes) {
        for (int i=0; i<passes-1; ++i) {
            // iterate through top level functions
            for (Map.Entry<Integer, HashSet<Integer>> hashedFnc : this.graph.entrySet()) {

                ArrayList<Integer> functionsToExpand = new ArrayList<>();

                // iterate through inner functions
                for (Integer innerFnc : hashedFnc.getValue()) {
                    // check if it exists in the top level
                    if (graph.containsKey(innerFnc) && !graph.get(innerFnc).isEmpty()) {
                        functionsToExpand.add(innerFnc);
                    }
                }

                if (functionsToExpand.isEmpty() == false) {
                    for (Integer fnc: functionsToExpand) {
                        HashSet<Integer> edges = hashedFnc.getValue();
                        edges.remove(fnc);
                        for (Integer inner : this.graph.get(fnc)) {
                            edges.add(inner);
                        }
                    }
                }
            }
        }
    }

    public void debugCallGraph() {
        for (String name: reverseTable.keySet()) {
            String key = name.toString();
            String value = reverseTable.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    public void analyze(SupportGraph sg, int T_SUPPORT, double T_THRESHOLD) {
        for (Map.Entry<Integer, HashSet<Integer>> hashedFnc : this.graph.entrySet()) {
            HashSet<Integer> children = hashedFnc.getValue();

            for (Integer child : children) { // loop through each function called
                HashMap<Integer, Integer> childrenPairs = sg.pairSupport.get(child);

                if (childrenPairs != null) {
                    for (Integer candidate : childrenPairs.keySet()) {
                        if (!children.contains(candidate)) { // pair doesn't exist in children
                            int pairSupportVal = sg.pairSupport.get(child).get(candidate);
                            double T_CONFIDENCE = pairSupportVal/(double)sg.support.get(child);
                            if (T_CONFIDENCE*100 >= T_THRESHOLD && pairSupportVal >= T_SUPPORT) {
                                System.out.print("bug: " + translationTable.get(child) + " in " + translationTable.get(hashedFnc.getKey()) + ", ");
                                String p1 = translationTable.get(child);
                                String p2 = translationTable.get(candidate);
                                if (p1.compareTo(p2) > 0) {
                                    System.out.print("pair: (" + p2 + ", " + p1 + "), ");
                                } else {
                                    System.out.print("pair: (" + p1 + ", " + p2 + "), ");
                                }
                                System.out.print("support: " + pairSupportVal + ", ");
                                System.out.print("confidence: " + String.format("%.2f", T_CONFIDENCE*100) + "%\n");
                            }
                        }
                    }
                }
            }
        }
    }
}
