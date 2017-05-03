package iengine;

import java.util.ArrayList;

public class ForwardChaining extends Method {

    public ForwardChaining() {
        code = "FC";
        name = "Forward Chaining";

        facts = new ArrayList<>();
        clauses = new ArrayList<>();
        count = new ArrayList<>();
        entailed = new ArrayList<>();
        frontier = new ArrayList<>();

        //initialise arrays here
    }

    @Override
    public String Solve(String kb, String query) {

        String result = "success";

        //do FC here
        return result;
    }
}
