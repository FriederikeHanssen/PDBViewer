package utils;

import javafx.scene.paint.Color;

/**
 * @author Friederike Hanssen, 01.2018
 */

public enum Colors {
    DARKRED(Color.DARKRED),
    DARKBLUE(Color.DARKBLUE),
    DARKGREEN(Color.DARKGREEN),
    DARKGOLDENROD(Color.DARKGOLDENROD),
    SALMON(Color.SALMON),
    LIGHTBLUE(Color.LIGHTBLUE),
    LIGHTSEAGREEN(Color.LIGHTSEAGREEN);

    private final Color c;

    Colors(Color c){
        this.c = c;
    }

    public Color getColor(){
        return this.c;
    }
}
