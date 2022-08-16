package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataSocialEvent {
    private long id = 0;
    private String name = null;
    private String description = null;
    private String category = null;
    private String location = null;
    private String startDate = null;
    private String endDate = null;
    private String timeZone = null;
    private double latitude = 0.00;
    private double longitude = 0.00;
    protected double distance = -1.00;

    public DataSocialEvent() {
    }

    //-----------------------------------------------------------------------------

    public static DataSocialEvent getInstance(ResultSet rset) {
        DataSocialEvent dse = new DataSocialEvent();
        try {
            dse.id = rset.getLong("ID");
            dse.name = rset.getString("NAME");
            dse.description = rset.getString("DESCRIPTION");
            dse.category = rset.getString("EVENT_CATEGORY_ID");
            dse.location = rset.getString("LOCATION");
            dse.startDate = rset.getString("START_DATE");
            dse.endDate = rset.getString("END_DATE");
            dse.timeZone = rset.getString("TIMEZONE");
            dse.longitude = rset.getDouble("XCOOR");
            dse.latitude = rset.getDouble("YCOOR");
            try {
                dse.distance = rset.getDouble("DISTANCE");
            } catch (Exception e) {
                ;
            }
            return dse;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"name\": \"" + Utils.convStr2Json(name) + "\",\n";
        s += indent + "  \"description\": \"" + Utils.convStr2Json(description) + "\",\n";
        s += indent + "  \"category\": " + category + ",\n";
        s += indent + "  \"location\": \"" + Utils.convStr2Json(location) + "\",\n";
        s += indent + "  \"startdate\": \"" + (startDate == null ? "" : startDate) + "\",\n";
        s += indent + "  \"enddate\": \"" + (endDate == null ? "" : endDate) + "\",\n";
        s += indent + "  \"timezone\": \"" + Utils.convStr2Json(timeZone) + "\",\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude);
        if (distance >= 0) {
            s += ",\n";
            s += indent + "  \"distance\": " + (int) (distance + 0.5);
        }
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <socialevent>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + Utils.convStr2Xml(name) + "</name>\n";
        s += "      <description>" + Utils.convStr2Xml(description) + "</description>\n";
        s += "      <category>" + Utils.convStr2Xml(category) + "</category>\n";
        s += "      <location>" + Utils.convStr2Xml(location) + "</location>\n";
        s += "      <startdate>" + (startDate == null ? "" : startDate) + "</startdate>\n";
        s += "      <enddate>" + (endDate == null ? "" : endDate) + "</enddate>\n";
        s += "      <timezone>" + Utils.convStr2Xml(timeZone) + "</timezone>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        if (distance >= 0) {
            s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
        }
        s += "    </socialevent>\n";
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

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategory() {
        return category;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getTimeZone() {
        return timeZone;
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

}
