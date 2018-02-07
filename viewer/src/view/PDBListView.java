package view;

import view.connector.Connector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;

import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class PDBListView{

    private final ObservableList<String> pdbList = FXCollections.observableArrayList();
    private final  FilteredList<String> filteredList;

    @SuppressWarnings("unchecked")
    public PDBListView(Connector connector){
        filteredList  = new FilteredList<>(pdbList, s -> true);
        connector.getPdbList().setItems(filteredList);
    }

    public void addPDBs(List<String> pdbs){
        this.pdbList.addAll(pdbs);
    }

    public FilteredList<String> getFilteredList() {
        return filteredList;
    }

    public void clear(){
        pdbList.clear();
        filteredList.clear();
    }
}
