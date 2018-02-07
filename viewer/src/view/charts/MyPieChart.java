package view.charts;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

/**
 * @author Friederike Hanssen, 01.2018
 */

public class MyPieChart {

    private final PieChart pieChart;
    private Stage stage;


    public MyPieChart() {

        this.pieChart = new PieChart();
        this.pieChart.setLegendVisible(false);
        setupStage();
    }

    private void setupStage() {
        Scene scene = new Scene(new Group());
        stage = new Stage();
        stage.setTitle("Charts");
        stage.setWidth(500);
        stage.setHeight(500);
        ((Group) scene.getRoot()).getChildren().add(pieChart);
        stage.setScene(scene);
    }

    public void setData(ObservableList<javafx.scene.chart.PieChart.Data> pieChartData, String title, String... colors) {
        pieChart.setTitle(title);

        pieChart.setData(pieChartData);
        int j = 0;
        for (PieChart.Data data : pieChartData) {
            data.getNode().setStyle(
                    "-fx-pie-color: " + colors[j] + ";"
            );
            j++;
        }
    }

    public void show() {
        stage.show();
    }
}
