package view.label;

import view.connector.Connector;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import model.peptide.AminoAcidProperties;
import model.peptide.SecondaryStructureProperties;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class LabelList extends Group {

    private final HBox labelList;

    public LabelList(Connector connector){

        this.labelList = new HBox(2);
        this.getChildren().addAll(this.labelList);

        connector.getLabelPane().setContent(this);

    }

    public void addLabel(int id, AminoAcidProperties aminoAcidProperties, SecondaryStructureProperties secondaryStructureProperties){
       labelList.getChildren().add(new LabelView(id, aminoAcidProperties, secondaryStructureProperties));
    }

    public void removeLabel(int id){
        LabelView delete = getLabel(id);

        if(delete.getID() != -1){
            this.labelList.getChildren().remove(delete);
        }
        delete = null;
    }

    public LabelView getLabel(int id){
        for(Node l : labelList.getChildren()){
            if(((LabelView) l).getID() == id){
                return (LabelView) l;
            }
        }
        return new LabelView();
    }

    public ObservableList<Node> getLabelList() {
        return labelList.getChildren();
    }
}
