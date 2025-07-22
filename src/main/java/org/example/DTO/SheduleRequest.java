package org.example.DTO;

import lombok.Data;

@Data
public class SheduleRequest {
    public SheduleRequest(boolean t){}

    private String city1;
    private String city2;
    private String transport;
    private String date;
}
