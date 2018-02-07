package model.graph;

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
