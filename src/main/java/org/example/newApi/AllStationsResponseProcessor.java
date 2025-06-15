package org.example.newApi;


import org.example.newApi.Station;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class AllStationsResponseProcessor {
    public static Map<String, Object> resultMap;

    public static void extractRussiaStations(JSONObject jsonObject) {
        resultMap = new HashMap<>();
        List<Map<String, Object>> regionsData = new ArrayList<>();

        JSONArray countriesArray = jsonObject.optJSONArray("countries");
        if (countriesArray == null) return;

        for (int i = 0; i < countriesArray.length(); i++) {
            JSONObject countryObj = countriesArray.getJSONObject(i);
            if (!"Россия".equalsIgnoreCase(countryObj.optString("title"))) continue;

            JSONArray regionsArray = countryObj.optJSONArray("regions");
            if (regionsArray == null) continue;

            for (int j = 0; j < regionsArray.length(); j++) {
                JSONObject regionObj = regionsArray.getJSONObject(j);
                List<Map<String, Object>> settlementsData = new ArrayList<>();

                JSONArray settlementsArray = regionObj.optJSONArray("settlements");
                if (settlementsArray == null) continue;

                for (int k = 0; k < settlementsArray.length(); k++) {
                    JSONObject settlementObj = settlementsArray.getJSONObject(k);
                    List<Map<String, Object>> stationsData = new ArrayList<>();

                    JSONArray stationsArray = settlementObj.optJSONArray("stations");
                    if (stationsArray == null) continue;

                    for (int m = 0; m < stationsArray.length(); m++) {
                        JSONObject stationObj = stationsArray.getJSONObject(m);
                        JSONObject codesObj = stationObj.optJSONObject("codes");

                        if (codesObj == null || !codesObj.has("yandex")) continue;

                        Map<String, Object> stationMap = new HashMap<>();
                        stationMap.put("title", stationObj.optString("title"));
                        stationMap.put("transport_type", stationObj.optString("transport_type"));
                        stationMap.put("station_type", stationObj.optString("station_type"));
                        stationMap.put("codes", jsonObjectToMap(codesObj));

                        stationsData.add(stationMap);
                    }

                    if (!stationsData.isEmpty()) {
                        Map<String, Object> settlementMap = new HashMap<>();
                        settlementMap.put("title", settlementObj.optString("title"));
                        settlementMap.put("stations", stationsData);
                        settlementsData.add(settlementMap);
                    }
                }

                if (!settlementsData.isEmpty()) {
                    Map<String, Object> regionMap = new HashMap<>();
                    regionMap.put("title", regionObj.optString("title"));
                    regionMap.put("settlements", settlementsData);
                    regionsData.add(regionMap);
                }
            }
        }

        resultMap.put("regions", regionsData);
    }

    public static Map<String, String> jsonObjectToMap(JSONObject obj) {
        Map<String, String> map = new HashMap<>();
        if (obj == null) return map;
        for (String key : obj.keySet()) {
            map.put(key, obj.optString(key));
        }
        return map;
    }
}
