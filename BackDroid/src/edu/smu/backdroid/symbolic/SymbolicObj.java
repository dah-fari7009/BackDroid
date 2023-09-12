package edu.smu.backdroid.symbolic;
/**
 * Base class for all tracked objects
 */
public class SymbolicObj extends SymbolicExpr{
    private String value;
    private TYPE type;

    public enum TYPE{
        CONSTANT,
        VARIABLE,
        CLASS
    }

    public SymbolicObj(String value){
        this.value = value;
        this.type = TYPE.VARIABLE;
    }

    public SymbolicObj(String value, TYPE type){
        this.value = value;
        this.type = type;
    }

    /*public SymbolicObj(Value value){

    }*/

    public String getValue(){
        return value;
    }

    public void setValue(String value){
        this.value = value;
    }

    public TYPE getType(){
        return type;
    }

    public void setType(TYPE type){
        this.type = type;
    }

    public SymbolicExpr.Operator getOperator(){
        return SymbolicExpr.Operator.NONE;
    }

    public boolean isBinary() {return false;}
    public boolean isUnary() {return true;}
    public boolean isNegationOf(SymbolicExpr other) {
        if(other == null)
            return false;
            //TODO NEQ(x) and x
        return false;
    }

    public boolean matchesPattern(){
        return false; //TODO
    }

    public String toString(){
        return this.value;
    }
}