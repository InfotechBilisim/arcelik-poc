package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataTrafficEvent {
    private long id = 0;
    private String name = null;
    private int code = 0;
    private int typ = 0;
    private double latitude = 0.00;
    private double longitude = 0.00;
    private int delay = 0;
    private int speed = 0;
    private String roadName = null;
    private String startName = null;
    private String endName = null;
    private double distance = 0.00;

    public DataTrafficEvent() {
    }

    //-----------------------------------------------------------------------------

    public static DataTrafficEvent getInstance(ResultSet rset) {
        DataTrafficEvent de = new DataTrafficEvent();

        try {
            de.id = rset.getLong("ID");
            de.name = rset.getString("EVENT_DESC");
            de.code = rset.getInt("EVENT_CODE");
            de.typ = rset.getInt("ICON_CODE");
            de.latitude = rset.getDouble("PRIMARY_LATITUDE");
            de.longitude = rset.getDouble("PRIMARY_LONGITUDE");
            de.delay = rset.getInt("DELAY");
            de.speed = rset.getInt("SPEED");
            de.roadName = rset.getString("TMC_ROAD_NAME");
            de.startName = rset.getString("TMC_START_POINT_NAME");
            de.endName = rset.getString("TMC_END_POINT_NAME");
            de.distance = rset.getDouble("DISTANCE");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return de;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";

        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
        s += indent + "  \"code\": " + code + ",\n";
        s += indent + "  \"type\": " + typ + ",\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude) + ",\n";
        s += indent + "  \"delay\": " + delay + ",\n";
        s += indent + "  \"speed\": " + speed + ",\n";
        s += indent + "  \"roadname\": \"" + (roadName == null ? "" : Utils.convStr2Xml(roadName)) + "\",";
        s += indent + "  \"startingpoint\": \"" + (startName == null ? "" : Utils.convStr2Xml(startName)) + "\",";
        s += indent + "  \"endingpoint\": \"" + (endName == null ? "" : Utils.convStr2Xml(endName)) + "\",";
        s += indent + "  \"distance\": " + (int) (distance + 0.5);
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <event>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
        s += "      <code>" + code + "</code>\n";
        s += "      <type>" + typ + "</type>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        s += "      <delay>" + delay + "</delay>\n";
        s += "      <speed>" + speed + "</speed>\n";
        s += "      <roadname>" + (roadName == null ? "" : Utils.convStr2Xml(roadName)) + "</roadname>\n";
        s += "      <startingpoint>" + (startName == null ? "" : Utils.convStr2Xml(startName)) + "</startingpoint>\n";
        s += "      <endingpoint>" + (endName == null ? "" : Utils.convStr2Xml(endName)) + "</endingpoint>\n";
        s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "    </event>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public int getTyp() {
        return typ;
    }

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

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public int getDelay() {
        return delay;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setRoadName(String roadName) {
        this.roadName = roadName;
    }

    public String getRoadName() {
        return roadName;
    }

    public void setStartName(String startName) {
        this.startName = startName;
    }

    public String getStartName() {
        return startName;
    }

    public void setEndName(String endName) {
        this.endName = endName;
    }

    public String getEndName() {
        return endName;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

}
