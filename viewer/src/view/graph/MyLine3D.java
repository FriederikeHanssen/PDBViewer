package view.graph;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Cylinder;


public class MyLine3D extends Group {

    public static final Double cylinderRadius = 5.0;

    private final DoubleProperty startXProperty = new SimpleDoubleProperty();
    private final DoubleProperty startYProperty = new SimpleDoubleProperty();
    private final DoubleProperty startZProperty= new SimpleDoubleProperty();
    private final DoubleProperty endXProperty= new SimpleDoubleProperty();
    private final DoubleProperty endYProperty= new SimpleDoubleProperty();
    private final DoubleProperty endZProperty= new SimpleDoubleProperty();
    private final Cylinder cylinder;
    private final PhongMaterial material;

    public MyLine3D(DoubleProperty startXProperty, DoubleProperty startYProperty, DoubleProperty startZProperty,
             DoubleProperty endXProperty, DoubleProperty endYProperty, DoubleProperty endZProperty, Color color){


        this.cylinder = new Cylinder();

        this.startXProperty.bind(startXProperty);
        this.startYProperty.bind(startYProperty);
        this.startZProperty.bind(startZProperty);
        this.endXProperty.bind(endXProperty);
        this.endYProperty.bind(endYProperty);
        this.endZProperty.bind(endZProperty);

        translateXProperty().bind(this.startXProperty);
        translateYProperty().bind(this.startYProperty);
        translateZProperty().bind(this.startZProperty);

        this.cylinder.setRadius(cylinderRadius);
        material = new PhongMaterial();
        setColor(color);

        cylinder.setMaterial(material);

        computeRotation();

        this.startXProperty.addListener(observable -> computeRotation());
        this.startYProperty.addListener(observable -> computeRotation());
        this.startZProperty.addListener(observable -> computeRotation());
        this.endXProperty.addListener(observable -> computeRotation());
        this.endYProperty.addListener(observable -> computeRotation());
        this.endZProperty.addListener(observable -> computeRotation());


        this.getChildren().add(cylinder);

    }

    public Cylinder getCylinder() {
        return cylinder;
    }

    private void computeRotation(){

        Point3D desiredDirection =  new Point3D(endXProperty.getValue() - startXProperty.getValue(),
                endYProperty.getValue() - startYProperty.getValue(),
                endZProperty.getValue() - startZProperty.getValue());

        Point3D normale = new Point3D(0.0,1.0,0.0);
        Point3D rotationVector = desiredDirection.crossProduct(normale);

        //For some reason rad instead of deg is computed, so multiply with 180/pi to get deg, also *(-1),
        // otherwise they are the wrong way around
        Double angle = -Math.acos(desiredDirection.normalize().dotProduct(normale)) * (180./Math.PI);

        this.cylinder.setTranslateX(desiredDirection.getX() * 0.5);
        this.cylinder.setTranslateY(desiredDirection.getY() * 0.5);
        this.cylinder.setTranslateZ(desiredDirection.getZ() * 0.5);

        this.cylinder.setRotationAxis(rotationVector);
        this.cylinder.setRotate(angle);

        this.cylinder.setHeight(desiredDirection.magnitude());
    }

    public void setColor(Color color){
        material.setDiffuseColor(color);

    }
}
