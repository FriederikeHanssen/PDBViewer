package tasks;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import model.peptide.AminoAcid;
import utils.PDBParser;

import java.util.List;

public class ReadInFromListService extends Service<List<List<AminoAcid>>> {

    private List<String> pdb;

    public ReadInFromListService(){

    }

    @Override
    protected Task<List<List<AminoAcid>>> createTask(){
        return new Task<List<List<AminoAcid>>>(){
            @Override
            protected List<List<AminoAcid>> call(){
                List<List<AminoAcid>> chains = PDBParser.readFromList(pdb);
                for(int i = 0; i <= chains.size(); i++){
                    updateProgress(i, chains.size());
                }
                return chains;
            }
        };
    }

    public void restart(List<String> pdb){
        this.pdb = pdb;
        super.restart();
    }
}
