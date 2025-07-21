package org.example.service;

import org.example.api.YandexAPI;

import java.util.Map;

import static org.example.service.AllStationsResponseProcessor.*;

public class main {
    public static void main(String[] args) {
        String url = YandexAPI.getAllStationsRequestUrl();
        System.out.println("🔗 Отправляем запрос: " + url);
        var json = YandexAPI.getJSON(url);
        System.out.println("🧾 Есть ли JSON? " + (json != null));
        extractRussiaStations(json);
    }
}