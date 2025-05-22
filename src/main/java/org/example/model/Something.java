package org.example.model;

import lombok.Builder;
import lombok.Data;

// хз как это назвать - ты это писал, тебе дать название этой сущности -> поэтому назвал как Something...

@Data
@Builder
public class Something {
    private String title;
    private String departure;
    private String terminal;
    private String platform;
}
