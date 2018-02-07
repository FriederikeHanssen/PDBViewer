package view.graph;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Cylinder;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class AEdgeView extends Group {

    private final int sourceID;
    private final int targetID;
    private Color chainCol;
    private final Color secStrcColor;
    private final Color aminoAcidColor;
    private final Color atomColor;

    private final MyLine3D line;

    AEdgeView(ANodeView source, ANodeView target, Color atomColor, Color aminoAcidColor, Color secStrcColor){
        this.sourceID = source.getID();
        this.targetID = target.getID();

        this.atomColor = atomColor;
        this.aminoAcidColor = aminoAcidColor;
        this.secStrcColor = secStrcColor;

        line = new MyLine3D(source.translateXProperty(), source.translateYProperty(), source.translateZProperty(),
                target.translateXProperty(), target.translateYProperty(), target.translateZProperty(), atomColor);
        this.getChildren().add(line);

    }

    public int getSourceID() {
        return sourceID;
    }

    public int getTargetID() {
        return targetID;
    }

    public Cylinder getLine() {
        return line.getCylinder();
    }

    public void specifyChainColor(Color color){
        chainCol = color;
    }

    public void setAtomColor(){
        line.setColor(atomColor);
    }
    public void setAminoAcidColor(){
        line.setColor(aminoAcidColor);
    }
    public void setSecStrcColor(){
        line.setColor(secStrcColor);
    }
    public void setChainColor(){
        line.setColor(chainCol);
    }

}
