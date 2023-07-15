package edu.smu.backdroid.graph;

public class CATGToDotGraph {

    public CATGToDotGraph() {
    }
    
    public DotGraph drawBDG(CATG catg, String graphname) {
        DotGraph canvas = initDotGraph(catg, graphname);
        
        // Draw the field nodes first
        //drawAllEdgeAndNode(canvas, bdg, true);
        //setSubGraphandNodeLabel(canvas, bdg, true);
        
        // Then draw the normal nodes
        //drawAllEdgeAndNode(canvas, bdg, false);
        //setSubGraphandNodeLabel(canvas, bdg, false);
        //Draw the nodes,
        //Draw the edges
        
        return canvas;
    }
    
}
