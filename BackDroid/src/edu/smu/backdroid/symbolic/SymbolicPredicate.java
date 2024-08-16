package edu.smu.backdroid.symbolic;

import soot.jimple.ConditionExpr;
/**
 * Class representing a boolean condition
 */
public class SymbolicPredicate{ //TODO unary, binary thingie
    private SymbolicPredicate.Operator op;
    private SymbolicPredicate left, right;
    /**
     * This class will serve as a base representation for the constraints in our graph
     * What we want is to replace the taints tracked by the BDG with symbolic variables, so that we also keep the most update value to put in the constraints
     * e.g. $z0 == 0
     * ==> $r9.equals($r1) == 0 after reading the definition $z0 = $r9.equals($r1)
     * ==> $r9.equals("ios") == 0 after reading the defintition $r1 = "ios"
     * and so on ...
     * 
     * We will also use it to build placeholder nodes
     * e.g., for branches that are ignored but we would still like to put it into the graph?
     * 
     * We need model for different operations and APIs
     * In particular, SharedPreferences, equals and other string operations
     * See Tiro or TSOpen for that
     */

    public enum Operator {
        NONE {
            public String toString(){
                return "";
            }
        },
        AND,
        OR
    }

    public void combinePredicate(SymbolicPredicate newPredicate, SymbolicPredicate.Operator op){
        //here we want op to be the new operator
        //and then left is gonna become this and right is gonna become newPredicate
            //if(left == null)
    
    }
    //Note: expression should be two objects??, to double check

}

class SymbolicExprPredicate extends SymbolicPredicate{
    private SymbolicExpr expr;
    
    public SymbolicExprPredicate(SymbolicExpr expr){
        super(this, null, SymbolicPredicate.NONE)
        this.expr = expr;
    }

    public static get

    private class SymbolicExpr{
        private SymbolicExpr.Operator op; 
        private SymbolicObj left, right;
        public enum Operator{
            //NOT,
            EQ,
            NEQ,
            GT,
            LT,
            GE,
            LE
        }

        private SymbolicExpr(SymbolicObj left, SymbolicObj right, SymbolicExpr.Operator op){
            this.left = left;
            this.right = right;
            this.op = op;
        }

        public static getOperatorFromCond(ConditionExpr){
            SymbolicExpr.Operator op = null;
            if(expr instanceof EqExpr)
                op = Operator.EQ;
            else if(expr instanceof NeExpr)
                op = Operator.NEQ;
            else if (expr instanceof GeExpr)
                op = Operator.GE;
            else if (expr instanceof LeExpr)
                op = Operator.LE;
            else if (expr instanceof GtExpr)
                op = Operator.GT;
            else if (expr instanceof LtExpr)
                op = Operator.LT;
            return op;
        }

        public static SymbolicExpr buildSymbolicExpression(ConditionExpr expr){
            //error case
            return buildSymbolicExpression(expr, getOperatorFromCond(expr));
        }

        private static SymbolicExpr buildSymbolicExpression(ConditionExpr expr, SymbolicExpr.Operator operator){
            SymbolicExpr newExpr = new SymbolicExpr();
            SymbolicObj leftO = SymbolicObj.buildSymbolicObj(expr.getOp1()),
                        rightO = SymbolicObj.buildSymbolicObj(expr.getOp2());
            return new SymbolicExpr(leftO, rightO, op);

        }

        public String toString(){
            return left.toString() + " " + op.toString() + " " + right.toString();
        }
    }
}