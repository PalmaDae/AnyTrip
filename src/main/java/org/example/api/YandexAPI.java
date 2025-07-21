package org.example.api;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

import org.example.util.ClosedStrings;
import org.json.JSONObject;

public class YandexAPI {
    public static JSONObject getJSON(String urlByString) {
        try {
            URL url = new URL(urlByString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            connection.setRequestProperty("Accept-Encoding", "gzip");

            connection.setConnectTimeout(500000);
            connection.setReadTimeout(500000);

            int responseCode = connection.getResponseCode();

            if (responseCode != HttpURLConnection.HTTP_OK) {
                throw new Exception("HTTP error code: " + responseCode);
            }

            InputStream gzipStream = new GZIPInputStream(connection.getInputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(gzipStream, "UTF-8"));

            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            int contentLength = connection.getContentLength();
            System.out.println("Ожидаемый размер: " + contentLength);
            System.out.println("Фактически получено: " + response.length());

            return new JSONObject(response.toString());

        } catch (SocketException e) {
            System.out.println("TimeOut");
        } catch (Exception e) {
            System.out.println("Error" + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String getAllStationsRequestUrl() {
        return "https://api.rasp.yandex.net/v3.0/stations_list/?apikey=" + ClosedStrings.API_KEY + "&lang=ru_RU&format=json";
    }
}