import javafx.util.Pair;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryRewriter {
    private Map<String, Integer> varToComponent;
    private List<List<Pair<String, String>>> connectedComponents; // Pair stores var name and location tuple.
    private Map<Pair<Integer, Integer>, List<Pair<String, String>>> crossEdges; // Track cross edges created by where clause.

    private List<List<String>> forClauseStr;
    private List<List<String>> whereClauseStr;
    private List<List<String>> returnClauseStr;

    private String lastReturnString = null;
    private final static String RESULT_START = "for $tuple in\n";
    private final static String RETURN = "return";
    private final static String TUPLE_START = "<tuple> {";
    private final static String TUPLE_END = "} </tuple>";
    private final static String JOIN_START = "join (\n";

    private String prevVarPattern = "^(\\$.*?)\\/";
    private String varNamePattern = "\\$([a-zA-Z0-9_]+)";

    public QueryRewriter() {
        this.varToComponent = new HashMap<>();
        this.connectedComponents = new ArrayList<>();
        this.crossEdges = new HashMap<>();

        this.whereClauseStr = new ArrayList<>();
        this.forClauseStr = new ArrayList<>();
        this.returnClauseStr = new ArrayList<>();
    }

    /*
     * Take in a XQueryParser xq as an argument, parse for, where and return clause,
     * construct an undirected graph based on variables in for clause, store edges created by where clause,
     * generate output by adding edges from where clause to for clause.
     */
    public String rewrite(XQueryParser.XqContext xqContext) {
        // No error checking here.
        XQueryParser.ForClauseContext forClauseContext = (XQueryParser.ForClauseContext) xqContext.getChild(0);
        XQueryParser.WhereClauseContext whereClauseContext = (XQueryParser.WhereClauseContext) xqContext.getChild(1);
        XQueryParser.ReturnClauseContext returnClauseContext = (XQueryParser.ReturnClauseContext) xqContext.getChild(2);

        // Construct graph by parsing for clause.
        for (int i = 0; i < forClauseContext.var().size(); ++i) {
            // Create mapping between a var name and node location.
            String varName = forClauseContext.var(i).getText();
            String location = forClauseContext.xq(i).getText();

            // Create varname to location pair.
            Pair<String, String> pair = new Pair<>(varName, location);

            if (location.startsWith("doc")) {
                // Treat this as a new component in our graph. Add a new list to connected components
                this.varToComponent.put(varName, this.connectedComponents.size());
                this.whereClauseStr.add(new ArrayList<>());
                this.connectedComponents.add(new ArrayList<>());
                // Create a new string in for clause string.
                List<String> clauses = new ArrayList<>();
                clauses.add("for " + varName + " in " + location + ",\n");
                this.forClauseStr.add(clauses);
                // Create a new string in return clause string.
                clauses = new ArrayList<>();
                clauses.add(this.RETURN + "\n");
                clauses.add("\t" + this.TUPLE_START + "\n");
                clauses.add("\t\t<" + varName.substring(1) + ">{" + varName + "}</" + varName.substring(1) + ">,\n");
                this.returnClauseStr.add(clauses);
            } else {
                // Get the prev varName in location string by applying regex.
                String prev = null;
                Pattern p = Pattern.compile(this.prevVarPattern);
                Matcher m = p.matcher(location);
                if (m.find())
                    prev = m.group(1);
                else {
                    System.err.println("prev node not found in location in QueryRewriter class.");
                    System.exit(1);
                }
                // Add var name to graph.
                this.varToComponent.put(varName, this.varToComponent.get(prev));
                // Add another for clause in current component's clause string.
                this.forClauseStr.get(this.varToComponent.get(prev)).add("\t" + varName + " in " + location + ",\n");
                // Add another return clause in current component's return clause string.
                this.returnClauseStr.get(this.varToComponent.get(prev)).add("\t\t<" + varName.substring(1) + ">{" + varName + "}</" + varName.substring(1) + ">,\n");
            }
            int idx = this.varToComponent.get(varName);
            this.connectedComponents.get(this.varToComponent.get(varName)).add(pair);
        }

        // Parse where clause
        this.evalCond(whereClauseContext.cond());

        // Parse return clause
        this.evalReturn(returnClauseContext);

        return this.generateResult();
    }

    /*
     * Helper function to generate result of rewrite
     */
    private String generateResult() {
        // Check if a join is possible
        if (this.connectedComponents.size() < 2) {
            return "unable to perform a join";
        }
        StringBuilder res = new StringBuilder();
        res.append(this.RESULT_START);

        // Implement union find
        int[] components = new int[this.connectedComponents.size()];
        // Initialize value of groups to index
        for (int i = 0; i < components.length; ++i)
            components[i] = i;

        for (Pair<Integer, Integer> pair: this.crossEdges.keySet()) {
            int id1 = pair.getKey();
            int id2 = pair.getValue();
            if (!this.find(components, id1, id2))
                this.union(components, id1, id2);
        }

        // Collect components into groups
        Map<Integer, List<Integer>> groups = new HashMap<>();
        for (int i = 0; i < this.connectedComponents.size(); ++i) {
            int rootId = this.root(components, i);
            if (!groups.containsKey(rootId))
                groups.put(rootId, new ArrayList<>());
            groups.get(rootId).add(i);
        }

        List<List<Integer>> joinTables = new ArrayList<>();
        joinTables.addAll(groups.values());
        Collections.sort(joinTables, (o1, o2) -> {
            int size1 = 0, size2 = 0;
            for (int id: o1)
                size1 += this.connectedComponents.get(id).size();
            for (int id: o2)
                size2 += this.connectedComponents.get(id).size();
            return size1 - size2;
        });

        String totalJoin = "";
        for (List<Integer> tableGroup: joinTables) {
            if (tableGroup.size() == 0) continue;
            // Obtain correct amount of offset
            String offset = "";
            for (int i = 0; i < tableGroup.size()-1; ++i)
                offset += "\t";
            Collections.sort(tableGroup, (o1, o2) -> o1 - o2);
            String inGroupJoin = this.generateForWhereReturn(tableGroup.get(0), offset + "\t");
            for (int i = 1; i < tableGroup.size(); ++i) {
                List<Pair<String, String>> merged = new ArrayList<>();
                for (int j = 0; j < i; ++j) {
                    Pair<Integer, Integer> pair = new Pair<>(j, i);
                    if (i < j) {
                        pair = new Pair<>(i, j);
                    }
                    if (this.crossEdges.containsKey(pair)) {
                        merged.addAll(this.crossEdges.get(pair));
                    }
                }
                inGroupJoin = generateJoin(inGroupJoin, i, merged, offset);
                // Update offset
                offset = offset.substring(0, offset.length() - 1);
            }
            if (totalJoin.equals("")) {
                // Remove comma
                totalJoin = inGroupJoin.substring(0, inGroupJoin.length() - 2) + "\n";
            } else
                totalJoin = this.JOIN_START + totalJoin + ",\n" + inGroupJoin + ", \n" + " [], []\n)\n";
        }
        res.append(totalJoin);

        //// Naive implementation, does not detect cycle.
        //List<Pair<Integer, Integer>> l = new ArrayList<>(this.crossEdges.keySet());
        //Collections.sort(l, (o1, o2) -> {
        //    if (o1.getKey() - o2.getKey() == 0)
        //        return o1.getValue() - o2.getValue();
        //    else
        //        return o1.getKey() - o2.getKey();
        //});
        //// Add cross edges to connect components
        //for (int i = 0; i < l.size(); ++i) {
        //    res.append(this.generateJoin(l.get(i).getKey(), l.get(i).getValue(), ""));
        //}
        // Remove comma
        // Add last return clause
        res.append(this.lastReturnString);
        return res.toString();
    }

    /*
     * Helper method for union find
     */
    private void union(int[] groups, int id1, int id2) {
        int root1 = this.root(groups, id1);
        int root2 = this.root(groups, id2);
        groups[root2] = root1;
    }

    /*
     * Helper method for union find
     */
    private int root(int[] groups, int idx){
        while (groups[idx] != idx) {
            groups[idx] = groups[groups[idx]];
            idx = groups[idx];
        }
        return idx;
    }

    /*
     * Helper method for union find
     */
    private boolean find(int[] groups, int id1, int id2) {
        return this.root(groups, id1) == this.root(groups, id2);
    }

    /*
     * Helper function to generate for, return and where string for specified group
     */
    private String generateForWhereReturn(int componentId, String offset) {
        StringBuilder res = new StringBuilder();
        List<String> currFor = this.forClauseStr.get(componentId);
        List<String> currWhere = this.whereClauseStr.get(componentId);
        List<String> currReturn = this.returnClauseStr.get(componentId);
        // Add for string to res
        for (int j = 0; j < currFor.size(); ++j) {
            // Remove last comma if no where clause
            if (j + 1 == currFor.size() && currWhere.size() == 0) {
                String temp = currFor.get(j);
                res.append(offset + temp.substring(0, temp.length()-2) + "\n");
            } else
                res.append(offset + currFor.get(j));
        }
        // Add where string to res
        for (int i = 0 ; i < currWhere.size(); ++i) {
            res.append(offset + currWhere.get(i));
        }
        // Add return string to res
        for (int i = 0; i < currReturn.size(); ++i) {
            // Remove last comma
            if (i + 1 == currReturn.size()) {
                String temp = currReturn.get(i);
                res.append(offset + temp.substring(0, temp.length()-2) + "\n");
            } else
                res.append(offset + currReturn.get(i));
        }
        // Append closing tuple tag for return string
        res.append(offset + "\t" + this.TUPLE_END + ",\n");
        return res.toString();
    }

    /*
     * Helper function to combine two connected components
     * Assume two components have cross edges
     */
    private String generateJoin(String prevJoin, int id, List<Pair<String, String>> merged, String offset) {
        StringBuilder res = new StringBuilder();
        res.append(offset + this.JOIN_START);
        // Append prevJoin result
        res.append(prevJoin);
        // Add an empty line between two groups
        res.append("\n");
        // Generate string for second group
        res.append(this.generateForWhereReturn(id, offset + "\t"));
        // Generate string for joinVars
        StringBuilder var1 = new StringBuilder();
        StringBuilder var2 = new StringBuilder();

        // Add merged vars to joinVars
        for (int i = 0; i < merged.size(); ++i) {
            String tmp1 = merged.get(i).getKey();
            String tmp2 = merged.get(i).getValue();
            // Strip off $
            if (i == merged.size() - 1) {
                var1.append(tmp1.substring(1));
                var2.append(tmp2.substring(1));
            } else {
                var1.append(tmp1.substring(1) + ", ");
                var2.append(tmp2.substring(1) + ", ");
            }
        }
        // Concatenate res with var1 and var2
        res.append(offset + "\t[" + var1 + "], [" + var2 + "]\n");
        // Closing join parentheses
        res.append(offset + "),\n");
        return res.toString();
    }

    /*
     * Helper function to parse last return clause
     */
    private void evalReturn(XQueryParser.ReturnClauseContext returnClauseContext) {
        // add tuple to prefix all variables
        this.lastReturnString = returnClauseContext.getText().replaceAll(varNamePattern, "\\$tuple/$1/*");
        // insert space after return
        this.lastReturnString = this.lastReturnString.replaceFirst("return", "return ");
        // insert space before and after {}
        this.lastReturnString = this.lastReturnString.replaceFirst("(\\{.*\\})", " $1 ");
        // insert space after ,
        this.lastReturnString = this.lastReturnString.replaceAll(",", ", ");
    }

    /*
     * Helper function to recursively evaluate cond.
     */
    private void evalCond(XQueryParser.CondContext condContext) {
        // cond 'and' cond
        if (condContext.getChild(1).getText().equals("and")) {
            this.evalCond((XQueryParser.CondContext) condContext.getChild(0));
            this.evalCond((XQueryParser.CondContext) condContext.getChild(2));
        } else if (condContext.getChild(1).getText().equals("=") || condContext.getChild(1).getText().equals("eq")) {
            // check if the edge is a cross edge.
            String str1 = condContext.getChild(0).getText();
            String str2 = condContext.getChild(2).getText();
            if (!str1.startsWith("$")) {
                this.whereClauseStr.get(this.varToComponent.get(str2)).add("where " + str1 + " " + condContext.getChild(1) + " " + str2 + "\n");
            } else if (!str2.startsWith("$")) {
                this.whereClauseStr.get(this.varToComponent.get(str1)).add("where " + str1 + " " + condContext.getChild(1) + " " + str2 + "\n");
            } else {
                // join two vars together. obtain component id
                int id1 = this.varToComponent.get(str1);
                int id2 = this.varToComponent.get(str2);
                if (id1 == id2)
                    // add non cross edge to whereClauseStr
                    this.whereClauseStr.get(this.varToComponent.get(id1)).add(condContext.getText());
                else {
                    // always make sure smaller index comes first.
                    if (id2 < id1) {
                        int temp = id1;
                        id1 = id2;
                        id2 = temp;
                        String st = str1;
                        str1 = str2;
                        str2 = st;
                    }
                    // add cross edge to crossEdges.
                    Pair<Integer, Integer> idToId = new Pair<Integer, Integer>(id1, id2);
                    if (!this.crossEdges.containsKey(idToId))
                        this.crossEdges.put(idToId, new ArrayList<>());
                    this.crossEdges.get(new Pair<Integer, Integer> (id1, id2)).add(new Pair<String, String> (str1, str2));
                }
            }
        }
    }
}
