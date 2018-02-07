package view.graph;

import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;

public class ACartoonView extends Group{

    private final MeshView meshView;
    private final PhongMaterial material;
    private final Color atomColor;
    private final Color secStrucColor;
    private Color chainColor;
    private final String chainID;

    public ACartoonView( TriangleMesh triangleMesh, Color atomColor, Color secStrucColor, String chainID) {
        this.material = new PhongMaterial();
        this.meshView = new MeshView(triangleMesh);
        this.atomColor = atomColor;
        this.secStrucColor = secStrucColor;
        this.chainID = chainID;

        this.getChildren().add(meshView);
    }

    private void setColor(Color color){
        material.setDiffuseMap(null);
        material.setDiffuseColor(color);
        meshView.setMaterial(material);
    }

    public String getChainID() {
        return chainID;
    }

    public void specifyChainColor(Color chainColor){
        this.chainColor = chainColor;
    }

    public void setAtomColor(){
        setColor(atomColor);
    }

    public void setAminoAcidColor(){
        material.setDiffuseColor(Color.WHITE);
        material.setDiffuseMap(new Image("file:PDBViewer/resources/amino_acid_colors.png"));
        meshView.setMaterial(material);
    }

    public void setSecondaryStructurColor() {
        setColor(secStrucColor);
    }

    public void setChainColor(){
        setColor(chainColor);
    }


}
