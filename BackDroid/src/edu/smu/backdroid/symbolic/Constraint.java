package edu.smu.backdroid.symbolic;

public class Constraint {
    /**
     * This class will serve as a base representation for the constraints in our graph
     * What we want is to replace the taints tracked by theBDG with symbolic variables, so that we also keep the most update value to put in the constraints
     * e.g. $z0 == 0
     * ==> $r9.equals($r1) == 0 after reading the definition $z0 = $r9.equals($r1)
     * ==> $r9.equals("ios") after reading the defintition $r1 = "ios"
     * and so on ...
     * 
     * We will also use it to build placeholder nodes
     * e.g., for branches that are ignored but we would still like to put it into the graph?
     * 
     * We need model for different operations and APIs
     * In particular, SharedPreferences, equals and other string operations
     * See Tiro or TSOpen for that
     */
}
