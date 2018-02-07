package presenter;

import exception.MyException;
import javafx.beans.value.ChangeListener;
import model.graph.AEdge;
import model.graph.ANode;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Friederike Hanssen, 01.2018
 */

@SuppressWarnings("unchecked")
class ShowPresenter {

    private static final Set<String> smallBackbone = new HashSet<>(Arrays.asList("N", "CA", "C"));
    private static final Set<String> largeBackbone = new HashSet<>(Arrays.asList("N", "CA", "C", "O", "CB"));

    private final MainPresenter mainPresenter;

    private ChangeListener allShowListener;
    private ChangeListener backboneShowListener;
    private ChangeListener smallBackboneShowListener;

    public ShowPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        addListener();
        setListener();
    }

    private void addListener() {

        addAllShowHandler();
        addBackboneShowHandler();
        addSmallBackboneShowHandler();
        addHideBinding();

    }

    private void setListener() {
        this.mainPresenter.getConnector().getAllShow().selectedProperty().addListener(allShowListener);
        this.mainPresenter.getConnector().getBackboneShow().selectedProperty().addListener(backboneShowListener);
        this.mainPresenter.getConnector().getSmallBackboneShow().selectedProperty().addListener(smallBackboneShowListener);

    }

    private void addAllShowHandler() {
        allShowListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                for (ANode n : mainPresenter.getaGraph().getNodes()) {
                    setVisibility(n, true);
                }
            }
        };
    }

    private void addBackboneShowHandler() {
        backboneShowListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                for (ANode n : mainPresenter.getaGraph().getNodes()) {
                    if (!largeBackbone.contains(n.getAtomSymb())) {
                        setVisibility(n, false);

                    } else {
                        setVisibility(n, true);
                    }
                }
            }
        };
    }

    private void addSmallBackboneShowHandler() {
        smallBackboneShowListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                for (ANode n : mainPresenter.getaGraph().getNodes()) {
                    if (!smallBackbone.contains(n.getAtomSymb())) {
                        setVisibility(n, false);

                    } else {
                        setVisibility(n, true);
                    }
                }
            }
        };
    }

    private void setVisibility(ANode n, boolean isVisible) {
        try {
            mainPresenter.getaGraphView().getNodeByID(n.getID()).setVisible(isVisible);
            for (AEdge edge : n.getIncomingEdges()) {
                mainPresenter.getaGraphView().getEdgeByIDs(edge.getSource().getID(), edge.getTarget().getID()).getLine().setVisible(isVisible);
            }
            for (AEdge edge : n.getOutgoingEdges()) {
                mainPresenter.getaGraphView().getEdgeByIDs(edge.getSource().getID(), edge.getTarget().getID()).getLine().setVisible(isVisible);
            }
        } catch (MyException e) {
            System.err.println(e.getMessage());
        }
    }

    private void addHideBinding() {
        this.mainPresenter.getaGraphView().getNodeViewGroup().visibleProperty().bind(this.mainPresenter.getConnector().getHide().selectedProperty().not());
        this.mainPresenter.getaGraphView().getEdgeViewGroup().visibleProperty().bind(this.mainPresenter.getConnector().getHide().selectedProperty().not());
    }
}
