package org.example.service;

import org.example.DTO.AllStationsResponse;
import org.example.DTO.AllStationsResponse.Station;
import org.example.DTO.ScheduleItem;
import org.example.DTO.SheduleRequest;
import org.example.DTO.SheduleResponce;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class AllStationsResponseProcessor {
    public static Map<String, Object> resultMap;

    public static void extractRussiaStations(JSONObject jsonObject) {
        resultMap = new HashMap<>();
        List<Station> stationList = new ArrayList<>();

        JSONArray countriesArray = jsonObject.optJSONArray("countries");
        if (countriesArray == null) {
            resultMap.put("stations", stationList);
            return;
        }

        for (int i = 0; i < countriesArray.length(); i++) {
            JSONObject countryObj = countriesArray.getJSONObject(i);
            String countryTitle = countryObj.optString("title", "");

            if (!"Россия".equalsIgnoreCase(countryTitle)) continue;

            resultMap.put("country", countryTitle);
            List<Map<String, Object>> regionsData = new ArrayList<>();

            JSONArray regionsArray = countryObj.optJSONArray("regions");
            if (regionsArray == null) break;

            for (int j = 0; j < regionsArray.length(); j++) {
                JSONObject regionObj = regionsArray.getJSONObject(j);
                Map<String, Object> regionMap = new HashMap<>();
                regionMap.put("title", regionObj.optString("title", ""));
                List<Map<String, Object>> settlementsData = new ArrayList<>();

                JSONArray settlementsArray = regionObj.optJSONArray("settlements");
                if (settlementsArray == null) continue;

                for (int k = 0; k < settlementsArray.length(); k++) {
                    JSONObject settlementObj = settlementsArray.getJSONObject(k);
                    Map<String, Object> settlementMap = new HashMap<>();
                    settlementMap.put("title", settlementObj.optString("title", ""));
                    List<Map<String, Object>> stationsData = new ArrayList<>();

                    JSONArray stationsArray = settlementObj.optJSONArray("stations");
                    if (stationsArray == null) continue;

                    for (int m = 0; m < stationsArray.length(); m++) {
                        JSONObject stationObj = stationsArray.getJSONObject(m);

                        Station station = new Station();
                        station.setDirection(stationObj.optString("direction", ""));
                        station.setCodes(jsonObjectToMap(stationObj.optJSONObject("codes")));
                        station.setStation_type(stationObj.optString("station_type", ""));
                        station.setTitle(stationObj.optString("title", ""));
                        station.setLongitude(stationObj.optDouble("longitude", 0));
                        station.setTransport_type(stationObj.optString("transport_type", ""));
                        station.setLatitude(stationObj.optDouble("latitude", 0));
                        stationList.add(station);

                        // Добавим в карту для сериализации
                        Map<String, Object> stationMap = new HashMap<>();
                        stationMap.put("title", station.getTitle());
                        stationMap.put("station_type", station.getStation_type());
                        stationMap.put("direction", station.getDirection());
                        stationMap.put("transport_type", station.getTransport_type());
                        stationMap.put("longitude", station.getLongitude());
                        stationMap.put("latitude", station.getLatitude());
                        stationMap.put("codes", station.getCodes());

                        stationsData.add(stationMap);
                    }

                    settlementMap.put("stations", stationsData);
                    settlementsData.add(settlementMap);
                }

                regionMap.put("settlements", settlementsData);
                regionsData.add(regionMap);
            }

            resultMap.put("regions", regionsData);
            resultMap.put("stations", stationList);  // полный список объектов Station, если нужно
            break; // остановка после России
        }

    }


    //Вспомогательный метод: преобразует JSONObject с парами ключ-значение в Map<String, String>

    private static Map<String, String> jsonObjectToMap(JSONObject jsonObj) {
        Map<String, String> map = new HashMap<>();
        if (jsonObj == null) return map;

        Iterator<String> keys = jsonObj.keys();
        while (keys.hasNext()) {
            String key = keys.next();
            map.put(key, jsonObj.optString(key, ""));
        }
        return map;
    }


    public static List<SheduleResponce> getSheduleFromMap(SheduleRequest sheduleRequest) {
        return null;
    }
}





