package com.infotech.locationbox.servlet;

public class DataSuggestion {
    private long id = 0;
    private String fromTable = null;
    private String name = null;
    private double latitude = 0.00;
    private double longitude = 0.00;
    private double distance = 0.00;

    public DataSuggestion() {
    }

    public DataSuggestion(long id, String fromTable, String name, double latitude, double longitude, double distance) {
        this.id = id;
        this.fromTable = fromTable;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.distance = distance;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
      StringBuilder s = new StringBuilder();
      s.append(indent + "{\n");
      s.append( indent + "  \"id\": " + id + ",\n");
      s.append( indent + "  \"fromtable\": \"" + Utils.convStr2Json(fromTable) + "\",\n");
      s.append( indent + "  \"name\": \"" + Utils.convStr2Json(name) + "\",\n");
      s.append( indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n");
      s.append( indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude) + ",\n");
      s.append(indent + "  \"distance\": " + (int) (distance + 0.5) + "\n");
      s.append(indent + "}");
       return s.toString();
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
       StringBuilder s = new StringBuilder();
       s.append("<suggestion>\n");
       s.append("      <id>" + id + "</id>\n");
       s.append("      <fromtable>" + Utils.convStr2Xml(fromTable) + "</fromtable>\n");
       s.append("      <name>" + Utils.convStr2Xml(name) + "</name>\n");
       s.append("      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n");
       s.append("      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n");
       s.append("      <distance>" + (int) (distance + 0.5) + "</distance>\n");
       s.append("    </suggestion>\n");
        return s.toString();
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFromTable(String fromTable) {
        this.fromTable = fromTable;
    }

    public String getFromTable() {
        return fromTable;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
