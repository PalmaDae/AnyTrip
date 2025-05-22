package org.example.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Schedule {
    private String title;
    private String departure;
    private String terminal;
    private String platform;
}
