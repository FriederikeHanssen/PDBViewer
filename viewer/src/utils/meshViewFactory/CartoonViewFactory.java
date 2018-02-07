package utils.meshViewFactory;

import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.shape.TriangleMesh;
import javafx.util.Pair;
import model.graph.ANode;
import model.graph.ASuperNode;
import model.peptide.AminoAcidProperties;
import model.peptide.SecondaryStructureProperties;
import presenter.GraphPresenter;
import utils.ArrayHelper;
import utils.Triplet;

import java.util.*;

/**
 * @author Friederike Hanssen, 01.2018
 */

public final class CartoonViewFactory {

    private static final Map<Integer, List<Point3D>> cAlpha = new HashMap<>();
    private static final Map<Integer, List<Point3D>> cPoints = new HashMap<>();
    private static final Map<Integer, List<Point3D>> nPoints = new HashMap<>();
    private static final Map<Integer, List<Point3D>> oPoints = new HashMap<>();
    private static final Map<Integer, List<AminoAcidProperties>> aminoAcids = new HashMap<>();

    private static final List<Point3D> cAlphaTemp = new ArrayList<>();
    private static final List<Point3D> cTemp = new ArrayList<>();
    private static final List<Point3D> nTemp = new ArrayList<>();
    private static final List<Point3D> oTemp = new ArrayList<>();
    private static final List<AminoAcidProperties> aminoAcidTemp = new ArrayList<>();

    private static final List<List<Pair<Triplet<Point3D, Point3D, Point3D>, String>>> betweenPoints = new ArrayList<>();

    public static void computeCartoonView(ObservableMap<Integer, ASuperNode> superNodes, GraphPresenter graphPresenter) {
        for (Triplet triplet : addCartoonView(superNodes)) {
            graphPresenter.getMainPresenter().getaGraphView().addCartoonView( (TriangleMesh) triplet.getFirst(), Color.GOLDENROD, ((SecondaryStructureProperties) triplet.getSecond()).getColor(), (String) triplet.getThird());
        }

        for (Triplet triplet : addBetweenPieces()) {
            graphPresenter.getMainPresenter().getaGraphView().addCartoonView((TriangleMesh) triplet.getFirst(), Color.GOLDENROD, ((SecondaryStructureProperties) triplet.getSecond()).getColor(), (String) triplet.getThird());
        }
    }

    private static void reset() {
        cAlpha.clear();
        cPoints.clear();
        oPoints.clear();
        nPoints.clear();
        cAlphaTemp.clear();
        cTemp.clear();
        oTemp.clear();
        nTemp.clear();
        betweenPoints.clear();
    }

    private static void setTempValues(ObservableList<ANode> nodes) {
        aminoAcidTemp.add(nodes.get(0).getAminoAcid());
        for (ANode n : nodes) {
            switch (n.getAtomSymb()) {
                case "CA":
                    cAlphaTemp.add(n.getCoordinates().multiply(GraphPresenter.coordScale));
                    break;
                case "C":
                    cTemp.add(n.getCoordinates().multiply(GraphPresenter.coordScale));
                    break;
                case "N":
                    nTemp.add(n.getCoordinates().multiply(GraphPresenter.coordScale));
                    break;
                case "O":
                    oTemp.add(n.getCoordinates().multiply(GraphPresenter.coordScale));
                    break;
            }
        }
    }

    private static void setValues(int counter) {
        cAlpha.put(counter, new ArrayList<>(cAlphaTemp));
        cPoints.put(counter, new ArrayList<>(cTemp));
        nPoints.put(counter, new ArrayList<>(nTemp));
        oPoints.put(counter, new ArrayList<>(oTemp));
        aminoAcids.put(counter, new ArrayList<>(aminoAcidTemp));
        cAlphaTemp.clear();
        cTemp.clear();
        nTemp.clear();
        oTemp.clear();
        aminoAcidTemp.clear();
    }

