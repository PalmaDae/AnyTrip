package org.example.DTO;

import lombok.Data;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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

    public static class Settlement {
        private String title;
        private String yandexCode;

        public Settlement(String title, String yandexCode) {
            this.title = title;
            this.yandexCode = yandexCode;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Settlement)) return false;
            Settlement that = (Settlement) o;
            return Objects.equals(title, that.title) &&
                    Objects.equals(yandexCode, that.yandexCode);
        }

        @Override
        public int hashCode() {
            return Objects.hash(title, yandexCode);
        }

        public String getTitle() {
            return title;
        }

        public String getYandexCode() {
            return yandexCode;
        }
    }

    @Data
    public static class Station {
        private String title;
        private String yandexCode;
        private String stationType;
        private String direction;
        private String transportType;
        private double latitude;
        private double longitude;

        public Station(String title, String yandexCode, String stationType,
                       String direction, String transportType, double latitude, double longitude) {
            this.title = title;
            this.yandexCode = yandexCode;
            this.stationType = stationType;
            this.direction = direction;
            this.transportType = transportType;
            this.latitude = latitude;
            this.longitude = longitude;
        }
    }
}