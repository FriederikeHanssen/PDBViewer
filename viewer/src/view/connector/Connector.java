package view.connector;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class Connector implements Initializable {

    @FXML
    private TextField titleTextField;

    @FXML
    private TextField numAtoms;

    @FXML
    private TextField numEdges;

    @FXML
    private TextField numRemovedEdges;

    @FXML
    private MenuItem loadFromFile;

    @FXML
    private MenuItem loadPDBList;

    @FXML
    private MenuItem loadFromRCSB;

    @FXML
    private MenuItem closeProgram;

    @FXML
    private RadioMenuItem showListView;

    @FXML
    private VBox rightVBox;

    @FXML
    private TextField searchBar;

    @FXML
    private ListView pdbList;

    @FXML
    private ProgressBar progressBar;

    @FXML
    private Button cancelRCSB;

    @FXML
    private StackPane drawPane;

    //Edit

    @FXML
    private MenuItem undo;

    @FXML
    private MenuItem redo;

    @FXML
    private MenuItem selectNone;

    @FXML
    private MenuItem selectAll;

    @FXML
    private MenuItem drawFill;

    @FXML
    private MenuItem drawLine;

    //Coloring

    @FXML
    private RadioMenuItem atomCol;

    @FXML
    private RadioMenuItem secStrcCol;

    @FXML
    private RadioMenuItem aminoAcidCol;

    @FXML
    private RadioMenuItem chainColor;


    //View options

    @FXML
    private RadioMenuItem ballStickView;

    @FXML
    private RadioMenuItem surfaceView;

    @FXML
    private RadioMenuItem edgeView;

    @FXML
    private RadioMenuItem ribbonView;

    @FXML
    private RadioMenuItem cartoonView;

    //Details shown


    @FXML
    private RadioMenuItem allShow;

    @FXML
    private RadioMenuItem backboneShow;

    @FXML
    private RadioMenuItem smallBackboneShow;

    @FXML
    private RadioMenuItem hide;

    @FXML
    private ScrollPane labelPane;

    //ToolBox

    @FXML
    private Button closeToolbar;

    @FXML
    private Pane closeButtonPane;
    @FXML
    private Slider changeAtomSize;

    @FXML
    private Slider changeBondSize;

    @FXML
    private CheckBox autoRotation;

    @FXML
    private Button resetRotation;

    @FXML
    private CheckBox whiteBackground;

    @FXML
    private ToolBar toolBox;

    @FXML
    private ProgressIndicator progressIndicator;

    @FXML
    private HBox colorBox;

    @FXML
    private Button colorButton;

    @FXML
    private RadioButton atomColorToggle;

    @FXML
    private RadioButton chainColorToggle;


    @FXML
    private HBox showBox;

    @FXML
    private Button showButton;

    @FXML
    private RadioButton aminoAcidColorToggle;

    @FXML
    private RadioButton secStrcColorToggle;

    @FXML
    private RadioButton allToggle;

    @FXML
    private RadioButton hideToggle;

    @FXML
    private RadioButton backboneToggle;

    @FXML
    private RadioButton smallBackboneToggle;

    @FXML
    private HBox modelBox;

    @FXML
    private Button modelButton;

    @FXML
    private RadioButton ballStickToggle;

    @FXML
    private RadioButton spaceFillToggle;

    @FXML
    private RadioButton edgeToggle;

    @FXML
    private CheckBox ribbonToggle;

    @FXML
    private HBox toolHBox;

    @FXML
    private CheckBox cartoonToggle;
    //Charts

    @FXML
    private MenuItem aminoAcidDistribution;

    @FXML
    private MenuItem atomDistribution;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        closeToolbar.setManaged(false);
        closeToolbar.prefHeightProperty().bind(closeButtonPane.heightProperty());
        closeToolbar.prefWidthProperty().bind(closeButtonPane.widthProperty());

    }

    public TextField getTitleTextField() {
        return titleTextField;
    }

    public TextField getNumAtoms() {
        return numAtoms;
    }

    public TextField getNumRemovedEdges() {
        return numRemovedEdges;
    }

    public TextField getNumEdges() {
        return numEdges;
    }

    public MenuItem getLoadFromFile() {
        return loadFromFile;
    }

    public MenuItem getLoadPDBList() {
        return loadPDBList;
    }

    public MenuItem getLoadFromRCSB() {
        return loadFromRCSB;
    }

    public MenuItem getCloseProgram() {
        return closeProgram;
    }

    public RadioMenuItem getShowListView() {
        return showListView;
    }

    public VBox getRightVBox() {
        return rightVBox;
    }

    public TextField getSearchBar() {
        return searchBar;
    }

    public ListView getPdbList() {
        return pdbList;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public Button getCancelRCSB() {
        return cancelRCSB;
    }

    public StackPane getDrawPane() {
        return drawPane;
    }

    public MenuItem getUndo() {
        return undo;
    }

    public MenuItem getRedo() {
        return redo;
    }

    public MenuItem getSelectNone() {
        return selectNone;
    }

    public MenuItem getSelectAll() {
        return selectAll;
    }

    public MenuItem getDrawFill() {
        return drawFill;
    }

    public MenuItem getDrawLine() {
        return drawLine;
    }


    public RadioMenuItem getAtomCol() {
        return atomCol;
    }

    public RadioMenuItem getSecStrcCol() {
        return secStrcCol;
    }

    public RadioMenuItem getAminoAcidCol() {
        return aminoAcidCol;
    }

    public RadioMenuItem getChainColor(){
        return chainColor;
    }


    public RadioMenuItem getBallStickView() {
        return ballStickView;
    }

    public RadioMenuItem getSurfaceView() {
        return surfaceView;
    }

    public RadioMenuItem getEdgeView() {
        return edgeView;
    }

    public RadioMenuItem getRibbonView() {
        return ribbonView;
    }

    public RadioMenuItem getCartoonView() {
        return cartoonView;
    }


    public RadioMenuItem getHide() {
        return hide;
    }

    public RadioMenuItem getAllShow() {
        return allShow;
    }

    public RadioMenuItem getBackboneShow() {
        return backboneShow;
    }

    public RadioMenuItem getSmallBackboneShow() {
        return smallBackboneShow;
    }

    public ScrollPane getLabelPane() {
        return labelPane;
    }

    public Slider getChangeAtomSize() {
        return changeAtomSize;
    }

    public Slider getChangeBondSize() {
        return changeBondSize;
    }

    public CheckBox getAutoRotation() {
        return autoRotation;
    }

    public Button getResetRotation() {
        return resetRotation;
    }

    public CheckBox getWhiteBackground() {
        return whiteBackground;
    }

    public ToolBar getToolBox() {
        return toolBox;
    }

    public ProgressIndicator getProgressIndicator() {
        return progressIndicator;
    }

    public Button getColorButton(){
        return colorButton;
    }

    public HBox getColorBox() {
        return colorBox;
    }

    public RadioButton getAtomColorToggle() {
        return atomColorToggle;
    }

    public RadioButton getChainColorToggle() {
        return chainColorToggle;
    }

    public RadioButton getAminoAcidColorToggle() {
        return aminoAcidColorToggle;
    }

    public RadioButton getSecStrcColorToggle() {
        return secStrcColorToggle;
    }

    public MenuItem getAminoAcidDistribution() {
        return aminoAcidDistribution;
    }

    public MenuItem getAtomDistribution(){
        return atomDistribution;
    }

    public RadioButton getAllToggle() {
        return allToggle;
    }

    public RadioButton getHideToggle() {
        return hideToggle;
    }

    public RadioButton getBackboneToggle() {
        return backboneToggle;
    }

    public RadioButton getSmallBackboneToggle() {
        return smallBackboneToggle;
    }

    public RadioButton getBallStickToggle() {
        return ballStickToggle;
    }

    public RadioButton getSpaceFillToggle() {
        return spaceFillToggle;
    }

    public RadioButton getEdgeToggle() {
        return edgeToggle;
    }

    public CheckBox getRibbonToggle() {
        return ribbonToggle;
    }

    public CheckBox getCartoonToggle() {
        return cartoonToggle;
    }

    public HBox getShowBox() {
        return showBox;
    }

    public Button getShowButton() {
        return showButton;
    }

    public HBox getModelBox() {
        return modelBox;
    }

    public Button getModelButton() {
        return modelButton;
    }

    public Button getCloseToolbar() {
        return closeToolbar;
    }

    public HBox getToolHBox(){
        return toolHBox;
    }

    public Pane getCloseButtonPane() {
        return closeButtonPane;
    }
}
