package edu.smu.backdroid.symbolic;

import java.lang.StringBuilder;
import soot.SootMethod;
import soot.SootField;
import soot.jimple.FieldRef;

public class SymbolicFieldAccess extends SymbolicObj {
    //private String className, methodName;
    private final SymbolicObjContainer base;
    private final String fieldName, fieldClass;


    /*public SymbolicMethodCall(){
        super()
    }*/
    public SymbolicFieldAccess(String name, SymbolicObj.TYPE type, SymbolicObjContainer base, SootField field){
        super(name, type);
        this.base = base;
        this.fieldName = field.getName();
        this.fieldClass = field.getDeclaringClass().getShortName();
    }

    public String toString(){
        //StringBuilder str = new StringBuilder(type.toString()+"_"+super.toString()+"(");
        return ((base != null) ? base.toString() : fieldClass) +"."+fieldName;
    }

}