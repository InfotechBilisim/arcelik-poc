package com.infotech.locationbox.servlet;


public class DataTollRoad {
    private long id = 0;
    private String name = null;
    private double value = 0.00;

    public DataTollRoad() {
    }

    public DataTollRoad(long id, String name, double value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"name\": \"" + name + "\",\n";
        s += indent + "  \"value\": " + value + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <tollroad>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + name + "</name>\n";
        s += "      <value>" + value + "</value>\n";
        s += "    </tollroad>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

}
