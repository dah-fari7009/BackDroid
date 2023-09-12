package edu.smu.backdroid.symbolic;

/**
 * Base implementation from Tiro at https://github.com/miwong/tiro/
 */
public class BinaryPredicate extends Predicate{
    private final Operator operator;
    private Predicate leftChild, rightChild;

    public BinaryPredicate(Operator operator, Predicate leftChild, Predicate rightChild){
        this.operator = operator;
        this.leftChild = leftChild;
        this.rightChild = rightChild;
    }

    @Override
    public Predicate.Operator getOperator() {
        return operator;
    }

    @Override public boolean isExpression() { return false; }
    @Override public boolean isUnary() { return false; }
    @Override public boolean isBinary() { return true; }

    public Predicate getLeftChild() {
        return leftChild;
    }

    public Predicate getRightChild() {
        return rightChild;
    }

    public String toString(){
        return "BIN(" + leftChild.toString()+ " " + operator.toString() + " " + rightChild.toString()+")BIN";
    }

    //@Override
    /*public boolean containsExpression(Expression expression) {
        return leftChild.containsExpression(expression)
                || rightChild.containsExpression(expression);
    }*/

    @Override
    public boolean contains(Predicate other) { //TO DOUBLE CHECK
        if (this.equals(other)) {
            return true;
        }
        if (operator.equals(Predicate.Operator.AND)) {
            return leftChild.contains(other) || rightChild.contains(other);
        } else if (operator.equals(Predicate.Operator.OR)) {
            return leftChild.contains(other) && rightChild.contains(other);
        }
        return false;
    }


}