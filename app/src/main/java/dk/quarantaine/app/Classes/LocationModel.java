package dk.quarantaine.app.Classes;

import java.util.Date;

public class LocationModel {
    // Properties
    private int id;
    private double lat;
    private double lon;
    private Date time;

    //Constructors
    public LocationModel(int id, double lat, double lon, Date time) {
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.time = time;
    }

    public LocationModel() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    // toString of all data

    @Override
    public String toString() {
        return "LocationModel{" +
                "id=" + id +
                ", lat=" + lat +
                ", lon=" + lon +
                ", time=" + time +
                '}';
    }
}
