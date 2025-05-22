package org.example.service;

import org.example.api.YandexAPI;
import org.example.model.Something;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class ScheduleProcessor {

    YandexAPI yandexAPI;

    public ScheduleProcessor(YandexAPI yandexAPI){
        this.yandexAPI = yandexAPI;
    }

    public List<Something> parseAndGetList(JSONObject jsonObject) {
        List<Something> result = new ArrayList<>();

        JSONArray scheduleArray = jsonObject.optJSONArray("schedule");

        if (scheduleArray != null) {
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject item = scheduleArray.getJSONObject(i);
                JSONObject thread = item.optJSONObject("thread");

                Something somethingItem = Something.builder()
                        .title(thread != null ? thread.optString("title", "Неизвестно") : "—")
                        .departure(item.optString("departure", "—"))
                        .terminal(item.optString("terminal", "—"))
                        .platform(item.optString("platform", "—"))
                        .build();

                result.add(somethingItem);
            }

            // сортировка по времени
            result.sort(Comparator.comparing(Something::getDeparture));
        }

        return result;
    }
}
