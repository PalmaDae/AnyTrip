package org.example.service;

import org.example.DTO.SheduleResponce;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class SheduleResponceProcessor {
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

}
