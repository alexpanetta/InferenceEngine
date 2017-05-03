package iengine;

import java.util.ArrayList;

public abstract class Method {

    public String code; //code used to identify the method at the command line
    public String name; //actual name of the method

    //extra arrays here for different methods
    public ArrayList<String> facts;
    public ArrayList<String> clauses;
    public ArrayList<Integer> count;
    public ArrayList<String> entailed;
    public ArrayList<String> frontier;

    public abstract String Solve(String kb, String query);
}
