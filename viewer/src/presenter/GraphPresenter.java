package presenter;

import command.SelectionCommand;
import javafx.collections.MapChangeListener;
import javafx.event.ActionEvent;
import model.graph.ASuperNode;
import model.peptide.AminoAcidProperties;
import exception.MyException;
import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import model.graph.AEdge;
import model.graph.ANode;
import model.peptide.AminoAcid;
import model.peptide.AtomProperties;
import utils.PDBToGraphFormatMover;
import utils.meshViewFactory.CartoonViewFactory;
import view.graph.*;
import view.label.LabelView;

import java.util.*;

public class GraphPresenter {

    private final MainPresenter mainPresenter;

    private double mouseOldX = Double.MIN_VALUE;
    private double mouseOldY = Double.MIN_VALUE;

    //Listener, Eventhandler
    private ListChangeListener<ANode> nodeListener;
    private ListChangeListener<AEdge> edgeListener;
    private MapChangeListener<Integer, ASuperNode> superNodeListener;
    private ListChangeListener<ANodeView> nodeViewListener;
    private ListChangeListener<AEdgeView> edgeViewListener;
    private ListChangeListener<Node> labelViewListener;

    private EventHandler<ActionEvent> selectAllHandler;
    private EventHandler<ActionEvent> selectNoneHandler;
    private EventHandler<MouseEvent> panePressedHandler;
    private EventHandler<MouseEvent> paneDraggedHandler;
    private InvalidationListener rotateListener;

    public static final double coordScale = 60.0;

    public GraphPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        this.mainPresenter.getaNodeASelectionModel().setItems(this.mainPresenter.getaGraph().getNodes().
                toArray(new ANode[this.mainPresenter.getaGraph().getNodes().size()]));

        this.mainPresenter.getConnector().getDrawPane().getChildren().addAll(this.mainPresenter.getaGraphView().getBottom(),
                this.mainPresenter.getaGraphView().getTop());

