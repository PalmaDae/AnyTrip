package org.example.service;

import org.example.DTO.AllStationsResponse;
import org.example.DTO.AllStationsResponse.Station;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllStationsResponseProcessor {
    public static List<Station> parseAndGetAllStations(JSONObject jsonObject) {
        List<Station> stations = new ArrayList<>();

        JSONArray countriesArray = jsonObject.optJSONArray("countries");
        if (countriesArray == null) return stations;

        for (int i = 0; i < countriesArray.length(); i++) {
            JSONObject countryObj = countriesArray.getJSONObject(i);
            JSONArray regionsArray = countryObj.optJSONArray("regions");
            if (regionsArray == null) continue;

            for (int j = 0; j < regionsArray.length(); j++) {
                JSONObject regionObj = regionsArray.getJSONObject(j);
                JSONArray settlementsArray = regionObj.optJSONArray("settlements");
                if (settlementsArray == null) continue;

                for (int k = 0; k < settlementsArray.length(); k++) {
                    JSONObject settlementObj = settlementsArray.getJSONObject(k);
                    JSONArray stationsArray = settlementObj.optJSONArray("stations");
                    if (stationsArray == null) continue;

                    for (int m = 0; m < stationsArray.length(); m++) {
                        JSONObject stationObj = stationsArray.getJSONObject(m);

                        Station station = new Station();
                        station.setDirection(stationObj.optString("direction", ""));
                        // "codes" — Map<String, String>, нужно распарсить JSONObject в Map
                        station.setCodes(jsonObjectToMap(stationObj.optJSONObject("codes")));
                        station.setStation_type(stationObj.optString("station_type", ""));
                        station.setTitle(stationObj.optString("title", ""));
                        station.setLongitude(stationObj.optDouble("longitude", 0));
                        station.setTransport_type(stationObj.optString("transport_type", ""));
                        station.setLatitude(stationObj.optDouble("latitude", 0));

                        stations.add(station);
                    }
                }
            }
        }

        return stations;
    }

    //Вспомогательный метод: преобразует JSONObject с парами ключ-значение в Map<String, String>

    private static Map<String, String> jsonObjectToMap(JSONObject jsonObject) {
        Map<String, String> map = new HashMap<>();
        if (jsonObject == null) return map;

        for (String key : jsonObject.keySet()) {
            map.put(key, jsonObject.optString(key, ""));
        }
        return map;
    }
}
