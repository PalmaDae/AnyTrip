package org.example.DTO;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


@Data
public class AllStationsResponse {
    private List<Country> countries;

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Country {
        private List<Region> regions;
        private Map<String, String> codes;
        private String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Region {
        private List<Settlement> settlements;
        private Map<String, String> codes;
        private String title;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Settlement {
        private String title;
        private Codes codes;
        private List<Station> stations;

        public Settlement() {}

        public String getYandexCode() {
            return codes != null ? codes.getYandex_code() : "";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Station {
        private String title;
        private Codes codes;
        private String station_type;
        private String direction;
        private String transport_type;
        private double latitude;
        private double longitude;

        public String getYandexCode() {
            return codes != null ? codes.getYandex_code() : "";
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    @Data
    public static class Codes {
        private String yandex_code;
    }
}