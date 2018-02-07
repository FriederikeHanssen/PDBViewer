package model.graph;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.peptide.AminoAcidProperties;
import model.peptide.SecondaryStructureProperties;

public class ASuperNode {

    private final ObservableList<ANode> nodeCollection;
    private final IntegerProperty ID;
    private final AminoAcidProperties aminoAcid;
    private final SecondaryStructureProperties secStruc;

    public ASuperNode(Integer ID, AminoAcidProperties aminoAcid, SecondaryStructureProperties secStruc){
        this.nodeCollection = FXCollections.observableArrayList();
        this.ID = new SimpleIntegerProperty(ID);
        this.aminoAcid = aminoAcid;
        this.secStruc = secStruc;
    }

    public void addNode(ANode n){
        this.nodeCollection.add(n);
    }

    public ObservableList<ANode> getNodeCollection() {
        return this.nodeCollection;
    }

    public int getID() {
        return this.ID.get();
    }

    @Override
    public String toString() {
        return "ASuperNode{" +
                ", ID=" + ID.getValue() +
                '}';
    }

    public AminoAcidProperties getAminoAcid() {
        return this.aminoAcid;
    }

    public SecondaryStructureProperties getSecStruc() {
        return this.secStruc;
    }
}
