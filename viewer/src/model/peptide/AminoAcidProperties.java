package model.peptide;

import javafx.scene.paint.Color;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

public enum AminoAcidProperties {
    //http://jmol.sourceforge.net/jscolors/#Residues:%20amino%20acids,%20nucleotides
    ALANINE("ALA", 'A', Color.web("C8C8C8"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB")))),
    ARGININE("ARG", 'R', Color.web("145AFF"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD"),
                    new Pair<>("CD", "NE"),
                    new Pair<>("NE", "CZ"),
                    new Pair<>("CZ", "NH1"),
                    new Pair<>("CZ", "NH2")))),
    ASPARAGINE("ASN", 'N', Color.web("00DCDC"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "OD1"),
                    new Pair<>("CG", "ND2")))),
    ASPARTATICACID("ASP", 'D', Color.web("E60A0A"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "OD1"),
                    new Pair<>("CG", "OD2")))),
    CYSTEINE("CYS", 'C', Color.web("E6E600"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "SG")))),
    GLUTAMICACID("GLU", 'E', Color.web("E60A0A"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD"),
                    new Pair<>("CD", "OE1"),
                    new Pair<>("CD", "OE2")))),
    GLUTAMINE("GLN", 'Q', Color.web("00DCDC"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD"),
                    new Pair<>("CD", "OE1"),
                    new Pair<>("CD", "NE2")))),
    GLYCINE("GLY", 'G', Color.web("EBEBEB"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O")))),
    HISTIDINE("HIS", 'H', Color.web("8282D2"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "ND1"),
                    new Pair<>("CG", "CD2"),
                    new Pair<>("ND1", "CE1"),
                    new Pair<>("CD2", "NE2"),
                    new Pair<>("CE1", "NE2")))),
    ISOLEUCINE("ILE", 'I', Color.web("0F820F"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG1"),
                    new Pair<>("CB", "CG2"),
                    new Pair<>("CG1", "CD1")))),
    LEUCINE("LEU", 'L', Color.web("0F820F"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD1"),
                    new Pair<>("CG", "CD2")))),
    LYSINE("LYS", 'K', Color.web("145AFF"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD"),
                    new Pair<>("CD", "CE"),
                    new Pair<>("CE", "NZ")))),
    METHIONINE("MET", 'M', Color.web("E6E600"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "SD"),
                    new Pair<>("SD", "CE")))),
    PHENYLALANINE("PHE", 'F', Color.web("3232AA"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD1"),
                    new Pair<>("CG", "CD2"),
                    new Pair<>("CD1", "CE1"),
                    new Pair<>("CD2", "CE2"),
                    new Pair<>("CE1", "CZ"),
                    new Pair<>("CE2", "CZ")))),
    PROLINE("PRO", 'P', Color.web("DC9682"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD"),
                    new Pair<>("CD", "N")))),
    SERINE("SER", 'S', Color.web("FA9600"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "OG")))),
    THREONINE("THR", 'T', Color.web("FA9600"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "OG1"),
                    new Pair<>("CB", "CG2")))),
    TRYPTOPHAN("TRP", 'W', Color.web("B45AB4"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD1"),
                    new Pair<>("CG", "CD2"),
                    new Pair<>("CD1", "NE1"),
                    new Pair<>("CD2", "CE2"),
                    new Pair<>("NE1", "CE2"),
                    new Pair<>("CD2", "CE3"),
                    new Pair<>("CE2", "CZ2"),
                    new Pair<>("CE3", "CZ3"),
                    new Pair<>("CZ2", "CH2"),
                    new Pair<>("CZ3", "CH2")))),
    TYROSINE("TYR", 'Y', Color.web("3232AA"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG"),
                    new Pair<>("CG", "CD1"),
                    new Pair<>("CG", "CD2"),
                    new Pair<>("CD1", "CE1"),
                    new Pair<>("CD2", "CE2"),
                    new Pair<>("CE1", "CZ"),
                    new Pair<>("CE2", "CZ"),
                    new Pair<>("CZ", "OH")))),
    VALINE("VAL", 'V', Color.web("0F820F"),
            new ArrayList<>(Arrays.asList(  new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O"),
                    new Pair<>("CA", "CB"),
                    new Pair<>("CB", "CG1"),
                    new Pair<>("CB", "CG2")))),
    UNKNOWN("UKN", '*', Color.web("000000"),
            new ArrayList<>(Arrays. asList( new Pair<>("N", "CA"),
                    new Pair<>("CA", "C"),
                    new Pair<>("C", "O")
            )));

    private final String threeLetterCode;
    private final char oneLetterCode;
    private final Color color;
    private final List<Pair<String, String>> bondRules;

    AminoAcidProperties(String threeLetterCode, char oneLetterCode, Color color, List<Pair<String, String>> bondRules){
        this.threeLetterCode = threeLetterCode;
        this.oneLetterCode = oneLetterCode;
        this.color = color;
        this.bondRules = bondRules;
    }

    public String getThreeLetterCode(){
        return threeLetterCode;
    }

    public char getOneLetterCode(){
        return oneLetterCode;
    }

    public Color getColor() {
        return color;
    }

    public List<Pair<String, String>> getBondRules() {
        return bondRules;
    }
}
