package view.graph;

import javafx.scene.shape.TriangleMesh;
import view.connector.Connector;
import exception.MyException;
import javafx.beans.property.Property;
import javafx.collections.ObservableList;
import javafx.geometry.Bounds;
import javafx.geometry.Point3D;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Transform;
import model.ASelectionModel;
import model.graph.ANode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class AGraphView extends Group {

    public static final int cameraZTranslate = -6000;

    private final Group nodeViewGroup;
    private final Group edgeViewGroup;
    private final Group ribbonViewGroup;
    private final Group cartoonViewGroup;

    private final Connector connector;
    private final ASelectionModel<ANode> selectionModel;
    private final Property<Transform> assemblageTransformProperty;

    private SubScene subScene;
    private PerspectiveCamera camera;
    private Pane top;
    private Pane bottom;


    public AGraphView(Connector connector, ASelectionModel<ANode> selectionModel, Property<Transform> assemblageTransformProperty) {
        this.nodeViewGroup = new Group();
        this.edgeViewGroup = new Group();
        this.ribbonViewGroup = new Group();
        this.cartoonViewGroup = new Group();

        this.getChildren().addAll(this.edgeViewGroup);
        this.getChildren().addAll(this.nodeViewGroup);
        this.getChildren().addAll(this.ribbonViewGroup);
        this.getChildren().addAll(this.cartoonViewGroup);

        this.connector = connector;
        this.selectionModel = selectionModel;
        this.assemblageTransformProperty = assemblageTransformProperty;

        setUpDrawArea();
        addBoundingBoxes();

    }

    private void setUpDrawArea() {
        this.bottom = new Pane();
        this.top = new Pane();

        bottom.setStyle("-fx-background-color: #000000");
        setUpCamera();
        setUpSubScene();
        subScene.setCamera(camera);
        this.bottom.getChildren().addAll(subScene);
        this.top.setPickOnBounds(false);
    }

    private void setUpCamera() {
        camera = new PerspectiveCamera(true);
        camera.setFarClip(15000);
        camera.setNearClip(0.1);
        camera.setTranslateZ(cameraZTranslate);
    }

    private void setUpSubScene() {
        subScene = new SubScene(this, 0, 0, true, SceneAntialiasing.BALANCED);
        subScene.widthProperty().bind(this.connector.getDrawPane().widthProperty());
        subScene.heightProperty().bind(this.connector.getDrawPane().heightProperty());
    }

    public Pane getBottom() {
        return bottom;
    }

    public PerspectiveCamera getCamera() {
        return camera;
    }

    public Point3D getCenter() {

        Bounds bounds = nodeViewGroup.getLayoutBounds();
        Double centerX = (bounds.getMinX() + bounds.getMaxX()) / 2.0;
        Double centerY = (bounds.getMinY() + bounds.getMaxY()) / 2.0;
        Double centerZ = (bounds.getMinZ() + bounds.getMaxZ()) / 2.0;
        return new Point3D(centerX, centerY, centerZ);
    }

    public Pane getTop() {
        return top;
    }

    private void addBoundingBoxes() {
        List<Property> propertyList = new ArrayList<>();
        propertyList.add(camera.translateXProperty());
        propertyList.add(camera.translateYProperty());
        propertyList.add(camera.translateZProperty());
        propertyList.add(assemblageTransformProperty);
        propertyList.add(subScene.widthProperty());
        propertyList.add(subScene.heightProperty());
        BoundingBoxes2D boundingBoxes2D = new BoundingBoxes2D(this.bottom, this, selectionModel,
                propertyList.toArray(new Property[propertyList.size()]));
        this.top.getChildren().add(boundingBoxes2D.getRectangles());
    }

    public void center(){
        Point3D center = getCenter();
        this.translateXProperty().setValue(-center.getX());
        this.translateYProperty().setValue(-center.getY());
        this.translateZProperty().setValue(-center.getZ());
    }


    /*
    Set NodeView Group
     */

    public void addNodeView(int id, double factor, Point3D coordinates, Color atomCol, Color aminoCol, Color secStrc, String tooltip){
        ANodeView n = new ANodeView(id, factor, coordinates, atomCol, aminoCol, secStrc);
        if (!tooltip.isEmpty()) {
            n.setTooltip(tooltip);
        }
        this.nodeViewGroup.getChildren().add(n);

    }

    public ObservableList getNodeViews() {
        return this.nodeViewGroup.getChildren();
    }

    public ANodeView getNodeByID(int ID) throws MyException {

        for (Node node : this.nodeViewGroup.getChildren()) {
            if (Objects.equals(((ANodeView) node).getID(), ID)) {
                return (ANodeView) node;
            }
        }
        throw new MyException("No such node exists with getNodeByID in graphview abstract " + ID);
    }

    public void removeNodeView(ANodeView n) {
        n.removeTooltip();
        this.nodeViewGroup.getChildren().remove(n);
        n = null;
    }

    public Group getNodeViewGroup() {
        return nodeViewGroup;
    }

    /*
    Set EdgeView Group
     */

    private void addEdgeView(AEdgeView e){
        this.edgeViewGroup.getChildren().add(e);
    }

    public AEdgeView getEdgeByIDs(int sourceID, int targetID) throws MyException{

        for(Node edge :  this.edgeViewGroup.getChildren()){
            if(Objects.equals(((AEdgeView) edge).getSourceID(), sourceID) && Objects.equals(((AEdgeView) edge).getTargetID(), targetID)){
                return (AEdgeView) edge;
            }
        }

        throw new MyException("No such edge exists");
    }

    public void createEdgeView(int sourceId, int targetId, Color atomColor, Color aminoAcid, Color secStrc){

        try {
            ANodeView sourceView = getNodeByID(sourceId);
            ANodeView targetView = getNodeByID(targetId);
            AEdgeView edgeView = new AEdgeView(sourceView, targetView, atomColor, aminoAcid, secStrc);
            addEdgeView(edgeView);
        }catch(MyException e){
            System.err.println("Create edge view in AGraphView failed: " + e.getMessage());
        }
    }

    public void removeEdgeView(AEdgeView e){
        this.edgeViewGroup.getChildren().remove(e);
        e = null;
    }

    public ObservableList getEdgeViews() {
        return edgeViewGroup.getChildren();
    }

    public Group getEdgeViewGroup() {
        return edgeViewGroup;
    }

    /*
    Set RibbonView Group
     */

    public void addRibbonView(int id, Map<String, Point3D> curr, Map<String, Point3D> next, Color atom, Color aminoACid, Color secStrc){
        boolean containsID = false;
        ARibbonView meshView = new ARibbonView();
        for(Node m : this.ribbonViewGroup.getChildren()){
            if(((ARibbonView)m).getID() == id) {
                containsID = true;
                meshView = (ARibbonView) m;
            }
        }

        if(containsID){
            meshView.setPoints(curr,next);
        }else {
            meshView = new ARibbonView(id, curr, next, atom, aminoACid, secStrc);
            this.ribbonViewGroup.getChildren().add(meshView);
        }
        meshView.setAtomColor();
    }

    public Group getRibbonViewGroup() {
        return this.ribbonViewGroup;
    }

    public ARibbonView getRibbonViewByID(int ID) throws MyException {

        for (Node node : this.ribbonViewGroup.getChildren()) {
            if (Objects.equals(((ARibbonView) node).getID(), ID)) {
                return (ARibbonView) node;
            }
        }
        throw new MyException("No such ribbonView exists with getRibbonByID in graphview: " + ID);
    }

    public void removeRibbonView(int ID) {
        ARibbonView removable = new ARibbonView();
        for(Node m : this.ribbonViewGroup.getChildren()){
            if(((ARibbonView)m).getID() == ID) {
                removable = (ARibbonView) m;
                break;
            }
        }
        this.ribbonViewGroup.getChildren().remove(removable);
        removable = null;
    }

    public ObservableList getRibbonViews(){
        return this.ribbonViewGroup.getChildren();
    }

    /*
    Set Cartoon View
     */
    public void addCartoonView(TriangleMesh triangleMesh, Color atom, Color secStrucColor, String chainID){
        ACartoonView cartoonView = new ACartoonView(triangleMesh, atom, secStrucColor, chainID);
        cartoonView.setAtomColor();
        this.cartoonViewGroup.getChildren().add(cartoonView);
    }

    public Group getCartoonViewGroup() {
        return this.cartoonViewGroup;
    }

    public void removeCartoonView() {
        this.cartoonViewGroup.getChildren().clear();
    }

    public ObservableList getCartoonViews(){
        return  this.cartoonViewGroup.getChildren();
    }

}
