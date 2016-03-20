package root;

import java.util.*;
import java.io.*;

public class CallGraph {

    HashMap<Integer, HashSet<Integer>> graph;
    ArrayList<String> translationTable;
    HashMap<String, Integer> reverseTable;

    public CallGraph(String filename) {

        graph = new HashMap<Integer, HashSet<Integer>>(20000);
        translationTable = new ArrayList<>();
        reverseTable = new HashMap<String, Integer>();

        try {
            String line;
            int lastFuncId = 0;

            BufferedReader br = new BufferedReader(new FileReader(new File(filename)));
            while ((line = br.readLine()) != null) {
                if (line.startsWith("Call graph node for function:")) {
                    String functionName = line.split("\'")[1];

                    translationTable.add(functionName);
                    int id = translationTable.size() - 1;
                    lastFuncId = id;
                    reverseTable.put(functionName, id);

                    graph.put(id, new HashSet<>());

                } else if (line.startsWith("CS<")) {
                    String functionName = line.split("\'")[1];

                    if (!translationTable.contains(functionName)) {
                        translationTable.add(functionName);
                        int id = translationTable.size() - 1;
                        reverseTable.put(functionName, id);
                        graph.put(id, new HashSet<>());
                    } else {
                        int id = reverseTable.get(functionName);
                        HashSet<Integer> edges = graph.get(lastFuncId);
                        edges.add(id);
                        graph.put(lastFuncId, edges);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
