package org.example.newApi;


import org.example.newApi.YandexAPI;
import org.example.newApi.AllStationsResponseProcessor;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;

    public class YandexScheduleHelper {

        public static class ScheduleItem {
            String fromCity, toCity, transport, departureTime;

            public ScheduleItem(String fromCity, String toCity, String transport, String departureTime) {
                this.fromCity = fromCity;
                this.toCity = toCity;
                this.transport = transport;
                this.departureTime = departureTime;
            }

            @Override
            public String toString() {
                return fromCity + " -> " + toCity + ", Транспорт: " + transport + ", Отправление: " + departureTime;
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
                        if (stationTransport == null) continue;
                        if (!stationTransport.equalsIgnoreCase(transport)) continue;

                        Map<String, Object> codes = (Map<String, Object>) station.get("codes");
                        if (codes == null) continue;

                        String yandexCode = (String) codes.get("yandex");
                        if (yandexCode == null || yandexCode.isEmpty()) {
                            // Пропускаем станции без кода Yandex
                            continue;
                        }

                        stationsList.add(station);
                    }
                }
            }

            return stationsList;
        }


        private static List<ScheduleItem> getScheduleBetweenStations(
                Map<String, Object> from, Map<String, Object> to, String fromCity, String toCity,
                String transport, String date, String apiKey) throws Exception {

            Map<String, String> fromCodes = (Map<String, String>) from.get("codes");
            Map<String, String> toCodes = (Map<String, String>) to.get("codes");
            if (fromCodes == null || toCodes == null) return Collections.emptyList();

            String fromId = fromCodes.get("yandex");
            String toId = toCodes.get("yandex");
            if (fromId == null || toId == null) return Collections.emptyList();

            String urlStr = "https://api.rasp.yandex.net/v3.0/search/?apikey=" + URLEncoder.encode(apiKey, "UTF-8") +
                    "&format=json&from=" + URLEncoder.encode(fromId, "UTF-8") +
                    "&to=" + URLEncoder.encode(toId, "UTF-8") +
                    "&lang=ru_RU&date=" + URLEncoder.encode(date, "UTF-8") +
                    "&transport_types=" + URLEncoder.encode(transport, "UTF-8") +
                    "&page=1";

            HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
            conn.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) response.append(line);
            in.close();

            JSONArray segments = new JSONObject(response.toString()).optJSONArray("segments");
            if (segments == null) return Collections.emptyList();

            List<ScheduleItem> result = new ArrayList<>();
            for (int i = 0; i < segments.length(); i++) {
                JSONObject seg = segments.getJSONObject(i);
                String departure = seg.optString("departure", "неизвестно");

                result.add(new ScheduleItem(
                        fromCity + " (" + from.get("title") + ")",
                        toCity + " (" + to.get("title") + ")",
                        transport,
                        departure
                ));
            }

            return result;
        }

        public static void main(String[] args) throws Exception {
            String apiKey = "82cfd045-18ee-49da-ad53-885ebe601d94"; // замените!

            JSONObject json = YandexAPI.getJSON(YandexAPI.getAllStationsRequestUrl().replace("YOUR_API_KEY", apiKey));
            AllStationsResponseProcessor.extractRussiaStations(json);

            String fromCity = "Москва";
            String toCity = "Санкт-Петербург";
            String transport = "train";
            String date = "2025-06-16";

            List<Map<String, Object>> fromStations = getStationsForCity(fromCity, transport);
            List<Map<String, Object>> toStations = getStationsForCity(toCity, transport);

            List<ScheduleItem> result = new ArrayList<>();
            for (Map<String, Object> from : fromStations) {
                for (Map<String, Object> to : toStations) {
                    result.addAll(getScheduleBetweenStations(from, to, fromCity, toCity, transport, date, apiKey));
                }
            }

            if (result.isEmpty()) {
                System.out.println("📭 Расписание не найдено.");
            } else {
                System.out.println("📅 Найдено расписание:");
                for (ScheduleItem item : result) {
                    System.out.println(item);
                }
            }
        }
    }


