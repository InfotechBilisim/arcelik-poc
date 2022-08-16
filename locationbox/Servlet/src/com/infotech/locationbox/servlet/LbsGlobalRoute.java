package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class LbsGlobalRoute {
    private String key = null;
    private int typ = 0;
    private String url = null;
    private String apiKey = null;


    public static LbsGlobalRoute getInstance(ResultSet rset) {
        LbsGlobalRoute lbsGlobalRoute = new LbsGlobalRoute();
        try {
            lbsGlobalRoute.key = rset.getString("KEY");
            lbsGlobalRoute.typ = rset.getInt("TYP");
            lbsGlobalRoute.url = rset.getString("URL");
            lbsGlobalRoute.apiKey = rset.getString("API_KEY");
            return lbsGlobalRoute;
        } catch (Exception e) {
            Utils.showError("LbsGlobalRoute.getInstance" + e.getMessage());
        }
        return null;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setTyp(int typ) {
        this.typ = typ;
    }

    public int getTyp() {
        return typ;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getApiKey() {
        return apiKey;
    }
}
