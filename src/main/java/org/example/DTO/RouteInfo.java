package org.example.DTO;

public class RouteInfo {
    private String fromCity;
    private String toCity;
    private String date;
    private String departureTime;
    private String arrivalTime;

    public void setFromCity(String fromCity) { this.fromCity = fromCity; }

    public void setToCity(String toCity) { this.toCity = toCity; }

    public void setDate(String date) { this.date = date; }

    public void setDepartureTime(String departureTime) { this.departureTime = departureTime; }

    public void setArrivalTime(String arrivalTime) { this.arrivalTime = arrivalTime; }


    @Override
    public String toString() {
        return "Маршрут: " + fromCity + " ➡ " + toCity + "\n"
             + "Время отправления: " + departureTime + "\n"
             + "Время прибытия: " + arrivalTime + "\n";
    }
}
