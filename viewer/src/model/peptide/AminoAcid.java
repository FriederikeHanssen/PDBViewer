package model.peptide;

import java.util.*;
import java.util.stream.Collectors;

public class AminoAcid {

    private final List<Atom> atoms;
    private final AminoAcidProperties aminoAcid;
    private final int position;
    private SecondaryStructureProperties secStructure;
    private final int id;

    public static int counter = 0;

    public AminoAcid(List<Atom> atoms){
        this.atoms = new ArrayList<>(atoms);
        this.aminoAcid = this.atoms.get(0).getAminoAcid();
        this.position = this.atoms.get(0).getResSeq();
        this.secStructure = SecondaryStructureProperties.UNKNOWN;
        id = counter;
        counter++;
        setAtoms();
    }

    private void setAtoms(){
        List<String> temp = new ArrayList<>();
        for(Atom a : atoms){
            temp.add(a.getAtom());
        }
        if(temp.size() != new HashSet<>(temp).size()){
            resolveAmbiguities(findDuplicateAtoms(atoms));
        }
    }

    private Set<String> findDuplicateAtoms(List<Atom> listContainingDuplicates) {

        final Set<String> setToReturn = new HashSet<>();
        final Set<String> atomNames = new HashSet<>();
        for (Atom atom : listContainingDuplicates)
        {
            if (!atomNames.add(atom.getAtom()))
            {
                setToReturn.add(atom.getAtom());
            }
        }
        return setToReturn;
    }

    private void resolveAmbiguities(Set<String> duplicateNames){
        for(String duplicate : duplicateNames){
            List<Atom> duplicates = atoms.stream().filter(atom -> duplicate.equals(atom.getAtom())).collect(Collectors.toList());
            Atom bestGuess = getBestGuess(duplicates);
            duplicates.remove(bestGuess);
            atoms.removeAll(duplicates);
        }
    }

    private Atom getBestGuess(List<Atom> selection){
        Atom best = selection.get(0);
        //pick the atom with the highest occupancy
        for(Atom a : selection){
            if(a.getOccupancy() > best.getOccupancy()){
                best = a;
            }
        }
        return best;
    }

    public List<Atom> getAtoms() {
        return atoms;
    }

    public AminoAcidProperties getAminoAcidProperties() {
        return aminoAcid;
    }

    public void setSecStructure(char secStructure){
        switch (secStructure) {
            case 'H':
                this.secStructure = SecondaryStructureProperties.HELIX;
                break;
            case 'E':
                this.secStructure = SecondaryStructureProperties.SHEET;
                break;
            case '-':
                this.secStructure = SecondaryStructureProperties.LOOP;
                break;
            default:
                this.secStructure = SecondaryStructureProperties.UNKNOWN;
                break;
        }
        for(Atom a : atoms){
            a.setSecondaryStructure(this.secStructure);
        }

    }

    public SecondaryStructureProperties getSecStructure() {
        return secStructure;
    }

    public int getId() {
        return id;
    }

    public int getPosition() {
        return position;
    }
}
