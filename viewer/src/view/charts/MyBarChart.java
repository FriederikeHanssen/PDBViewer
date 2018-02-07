package view.charts;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.stage.Stage;


@SuppressWarnings("unchecked")
public class MyBarChart {

    private final CategoryAxis xAxis;
    private final NumberAxis yAxis;
    private final BarChart<String,Number> bc;
    private Stage stage;

    public MyBarChart(){
        this.xAxis = new CategoryAxis();
        this.yAxis = new NumberAxis();
        this.bc = new BarChart<>(xAxis,yAxis);
        this.bc.setLegendVisible(false);

        setupStage();
    }

    private void setupStage(){
        Scene scene = new Scene(new Group());
        stage = new Stage();
        stage.setTitle("Charts");
        stage.setWidth(500);
        stage.setHeight(500);
        ((Group) scene.getRoot()).getChildren().add(bc);
        stage.setScene(scene);
    }
    public void setData(XYChart.Series data, String title, String xLabel, String yLabel){
        bc.getData().removeAll(bc.getData());

        bc.getData().add(data);

        bc.setTitle(title);
        xAxis.setLabel(xLabel);
        yAxis.setLabel(yLabel);
    }

    public void show(){
        stage.show();

    }



}
