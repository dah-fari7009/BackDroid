package edu.smu.backdroid.symbolic;

import java.lang.StringBuilder;
import java.util.ArrayList;
import soot.SootMethod;

public class SymbolicMethodCall extends SymbolicObj {
    private String className, methodName;
    private final SymbolicObjContainer base;
    private SymbolicObjContainer[] parameters;


    /*public SymbolicMethodCall(){
        super()
    }*/
    public SymbolicMethodCall(SootMethod method, String value, SymbolicObj.TYPE type, SymbolicObjContainer base, SymbolicObjContainer[] parameters){
        super(value, type);
        this.base = base;
        this.parameters = parameters;
        this.className = method.getDeclaringClass().getName();
        this.methodName = method.getName();
    }

    public SymbolicMethodCall(String method, SymbolicObj.TYPE type, SymbolicObjContainer base, SymbolicObjContainer[] parameters){
        //super(formatMethodName(method), type);
        super(method);
        this.base = base;
        this.parameters = parameters;
        this.className = "";
        this.methodName = method ;
    }

    public String getMethodName(){
        return methodName;
    }

    public boolean removeParameter(SymbolicObjContainer param){
        boolean updated = false;
        int totalLength = parameters.length;
        ArrayList<SymbolicObjContainer> newParams = new ArrayList<>();
        for(int i = 0; i < parameters.length; i ++){
            if(param.equals(parameters[i])){
                parameters[i] = null;
                totalLength --;
                updated = true;
            }
            else newParams.add(parameters[i]);
        }
        if(updated){
            parameters = new SymbolicObjContainer[totalLength];
            parameters = newParams.toArray(parameters);

        }
        return updated;
    }

    public int hasParameter(SymbolicObjContainer param){
        for(int i = 0; i < parameters.length; i ++){
            if(param.equals(parameters[i]))
                return i;
        }
        return -1;
    }

    public String toString(){
        //StringBuilder str = new StringBuilder(type.toString()+"_"+super.toString()+"(");
        String start = (base == null) ? 
        super.toString() : type.toString() + base.toString()+"."+ methodName;
        StringBuilder str = new StringBuilder(start+"(");
        for(SymbolicObjContainer param: parameters)
            str.append(param.toString()+",");
        str.append(")");
        return str.toString();
    }

}