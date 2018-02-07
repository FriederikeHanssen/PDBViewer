package model.graph;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class AEdge {

    private final ANode source;
    private final ANode target;

    public AEdge(ANode source, ANode target){
        this.source = source;
        this.target = target;
    }

    public ANode getSource() {
        return this.source;
    }

    public ANode getTarget() {
        return this.target;
    }

}
