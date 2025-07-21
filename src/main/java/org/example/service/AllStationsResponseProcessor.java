package org.example.service;

import org.example.DTO.SheduleRequest;
import org.example.DTO.SheduleResponce;
import org.json.JSONArray;
import org.json.JSONObject;

import org.example.DTO.AllStationsResponse.*;


import java.util.*;


//Вспомогательный метод: преобразует JSONObject с парами ключ-значение в Map<String, String>

public class AllStationsResponseProcessor {
    public static HashMap<String, HashMap<Settlement, List<Station>>> resultMap;

    public static void extractRussiaStations(JSONObject jsonObject) {
        resultMap = new HashMap<>();

        JSONArray countries = jsonObject.getJSONArray("countries");

        for (int i = 0; i < countries.length(); i++) {
            JSONObject country = countries.getJSONObject(i);
            String countryTitle = country.optString("title", "");
            if (!countryTitle.equalsIgnoreCase("Россия")) continue;

            JSONArray regions = country.optJSONArray("regions");
            if (regions == null) continue;

            HashMap<Settlement, List<Station>> regionMap = new HashMap<>();

            for (int j = 0; j < regions.length(); j++) {
                JSONObject region = regions.getJSONObject(j);
                JSONArray settlements = region.optJSONArray("settlements");

                if (settlements == null) continue;

                for (int k = 0; k < settlements.length(); k++) {
                    JSONObject settlementJson = settlements.getJSONObject(k);
                    String settlementTitle = settlementJson.optString("title", "");
                    JSONObject settlementCodes = settlementJson.optJSONObject("codes");
                    String settlementYandexCode = settlementCodes != null ? settlementCodes.optString("yandex_code", "") : "";

                    Settlement settlement = new Settlement(settlementTitle, settlementYandexCode);
                    List<Station> stationList = new ArrayList<>();

                    JSONArray stations = settlementJson.optJSONArray("stations");
                    if (stations == null) continue;

                    for (int l = 0; l < stations.length(); l++) {
                        JSONObject stationJson = stations.getJSONObject(l);

                        String direction = stationJson.optString("direction", "");
                        JSONObject stationCodes = stationJson.optJSONObject("codes");
                        String yandexCode = stationCodes != null ? stationCodes.optString("yandex_code", "") : "";
                        String stationType = stationJson.optString("station_type", "");
                        String stationTitle = stationJson.optString("title", "");
                        double longitude = stationJson.optDouble("longitude", 0);
                        double latitude = stationJson.optDouble("latitude", 0);
                        String transportType = stationJson.optString("transport_type", "");

                        Station station = new Station(
                                stationTitle,
                                yandexCode,
                                stationType,
                                direction,
                                transportType,
                                latitude,
                                longitude
                        );
                        stationList.add(station);
                    }

                    regionMap.put(settlement, stationList);
                }
            }

            resultMap.put(countryTitle, regionMap);
            break; // Only process "Россия", exit loop
        }
    }
}