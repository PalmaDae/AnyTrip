package org.example.service;

import org.example.model.Schedule;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;

public class ScheduleProcessor {

    public List<Schedule> parseAndGetList(JSONObject jsonObject) {
        List<Schedule> result = new ArrayList<>();

        JSONArray scheduleArray = jsonObject.optJSONArray("schedule");
        if (scheduleArray != null) {
            for (int i = 0; i < scheduleArray.length(); i++) {
                JSONObject item = scheduleArray.getJSONObject(i);
                JSONObject thread = item.optJSONObject("thread");

                Schedule scheduleItem = Schedule.builder()
                        .title(thread != null ? thread.optString("title", "Неизвестно") : "—")
                        .departure(item.optString("departure", "—"))
                        .terminal(item.optString("terminal", "—"))
                        .platform(item.optString("platform", "—"))
                        .build();

                result.add(scheduleItem);
            }

            // сортировка по времени
            result.sort(Comparator.comparing(Schedule::getDeparture));
        }

        return result;
    }
}
