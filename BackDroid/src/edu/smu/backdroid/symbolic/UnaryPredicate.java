package edu.smu.backdroid.symbolic;

/**
 * Base implementation from Tiro at https://github.com/miwong/tiro/
 */
public class UnaryPredicate extends Predicate{
    private final Operator operator;
    private Predicate child;

    public UnaryPredicate(Operator operator, Predicate child){
        this.operator = operator;
        this.child = child;
    }

    @Override
    public Predicate.Operator getOperator() {
        return operator;
    }

    @Override public boolean isExpression() { return false; }
    @Override public boolean isUnary() { return true; }
    @Override public boolean isBinary() { return false; }

    public Predicate getChild() {
        return child;
    }

    public boolean contains(Predicate other) {
        return this.equals(other);
    }

    public String toString(){
        return //"UNARY_ 
        "("+operator.toString()+" "+child.toString()+")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof UnaryPredicate)) {
            return false;
        }

        UnaryPredicate other = (UnaryPredicate)obj;
        return operator.equals(other.getOperator()) && child.equals(other.getChild());
    }


}