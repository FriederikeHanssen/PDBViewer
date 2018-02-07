package view.label;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import model.peptide.AminoAcidProperties;
import model.peptide.SecondaryStructureProperties;


public class LabelView extends Group{

    private  Label label;
    private final IntegerProperty ID;

    public LabelView(){
        this.ID = new SimpleIntegerProperty(-1);
    }

    public LabelView(int id, AminoAcidProperties aminoAcidProperties, SecondaryStructureProperties secondaryStructureProperties){
        this.ID = new SimpleIntegerProperty(id);
        label = new Label(String.valueOf(aminoAcidProperties.getOneLetterCode()).concat("\n".concat(String.valueOf(secondaryStructureProperties.getRepresentation()))));
        label.setFont(Font.font ("Lucida Console", 13));
        label.setWrapText(true);
        label.setStyle("-fx-border-color: black;");
        label.setStyle("-fx-padding: 1 1 1 1");

        this.getChildren().add(label);
    }

    public int getID() {
        return ID.get();
    }

    public void setColor(String color) {
        if(label != null){
            label.setStyle("-fx-background-color: ".concat(color));
            label.setStyle("-fx-border-color: ".concat(color));

        }
    }
}
