package org.example.service;

import org.example.DTO.AllStationsResponse.*;
import org.example.DTO.AllStationsResponse;

import java.util.*;

public class AllStationsResponseProcessor {
    public static HashMap<String, HashMap<Settlement, List<Station>>> resultMap;

    public static void extractRussiaStations(AllStationsResponse response) {
        resultMap = new HashMap<>();

        for (Country country : response.getCountries()) {
            if (!"Россия".equalsIgnoreCase(country.getTitle())) continue;

            HashMap<Settlement, List<Station>> regionMap = new HashMap<>();

            if (country.getRegions() == null) continue;

            for (Region region : country.getRegions()) {
                if (region.getSettlements() == null) continue;

                for (Settlement settlement : region.getSettlements()) {
                    List<Station> stations = settlement.getStations();
                    if (stations == null || stations.isEmpty()) continue;

                    regionMap.put(settlement, stations);
                }
            }

            resultMap.put(country.getTitle(), regionMap);
            break; // только Россия
        }
    }
}