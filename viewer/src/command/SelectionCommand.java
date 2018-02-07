package command;

import model.graph.ANode;
import model.ASelectionModel;
import view.label.LabelList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectionCommand extends ACommand {

    private final boolean isShiftDown;
    private final ASelectionModel<ANode> nodeSelectionModel;
    private final List<ANode> nodeID;
    private final List<ANode> previousSelectedNodes;
    private final LabelList labelList;

    public SelectionCommand(boolean isShiftDown, ASelectionModel<ANode> nodeSelectionModel, LabelList labels, ANode... nodeID){
        this.setName("Selection");
        this.isShiftDown = isShiftDown;
        this.nodeSelectionModel = nodeSelectionModel;
        this.nodeID = Arrays.asList(nodeID);
        this.previousSelectedNodes = new ArrayList<>(nodeSelectionModel.getSelectedItems());
        this.labelList = labels;
    }

    @Override
    public void execute() {
        if(!isShiftDown){
            clearSelections();
        }

        nodeID.forEach(node ->{
            nodeSelectionModel.select(node);
            labelList.getLabel(node.getAminoAcidID()).setColor("#B8860B");
        });

        if(nodeID.size() == 0){
            clearSelections();
        }
    }

    @Override
    public void undo(){
        clearSelections();

        previousSelectedNodes.forEach( node ->{
            nodeSelectionModel.select(node);
            labelList.getLabel(node.getAminoAcidID()).setColor("#B8860B");

        });
    }

    @Override
    public void redo(){
        nodeID.forEach(node ->{
            nodeSelectionModel.select(node);
            labelList.getLabel(node.getAminoAcidID()).setColor("#B8860B");
        });
        if(nodeID.size() == 0){
            clearSelections();
        }
    }

    private void clearSelections(){
        nodeSelectionModel.getSelectedItems().forEach(node -> labelList.getLabel(node.getAminoAcidID()).setColor("#FFFFFF"));
        nodeSelectionModel.clearSelection();
    }
}