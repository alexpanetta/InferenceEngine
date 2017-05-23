package iengine;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

class InferenceEngine {

    //should be 3- FC, BC and TT
    public static final int METHOD_COUNT = 2;
    public static Method[] methods;

    public static void main(String[] args) {

        //initialise methods
        initMethods();

        if (args.length < 2) {
            System.out.println("Usage: iengine <method> <filename>");
            System.exit(1);
        }

        String method = args[0];

        //file[0] is KB/tell and file[1] is the query/ask
        String[] file = readFile(args[1]);

        Method thisMethod = null;

        for (int i = 0; i < METHOD_COUNT; i++) {
            if (methods[i].code.compareTo(method) == 0) {
                //use the method the user entered in command prompt
                thisMethod = methods[i];
            }
        }

        if (thisMethod == null) {
            System.out.println("Method identified by " + method + " not implemented. "
                    + "Methods are case sensitive");
            System.exit(1);
        }

        //pass kb and query to method to calculate solution
        String thisSolution = thisMethod.Solve(file[0], file[1]);
        
        if (thisSolution == null) {
            System.out.println("NO:");
        }
        else
            System.out.println("YES: " + thisSolution);

        //get initial kb and store?
    }

    private static void initMethods() {
        methods = new Method[METHOD_COUNT];
        methods[0] = new ForwardChaining();
        methods[1] = new BackwardChaining();
    }

    private static String[] readFile(String fileName) {
        try {
            //create file reading objects
            FileReader reader = new FileReader(fileName);
            BufferedReader file = new BufferedReader(reader);

            String line;
            String[] result = new String[2];

            //skip first line (TELL)
            file.readLine();

            line = file.readLine(); //store kb
            result[0] = line;

            file.readLine();        //skip third line (ASK)

            //retrieve query from the file
            line = file.readLine();
            result[1] = line;

            return result;
        } catch (FileNotFoundException ex) {
            //The file didn't exist, show an error
            System.out.println("Error: File \"" + fileName + "\" not found.");
            System.out.println("Please check the path to the file.");
            System.exit(1);
        } catch (IOException ex) {
            //There was an IO error, show and error message
            System.out.println("Error in reading \"" + fileName + "\". Try closing it and programs that may be accessing it.");
            System.out.println("If you're accessing this file over a network, try making a local copy.");
            System.exit(1);
        }

        //this code should be unreachable. This statement is simply to satisfy Eclipse.
        return null;
    }
}
