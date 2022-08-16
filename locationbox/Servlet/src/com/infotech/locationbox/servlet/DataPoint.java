package com.infotech.locationbox.servlet;

public class DataPoint {
    private String id = null;
    private String name = null;
    private int type = 0;
    private double latitude = 0.00;
    private double longitude = 0.00;
    private double distance = 0.00;

    public DataPoint() {
    }

    public DataPoint(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataPoint(String id, String name, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataPoint(int type, double latitude, double longitude) {
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataPoint(String name, int type, double latitude, double longitude) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DataPoint(String name, int type, double latitude, double longitude, double distance) {
        this.name = name;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }
    
    public DataPoint(String name, int type, float latitude, float longitude, double distance) {
      this.name = name;
      this.type = type;
      this.latitude = latitude;
      this.longitude = longitude;
      this.distance = distance;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
        s += indent + "  \"type\": " + type + ",\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude) + ",\n";
        s += indent + "  \"distance\": " + (int) (distance + 0.5) + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <point>\n";
        s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
        s += "      <type>" + type + "</type>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "    </point>\n";
        return s;
    } // toXml()

    //----------------------------------------------------------------------------

    public boolean isCoorsShuffled() {
        if (latitude <= 90.0 && longitude <= 180.0)
            return false;

        return true;
    } // isCoorsShuffled()

    //----------------------------------------------------------------------------

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
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

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

}
