package com.infotech.locationbox.servlet;

public class DataSpatialAnalysis {
    protected DataPoi[] pois = null;
    protected DataUserPoint[] userPoints = null;
    protected String errCode = "0";
    protected String errDesc = "";

    public DataSpatialAnalysis() {
    }

    public DataSpatialAnalysis(String errCode, String errDesc) {
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    //-----------------------------------------------------------------------------

    public void setPois(DataPoi[] pois) {
        this.pois = pois;
    }

    public DataPoi[] getPois() {
        return pois;
    }

    public void setUserPoints(DataUserPoint[] userPoints) {
        this.userPoints = userPoints;
    }

    public DataUserPoint[] getUserPoints() {
        return userPoints;
    }

}
