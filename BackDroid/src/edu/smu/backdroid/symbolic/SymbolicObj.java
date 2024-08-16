package edu.smu.backdroid.symbolic;

import soot.Type;
import soot.ArrayType;
import soot.BooleanType;
import soot.CharType;
import soot.IntType;
import soot.NullType;
import soot.RefType;
import soot.DoubleType;
import soot.FloatType;
import soot.LongType;
import soot.ShortType;
import java.util.Set;
import java.util.HashSet;
/**
 * Base class for all tracked objects
 */
public class SymbolicObj{
    protected String value;
    protected TYPE type;
    private static final Set<String> stringTypes = new HashSet<>();

    static {
        stringTypes.add("java.lang.String");
        stringTypes.add("java.lang.StringBuffer");
        stringTypes.add("java.lang.StringBuilder");
        stringTypes.add("java.lang.CharSequence");
    }

    public enum TYPE{
        UNKNOWN,
        CONSTANT{
            public String toString(){
                return "CONST";
            }
        },
        VARIABLE,
        CLASS,
        BOOL,
        STRING,
        NUM,
        REF
    }

    public SymbolicObj(String value){
        this.value = value;
        this.type = TYPE.VARIABLE;
    }

    public SymbolicObj(String value, TYPE type){
        this.value = value;
        this.type = type;
    }

    public static TYPE getSymbType(Type type){
        if(type instanceof BooleanType){
            return TYPE.BOOL;
        }
        if(type instanceof IntType || type instanceof ShortType || type instanceof LongType || type instanceof FloatType || type instanceof DoubleType)
            return TYPE.NUM;
        if(type instanceof RefType){
            if(type instanceof NullType){
                return TYPE.CONSTANT;
            }
            if(stringTypes.contains(type.toString()))
                return TYPE.STRING;
            return TYPE.REF;
        }
        return TYPE.UNKNOWN;
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

    public boolean isBinary() {return false;}
    public boolean isUnary() {return true;}
    public boolean isNegationOf(SymbolicObj other) {
        if(other == null)
            return false;
            //TODO NEQ(x) and x
        return false;
    }

    public boolean matchesPattern(){
        return false; //TODO
        //software, hardware, device, time, location, server?
    }

    /*public Pattern getTag(){
        return PatternDetector.getTags()
    }*/

    public String toString(){
        return this.type +"_"+this.value;
    }
}