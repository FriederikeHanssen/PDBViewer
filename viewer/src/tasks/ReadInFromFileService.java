package tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import model.peptide.AminoAcid;
import utils.PDBParser;

import java.io.IOException;
import java.util.List;

public class ReadInFromFileService extends Service<List<List<AminoAcid>>> {
    private String filepath;

    @Override
    protected Task<List<List<AminoAcid>>> createTask(){
        return new Task<List<List<AminoAcid>>>(){
            @Override
            protected List<List<AminoAcid>> call(){
                try {
                    List<List<AminoAcid>> chains = PDBParser.readFromFile(filepath);
                    for(int i = 0; i <= chains.size(); i++){
                        updateProgress(i, chains.size());
                    }
                    return chains;
                }catch(IOException exp){
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "File could not be loaded: \n" + exp.getMessage(), ButtonType.OK);
                    alert.showAndWait();
                    exp.printStackTrace();
                    return null;
                }

            }
        };
    }

    public void restart(String filepath){
        this.filepath = filepath;
        super.restart();
    }
}
