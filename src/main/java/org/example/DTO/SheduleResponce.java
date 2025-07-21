package org.example.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SheduleResponce {
    private String title;
    private String departure;
    private String terminal;
    private String platform;

    @Override
    public String toString(){
        return title + " " + departure + " " + terminal + " " + platform;
    }

}
