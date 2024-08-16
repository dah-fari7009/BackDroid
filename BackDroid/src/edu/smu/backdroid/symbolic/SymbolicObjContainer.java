package edu.smu.backdroid.symbolic;

/**
 * Base implementation from Tiro at https://github.com/miwong/tiro/
 */
public class SymbolicObjContainer extends SymbolicExpr{
    private TYPE type;
    private SymbolicObj child;

    public enum TYPE{
        ANY{
            public String toString(){
                return "";
            }
        },
        PHI{
            public String toString(){
                return "";
            }
        },
        METHOD,
        FIELD
    }

    public SymbolicObjContainer(SymbolicObjContainer.TYPE type, SymbolicObj child){
        this.type = type;
        this.child = child;
    }

    public SymbolicObjContainer(SymbolicObj child){
        this(TYPE.ANY, child);
    }

    public SymbolicObj getChild(){
        return this.child;
    }
    
    public SymbolicObjContainer.TYPE getType(){
        return this.type;
    }

    public void setChild(SymbolicObj child){
        this.child = child;
        if(child instanceof SymbolicMethodCall)
            type = TYPE.METHOD;
        else if (child instanceof SymbolicFieldAccess)
            type = TYPE.FIELD;
    }

    public void setType(SymbolicObjContainer.TYPE type){
        this.type = type;
    }

    public static SymbolicObjContainer build(String val){
        return new SymbolicObjContainer(new SymbolicObj(val));
    }

    public static SymbolicObjContainer build(SymbolicObj obj){
        SymbolicObjContainer.TYPE type = TYPE.ANY;
        if(obj instanceof SymbolicMethodCall)
            type = TYPE.METHOD;
        else if (obj instanceof SymbolicFieldAccess)
            type = TYPE.FIELD;
        return new SymbolicObjContainer(type, obj);
    }

    public SymbolicExpr.Operator getOperator(){
        return SymbolicExpr.Operator.NONE;
    }

    public boolean isBinary() {return false;}
    public boolean isUnary() {return true;}
    public boolean isNegationOf(SymbolicExpr other) {
        if(other == null)
            return false;
        if(!(other instanceof SymbolicObjContainer))
            return false;
            //TODO NEQ(x) and x
        SymbolicObjContainer cont = (SymbolicObjContainer)other;
        return child.isNegationOf(cont.getChild());
    }

    @Override
    public String toString(){
        return type.toString()+":"+child.toString();
    }
    
}