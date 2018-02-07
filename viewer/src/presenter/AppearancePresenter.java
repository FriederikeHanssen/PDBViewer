package presenter;

import javafx.beans.value.ChangeListener;
import view.graph.AEdgeView;
import view.graph.ANodeView;
import view.graph.MyLine3D;

/**
 * @author Friederike Hanssen, 01.2018
 */

@SuppressWarnings("unchecked")
class AppearancePresenter {

    private final MainPresenter mainPresenter;

    private ChangeListener ballStickViewListener;
    private ChangeListener surfaceViewListener;
    private ChangeListener edgeViewListener;

    public AppearancePresenter(MainPresenter mainPresenter) {
        this.mainPresenter = mainPresenter;

        addListener();
        setListener();
    }

    private void addListener() {
        addBallStickViewHandler();
        addSurfaceViewHandler();
        addEdgeViewHandler();

        addRibbonViewBinding();
        addCartoonViewBinding();
    }

    private void setListener() {
        this.mainPresenter.getConnector().getBallStickView().selectedProperty().addListener(ballStickViewListener);
        this.mainPresenter.getConnector().getSurfaceView().selectedProperty().addListener(surfaceViewListener);
        this.mainPresenter.getConnector().getEdgeView().selectedProperty().addListener(edgeViewListener);
    }

    private void addBallStickViewHandler() {

        ballStickViewListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getCommandManager().clear();

                mainPresenter.getaGraphView().getNodeViews().forEach(n -> {

                    ((ANodeView) n).scaleXProperty().setValue(1.0);
                    ((ANodeView) n).scaleYProperty().setValue(1.0);
                    ((ANodeView) n).scaleZProperty().setValue(1.0);
                });
                mainPresenter.getaGraphView().getEdgeViews().forEach(e -> ((AEdgeView) e).getLine().setRadius(MyLine3D.cylinderRadius));
            }
        };

    }

    private void addSurfaceViewHandler() {
        surfaceViewListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getCommandManager().clear();
                mainPresenter.getaGraphView().getNodeViews().forEach(n -> {
                    ((ANodeView) n).scaleXProperty().setValue(5);
                    ((ANodeView) n).scaleYProperty().setValue(5);
                    ((ANodeView) n).scaleZProperty().setValue(5);

                });

                mainPresenter.getaGraphView().getEdgeViews().forEach(e -> ((AEdgeView) e).getLine().setRadius(MyLine3D.cylinderRadius));

            }
        };
    }

    private void addEdgeViewHandler() {
        edgeViewListener = (ChangeListener<Boolean>) (observable, oldValue, newValue) -> {
            if (newValue) {
                mainPresenter.getCommandManager().clear();
                mainPresenter.getaGraphView().getNodeViews().forEach(n -> {
                    ((ANodeView) n).scaleXProperty().setValue(0.7);
                    ((ANodeView) n).scaleYProperty().setValue(0.7);
                    ((ANodeView) n).scaleZProperty().setValue(0.7);
                    ((ANodeView) n).getSphere().setRadius(ANodeView.radius - 3);
                });

                mainPresenter.getaGraphView().getEdgeViews().forEach(e -> ((AEdgeView) e).getLine().setRadius(ANodeView.radius));
            }
        };
    }

    private void addRibbonViewBinding() {
        mainPresenter.getaGraphView().getRibbonViewGroup().visibleProperty().bind(
                mainPresenter.getConnector().getRibbonView().selectedProperty());
        this.mainPresenter.getCommandManager().clear();
    }

    private void addCartoonViewBinding() {
        mainPresenter.getaGraphView().getCartoonViewGroup().visibleProperty().bind(
                mainPresenter.getConnector().getCartoonView().selectedProperty());
        this.mainPresenter.getCommandManager().clear();
    }

}
