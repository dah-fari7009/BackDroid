package edu.smu.backdroid.symbolic;

public class SymbolicKeyValueAccess extends SymbolicObj{

    public enum DbType{
        BUNDLE,
        SHARED_PREFS,
        ANY_TABLE
    }

    private final DbType dbType;
    private final SymbolicObjContainer db;
    private final SymbolicObjContainer key;

    public SymbolicKeyValueAccess(String name, SymbolicObj.TYPE type, DbType dbType, SymbolicObjContainer db, SymbolicObjContainer key ){
        super(name, type);
        this.dbType = dbType;
        this.db = db;
        this.key = key;
    }

    public SymbolicKeyValueAccess(String name, SymbolicObj.TYPE type, SymbolicObjContainer db, SymbolicObjContainer key ){
        this(name, type, DbType.ANY_TABLE, db, key);
    }

    public DbType getDbType(){
        return dbType;
    }

    public String toString(){
        return this.dbType+"_"+db.toString()+"["+key.toString()+"]";
    }
}