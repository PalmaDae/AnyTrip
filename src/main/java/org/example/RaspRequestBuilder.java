package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.AllStationsResponse;
import org.example.DTO.RouteInfo;
import org.example.service.AllStationsResponseProcessor;
import org.example.util.ClosedStrings;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class RaspRequestBuilder {

    private static final ObjectMapper mapper = new ObjectMapper();

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

        Map<AllStationsResponse.Settlement, List<AllStationsResponse.Station>> russiaMap = AllStationsResponseProcessor.resultMap.get("Россия");
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
                    "https://api.rasp.yandex.net/v3.0/search/?apikey=%s&format=json&from=%s&to=%s&lang=ru_RU&page=1&date=%s&transport_types=%s&limit=5",
                    URLEncoder.encode(apiKey, StandardCharsets.UTF_8),
                    URLEncoder.encode(fromCode, StandardCharsets.UTF_8),
                    URLEncoder.encode(toCode, StandardCharsets.UTF_8),
                    URLEncoder.encode(date, StandardCharsets.UTF_8),
                    URLEncoder.encode(transportType, StandardCharsets.UTF_8)
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

            try (InputStream is = conn.getInputStream()) {

                JsonNode root = mapper.readTree(is);
                JsonNode segments = root.path("segments");

                if (!segments.isArray() || segments.isEmpty()) {
                    System.out.println("Нет доступных маршрутов.");
                    return routeList;
                }

                for (JsonNode segment : segments) {
                    RouteInfo route = new RouteInfo();

                    route.setFromCity(segment.path("from").path("title").asText(""));
                    route.setToCity(segment.path("to").path("title").asText(""));
                    route.setTransportType(segment.path("transport_type").asText(""));

                    String departureISO = segment.path("departure").asText("");
                    String arrivalISO = segment.path("arrival").asText("");

                    if (departureISO.contains("T")) {
                        String[] depParts = departureISO.split("T");
                        route.setDate(depParts[0]);
                        route.setDepartureTime(depParts[1].split("\\+")[0].substring(0, 5));
                    }

                    if (arrivalISO.contains("T")) {
                        String[] arrParts = arrivalISO.split("T");
                        route.setArrivalTime(arrParts[1].split("\\+")[0].substring(0, 5));
                    }

                    routeList.add(route);
                }
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