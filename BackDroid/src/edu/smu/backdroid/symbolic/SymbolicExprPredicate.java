package edu.smu.backdroid.symbolic;

public class SymbolicExprPredicate extends Predicate{
    private SymbolicExpr expr;
    
    public SymbolicExprPredicate(SymbolicExpr expr){
        this.expr = expr;
    }

    @Override
    public Predicate.Operator getOperator() {
        return Predicate.Operator.NONE;
    }

    @Override public boolean isExpression() { return true; }
    @Override public boolean isUnary() { return false; }
    @Override public boolean isBinary() { return false; }

    public SymbolicExpr getExpr() {
        return expr;
    }


    public boolean contains(Predicate other) {
        return this.equals(other);
    }

    public String toString(){
        return expr.toString();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof SymbolicExprPredicate)) {
            return false;
        }
        SymbolicExprPredicate other = (SymbolicExprPredicate)obj;
        return expr.equals(other.getExpr());
    }
}