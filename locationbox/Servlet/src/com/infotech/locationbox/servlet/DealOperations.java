package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class DealOperations {

    public DealOperations() {
    }

    //-----------------------------------------------------------------------------

    public static DealCategory[] getCategoryList(boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DISTINCT CAT_ID,CAT_NAME FROM DEAL_CATEGORY ORDER BY CAT_NAME";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            while (rset.next()) {
                int id = rset.getInt(1);
                String name = rset.getString(2);
                array.add(new DealCategory(id, name));
            } // while()

            DbConn.closeDBConnection(pstmt, rset);

            if (counts) {
                sql = "SELECT CAT_ID,COUNT(*) FROM DEAL_CATEGORY GROUP BY CAT_ID";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    int id = rset.getInt(1);
                    int count = rset.getInt(2);
                    for (int i = 0; i < array.size(); i++) {
                        DealCategory dc = (DealCategory) array.get(i);
                        if (dc.getId() == id)
                            dc.setCount(count);
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

        DealCategory[] dcs = new DealCategory[array.size()];
        for (int i = 0; i < dcs.length; i++)
            dcs[i] = (DealCategory) array.get(i);
        return dcs;
    } // getCategoryList()

    //-----------------------------------------------------------------------------

    public static DealDeal[] getDealList(int categoryId, boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DD.*,DC.* FROM DEAL_CATEGORY DC, DEAL_DEAL DD WHERE (DC.DEAL_ID = DD.ID)" +
                (categoryId > 0 ? " AND DC.CAT_ID=?" : "") + " ORDER BY DD.NAME";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            if (categoryId > 0)
                pstmt.setInt(1, categoryId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DealDeal dd = DealDeal.getInstance(rset);
                array.add(dd);
            } // while()

            DbConn.closeDBConnection(pstmt, rset);

            if (counts) {
                sql = "SELECT DP.DEAL_ID,COUNT(*) FROM DEAL_CATEGORY DC, DEAL_DEAL DD, DEAL_PROVIDER DP WHERE (DC.DEAL_ID = DD.ID) AND (DD.ID = DP.DEAL_ID)" +
                    (categoryId > 0 ? " AND DC.CAT_ID=?" : "") + " GROUP BY DP.DEAL_ID";
                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                if (categoryId > 0)
                    pstmt.setInt(1, categoryId);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    long id = rset.getLong(1);
                    int count = rset.getInt(2);
                    for (int i = 0; i < array.size(); i++) {
                        DealDeal dd = (DealDeal) array.get(i);
                        if (dd.getId() == id)
                            dd.setCount(count);
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

        DealDeal[] dds = new DealDeal[array.size()];
        for (int i = 0; i < dds.length; i++)
            dds[i] = (DealDeal) array.get(i);
        return dds;
    } // getDealList()

    //-----------------------------------------------------------------------------

    public static DealDeal[] getNearDeals(double latitude, double longitude, int radius, int categoryId, int zone,
                                          boolean counts) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            String categoryWhereClause = (categoryId > 0 ? " AND DC.CAT_ID=?" : "");

            sql = "SELECT * FROM (SELECT DD.*,DC.*,DP.XCOOR,DP.YCOOR,((?-DP.XCOOR)*(?-DP.XCOOR)+(?-DP.YCOOR)*(?-DP.YCOOR)) REF_DISTANCE FROM DEAL_DEAL DD, DEAL_CATEGORY DC, DEAL_PROVIDER DP WHERE (DD.ID = DC.DEAL_ID) AND (DD.ID = DP.DEAL_ID)" +
                categoryWhereClause + " ORDER BY REF_DISTANCE) INNER_QUERY WHERE REF_DISTANCE <= ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            if (categoryId > 0)
                pstmt.setInt(colno++, categoryId);
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DealDeal dd = DealDeal.getInstance(rset);
                if (counts)
                    dd.setCount(1);

                boolean found = false;
                for (int i = 0; i < array.size(); i++) {
                    DealDeal ddx = (DealDeal) array.get(i);
                    if (ddx.getId() == dd.getId()) {
                        found = true;
                        if (counts)
                            ddx.setCount(ddx.getCount() + 1);
                        break;
                    }

                } // for()

                if (!found)
                    array.add(dd);
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

        DealDeal[] dds = new DealDeal[array.size()];
        for (int i = 0; i < dds.length; i++)
            dds[i] = (DealDeal) array.get(i);
        return dds;
    } // getNearDeals()

}
