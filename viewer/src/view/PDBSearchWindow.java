package view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import view.connector.PopupConnector;

import java.io.IOException;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class PDBSearchWindow {

    private final FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/connector/Popup.fxml"));
    private final Stage stage;

    public PDBSearchWindow() {
        stage = new Stage();
        try {
            final Parent root = loader.load();
            final Scene scene = new Scene(root);
            stage.setScene(scene);

        } catch (IOException e) {

        }
    }

    public void show(){

            stage.show();
    }

    public PopupConnector getController(){
        return loader.getController();
    }

    public void hide(){
        stage.hide();
    }
}
