package edu.smu.backdroid.structure;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ManifestComp {
    
    private int type;
    
    private String name;
    
    private Set<String> action_set;
    private Set<String> category_set;
    private List<ManifestData> data;
    //Should be a map from?

    public ManifestComp(int type, String name) {
        this.type = type;
        this.name = name;
        this.action_set = null;
        this.category_set = null;
        this.data = null;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Must be checked before calling getActions()
     * @return
     */
    public boolean hasAction() {
        if (action_set == null)
            return false;
        else if (action_set.isEmpty())
            return false;
        else
            return true;
    }

    public Set<String> getActions() {
        return action_set;
    }

    public void addAction(String action) {
        if (action_set == null)
            action_set = new HashSet<String>();
        
        action_set.add(action);
    }

    /**
     * Must be checked before calling getCategories()
     * @return
     */
    public boolean hasCategory() {
        if (category_set == null)
            return false;
        else if (category_set.isEmpty())
            return false;
        else
            return true;
    }

    public Set<String> getCategories(){
        return category_set;
    }

    public void addCategory(String category){
        if (category_set == null)
            category_set = new HashSet<String>();

        category_set.add(category);
    }

    public boolean hasData(){
        return data != null && !data.isEmpty();
    }

    /**
     * @return the data
     */
    public List<ManifestData> getData() {
        return this.data;
    }

    /**
     * @return the data
     */
    public void addData(ManifestData manifestData) {
        if (this.data == null) {
        this.data = new ArrayList<>();
        }
        this.data.add(manifestData);
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("ManifestComp [");
        result.append("type=");
        result.append(type);
        result.append(", name=");
        result.append(name);
        if(hasAction()){
            result.append(", actions=");
            result.append(action_set);
        }
        if(hasCategory()){
            result.append(", categories=");
            result.append(category_set);
        }
        if(hasData()){
            result.append(", data=");
            result.append(data);
        }
        return result.toString();
    }

}
