package com.infotech.locationbox.servlet;


public class Vertex {
    private double latitude = 0.00;
    private double longitude = 0.00;
    private double length = 0;

    public Vertex() {
    }

    public Vertex(double latitude, double longitude, double length) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.length = length;
    }

    //-----------------------------------------------------------------------------

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getLength() {
        return length;
    }

}
