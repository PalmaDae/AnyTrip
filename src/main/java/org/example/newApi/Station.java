package org.example.newApi;


import java.util.Map;

public class Station {
    private String title;
    private String station_type;
    private String direction;
    private String transport_type;
    private double longitude;
    private double latitude;
    private Map<String, String> codes;

    // Getters & setters
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getStation_type() { return station_type; }
    public void setStation_type(String station_type) { this.station_type = station_type; }

    public String getDirection() { return direction; }
    public void setDirection(String direction) { this.direction = direction; }

    public String getTransport_type() { return transport_type; }
    public void setTransport_type(String transport_type) { this.transport_type = transport_type; }

    public double getLongitude() { return longitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }

    public double getLatitude() { return latitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    public Map<String, String> getCodes() { return codes; }
    public void setCodes(Map<String, String> codes) { this.codes = codes; }
}
