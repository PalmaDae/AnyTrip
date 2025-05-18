package org.example.util;

public enum TransportTypes {
    // константа и значение должны соответсвовать по регистру
    PLANE("PLANE"),
    BUS("BUS"),
    TRAIN("TRAIN"),
    SUBURBAN("SUBURBAN"),
    WATER("WATER"),
    HELICOPTER("HELICOPTER");

    private String description;

    TransportTypes(String description){
        this.description = description;
    }

    public String getDescription(){
        return description;
    }

}
