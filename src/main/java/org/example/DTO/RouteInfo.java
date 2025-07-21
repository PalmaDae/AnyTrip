package org.example.DTO;

public class RouteInfo {
    private String fromCity;
    private String toCity;
    private String date;
    private String departureTime;
    private String arrivalTime;
    private String transportType;

    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public void setToCity(String toCity) { this.toCity = toCity; }

    public void setDate(String date) { this.date = date; }

    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }

    public void setTransportType(String transportType) { this.transportType = transportType; }

    @Override
    public String toString() {
        return String.format(
                "Маршрут: %s → %s\nДата: %s\nОтправление: %s\nПрибытие: %s\n",
                fromCity, toCity, date, departureTime, arrivalTime
        );
    }
}
