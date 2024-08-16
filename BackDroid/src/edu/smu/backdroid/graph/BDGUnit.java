package edu.smu.backdroid.graph;

import soot.Unit;
import edu.smu.backdroid.symbolic.*;

/**
 * a node in BDG
 * 
 * @author Daoyuan
 * @since 13-04-13
 * @restart 17-06-06
 * updated by Faridah
 * 
 * This class will also serve to maintain per-unit flow information (i.e., collecting constraints) for implementing merge or flow through operations
 */
public class BDGUnit {
    
    public static int NODE_ID = 0;
    
    private int id;
    
    /**
     * this node is located in which method
     * 
     * <com.nupt.stitp.MobileSecService: void onStart(android.content.Intent,int)>
     */
    private String msig;
    
    private Unit unit;

    private boolean isNeeded = true;

    private Predicate predicate;

    //private Constraint ...

    public BDGUnit(String msig, Unit unit) {
        this.msig = msig;
        this.unit = unit;
        this.id = NODE_ID++;
        this.predicate = Predicate.TRUE();
        //TODO default predicate to TRUE and minimize

    }


    /*public void combine(Predicate newPredicate){
        if(predicate.equals(Predicate.Operator.NONE));
            predicate = newPredicate;
    }*/

    public Predicate getPredicate(){
        return predicate;
    }

    public void setPredicate(Predicate predicate){
        this.predicate = predicate;
    }


    public boolean isNeeded(){
        return isNeeded;
    }

    public void setNeeded(boolean isNeeded){
        this.isNeeded = isNeeded;
    }

    public String getMSig() {
        return msig;
    }

    public void setMSig(String msig) {
        this.msig = msig;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }
    
    public String getUnitStr() {
        return unit.toString();
    }
    
    /**
     * After implementing the hashCode(),
     * We can use it for contains() in a Set/List
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        
        BDGUnit other = (BDGUnit) obj;
        if (this.id != other.id)
            return false;
        if (!this.msig.equals(other.msig))
            return false;
        if (!this.getUnitStr().equals(other.getUnitStr()))
            return false;
        
        return true;
    }
    
    /**
     * For using contains() in Set/List
     * We must override this function
     * 
     * @see https://stackoverflow.com/a/17104610/197165
     * @see https://www.sitepoint.com/how-to-implement-javas-hashcode-correctly/
     * @see https://stackoverflow.com/questions/113511/best-implementation-for-hashcode-method
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((msig == null) ? 0 : msig.hashCode());
        result = prime * result + ((getUnitStr() == null) ? 0 : getUnitStr().hashCode());
        result = prime * result + id;
        return result;
    }

    @Override
    public String toString() {
        return "BDGUnit [msig=" + msig + ", unit=" + unit + "]";
    }
    
    // Only for draw
    public int getDrawId() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
