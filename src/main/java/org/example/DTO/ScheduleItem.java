package org.example.DTO;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class ScheduleItem {
    private String fromCity;
    private String toCity;
    private String transport;
    private String date;

    public ScheduleItem(String fromCity, String toCity, String transport, String date) {
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.transport = transport;
        this.date = date;
    }
}
