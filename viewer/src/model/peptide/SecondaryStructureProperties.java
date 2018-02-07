package model.peptide;

import javafx.scene.paint.Color;

public enum SecondaryStructureProperties {
    //http://jmol.sourceforge.net/jscolors/#Residues:%20amino%20acids,%20nucleotides
    HELIX(Color.web("FF0080"), 'H'),
    SHEET(Color.web("FFC800"), 'E'),
    LOOP(Color.WHITE, '-'),
    UNKNOWN(Color.BLACK, '*');

    private final Color color;
    private final char representation;

    SecondaryStructureProperties(Color color, char representation){

        this.color = color;
        this.representation = representation;
    }

    public Color getColor() {
        return color;
    }

    public char getRepresentation() {
        return representation;
    }
}
