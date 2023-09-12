package edu.smu.backdroid.symbolic;

/**
 * Base implementation from Tiro at https://github.com/miwong/tiro/
 */
public abstract class Predicate {
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
        NONE,
        AND,
        OR,
        NOT
    }


    private static final Predicate TRUE = new SymbolicExprPredicate(SymbolicExpr.TRUE());
    private static final Predicate FALSE = new SymbolicExprPredicate(SymbolicExpr.FALSE());


    public static Predicate TRUE(){ return TRUE;}
    public static Predicate FALSE(){ return FALSE; }

    public static Predicate combine(Operator unaryOperator, Predicate pred){
        if (pred == null)
            return null;
        return new UnaryPredicate(unaryOperator, pred);
    }

     public static Predicate combine(Operator binaryOperator, Predicate left, Predicate right) {
        if (left == null && right == null) {
            return null;
        } else if (left == null) {
            return right;
        } else if (right == null) {
            return left;
        }
        if (left.equals(right)) {
            return left;
        }

        return new BinaryPredicate(binaryOperator, left, right);
    }

    public abstract Predicate.Operator getOperator();
    public abstract boolean isUnary();
    public abstract boolean isBinary();
    public abstract boolean isExpression();

    public abstract boolean contains(Predicate pred);


    public boolean isNegationOf(Predicate other){
        if(other == null)
            return false;
        if(this.isExpression() && other.isExpression()){
            return ((SymbolicExprPredicate)this).getExpr().isNegationOf(((SymbolicExprPredicate)other).getExpr());
        }
        else if (other.isUnary() && other.getOperator().equals(Operator.NOT)){
            return this.equals(((UnaryPredicate)other).getChild());
        }
        else if (this.isUnary() && this.getOperator().equals(Operator.NOT)){
            return ((UnaryPredicate)this).getChild().equals(other);
        }
        else if (this.isBinary() && other.isBinary()) {
            BinaryPredicate thisPred = (BinaryPredicate)this,
                            otherPred = (BinaryPredicate)other;
            if (thisPred.getOperator().equals(Predicate.Operator.OR)
                    && otherPred.getOperator().equals(Predicate.Operator.OR)) {
                if (thisPred.getLeftChild().isNegationOf(otherPred.getLeftChild())
                        && thisPred.getRightChild().isNegationOf(otherPred.getRightChild())) {
                    return true;
                }
                if (thisPred.getLeftChild().isNegationOf(otherPred.getRightChild())
                        && thisPred.getRightChild().isNegationOf(otherPred.getLeftChild())) {
                    return true;
                }
            } else if (thisPred.getOperator().equals(Predicate.Operator.AND)
                           && otherPred.getOperator().equals(Predicate.Operator.AND)) {

                return thisPred.getLeftChild().isNegationOf(otherPred.getLeftChild())
                        || thisPred.getRightChild().isNegationOf(otherPred.getRightChild());
            }
        } //missing and and or case
        //Case of a vs not(a) and b ==> incompatible
        else if (this.isExpression() && other.isBinary() && other.getOperator().equals(Operator.AND)){
            return this.isNegationOf(((BinaryPredicate)other).getLeftChild())
            || this.isNegationOf(((BinaryPredicate)other).getRightChild());
        }

        else if (other.isExpression() && this.isBinary() && this.getOperator().equals(Operator.AND)){
            return other.isNegationOf(((BinaryPredicate)this).getLeftChild())
            || other.isNegationOf(((BinaryPredicate)this).getRightChild());
        }
        return false;
    }



}