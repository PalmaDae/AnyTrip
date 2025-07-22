package org.example.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.DTO.AllStationsResponse;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.SocketException;
import java.net.URL;
import java.util.zip.GZIPInputStream;

public class YandexAPI {
    public static AllStationsResponse getAllStationsResponse() {
        try {
            URL url = new URL(getAllStationsRequestUrl());
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

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(gzipStream, AllStationsResponse.class);

        } catch (SocketException e) {
            System.out.println("TimeOut");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public static String getAllStationsRequestUrl() {
        return "https://api.rasp.yandex.net/v3.0/stations_list/?apikey=" +
                org.example.util.ClosedStrings.API_KEY +
                "&lang=ru_RU&format=json";
    }
}