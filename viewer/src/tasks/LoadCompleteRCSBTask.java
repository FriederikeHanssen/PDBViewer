package tasks;

import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

class LoadCompleteRCSBTask extends Task<List<String>> {

    private final List<String> result = new ArrayList<>();

    @Override
    protected List<String> call(){
        accessRCSB();
        return result;
    }

    private void accessRCSB(){
        try {
            int counter = 0;
            //get all pdb ids, create custom report with given fields: (pdb)id, Type(Protein, DNA,....), name, service wsfile: access webservice client
            //For further information on fields: https://www.rcsb.org/pdb/results/reportField.do
            LoadFromURLTask urlTask = new LoadFromURLTask("http://www.rcsb.org/pdb/rest/customReport.csv?pdbids=*&customReportColumns=structureId,macromoleculeType,classification&format=csv&service=wsfile");
            List<String> pdbs = urlTask.call();
            for (String line : pdbs) {
                line = line.replaceAll("\"", "");
                String[] lineMembers = line.split(",");
                if (lineMembers[1].equals("Protein")) {
                    String temp = "";
                    for (int i = 0; i < lineMembers.length; i++) {
                        if (i != 1) {
                            temp = temp.concat(lineMembers[i].concat("    "));
                        }
                    }
                    counter++;
                    //looked up currently loaded files: 126208
                    this.updateProgress(counter,150000);
                    result.add(temp);
                }
            }
            this.updateProgress(counter, result.size());

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Failed to access RCSB: \n" + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
        }
    }


}
