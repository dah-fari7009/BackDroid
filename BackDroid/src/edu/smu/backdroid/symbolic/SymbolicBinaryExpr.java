package edu.smu.backdroid.symbolic;

public class SymbolicBinaryExpr extends SymbolicExpr{
    private SymbolicExpr.Operator op; 
    private SymbolicExpr left, right;

    public SymbolicBinaryExpr(SymbolicExpr.Operator op, SymbolicExpr left, SymbolicExpr right){
        this.left = left;
        this.right = right;
        this.op = op;
    }

    public boolean isBinary() {return true;}
    public boolean isUnary() {return false;}

    @Override
    public SymbolicExpr.Operator getOperator() {
        return op;
    }

    public SymbolicExpr getLeft(){
        return left;
    }

    public SymbolicExpr getRight(){
        return right;
    }

    @Override
    public String toString(){
        return "( " + left.toString() + " " + op.toString() + " " + right.toString() + " )";
    }

    public boolean contains(SymbolicExpr other){
        if(this.equals(other))
            return true;
        return left.contains(other) || right.contains(other);
    }

    public boolean equals(Object obj){
        if(obj == null || !(obj instanceof SymbolicBinaryExpr))
            return false;
        SymbolicBinaryExpr other = (SymbolicBinaryExpr)obj;
        return op.equals(other.getOperator())
            && left.equals(other.getLeft())
            && right.equals(other.getRight());
    }

    public boolean isNegationOf(SymbolicExpr other){
        //Need to be reduced first?
        if(other == null || !(other instanceof SymbolicBinaryExpr))
            return false;
        SymbolicBinaryExpr otherExpr = (SymbolicBinaryExpr)other;
        if(left.equals(otherExpr.getLeft()) && right.equals(otherExpr.getRight())){
            //if(op.equals(Operator.EQ) && other.getOperator())
        }
        return false;
    }
}