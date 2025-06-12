package org.example.DTO;

import lombok.Builder;
import lombok.Data;

// хз как это назвать - ты это писал, тебе дать название этой сущности -> поэтому назвал как Something...

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