    private static List<Triplet<TriangleMesh, SecondaryStructureProperties, String>> addCartoonView(ObservableMap<Integer, ASuperNode> superNodes) {
        reset();
        List<Triplet<TriangleMesh, SecondaryStructureProperties, String>> meshes = new ArrayList<>();
        List<Pair<Triplet<Point3D, Point3D, Point3D>, String>> tempBetweenPoints = new ArrayList<>();
        int counter = 0;

        List<Integer> indices = new ArrayList<>(superNodes.keySet());
        Collections.sort(indices);

        for (Integer superNodeID : indices) {

            setTempValues(superNodes.get(superNodeID).getNodeCollection());

            //We are not at the end, but a new structure type or new chain starts in the next iteration: add current and store
            if (superNodeID < superNodes.keySet().size() - 1 &&
                    (superNodes.get(superNodeID).getSecStruc() != superNodes.get(superNodeID + 1).getSecStruc()
                            || !superNodes.get(superNodeID).getNodeCollection().get(0).getChainID().equals(superNodes.get(superNodeID + 1).getNodeCollection().get(0).getChainID()))) {

                //if not a new chain than add next value to get a smoother transition between structures
                if (superNodes.get(superNodeID).getNodeCollection().get(0).getChainID().equals(superNodes.get(superNodeID + 1).getNodeCollection().get(0).getChainID())) {
                    setTempValues(superNodes.get(superNodeID + 1).getNodeCollection());
                }
                setValues(counter);
                counter++;

                TriangleMesh mesh = getMesh(superNodes.get(superNodeID).getSecStruc(), cAlpha.size() - 1);
                if(mesh != null){
                if (tempBetweenPoints.size() > 0 && superNodes.get(superNodeID).getNodeCollection().get(0).getChainID().equals(superNodes.get(superNodeID - 1).getNodeCollection().get(0).getChainID())) {
                    tempBetweenPoints.add(new Pair<>(MeshViewFactory.getFirstComputedPoints(), superNodes.get(superNodeID).getNodeCollection().get(0).getChainID()));
                }
                if (superNodes.get(superNodeID).getNodeCollection().get(0).getChainID().equals(superNodes.get(superNodeID + 1).getNodeCollection().get(0).getChainID())) {
                    tempBetweenPoints.add(new Pair<>(MeshViewFactory.getLastComputedPoints(), superNodes.get(superNodeID).getNodeCollection().get(0).getChainID()));
                }else {
                    betweenPoints.add(new ArrayList<>(tempBetweenPoints));
                    tempBetweenPoints.clear();
                }}


                meshes.add(new Triplet<>(mesh,
                        superNodes.get(superNodeID).getSecStruc(),
                        superNodes.get(superNodeID).getNodeCollection().get(0).getChainID()));
            }

        }
        setTempValues(superNodes.get(superNodes.keySet().size() - 1).getNodeCollection());
        setValues(counter);
        TriangleMesh mesh = getMesh(superNodes.get(indices.get(indices.size() - 1)).getSecStruc(), cAlpha.size() - 1);
        if(tempBetweenPoints.size() > 0) {
            tempBetweenPoints.add(new Pair<>(MeshViewFactory.getFirstComputedPoints(), superNodes.get(indices.get(indices.size() - 1)).getNodeCollection().get(0).getChainID()));
            betweenPoints.add(tempBetweenPoints);
        }
        meshes.add(new Triplet<>(mesh,
                superNodes.get(indices.get(indices.size() - 1)).getSecStruc(),
                superNodes.get(indices.get(indices.size() - 1)).getNodeCollection().get(0).getChainID()));
        return meshes;
    }

    private static TriangleMesh getMesh(SecondaryStructureProperties struc, int i) {
        if (struc == SecondaryStructureProperties.HELIX) {
            if (cAlpha.get(i).size() == 2) {
                return MeshViewFactory.computeMesh(aminoAcids.get(i), cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        1, 20, 80, 0);

            } else {
                return MeshViewFactory.computeMesh(aminoAcids.get(i), cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        4, 20, 80, 10);

            }
        }

        if (struc == SecondaryStructureProperties.SHEET) {
            if (cAlpha.get(i).size() == 2) {
                return MeshViewFactory.computeMesh(aminoAcids.get(i),cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        1, 20, 80, 0);
            } else {
                return MeshViewFactory.computeMesh(aminoAcids.get(i),cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        2, 20, 80, 5);
            }
        }

        if (struc == SecondaryStructureProperties.LOOP) {
            if (cAlpha.get(i).size() == 2) {
                return MeshViewFactory.computeMesh(aminoAcids.get(i),cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        1, 20, 20, 0);
            } else {
                return MeshViewFactory.computeMesh(aminoAcids.get(i),cAlpha.get(i), cPoints.get(i), oPoints.get(i), nPoints.get(i),
                        2, 20, 20, 4);
            }
        }
        return null;
    }

