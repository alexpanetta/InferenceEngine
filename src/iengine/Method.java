package iengine;

import java.util.HashMap;
import java.util.LinkedList;

public abstract class Method {

    public String code; //code used to identify the method at the command line
    public String name; //actual name of the method

    //variables for chaining methods
    public HashMap<String, Integer> count;
    public HashMap<String, Boolean> inferred;
    public LinkedList<String> agenda;
    public StringBuilder solution;

    public abstract String Solve(String kb, String query);
}
