package presenter;

import command.CommandManager;
import javafx.application.Platform;
import javafx.beans.property.*;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Transform;
import model.ASelectionModel;
import model.graph.AGraph;
import model.graph.ANode;
import model.peptide.AminoAcid;
import utils.PDBParser;
import utils.PDBToGraphFormatMover;
import view.connector.Connector;
import javafx.stage.*;
import view.graph.AGraphView;
import view.label.LabelList;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class MainPresenter {

    private final double changeAtomSliderDefault = 1.0;
    private final double changeBondSliderDefault = 5.0;

    private final Connector connector;
    private final AGraph aGraph;
    private final AGraphView aGraphView;
    private final Stage primaryStage;
    private final ASelectionModel<ANode> aNodeASelectionModel;
    private final Property<Transform> assemblageTransformProperty = new SimpleObjectProperty<>(new Rotate());
    private final CommandManager commandManager;
    private final DoubleProperty pivotX = new SimpleDoubleProperty();
    private final DoubleProperty pivotY = new SimpleDoubleProperty();
    private final DoubleProperty pivotZ = new SimpleDoubleProperty();
    private final LabelList labelList;
    private Rotate rotate = new Rotate(1.0, new Point3D(0, 1, 0));

    private final BooleanProperty isgraphReadIn;

    private GraphPresenter graphPresenter;
    private ColorPresenter colorPresenter;


    public MainPresenter(Connector connector, Stage primaryStage) {
        this.connector = connector;
        this.primaryStage = primaryStage;
        this.aGraph = new AGraph();
        this.aNodeASelectionModel = new ASelectionModel<>();
        this.aGraphView = new AGraphView(connector, aNodeASelectionModel, assemblageTransformProperty);
        this.labelList = new LabelList(connector);
        this.commandManager = new CommandManager();
        this.isgraphReadIn = new SimpleBooleanProperty(false);


        addCloseProgram();
        addBindings();
        setUpPresenter();
    }

    private void addCloseProgram() {
        this.connector.getCloseProgram().setOnAction(event -> Platform.exit());
    }


    private void setUpPresenter() {
        graphPresenter = new GraphPresenter(this);
        IOPresenter ioPresenter = new IOPresenter(this);
        AppearancePresenter appearancePresenter = new AppearancePresenter(this);
        colorPresenter = new ColorPresenter(this);
        ToolBoxPresenter toolBoxPresenter = new ToolBoxPresenter(this);
        ShowPresenter showPresenter = new ShowPresenter(this);
        EditPresenter editPresenter = new EditPresenter(this);
        ChartPresenter chartPresenter = new ChartPresenter(this);
    }

    public void reset(){
        this.connector.getHide().setSelected(false);
        this.connector.getRibbonView().setSelected(false);
        this.aGraph.clear();
        this.assemblageTransformProperty.setValue(new Rotate());
        this.rotate = new Rotate(1.0, new Point3D(0, 1, 0));
        this.aGraphView.getCamera().setTranslateZ(AGraphView.cameraZTranslate);
        this.aNodeASelectionModel.setItems(this.aGraph.getNodes().
                toArray(new ANode[this.aGraph.getNodes().size()]));
        this.commandManager.clear();
        this.aGraphView.removeCartoonView();
        AminoAcid.counter = 0;

        this.connector.getAtomCol().selectedProperty().setValue(true);
        this.connector.getAllShow().selectedProperty().setValue(true);
        this.connector.getBallStickView().selectedProperty().setValue(true);

        this.connector.getColorBox().managedProperty().setValue(false);
        this.connector.getColorBox().visibleProperty().setValue(false);
        this.connector.getShowBox().managedProperty().setValue(false);
        this.connector.getShowBox().visibleProperty().setValue(false);
        this.connector.getModelBox().managedProperty().setValue(false);
        this.connector.getModelBox().visibleProperty().setValue(false);

        this.connector.getChangeAtomSize().setValue(changeAtomSliderDefault);
        this.connector.getChangeBondSize().setValue(changeBondSliderDefault);
        this.isgraphReadIn.setValue(false);



    }

    private void addBindings() {
        this.connector.getRightVBox().visibleProperty().bind(this.connector.getShowListView().selectedProperty());
        this.connector.getRightVBox().managedProperty().bind(this.connector.getShowListView().selectedProperty());

        this.connector.getSearchBar().visibleProperty().bind(this.connector.getProgressBar().visibleProperty().not());
        this.connector.getSearchBar().managedProperty().bind(this.connector.getProgressBar().managedProperty().not());

        this.connector.getCancelRCSB().visibleProperty().bind(this.connector.getProgressBar().visibleProperty());
        this.connector.getCancelRCSB().managedProperty().bind(this.connector.getProgressBar().managedProperty());

        //Disable functionality if no graph is loaded
        BooleanProperty graphLoaded = new SimpleBooleanProperty();
        graphLoaded.bind(new SimpleListProperty<>(aGraph.getNodes()).emptyProperty().not());
        this.connector.getToolHBox().visibleProperty().bind(graphLoaded);
        this.connector.getToolHBox().managedProperty().bind(graphLoaded);

        this.connector.getUndo().disableProperty().bind(commandManager.canUndoProperty().not());
        this.connector.getRedo().disableProperty().bind(commandManager.canRedoProperty().not());

        this.connector.getSelectAll().disableProperty().bind(graphLoaded.not());
        this.connector.getSelectNone().disableProperty().bind(graphLoaded.not());
        this.connector.getDrawFill().disableProperty().bind(graphLoaded.not());
        this.connector.getDrawLine().disableProperty().bind(graphLoaded.not());
        this.connector.getBallStickView().disableProperty().bind(graphLoaded.not());
        this.connector.getSurfaceView().disableProperty().bind(graphLoaded.not());
        this.connector.getEdgeView().disableProperty().bind(graphLoaded.not());
        this.connector.getRibbonView().disableProperty().bind(graphLoaded.not());
        this.connector.getCartoonView().disableProperty().bind(graphLoaded.not());
        this.connector.getAtomCol().disableProperty().bind(graphLoaded.not());
        this.connector.getAminoAcidCol().disableProperty().bind(graphLoaded.not());
        this.connector.getSecStrcCol().disableProperty().bind(graphLoaded.not());
        this.connector.getChainColor().disableProperty().bind(graphLoaded.not());
        this.connector.getAllShow().disableProperty().bind(graphLoaded.not());
        this.connector.getBackboneShow().disableProperty().bind(graphLoaded.not());
        this.connector.getSmallBackboneShow().disableProperty().bind(graphLoaded.not());
        this.connector.getHide().disableProperty().bind(graphLoaded.not());
        this.connector.getAtomDistribution().disableProperty().bind(graphLoaded.not());
        this.connector.getAminoAcidDistribution().disableProperty().bind(graphLoaded.not());


        //Bind text fields
        this.connector.getTitleTextField().textProperty().bind(PDBParser.header);
        this.connector.getNumRemovedEdges().textProperty().bind(PDBToGraphFormatMover.removedBonds.asString());

        this.connector.getNumAtoms().textProperty().bind(new SimpleListProperty<>(aGraph.getNodes()).sizeProperty().asString());
        this.connector.getNumEdges().textProperty().bind(new SimpleListProperty<>(aGraph.getEdges()).sizeProperty().asString());

        this.connector.getUndo().textProperty().bind(commandManager.undoNameProperty());
        this.connector.getRedo().textProperty().bind(commandManager.redoNameProperty());

    }

    public Connector getConnector() {
        return connector;
    }

    public AGraph getaGraph() {
        return aGraph;
    }

    public AGraphView getaGraphView() {
        return aGraphView;
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ASelectionModel<ANode> getaNodeASelectionModel() {
        return aNodeASelectionModel;
    }

    public Property<Transform> getAssemblageTransformProperty() {
        return assemblageTransformProperty;
    }

    public GraphPresenter getGraphPresenter() {
        return graphPresenter;
    }

    public LabelList getLabelList() {
        return labelList;
    }

    public DoubleProperty getPivotX() {
        return pivotX;
    }

    public DoubleProperty getPivotY() {
        return pivotY;
    }

    public DoubleProperty getPivotZ() {
        return pivotZ;
    }

    public Rotate getRotate() {
        return rotate;
    }

    public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public ColorPresenter getColorPresenter() {
        return colorPresenter;
    }

    public BooleanProperty getIsgraphReadIn() {
        return isgraphReadIn;
    }
}
