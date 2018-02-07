package presenter;

import javafx.beans.InvalidationListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.chart.*;
import model.graph.ANode;
import model.peptide.AminoAcidProperties;
import model.peptide.AtomProperties;
import view.charts.MyBarChart;
import view.charts.MyPieChart;

import java.util.*;


@SuppressWarnings("unchecked")
class ChartPresenter {

    private final MainPresenter mainPresenter;

    private InvalidationListener nodeInvalidationListener;
    private InvalidationListener superNodeInvalidationListener;
    private EventHandler<ActionEvent> atomDistributionHandler;
    private EventHandler<ActionEvent> aminoAcidDistributionHandler;

    private final MyPieChart atomDistributionChart;
    private final MyBarChart aminoAcidDistributionChart;

    public ChartPresenter(MainPresenter mainPresenter){
        this.mainPresenter = mainPresenter;

        atomDistributionChart = new MyPieChart();
        aminoAcidDistributionChart = new MyBarChart();

        addListener();
        setListener();

    }

    private void addListener(){
        addNodeListener();
        addSuperNodeListener();
        addPieChartAtomDistributionHandler();
        addPieChartAminoAcidDistributionHandler();

    }

    private void setListener(){
        this.mainPresenter.getaGraph().getNodes().addListener(nodeInvalidationListener);
        this.mainPresenter.getaGraph().getSuperNodes().addListener(superNodeInvalidationListener);
        this.mainPresenter.getConnector().getAtomDistribution().setOnAction(atomDistributionHandler);
        this.mainPresenter.getConnector().getAminoAcidDistribution().setOnAction(aminoAcidDistributionHandler);
    }

    private void addNodeListener(){
        nodeInvalidationListener = InvalidationListener -> {
            //If nodes change recompute atom distribution
            Map<AtomProperties, Integer> map = new HashMap<>();
            for(ANode node : this.mainPresenter.getaGraph().getNodes()){
                int counter = 0;
                if(map.containsKey(AtomProperties.valueOf(node.getAtomType()))){
                    counter = map.get(AtomProperties.valueOf(node.getAtomType()));
                }
                counter++;
                map.put(AtomProperties.valueOf(node.getAtomType()), counter);
            }
            ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
            String[] colors = new String[map.keySet().size()];
            int i = 0;
            for(AtomProperties a : map.keySet()){
                pieChartData.add( new PieChart.Data(a.getName(), map.get(a)));
                colors[i] = "#".concat(a.getColorAsString().substring(2,8));
                i++;

            }
            atomDistributionChart.setData(pieChartData, "Atom Distribution", colors);

        };
    }

    private void addSuperNodeListener(){
        superNodeInvalidationListener = InvalidationListener -> {
            //If amino acids are added or removed the distribution changes
            Map<AminoAcidProperties, Integer> map = new HashMap<>();

            for (Integer superNodeID : this.mainPresenter.getaGraph().getSuperNodes().keySet()) {
                int counter = 0;
                if (map.containsKey(this.mainPresenter.getaGraph().getSuperNodes().get(superNodeID).getAminoAcid())) {
                    counter = map.get(this.mainPresenter.getaGraph().getSuperNodes().get(superNodeID).getAminoAcid());
                }
                counter++;
                map.put(this.mainPresenter.getaGraph().getSuperNodes().get(superNodeID).getAminoAcid(), counter);
            }

            List<AminoAcidProperties> aminoAcids = new ArrayList<>(map.keySet());
            Collections.sort(aminoAcids);
            XYChart.Series data = new XYChart.Series();
            for(AminoAcidProperties a : aminoAcids){
                data.getData().add(new XYChart.Data(a.getThreeLetterCode(), map.get(a)));
            }

            aminoAcidDistributionChart.setData(data, "Amino Acid Distribution", "Amino Acids", "Count");
        };

    }

    private void addPieChartAtomDistributionHandler(){
        atomDistributionHandler = event -> atomDistributionChart.show();
    }

    private void addPieChartAminoAcidDistributionHandler(){
        aminoAcidDistributionHandler = event -> aminoAcidDistributionChart.show();
    }
}
