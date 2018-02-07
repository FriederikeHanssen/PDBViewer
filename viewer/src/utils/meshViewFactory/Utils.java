package utils.meshViewFactory;

import javafx.geometry.Point3D;
import javafx.scene.transform.Rotate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Denis Heid, Friederike Hanssen, Jan. 2018
 */

@SuppressWarnings("unchecked")
final class Utils {

    public static Point3D computeProjectedPoint(List<Point3D> cAlphaPoints, List<Point3D> cPoints, List<Point3D> oPoints,
                                                 List<Point3D> nPoints,Point3D normal,int startIndex, int endIndex, int helixSegment){
        //noinspection unchecked
        final Point3D meanInTubePoint = Utils.calculateMeanForLists(cAlphaPoints.subList(startIndex,endIndex),
                cPoints.subList(startIndex,endIndex),
                oPoints.subList(startIndex,endIndex),
                nPoints.subList(startIndex,endIndex));
        Point3D aVector = cAlphaPoints.get(helixSegment).subtract(meanInTubePoint);

        final double aOneMagnitude = aVector.dotProduct(normal);
        return meanInTubePoint.add(normal.multiply(aOneMagnitude));
    }

    public static List<Point3D> calculateSegmentPoints(double deltaAnglePerSegment, double subSegmentCount,
                                                        Point3D projectedPoint,Point3D nextProjectedPoint, Point3D cAlphaPoint,
                                                        Point3D nextCAlphaPoint, Point3D rotationAxis){
        final double degreesPerSegment = deltaAnglePerSegment / subSegmentCount;
        final List<Point3D> segmentPoints = new ArrayList<>();
        final Point3D segmentVector = rotationAxis.multiply(projectedPoint.subtract(nextProjectedPoint).magnitude()/(subSegmentCount-1));

        double alphaOffset = 0;
        double alphaSegment = 1;
        final double alphaStep = 1.0/(subSegmentCount);

        for(int i = 0; i < subSegmentCount-1; ++i,alphaOffset+=alphaStep, alphaSegment-=alphaStep){
            final Rotate rotate = new Rotate();
            double nextAngle = i*degreesPerSegment;
            nextAngle = nextAngle >= 360? nextAngle%360 : nextAngle;
            rotate.setAngle(nextAngle);
            rotate.setAxis(rotationAxis);
            rotate.setPivotX(projectedPoint.getX());
            rotate.setPivotY(projectedPoint.getY());
            rotate.setPivotZ(projectedPoint.getZ());
            Point3D pointOnCircle = rotate.transform(cAlphaPoint.getX(),cAlphaPoint.getY(),cAlphaPoint.getZ());

            pointOnCircle = pointOnCircle.add(segmentVector.multiply(i));
            Point3D correctionVector = nextCAlphaPoint.subtract(pointOnCircle);

            pointOnCircle = pointOnCircle.add(correctionVector.multiply(alphaOffset));
            segmentPoints.add(pointOnCircle);
        }

        return segmentPoints;
    }

    public static Point3D calculateTubeNormal(List<Point3D> oVectors, List<Point3D> cVectors){
        final List<Point3D> tmp = new ArrayList<>();
        for(int i = 0; i < oVectors.size(); ++i){
            tmp.add(oVectors.get(i).subtract(cVectors.get(i)));
        }
        return calculateMeanForLists(tmp).normalize();
    }

    private static Point3D calculateMeanForLists(List<Point3D>... lists){
        Point3D mean = new Point3D(0.0,0.0,0.0);
        for(List<Point3D> l : lists){
            for(Point3D p : l){
                mean = mean.add(p);
            }
        }
        return mean.
                multiply( 1.0 / (Arrays.
                        stream(lists).
                        mapToInt(List::size).
                        sum()));
    }

    public static List<Point3D> smoothPoints(List<Point3D> temp){
        if(temp.size() <= 0)
            return null;
        return convolve1D(temp,2);
    }

    private static List<Point3D> convolve1D(List<Point3D> list , @SuppressWarnings("SameParameterValue") int windowSize){
        double sum = 2*windowSize;
        List<Point3D> smoothedPoints = new ArrayList<>();
        for(int i = 0; i < list.size(); ++i){
            Point3D smoothedPoint = new Point3D(0.0,0.0,0.0);
            if(i-windowSize > 0 && i + windowSize < list.size())
                for(int j = -windowSize; j< windowSize; j++){
                    smoothedPoint = smoothedPoint.add(list.get(j + i));
                }
            else
                smoothedPoint = list.get(i).multiply(sum);
            smoothedPoints.add(smoothedPoint.multiply(1.0/sum));
        }
        return smoothedPoints;
    }


}
