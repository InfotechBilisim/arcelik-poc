package com.infotech.locationbox.servlet;

public class DataTspPoint {
    private String name = null;
    private double cost = 0.00;
    private double distance = 0.00;
    private double duration = 0.00;

    public DataTspPoint() {
    }

    public DataTspPoint(String name, double cost, double distance, double duration) {
        this.name = name;
        this.cost = cost;
        this.distance = distance;
        this.duration = duration;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"name\": \"" + name + "\",\n";
        s += indent + "  \"cost\": " + (int) (cost + 0.5) + ",\n";
        s += indent + "  \"distance\": " + (int) (distance + 0.5) + ",\n";
        s += indent + "  \"duration\": " + (int) (duration + 0.5) + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "      <tsppoint>\n";
        s += "        <name>" + name + "</name>\n";
        s += "        <cost>" + (int) (cost + 0.5) + "</cost>\n";
        s += "        <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "        <duration>" + (int) (duration + 0.5) + "</duration>\n";
        s += "      </tsppoint>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

}
