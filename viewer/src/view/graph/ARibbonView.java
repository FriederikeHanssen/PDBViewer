package view.graph;

import javafx.geometry.Point3D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ARibbonView extends Group{

    private  TriangleMesh mesh;
    private MeshView meshView;
    private  int id;
    private PhongMaterial material;
    private  Color aminoAcidColor;
    private  Color atomColor;
    private  Color secStrucColor;
    private Color chainColor;

    ARibbonView(){

    }

    ARibbonView(int id, Map<String, Point3D> curr, Map<String, Point3D> next, Color atomColor, Color aminoAcidColor, Color secStrucColor
    ) {

        this.id = id;
        this.mesh = new TriangleMesh();
        this.material = new PhongMaterial();
        this.meshView = new MeshView();

        this.aminoAcidColor = aminoAcidColor;
        this.atomColor = atomColor;
        this.secStrucColor = secStrucColor;

        this.getChildren().add(meshView);

        setPoints(curr, next);

    }

    public void setPoints(Map<String, Point3D> curr, Map<String, Point3D> next){
        float[] points = getPoints(curr, next);
        mesh.getPoints().setAll(points);
        mesh.getTexCoords().setAll(0, 0);
        int[] faces = getFaces(points);
        mesh.getFaces().setAll(faces);
        mesh.getFaceSmoothingGroups().setAll(getFaceSmoothingGroups(faces));
        this.meshView.setMesh(mesh);

    }

    private static float[] getPoints(Map<String, Point3D> curr, Map<String, Point3D> next) {
        List<Float> points = new ArrayList<>();

        Point3D cb1coord = curr.get("CB");
        Point3D ca1coord = curr.get("CA");
        Point3D cb2coord = next.get("CB");
        Point3D ca2coord = next.get("CA");

        if (cb1coord != null && ca1coord != null && cb2coord != null && ca2coord != null) {

            Point3D op1coord = (ca1coord.subtract(cb1coord)).add(ca1coord);
            points.add((float) op1coord.getX() * 60);
            points.add((float) op1coord.getY() * 60);
            points.add((float) op1coord.getZ() * 60);

            points.add((float) ca1coord.getX() * 60);
            points.add((float) ca1coord.getY() * 60);
            points.add((float) ca1coord.getZ() * 60);

            points.add((float) cb1coord.getX() * 60);
            points.add((float) cb1coord.getY() * 60);
            points.add((float) cb1coord.getZ() * 60);


            points.add((float) cb2coord.getX() * 60);
            points.add((float) cb2coord.getY() * 60);
            points.add((float) cb2coord.getZ() * 60);

            points.add((float) ca2coord.getX() * 60);
            points.add((float) ca2coord.getY() * 60);
            points.add((float) ca2coord.getZ() * 60);

            Point3D op2coord = (ca2coord.subtract(cb2coord)).add(ca2coord);
            points.add((float) op2coord.getX() * 60);
            points.add((float) op2coord.getY() * 60);
            points.add((float) op2coord.getZ() * 60);
        }


        float[] floatArray = new float[points.size()];
        int i = 0;
        for (Float f : points) {
            floatArray[i++] = (f != null ? f : Float.NaN); // Or whatever default you want.
        }
        return floatArray;
    }

    private static int[] getFaces(float[] points) {

        List<Integer> face = new ArrayList<>();

        for (int i = 0; i < points.length / 3 - 3; i += 3) {

            face.add(i);
            face.add(0);
            face.add(i + 1);
            face.add(0);
            face.add(i + 4);
            face.add(0);

            face.add(i);
            face.add(0);
            face.add(i + 4);
            face.add(0);
            face.add(i + 3);
            face.add(0);

            face.add(i + 1);
            face.add(0);
            face.add(i + 2);
            face.add(0);
            face.add(i + 5);
            face.add(0);

            face.add(i +1);
            face.add(0);
            face.add(i + 5);
            face.add(0);
            face.add(i + 4);
            face.add(0);

            face.add(i);
            face.add(0);
            face.add(i + 4);
            face.add(0);
            face.add(i + 1);
            face.add(0);

            face.add(i);
            face.add(0);
            face.add(i + 3);
            face.add(0);
            face.add(i + 4);
            face.add(0);

            face.add(i + 1);
            face.add(0);
            face.add(i + 5);
            face.add(0);
            face.add(i + 2);
            face.add(0);

            face.add(i + 1);
            face.add(0);
            face.add(i + 4);
            face.add(0);
            face.add(i + 5);
            face.add(0);
        }

        return face.stream().mapToInt(i -> i).toArray();
    }

    private static int[] getFaceSmoothingGroups(int[] faces) {
        int[] smoothingGroups = new int[faces.length / 6];

        for (int i = 0; i < smoothingGroups.length/2; i++) {
            smoothingGroups[i] = 1;
        }
        for (int i = smoothingGroups.length/2; i < smoothingGroups.length; i++) {
            smoothingGroups[i] = 2;
        }
        return smoothingGroups;
    }

    private void setColor(Color color){
        material.setDiffuseColor(color);
        meshView.setMaterial(material);
    }

    public int getID() {
        return id;
    }

    public void specifyChainColor(Color chainColor){
        this.chainColor = chainColor;
    }

    public void setAtomColor(){
        setColor(atomColor);
    }

    public void setAminoAcidColor(){
        setColor(aminoAcidColor);
    }

    public void setSecStrucColor(){
        setColor(secStrucColor);
    }

    public void setChainColor(){
        setColor(chainColor);
    }
}
