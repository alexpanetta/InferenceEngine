package iengine;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public abstract class Method {

    public String code; //code used to identify the method at the command line
    public String name; //actual name of the method

    //instances for chaining methods and truth tabling
    public HashMap<String, Integer> Count;
    public HashMap<String, Boolean> Inferred;
    public LinkedList<String> Agenda;
    public HashSet<HornClause> KnowledgeBase;   //testing for TT
    public Integer ValidModels;
    public LinkedList<String> Entailed;
    public StringBuilder Solution;

    public abstract String Solve(String kb, String query);     
    
    public void reset() {
        this.Agenda.clear();
        this.Inferred.clear();
        this.Count.clear();
        this.KnowledgeBase.clear();
        this.ValidModels = 0;
        this.Entailed.clear();
        this.Solution.setLength(0);
    }
}
