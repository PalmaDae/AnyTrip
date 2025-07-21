package org.example;

import org.example.DTO.AllStationsResponse;
import org.example.api.YandexAPI;
import org.example.service.AllStationsResponseProcessor;
import org.example.util.ClosedStrings;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import org.example.DTO.RouteInfo;

public class RaspRequestBuilder {

    public static List<String> buildSearchRequests(
            String fromCity,
            String toCity,
            String transportType,
            String date,
            String apiKey
    ) {
        List<String> requests = new ArrayList<>();

        if (AllStationsResponseProcessor.resultMap == null) {
            System.out.println("resultMap is empty. Call extractRussiaStations() first.");
            return requests;
        }

        HashMap<AllStationsResponse.Settlement, List<AllStationsResponse.Station>> russiaMap = AllStationsResponseProcessor.resultMap.get("Россия");
        if (russiaMap == null) {
            System.out.println("Россия не найдена в resultMap.");
            return requests;
        }

        String fromCode = null;
        String toCode = null;

        for (Map.Entry<AllStationsResponse.Settlement, List<AllStationsResponse.Station>> entry : russiaMap.entrySet()) {
            AllStationsResponse.Settlement settlement = entry.getKey();

            if (settlement.getTitle().equalsIgnoreCase(fromCity)) {
                fromCode = settlement.getYandexCode();
            }

            if (settlement.getTitle().equalsIgnoreCase(toCity)) {
                toCode = settlement.getYandexCode();
            }

            if (fromCode != null && toCode != null) break;
        }

        if (fromCode == null || toCode == null) {
            System.out.println("Не удалось найти коды городов: " + fromCity + ", " + toCity);
            return requests;
        }

        try {
            String url = String.format(
                    "https://api.rasp.yandex.net/v3.0/search/?apikey=" + ClosedStrings.API_KEY + "&format=json&from=" + fromCode + "&to=" + toCode + "&lang=ru_RU&page=1&date=" + date + "&transport_types=" + transportType + "&limit=5",
                    URLEncoder.encode(apiKey, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(fromCode, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(toCode, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(date, StandardCharsets.UTF_8.toString()),
                    URLEncoder.encode(transportType, StandardCharsets.UTF_8.toString())
            );
            requests.add(url);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return requests;
    }

    public static List<RouteInfo> parseRoutesFromUrl(String requestUrl) {
        List<RouteInfo> routeList = new ArrayList<>();

        try {
            URL url = new URL(requestUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            if (conn.getResponseCode() != 200) {
                System.out.println("Ошибка при получении данных: " + conn.getResponseCode());
                return routeList;
            }

            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder jsonBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                jsonBuilder.append(line);
            }

            reader.close();
            JSONObject json = new JSONObject(jsonBuilder.toString());
            JSONArray segments = json.optJSONArray("segments");

            if (segments == null || segments.isEmpty()) {
                System.out.println("Нет доступных маршрутов.");
                return routeList;
            }

            for (int i = 0; i < segments.length(); i++) {
                JSONObject segment = segments.getJSONObject(i);
                RouteInfo route = new RouteInfo();

                route.setFromCity(segment.getJSONObject("from").optString("title", ""));
                route.setToCity(segment.getJSONObject("to").optString("title", ""));
                route.setTransportType(segment.optString("transport_type", ""));

                String departureISO = segment.optString("departure");
                String arrivalISO = segment.optString("arrival");

                String departureDate = "", departureTime = "";
                String arrivalTime = "";

                if (departureISO.contains("T")) {
                    String[] depParts = departureISO.split("T");
                    departureDate = depParts[0]; // yyyy-MM-dd
                    departureTime = depParts[1].split("\\+")[0].substring(0, 5); // HH:mm
                }

                if (arrivalISO.contains("T")) {
                    String[] arrParts = arrivalISO.split("T");
                    arrivalTime = arrParts[1].split("\\+")[0].substring(0, 5); // HH:mm
                }

                route.setDate(departureDate);               // "2025-06-20"
                route.setDepartureTime(departureTime);      // "06:10"
                route.setArrivalTime(arrivalTime);          // "11:55"

                routeList.add(route);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return routeList;
    }


    public static String getInString(List<String> urls) {
        StringBuilder stringBuilder = new StringBuilder();

        for (String u : urls) {
            List<RouteInfo> routeInfoList = parseRoutesFromUrl(u);

            for (RouteInfo routeInfo : routeInfoList) {
                stringBuilder.append(routeInfo);
                stringBuilder.append("\n\n"); // Два переноса строки после каждого маршрута
            }
        }

        return stringBuilder.toString();
    }
}
