package org.example.tgBot.service;

import org.example.DTO.AllStationsResponse;
import org.example.DTO.SheduleRequest;
import org.example.DTO.SheduleResponce;
import org.example.api.YandexAPI;
import org.example.service.SheduleResponceProcessor;
import org.json.JSONObject;

import java.util.List;

public class RequestSheduleService {

    public static String getSgedule(SheduleRequest sheduleRequest) {
        String urlString = YandexAPI.getScheduleRequestUrl(sheduleRequest.getStation(), sheduleRequest.getTransport(), sheduleRequest.getDate());
        JSONObject jsonText = YandexAPI.getJSON(urlString);

        return jsonText != null ? convertData(jsonText) : '\u274C' + " запрос неправильный";
    }

    private static String convertData(JSONObject jsonObject) {
        if (jsonObject != null) {
            SheduleResponceProcessor sheduleResponceProcessor = new SheduleResponceProcessor();
            List<SheduleResponce> listShedules = sheduleResponceProcessor.parseAndGetList(jsonObject);

            StringBuilder stringBuilder = new StringBuilder();

            for (SheduleResponce sheduleResponce : listShedules) {
                stringBuilder.append(sheduleResponce.toString() + "\n");
            }

            return stringBuilder.toString();
        }

        return "";
    }
}