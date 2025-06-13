package org.example.DTO;

import lombok.Data;
import java.util.List;
import java.util.Map;

@Data
public class AllStationsResponse {
    private List<Country> countries;

    @Data
    public static class Country {
        private List<Region> regions;
        private Map<String, String> codes;
        private String title;
    }

    @Data
    public static class Region {
        private List<Settlement> settlements;
        private Map<String, String> codes;
        private String title;
    }

    @Data
    public static class Settlement {
        private String title;
        private Map<String, String> codes;
        private List<Station> stations;
    }

    @Data
    public static class Station {
        private String direction;
        private Map<String, String> codes;
        private String station_type;
        private String title;
        private double longitude;
        private String transport_type;
        private double latitude;
    }
}