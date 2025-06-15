package org.example.tgBot.service;

import org.example.DTO.AllStationsResponse;
import org.example.DTO.SheduleRequest;
import org.example.DTO.SheduleResponce;
import org.example.api.YandexAPI;
import org.example.service.AllStationsResponseProcessor;
import org.example.service.SheduleResponceProcessor;
import org.json.JSONObject;

import java.util.List;

public class RequestSheduleService {

    public static String getSgedule(SheduleRequest sheduleRequest) {
        String urlString = YandexAPI.getAllStationsRequestUrl();
        JSONObject jsonText = YandexAPI.getJSON(urlString);
        AllStationsResponseProcessor.extractRussiaStations(jsonText);

        return jsonText != null ? convertData(jsonText, sheduleRequest) : '\u274C' + " запрос неправильный";
    }

    private static String convertData(JSONObject jsonObject, SheduleRequest sheduleRequest) {
        List<SheduleResponce> listSheduleResponces = AllStationsResponseProcessor.getSheduleFromMap(sheduleRequest);

        StringBuilder stringBuilder = new StringBuilder();

        for (SheduleResponce s: listSheduleResponces){
            stringBuilder.append(s);
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }
}