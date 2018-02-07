package model.graph;

import exception.MyException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import utils.MyFileContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AGraph {

    private final ObservableList<ANode> nodes;
    private final ObservableList<model.graph.AEdge> edges;
    private final List<Integer> currentIdentifiers;
    private final ObservableMap<Integer, ASuperNode> superNodes;


    public AGraph() {
        this.nodes = FXCollections.observableArrayList();
        this.edges = FXCollections.observableArrayList();
        this.currentIdentifiers = new ArrayList<>();
        this.superNodes = FXCollections.observableHashMap();
    }

    private void addNode(ANode node) throws MyException {
        //Check for unique node id
        if (isUnique(node)) {
            this.nodes.add(node);
            this.currentIdentifiers.add(node.getID());

            ASuperNode s;
            if(this.superNodes.containsKey(node.getAminoAcidID())){
                s = this.superNodes.get(node.getAminoAcidID());
            }else{
                s = new ASuperNode(node.getAminoAcidID(), node.getAminoAcid(), node.getSecStructure());
            }
            s.addNode(node);
            this.superNodes.put(node.getAminoAcidID(),s);

        } else {
            throw new MyException("Node Id is not unique!");
        }
    }

    private void connectTwoNodes(ANode source, ANode target) throws MyException {

        if (source == target) {
            throw new MyException("Graph contains self loop at node" + source.getID() + " " + target.getID());
        }
        for (model.graph.AEdge edge : this.edges) {
            //If an edge exists with same source and target already, throw some exception
            if (source == edge.getSource() && target == edge.getTarget()) {
                throw new MyException("Graph contains parallel edges at nodes" + source.getID() + " " + target.getID());
            }
        }
        model.graph.AEdge connection = new model.graph.AEdge(source, target);
        this.edges.add(connection);

        source.addOutgoingEdge(connection);
        target.addIncomingEdge(connection);
    }

    public void clear() {
        this.nodes.clear();
        this.edges.clear();
        this.currentIdentifiers.clear();
        this.superNodes.clear();
    }

    public void readIn(MyFileContainer container) throws MyException {
        for (ANode n : container.getNodes()) {
            if (isUnique(n)) {
                addNode(n);
            } else {
                throw new MyException("Ids are not unique. Please load a file with unique identifiers!");
            }
        }
        for (model.graph.AEdge e : container.getEdges()) {
            if (e.getSource() == null || e.getTarget() == null) {
                throw new MyException("Edge (" + e.getSource().getID() + "," + e.getTarget().getID() + ") was found, but target or source node does not exist.");
            }
            this.connectTwoNodes(e.getSource(), e.getTarget());
        }
    }

    private boolean isUnique(ANode n) {
        return !this.currentIdentifiers.contains(n.getID());
    }

    public ObservableList<ANode> getNodes() {
        return this.nodes;
    }

    public ObservableList<model.graph.AEdge> getEdges() {
        return this.edges;
    }

    public ObservableMap<Integer, ASuperNode> getSuperNodes() {
        return this.superNodes;
    }

    public ANode getNodeByID(int ID) throws MyException {

        for (ANode node : this.nodes) {
            if (Objects.equals(node.getID(), ID)) {
                return node;
            }
        }
        throw new MyException("No such node exists with getNodeByID in graph  " + ID);
    }

    public ASuperNode getSuperNodeByID(int ID)throws MyException {

        for (Integer superNodeID : this.superNodes.keySet()) {
            if (Objects.equals(superNodeID, ID)) {
                return this.superNodes.get(superNodeID);
            }
        }
        throw new MyException("No such super node exists with getSuperNodeByID in graph  " + ID);
    }
}
