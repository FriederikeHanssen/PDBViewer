package presenter;

import exception.MyException;
import javafx.beans.value.ChangeListener;
import javafx.scene.paint.Color;
import model.graph.AEdge;
import model.graph.ANode;
import utils.Colors;
import view.graph.*;


import java.util.*;

/**
 * @author Friederike Hanssen, 01.2018
 */

@SuppressWarnings("unchecked")
class ColorPresenter {

    private final MainPresenter mainPresenter;

    private ChangeListener atomColorListener;
    private ChangeListener aminoAcidColorListener;
    private ChangeListener secondaryStructureColorListener;
    private ChangeListener chainColorListener;
    private ChangeListener graphReadInListener;

    private List<Colors> chainColors = new ArrayList<>();
    private final Map<String, Integer> chainIDMap = new HashMap<>();
    private int numChains = 0;


    public ColorPresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        addListener();
        setListener();
    }

    private void addListener() {
        addGraphReadInListener();
        addAtomColorHandler();
        addAminoAcidColorHandler();
        addSecondaryStructureColorHandler();
        addChainColorHandler();
    }

    @SuppressWarnings("unchecked")
    private void setListener() {
        this.mainPresenter.getConnector().getAtomCol().selectedProperty().addListener(atomColorListener);
        this.mainPresenter.getConnector().getAminoAcidCol().selectedProperty().addListener(aminoAcidColorListener);
        this.mainPresenter.getConnector().getSecStrcCol().selectedProperty().addListener(secondaryStructureColorListener);
        this.mainPresenter.getConnector().getChainColor().selectedProperty().addListener(chainColorListener);
        this.mainPresenter.getIsgraphReadIn().addListener(graphReadInListener);
    }

    private void addAtomColorHandler() {

        atomColorListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getaGraphView().getNodeViews().forEach(node -> ((ANodeView) node).setAtomColor());
                mainPresenter.getaGraphView().getEdgeViews().forEach(edge -> ((AEdgeView) edge).setAtomColor());
                mainPresenter.getaGraphView().getRibbonViews().forEach(meshView -> ((ARibbonView) meshView).setAtomColor());
                mainPresenter.getaGraphView().getCartoonViews().forEach(cartoonView -> ((ACartoonView) cartoonView).setAtomColor());
            }
        };
    }

    private void addAminoAcidColorHandler() {
        aminoAcidColorListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getaGraphView().getNodeViews().forEach(node -> ((ANodeView) node).setAminoAcidColor());
                mainPresenter.getaGraphView().getEdgeViews().forEach(edge -> ((AEdgeView) edge).setAminoAcidColor());
                mainPresenter.getaGraphView().getRibbonViews().forEach(ribbonView -> ((ARibbonView) ribbonView).setAminoAcidColor());
                mainPresenter.getaGraphView().getCartoonViews().forEach(cartoonView -> ((ACartoonView) cartoonView).setAminoAcidColor());
            }
        };
    }

    private void addSecondaryStructureColorHandler() {
        secondaryStructureColorListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getaGraphView().getNodeViews().forEach(node -> ((ANodeView) node).setSecStrucColor());
                mainPresenter.getaGraphView().getEdgeViews().forEach(edge -> ((AEdgeView) edge).setSecStrcColor());
                mainPresenter.getaGraphView().getRibbonViews().forEach(ribbonView -> ((ARibbonView) ribbonView).setSecStrucColor());
                mainPresenter.getaGraphView().getCartoonViews().forEach(cartoonView -> ((ACartoonView) cartoonView).setSecondaryStructurColor());

            }
        };
    }

    private void addChainColorHandler() {
        chainColorListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                this.mainPresenter.getaGraphView().getNodeViews().forEach(nodeView -> ((ANodeView) nodeView).setChainColor());
                this.mainPresenter.getaGraphView().getEdgeViews().forEach(edgeView -> ((AEdgeView) edgeView).setChainColor());
                this.mainPresenter.getaGraphView().getRibbonViews().forEach(ribbonView -> ((ARibbonView) ribbonView).setChainColor());
                this.mainPresenter.getaGraphView().getCartoonViews().forEach(cartoonView -> ((ACartoonView) cartoonView).setChainColor());

            }
        };
    }

    private void precomputeChainColors() {
        Set<String> chainIDs = new HashSet<>();
        for (ANode n : mainPresenter.getaGraph().getNodes()) {
            chainIDs.add(n.getChainID());
        }
        numChains = chainIDs.size();

        List<String> chainIDList = new ArrayList<>(chainIDs);
        for (int i = 0; i < chainIDs.size(); i++) {
            chainIDMap.put(chainIDList.get(i), i);
        }

        chainColors = new ArrayList<>(Arrays.asList(Colors.values()));
    }

    private void setChainColors() {
        for (ANode n : mainPresenter.getaGraph().getNodes()) {
            try {
                mainPresenter.getaGraphView().getNodeByID(n.getID()).specifyChainColor(chainColors.get(chainIDMap.get(n.getChainID()) % numChains).getColor());
                for (AEdge e : n.getIncomingEdges()) {
                    mainPresenter.getaGraphView().getEdgeByIDs(e.getSource().getID(), e.getTarget().getID()).specifyChainColor(chainColors.get(chainIDMap.get(n.getChainID()) % numChains).getColor());
                }
            } catch (MyException esp) {

            }
        }
        for (Integer id : mainPresenter.getaGraph().getSuperNodes().keySet()) {
            try {
                Color c = chainColors.get(chainIDMap.get(mainPresenter.getaGraph().getSuperNodes().get(id).getNodeCollection().get(0).getChainID()) % numChains).getColor();
                mainPresenter.getaGraphView().getRibbonViewByID(id).specifyChainColor(c);
            } catch (MyException esp) {

            }
        }
        mainPresenter.getaGraphView().getCartoonViews().forEach(cartoonView ->
                ((ACartoonView) cartoonView).specifyChainColor(chainColors.get(chainIDMap.get(((ACartoonView) cartoonView).getChainID()) % numChains).getColor()));

    }

    private void addGraphReadInListener() {
        graphReadInListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                this.mainPresenter.getColorPresenter().precomputeChainColors();
                this.mainPresenter.getColorPresenter().setChainColors();
            }
        };
    }

}
