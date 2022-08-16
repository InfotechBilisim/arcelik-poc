package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.spatial.geometry.JGeometry;
import oracle.sql.STRUCT;

public class DataTmcHat {
    private String tmcCode = null;
    private String frc = null;
    private int speedLimit = 0;
    private JGeometry[] objs = null;
    private boolean reverse = false;
    private boolean encode = false;

    public DataTmcHat() {
    }

    public DataTmcHat(String tmcCode, String frc, int speedLimit) {
        this.tmcCode = tmcCode;
        this.frc = frc;
        this.speedLimit = speedLimit;
    }

    //-----------------------------------------------------------------------------

    public static DataTmcHat getInstance(ResultSet rset, boolean withCoors, boolean reverse, boolean encode) {
        DataTmcHat dth = new DataTmcHat();
        try {
            dth.tmcCode = rset.getString(1);
            dth.frc = rset.getString(2);
            dth.speedLimit = rset.getInt(3);
            dth.reverse = reverse;
            if (withCoors) {
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(4));
                JGeometry geo = JGeometry.load(obj);
                dth.objs = geo.getElements();
                dth.encode = encode;
            }
            return dth;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getInstance()

    //----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"tmccode\": \"" + tmcCode + "\",\n";
        s += indent + "  \"frc\": \"" + frc + "\",\n";
        s += indent + "  \"speedlimit\": " + speedLimit;
        if (objs != null) {
            s += ",\n";
            s += indent + "  \"geometry\": [\n";
            double[] oarray = null;
            for (int i = 0; i < objs.length; i++) {
                double[] narray = objs[i].getOrdinatesArray();
                if (oarray == null)
                    oarray = narray;
                else {
                    if (oarray.length < narray.length)
                        oarray = narray;
                }
            } // for()
            if (reverse) {
                double tmp = 0.00;
                int length = (oarray.length >> 1);
                for (int i = 0; i < (length >> 1); i++) {
                    int begOffs = 2 * i;
                    int endOffs = 2 * (length - 1 - i);
                    tmp = oarray[begOffs];
                    oarray[begOffs] = oarray[endOffs];
                    oarray[endOffs] = tmp;
                    tmp = oarray[begOffs + 1];
                    oarray[begOffs + 1] = oarray[endOffs + 1];
                    oarray[endOffs + 1] = tmp;
                } // for()
            }
            if (encode) {
                String pline = PolylineUtils.encodePolyline(oarray);
                s += indent + "    \"" + pline + "\"\n";
            } else {
                StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                for (int i = 0; i < oarray.length; i++) {
                    if (i > 0)
                        sbuf.append(',');
                    sbuf.append(oarray[i]);
                } // for(i)
                s += indent + "    " + sbuf.toString() + "\n";
            }
            s += indent + "  ]";
        } // if(objs)
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "  <tmchat>\n";
        s += "    <tmccode>" + tmcCode + "</tmccode>\n";
        s += "    <frc>" + frc + "</frc>\n";
        s += "    <speedlimit>" + speedLimit + "</speedlimit>\n";
        if (objs != null) {
            s += "    <geometry>\n";
            double[] oarray = null;
            for (int i = 0; i < objs.length; i++) {
                double[] narray = objs[i].getOrdinatesArray();
                if (oarray == null)
                    oarray = narray;
                else {
                    if (oarray.length < narray.length)
                        oarray = narray;
                }
            } // for()
            if (reverse) {
                double tmp = 0.00;
                int length = (oarray.length >> 1);
                for (int i = 0; i < (length >> 1); i++) {
                    int begOffs = 2 * i;
                    int endOffs = 2 * (length - 1 - i);
                    tmp = oarray[begOffs];
                    oarray[begOffs] = oarray[endOffs];
                    oarray[endOffs] = tmp;
                    tmp = oarray[begOffs + 1];
                    oarray[begOffs + 1] = oarray[endOffs + 1];
                    oarray[endOffs + 1] = tmp;
                } // for()
            }
            if (encode) {
                String pline = PolylineUtils.encodePolyline(oarray);
                s += "        <ordinates>" + pline + "</ordinates>\n";
            } else {
                StringBuilder sbuf = new StringBuilder(oarray.length * 12);
                for (int i = 0; i < oarray.length; i++) {
                    if (i > 0)
                        sbuf.append(',');
                    sbuf.append(oarray[i]);
                } // for(i)
                s += "      <ordinates>" + sbuf.toString() + "</ordinates>\n";
            }
            s += "    </geometry>\n";
        } // if(objs)
        s += "  </tmchat>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setTmcCode(String tmcCode) {
        this.tmcCode = tmcCode;
    }

    public String getTmcCode() {
        return tmcCode;
    }

    public void setFrc(String frc) {
        this.frc = frc;
    }

    public String getFrc() {
        return frc;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

}
