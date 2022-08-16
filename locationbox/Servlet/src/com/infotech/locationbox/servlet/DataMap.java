package com.infotech.locationbox.servlet;

import oracle.spatial.geometry.JGeometry;


public class DataMap {
    private double centerLatitude = 0.00;
    private double centerLongitude = 0.00;
    private int zoomLevel = 0;
    private Extent extent = null;
    private int width = 0;
    private int height = 0;

    private String url = null;
    private DataPoint[] points = null;

    private String userData = null;
    private String userDataId = null;
    private int userDataStyle = 0;
    private int baseMap = 0;
    private int pan = 0;

    private double minLatitude;
    private double minLongitude;
    private double maxLatitude;
    private double maxLongitude;
    private double[] userCoors;
    
    private JGeometry geo;


    public DataMap() {
    }

    public DataMap(double latitude, double longitude, int zoomLevel, Extent extent, int width, int height, String url, DataPoint[] points) {
        this.centerLatitude = latitude;
        this.centerLongitude = longitude;
        this.zoomLevel = zoomLevel;
        this.extent = extent;
        this.width = width;
        this.height = height;
        this.url = url;
        this.points = points;
    }

    public DataMap(double latitude, double longitude, int zoomLevel, int pan, Extent extent, int width, int height, String url, DataPoint[] points) {
        this.centerLatitude = latitude;
        this.centerLongitude = longitude;
        this.zoomLevel = zoomLevel;
        this.pan = pan;
        this.extent = extent;
        this.width = width;
        this.height = height;
        this.url = url;
        this.points = points;
    }

    public DataMap(double centerLatitude, double centerLongitude, int zoomLevel, int pan, Extent extent, int width, int height, String url, DataPoint[] points, String userData, String userDataId,
                   int userDataStyle, int baseMap, double[] userCoors) {
        this.centerLatitude = centerLatitude;
        this.centerLongitude = centerLongitude;
        this.zoomLevel = zoomLevel;
        this.extent = extent;
        this.width = width;
        this.height = height;
        this.url = url;
        this.points = points;
        this.userData = userData;
        this.userDataId = userDataId;
        this.userDataStyle = userDataStyle;
        this.baseMap = baseMap;
        this.pan = pan;
        this.userCoors = userCoors;

    }

    public DataMap(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, int zoomLevel, int pan, Extent extent, int width, int height, String url, DataPoint[] points,
                   String userData, String userDataId, int userDataStyle, int baseMap, double[] userCoors) {
        this.minLatitude = minLatitude;
        this.minLongitude = minLongitude;
        this.maxLatitude = maxLatitude;
        this.maxLongitude = maxLongitude;
        this.zoomLevel = zoomLevel;
        this.extent = extent;
        this.width = width;
        this.height = height;
        this.url = url;
        this.points = points;
        this.userData = userData;
        this.userDataId = userDataId;
        this.userDataStyle = userDataStyle;
        this.baseMap = baseMap;
        this.pan = pan;
        this.userCoors = userCoors;
    }


    public void setUserCoors(double[] userCoors) {
        this.userCoors = userCoors;
    }

    public double[] getUserCoors() {
        return userCoors;
    }

    public void setMinLatitude(double minLatitude) {
        this.minLatitude = minLatitude;
    }

    public double getMinLatitude() {
        return minLatitude;
    }

    public void setMinLongitude(double minLongitude) {
        this.minLongitude = minLongitude;
    }

    public double getMinLongitude() {
        return minLongitude;
    }

    public void setMaxLatitude(double maxLatitude) {
        this.maxLatitude = maxLatitude;
    }

    public double getMaxLatitude() {
        return maxLatitude;
    }

    public void setMaxLongitude(double maxLongitude) {
        this.maxLongitude = maxLongitude;
    }

    public double getMaxLongitude() {
        return maxLongitude;
    }

    public void setPan(int pan) {
        this.pan = pan;
    }

    public int getPan() {
        return pan;
    }

