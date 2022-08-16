package com.infotech.locationbox.servlet;

public class DataDirection {
    private int linkId = 0;
    private String direction = null;
    private double distance = 0.00;
    private double duration = 0.00;

    public DataDirection() {
    }

    public DataDirection(int linkId, String direction, double distance, double duration) {
        this.linkId = linkId;
        this.direction = direction;
        this.distance = distance;
        this.duration = duration;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + linkId + ",\n";
        s += indent + "  \"direction\": \"" + (direction == null ? "" : direction) + "\",\n";
        s += indent + "  \"distance\": " + (int) (distance + 0.5) + ",\n";
        s += indent + "  \"duration\": " + (int) (duration + 0.5) + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "      <direction>\n";
        s += "        <id>" + linkId + "</id>\n";
        s += "        <explanation>" + (direction == null ? "" : Utils.convStr2Xml(direction)) + "</explanation>\n";
        s += "        <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "        <duration>" + (int) (duration + 0.5) + "</duration>\n";
        s += "      </direction>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public int getLinkId() {
        return linkId;
    }

    public void setLinkId(int linkId) {
        this.linkId = linkId;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

}
