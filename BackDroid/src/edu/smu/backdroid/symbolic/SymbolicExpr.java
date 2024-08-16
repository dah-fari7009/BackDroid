package edu.smu.backdroid.symbolic;

import soot.jimple.ConditionExpr;
import soot.jimple.LtExpr;
import soot.jimple.GtExpr;
import soot.jimple.LeExpr;
import soot.jimple.GeExpr;
import soot.jimple.EqExpr;
import soot.jimple.NeExpr;

public abstract class SymbolicExpr{
    public enum Operator{
        //NOT,
        NONE {
            public String toString(){
                return "";
            }
        },
        EQ,
        NEQ,
        GT,
        LT,
        GE,
        LE
    }

    //private final Type type;//?

    protected SymbolicExpr(){

    }

    private static final SymbolicObjContainer TRUE = SymbolicObjContainer.build(new SymbolicObj("TRUE", SymbolicObj.TYPE.CONSTANT)),
                                    FALSE = SymbolicObjContainer.build(new SymbolicObj("FALSE", SymbolicObj.TYPE.CONSTANT)),
                                    NULL = SymbolicObjContainer.build(new SymbolicObj("NULL", SymbolicObj.TYPE.CONSTANT)) ;

    public static SymbolicObjContainer TRUE(){
        return TRUE;
    }

    public static SymbolicObjContainer FALSE(){
        return FALSE;
    }

    public static SymbolicObjContainer NULL(){
        return NULL;
    }


    public boolean isTrue(){
        return this.equals(TRUE);
    }
    public boolean isFalse(){
        return this.equals(FALSE);
    }
    public boolean isNull(){
        return this.equals(NULL);
    }

    public abstract SymbolicExpr.Operator getOperator();
    public abstract boolean isBinary();
    public abstract boolean isUnary();
    public abstract boolean isNegationOf(SymbolicExpr expr);
    public boolean contains(SymbolicExpr other){
        return false;
    }
    
    public static boolean isEqOperator(SymbolicExpr.Operator operator){
        switch(operator) {
            case EQ:
            case GE:
            case LE:
                return true;
            default:
                return false;
        }
    }

    public static boolean isNotEqOperator(SymbolicExpr.Operator operator){
        switch(operator) {
            case NEQ:
            case GT:
            case LT:
                return true;
            default:
                return false;
        }
    }

    public static Operator getNegatedOperator(SymbolicExpr.Operator operator){
        switch(operator){
            case EQ: return Operator.NEQ;
            case NEQ: return Operator.EQ;
            case GT: return Operator.LE;
            case GE: return Operator.LT;
            case LT: return Operator.GE;
            case LE: return Operator.GT;
            default: return Operator.NONE;
        }
    }


    public static SymbolicExpr combine(Operator operator, SymbolicExpr left, SymbolicExpr right){
        if(left == null && right == null)
            return null;
        else if (left == null)
            return right;
        else if (right == null)
            return left;
            //TODO deal with types?
        return new SymbolicBinaryExpr(operator, left, right);
    }

    public static SymbolicExpr.Operator getOperatorFromCond(ConditionExpr expr){
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

    /*public static SymbolicExpr buildSymbolicExpression(ConditionExpr expr){
        //error case
        return buildSymbolicExpression(expr, getOperatorFromCond(expr));
    }

    private static SymbolicExpr buildSymbolicExpression(ConditionExpr expr, SymbolicExpr.Operator operator){
        SymbolicExpr newExpr = new SymbolicExpr();
        SymbolicObj leftO = SymbolicObj.buildSymbolicObj(expr.getOp1()),
                    rightO = SymbolicObj.buildSymbolicObj(expr.getOp2());
        return new SymbolicExpr(leftO, rightO, op);
    }*/

    
}