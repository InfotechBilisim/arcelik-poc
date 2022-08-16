package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataTrafficInfo {
    private long id = 0;
    private int speed = 0;
    private int speedLimit = 0;

    public DataTrafficInfo() {
    }

    //-----------------------------------------------------------------------------

    public static DataTrafficInfo getInstance(ResultSet rset) {
        DataTrafficInfo dti = new DataTrafficInfo();

        try {
            dti.id = rset.getLong("ID");
            dti.speed = rset.getInt("SPEED");
            dti.speedLimit = rset.getInt("SPEED_LIMIT");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dti;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";

        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"speed\": " + speed + ",\n";
        s += indent + "  \"speedlimit\": " + speedLimit + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <info>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <speed>" + speed + "</speed>\n";
        s += "      <speedlimit>" + speedLimit + "</speedlimit>\n";
        s += "    </info>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

}
