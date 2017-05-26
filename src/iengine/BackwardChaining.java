package iengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class BackwardChaining extends Method {

    public BackwardChaining() {
        code = "BC";
        name = "Backward Chaining";

        //initialise variables
        Count = new HashMap<>();
        Inferred = new HashMap<>();
        Agenda = new LinkedList<>();
        Solution = new StringBuilder();
        Entailed = new LinkedList<>();
        KnowledgeBase = new HashSet<>();
    }

    @Override
    public String Solve(String kb, String query) {
        InitSolution(kb, query);

        while (!Agenda.isEmpty()) {
            //get the next symbol
            String q = Agenda.pop();

            Entailed.add(q);

            //loop through all the clauses
            for (HornClause clause : KnowledgeBase) {
                //if q is a fact and we can stop otherwise keep executing
                if (clause.getConsequent() == null && clause.getAntecedent().equals(q)) {
                    return ReturnSolution();
                }
            }

            //create a new HashSet to store new symbols
            HashSet<String> p = new HashSet<String>();

            for (HornClause clause : KnowledgeBase) {
                //if there is a clause where q is the consequent, add those symbols
                // to the list
                if (clause.getConsequent() != null && clause.getConsequent().equals(q)) {
                    for (String premise : clause.getAntSymbols()) {
                        p.add(premise);
                    }
                }
            }

            if (p.isEmpty()) {
                //if there are no other premises then KB cannot be entailed by the query
                return null;
            } else {
                //there are still symbols in p
                for (String s : p) {
                    //check if it has been processed before, otherwise add it to
                    //the Agenda
                    if (!Entailed.contains(s)) {
                        Agenda.add(s);
                    }
                }
            }
        }

        return ReturnSolution();
    }

    /**
     * Loops through Entailed and returns a String consisting of symbols separated by commas
     * @return String 
     */
    private String ReturnSolution() {
        for (int i = Entailed.size() - 1; i >= 0; i--) {
            Solution.append(Entailed.get(i));
            Solution.append(", ");
        }

        //remove last comma
        Solution.deleteCharAt(Solution.length() - 1);
        Solution.deleteCharAt(Solution.length() - 1);

        return Solution.toString();
    }

    /**
     * Concise method to clear spaces in the String kb and setup the
     * KnowledgeBase and symbols variables
     *
     * @param kb
     * @param symbols
     * @return symbols
     */
    private void InitSolution(String kb, String query) {
        //remove spaces in the kb sentence
        kb = kb.replaceAll("\\s", "");

        Agenda.add(query);
        InitKB(kb);
    }

    /**
     * Separate clauses into HornClause array list KnowledgeBase.
     *
     * @param kb
     */
    private void InitKB(String kb) {
        //seperate clauses
        String[] clauses = kb.split(";");

        for (String clause : clauses) {
            if (!clause.contains("=>")) {
                //adds facts to the KB
                KnowledgeBase.add(new HornClause(clause.trim(), null));

            } else {
                String[] splitClause = clause.split("=>");
                //add premises to the KB, ie A => B (store A and B)
                KnowledgeBase.add(new HornClause(splitClause[0].trim(), splitClause[1].trim()));
            }
        }
    }
}
