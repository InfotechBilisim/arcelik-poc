package com.infotech.locationbox.servlet;


public class DataDistance {
    private DataPoint dpFrom = null;
    private DataPoint dpTo = null;
    private double distance = 0.00;

    public DataDistance() {
    }

    public DataDistance(DataPoint dpFrom, DataPoint dpTo, double distance) {
        this.dpFrom = dpFrom;
        this.dpTo = dpTo;
        this.distance = distance;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"frompoint\": {\n";
        s +=
            indent + "    \"name\": \"" + (dpFrom.getName() == null ? "" : Utils.convStr2Json(dpFrom.getName())) +
            "\",\n";
        s += indent + "    \"type\": " + dpFrom.getType() + ",\n";
        s += indent + "    \"latitude\": " + Utils.makeCoorFormat(dpFrom.getLatitude()) + ",\n";
        s += indent + "    \"longitude\": " + Utils.makeCoorFormat(dpFrom.getLongitude()) + "\n";
        s += indent + "  },\n";
        s += indent + "  \"topoint\": {\n";
        s += indent + "    \"name\": \"" + (dpTo.getName() == null ? "" : Utils.convStr2Json(dpTo.getName())) + "\",\n";
        s += indent + "    \"type\": " + dpTo.getType() + ",\n";
        s += indent + "    \"latitude\": " + Utils.makeCoorFormat(dpTo.getLatitude()) + ",\n";
        s += indent + "    \"longitude\": " + Utils.makeCoorFormat(dpTo.getLongitude()) + "\n";
        s += indent + "  },\n";
        s += indent + "  \"distance\": " + (int) (distance + 0.5) + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <distance>\n";
        s += "      <frompoint>\n";
        s += "        <name>" + (dpFrom.getName() == null ? "" : Utils.convStr2Xml(dpFrom.getName())) + "</name>\n";
        s += "        <type>" + dpFrom.getType() + "</type>\n";
        s += "        <latitude>" + Utils.makeCoorFormat(dpFrom.getLatitude()) + "</latitude>\n";
        s += "        <longitude>" + Utils.makeCoorFormat(dpFrom.getLongitude()) + "</longitude>\n";
        s += "      </frompoint>\n";
        s += "      <topoint>\n";
        s += "        <name>" + (dpTo.getName() == null ? "" : Utils.convStr2Xml(dpTo.getName())) + "</name>\n";
        s += "        <type>" + dpTo.getType() + "</type>\n";
        s += "        <latitude>" + Utils.makeCoorFormat(dpTo.getLatitude()) + "</latitude>\n";
        s += "        <longitude>" + Utils.makeCoorFormat(dpTo.getLongitude()) + "</longitude>\n";
        s += "      </topoint>\n";
        s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "    </distance>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setDpFrom(DataPoint dpFrom) {
        this.dpFrom = dpFrom;
    }

    public DataPoint getDpFrom() {
        return dpFrom;
    }

    public void setDpTo(DataPoint dpTo) {
        this.dpTo = dpTo;
    }

    public DataPoint getDpTo() {
        return dpTo;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

}
