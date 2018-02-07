package model.graph;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import model.peptide.AminoAcidProperties;
import model.peptide.SecondaryStructureProperties;

import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class ANode {

    private final IntegerProperty id;
    private final StringProperty atomSymb;
    private final StringProperty atomType;
    private final SecondaryStructureProperties secStructure;
    private final StringProperty chainID;
    private final Point3D coordinates;

    private final IntegerProperty position;
    private final AminoAcidProperties aminoAcid;

    private final IntegerProperty aminoAcidID;
    private final ObservableList<model.graph.AEdge> outgoingEdges;
    private final ObservableList<model.graph.AEdge> incomingEdges;


    public ANode(int id, String atomSymbol,
                 String atomType,
                 SecondaryStructureProperties secStructure,
                 String chainID,
                 Point3D coordinates,
                 int position,
                 AminoAcidProperties aminoAcid,
                 List<model.graph.AEdge> outgoing,
                 List<model.graph.AEdge> incoming){

        this.id = new SimpleIntegerProperty(id);
        this.atomSymb = new SimpleStringProperty(atomSymbol);
        this.atomType = new SimpleStringProperty(atomType);
        this.secStructure = secStructure;
        this.chainID = new SimpleStringProperty(chainID);
        this.coordinates = coordinates;
        this.position = new SimpleIntegerProperty(position);
        this.aminoAcid = aminoAcid;
        this.outgoingEdges = FXCollections.observableArrayList(outgoing);
        this.incomingEdges = FXCollections.observableArrayList(incoming);
        this.aminoAcidID = new SimpleIntegerProperty();
    }

    public int getID() {
        return this.id.get();
    }

    public String getAtomSymb() {
        return this.atomSymb.get();
    }

    public String getAtomType() {
        return this.atomType.get();
    }

    public Point3D getCoordinates() {
        return this.coordinates;
    }

    public ObservableList<model.graph.AEdge> getOutgoingEdges() {
        return this.outgoingEdges;
    }

    public ObservableList<model.graph.AEdge> getIncomingEdges() {
        return this.incomingEdges;
    }

    public void addOutgoingEdge(model.graph.AEdge edge){
        this.outgoingEdges.add(edge);
    }

    public void addIncomingEdge(model.graph.AEdge edge){
        this.incomingEdges.add(edge);
    }

    public int getPosition() {
        return position.get();
    }

    public void setAminoAcidID(int pos){
        this.aminoAcidID.setValue(pos);
    }

    public int getAminoAcidID() {
        return this.aminoAcidID.get();
    }

    public AminoAcidProperties getAminoAcid() {
        return this.aminoAcid;
    }

    public SecondaryStructureProperties getSecStructure() {
        return this.secStructure;
    }

    public String getChainID() {
        return this.chainID.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ANode aNode = (ANode) o;

        if (id != null ? !id.equals(aNode.id) : aNode.id != null) return false;
        if (atomSymb != null ? !atomSymb.equals(aNode.atomSymb) : aNode.atomSymb != null) return false;
        if (atomType != null ? !atomType.equals(aNode.atomType) : aNode.atomType != null) return false;
        if (secStructure != null ? !secStructure.equals(aNode.secStructure) : aNode.secStructure != null) return false;
        if (coordinates != null ? !coordinates.equals(aNode.coordinates) : aNode.coordinates != null) return false;
        if (position != null ? !position.equals(aNode.position) : aNode.position != null) return false;
        if (aminoAcid != aNode.aminoAcid) return false;
        if (aminoAcidID != null ? !aminoAcidID.equals(aNode.aminoAcidID) : aNode.aminoAcidID != null) return false;
        if (outgoingEdges != null ? !outgoingEdges.equals(aNode.outgoingEdges) : aNode.outgoingEdges != null)
            return false;
        return incomingEdges != null ? incomingEdges.equals(aNode.incomingEdges) : aNode.incomingEdges == null;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (atomSymb != null ? atomSymb.hashCode() : 0);
        result = 31 * result + (atomType != null ? atomType.hashCode() : 0);
        result = 31 * result + (secStructure != null ? secStructure.hashCode() : 0);
        result = 31 * result + (coordinates != null ? coordinates.hashCode() : 0);
        result = 31 * result + (position != null ? position.hashCode() : 0);
        result = 31 * result + (aminoAcid != null ? aminoAcid.hashCode() : 0);
        result = 31 * result + (aminoAcidID != null ? aminoAcidID.hashCode() : 0);
        result = 31 * result + (outgoingEdges != null ? outgoingEdges.hashCode() : 0);
        result = 31 * result + (incomingEdges != null ? incomingEdges.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ANode{" +
                "id=" + id.getValue() +
                ", atomSymb=" + atomSymb.getValue() +
                '}';
    }
}
