package edu.smu.backdroid.analysis;

import edu.smu.backdroid.symbolic.*;

/**
 * Class for mapping extracted symbolic constraints to patterns
 * e.g. System.currentTimeMillis > 895T6 => TIME
 */
public class PatternDetector {
    private Set<PATTERN> identifiedPatterns;
    
    public enum PATTERN{
        SOFTWARE,
        HARDWARE,
        ENVIRONMENT,
        INFO,
        EQUIPMENT,
        SERVER,
        ALTERNATE,
        INCOMPLETE_EXPLORATION,
        UNKNOWN

        //public final String label;
    }

    public PatternDetector(){
        this.identifiedPatterns = new HashSet<>();
    }

    public static Set<PATTERN> match(Predicate predicate){
        Set<PATTERN> patterns = new HashSet<>();
        if(predicate.isUnary()){
            UnaryPredicate pred = (UnaryPredicate)predicate;
            //TODO deal with operator if keeping constraint, for NEQ
            patterns.add(match(pred.getChild()))
        }
        else if(predicate.isBinary()){
            BinaryPredicate pred = (BinaryPredicate)predicate;
            patterns.add(PatternDetector.match(pred.getLeftChild()));
            patterns.add(PatternDetector.match(pred.getRightChild()));
        }
        else{
            SymbolicExprPredicate pred = (SymbolicExprPredicate)predicate;
            patterns.add(pred.getExpr())
        }
        return patterns;
    }

    public static Set<PATTERN> match(SymbolicExpr expr){
        Set<PATTERN> patterns = new HashSet<>();
        if(expr instanceof SymbolicBinaryExpr){
            expr = (SymbolicBinaryExpr)expr;
            patterns.add(PatternDetector.match(expr.left));
            patterns.add(PatternDetector.match(expr.right));
        }
        else if(expr instanceof SymbolicObjContainer){
            expr = (SymbolicObjContainer)expr;
            SymbolicObj obj = (SymbolicObj)child;
            switch(expr.getType()){
                case SymbolicObjContainer.TYPE.METHOD:
                    SymbolicMethodCall sm = (SymbolicMethodCall)obj;
                    patterns.add(PatternDetector.match(sm));
                    break;
                case SymbolicObjContainer.TYPE.PHI:
                    break;
                default: //any type
                    break;
            }
        }
        return patterns;
    }

    private static PATTERN match(SymbolicMethodCall sm){
        //Hardware
        if(sm.getClassName().equals("android.os.SemSystemProperties") && sm.getMethodName().equals("get"))
            return PATTERN.HARDWARE;
        //Software
        //
        //Usage patterns
        if(sm.getClassName().contains("SharedPreferences") && sm.getMethodName().startswith("get")){
            //need to keep the key
            //need to find set to the same key
        }
    }
    
}