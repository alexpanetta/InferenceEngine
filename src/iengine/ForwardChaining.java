package iengine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ForwardChaining extends Method {

    public ForwardChaining() {
        code = "FC";
        name = "Forward Chaining";

        //initialise variables
        count = new HashMap<>();
        inferred = new HashMap<>();
        agenda = new LinkedList<>();
        solution = new StringBuilder();
    }

    @Override
    public String Solve(String kb, String query) {
        InitSolution(kb);

        //continue looping until 
        while (!agenda.isEmpty()) {
            String p = agenda.remove(0);

            solution.append(p);
            solution.append(", ");

            inferred.put(p, false);

            //unless inferred[p] execue statement block
            if (inferred.get(p) != true) {
                inferred.replace(p, true);

                //for each Horn clause i in whose premise p appears do
                for (String c : count.keySet()) {
                    //check if premise is in clause
                    if (premiseContains(c, p)) {

                        //decrement count of clause
                        int temp = count.get(c);
                        count.replace(c, --temp);

                        if (count.get(c) == 0) {
                            //get first key of count
                            String head = c.split("=>")[1];

                            if (head.equals(query)) {
                                //we have found the solution
                                solution.append(query);
                                return solution.toString();
                            } else {
                                //keep searching
                                agenda.add(head);
                            }
                        }
                    }
                }
            }
        }

        //return null to signal failure to find solution
        return null;
    }

    private boolean premiseContains(String clause, String p) {
        String premise = clause.split("=>")[0];
        String[] conj = premise.split("&");

        //check if the premise is in the clause
        if (conj.length == 1) {
            return premise.equals(p);
        } else {
            return Arrays.asList(conj).contains(p);
        }
    }

    private void InitSolution(String kb) {
        String[] sentences = kb.split(";");
        for (String s : sentences) {
            s = s.replaceAll("\\s", "");
            s = s.trim();
            if (!s.contains("=>")) {
                agenda.add(s);
                //add to inferred list with entry initially set to false
                if (!inferred.containsKey(s)) {
                    inferred.put(s, false);
                }
            } else {
                if (!count.containsKey(s)) {
                    count.put(s, s.split("&").length);
                }
                String[] temp = s.split("=>");

                for (String symbol : temp) {
                    symbol = symbol.replaceAll("\\s", "");
                    if (symbol.contains("&")) {

                        String[] temp1 = symbol.split("&");
                        for (String sym : temp1) {
                            sym = sym.replaceAll("\\s", "");
                            if (!inferred.containsKey(sym)) {
                                inferred.put(sym, false);
                            }
                        }

                    } else {
                        if (!inferred.containsKey(symbol)) {
                            inferred.put(symbol, false);
                        }
                    }
                }
            }
        }
    }
}
