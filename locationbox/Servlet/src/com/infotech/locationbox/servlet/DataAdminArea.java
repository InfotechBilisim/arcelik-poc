package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataAdminArea {
    protected long ulkeId = 0;
    protected String ulkeAdi = null;
    protected String ulkeKodu = null;
    protected String suAdi = null;
    protected long ilId = 0;
    protected String ilAdi = null;
    protected long ilceId = 0;
    protected String ilceAdi = null;
    protected long mahalleId = 0;
    protected String mahalleAdi = null;
    protected String postaKodu = null;
    protected int zone = 0;

    public DataAdminArea() {
    }
    //-----------------------------------------------------------------------------

    public static DataAdminArea getInstance(ResultSet rset) {
        DataAdminArea daa = new DataAdminArea();
        try {
            daa.ulkeId = rset.getLong("ULKE_ID");
            daa.ulkeAdi = rset.getString("ULKE_ADI");
            daa.ulkeKodu = rset.getString("ULKE_KODU");
            daa.suAdi = rset.getString("SU_ADI");
            daa.ilId = rset.getLong("IL_ID");
            daa.ilAdi = rset.getString("IL_ADI");
            daa.ilceId = rset.getLong("ILCE_ID");
            daa.ilceAdi = rset.getString("ILCE_ADI");
            daa.mahalleId = rset.getLong("MAHALLE_ID");
            daa.mahalleAdi = rset.getString("MAHALLE_ADI");
            daa.postaKodu = rset.getString("POSTA_KODU");
            daa.zone = rset.getInt("ZONE");
            return daa;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return daa;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public static DataAdminArea getInstance(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        DataAdminArea daa = new DataAdminArea();
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM ADMIN_AREA WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();
            if (rset.next())
                return getInstance(rset);

            return daa;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return daa;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public static int getZone(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT ZONE FROM ADMIN_AREA WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int zone = rset.getInt(1);
                DbConn.closeDBConnection(pstmt, rset);
                return zone;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // getZone()

}
