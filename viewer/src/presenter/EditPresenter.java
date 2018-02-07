package presenter;


import command.ChangeSphereDrawModeCommand;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.ASelectionModel;
import model.graph.ANode;

/**
 * @author Friederike Hanssen, 01.2018
 */

class EditPresenter {

    private final MainPresenter mainPresenter;

    private EventHandler<ActionEvent> undoHandler;
    private EventHandler<ActionEvent> redoHandler;
    private EventHandler<ActionEvent> drawFillHandler;
    private EventHandler<ActionEvent> drawLineHandler;

    public EditPresenter(MainPresenter mainPresenter){
        this.mainPresenter = mainPresenter;

        addListener();
        setListener();
    }

    private void addListener(){
        addUndoRedoHandler();
        addSphereDrawMode();
    }

    private void setListener(){
        this.mainPresenter.getConnector().getUndo().setOnAction(undoHandler);
        this.mainPresenter.getConnector().getRedo().setOnAction(redoHandler);
        this.mainPresenter.getConnector().getDrawFill().setOnAction(drawFillHandler);
        this.mainPresenter.getConnector().getDrawLine().setOnAction(drawLineHandler);
    }

    private void addUndoRedoHandler() {
        undoHandler = event -> {
            try {
                mainPresenter.getCommandManager().undo();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Event cannot be undone.", ButtonType.OK);
                alert.showAndWait();
            }
        };

        redoHandler = event -> {
            try {
                mainPresenter.getCommandManager().redo();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Event cannot be redone.", ButtonType.OK);
                alert.showAndWait();
            }
        };
    }


    private void addSphereDrawMode() {
        drawLineHandler = event -> {
            ASelectionModel<ANode> copy = new ASelectionModel<>(mainPresenter.getaGraph().getNodes().toArray(new ANode[mainPresenter.getaGraph().getNodes().size()]));
            mainPresenter.getaNodeASelectionModel().getSelectedItems().forEach(copy::select);
            ChangeSphereDrawModeCommand lineCommand = new ChangeSphereDrawModeCommand(false, mainPresenter.getaGraphView(), copy.getSelectedItems());
            try {
                mainPresenter.getCommandManager().executeAndAdd(lineCommand);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };

        drawFillHandler = event -> {
            ASelectionModel<ANode> copy = new ASelectionModel<>(mainPresenter.getaGraph().getNodes().toArray(new ANode[mainPresenter.getaGraph().getNodes().size()]));
            mainPresenter.getaNodeASelectionModel().getSelectedItems().forEach(copy::select);
            ChangeSphereDrawModeCommand fillCommand = new ChangeSphereDrawModeCommand(true, mainPresenter.getaGraphView(), copy.getSelectedItems());
            try {
                mainPresenter.getCommandManager().executeAndAdd(fillCommand);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        };
    }


}
