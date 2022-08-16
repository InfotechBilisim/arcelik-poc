package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;


public class CampOperations {

    public CampOperations() {
    }

    //-----------------------------------------------------------------------------

    public static CampCategory[] getCategoryList(String key, boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT CATEGORY,DESCRIPTION FROM CAMP_CATEGORY WHERE KEY=? ORDER BY CATEGORY";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                String description = rset.getString(2);
                array.add(new CampCategory(category, description));
            } // while()

            DbConn.closeDBConnection(pstmt, rset);

            if (counts) {
                sql = "SELECT CK.CATEGORY,COUNT(*) FROM CAMP_CATEGORY CK, CAMP_CAMPAIGN CC WHERE (CK.CATEGORY = CC.CATEGORY) AND CK.KEY=? GROUP BY CK.CATEGORY,CK.DESCRIPTION ORDER BY CK.CATEGORY";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                pstmt.setString(1, key);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    String category = rset.getString(1);
                    int count = rset.getInt(2);
                    for (int i = 0; i < array.size(); i++) {
                        CampCategory cc = (CampCategory) array.get(i);
                        if (cc.getCategory().equals(category))
                            cc.setCount(count);
                    } // for()
                } // while()
            } // if(counts)

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("EXCEPTION: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        CampCategory[] ccs = new CampCategory[array.size()];
        for (int i = 0; i < ccs.length; i++)
            ccs[i] = (CampCategory) array.get(i);
        return ccs;
    } // getCategoryList()

    //-----------------------------------------------------------------------------

    public static CampCampaign[] getCampaignList(String key, String category, long poiId, boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (poiId > 0)
                sql = "SELECT CC.* FROM CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP WHERE (CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND CC.ACTIVE_FLAG=1 AND CC.START_DATE <= SYSDATE AND CC.END_DATE >= SYSDATE AND CG.KEY=?" +
                    (category != null && category.length() > 0 ? " AND CG.CATEGORY=?" : "") +
                    "AND CCP.POI_ID=? ORDER BY NAME";
            else
                sql = "SELECT CC.* FROM CAMP_CATEGORY CG, CAMP_CAMPAIGN CC WHERE (CG.CATEGORY = CC.CATEGORY) AND CC.ACTIVE_FLAG=1 AND CC.START_DATE <= SYSDATE AND CC.END_DATE >= SYSDATE AND CG.KEY=?" +
                    (category != null && category.length() > 0 ? " AND CG.CATEGORY=?" : "") + " ORDER BY NAME";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            if (category != null && category.length() > 0)
                pstmt.setString(colno++, category);
            if (poiId > 0)
                pstmt.setLong(colno++, poiId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                CampCampaign cc = CampCampaign.getInstance(rset);
                array.add(cc);
            } // while()

            DbConn.closeDBConnection(pstmt, rset);

            if (counts) {
                sql = "SELECT CC.CAMPAIGN_ID,COUNT(*) FROM CAMP_CATEGORY CG, CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP WHERE (CG.CATEGORY = CC.CATEGORY) AND (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND CC.ACTIVE_FLAG=1 AND CC.START_DATE <= SYSDATE AND CC.END_DATE >= SYSDATE AND CG.KEY=?" +
                    (category != null && category.length() > 0 ? " AND CG.CATEGORY=?" : "") +
                    " GROUP BY CC.CAMPAIGN_ID";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                pstmt.setString(1, key);
                if (category != null && category.length() > 0)
                    pstmt.setString(2, category);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    long cid = rset.getLong(1);
                    int count = rset.getInt(2);
                    for (int i = 0; i < array.size(); i++) {
                        CampCampaign cc = (CampCampaign) array.get(i);
                        if (cc.getId() == cid)
                            cc.setCount(count);
                    } // for()
                } // while()
            } // if(counts)

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("EXCEPTION: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        CampCampaign[] ccs = new CampCampaign[array.size()];
        for (int i = 0; i < ccs.length; i++)
            ccs[i] = (CampCampaign) array.get(i);
        return ccs;
    } // getCampaignList()

    //-----------------------------------------------------------------------------

    public static CampCampaign[] getNearCampaigns(String key, double latitude, double longitude, int radius,
                                                  String category, int zone, boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (category != null && category.length() > 0) {
                sql = "SELECT COUNT(*) FROM CAMP_CATEGORY WHERE KEY=? AND CATEGORY=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                pstmt.setString(1, key);
                pstmt.setString(2, category);
                rset = pstmt.executeQuery();
                rset.next();
                int count = rset.getInt(1);
                if (count <= 0)
                    return null;

                DbConn.closeDBConnection(pstmt, rset);
            }

            String categoryWhereClause =  "CC.CATEGORY IN (" +
                (category == null || category.length() <= 0 ? "(SELECT CATEGORY FROM CAMP_CATEGORY WHERE KEY=?)" :  "?") + ")";

            sql = "SELECT IQ.CAMPAIGN_ID,IQ.NAME,IQ.DESCRIPTION,IQ.CATEGORY,IQ.HOT,IQ.START_DATE,IQ.END_DATE,IQ.BRAND_ID,IQ.BRAND_NAME,IQ.PRODUCT_NAME,IQ.PRODUCT_TITLE,IQ.PRODUCT_DETAILS,IQ.PRODUCT_PAYMENT,IQ.PRODUCT_LIST_PRICE,IQ.PRODUCT_PRICE,IQ.PRODUCT_DISCOUNT_PERCENT,IQ.STOCK_AMOUNT";
            sql += ",IQ.ID,IQ.STANDARD_NAME,IQ.STREET_NAME,IQ.STREET_TYPE,IQ.MAHALLE_ADI,IQ.HSN,IQ.POSTAL_CODE,IQ.ILCE_ADI,IQ.IL_ADI,IQ.COUNTRY_CODE,IQ.AREA_CODE,IQ.TELEPHONE";
            sql += ",IQ.XCOOR,IQ.YCOOR,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR,IQ.YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (SELECT CC.*,P.ID,P.STANDARD_NAME,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.XCOOR,P.YCOOR,((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE FROM CAMP_CAMPAIGN CC, CAMP_CAMPAIGN_POI CCP, POI P WHERE (CC.CAMPAIGN_ID = CCP.CAMPAIGN_ID) AND (CCP.POI_ID = P.ID) AND CC.ACTIVE_FLAG=1 AND CC.START_DATE <= SYSDATE AND CC.END_DATE >= SYSDATE AND P.ZONE=? AND " +
                categoryWhereClause + " ORDER BY REF_DISTANCE) IQ";
            sql += " WHERE REF_DISTANCE <= ?";
            // Utils.showText("SQL: " + sql);
            // Utils.showText("LON,LAT: " + longitude + "," + latitude + ", ZONE: " + zone);
            // Utils.showText("RADIUS: " + radius + ", TOLARANCE: " + (radius / 100000.0) * (radius / 100000.0));
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setInt(colno++, zone);
            if (category == null || category.length() <= 0)
                pstmt.setString(colno++, key);
            else
                pstmt.setString(colno++, category);
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            rset = pstmt.executeQuery();
            while (rset.next()) {
                CampCampaign cc = CampCampaign.getInstance(rset);
                if (counts)
                    cc.setCount(1);

                boolean found = false;
                for (int i = 0; i < array.size(); i++) {
                    CampCampaign ccx = (CampCampaign) array.get(i);
                    if (ccx.getId() == cc.getId()) {
                        found = true;
                        if (counts)
                            ccx.setCount(ccx.getCount() + 1);
                        break;
                    }

                } // for()

                if (!found)
                    array.add(cc);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        CampCampaign[] ccs = new CampCampaign[array.size()];
        for (int i = 0; i < ccs.length; i++)
            ccs[i] = (CampCampaign) array.get(i);
        return ccs;
    } // getNearCampaigns()

}
