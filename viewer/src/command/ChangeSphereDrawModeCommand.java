package command;


import exception.MyException;
import javafx.scene.shape.DrawMode;
import model.graph.ANode;
import view.graph.AGraphView;
import view.graph.ANodeView;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChangeSphereDrawModeCommand extends ACommand {

    private final AGraphView graphView;
    private final List<ANode> selectedItems;
    private final Map<Integer, DrawMode> previous;
    private final Map<Integer, DrawMode> redo;
    private final boolean isFill;

    public ChangeSphereDrawModeCommand(boolean isFill, AGraphView graphView, List<ANode> selectedItems){
        this.graphView = graphView;
        this.selectedItems = selectedItems;
        this.previous = new HashMap<>();
        this.redo = new HashMap<>();
        this.isFill = isFill;
        if(isFill){
            this.setName("Fill");
        }else {
            this.setName("Line");
        }

    }

    @Override
    public void execute()  {
        if(isFill){
            drawWithDrawMode(DrawMode.FILL);
        }else{
            drawWithDrawMode(DrawMode.LINE);
        }
    }


    private void drawWithDrawMode(DrawMode mode){
        selectedItems.forEach( n -> {
            try {
                ANodeView nView = this.graphView.getNodeByID(n.getID());
                previous.put(n.getID(), nView.getSphere().getDrawMode());
                nView.getSphere().setDrawMode(mode);
            }catch(MyException e){
                System.err.println("Error during change draw command");
            }
        });
    }

    @Override
    public void undo()  {
        selectedItems.forEach( n -> {
            try {
                ANodeView nView = this.graphView.getNodeByID(n.getID());
                redo.put(n.getID(),nView.getSphere().getDrawMode());
                nView.getSphere().setDrawMode(previous.get(n.getID()));
            }catch(MyException e){
                System.err.println("Error during change draw command");
            }
        });
    }

    @Override
    public void redo() {
        selectedItems.forEach( n -> {
            try {
                ANodeView nView = this.graphView.getNodeByID(n.getID());
                nView.getSphere().setDrawMode(redo.get(n.getID()));
            }catch(MyException e){
                System.err.println("Error during change draw command");
            }
        });
    }
}