    public void setBaseMap(int baseMap) {
        this.baseMap = baseMap;
    }

    public int getBaseMap() {
        return baseMap;
    }

    public void setUserData(String userData) {
        this.userData = userData;
    }

    public String getUserData() {
        return userData;
    }

    public void setUserDataId(String userDataId) {
        this.userDataId = userDataId;
    }

    public String getUserDataId() {
        return userDataId;
    }

    public void setUserDataStyle(int userDataStyle) {
        this.userDataStyle = userDataStyle;
    }

    public int getUserDataStyle() {
        return userDataStyle;
    }

    public void setCenterLatitude(double centerLatitude) {
        this.centerLatitude = centerLatitude;
    }

    public double getCenterLatitude() {
        return centerLatitude;
    }

    public void setCenterLongitude(double centerLongitude) {
        this.centerLongitude = centerLongitude;
    }

    public double getCenterLongitude() {
        return centerLongitude;
    }
    //----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"latitude\": " + Utils.makeCoorFormat(centerLatitude) + ",\n";
        s += indent + "  \"longitude\": " + Utils.makeCoorFormat(centerLongitude) + ",\n";
        s += indent + "  \"zoomlevel\": " + zoomLevel + ",\n";
        s += indent + "  \"extent\": {\n";
        s += indent + "    \"minlatitude\": " + Utils.makeCoorFormat(extent.getMinLatitude()) + ",\n";
        s += indent + "    \"minlongitude\": " + Utils.makeCoorFormat(extent.getMinLongitude()) + ",\n";
        s += indent + "    \"maxlatitude\": " + Utils.makeCoorFormat(extent.getMaxLatitude()) + ",\n";
        s += indent + "    \"maxlongitude\": " + Utils.makeCoorFormat(extent.getMaxLongitude()) + "\n";
        s += indent + "  },\n";
        s += indent + "  \"width\": " + width + ",\n";
        s += indent + "  \"height\": " + height + ",\n";
        s += indent + "  \"url\": \"" + url + "\"\n";
        s += indent + "}\n";
        return s;
    } // toJson()

    //----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <latitude>" + Utils.makeCoorFormat(centerLatitude) + "</latitude>\n";
        s += "    <longitude>" + Utils.makeCoorFormat(centerLongitude) + "</longitude>\n";
        s += "    <zoomlevel>" + zoomLevel + "</zoomlevel>\n";
        s += "    <extent>\n";
        s += "      <minlatitude>" + Utils.makeCoorFormat(extent.getMinLatitude()) + "</minlatitude>\n";
        s += "      <minlongitude>" + Utils.makeCoorFormat(extent.getMinLongitude()) + "</minlongitude>\n";
        s += "      <maxlatitude>" + Utils.makeCoorFormat(extent.getMaxLatitude()) + "</maxlatitude>\n";
        s += "      <maxlongitude>" + Utils.makeCoorFormat(extent.getMaxLongitude()) + "</maxlongitude>\n";
        s += "    </extent>\n";
        s += "    <width>" + width + "</width>\n";
        s += "    <height>" + height + "</height>\n";
        s += "    <url>" + Utils.convStr2Xml(url) + "</url>\n";
        return s;
    } // toXml()

    //----------------------------------------------------------------------------

    public void setLatitude(double latitude) {
        this.centerLatitude = latitude;
    }

    public double getLatitude() {
        return centerLatitude;
    }

    public void setLongitude(double longitude) {
        this.centerLongitude = longitude;
    }

    public double getLongitude() {
        return centerLongitude;
    }

    public void setZoomLevel(int zoomLevel) {
        this.zoomLevel = zoomLevel;
    }

    public int getZoomLevel() {
        return zoomLevel;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setPoints(DataPoint[] points) {
        this.points = points;
    }

    public DataPoint[] getPoints() {
        return points;
    }

    public void setGeo(JGeometry geo) {
        this.geo = geo;
    }

    public JGeometry getGeo() {
        return geo;
    }
}