    private static List<Triplet<TriangleMesh, SecondaryStructureProperties, String>> addBetweenPieces() {
        List<Triplet<TriangleMesh, SecondaryStructureProperties, String>> meshes = new ArrayList<>();

        for(List<Pair<Triplet<Point3D, Point3D, Point3D>, String>> l :betweenPoints) {
            for (int i = 0; i < l.size() - 1; i += 2) {
                meshes.add(new Triplet<>(getBetweenMesh(l.get(i).getKey(), l.get(i + 1).getKey()),
                        SecondaryStructureProperties.LOOP,
                        l.get(i).getValue()));
            }
        }

        return meshes;
    }

    private static TriangleMesh getBetweenMesh(Triplet<Point3D, Point3D, Point3D> source, Triplet<Point3D, Point3D, Point3D> target) {
        TriangleMesh mesh = new TriangleMesh();
        ArrayList<Float> pointList = new ArrayList<>();
        ArrayList<Integer> facesList = new ArrayList<>();
        float[] texCoords = new float[]{0, 0};

        addPointToPointList(pointList, source);
        addPointToPointList(pointList, target);


        int[] facetex = new int[]{0, 0, 0};
        for (int i = 0; i < pointList.size() / 3 - 3; i += 3) {
            //clockwise
            addPointToFaces(facesList, i, i + 4, i + 3, facetex);
            addPointToFaces(facesList, i, i + 1, i + 4, facetex);
            addPointToFaces(facesList, i, i + 5, i + 2, facetex);
            addPointToFaces(facesList, i, i + 3, i + 5, facetex);

            //counter clock wise
            addPointToFaces(facesList, i, i + 3, i + 4, facetex);
            addPointToFaces(facesList, i, i + 4, i + 1, facetex);
            addPointToFaces(facesList, i, i + 2, i + 5, facetex);
            addPointToFaces(facesList, i, i + 5, i + 3, facetex);

        }
        int[] smoothing = new int[facesList.size() / 6];
        for (int i = 0; i < smoothing.length / 2; i++) {
            smoothing[i] = 1;
        }
        for (int i = smoothing.length / 2; i < smoothing.length; i++) {
            smoothing[i] = 2;
        }


        mesh.getFaces().addAll(facesList.stream().mapToInt(i -> i).toArray());
        mesh.getPoints().addAll(ArrayHelper.toPrimitiveFloatArray(pointList));
        mesh.getTexCoords().addAll(texCoords);
        mesh.getFaceSmoothingGroups().addAll(smoothing);
        return mesh;
    }

    private static void addPointToFaces(ArrayList<Integer> facesList, int first, int second, int third, int[] texCoords) {
        facesList.add(first);
        facesList.add(texCoords[0]);
        facesList.add(second);
        facesList.add(texCoords[1]);
        facesList.add(third);
        facesList.add(texCoords[2]);
    }

    private static void addPointToPointList(ArrayList<Float> pointList, Triplet<Point3D, Point3D, Point3D> source) {

        if(source != null){
        pointList.add((float) source.getSecond().getX());
        pointList.add((float) source.getSecond().getY());
        pointList.add((float) source.getSecond().getZ());

        pointList.add((float) source.getFirst().getX());
        pointList.add((float) source.getFirst().getY());
        pointList.add((float) source.getFirst().getZ());

        pointList.add((float) source.getThird().getX());
        pointList.add((float) source.getThird().getY());
        pointList.add((float) source.getThird().getZ());
        }
    }


}
