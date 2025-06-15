package org.example.newApi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Main {
    public static void main(String[] args) throws Exception {
        String apiKey = "82cfd045-18ee-49da-ad53-885ebe601d94";  // <-- замени

        String from = "s9600213"; // Москва, Ленинградский вокзал
        String to = "s9602496";   // СПб, Московский вокзал
        String date = "2025-06-16";

        String urlStr = String.format(
                "https://api.rasp.yandex.net/v3.0/search/?apikey=%s&from=%s&to=%s&format=json&lang=ru_RU&transport_types=train&date=%s",
                apiKey, from, to, date
        );

        HttpURLConnection conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder response = new StringBuilder();
        String line;
        while ((line = in.readLine()) != null) response.append(line);
        in.close();

        JSONObject json = new JSONObject(response.toString());
        JSONArray segments = json.optJSONArray("segments");

        if (segments == null || segments.length() == 0) {
            System.out.println("📭 Расписание не найдено.");
            return;
        }

        System.out.println("📅 Найдено расписание:");
        for (int i = 0; i < segments.length(); i++) {
            JSONObject seg = segments.getJSONObject(i);
            String departure = seg.optString("departure");
            String thread = seg.optJSONObject("thread").optString("title");
            System.out.println("🚆 " + thread + " — выезд в " + departure);
        }
    }
}

