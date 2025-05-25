package org.example.tgBot.service;

import org.example.DTO.Shedule;
import org.example.DTO.Something;
import org.example.api.YandexAPI;
import org.example.service.SomethingProcessor;
import org.example.util.ScannerUtils;
import org.json.JSONObject;

import java.util.List;

public class RequestShedule {

    public static String getSgedule(Shedule shedule){
        String urlString = YandexAPI.getScheduleRequestUrl(shedule.getStation(), shedule.getTransport(), shedule.getDate());
        JSONObject jsonText = YandexAPI.getJSON(urlString);

        return convertData(jsonText);
    }

    private static String convertData(JSONObject jsonObject){
        if (jsonObject != null) {
            SomethingProcessor somethingProcessor = new SomethingProcessor();
            List<Something> listShedules = somethingProcessor.parseAndGetList(jsonObject);

            StringBuilder stringBuilder = new StringBuilder();

            for (Something something : listShedules){
                stringBuilder.append(something.toString() + "\n");
            }

            return stringBuilder.toString();
        }

        return "";
    }


}
