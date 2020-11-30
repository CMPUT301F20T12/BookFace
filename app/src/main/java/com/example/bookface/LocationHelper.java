package com.example.bookface;

/**
 * This is a class that helps in setting and retrieving the location values from the database
 */
public class LocationHelper {
    // Variable declarations
    private double latitude;
    private double longitude;

    /**
     * This is the constructor
     * @param latitude
     * @param longitude
     */
    public LocationHelper(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Getters and Setters
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
