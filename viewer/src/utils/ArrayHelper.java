package utils;

import java.util.ArrayList;

public class ArrayHelper {

    public static float[] toPrimitiveFloatArray(ArrayList<Float> list){
        float[] primitiveList = new float[list.size()];

        for(int i = 0; i < list.size(); i++){
            primitiveList[i] = list.get(i);
        }

        return primitiveList;

    }
}
