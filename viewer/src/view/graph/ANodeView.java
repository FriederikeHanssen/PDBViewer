package view.graph;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Sphere;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class ANodeView extends Group{

    public static final double radius = 12.0;

    private final int id;
    private final double factor;
    private final Point3D coordinates;
    private final PhongMaterial material;
    private final Color aminoAcidColor;
    private final Color atomColor;
    private final Color secStrucColor;
    private Color chainColor;

    private Sphere sphere;
    private Tooltip tooltip;

    ANodeView(int id, double factor, Point3D coordinates, Color atomColor, Color aminoAcidColor, Color secStrucColor){
        this.id = id;
        this.factor = factor;
        this.coordinates = coordinates;

        specifySphere();

        this.getChildren().add(sphere);
        positionGroup();

        this.aminoAcidColor = aminoAcidColor;
        this.atomColor = atomColor;
        this.secStrucColor = secStrucColor;

        material = new PhongMaterial();
        this.sphere.setMaterial(material);
        setAtomColor();

    }

    private void positionGroup(){
        this.translateXProperty().setValue(coordinates.getX());
        this.translateYProperty().setValue(coordinates.getY());
        this.translateZProperty().setValue(coordinates.getZ());
    }

    private void specifySphere(){
        this.sphere = new Sphere();
        this.sphere.setRadius(radius);

        this.sphere.setScaleX(factor);
        this.sphere.setScaleY(factor);
        this.sphere.setScaleZ(factor);
    }

    public int getID() {
        return id;
    }

    public Sphere getSphere() {
        return sphere;
    }

    public void setTooltip(String text){
        tooltip = new Tooltip(text);
        Tooltip.install(this, tooltip);
    }

    public void removeTooltip(){
        Tooltip.uninstall(this, tooltip);
    }


    public void setAtomColor(){
        setColor(atomColor);
    }

    public void setAminoAcidColor(){
        setColor(aminoAcidColor);
    }

    public void setSecStrucColor(){
        setColor(secStrucColor);
    }

    public void specifyChainColor(Color chainCol){
        this.chainColor = chainCol;
    }

    public void setChainColor(){
        setColor(chainColor);
    }

    private void setColor(Color color){
        material.setDiffuseColor(color);
        material.setSpecularColor(color);
    }

}
