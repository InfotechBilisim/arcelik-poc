package com.infotech.locationbox.servlet;

import java.sql.ResultSet;
import oracle.sql.STRUCT;

public class DataTrafficRoute {
    private long id = 0;
    private String fromName = null;
    private String toName = null;
    private String exp = null;
    private double distance = 0.00;
    private double duration = 0.00;
    private double delay = 0.00;
    private Extent extent = null;
    private double fromLatitude = 0.00;
    private double fromLongitude = 0.00;
    private double toLatitude = 0.00;
    private double toLongitude = 0.00;

    public DataTrafficRoute() {
        super();
    }

    //-----------------------------------------------------------------------------

    public static DataTrafficRoute getInstance(ResultSet rset) {
        DataTrafficRoute dtr = new DataTrafficRoute();

        try {
            dtr.id = rset.getLong("PATH_ID");
            dtr.fromName = rset.getString("FROM_NAME");
            dtr.toName = rset.getString("TO_NAME");
            dtr.exp = rset.getString("EXP");
            dtr.distance = rset.getDouble("DISTANCE");
            dtr.duration = rset.getDouble("DURATION");
            dtr.delay = rset.getDouble("DELAY");
            try {
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject("GEOMBR"));
                if (obj != null)
                    dtr.extent = Utils.getExtent(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
            dtr.fromLatitude = rset.getDouble("FROM_YCOOR");
            dtr.fromLongitude = rset.getDouble("FROM_XCOOR");
            dtr.toLatitude = rset.getDouble("TO_YCOOR");
            dtr.toLongitude = rset.getDouble("TO_XCOOR");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dtr;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";

        s += indent + "{\n";
        s += indent + "  \"id\": " + id + ",\n";
        s += indent + "  \"fromname\": \"" + Utils.convStr2Json(fromName) + "\",\n";
        s += indent + "  \"toname\": \"" + Utils.convStr2Json(toName) + "\",\n";
        s += indent + "  \"exp\": \"" + Utils.convStr2Json(exp) + "\",\n";
        s += indent + "  \"distance\": " + (int) (distance + 0.5) + ",\n";
        s += indent + "  \"duration\": " + (int) (duration + 0.5) + ",\n";
        s += indent + "  \"delay\": " + (int) (delay + 0.5);
        if (extent != null) {
            s += ",\n";
            s +=
                indent + "  \"extent\": { \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) +
                ", \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ", \"maxlatitude\": " +
                Utils.makeCoorFormat(extent.getMaxLatitude()) + ", \"maxlongitude\": " +
                Utils.makeCoorFormat(extent.getMaxLongitude()) + " }";

        }
        s += ",\n";
        s +=
            indent + "  \"frompoint\": { \"latitude\": " + Utils.makeCoorFormat(fromLatitude) + ", \"longitude\": " +
            Utils.makeCoorFormat(fromLongitude) + " }";
        s += ",\n";
        s +=
            indent + "  \"topoint\": { \"latitude\": " + Utils.makeCoorFormat(toLatitude) + ", \"longitude\": " +
            Utils.makeCoorFormat(toLongitude) + " }";
        s += "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <trafficroute>\n";
        s += "      <id>" + id + "</id>\n";
        s += "      <fromname>" + Utils.convStr2Xml(fromName) + "</fromname>\n";
        s += "      <toname>" + Utils.convStr2Xml(toName) + "</toname>\n";
        s += "      <exp>" + Utils.convStr2Xml(exp) + "</exp>\n";
        s += "      <distance>" + (int) (distance + 0.5) + "</distance>\n";
        s += "      <duration>" + (int) (duration + 0.5) + "</duration>\n";
        s += "      <delay>" + (int) (delay + 0.5) + "</delay>\n";
        if (extent != null) {
            s += "      <extent>\n";
            s += "        <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n";
            s += "        <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
            s += "        <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
            s += "        <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
            s += "      </extent>\n";
        }
        s += "      <frompoint>\n";
        s += "        <latitude>" + Utils.makeCoorFormat(fromLatitude) + "</latitude>\n";
        s += "        <longitude>" + Utils.makeCoorFormat(fromLongitude) + "</longitude>\n";
        s += "      </frompoint>\n";
        s += "      <topoint>\n";
        s += "        <latitude>" + Utils.makeCoorFormat(toLatitude) + "</latitude>\n";
        s += "        <longitude>" + Utils.makeCoorFormat(toLongitude) + "</longitude>\n";
        s += "      </topoint>\n";

        s += "    </trafficroute>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getToName() {
        return toName;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }

    public String getExp() {
        return exp;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public double getDistance() {
        return distance;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public double getDuration() {
        return duration;
    }

    public void setDelay(double delay) {
        this.delay = delay;
    }

    public double getDelay() {
        return delay;
    }

    public void setFromLatitude(double fromLatitude) {
        this.fromLatitude = fromLatitude;
    }

    public double getFromLatitude() {
        return fromLatitude;
    }

    public void setFromLongitude(double fromLongitude) {
        this.fromLongitude = fromLongitude;
    }

    public double getFromLongitude() {
        return fromLongitude;
    }

    public void setToLatitude(double toLatitude) {
        this.toLatitude = toLatitude;
    }

    public void setToLongitude(double toLongitude) {
        this.toLongitude = toLongitude;
    }

    public double getToLongitude() {
        return toLongitude;
    }

}
