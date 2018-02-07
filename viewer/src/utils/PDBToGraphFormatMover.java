package utils;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Point3D;
import javafx.util.Pair;
import model.peptide.AminoAcid;
import model.peptide.Atom;
import model.graph.AEdge;
import model.graph.ANode;

import java.util.*;

/**
 * @author Friederike Hanssen, 01.2018
 */

public final class PDBToGraphFormatMover {

    private static final MyFileContainer nodes = new MyFileContainer();
    public static final IntegerProperty removedBonds = new SimpleIntegerProperty(0);


    public static MyFileContainer getContainer(List<AminoAcid> sequence) {

        nodes.clear();

        for (AminoAcid a : sequence) {
            for (Atom atom : a.getAtoms()) {
                ANode node = new ANode(atom.getSerial(), atom.getAtom(), atom.getElement(), atom.getSecondaryStructure(), String.valueOf(atom.getChainID()), atom.getCoordinates(),
                        atom.getResSeq(), atom.getAminoAcid(), new ArrayList<>(), new ArrayList<>());
                node.setAminoAcidID(a.getId());
                nodes.addNode(node);
            }
        }

        addIntraAminoAcidBonds(sequence.get(0));
        addInterAminoAcidBonds(sequence);
        addFinalCarboxylOxygen(sequence);

        return nodes;
    }

    private static void addFinalCarboxylOxygen(List<AminoAcid> sequence) {
        //add last oxygen of carboxyl group, if exists:
        AminoAcid last = sequence.get(sequence.size()-1);
        ANode source = nodes.getNode(last.getId(), "C", last.getAminoAcidProperties());
        ANode target = nodes.getNode(last.getId(), "OXT", last.getAminoAcidProperties());
        if(source != null && target != null) {
            addBondIfValid(source, target);
        }
    }

    private static void addInterAminoAcidBonds(List<AminoAcid> sequence) {
        //connect amino acid with predecessor, start with second
        for (int i = 1; i < sequence.size(); i++) {
            AminoAcid sourceAA = sequence.get(i - 1);
            AminoAcid targetAA = sequence.get(i);
            ANode source = nodes.getNode(sourceAA.getId(), "C", sourceAA.getAminoAcidProperties());
            ANode target = nodes.getNode(targetAA.getId(), "N", targetAA.getAminoAcidProperties());
            if(source != null && target != null) {
                addBondIfValid(source, target);
            }
            addIntraAminoAcidBonds(sequence.get(i));
        }
    }

    private static void addBondIfValid(ANode source, ANode target) {
        //discard bonds of length > 3 angstroem, sth is def not right with them
        if (bondLength(source, target) < 3.0) {
            nodes.addEdge(new AEdge(source, target));
        } else {
            removedBonds.setValue(removedBonds.getValue() + 1);
        }
    }

    private static double bondLength(ANode source, ANode target) {

        Point3D sCoordinates = source.getCoordinates();
        Point3D tCoordinates = target.getCoordinates();
        return Math.sqrt(Math.pow(tCoordinates.getX() - sCoordinates.getX(), 2) +
                Math.pow(tCoordinates.getY() - sCoordinates.getY(), 2) +
                Math.pow(tCoordinates.getZ() - sCoordinates.getZ(), 2));
    }

    private static void addIntraAminoAcidBonds(AminoAcid aminoAcid) {
        List<Pair<String, String>> connections = aminoAcid.getAminoAcidProperties().getBondRules();

        for (Pair<String, String> c : connections) {
            for (Atom a : aminoAcid.getAtoms()) {
                if(a.getAtom().equals(c.getKey())) {
                    ANode source = nodes.getNode(aminoAcid.getId(), a.getAtom(), aminoAcid.getAminoAcidProperties());
                    ANode target = nodes.getNode(aminoAcid.getId(), c.getValue(), aminoAcid.getAminoAcidProperties());
                    if(source != null && target != null) {
                        addBondIfValid(source, target);
                    }
                }
            }
        }
    }

}
