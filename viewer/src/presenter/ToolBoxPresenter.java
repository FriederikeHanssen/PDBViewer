package presenter;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

/**
 * @author Friederike Hanssen, 01.2018
 */

class ToolBoxPresenter {

    private final MainPresenter mainPresenter;

    private EventHandler<ActionEvent> autoRotateHandler;
    private EventHandler<ActionEvent> resetRotation;
    private EventHandler<ActionEvent> colorBoxHandler;
    private EventHandler<ActionEvent> showBoxHandler;
    private EventHandler<ActionEvent> modelBoxHandler;
    private EventHandler<ActionEvent> closeToolBarHandler;


    public ToolBoxPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        addListener();
        setListener();
    }

    private void addListener() {
        changeBackgroundColor();
        addAutoRotateHandler();
        resetRotationHandler();
        addColorToggleGroup();
        addShowToggleGroup();
        addModelToggleGroup();
        addColorBoxHandler();
        addShowBoxHandler();
        addModelBoxHandler();
        addCloseToolBarHandler();

    }

    private void setListener() {
        this.mainPresenter.getConnector().getAutoRotation().setOnAction(autoRotateHandler);
        this.mainPresenter.getConnector().getResetRotation().setOnAction(resetRotation);

        this.mainPresenter.getConnector().getColorButton().setOnAction(colorBoxHandler);
        this.mainPresenter.getConnector().getShowButton().setOnAction(showBoxHandler);
        this.mainPresenter.getConnector().getModelButton().setOnAction(modelBoxHandler);

        this.mainPresenter.getConnector().getCloseToolbar().setOnAction(closeToolBarHandler);
    }

    private void changeBackgroundColor() {
        this.mainPresenter.getConnector().getWhiteBackground().setOnAction((ActionEvent event) -> {
            if (this.mainPresenter.getConnector().getWhiteBackground().isSelected()) {
                this.mainPresenter.getaGraphView().getBottom().setStyle("-fx-background-color: #FFFFFF");
                this.mainPresenter.getConnector().getCloseButtonPane().setStyle("-fx-background-color: #FFFFFF");
            } else {
                this.mainPresenter.getaGraphView().getBottom().setStyle("-fx-background-color: #000000");
                this.mainPresenter.getConnector().getCloseButtonPane().setStyle("-fx-background-color: #000000");

            }
        });
    }

    private void addAutoRotateHandler() {
        Timeline timelineSimple = new Timeline();
        autoRotateHandler = event -> {
            if (mainPresenter.getConnector().getAutoRotation().isSelected()) {


                KeyFrame k = new KeyFrame(Duration.millis(10), e -> {
                    mainPresenter.getRotate().setPivotX(mainPresenter.getPivotX().getValue());
                    mainPresenter.getRotate().setPivotY(mainPresenter.getPivotY().getValue());
                    mainPresenter.getRotate().setPivotZ(mainPresenter.getPivotZ().getValue());
                    mainPresenter.getRotate().setAngle(0.1);
                    mainPresenter.getAssemblageTransformProperty().setValue(mainPresenter.getRotate().createConcatenation(mainPresenter.getAssemblageTransformProperty().getValue()));
                });
                timelineSimple.getKeyFrames().add(k);
                timelineSimple.setCycleCount(Timeline.INDEFINITE);
                timelineSimple.play();
            } else {
                timelineSimple.stop();
            }
        };
    }

    private void resetRotationHandler(){
        resetRotation = event -> {
            mainPresenter.getAssemblageTransformProperty().setValue(new Rotate());
            mainPresenter.setRotate(new Rotate(1.0, new Point3D(0, 1, 0)));
        };
    }

    private void addColorToggleGroup(){
        this.mainPresenter.getConnector().getAtomColorToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getAtomCol().selectedProperty());
        this.mainPresenter.getConnector().getAminoAcidColorToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getAminoAcidCol().selectedProperty());
        this.mainPresenter.getConnector().getSecStrcColorToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getSecStrcCol().selectedProperty());
        this.mainPresenter.getConnector().getChainColorToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getChainColor().selectedProperty());

    }

    private void addShowToggleGroup(){
        this.mainPresenter.getConnector().getAllToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getAllShow().selectedProperty());
        this.mainPresenter.getConnector().getBackboneToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getBackboneShow().selectedProperty());
        this.mainPresenter.getConnector().getSmallBackboneToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getSmallBackboneShow().selectedProperty());
        this.mainPresenter.getConnector().getHideToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getHide().selectedProperty());

    }

    private void addModelToggleGroup(){
        this.mainPresenter.getConnector().getBallStickToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getBallStickView().selectedProperty());
        this.mainPresenter.getConnector().getSpaceFillToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getSurfaceView().selectedProperty());
        this.mainPresenter.getConnector().getEdgeToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getEdgeView().selectedProperty());
        this.mainPresenter.getConnector().getRibbonToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getRibbonView().selectedProperty());
        this.mainPresenter.getConnector().getCartoonToggle().selectedProperty().bindBidirectional(this.mainPresenter.getConnector().getCartoonView().selectedProperty());

    }

    private void addColorBoxHandler(){
        colorBoxHandler = event -> {
            mainPresenter.getConnector().getColorBox().managedProperty().setValue(!mainPresenter.getConnector().getColorBox().managedProperty().getValue());
            mainPresenter.getConnector().getColorBox().visibleProperty().setValue(!mainPresenter.getConnector().getColorBox().visibleProperty().getValue());

        };
    }

    private void addShowBoxHandler(){
        showBoxHandler = event -> {
            mainPresenter.getConnector().getShowBox().managedProperty().setValue(!mainPresenter.getConnector().getShowBox().managedProperty().getValue());
            mainPresenter.getConnector().getShowBox().visibleProperty().setValue(!mainPresenter.getConnector().getShowBox().visibleProperty().getValue());

        };
    }

    private void addModelBoxHandler(){
        modelBoxHandler = event -> {
            mainPresenter.getConnector().getModelBox().managedProperty().setValue(!mainPresenter.getConnector().getModelBox().managedProperty().getValue());
            mainPresenter.getConnector().getModelBox().visibleProperty().setValue(!mainPresenter.getConnector().getModelBox().visibleProperty().getValue());

        };
    }

    private void addCloseToolBarHandler() {
        this.mainPresenter.getConnector().getToolHBox().setOnMouseEntered(event -> {
                    mainPresenter.getConnector().getCloseToolbar().setManaged(true);
                    mainPresenter.getConnector().getCloseToolbar().setVisible(true);

            }
        );

        this.mainPresenter.getConnector().getToolHBox().setOnMouseExited(event -> {
                    mainPresenter.getConnector().getCloseToolbar().setManaged(false);
                    mainPresenter.getConnector().getCloseToolbar().setVisible(false);
                }
        );

        closeToolBarHandler = event -> {
            mainPresenter.getConnector().getToolBox().setManaged(!mainPresenter.getConnector().getToolBox().managedProperty().getValue());
            mainPresenter.getConnector().getToolBox().setVisible(!mainPresenter.getConnector().getToolBox().visibleProperty().getValue());
            if(mainPresenter.getConnector().getCloseToolbar().textProperty().getValue().equals(">")){
                mainPresenter.getConnector().getCloseToolbar().textProperty().setValue("<");
            }else{
                mainPresenter.getConnector().getCloseToolbar().textProperty().setValue(">");
            }
        };
    }
}
