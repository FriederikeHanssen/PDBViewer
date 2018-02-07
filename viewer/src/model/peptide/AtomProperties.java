package model.peptide;

import javafx.scene.paint.Color;

public enum AtomProperties {

    //http://jmol.sourceforge.net/jscolors/
    O("OXYGEN", 1.0, Color.web("FF0D0D")),
    C("CARBON", 1.4, Color.web("909090")),
    N("NITROGEN", 1.2, Color.web("3050F8")),
    SE("SELENIUM", 2.2, Color.web("FFA100")),
    S("SULFUR", 1.8, Color.web("FFFF30")),
    P("PHOSPHOR", 2.0, Color.web("FF8000")),
    CL("CHLOR",1.7,Color.web("1FF01F")),
    BR("BROM", 2.0, Color.web("A62929")),
    I("IOD", 2.4, Color.web("940094"));


    private final String name;
    private final double factor;
    private final Color normalColor;

    AtomProperties(String name, double factor, Color normalColor){
        this.name = name;
        this.factor = factor;
        this.normalColor = normalColor;
    }

    public Color getColor(){
        return this.normalColor;
    }

    public String getColorAsString(){
        return this.normalColor.toString();
    }

    public double getFactor(){
        return this.factor;
    }

    public String getName() {
        return name;
    }
}
