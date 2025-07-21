package org.example.util;

import java.util.Arrays;
import java.util.List;

public enum TransportTypes {
    // константа и значение должны соответсвовать по регистру
    PLANE("PLANE"),
    BUS("BUS"),
    TRAIN("TRAIN"),
    SUBURBAN("SUBURBAN"),
    WATER("WATER"),
    HELICOPTER("HELICOPTER");

    public static List<TransportTypes> listTransportTypes = Arrays.asList(TransportTypes.values());

    private String description;

    TransportTypes(String description){
        this.description = description;
    }

}
