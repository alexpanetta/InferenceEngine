package iengine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class ForwardChaining extends Method {

    public ForwardChaining() {
        code = "FC";
        name = "Forward Chaining";

        //initialise variables
        Count = new HashMap<>();
        Inferred = new HashMap<>();
        Agenda = new LinkedList<>();
        Solution = new StringBuilder();
    }

    @Override
    public String Solve(String kb, String query) {
        InitSolution(kb);

        //continue looping until 
        while (!Agenda.isEmpty()) {
            String p = Agenda.pop();

            Solution.append(p);
            Solution.append(", ");

            Inferred.put(p, false);

            //unless inferred[p] execue statement block
            if (!Inferred.get(p)) {
                Inferred.replace(p, true);

                //for each Horn clause i in whose premise p appears do
                for (String c : Count.keySet()) {

                    //check if premise is in clause
                    if (premiseContains(c, p)) {

                        //decrement the count of the clause
                        int temp = Count.get(c);
                        Count.replace(c, --temp);

                        if (Count.get(c) == 0) {
                            //get the head of the clause
                            String head = c.split("=>")[1];

                            if (head.equals(query)) {
                                //we have found the solution
                                Solution.append(query);
                                return Solution.toString();
                            } else {
                                //keep searching by adding it to the stack
                                Agenda.add(head);
                            }
                        }
                    }
                }
            }
        }

        //return null to signal a failure to find solution
        return null;
    }

    private boolean premiseContains(String clause, String p) {
        String premise = clause.split("=>")[0];
        String[] conjunction = premise.split("&");

        //check if p is in the premise
        if (conjunction.length == 1) {
            return premise.equals(p);
        } else {
            return Arrays.asList(conjunction).contains(p);
        }
    }

    private void InitSolution(String kb) {
        //get rid of spaces
        kb = kb.replaceAll("\\s", "");

        //seperate clauses
        String[] clauses = kb.split(";");

        for (String clause : clauses) {
            if (!clause.contains("=>")) {
                //adds facts to the agenda in order to be processed
                Agenda.add(clause);

                //add to inferred list with symbol initially set to false
                Inferred.put(clause, false);

            } else {
                //add premise along with the number of symbols in the clause
                Count.put(clause, clause.split("&").length);

                String[] splitImplication = clause.split("=>");

                for (String propSymbol: splitImplication) {
                    if (propSymbol.contains("&")) {
                        String[] symbols = propSymbol.split("&");

                        for (String symbol : symbols) {
                            Inferred.put(symbol, false);
                        }
                    }
                    else {
                        Inferred.put(clause, false);
                    }
                }
            }
        }
    }
}
