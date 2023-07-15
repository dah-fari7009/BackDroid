package edu.smu.backdroid.graph;

import java.util.Set;

/**
 * Conditional ATG
 */
public class CATG {
    //TODO: subgraph with all reached activities, edges to unreached activities annotated
    public Set<CATGNode> nodes;
    public Set<CATGEdge> edges;

    public class CATGNode{
        private String activityName;

        public CATGNode(String name){
            this.activityName = name;
        }

        public boolean equals(CATGNode node){
            return node != null && this.activityName == node.activityName;
        }
    }

    
    private class CATGEdge{
        private CATGNode src, dst;
        //entrypoint callback name?
        private String callback;
        private REASON edgeType;

        public CATGEdge(CATGNode src, CATGNode dst, String callback, REASON edgeType){
            this.src = src;
            this.dst = dst;
            this.callback = callback;
            this.edgeType = edgeType;
        }

        public CATGEdge(CATGNode src, CATGNode dst, String callback){
            this(src, dst, callback, REASON.UNKNOWN);
        }

        public CATGEdge(CATGNode src, CATGNode dst){
            this(src, dst, "", REASON.UNKNOWN);
        }

        public void setReason(REASON edgeType){
            this.edgeType = edgeType; //should be a list?
        }
    }
    
    private enum REASON {
        SOFTWARE,
        HARDWARE,
        ENVIRONMENT,
        INFO,
        EQUIPMENT,
        SERVER,
        ALTERNATE,
        INCOMPLETE_EXPLORATION,
        UNKNOWN,

        public final String label;
        //Conditions
        //probably symbolic constraints (simplified somehow)
    }

    public CATGNode getNodeByName(String name){
        return nodes.get(new CATGNode(name));
    }

    public void addEdge(CATGNode src, CATGNode dest, String callback){
        CATGEdge edge = new CATGEdge(src, dest, callback);
        edges.add(edge);
    }

    public void updateCATG(BDG bdg){
        //Get tail nodes (starting points)
        Set<BDGUnit> tails = bdg.getNormalTails();
        if(tails == null || tails.isEmpty()){
            tails = bdg.getFakeTails();
        }
        //Get target intent class (dest)
        String targetClass = bdg.getTargetIntentClass();
        //Map target to a node
        String properName = targetClass;
        CATGNode dest = new CATGNode(properName);

        for(BDGUnit unit: tails){
            String mSig = unit.getMSig();
            String className, methodName;

            CATGNode src = new CATGNode(className);
            this.addEdge(src, dest, methodName);
        }

    }
}
