package model.peptide;

import javafx.geometry.Point3D;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class Atom {

    private  int serial;
    private  String atom;
    private  String resName;
    private  char chainID;
    private  int resSeq;
    private  Point3D coordinates;
    private  double occupancy;
    private  String element;
    private  AminoAcidProperties aminoAcid;
    private SecondaryStructureProperties secondaryStructure;

    public Atom(int serial,
                String atom,
                String resName,
                char chainID,
                int resSeq,
                double x,
                double y,
                double z,
                double occupancy,
                String element){

        this.serial = serial;
        this.atom = atom;

        this.resName= resName;
        this.chainID=chainID;
        this.resSeq = resSeq;

        this.coordinates = new Point3D(x,y,z);
        this.occupancy = occupancy;

        this.element= element;

        this.aminoAcid = getAminoAcidProperty(resName);

        this.secondaryStructure = SecondaryStructureProperties.UNKNOWN;
    }

    public int getSerial() {
        return serial;
    }

    public String getAtom() {
        return atom;
    }

    public int getResSeq() {
        return resSeq;
    }

    public Point3D getCoordinates() {
        return coordinates;
    }

    public double getOccupancy() {
        return occupancy;
    }

    public String getElement() {
        return element;
    }

    public AminoAcidProperties getAminoAcid() {
        return aminoAcid;
    }

    private AminoAcidProperties getAminoAcidProperty(String resName) {
        switch (resName) {
            case "ARG":
                return AminoAcidProperties.ARGININE;
            case "ASN":
                return AminoAcidProperties.ASPARAGINE;
            case "ASP":
                return AminoAcidProperties.ASPARTATICACID;
            case "ALA":
                return AminoAcidProperties.ALANINE;
            case "CYS":
                return AminoAcidProperties.CYSTEINE;
            case "GLN":
                return AminoAcidProperties.GLUTAMINE;
            case "GLU":
                return AminoAcidProperties.GLUTAMICACID;
            case "GLY":
                return AminoAcidProperties.GLYCINE;
            case "HIS":
                return AminoAcidProperties.HISTIDINE;
            case "ILE":
                return AminoAcidProperties.ISOLEUCINE;
            case "LEU":
                return AminoAcidProperties.LEUCINE;
            case "LYS":
                return AminoAcidProperties.LYSINE;
            case "MET":
                return AminoAcidProperties.METHIONINE;
            case "PHE":
                return AminoAcidProperties.PHENYLALANINE;
            case "PRO":
                return AminoAcidProperties.PROLINE;
            case "SER":
                return AminoAcidProperties.SERINE;
            case "THR":
                return AminoAcidProperties.THREONINE;
            case "TRP":
                return AminoAcidProperties.TRYPTOPHAN;
            case "TYR":
                return AminoAcidProperties.TYROSINE;
            case "VAL":
                return AminoAcidProperties.VALINE;
            default:
                return AminoAcidProperties.UNKNOWN;

        }
    }

    public void setSecondaryStructure(SecondaryStructureProperties secondaryStructure){
        this.secondaryStructure = secondaryStructure;
    }

    public char getChainID(){
        return this.chainID;
    }

    public SecondaryStructureProperties getSecondaryStructure() {
        return secondaryStructure;
    }

    @Override
    public String toString() {
        return "Atom{" +
                "serial=" + serial +
                ", atom='" + atom + '\'' +
                ", resName='" + resName + '\'' +
                ", chainID=" + chainID +
                ", resSeq=" + resSeq +
                ", coordinates=" + coordinates +
                ", occupancy=" + occupancy +
                ", element='" + element + '\'' +
                '}';
    }




}
