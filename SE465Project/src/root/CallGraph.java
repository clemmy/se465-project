package root;

import java.util.*;
import java.io.*;

public class CallGraph {

    HashMap<Integer, HashSet<Integer>> graph;
    ArrayList<String> translationTable;
    HashMap<String, Integer> reverseTable;

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
                    }

                    graph.put(id, new HashSet<Integer>());

                } else if (line.startsWith("  CS<")) { // inside function

                    String functionName = line.split("\'")[1];

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
                } else if (line.startsWith("Call graph node <<null function>>")) {
                    String uselessString;
                    while (br.readLine().startsWith("  CS<0x0>")) {}
                }
            }
        } catch (Exception e) {

        }
    }

    public void debugCallGraph() {
        for (String name: reverseTable.keySet()){
            String key = name.toString();
            String value = reverseTable.get(name).toString();
            System.out.println(key + " " + value);
        }
    }

    public void analyze(SupportGraph sg, int T_SUPPORT, double T_THRESHOLD) {
        for (HashMap.Entry<Integer, HashSet<Integer>> hashedFnc : this.graph.entrySet()) {
            HashSet<Integer> children = hashedFnc.getValue();

            for (Integer child : children) { // loop through each function called
                HashMap<Integer, Integer> childrenPairs = sg.pairSupport.get(child);

                for (Integer candidate : childrenPairs.keySet()) {
                    if (!children.contains(candidate)) { // pair doesn't exist in children
                        double pairSupportVal = (double)sg.pairSupport.get(child).get(candidate);
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
                            System.out.print("confidence: " + String.format( "%.2f", T_CONFIDENCE*100 ) + "%\n");
                        }
                    }
                }
            }
        }
    }
}
