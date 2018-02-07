package presenter;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.stage.FileChooser;
import tasks.*;
import view.PDBListView;
import view.PDBSearchWindow;
import view.connector.PopupConnector;

import java.io.File;
import java.util.List;

/**
 * @author Friederike Hanssen, 01.2018
 */

@SuppressWarnings("unchecked")
class IOPresenter {

    private final MainPresenter mainPresenter;
    private final FileChooser pdbFileChooser;
    private final LoadCompleteRCSBTaskService rcsbService;
    private final LoadFromURLService pdbService;
    private final ReadInFromListService readInFromListService;
    private final ReadInFromFileService readInFromFileService;
    private final PDBListView pdbListView;
    private final PDBSearchWindow pdbSearchWindow;
    private final PopupConnector popupConnector;


    public IOPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        this.pdbFileChooser = new FileChooser();
        this.pdbFileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter(".pdb", "*.pdb"));

        this.rcsbService = new LoadCompleteRCSBTaskService();
        this.pdbService = new LoadFromURLService();
        this.readInFromListService = new ReadInFromListService();
        this.readInFromFileService = new ReadInFromFileService();
        this.pdbListView = new PDBListView(this.mainPresenter.getConnector());
        this.pdbSearchWindow = new PDBSearchWindow();
        this.popupConnector = pdbSearchWindow.getController();

        setUpButtonListener();
        addTasksAndServices();
        loadListView();
    }

    private void setUpButtonListener() {
        addLoadPDBFileHandler();
        addListViewHandler();
        addLoadFromRCSB();
        addProgressBarHandler();
        addSearchBarHandler();
    }

    /* Visibility and Button Bindings */

    private void addProgressBarHandler() {
        this.mainPresenter.getConnector().getProgressBar().progressProperty().bind(rcsbService.progressProperty());

        //Hide Progress bar on completion
        this.mainPresenter.getConnector().getProgressBar().visibleProperty().bind(rcsbService.runningProperty());
        this.mainPresenter.getConnector().getProgressBar().managedProperty().bind(this.mainPresenter.getConnector().getProgressBar().visibleProperty());

    }

    /* Load PDBs*/

    private void addLoadPDBFileHandler() {
        this.mainPresenter.getConnector().getLoadFromFile().setOnAction(e -> loadFromFile(this.pdbFileChooser.showOpenDialog(this.mainPresenter.getPrimaryStage())));

    }

    private void addListViewHandler() {
        this.mainPresenter.getConnector().getPdbList().getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> loadFromList(newValue));
    }

    private void loadFromFile(File file) {
        if (file != null) {
            unbindProgressIndicator();
            this.mainPresenter.getConnector().getProgressIndicator().progressProperty().bind(readInFromFileService.progressProperty());
            this.mainPresenter.getConnector().getProgressIndicator().visibleProperty().bind(readInFromFileService.runningProperty());
            this.mainPresenter.getConnector().getProgressIndicator().managedProperty().bind(readInFromFileService.runningProperty());

            this.mainPresenter.reset();
            this.readInFromFileService.restart(file.getPath());
        }
    }

    private void loadFromList(Object pdbName) {
        if (pdbName != null) {
            unbindProgressIndicator();
            this.mainPresenter.getConnector().getProgressIndicator().progressProperty().bind(readInFromListService.progressProperty());
            this.mainPresenter.getConnector().getProgressIndicator().visibleProperty().bind(readInFromListService.runningProperty().or(pdbService.runningProperty()));
            this.mainPresenter.getConnector().getProgressIndicator().managedProperty().bind(readInFromListService.runningProperty().or(pdbService.runningProperty()));

            this.mainPresenter.reset();
            pdbService.restart("https://files.rcsb.org/download/".concat(((String) pdbName).split(" ")[0].concat(".pdb")));
        }
    }

    private void unbindProgressIndicator(){
        this.mainPresenter.getConnector().getProgressIndicator().progressProperty().unbind();
        this.mainPresenter.getConnector().getProgressIndicator().visibleProperty().unbind();
        this.mainPresenter.getConnector().getProgressIndicator().managedProperty().unbind();
    }

    private void addLoadFromRCSB() {

        popupConnector.getPdbCode().textProperty().addListener((Observable obs) -> {
            String filter = popupConnector.getPdbCode().getText();
            if (filter == null || filter.length() == 0) {
                pdbListView.getFilteredList().setPredicate(s -> true);
            } else {
                pdbListView.getFilteredList().setPredicate(s -> (s.toUpperCase()).contains(filter.toUpperCase()));
            }
        });

        popupConnector.getOkButton().setOnAction(event1 -> {
            pdbSearchWindow.hide();
            loadFromList(popupConnector.getPdbCode().getCharacters().toString().toUpperCase());
            pdbListView.getFilteredList().setPredicate(s -> true);

        });

        this.mainPresenter.getConnector().getLoadFromRCSB().setOnAction(event -> {
            try {
                pdbSearchWindow.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void addTasksAndServices() {

        pdbService.setOnSucceeded(event -> {
            List<String> pdb = pdbService.getValue();
            readInFromListService.restart(pdb);
            readInFromListService.setOnSucceeded(event1 -> mainPresenter.getGraphPresenter().readIn(readInFromListService.getValue()));
        });

        readInFromFileService.setOnSucceeded(event -> mainPresenter.getGraphPresenter().readIn(readInFromFileService.getValue()));

        rcsbService.setOnSucceeded(event -> pdbListView.addPDBs(rcsbService.getValue()));
    }

    private void loadListView() {
        rcsbService.start();
        this.mainPresenter.getConnector().getCancelRCSB().setOnAction(event -> rcsbService.cancel());

        this.mainPresenter.getConnector().getLoadPDBList().setOnAction(event -> {
            pdbListView.clear();
            rcsbService.restart();
        });
    }

    private void addSearchBarHandler() {
        final BooleanProperty firstTime = new SimpleBooleanProperty(true);
        this.mainPresenter.getConnector().getSearchBar().focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue && firstTime.get()) {
                this.mainPresenter.getConnector().getDrawPane().requestFocus(); // Delegate the focus pane
                firstTime.setValue(false); // Variable value changed for future references
            }
        });

        this.mainPresenter.getConnector().getSearchBar().textProperty().addListener((Observable obs) -> {
            String filter = this.mainPresenter.getConnector().getSearchBar().getText();
            if (filter == null || filter.length() == 0) {
                pdbListView.getFilteredList().setPredicate(s -> true);
            } else {
                pdbListView.getFilteredList().setPredicate(s -> (s.toUpperCase()).contains(filter.toUpperCase()));
            }
        });
    }
}
