package utils;

import model.peptide.AminoAcidProperties;
import model.graph.AEdge;
import model.graph.ANode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class MyFileContainer {

    private final List<ANode> nodes;
    private final List<AEdge> edges;

    MyFileContainer() {
        this.nodes = new ArrayList<>();
        this.edges = new ArrayList<>();
    }

    public List<ANode> getNodes() {
        return nodes;
    }

    public List<AEdge> getEdges() {
        return edges;
    }

    public void addNode(ANode n){
        this.nodes.add(n);
    }

    public void addEdge(AEdge e){
        this.edges.add(e);
    }

    public void clear(){
        nodes.clear();
        edges.clear();
    }

    public ANode getNode(int position, String atomSymb, AminoAcidProperties prop){
        for(ANode n : nodes){
            if(n.getAminoAcidID() == position && n.getAtomSymb().equals(atomSymb) && prop == n.getAminoAcid()){
                return n;
            }
        }
        return null;

    }
}