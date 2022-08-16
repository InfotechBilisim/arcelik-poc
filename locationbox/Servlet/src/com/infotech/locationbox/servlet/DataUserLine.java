package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class DataUserLine {
    protected String id = null;
    protected String name = null;
    protected int type = 0;
    protected String string_1 = null;
    protected String string_2 = null;
    protected String string_3 = null;
    protected String string_4 = null;
    protected String string_5 = null;
    protected String string_6 = null;
    protected String string_7 = null;
    protected String string_8 = null;
    protected String string_9 = null;
    protected double number_1 = 0;
    protected double number_2 = 0;
    protected double number_3 = 0;
    protected double number_4 = 0;
    protected double number_5 = 0;
    protected double number_6 = 0;
    protected double number_7 = 0;
    protected double number_8 = 0;
    protected double number_9 = 0;
    protected double latitude = 0.00;
    protected double longitude = 0.00;
    protected Extent extent = null;
    protected double[] oarray = null;

    public DataUserLine() {
    }

    //-----------------------------------------------------------------------------

    public static DataUserLine getInstance(ResultSet rset, boolean withCoors) {
        DataUserLine dul = new DataUserLine();
        try {
            dul.id = rset.getString("LINE_ID");
            dul.name = rset.getString("LINE_NAME");
            dul.type = rset.getInt("TYP");
            dul.string_1 = rset.getString("STRING_1");
            dul.string_2 = rset.getString("STRING_2");
            dul.string_3 = rset.getString("STRING_3");
            dul.string_4 = rset.getString("STRING_4");
            dul.string_5 = rset.getString("STRING_5");
            dul.string_6 = rset.getString("STRING_6");
            dul.string_7 = rset.getString("STRING_7");
            dul.string_8 = rset.getString("STRING_8");
            dul.string_9 = rset.getString("STRING_9");
            dul.number_1 = rset.getDouble("NUMBER_1");
            dul.number_2 = rset.getDouble("NUMBER_2");
            dul.number_3 = rset.getDouble("NUMBER_3");
            dul.number_4 = rset.getDouble("NUMBER_4");
            dul.number_5 = rset.getDouble("NUMBER_5");
            dul.number_6 = rset.getDouble("NUMBER_6");
            dul.number_7 = rset.getDouble("NUMBER_7");
            dul.number_8 = rset.getDouble("NUMBER_8");
            dul.number_9 = rset.getDouble("NUMBER_9");
            dul.latitude = rset.getDouble("YCOOR");
            dul.longitude = rset.getDouble("XCOOR");
            STRUCT obj = DbConn.convToSTRUCT(rset.getObject("GEOMBR"));
            dul.extent = Utils.getExtent(obj);
            if (withCoors) {
                obj = DbConn.convToSTRUCT(rset.getObject("GEOLOC"));
                JGeometry geo = JGeometry.load(obj);
                dul.oarray = geo.getOrdinatesArray();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dul;
    } // getInstance()

    //-----------------------------------------------------------------------------

    private String formatString(String txt) {
        if (txt == null)
            return "";

        return txt;
    } // formatString()

    //-----------------------------------------------------------------------------

    private String formatNumber(double num) {
        long lnum = (long) num;
        if ((num - lnum) == 0.00)
            return "" + lnum;

        return "" + num;
    } // formatNumber()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"id\": \"" + id + "\",\n";
        s += indent + "  \"name\": \"" + (name == null ? "" : Utils.convStr2Json(name)) + "\",\n";
        s += indent + "  \"type\": " + type + ",\n";
        s += indent + "  \"string1\": \"" + formatString(string_1) + "\",\n";
        s += indent + "  \"string2\": \"" + formatString(string_2) + "\",\n";
        s += indent + "  \"string3\": \"" + formatString(string_3) + "\",\n";
        s += indent + "  \"string4\": \"" + formatString(string_4) + "\",\n";
        s += indent + "  \"string5\": \"" + formatString(string_5) + "\",\n";
        s += indent + "  \"string6\": \"" + formatString(string_6) + "\",\n";
        s += indent + "  \"string7\": \"" + formatString(string_7) + "\",\n";
        s += indent + "  \"string8\": \"" + formatString(string_8) + "\",\n";
        s += indent + "  \"string9\": \"" + formatString(string_9) + "\",\n";
        s += indent + "  \"number1\": " + formatNumber(number_1) + ",\n";
        s += indent + "  \"number2\": " + formatNumber(number_2) + ",\n";
        s += indent + "  \"number3\": " + formatNumber(number_3) + ",\n";
        s += indent + "  \"number4\": " + formatNumber(number_4) + ",\n";
        s += indent + "  \"number5\": " + formatNumber(number_5) + ",\n";
        s += indent + "  \"number6\": " + formatNumber(number_6) + ",\n";
        s += indent + "  \"number7\": " + formatNumber(number_7) + ",\n";
        s += indent + "  \"number8\": " + formatNumber(number_8) + ",\n";
        s += indent + "  \"number9\": " + formatNumber(number_9) + ",\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(latitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(longitude) + ",\n";
        s += indent + "  \"extent\": {\n";
        if (extent != null) {
            s += indent + "    \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) + ",\n";
            s += indent + "    \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ",\n";
            s += indent + "    \"maxlatitude\": " + Utils.makeCoorFormat(extent.getMaxLatitude()) + ",\n";
            s += indent + "    \"maxlongitude\": " + Utils.makeCoorFormat(extent.getMaxLongitude()) + "\n";
        }
        s += indent + "  }";
        if (oarray != null) {
            s += ",\n";
            StringBuffer coors = new StringBuffer();
            for (int i = 0; i < oarray.length; i++) {
                if (i > 0)
                    coors.append(',');
                coors.append(oarray[i]);
            } // for()
            s += indent + "  \"coors\": \"" + coors.toString() + "\"";
        }
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <userline>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <name>" + (name == null ? "" : Utils.convStr2Xml(name)) + "</name>\n";
        s += "      <type>" + type + "</type>\n";
        s += "      <string1>" + formatString(string_1) + "</string1>\n";
        s += "      <string2>" + formatString(string_2) + "</string2>\n";
        s += "      <string3>" + formatString(string_3) + "</string3>\n";
        s += "      <string4>" + formatString(string_4) + "</string4>\n";
        s += "      <string5>" + formatString(string_5) + "</string5>\n";
        s += "      <string6>" + formatString(string_6) + "</string6>\n";
        s += "      <string7>" + formatString(string_7) + "</string7>\n";
        s += "      <string8>" + formatString(string_8) + "</string8>\n";
        s += "      <string9>" + formatString(string_9) + "</string9>\n";
        s += "      <number1>" + formatNumber(number_1) + "</number1>\n";
        s += "      <number2>" + formatNumber(number_2) + "</number2>\n";
        s += "      <number3>" + formatNumber(number_3) + "</number3>\n";
        s += "      <number4>" + formatNumber(number_4) + "</number4>\n";
        s += "      <number5>" + formatNumber(number_5) + "</number5>\n";
        s += "      <number6>" + formatNumber(number_6) + "</number6>\n";
        s += "      <number7>" + formatNumber(number_7) + "</number7>\n";
        s += "      <number8>" + formatNumber(number_8) + "</number8>\n";
        s += "      <number9>" + formatNumber(number_9) + "</number9>\n";
        s += "      <latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
        s += "      <longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
        s += "      <extent>\n";
        if (extent != null) {
            s += "        <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n";
            s += "        <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
            s += "        <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
            s += "        <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
        }
        s += "      </extent>\n";
        if (oarray != null) {
            StringBuffer coors = new StringBuffer();
            for (int i = 0; i < oarray.length; i++) {
                if (i > 0)
                    coors.append(',');
                coors.append(oarray[i]);
            } // for()
            s += "      <coors>" + coors.toString() + "</coors>\n";
        }
        s += "    </userline>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setString_1(String string_1) {
        this.string_1 = string_1;
    }

    public String getString_1() {
        return string_1;
    }

    public void setString_2(String string_2) {
        this.string_2 = string_2;
    }

    public String getString_2() {
        return string_2;
    }

    public void setString_3(String string_3) {
        this.string_3 = string_3;
    }

    public String getString_3() {
        return string_3;
    }

    public void setString_4(String string_4) {
        this.string_4 = string_4;
    }

    public String getString_4() {
        return string_4;
    }

    public void setString_5(String string_5) {
        this.string_5 = string_5;
    }

    public String getString_5() {
        return string_5;
    }

    public void setString_6(String string_6) {
        this.string_6 = string_6;
    }

    public String getString_6() {
        return string_6;
    }

    public void setString_7(String string_7) {
        this.string_7 = string_7;
    }

    public String getString_7() {
        return string_7;
    }

    public void setString_8(String string_8) {
        this.string_8 = string_8;
    }

    public String getString_8() {
        return string_8;
    }

    public void setString_9(String string_9) {
        this.string_9 = string_9;
    }

    public String getString_9() {
        return string_9;
    }

    public void setNumber_1(double number_1) {
        this.number_1 = number_1;
    }

    public double getNumber_1() {
        return number_1;
    }

    public void setNumber_2(double number_2) {
        this.number_2 = number_2;
    }

    public double getNumber_2() {
        return number_2;
    }

    public void setNumber_3(double number_3) {
        this.number_3 = number_3;
    }

    public double getNumber_3() {
        return number_3;
    }

    public void setNumber_4(double number_4) {
        this.number_4 = number_4;
    }

    public double getNumber_4() {
        return number_4;
    }

    public void setNumber_5(double number_5) {
        this.number_5 = number_5;
    }

    public double getNumber_5() {
        return number_5;
    }

    public void setNumber_6(double number_6) {
        this.number_6 = number_6;
    }

    public double getNumber_6() {
        return number_6;
    }

    public void setNumber_7(double number_7) {
        this.number_7 = number_7;
    }

    public double getNumber_7() {
        return number_7;
    }

    public void setNumber_8(double number_8) {
        this.number_8 = number_8;
    }

    public double getNumber_8() {
        return number_8;
    }

    public void setNumber_9(double number_9) {
        this.number_9 = number_9;
    }

    public double getNumber_9() {
        return number_9;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setExtent(Extent extent) {
        this.extent = extent;
    }

    public Extent getExtent() {
        return extent;
    }

    public void setOarray(double[] oarray) {
        this.oarray = oarray;
    }

    public double[] getOarray() {
        return oarray;
    }

}
