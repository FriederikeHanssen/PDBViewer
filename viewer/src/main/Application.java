package main;

import view.connector.Connector;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SceneAntialiasing;
import javafx.stage.Stage;
import presenter.MainPresenter;

import java.io.InputStream;


public class Application extends javafx.application.Application {

    private final double prefWidth = 1000.;
    private final double prefHeight = 800.;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root;
        try(InputStream ins= getClass().getResource("../view/connector/mainView.fxml").openStream()){
            root=fxmlLoader.load(ins);
        }

        Connector connector = fxmlLoader.getController();

        MainPresenter presenter = new MainPresenter(connector, primaryStage);

        //set Stage boundaries to visible bounds of the main screen
        primaryStage.setScene(new Scene(root, prefWidth, prefHeight,false, SceneAntialiasing.BALANCED));
        primaryStage.setTitle("PDBViewer");
        primaryStage.setFullScreen(true);
        primaryStage.show();
    }

}
