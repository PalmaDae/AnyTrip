package org.example.util;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum TransportTypes {
    PLANE("PLANE"),
    BUS("BUS"),
    TRAIN("TRAIN"),
    SUBURBAN("SUBURBAN"),
    WATER("WATER"),
    HELICOPTER("HELICOPTER");

    public static final List<TransportTypes> listTransportTypes = Arrays.asList(values());

    private final String description;

    TransportTypes(String description) {
        this.description = description;
    }

    private static final Map<String, TransportTypes> russianToEnum = Map.ofEntries(
        Map.entry("АВТОБУС", BUS),
        Map.entry("ЭЛЕКТРИЧКА", SUBURBAN),
        Map.entry("ПОЕЗД", TRAIN),
        Map.entry("САМОЛЁТ", PLANE),
        Map.entry("САМОЛЕТ", PLANE),
        Map.entry("ВЕРТОЛЁТ", HELICOPTER),
        Map.entry("ВЕРТОЛЕТ", HELICOPTER)
    );

    public static TransportTypes fromRussian(String russian) {
        if (russian == null) return null;
        return russianToEnum.get(russian.toUpperCase());
    }

    @Override
    public String toString() {
        return description;
    }
}