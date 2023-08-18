package edu.smu.backdroid.analysis;


import edu.smu.backdroid.graph.BDG;

public class IntentResolver{
    /**
     * Class to deal with unresolved intents after BDG construction (i.e, implicit intents)
     * 
     */

    public static resolveIntents(BDG bdg){
        //Get target intent sets from bdg
        //For each string argument
        //Check if corresponds to an intent filter identified from the manifest
        //If unique match, replace with the activity
        //If multiple match, need category + data
        //Here rely on the bdg traversal to find those, similar to forward worker
        //BDG should include the URI construction statements which can be used to build the activity of interest
        //We wouldn't know which map to which unless we parse the whole BDG again, hmm, (e.g., if the action and the data are not defined in the same method)
        //similar to https://github.com/hanada31/ICCBot/blob/main/ICCBot/src/main/java/com/iscas/iccbot/analyze/utils/ValueObtainer.java

    }
}