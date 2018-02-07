package view.connector;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class PopupConnector implements Initializable {

    @FXML
    private TextField pdbCode;

    @FXML
    private Button okButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public TextField getPdbCode() {
        return pdbCode;
    }

    public Button getOkButton() {
        return okButton;
    }
}
