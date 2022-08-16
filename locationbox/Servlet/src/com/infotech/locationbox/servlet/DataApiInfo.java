package com.infotech.locationbox.servlet;

public class DataApiInfo {
    private int coorSrid = 0;
    private String lboxUrl = null;
    private String lboxKey = null;
    private String datasrc = null;
    private String datasrcApp = null;
    private String urlBase = null;
    private String mapBase = null;
    private String categoryList = null;
    private String brandList = null;
    private String packages = null;
    private String[] baseMaps = null;
    private String baseMapPoiLogo = null;
    private String baseMapPoi = null;
    private String baseMapSimple = null;

    public DataApiInfo() {
    }

    //-----------------------------------------------------------------------------

    public static DataApiInfo getInstance(String lboxUrl, String lboxKey, String datasrc) {
        DataApiInfo dai = new DataApiInfo();
        try {
            dai.coorSrid = Integer.parseInt(Utils.getParameter("fakecoor_srid"));
            dai.lboxUrl = lboxUrl;
            dai.lboxKey = lboxKey;
            dai.datasrc = datasrc;
            dai.datasrcApp = Utils.getParameter("mapviewer_app_datasource");
            dai.urlBase = Utils.getParameter("mapviewer_url");
            dai.mapBase = Utils.getParameter("mapviewer_basemap_0");
            dai.categoryList = "[" + Utils.getCategoryList(lboxKey) + "]";
            dai.brandList = "[" + Utils.getBrandList(lboxKey) + "]";
            dai.packages = "[" + Utils.getPackages(lboxKey) + "]";
            dai.baseMapPoiLogo = Utils.getParameter("mapviewer_basemap_1");
            dai.baseMapPoi = Utils.getParameter("mapviewer_basemap_2");
            dai.baseMapSimple = Utils.getParameter("mapviewer_basemap_3");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dai;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"coorsrid\": " + coorSrid + ",\n";
        s += indent + "  \"lboxurl\": \"" + lboxUrl + "\",\n";
        s += indent + "  \"lboxkey\": \"" + lboxKey + "\",\n";
        s += indent + "  \"datasrc\": \"" + datasrc + "\",\n";
        s += indent + "  \"datasrcapp\": \"" + datasrcApp + "\",\n";
        s += indent + "  \"urlbase\": \"" + urlBase + "\",\n";
        s += indent + "  \"mapbase\": \"" + mapBase + "\",\n";
        s += indent + "  \"categories\": " + categoryList + ",\n";
        s += indent + "  \"brands\": " + brandList + ",\n";
        s += indent + "  \"packages\": " + packages + ",\n";
        s += indent + "  \"basemappoilogo\": \"" + baseMapPoiLogo + "\",\n";
        s += indent + "  \"basemappoi\": \"" + baseMapPoi + "\",\n";
        s += indent + "  \"basemapsimple\": \"" + baseMapSimple + "\"\n";
        s += indent + "}";
        return s;
    } // toJson()

}
