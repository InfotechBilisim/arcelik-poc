package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataOptResult {
    private long mobile = 0;
    private long pointId = 0;
    private int jobOrder = 0;
    private int finishTime = 0;

    public DataOptResult() {
    }

    public DataOptResult(long mobile, long pointId, int jobOrder, int finishTime) {
        this.mobile = mobile;
        this.pointId = pointId;
        this.jobOrder = jobOrder;
        this.finishTime = finishTime;
    }

    //-----------------------------------------------------------------------------

    public static DataOptResult getInstance(ResultSet rset) {
        DataOptResult dor = new DataOptResult();

        try {
            dor.mobile = rset.getLong("MOBILE");
            dor.pointId = rset.getLong("POINTID");
            dor.jobOrder = rset.getInt("JOB_ORDER");
            dor.finishTime = rset.getInt("FINISH_TIME");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dor;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"mobile\": " + mobile + ",\n";
        s += indent + "  \"pointId\": " + pointId + "\n";
        s += indent + "  \"jobOrder\": " + jobOrder + "\n";
        s += indent + "  \"finishTime\": " + finishTime + "\n";
        s += indent + "}";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <item>\n";
        s += "      <mobile>" + mobile + "</mobile>\n";
        s += "      <pointid>" + pointId + "</pointid>\n";
        s += "      <joborder>" + jobOrder + "</joborder>\n";
        s += "      <finishtime>" + finishTime + "</finishtime>\n";
        s += "    </item>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setMobile(long mobile) {
        this.mobile = mobile;
    }

    public long getMobile() {
        return mobile;
    }

    public void setPointId(long pointId) {
        this.pointId = pointId;
    }

    public long getPointId() {
        return pointId;
    }

    public void setJobOrder(int jobOrder) {
        this.jobOrder = jobOrder;
    }

    public int getJobOrder() {
        return jobOrder;
    }

    public void setFinishTime(int finishTime) {
        this.finishTime = finishTime;
    }

    public int getFinishTime() {
        return finishTime;
    }

}
