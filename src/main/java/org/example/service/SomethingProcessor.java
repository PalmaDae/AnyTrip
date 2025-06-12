package org.example.service;

import org.example.DTO.SheduleResponce;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

import org.example.util.ScannerUtils;
import org.example.handler.StartCommands;

public class SomethingProcessor {


    public static List<SheduleResponce> parseAndGetList(JSONObject jsonObject) {
        List<SheduleResponce> result = new ArrayList<>();

        JSONArray scheduleArray = jsonObject.optJSONArray("schedule");

        if (scheduleArray != null) {
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject item = scheduleArray.getJSONObject(i);
                JSONObject thread = item.optJSONObject("thread");

                SheduleResponce sheduleResponceItem = SheduleResponce.builder()
                        .title(thread != null ? thread.optString("title", "Неизвестно") : "—")
                        .departure(item.optString("departure", "—"))
                        .terminal(item.optString("terminal", "—"))
                        .platform(item.optString("platform", "—"))
                        .build();

                result.add(sheduleResponceItem);
            }

            // сортировка по времени
            result.sort(Comparator.comparing(SheduleResponce::getDeparture));
        }

        return result;
    }

    public static void createNewSomething(String station, String transport_type, String date){
        System.out.println("Введите данные, сейвов нет");
        station = ScannerUtils.askStationCode(StartCommands.getScanner());
        transport_type = ScannerUtils.getTransportTypeFromScanner(StartCommands.getScanner());
        date = ScannerUtils.getDate(StartCommands.getScanner());

        //saveLastRequest(station, transport_type, date);
    }
}
