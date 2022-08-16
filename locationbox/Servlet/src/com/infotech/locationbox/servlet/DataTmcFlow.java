package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataTmcFlow {
    private String tmcCode = null;
    private double travelTime = 0.00;
    private int speed = 0;
    private double delay = 0.00;
    private double optimalTravelTime = 0.00;
    private int optimalSpeed = 0;
    private long lengthMeter = 0;
    private long linkInJam = 0;

    public DataTmcFlow() {
    }

    //-----------------------------------------------------------------------------

    public static DataTmcFlow getInstance(ResultSet rset) {
        DataTmcFlow dtf = new DataTmcFlow();
        try {
            dtf.tmcCode = rset.getString(1);
            dtf.travelTime = rset.getDouble(2);
            dtf.speed = rset.getInt(3);
            dtf.delay = rset.getDouble(4);
            dtf.optimalTravelTime = rset.getDouble(5);
            dtf.optimalSpeed = rset.getInt(6);
            dtf.lengthMeter = rset.getLong(7);
            dtf.linkInJam = rset.getLong(8);
            return dtf;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";

        s += indent + "{\n";
        s += indent + "  \"tmccode\": " + tmcCode + ",\n";
        s += indent + "  \"traveltime\": " + travelTime + ",\n";
        s += indent + "  \"speed\": " + speed + ",\n";
        s += indent + "  \"delay\": " + delay + ",\n";
        s += indent + "  \"optimaltraveltime\": " + optimalTravelTime + ",\n";
        s += indent + "  \"optimalspeed\": " + optimalSpeed + ",\n";
        s += indent + "  \"lengthmeter\": " + lengthMeter + ",\n";
        s += indent + "  \"linkinjam\": " + linkInJam + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <info>\n";
        s += "      <tmccode>" + tmcCode + "</tmccode>\n";
        s += "      <traveltime>" + travelTime + "</traveltime>\n";
        s += "      <speed>" + speed + "</speed>\n";
        s += "      <delay>" + delay + "</delay>\n";
        s += "      <optimaltraveltime>" + optimalTravelTime + "</optimaltraveltime>\n";
        s += "      <optimalspeed>" + optimalSpeed + "</optimalspeed>\n";
        s += "      <lengthmeter>" + lengthMeter + "</lengthmeter>\n";
        s += "      <linkinjam>" + linkInJam + "</linkinjam>\n";
        s += "    </info>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setTmcCode(String tmcCode) {
        this.tmcCode = tmcCode;
    }

    public String getTmcCode() {
        return tmcCode;
    }

    public void setTravelTime(double travelTime) {
        this.travelTime = travelTime;
    }

    public double getTravelTime() {
        return travelTime;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public double getDelay() {
        return delay;
    }

    public void setOptimalTravelTime(double optimalTravelTime) {
        this.optimalTravelTime = optimalTravelTime;
    }

    public double getOptimalTravelTime() {
        return optimalTravelTime;
    }

    public void setOptimalSpeed(int optimalSpeed) {
        this.optimalSpeed = optimalSpeed;
    }

    public int getOptimalSpeed() {
        return optimalSpeed;
    }

    public void setLengthMeter(long lengthMeter) {
        this.lengthMeter = lengthMeter;
    }

    public long getLengthMeter() {
        return lengthMeter;
    }

    public void setLinkInJam(long linkInJam) {
        this.linkInJam = linkInJam;
    }

    public long getLinkInJam() {
        return linkInJam;
    }

}
