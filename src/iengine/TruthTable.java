package iengine;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

public class TruthTable extends Method {

    public TruthTable() {
        code = "TT";
        name = "Truth Table";

        //initialise variables
        KnowledgeBase = new HashSet<>();
        Solution = new StringBuilder();
        ValidModels = 0;
    }

    
    /**
     * Finds number of models where KB can entail query using Truth Tabling.
     * @param kb
     * @param query
     * @return ValidModels 
     */
    @Override
    public String Solve(String kb, String query) {
        HashSet<String> symbols = new HashSet<String>();

        //initialise kb and symbols
        symbols = InitSolution(kb, symbols);

        if (!CheckAll(query, symbols, new HashMap<String, Boolean>())) {
            return null;
        } else {
            //output the number of valid models
            return ValidModels.toString();
        }
    }
    
    
    /**
     * Recursive function, for every symbol assign it true/false in the model.
     * There will be 2^(n) models created for n symbols in the KB.
     * Once every model has been created it will loop through and determine
     * which models can allow the KB to entail query.
     * @param query
     * @param symbols
     * @param model
     * @return boolean
     */
    private boolean CheckAll(String query, HashSet<String> symbols, HashMap<String, Boolean> model) {
        if (symbols.isEmpty()) {
            if (CheckKB(model)) {
                //if true check query against model, follows rules of implication (true => false) is false
                if (model.get(query)) {
                    //increment counter as KB entails query
                    ValidModels++;
                    return true;
                } else {
                    return false;
                }
            } else {
                //rules of implication, true if the first symbol of the expression is false
                return true;
            }
        } else {
            //call copy constructor, rest = symbols
            HashSet<String> rest = new HashSet<String>(symbols);
            String p = symbols.iterator().next();
            
            //remove the head of rest and pass it into a recursive function call
            rest.remove(p);

            //two possible states for symbol to be in (true or false)
            //return 
            return (CheckAll(query, rest, Extend(p, true, model)))
                    && (CheckAll(query, rest, Extend(p, false, model)));
        }
    }

    /**
     * CheckTrue is called for every clause and calculates and returns the value
     * of the expression.
     * @param clause
     * @param model
     * @return boolean 
     */
    private boolean CheckTrue(HornClause clause, HashMap<String, Boolean> model) {
        // only use antecedent if there is no consequent, ie a fact (a;)
        if (clause.getConsequent() == null) {
            return model.get(clause.getAntecedent());
        }
        if (clause.getAntSymbols().size() == 1) {
            if (model.get(clause.getAntecedent())) {
                return model.get(clause.getConsequent());
            }
        }
        //if there are two symbols iterate through the list
        else if (clause.getAntSymbols().size() == 2) {
            if (model.get(clause.getAntSymbols().iterator().next()) 
                    && model.get(clause.getAntSymbols().iterator().next())) {
                return model.get(clause.getConsequent());
            }
        }
        
        //otherwise if the antecedent is false always return true
        return true;
    }
    
    
    /**
     * Loop through each clause in the KB and return whether that clause is
     * true/false. If any of the clauses are found to be false, return false.
     * Otherwise if every clause has been validated as true, return true.
     * @param model
     * @return boolean 
     */
    private boolean CheckKB(HashMap<String, Boolean> model) {
        for (HornClause clause : KnowledgeBase) {
            if (!CheckTrue(clause, model)) {
                return false;
            }
        }

        //every clause is valid
        return true;
    }
    

    /**
     * extend model with a boolean value for a particular symbol
     * @param p
     * @param b
     * @param model
     * @return model 
     */
    private HashMap<String, Boolean> Extend(String p, Boolean b, HashMap<String, Boolean> model) {
        model.put(p, b);
        return model;
    }

    /**
     * Concise method to clear spaces in the String kb and setup
     * the KnowledgeBase and symbols variables
     * @param kb
     * @param symbols
     * @return symbols
     */
    private HashSet<String> InitSolution(String kb, HashSet<String> symbols) {
        //remove spaces in the kb sentence
        kb = kb.replaceAll("\\s", "");
        
        ValidModels = 0;

        InitKB(kb);
        return InitSymbols(kb, symbols);
    }

    
    /**
     * Separate clauses into HornClause array list KnowledgeBase.
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

    
    /**
     * Loop through each clause in KnowledgeBase and add the symbol to the HashSet
     * HashSet already prevents duplication and offers linear time complexity 
     * when adding.
     * @param kb
     * @param symbols
     * @return symbols
     */
    private HashSet<String> InitSymbols(String kb, HashSet<String> symbols) {
        for (HornClause clause : KnowledgeBase) {
            AddSymbol(clause.getAntecedent(), symbols);
            AddSymbol(clause.getConsequent(), symbols);
        }

        return symbols;
    }

    
    /**
     * Manage the clause and separate symbols if needed be and then add to symbols
     * @param symbol
     * @param symbols 
     */
    private void AddSymbol(String symbol, HashSet<String> symbols) {
        if (symbol != null) {
            if (symbol.contains("&")) {
                String[] splitSymbols = symbol.split("&");
                symbols.addAll(Arrays.asList(splitSymbols));
            } else {
                symbols.add(symbol);
            }
        }
    }
}
