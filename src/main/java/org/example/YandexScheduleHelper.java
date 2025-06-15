package org.example;

import org.example.api.YandexAPI;
import org.example.service.AllStationsResponseProcessor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

public class YandexScheduleHelper {

    public static class ScheduleItem {
        private final String fromCity;
        private final String toCity;
        private final String transport;
        private final String departureTime;

        public ScheduleItem(String fromCity, String toCity, String transport, String departureTime) {
            this.fromCity = fromCity;
            this.toCity = toCity;
            this.transport = transport;
            this.departureTime = departureTime;
        }

        @Override
        public String toString() {
            return fromCity + " → " + toCity + ", Транспорт: " + transport + ", Отправление: " + departureTime;
        }
    }

    public static void printStationsWithYandexCode(String city, String transport) {
        List<Map<String, Object>> regions = (List<Map<String, Object>>) AllStationsResponseProcessor.resultMap.get("regions");
        if (regions == null) {
            System.out.println("❌ В resultMap отсутствует ключ 'regions'");
            return;
        }

        System.out.println("📍 Поиск станций в городе: " + city + ", транспорт: " + transport);
        int count = 0;

        for (Map<String, Object> region : regions) {
            List<Map<String, Object>> settlements = (List<Map<String, Object>>) region.get("settlements");
            if (settlements == null) continue;

            for (Map<String, Object> settlement : settlements) {
                String settlementTitle = (String) settlement.get("title");
                if (!settlementTitle.equalsIgnoreCase(city)) continue;

                List<Map<String, Object>> stations = (List<Map<String, Object>>) settlement.get("stations");
                if (stations == null) continue;

                for (Map<String, Object> station : stations) {
                    String stationTransport = (String) station.get("transport_type");
                    if (stationTransport != null && stationTransport.equalsIgnoreCase(transport)) {
                        Map<String, String> codes = (Map<String, String>) station.get("codes");
                        String yandexCode = codes != null ? codes.get("yandex") : null;
                        if (yandexCode != null) {
                            count++;
                            System.out.println("✅ " + station.get("title") + " — код: " + yandexCode);
                        } else {
                            System.out.println("⚠️ " + station.get("title") + " — нет кода Yandex");
                        }
                    }
                }
            }
        }

        if (count == 0) {
            System.out.println("❌ Не найдено ни одной станции с кодом Yandex.");
        } else {
            System.out.println("✅ Найдено " + count + " станций с кодом Yandex.");
        }
    }

    private static List<Map<String, Object>> getStationsForCity(String city, String transport) {
        List<Map<String, Object>> stationsList = new ArrayList<>();
        List<Map<String, Object>> regions = (List<Map<String, Object>>) AllStationsResponseProcessor.resultMap.get("regions");
        if (regions == null) return stationsList;

        for (Map<String, Object> region : regions) {
            List<Map<String, Object>> settlements = (List<Map<String, Object>>) region.get("settlements");
            if (settlements == null) continue;

            for (Map<String, Object> settlement : settlements) {
                String settlementTitle = (String) settlement.get("title");
                if (!settlementTitle.equalsIgnoreCase(city)) continue;

                List<Map<String, Object>> stations = (List<Map<String, Object>>) settlement.get("stations");
                if (stations == null) continue;

                for (Map<String, Object> station : stations) {
                    String stationTransport = (String) station.get("transport_type");
                    if (stationTransport != null && stationTransport.equalsIgnoreCase(transport)) {
                        Map<String, String> codes = (Map<String, String>) station.get("codes");
                        if (codes != null && codes.get("yandex") != null) {
                            stationsList.add(station);
                        }
                    }
                }
            }
        }

        return stationsList;
    }

    private static List<ScheduleItem> getScheduleBetweenStations(
            Map<String, Object> fromStation,
            Map<String, Object> toStation,
            String fromCity,
            String toCity,
            String transport,
            String date,
            String apiKey) throws Exception {

        Map<String, String> fromCodes = (Map<String, String>) fromStation.get("codes");
        Map<String, String> toCodes = (Map<String, String>) toStation.get("codes");

        String fromId = fromCodes.get("yandex");
        String toId = toCodes.get("yandex");

        String urlStr = "https://api.rasp.yandex.net/v3.0/search/"
                + "?apikey=" + URLEncoder.encode(apiKey, "UTF-8")
                + "&format=json"
                + "&from=" + URLEncoder.encode(fromId, "UTF-8")
                + "&to=" + URLEncoder.encode(toId, "UTF-8")
                + "&lang=ru_RU"
                + "&date=" + URLEncoder.encode(date, "UTF-8")
                + "&transport_types=" + URLEncoder.encode(transport, "UTF-8")
                + "&page=1";

        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) {
            response.append(line);
        }
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray segments = json.optJSONArray("segments");
        if (segments == null) return Collections.emptyList();

        List<ScheduleItem> result = new ArrayList<>();
        for (int i = 0; i < segments.length(); i++) {
            JSONObject seg = segments.getJSONObject(i);
            String departure = seg.optString("departure", "неизвестно");

            result.add(new ScheduleItem(
                    fromCity + " (" + fromStation.get("title") + ")",
                    toCity + " (" + toStation.get("title") + ")",
                    transport,
                    departure
            ));
        }

        return result;
    }

    public static List<ScheduleItem> getRouteInfoListFromMap(
            String fromCity,
            String toCity,
            String transport,
            String date,
            String apiKey) throws Exception {

        List<ScheduleItem> result = new ArrayList<>();

        List<Map<String, Object>> fromStations = getStationsForCity(fromCity, transport);
        List<Map<String, Object>> toStations = getStationsForCity(toCity, transport);

        if (fromStations.isEmpty() || toStations.isEmpty()) {
            System.out.println("❌ Не найдены подходящие станции с кодами Yandex.");
            return result;
        }

        for (Map<String, Object> fromStation : fromStations) {
            for (Map<String, Object> toStation : toStations) {
                result.addAll(getScheduleBetweenStations(
                        fromStation, toStation, fromCity, toCity, transport, date, apiKey
                ));
            }
        }

        return result;
    }

    public static void main(String[] args) throws Exception {
        String apiKey = "82cfd045-18ee-49da-ad53-885ebe601d94"; // ← Вставь свой рабочий API-ключ

        // 1. Загрузка списка станций
        JSONObject json = YandexAPI.getJSON(YandexAPI.getAllStationsRequestUrl());
        AllStationsResponseProcessor.extractRussiaStations(json);

        String fromCity = "Москва";
        String toCity = "Санкт-Петербург";
        String transport = "train";
        String date = "2025-06-16";

        // 2. Показать станции с кодами
        printStationsWithYandexCode(fromCity, transport);
        printStationsWithYandexCode(toCity, transport);

        // 3. Получить и вывести расписание
        List<ScheduleItem> schedule = getRouteInfoListFromMap(fromCity, toCity, transport, date, apiKey);

        if (schedule.isEmpty()) {
            System.out.println("📭 Расписание не найдено.");
        } else {
            System.out.println("📅 Найдено расписание:");
            for (ScheduleItem item : schedule) {
                System.out.println(item);
            }
        }
    }
}
