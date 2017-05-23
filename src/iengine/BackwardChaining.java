package iengine;

import java.util.HashMap;
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
    }

    @Override
    public String Solve(String kb, String query) {
        InitSolution(kb);
        
        //return null to signal a failure to find solution
        return null;
    }
    
    private void InitSolution(String kb) {
        
    }
}