        addListener();
        setListener();
        addZoom();
    }

    public void readIn(List<List<AminoAcid>> chains) {
        try {
            for (List<AminoAcid> chain : chains) {
                this.mainPresenter.getaGraph().readIn(PDBToGraphFormatMover.getContainer(chain));
                for (AminoAcid a : chain) {
                    mainPresenter.getLabelList().addLabel(a.getId(), a.getAminoAcidProperties(), a.getSecStructure());
                }
            }
            this.mainPresenter.getConnector().getShowListView().setSelected(false);
            CartoonViewFactory.computeCartoonView(this.mainPresenter.getaGraph().getSuperNodes(), this);

            this.mainPresenter.getIsgraphReadIn().setValue(true);


        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "PDB could not be converted to graph format: \n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            this.mainPresenter.getaGraph().clear();
            e.printStackTrace();

        }
    }

    private void addListener() {
        addGraphListener();
        addGraphViewListener();
        addLabelViewListener();
        addRotateListener();
        addPaneListener();
        addSelectAllOrNoneHandler();
    }


    @SuppressWarnings("unchecked")
    private void setListener() {
        this.mainPresenter.getaGraph().getNodes().addListener(nodeListener);
        this.mainPresenter.getaGraph().getEdges().addListener(edgeListener);
        this.mainPresenter.getaGraph().getSuperNodes().addListener(superNodeListener);
        this.mainPresenter.getaGraphView().getNodeViews().addListener(nodeViewListener);
        this.mainPresenter.getaGraphView().getEdgeViews().addListener(edgeViewListener);
        this.mainPresenter.getLabelList().getLabelList().addListener(labelViewListener);
        this.mainPresenter.getAssemblageTransformProperty().addListener(rotateListener);

        this.mainPresenter.getConnector().getSelectAll().setOnAction(selectAllHandler);
        this.mainPresenter.getConnector().getSelectNone().setOnAction(selectNoneHandler);
        this.mainPresenter.getConnector().getDrawPane().setOnMousePressed(panePressedHandler);
        this.mainPresenter.getConnector().getDrawPane().setOnMouseDragged(paneDraggedHandler);


    }

    private void addGraphListener() {
        nodeListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(this::addNodeToView);
                c.getRemoved().forEach(this::removeNodeFromView);
                mainPresenter.getaNodeASelectionModel().setItems(mainPresenter.getaGraph().getNodes().
                        toArray(new ANode[mainPresenter.getaGraph().getNodes().size()]));

            }
        };

        edgeListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(this::addEdgeToView);
                c.getRemoved().forEach(this::removeEdgeFromView);
            }
        };

        superNodeListener = c -> {
            if (c.wasAdded()) {
                addListenerToNodeCollection(c.getValueAdded());
            }
            if (c.wasRemoved()) {
                removeARibbonView(c.getKey());
            }
        };
    }

    private void addNodeToView(ANode n) {

        Point3D coordinates = n.getCoordinates();
        //Color un-proper nodes in pink to make them identifiable
        Color color = Color.PINK;
        double factor = 10.0; //Scale atoms in context of oxygen
        //Source atom radii: https://chem.libretexts.org/Textbook_Maps/General_Chemistry_Textbook_Maps/Map%3A_Chemistry%3A_The_Central_Science_(Brown_et_al.)/07._Periodic_Properties_of_the_Elements/7.3%3A_Sizes_of_Atoms_and_Ions
        try {
            AtomProperties ap = AtomProperties.valueOf(n.getAtomType().toUpperCase());
            color = ap.getColor();
            factor = ap.getFactor();
        } catch (IllegalArgumentException e) {
            System.out.println("Atom could not be identified" + n.getAtomType());
        }

        String tooltip = n.getPosition() + ":" + n.getAminoAcid().getThreeLetterCode() + ":" + n.getAtomSymb() + ":" + n.getSecStructure();
        this.mainPresenter.getaGraphView().addNodeView(n.getID(), factor, coordinates.multiply(coordScale), color, n.getAminoAcid().getColor(), n.getSecStructure().getColor(), tooltip);
        centerGraphView();
    }

    private void removeNodeFromView(ANode n) {
        try {
            this.mainPresenter.getaGraphView().removeNodeView(this.mainPresenter.getaGraphView().getNodeByID(n.getID()));
            this.mainPresenter.getLabelList().removeLabel(n.getAminoAcidID());
        } catch (MyException e) {
            System.err.println("Remove node view error " + e.getMessage());
        }
    }

    private void addEdgeToView(AEdge e) {
        Color aminoAcid = Color.LIGHTGRAY;
        if (e.getSource().getAminoAcid() == e.getTarget().getAminoAcid()) {
            aminoAcid = e.getSource().getAminoAcid().getColor();
        }

        Color secStrc = Color.LIGHTGRAY;
        if (e.getSource().getSecStructure().equals(e.getTarget().getSecStructure())) {
            secStrc = e.getSource().getSecStructure().getColor();
        }
        this.mainPresenter.getaGraphView().createEdgeView(e.getSource().getID(), e.getTarget().getID(), Color.LIGHTGRAY, aminoAcid, secStrc);
    }

    private void removeEdgeFromView(AEdge e) {
        try {
            this.mainPresenter.getaGraphView().removeEdgeView(this.mainPresenter.getaGraphView().getEdgeByIDs(e.getSource().getID(), e.getTarget().getID()));
        } catch (MyException exception) {
            System.err.println(exception.getMessage());
        }
    }

    private void addListenerToNodeCollection(ASuperNode superNode) {
        superNode.getNodeCollection().addListener((ListChangeListener<ANode>) c -> addARibbonView(superNode.getID(), superNode));
    }

    private void addARibbonView(Integer id, ASuperNode superNodes) {
        if (this.mainPresenter.getaGraph().getSuperNodes().containsKey(id - 1)) {

            Map<String, Point3D> curr = getRibbonPoints(superNodes);
            String currChainID = superNodes.getNodeCollection().get(0).getChainID();

            Map<String, Point3D> next = getRibbonPoints(mainPresenter.getaGraph().getSuperNodes().get(id - 1));
            String nextChainID = mainPresenter.getaGraph().getSuperNodes().get(id - 1).getNodeCollection().get(0).getChainID();

            if (curr.size() == 2 && next.size() == 2 && currChainID.equals(nextChainID)) {
                this.mainPresenter.getaGraphView().addRibbonView(id, next, curr, Color.GOLDENROD, superNodes.getAminoAcid().getColor(), superNodes.getSecStruc().getColor());
            }
        }
    }

    private Map<String, Point3D> getRibbonPoints(ASuperNode superNodes) {
        Map<String, Point3D> map = new HashMap<>();
        for (ANode n : superNodes.getNodeCollection()) {
            if (n.getAminoAcid() == AminoAcidProperties.GLYCINE || n.getAminoAcid() == AminoAcidProperties.UNKNOWN) {
                if (n.getAtomSymb().equals("O")) {
                    map.put("CB", n.getCoordinates());
                }
                if (n.getAtomSymb().equals("C")) {
                    map.put("CA", n.getCoordinates());
                }
            } else {
                if (n.getAtomSymb().equals("CB")) {
                    map.put("CB", n.getCoordinates());
                }
                if (n.getAtomSymb().equals("CA")) {
                    map.put("CA", n.getCoordinates());
                }
            }
        }
        return map;
    }

    private void removeARibbonView(int ID) {
        this.mainPresenter.getaGraphView().removeRibbonView(ID);
    }

    private void centerGraphView() {

        this.mainPresenter.getaGraphView().center();

        Point3D graphCenter = this.mainPresenter.getaGraphView().getCenter();
        this.mainPresenter.getPivotX().set(graphCenter.getX());
        this.mainPresenter.getPivotY().set(graphCenter.getY());
        this.mainPresenter.getPivotZ().set(graphCenter.getZ());

    }

    private void addGraphViewListener() {
        nodeViewListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(this::nodeHandlers);
                c.getRemoved().forEach(o -> {
                    o.scaleXProperty().unbind();
                    o.scaleYProperty().unbind();
                    o.scaleZProperty().unbind();
                });
            }
        };

        edgeViewListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(this::edgeHandlers);
                c.getRemoved().forEach(o -> o.getLine().radiusProperty().unbind());
            }
        };
    }

    private void nodeHandlers(ANodeView n) {
        n.setOnMouseClicked(event -> {
            if (!event.isShiftDown()) {
                selectNode(false, n);
            } else {
                selectNode(true, n);
            }
        });

        n.scaleXProperty().bindBidirectional(this.mainPresenter.getConnector().getChangeAtomSize().valueProperty());
        n.scaleYProperty().bindBidirectional(this.mainPresenter.getConnector().getChangeAtomSize().valueProperty());
        n.scaleZProperty().bindBidirectional(this.mainPresenter.getConnector().getChangeAtomSize().valueProperty());

    }

    private void edgeHandlers(AEdgeView e) {
        e.getLine().radiusProperty().bindBidirectional(this.mainPresenter.getConnector().getChangeBondSize().valueProperty());
    }

    private void addLabelViewListener() {
        labelViewListener = c -> {
            while (c.next()) {
                c.getAddedSubList().forEach(l -> labelHandlers((LabelView) l));
            }
        };
    }

    private void labelHandlers(LabelView labelView) {
        labelView.setOnMouseClicked(event -> {
            if (!event.isShiftDown()) {
                selectLabel(false, labelView);
            } else {
                selectLabel(true, labelView);
            }
        });
    }

    private void addSelectAllOrNoneHandler() {

        selectAllHandler = event -> {
            SelectionCommand sc = new SelectionCommand(false, mainPresenter.getaNodeASelectionModel(), mainPresenter.getLabelList(), mainPresenter.getaNodeASelectionModel().getItems());
            try {
                mainPresenter.getCommandManager().executeAndAdd(sc);
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Could not select all: " + e.getMessage(), ButtonType.OK);
                alert.showAndWait();
            }
        };

        selectNoneHandler = event -> {
            ANode[] ids = new ANode[0];
            SelectionCommand sc = new SelectionCommand(false, mainPresenter.getaNodeASelectionModel(), mainPresenter.getLabelList(), ids);
            try {
                mainPresenter.getCommandManager().executeAndAdd(sc);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }

    private void selectNode(boolean isShiftDown, ANodeView n) {
        try {
            SelectionCommand sc = new SelectionCommand(isShiftDown, mainPresenter.getaNodeASelectionModel(), mainPresenter.getLabelList(), getSelectedAtoms(n));
            mainPresenter.getCommandManager().executeAndAdd(sc);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Could not select amino acid with ID: " + n.getID() + "\n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private ANode[] getSelectedAtoms(ANodeView n) {
        try {
            ANode selectedNode = this.mainPresenter.getaGraph().getNodeByID(n.getID());
            ASuperNode superNode = this.mainPresenter.getaGraph().getSuperNodeByID(selectedNode.getAminoAcidID());
            return superNode.getNodeCollection().toArray(new ANode[superNode.getNodeCollection().size()]);

        } catch (MyException e) {

        }
        return new ANode[0];
    }

    private ANode[] getSelectedAtoms(int aminoAcidID) {
        try {
            ASuperNode superNode = this.mainPresenter.getaGraph().getSuperNodeByID(aminoAcidID);
            return superNode.getNodeCollection().toArray(new ANode[superNode.getNodeCollection().size()]);
        } catch (MyException e) {
        }
        return new ANode[0];
    }

    private void selectLabel(boolean isShiftDown, LabelView l) {
        try {
            SelectionCommand sc = new SelectionCommand(isShiftDown, mainPresenter.getaNodeASelectionModel(), mainPresenter.getLabelList(), getSelectedAtoms(l.getID()));
            mainPresenter.getCommandManager().executeAndAdd(sc);
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Could not select amino acid with ID: " + l.getID() + "\n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }

    private void addZoom() {
        this.mainPresenter.getConnector().getDrawPane().setOnScroll(event ->
                this.mainPresenter.getaGraphView().getCamera().setTranslateZ(this.mainPresenter.getaGraphView().getCamera().getTranslateZ() + event.getDeltaY() * 1.8));
    }

    private void addRotateListener() {
        //fixed some of the bounding box/rotation lagging by replacing changelistener with inval listener
        rotateListener = observable -> mainPresenter.getaGraphView().getTransforms().setAll(mainPresenter.getAssemblageTransformProperty().getValue());
    }

    private void addPaneListener() {
        panePressedHandler = event -> {
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();
        };

        paneDraggedHandler = event -> {
            double dx = mouseOldX - event.getSceneX();
            double dy = mouseOldY - event.getSceneY();
            Point3D axis = new Point3D(-dy, dx, 0);
            double angle = 0.5 * (new Point2D(dx, dy)).magnitude();
            mainPresenter.setRotate(new Rotate(angle, axis));
            mainPresenter.getRotate().setPivotX(mainPresenter.getPivotX().getValue());
            mainPresenter.getRotate().setPivotY(mainPresenter.getPivotY().getValue());
            mainPresenter.getRotate().setPivotZ(mainPresenter.getPivotZ().getValue());
            mainPresenter.getAssemblageTransformProperty().setValue(mainPresenter.getRotate().createConcatenation(mainPresenter.getAssemblageTransformProperty().getValue()));
            mouseOldX = event.getSceneX();
            mouseOldY = event.getSceneY();

        };
    }

    public MainPresenter getMainPresenter() {
        return mainPresenter;
    }

}
