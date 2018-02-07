package utils.meshViewFactory;

import javafx.geometry.Point3D;
import javafx.scene.shape.TriangleMesh;
import model.peptide.AminoAcidProperties;
import utils.ArrayHelper;
import utils.Triplet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Denis Heid, Friederike Hanssen, Jan. 2018
 */
final class MeshViewFactory {

    private static List<Point3D> mainPoints = new ArrayList<>();
    private static List<Point3D> subPositivePoints = new ArrayList<>();
    private static List<Point3D> subNegativePoints = new ArrayList<>();

    public static TriangleMesh computeMesh(List<AminoAcidProperties> aminoAcids, List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                           List<Point3D> nPoints, int windowSize, int subSegmentCount, int sheetLength, int smoothFactor) {
        mainPoints.clear();
        subPositivePoints.clear();
        subNegativePoints.clear();

        if (cAlphaPoints.size() != cPoints.size() || cAlphaPoints.size() != oPoints.size() || cAlphaPoints.size() != nPoints.size()) {
            System.err.println("Input is garbage.");
            return null;
        }

        final int numSegments = cAlphaPoints.size();
        double deltaAnglePerSegment;


        for (int segment = 0; segment < numSegments - 1; ++segment) {
            final int startIndex = segment < windowSize ? 0 : segment - windowSize;
            final int endIndex = segment >= (numSegments - windowSize) ? numSegments : segment + windowSize;
            final int startIndexNextSegment = segment + 1 < windowSize ? startIndex + 1 : (segment + 1) - windowSize;
            final int endIndexNextSegment = segment + 1 > (numSegments - windowSize) ? numSegments : segment + 1 + windowSize;

            final Point3D tubeVectorNormal = Utils.calculateTubeNormal(oPoints.subList(startIndex, endIndex),
                    cPoints.subList(startIndex, endIndex));
            final Point3D tubeNextVectorNormal = Utils.calculateTubeNormal(oPoints.subList(startIndexNextSegment, endIndexNextSegment),
                    cPoints.subList(startIndexNextSegment, endIndexNextSegment));

            final Point3D currentProjectedPoint = Utils.computeProjectedPoint(cAlphaPoints, cPoints, oPoints, nPoints, tubeVectorNormal, startIndex, endIndex, segment);
            final Point3D nextProjectedPoint = Utils.computeProjectedPoint(cAlphaPoints, cPoints, oPoints, nPoints, tubeNextVectorNormal, startIndexNextSegment, endIndexNextSegment, segment + 1);

            final Point3D r = cAlphaPoints.get(segment).subtract(currentProjectedPoint).normalize();
            final Point3D rPrime = cAlphaPoints.get(segment + 1).subtract(nextProjectedPoint).normalize();
            deltaAnglePerSegment = Math.acos(r.dotProduct(rPrime)) * 180.0 / Math.PI;
            mainPoints.addAll(Utils.calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint, nextProjectedPoint, cAlphaPoints.get(segment), cAlphaPoints.get(segment + 1), tubeVectorNormal));

            subPositivePoints.addAll(Utils.calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint.add(tubeVectorNormal.multiply(sheetLength)),
                    nextProjectedPoint.add(tubeNextVectorNormal.multiply(sheetLength)),
                    cAlphaPoints.get(segment).add(tubeVectorNormal.multiply(sheetLength)),
                    cAlphaPoints.get(segment + 1).add(tubeNextVectorNormal.multiply(sheetLength)),
                    tubeVectorNormal));
            subNegativePoints.addAll(Utils.calculateSegmentPoints(deltaAnglePerSegment, subSegmentCount,
                    currentProjectedPoint.add(tubeVectorNormal.multiply(-sheetLength)),
                    nextProjectedPoint.add(tubeNextVectorNormal.multiply(-sheetLength)),
                    cAlphaPoints.get(segment).add(tubeVectorNormal.multiply(-sheetLength)),
                    cAlphaPoints.get(segment + 1).add(tubeNextVectorNormal.multiply(-sheetLength)),
                    tubeVectorNormal));
        }


        for (int i = 0; i < smoothFactor; i++) {
            List<Point3D> smoothedMain = Utils.smoothPoints(mainPoints);
            List<Point3D> smoothedPositive = Utils.smoothPoints(subPositivePoints);
            List<Point3D> smoothedNegative = Utils.smoothPoints(subNegativePoints);
            if (smoothedMain != null)
                mainPoints = smoothedMain;
            if (smoothedPositive != null)
                subPositivePoints = smoothedPositive;
            if (smoothedNegative != null)
                subNegativePoints = smoothedNegative;
        }

        return generateMeshFromPoints(subSegmentCount, aminoAcids);
    }

    public static Triplet<Point3D, Point3D, Point3D> getLastComputedPoints(){
        if(mainPoints.size() == 0){
            return null;
        }
        return new Triplet<>(subPositivePoints.get(subPositivePoints.size()-1),
                            mainPoints.get(mainPoints.size()-1),
                            subNegativePoints.get(subNegativePoints.size()-1))
                ;
    }

    public static Triplet<Point3D, Point3D, Point3D> getFirstComputedPoints(){
        if(mainPoints.size() == 0){
            return null;
        }
        return new Triplet<>(subPositivePoints.get(0),
                mainPoints.get(0),
                subNegativePoints.get(0))
                ;
    }

    private static TriangleMesh generateMeshFromPoints(int subSegmentCount, List<AminoAcidProperties> aminoAcids) {
        TriangleMesh mesh = new TriangleMesh();
        ArrayList<Float> pointList = new ArrayList<>();
        ArrayList<Integer> facesList = new ArrayList<>();
        float[] texCoords = getTexCoord();
        for (int i = 0; i < mainPoints.size(); ++i) {
            addPointToPointList(pointList, mainPoints.get(i), subPositivePoints.get(i), subNegativePoints.get(i));
        }

        int counter = 0;
        int index = 0;
        AminoAcidProperties ap = aminoAcids.get(index);
        for (int i = 0; i < (pointList.size() / 3) - 5; i += 3) {

            if(counter >= subSegmentCount-1){
                counter = 0;
                index++;
                ap = aminoAcids.get(index);
            }
            //clockwise
            addPointToFaces(facesList, i, i + 4, i + 3, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 1, i + 4, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 5, i + 2, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 3, i + 5, translateTexAminoAcid(ap));

            //counter clock wise
            addPointToFaces(facesList, i, i + 3, i + 4, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 4, i + 1, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 2, i + 5, translateTexAminoAcid(ap));
            addPointToFaces(facesList, i, i + 5, i + 3, translateTexAminoAcid(ap));

            counter++;

        }
        int[] smoothing = new int[facesList.size() / 6];
        for (int i = 0; i < smoothing.length; i+=8) {
            smoothing[i] = 1;
            smoothing[i+1] = 1;
            smoothing[i+2] = 1;
            smoothing[i+3] = 1;

            smoothing[i+4] = 2;
            smoothing[i+5] = 2;
            smoothing[i+6] = 2;
            smoothing[i+7] = 2;

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

    private static void addPointToPointList(ArrayList<Float> pointList, Point3D mainHelix, Point3D secondHelix, Point3D negativeHelix) {
        pointList.add((float) mainHelix.getX());
        pointList.add((float) mainHelix.getY());
        pointList.add((float) mainHelix.getZ());

        pointList.add((float) secondHelix.getX());
        pointList.add((float) secondHelix.getY());
        pointList.add((float) secondHelix.getZ());

        pointList.add((float) negativeHelix.getX());
        pointList.add((float) negativeHelix.getY());
        pointList.add((float) negativeHelix.getZ());
    }

    private static float[] getTexCoord(){
        float[] tex = new float[120];

        float x = 0.0f;
        float y = 1.0f;

        float offset = 0.05f;
        int counter = 0;

        for(int i = 0; i <120; i+=6){


            //t1
            tex[i] = x + offset;
            tex[i + 1] = y - offset;

            x += 0.2f;
            //t2
            tex[i+2] = x - offset;
            tex[i+3] = y - offset;


            y -= 0.25f;
            //t3
            tex[i+4] = x - offset;
            tex[i+5] = y + offset;

            if(counter % 5 != 4) {
                y += 0.25f;
            }
            if(counter % 5 == 4){
                x = 0.0f;
            }

            counter++;
        }

        return tex;
    }

    private static int[] translateTexAminoAcid(AminoAcidProperties aminoAcid){
        switch (aminoAcid.getThreeLetterCode()) {
            case "ARG":
                return new int[]{48,49,50};
            case "ASN":
                return new int[]{51,52,53};
            case "ASP":
                return new int[]{54,55,56};
            case "ALA":
                return new int[]{45,46,47};
            case "CYS":
                return new int[]{57,58,59};
            case "GLN":
                return new int[]{30,31,32};
            case "GLU":
                return new int[]{33,34,35};
            case "GLY":
                return new int[]{36,37,38};
            case "HIS":
                return new int[]{39,40,41};
            case "ILE":
                return new int[]{42,43,44};
            case "LEU":
                return new int[]{15,16,17};
            case "LYS":
                return new int[]{18,19,20};
            case "MET":
                return new int[]{21,22,23};
            case "PHE":
                return new int[]{24,25,26};
            case "PRO":
                return new int[]{27,28,29};
            case "SER":
                return new int[]{0,1,2};
            case "THR":
                return new int[]{3,4,5};
            case "TRP":
                return new int[]{6,7,8};
            case "TYR":
                return new int[]{9,10,11};
            case "VAL":
                return new int[]{12,13,14};
            default:
                return new int[]{36,37,38};

        }
    }
}
