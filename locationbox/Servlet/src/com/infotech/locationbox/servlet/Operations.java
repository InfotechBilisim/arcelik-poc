package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.DataAddress;


import com.infotech.locationbox.stromberglabs.cluster.Clusterable;

import com.infotech.locationbox.stromberglabs.cluster.KClusterer;
import com.infotech.locationbox.stromberglabs.cluster.KMeansClusterer;
import com.infotech.locationbox.stromberglabs.cluster.Point;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiLineString;

import java.awt.Dimension;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.net.URLEncoder;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleConnection;

import oracle.lbs.mapclient.MapViewer;

import oracle.spatial.geometry.JGeometry;

import oracle.sql.BLOB;
import oracle.sql.STRUCT;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.geotools.data.oracle.sdo.GeometryConverter;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileWriter;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileWriter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Operations {
    protected static int fakeCoorSrid = 81989006;
    protected static int xmlParserPosStart = 0;
    private static final String USER_AGENT = "Mozilla/5.0";

    public Operations() {
    }
    //-----------------------------------------------------------------------------

    public static DataPackage[] getMyPackages(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn =null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_KEY_PACKAGE KP, LBS_PACKAGE P WHERE (KP.PACKAGE_NAME = P.PACKAGE_NAME) AND KEY=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataPackage dp = DataPackage.getInstance(rset);
                array.add(dp);
            } // while()
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

        DataPackage[] dps = new DataPackage[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPackage) array.get(i);
        return dps;
    } // getMyPackages()

    //-----------------------------------------------------------------------------

    public static DataIl[] getIlList(int majorCities, long countryId, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
                                   
            if (majorCities > 0){
                sql = "SELECT IL_ID,IL_ADI,XCOOR,YCOOR,UAVT_IL_ID,GEOMBR FROM IL " + (countryId > 0 ? "WHERE ULKE_ID = ?" : "");
                if( isNotNullKeyword ){
                     sql +=  (countryId > 0) ? " AND KEYWORD LIKE ? " : " WHERE  KEYWORD LIKE ? ";  
                }
               sql += " ORDER BY CASE IL_ID WHEN 34 THEN 1 WHEN 35 THEN 2 WHEN 6 THEN 3 END, NLSSORT(IL_ADI, 'nls_sort=xturkish')";
            }
            else{
                sql = "SELECT IL_ID,IL_ADI,XCOOR,YCOOR,UAVT_IL_ID,GEOMBR FROM IL " + (countryId > 0 ? "WHERE ULKE_ID = ?" : "");
                
                if( isNotNullKeyword ){
                     sql +=  (countryId > 0) ? " AND KEYWORD LIKE ? " : " WHERE  KEYWORD LIKE ? ";  
                }
                
                sql += " ORDER BY NLSSORT(IL_ADI, 'nls_sort=xturkish') ";
            }
          //  Utils.showInfo("SQL: " + sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int col = 1;
            if (countryId > 0)
                pstmt.setLong(col++, countryId);
            if ( isNotNullKeyword )
               pstmt.setString(col++, "%" + keyword + "%");
            
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataIl di = DataIl.getInstance(rset, false, false);
                array.add(di);
            } // while()
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

        DataIl[] dis = new DataIl[array.size()];
        for (int i = 0; i < dis.length; i++)
            dis[i] = (DataIl) array.get(i);
        return dis;
    } // getIlList()

    //-----------------------------------------------------------------------------

    public static DataIl[] getIlListWithExtent(Extent ext, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            sql = "SELECT IL_ID,IL_ADI,XCOOR,YCOOR,UAVT_IL_ID,GEOMBR FROM IL WHERE SDO_ANYINTERACT(GEOLOC, (SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'TRUE' ";
            if( isNotNullKeyword ){
                 sql += " AND KEYWORD LIKE ? ";
            }
            sql += " ORDER BY NLSSORT(IL_ADI, 'nls_sort=xturkish') ";
            System.out.println("SQL: " + sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            if ( isNotNullKeyword )
             pstmt.setString(colno++, "%" + keyword + "%");
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataIl di = DataIl.getInstance(rset, false, false);
                array.add(di);
            } // while()
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

        DataIl[] dis = new DataIl[array.size()];
        for (int i = 0; i < dis.length; i++)
            dis[i] = (DataIl) array.get(i);
        return dis;
    } // getIlListWithExtent()

    //-----------------------------------------------------------------------------

    public static DataIlce[] getIlceList(long ilId, String ilAdi, String keyword) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        ArrayList array = new ArrayList();
        boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
        if (isNotNullKeyword)
        keyword = Utils.convToUpperEnglishChars(keyword);
        if (ilId > 0) {
            cnn = DbConn.getPooledConnection();
            try {
                sql = "SELECT ILCE_ID,ILCE_ADI,IL_ID,XCOOR,YCOOR,UAVT_ILCE_ID,GEOMBR FROM ILCE WHERE IL_ID=? ";
                if( isNotNullKeyword ){
                    sql += " AND  KEYWORD LIKE ? ";
                }
                sql += " ORDER BY NLSSORT(ILCE_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int col = 1;
                pstmt.setLong(col++, ilId);
                if ( isNotNullKeyword )
                  pstmt.setString(col++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataIlce di = DataIlce.getInstance(rset, false, false);
                    array.add(di);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                Utils.showError("EXCEPTION: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        } else if (ilAdi != null) {
            cnn = DbConn.getPooledConnection();
            try {
                sql = "SELECT ILCE_ID,ILCE_ADI,IL_ID,XCOOR,YCOOR,UAVT_ILCE_ID,GEOMBR FROM ILCE WHERE IL_ID IN (SELECT IL_ID FROM IL WHERE UPPERENG(IL_ADI) = UPPERENG(?)) ";
                if( isNotNullKeyword ){
                    sql += " AND  KEYWORD LIKE ? ";
                }
                sql += " ORDER BY NLSSORT(ILCE_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int col = 1;
                pstmt.setString(col++, ilAdi);
                if ( isNotNullKeyword )
                  pstmt.setString(col++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataIlce di = DataIlce.getInstance(rset, false, false);
                    array.add(di);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                Utils.showError("EXCEPTION: " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataIlce[] dis = new DataIlce[array.size()];
        for (int i = 0; i < dis.length; i++)
            dis[i] = (DataIlce) array.get(i);
        return dis;
    } // getIlceList()

    //-----------------------------------------------------------------------------

    public static DataIlce[] getIlceListWithExtent(Extent ext, String keyword) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        ArrayList array = new ArrayList();

        int adrCount = 100;
        try {
            adrCount = Integer.parseInt(Utils.getParameter("adr_count"));
        } catch (Exception e) {
            ;
        }
        try {
            cnn = DbConn.getPooledConnection();
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            sql = "SELECT ILCE_ID,ILCE_ADI,IL_ID,XCOOR,YCOOR,UAVT_ILCE_ID,GEOMBR FROM ILCE WHERE SDO_ANYINTERACT(GEOLOC, (SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'TRUE'";
            if( isNotNullKeyword ){
                sql += " AND  KEYWORD LIKE ? ";
            }
            sql += " ORDER BY NLSSORT(ILCE_ADI, 'nls_sort=xturkish') ";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            if ( isNotNullKeyword )
               pstmt.setString(colno++, "%" + keyword + "%");
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataIlce di = DataIlce.getInstance(rset, false, false);
                array.add(di);
            } // while()
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

        DataIlce[] dis = new DataIlce[array.size()];
        for (int i = 0; i < dis.length; i++)
            dis[i] = (DataIlce) array.get(i);
        return dis;
    } // getIlceListWithExtent()

    //-----------------------------------------------------------------------------

    public static DataMahalle[] getMahalleList( int type, String keyword ) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        ArrayList array = new ArrayList();

            try {
                boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
                if (isNotNullKeyword)
                keyword = Utils.convToUpperEnglishChars(keyword);
                
                cnn = DbConn.getPooledConnection();
                sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID, DECODE(M.GEOMBR.SDO_GTYPE, 2001, NULL, GEOMBR) GEOMBR FROM MAHALLE M WHERE ILCE_ID IN(SELECT ILCE_ID FROM ILCE ) AND TYPE IN (" +
                    (type == 0 ? "3" : (type == 1 ? "3,4,6" : "4,5,6")) + ") ";
                
                if( isNotNullKeyword ){
                    sql += " AND  KEYWORD LIKE ? ";
                }
                sql += "ORDER BY NLSSORT(MAHALLE_ADI, 'nls_sort=xturkish') ";

                pstmt = cnn.prepareStatement(sql);
                pstmt.clearParameters();
                int col = 1;
                if ( isNotNullKeyword )
                   pstmt.setString(col++, "%" + keyword + "%");
                rset = pstmt.executeQuery();

                while (rset.next()) {
                    DataMahalle dm = DataMahalle.getInstance(rset, false, false);
                    array.add(dm);
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

        DataMahalle[] dms = new DataMahalle[array.size()];
        for (int i = 0; i < dms.length; i++)
            dms[i] = (DataMahalle) array.get(i);
        return dms;
    } // getMahalleList()

    //-----------------------------------------------------------------------------

    public static DataMahalle[] getMahalleList(long ilceId, String ilAdi, String ilceAdi, int type, String keyword) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        ArrayList array = new ArrayList();
        boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
        if (isNotNullKeyword)
        keyword = Utils.convToUpperEnglishChars(keyword);
        if (ilceId > 0) {
            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID, DECODE(M.GEOMBR.SDO_GTYPE, 2001, NULL, GEOMBR) GEOMBR FROM MAHALLE M WHERE ILCE_ID=? AND TYPE IN (" +
                    (type == 0 ? "3" : (type == 1 ? "3,4,6" : "4,5,6")) + ") ";
                if( isNotNullKeyword ){
                    sql += " AND  KEYWORD LIKE ? ";
                }
                sql += "ORDER BY NLSSORT(MAHALLE_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int col = 1;
                pstmt.setLong(col++, ilceId);
                if ( isNotNullKeyword )
                   pstmt.setString(col++, "%" + keyword + "%");
                
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataMahalle dm = DataMahalle.getInstance(rset, false, false);
                    array.add(dm);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        } else if (ilAdi != null && ilceAdi != null) {
            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID,GEOMBR FROM MAHALLE WHERE ILCE_ID IN (SELECT ILCE_ID FROM ILCE WHERE UPPERENG(ILCE_ADI) = UPPERENG(?) AND IL_ID IN (SELECT IL_ID FROM IL WHERE UPPERENG(IL_ADI) = UPPERENG(?) )) AND TYPE IN (" +
                    (type == 0 ? "3" : (type == 1 ? "3,4,6" : "4,5,6")) + ") ";
                
                if( isNotNullKeyword ){
                    sql += " AND  KEYWORD LIKE ? ";
                }
                sql += "ORDER BY NLSSORT(MAHALLE_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int col = 1;
                pstmt.setString(col++, ilceAdi);
                pstmt.setString(col++, ilAdi);
                if ( isNotNullKeyword )
                   pstmt.setString(col++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataMahalle dm = DataMahalle.getInstance(rset, false, false);
                    array.add(dm);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataMahalle[] dms = new DataMahalle[array.size()];
        for (int i = 0; i < dms.length; i++)
            dms[i] = (DataMahalle) array.get(i);
        return dms;
    } // getMahalleList()

    //-----------------------------------------------------------------------------

    public static DataMahalle[] getMahalleListWithExtent(Extent ext, int type, String keyword) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        
        ArrayList array = new ArrayList();

        int adrCount = 100;
        try {
            adrCount = Integer.parseInt(Utils.getParameter("adr_count"));
        } catch (Exception e) {
            ;
        }

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID,DECODE(M.GEOMBR.SDO_GTYPE, 2001, NULL, GEOMBR) GEOMBR FROM MAHALLE M WHERE TYPE IN (" +
                (type == 0 ? "3" : (type == 1 ? "3,4,6" : "4,5,6")) + ") ";
            if( isNotNullKeyword ){
                sql += " AND KEYWORD LIKE ? ";
            }
            sql += "AND SDO_ANYINTERACT(GEOLOC, (SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'TRUE' ORDER BY NLSSORT(MAHALLE_ADI, 'nls_sort=xturkish') ";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount;
            pstmt = cnn.prepareStatement(sql);
            System.out.println(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if ( isNotNullKeyword )
               pstmt.setString(colno++, "%" + keyword + "%");
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
           
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataMahalle dm = DataMahalle.getInstance(rset, false, false);
                array.add(dm);
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

        DataMahalle[] dms = new DataMahalle[array.size()];
        for (int i = 0; i < dms.length; i++)
            dms[i] = (DataMahalle) array.get(i);
        return dms;
    } // getMahalleListWithExtent()

    //-----------------------------------------------------------------------------

    public static DataYol[] getYolList(long ilId, long ilceId, long mahalleId, String ilAdi, String ilceAdi, String mahalleAdi, String yolAdi) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        if (yolAdi != null)
            yolAdi = Utils.convToUpperEnglishChars(yolAdi);

        ArrayList array = new ArrayList();
        DataYol dyPrev = null;
        String prevName = "";


        int adrCount = 100;
        try {
            adrCount = Integer.parseInt(Utils.getParameter("adr_count"));
        } catch (Exception e) {
            ;
        }
        if ((ilId > 0 || ilceId > 0 || mahalleId > 0)) {

            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT Y.YOL_ID,Y.YOL_ADI,Y.ZOOMLEVEL,Y.POSTA_KODU,Y.XCOOR,Y.YCOOR,ISY.UAVT_CSBM_ID,ISY.IL_ID,ISY.IL_ADI,ISY.ILCE_ID,ISY.ILCE_ADI,ISY.MAHALLE_ID,ISY.MAHALLE_ADI FROM IDARI_SINIR_YOL ISY, YOL Y WHERE (ISY.YOL_ID = Y.YOL_ID)";
                if (ilId > 0)
                    sql += " AND ISY.IL_ID=?";
                if (ilceId > 0)
                    sql += " AND ISY.ILCE_ID=?";
                if (mahalleId > 0)
                    sql += " AND ISY.MAHALLE_ID=?";
                if (yolAdi != null && yolAdi.length() > 0)
                    sql += " AND Y.KEYWORD LIKE ?";
                sql += " ORDER BY NLSSORT(YOL_ADI, 'nls_sort=xturkish'),NLSSORT(MAHALLE_ADI, 'nls_sort=xturkish') ";
                sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                if (ilId > 0)
                    pstmt.setLong(colno++, ilId);
                if (ilceId > 0)
                    pstmt.setLong(colno++, ilceId);
                if (mahalleId > 0)
                    pstmt.setLong(colno++, mahalleId);
                if (yolAdi != null && yolAdi.length() > 0)
                    pstmt.setString(colno++, "%" + yolAdi + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataYol dy = DataYol.getInstance(rset, false, false);
                    if (dyPrev == null)
                        prevName = dy.name;
                    else {
                        if (!prevName.equals(dy.name))
                            prevName = dy.name;
                        else {
                            if (prevName.equals(dyPrev.name))
                                dyPrev.name += "(" + dyPrev.mahalleAdi + ")";
                            dy.name += "(" + dy.mahalleAdi + ")";
                        }
                    }
                    dyPrev = dy;
                    array.add(dy);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }

        } else if ((ilAdi != null && ilceAdi != null && mahalleAdi != null)) {

            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT Y.YOL_ID,Y.YOL_ADI,Y.ZOOMLEVEL,Y.POSTA_KODU,Y.XCOOR,Y.YCOOR,ISY.UAVT_CSBM_ID,ISY.IL_ID,ISY.IL_ADI,ISY.ILCE_ID,ISY.ILCE_ADI,ISY.MAHALLE_ID,ISY.MAHALLE_ADI FROM IDARI_SINIR_YOL ISY, YOL Y WHERE (ISY.YOL_ID = Y.YOL_ID)";
                sql += " AND UPPERENG(ISY.IL_ADI)=UPPERENG(?)";
                sql += " AND UPPERENG(ISY.ILCE_ADI)=UPPERENG(?)";
                sql += " AND UPPERENG(ISY.MAHALLE_ADI)=UPPERENG(?)";
                if (yolAdi != null && yolAdi.length() > 0)
                    sql += " AND Y.KEYWORD LIKE ?";
                sql += " ORDER BY NLSSORT(Y.YOL_ADI, 'nls_sort=xturkish'), NLSSORT(ISY.MAHALLE_ADI, 'nls_sort=xturkish') ";
                sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setString(colno++, ilAdi);
                pstmt.setString(colno++, ilceAdi);
                pstmt.setString(colno++, mahalleAdi);
                if (yolAdi != null && yolAdi.length() > 0)
                    pstmt.setString(colno++, "%" + yolAdi + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataYol dy = DataYol.getInstance(rset, false, false);
                    if (dyPrev == null)
                        prevName = dy.name;
                    else {
                        if (!prevName.equals(dy.name))
                            prevName = dy.name;
                        else {
                            if (prevName.equals(dyPrev.name))
                                dyPrev.name += "(" + dyPrev.mahalleAdi + ")";
                            dy.name += "(" + dy.mahalleAdi + ")";
                        }
                    }
                    dyPrev = dy;
                    array.add(dy);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataYol[] dys = new DataYol[array.size()];
        for (int i = 0; i < dys.length; i++)
            dys[i] = (DataYol) array.get(i);
        return dys;
    } // getYolList()

    //-----------------------------------------------------------------------------

    public static DataYol[] getYolListWithExtent(Extent ext, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();
        DataYol dyPrev = null;
        String prevName = "";

        int adrCount = 100;
        try {
            adrCount = Integer.parseInt(Utils.getParameter("adr_count"));
        } catch (Exception e) {
            ;
        }

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            sql = "SELECT Y.YOL_ID,Y.YOL_ADI,Y.ZOOMLEVEL,Y.POSTA_KODU,Y.XCOOR,Y.YCOOR,ISY.UAVT_CSBM_ID,ISY.IL_ID,ISY.IL_ADI,ISY.ILCE_ID,ISY.ILCE_ADI,ISY.MAHALLE_ID,ISY.MAHALLE_ADI FROM IDARI_SINIR_YOL ISY, YOL Y WHERE (ISY.YOL_ID = Y.YOL_ID)";
            sql += " AND (Y.XCOOR >= ? AND Y.XCOOR <= ? AND Y.YCOOR >= ? AND Y.YCOOR <= ?)";
            if (isNotNullKeyword)
                sql += " AND Y.KEYWORD LIKE ? ";
            sql += " ORDER BY  NLSSORT(Y.YOL_ADI, 'nls_sort=xturkish'), NLSSORT(ISY.MAHALLE_ADI, 'nls_sort=xturkish') ";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            if (isNotNullKeyword)
                pstmt.setString(colno++, "%" + keyword + "%");
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataYol dy = DataYol.getInstance(rset, false, false);
                if (dyPrev == null)
                    prevName = dy.name;
                else {
                    if (!prevName.equals(dy.name))
                        prevName = dy.name;
                    else {
                        if (prevName.equals(dyPrev.name))
                            dyPrev.name += "(" + dyPrev.mahalleAdi + ")";
                        dy.name += "(" + dy.mahalleAdi + ")";
                    }
                }
                dyPrev = dy;
                array.add(dy);
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

        DataYol[] dys = new DataYol[array.size()];
        for (int i = 0; i < dys.length; i++)
            dys[i] = (DataYol) array.get(i);
        return dys;
    } // getYolListWithExtent()

    //-----------------------------------------------------------------------------

    public static String getYol2ShapeWithExtent(String path, Extent ext) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        OracleConnection cnn = null;
        GeometryConverter conv = null;

        ArrayList array = new ArrayList();
        ArrayList idArray = new ArrayList();

        try {
            cnn = (OracleConnection)DbConn.getPooledConnection();
            conv = new GeometryConverter(cnn);
            sql = "SELECT YOL_ID,GEOLOC FROM YOL WHERE SDO_ANYINTERACT(GEOLOC, (SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            rset = pstmt.executeQuery();
            while (rset.next()) {
                long id = rset.getLong(1);
                STRUCT obj = (STRUCT) rset.getObject(2);
                if (obj == null)
                    continue;

                Geometry geo = conv.asGeometry(obj);
                if (geo.getGeometryType().equalsIgnoreCase("LineString")) {
                    LineString[] lss = new LineString[1];
                    lss[0] = (LineString) geo;
                    MultiLineString mls = new MultiLineString(lss, new GeometryFactory());
                    array.add(mls);
                    idArray.add(new Long(id));
                    continue;
                }

                array.add(geo);
                idArray.add(new Long(id));
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

        Geometry[] geos = new Geometry[array.size()];
        for (int i = 0; i < geos.length; i++)
            geos[i] = (Geometry) array.get(i);

        String shpFileName = path + ".shp";
        String shxFileName = path + ".shx";
        String dbfFileName = path + ".dbf";

        FileOutputStream shp = null;
        FileOutputStream shx = null;
        ShapefileWriter writer = null;
        try {
            shp = new FileOutputStream(shpFileName);
            shx = new FileOutputStream(shxFileName);
            writer = new ShapefileWriter(shp.getChannel(), shx.getChannel());
            GeometryCollection geoColl = new GeometryCollection(geos, new GeometryFactory());
            writer.write(geoColl, ShapeType.ARC);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (writer != null)
                    writer.close();
            } catch (Exception e) {
                ;
            }
            DbConn.closeFileConn(null, shp);
            DbConn.closeFileConn(null, shx);
        }

        DbaseFileHeader dbfHeader = null;
        FileOutputStream dbf = null;
        DbaseFileWriter dbfWriter = null;
        try {
            dbfHeader = new DbaseFileHeader();
            dbfHeader.addColumn("ID", 'N', 16, 0);
            dbfHeader.setNumRecords(idArray.size());
            dbf = new FileOutputStream(dbfFileName);
            dbfWriter = new DbaseFileWriter(dbfHeader, dbf.getChannel());
            for (int i = 0; i < idArray.size(); i++) {
                Long[] objs = new Long[1];
                objs[0] = (Long) idArray.get(i);
                dbfWriter.write(objs);
            } // for()
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            try {
                if (dbfWriter != null)
                    dbfWriter.close();
            } catch (Exception e) {
                ;
            }
            DbConn.closeFileConn(null, dbf);
        }

        String zipFileName = path + ".zip";
        String[] fileNames = new String[3];
        fileNames[0] = shpFileName;
        fileNames[1] = shxFileName;
        fileNames[2] = dbfFileName;
        Utils.zipFiles(fileNames, zipFileName);

        File f = null;
        f = new File(shpFileName);
        f.delete();
        f = new File(shxFileName);
        f.delete();
        f = new File(dbfFileName);
        f.delete();

        return zipFileName;
    } // getYol2ShapeWithExtent()

    //-----------------------------------------------------------------------------

    public static DataYol getNearestYol(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        DataYol dy = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT Y.*, SDO_NN_DISTANCE(1) DISTANCE FROM YOL Y WHERE SDO_NN(GEOLOC,  SDO_GEOMETRY(2001, 8307, SDO_POINT_TYPE(?, ?, NULL), NULL,  NULL),'SDO_NUM_RES=1 unit=M', 1) = 'TRUE' ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            rset = pstmt.executeQuery();
            if (rset.next())
                dy = DataYol.getInstance(rset);
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }
        return dy;
    } // getNearestYol()

    //-----------------------------------------------------------------------------

    public static DataIl getIlInfo(long ilId, boolean geometry, boolean encode, long adresKodu, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            if (adresKodu <= 0) {
                sql = "SELECT IL_ID,IL_ADI,XCOOR,YCOOR,UAVT_IL_ID,GEOMBR " + (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "") + " ";
                sql += "FROM IL WHERE IL_ID=?";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? ";  
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, ilId);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    DataIl di = DataIl.getInstance(rset, geometry, encode);
                    return di;
                }
            } else {
                sql = "SELECT IL_ID,IL_ADI,XCOOR,YCOOR,UAVT_IL_ID,GEOMBR " + (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "") + " ";
                sql += "FROM IL WHERE UAVT_IL_ID=?";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? ";  
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, adresKodu);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    DataIl di = DataIl.getInstance(rset, geometry, encode);
                    return di;
                }
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getIlInfo()

    //-----------------------------------------------------------------------------

    public static DataIlce getIlceInfo(long ilceId, boolean geometry, boolean encode, long adresKodu, String keyword) {
        DataIlce di = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            
            cnn = DbConn.getPooledConnection();
            if (adresKodu <= 0) {
                sql = "SELECT ILCE_ID,ILCE_ADI,IL_ID,XCOOR,YCOOR,UAVT_ILCE_ID, GEOMBR " + (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC,0.5)" : "") + " ";
                sql += "FROM ILCE WHERE ILCE_ID=?";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? "; 
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, ilceId);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    di = DataIlce.getInstance(rset, geometry, encode);
                }
            } else {
                sql = "SELECT ILCE_ID,ILCE_ADI,IL_ID,XCOOR,YCOOR,UAVT_ILCE_ID, GEOMBR " + (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC,0.5)" : "") + " ";
                sql += "FROM ILCE WHERE UAVT_ILCE_ID=?";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? "; 
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, adresKodu);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    di = DataIlce.getInstance(rset, geometry, encode);
                }
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return di;
    } // getIlceInfo()

    //-----------------------------------------------------------------------------

    public static DataMahalle getMahalleInfo(long mahalleId, boolean geometry, boolean encode, long adresKodu, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            if (adresKodu <= 0) {
                sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID,GEOMBR " + (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC,0.5)" : ",NULL") + " ";
                sql += "FROM MAHALLE WHERE MAHALLE_ID=? ";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? "; 
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, mahalleId);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    DataMahalle dm = DataMahalle.getInstance(rset, geometry, encode);
                    return dm;
                }
            } else {
                sql = "SELECT MAHALLE_ID,MAHALLE_ADI,TYPE,ILCE_ID,XCOOR,YCOOR,POSTA_KODU,UAVT_MAHALLE_ID, GEOMBR " + (geometry ? ",GEOLOC" : ",NULL") + " ";
                sql += "FROM MAHALLE WHERE UAVT_MAHALLE_ID=? ";
                if( isNotNullKeyword )
                    sql += " AND KEYWORD LIKE ? "; 
                cnn = DbConn.getPooledConnection(); //ekn
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, adresKodu);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    DataMahalle dm = DataMahalle.getInstance(rset, geometry, encode);
                    return dm;
                }
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getMahalleInfo()

    //-----------------------------------------------------------------------------

    public static DataYol[] getYolInfo(long yolId, boolean geometry, boolean encode, long adresKodu, String keyword) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();
        DataYol dy = new DataYol();
        try {
            boolean isNotNullKeyword = ( keyword != null && keyword.length()>0 );
            if (isNotNullKeyword)
            keyword = Utils.convToUpperEnglishChars(keyword);
            cnn = DbConn.getPooledConnection();
            if (adresKodu <= 0) {
                sql = "SELECT Y.YOL_ID, ISY.UAVT_CSBM_ID, Y.YOL_ADI,Y.XCOOR,Y.YCOOR,Y.ZOOMLEVEL,ISY.GEOLOC ";
                sql += "FROM IDARI_SINIR_YOL ISY, YOL Y ";
                sql += "WHERE (ISY.YOL_ID = Y.YOL_ID) AND ISY.YOL_ID=? AND Y.YSK = 0 ";
                if( isNotNullKeyword )
                    sql += " AND Y.KEYWORD LIKE ? "; 
                sql += " ORDER BY  NLSSORT(Y.YOL_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, yolId);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    dy = DataYol.getInstance(rset, geometry, encode, adresKodu);
                    array.add(dy);
                }
            } else {
                sql = "SELECT Y.YOL_ID, ISY.UAVT_CSBM_ID, Y.YOL_ADI,Y.XCOOR,Y.YCOOR,Y.ZOOMLEVEL,ISY.GEOLOC ";
                sql += "FROM IDARI_SINIR_YOL ISY, YOL Y ";
                sql += "WHERE (ISY.YOL_ID = Y.YOL_ID) AND ISY.UAVT_CSBM_ID=? ";
                if( isNotNullKeyword )
                    sql += " AND Y.KEYWORD LIKE ? "; 
                sql += " ORDER BY  NLSSORT(Y.YOL_ADI, 'nls_sort=xturkish') ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setLong(colno++, adresKodu);
                if ( isNotNullKeyword )
                  pstmt.setString(colno++, "%" + keyword + "%");
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    dy = DataYol.getInstance(rset, geometry, encode, adresKodu);
                    array.add(dy);
                }
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataYol[] dys = new DataYol[array.size()];
        for (int i = 0; i < dys.length; i++)
            dys[i] = (DataYol) array.get(i);
        return dys;

    } // getYolInfo()

    //-----------------------------------------------------------------------------

    public static DataKapi getKapiInfo(long kapiId, long adresKodu , boolean geometry, boolean encode) {
        DataKapi dk = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (adresKodu <= 0) {
                sql = "SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, IL_ID, IL_ADI, ";
                sql += "ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, YOL_ID, YOL_ADI, ZONE, NULL, UAVT_KAPI_ID, ";
                sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID  "+ (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "");
                sql += "FROM KAPI WHERE KAPI_ID=? ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setLong(1, kapiId);
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    dk = DataKapi.getInstance(rset, geometry, encode);
                }
            } else {
                sql = "SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, IL_ID, IL_ADI, ";
                sql += "ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID,  MAHALLE_ADI AS KOY_ADI, YOL_ID, YOL_ADI, ZONE, NULL, UAVT_KAPI_ID, ";
                sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID "+ (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "");
                sql += "FROM KAPI WHERE UAVT_KAPI_ID=? ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setLong(1, adresKodu);
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    dk = DataKapi.getInstance(rset, geometry, encode);
                }
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dk;
    } // getKapiInfo()

    //-----------------------------------------------------------------------------

    public static DataKapi[] getKapiList(long yolId, boolean geometry, boolean encode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, ";
            sql += "MAHALLE_ID, MAHALLE_ADI, KOY_ID, KOY_ADI, YOL_ID, YOL_ADI, ZONE, NULL, UAVT_KAPI_ID, ";
            sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID "+ (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "");
            sql += " FROM KAPI WHERE YOL_ID=?  ORDER BY NLSSORT(KAPI_NO, 'NLS_SORT=XTURKISH'), NLSSORT(KAPI_ADI, 'NLS_SORT=XTURKISH')";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, yolId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataKapi dk = DataKapi.getInstance(rset, geometry, encode);
                array.add(dk);
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

        DataKapi[] dks = new DataKapi[array.size()];
        for (int i = 0; i < dks.length; i++)
            dks[i] = (DataKapi) array.get(i);
        return dks;
    } // getKapiList()

    //-----------------------------------------------------------------------------

    public static DataKapi[] getKapiListWithExtent(Extent ext, boolean geometry, boolean encode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        int adrCount = 100;
        try {
            adrCount = Integer.parseInt(Utils.getParameter("adr_count"));
        } catch (Exception e) {
            ;
        }
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, ";
            sql += "MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, YOL_ID ,YOL_ADI, ZONE, NULL, UAVT_KAPI_ID, ";
            sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID "+ (geometry ? ",SDO_UTIL.SIMPLIFY(GEOLOC, 0.5)" : "");
            sql += " FROM KAPI WHERE SDO_ANYINTERACT(GEOLOC, (SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?)))) = 'TRUE'";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + adrCount + "  ORDER BY NLSSORT(KAPI_NO, 'NLS_SORT=XTURKISH'), NLSSORT(KAPI_ADI, 'NLS_SORT=XTURKISH')";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataKapi dk = DataKapi.getInstance(rset, geometry, encode);
                array.add(dk);
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

        DataKapi[] dks = new DataKapi[array.size()];
        for (int i = 0; i < dks.length; i++)
            dks[i] = (DataKapi) array.get(i);
        return dks;
    } // getKapiListWithExtent()

    //-----------------------------------------------------------------------------

    public static String[] getCategoryList(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DISTINCT CATEGORY FROM ((SELECT CATEGORY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND BRAND_ID=0) UNION ALL (SELECT CATEGORY FROM LBS_POI_CATEGORY WHERE BRAND_ID=0)) ORDER BY   NLSSORT(CATEGORY, 'NLS_SORT=XTURKISH') ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                array.add(category);
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

        String[] dcs = new String[array.size()];
        for (int i = 0; i < dcs.length; i++)
            dcs[i] = (String) array.get(i);
        return dcs;
    } // getCategoryList()

    //-----------------------------------------------------------------------------

    public static DataCategory[] getBrandList(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT CATEGORY,DESCRIPTION,BRAND_ID FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND BRAND_ID > 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString("CATEGORY");
                if (DataCategory.isInArray(array, category))
                    continue;

                String description = rset.getString("DESCRIPTION");
                long brandId = rset.getLong("BRAND_ID");
                array.add(new DataCategory(category, description, brandId));
            } // while()
            
            DbConn.closeDBConnection(pstmt, rset);
            
            sql = "SELECT CATEGORY,DESCRIPTION,BRAND_ID FROM LBS_POI_CATEGORY WHERE BRAND_ID > 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString("CATEGORY");
                if (DataCategory.isInArray(array, category))
                    continue;

                String description = rset.getString("DESCRIPTION");
                long brandId = rset.getLong("BRAND_ID");
                array.add(new DataCategory(category, description, brandId));
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

        DataCategory[] dbs = new DataCategory[array.size()];
        for (int i = 0; i < dbs.length; i++)
            dbs[i] = (DataCategory) array.get(i);
        for (int i = 0; i < dbs.length - 1; i++) {
            for (int j = (i + 1); j < dbs.length; j++) {
                if (dbs[i].getCategory().compareTo(dbs[j].getCategory()) > 0) {
                    DataCategory tmp = dbs[i];
                    dbs[i] = dbs[j];
                    dbs[j] = tmp;
                }
            } // for(j)
        } // for(i)
        return dbs;
    } // getBrandList()

    //-----------------------------------------------------------------------------

    public static int getPoiCount(String key, long ilId, long ilceId, long mahalleId, String regionId, String category, String brand) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, brand);
            if (sqlQuery == null)
                return -1;

            sqlQuery = sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>.* FROM", "COUNT(*) FROM");
            sql = sqlQuery;

            if (ilId > 0)
                sql += " AND P.IL_ID=?";
            if (ilceId > 0)
                sql += " AND P.ILCE_ID=?";
            if (mahalleId > 0)
                sql += " AND P.MAHALLE_ID=?";
            if (regionId != null)
                sql += " AND SDO_ANYINTERACT(P.GEOLOC, (SELECT GEOLOC FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=? ))='TRUE' ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (ilId > 0)
                pstmt.setLong(colno++, ilId);
            if (ilceId > 0)
                pstmt.setLong(colno++, ilceId);
            if (mahalleId > 0)
                pstmt.setLong(colno++, mahalleId);
            if (regionId != null) {
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, regionId);
            }
            rset = pstmt.executeQuery();
            rset.next();
            int count = rset.getInt(1);
            return count;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return -3;
    } // getPoiCount()

    //-----------------------------------------------------------------------------

    public static int getPoiCountWithExtent(String key, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, String category, String brand) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn =null;

        try {
            cnn = DbConn.getPooledConnection();
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, brand);
            if (sqlQuery == null)
                return -1;

            sqlQuery = sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>.* FROM", "COUNT(*) FROM");
            sql = sqlQuery;
            sql += " AND XCOOR >= ? AND XCOOR <= ? AND YCOOR >= ? AND YCOOR <= ?";

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, minLongitude);
            pstmt.setDouble(colno++, maxLongitude);
            pstmt.setDouble(colno++, minLatitude);
            pstmt.setDouble(colno++, maxLatitude);
            rset = pstmt.executeQuery();
            rset.next();
            int count = rset.getInt(1);
            return count;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return -3;
    } // getPoiCountWithExtent()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoi(String key, long[] idList, String category, String brand, boolean isFakeCoorNeeded) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String cbSql = null;
        DataPoi dp = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();
        try {
            DataPoiAttribute[] dpaList = getPoiAttributes(key);
    
            cnn = DbConn.getPooledConnection();
    
            if (category != null || brand != null)
                cbSql = DataCategory.getCategorySqlQuery(key, category, brand);
    
            for (int i = 0; i < idList.length; i++) {
    
                try {
                    if (category != null || brand != null) {
                        if (cbSql == null)
                            return null;
                        sql = cbSql.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                                             "P.*,P.GEOLOC.SDO_POINT.X XCOOR,P.GEOLOC.SDO_POINT.Y YCOOR FROM (SELECT ID,STANDARD_NAME,IL_ID,ILCE_ID,MAHALLE_ID,STREET_NAME,STREET_TYPE,MAHALLE_ADI,HSN,POSTAL_CODE,ILCE_ADI,IL_ADI,COUNTRY_CODE,AREA_CODE,TELEPHONE,SUB_TYPE," +
                                             (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(GEOLOC,?) " : "") + "GEOLOC");
                        sql += " AND ID=?) P";
                    } else {
                        sql = "SELECT P.*,P.GEOLOC.SDO_POINT.X XCOOR,P.GEOLOC.SDO_POINT.Y YCOOR FROM (SELECT ID,STANDARD_NAME,STREET_NAME,IL_ID,ILCE_ID,MAHALLE_ID,STREET_TYPE,MAHALLE_ADI,HSN,POSTAL_CODE,ILCE_ADI,IL_ADI,COUNTRY_CODE,AREA_CODE,TELEPHONE,SUB_TYPE," +
                            (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(GEOLOC,?) " : "") + "GEOLOC FROM POI WHERE ID=?) P";
                    }
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    int colno = 1;
                    if (isFakeCoorNeeded)
                        pstmt.setInt(colno++, fakeCoorSrid);
                    pstmt.setLong(colno++, idList[i]);
                    rset = pstmt.executeQuery();
                    while (rset.next()) {
                        dp = DataPoi.getInstance(rset);
                        if (dpaList != null && dpaList.length > 0) {
                            DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                            dp.setPoiAttributes(dpal);
                        }
                        array.add(dp);
                    } // while()
                } catch (Exception ex) {
                    Utils.showError("SQL: " + sql);
                    ex.printStackTrace();
                }finally{
                   DbConn.closeDBConnection(pstmt, rset);  
                }
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);

        return dps;
    } // getPoi()

    //-----------------------------------------------------------------------------

    public static DataPoi getPoi(String key, long id, String category, String brand, boolean isFakeCoorNeeded) {
        DataPoi dp = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (category != null || brand != null) {
                sql = DataCategory.getCategorySqlQuery(key, category, brand);
                if (sql == null)
                    return null;
                sql = sql.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                                   "P.*,P.GEOLOC.SDO_POINT.X XCOOR,P.GEOLOC.SDO_POINT.Y YCOOR FROM (SELECT ID,STANDARD_NAME,IL_ID,ILCE_ID,MAHALLE_ID,STREET_NAME,STREET_TYPE,MAHALLE_ADI,HSN,POSTAL_CODE,ILCE_ADI,IL_ADI,COUNTRY_CODE,AREA_CODE,TELEPHONE,SUB_TYPE," +
                                   (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(GEOLOC,?) " : "") + "GEOLOC");
                sql += " AND ID=?) P";
            } else {
                sql = "SELECT P.*,P.GEOLOC.SDO_POINT.X XCOOR,P.GEOLOC.SDO_POINT.Y YCOOR FROM (SELECT ID,STANDARD_NAME,IL_ID,ILCE_ID,MAHALLE_ID,STREET_NAME,STREET_TYPE,MAHALLE_ADI,HSN,POSTAL_CODE,ILCE_ADI,IL_ADI,COUNTRY_CODE,AREA_CODE,TELEPHONE,SUB_TYPE," +
                    (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(GEOLOC,?) " : "") + "GEOLOC FROM POI WHERE ID=?) P";
            }
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setLong(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                dp = DataPoi.getInstance(rset);
                DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                dp.setPoiAttributes(dpal);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dp;
    } // getPoi()
    
    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiGroup(String key, long id, boolean isFakeCoorNeeded) {
        DataPoi dp = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT P.*,P.GEOLOC.SDO_POINT.X XCOOR,P.GEOLOC.SDO_POINT.Y YCOOR FROM ";
            sql += "(SELECT ID,STANDARD_NAME,IL_ID,ILCE_ID,MAHALLE_ID,STREET_NAME,STREET_TYPE,MAHALLE_ADI,HSN,POSTAL_CODE,ILCE_ADI,IL_ADI,COUNTRY_CODE,AREA_CODE,TELEPHONE,SUB_TYPE," +
                (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(GEOLOC,?) " : "") + "GEOLOC FROM POI WHERE GROUP_ID=?) P";

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setLong(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                dp = DataPoi.getInstance(rset);
                DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                dp.setPoiAttributes(dpal);
                array.add(dp);
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);

        return dps;
    } // getPoiGroup()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiList(String key, long ilId, long ilceId, long mahalleId, String poiAdi, String category, String brand, String orderName, String orderType, String attOrderName, String attOrderType,
                                       String attWhereClause, boolean isFakeCoorNeeded, String regionId, double lat, double lon, int radius) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";
        Connection cnn = null;

        ArrayList array = new ArrayList();

        if (poiAdi != null)
            poiAdi = Utils.convToUpperEnglishChars(poiAdi);

        int poiCount = 100;
        try {
            poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        } catch (Exception e) {
            ;
        }

        if (ilId == 0 && ilceId == 0 && mahalleId == 0 && regionId == null)
            return null;

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        // ordering..
        if (orderName != null && orderName.equalsIgnoreCase("DATE")) {
            orderName = "GEOCODE_DATE";
            if (orderType == null || !orderType.equalsIgnoreCase("DESC"))
                orderType = "ASC";
        }

        // att ordering..
        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            cnn = DbConn.getPooledConnection();
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, brand);
            if (sqlQuery == null)
                return null;
            String replaceSql = "P.ID,P.GEOCODE_DATE,P.STANDARD_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE,P.XCOOR XCOOR_8307,P.YCOOR YCOOR_8307," +
                        (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC";
            replaceSql += (radius>0 )? ",((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE" : "";
            sqlQuery =  sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",replaceSql);
            sql = "SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR";
            sql += (radius>0 ) ? ",SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR_8307,IQ.YCOOR_8307,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE" : "";
            sql += " FROM (" + sqlQuery;
            if (ilId > 0)
                sql += " AND P.IL_ID=?";
            if (ilceId > 0)
                sql += " AND P.ILCE_ID=?";
            if (mahalleId > 0)
                sql += " AND P.MAHALLE_ID=?";
            if (poiAdi != null && poiAdi.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            if (regionId != null) {
                sql += " AND SDO_ANYINTERACT(P.GEOLOC, (SELECT GEOLOC FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=? ))='TRUE' ";
            }
            sql += ") IQ";

            // order and filter operations..
            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";
                }

                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";

                // ordering..
                if (attOrderName != null && attOrderName.length() > 0) {
                    sql += " AND L.ATTRIBUTE=?";
                   if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.STANDARD_NAME ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.STANDARD_NAME ASC ";
                }

                else {
                    if (orderName != null)
                        sql += " ORDER BY NLSSORT(A." + orderName + ",'NLS_SORT=XTURKISH') " + orderType;
                    else
                        sql += " ORDER BY NLSSORT(A.STANDARD_NAME, 'NLS_SORT=XTURKISH') ";
                }
                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                if (poiAdi != null && poiAdi.length() > 0) {
                    if (orderName != null)
                        sql += " ORDER BY NLSSORT(IQ." + orderName + ",'NLS_SORT=XTURKISH') " + orderType;
                    else
                        sql += " ORDER BY NLSSORT(IQ.STANDARD_NAME, 'NLS_SORT=XTURKISH') ";
                }
                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            }
            if(radius>0 ){
              sql += " AND REF_DISTANCE < ? ";
              sql += " ORDER BY DISTANCE";
            }
            
            Utils.showText("SQL: " + sql);
            if( radius>0 )
              Utils.showText("LON/LAT: " + lon + "," + lat + ", RADIUS: " +  (radius / 100000.0) * (radius / 100000.0) );
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            if(radius>0){
               pstmt.setDouble(colno++, lon);
               pstmt.setDouble(colno++, lat);
               pstmt.setDouble(colno++, lon);
               pstmt.setDouble(colno++, lon);
               pstmt.setDouble(colno++, lat);
               pstmt.setDouble(colno++, lat);
            }
            
            if (ilId > 0)
                pstmt.setLong(colno++, ilId);
            if (ilceId > 0)
                pstmt.setLong(colno++, ilceId);
            if (mahalleId > 0)
                pstmt.setLong(colno++, mahalleId);
            if (poiAdi != null && poiAdi.length() > 0)
                pstmt.setString(colno++, "%" + poiAdi + "%");
            if (regionId != null) {
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, regionId);
            }
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            if(radius>0){
              pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            }
            rset = pstmt.executeQuery();
            long poiId = 0;
            while (rset.next()) {
                DataPoi dp = DataPoi.getInstance(rset);
                if (poiId != dp.getId()) {
                    if (dpaList != null && dpaList.length > 0) {
                        DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                        dp.setPoiAttributes(dpal);
                    }
                    array.add(dp);
                }
                poiId = dp.getId();
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);


        return dps;
    } // getPoiList()

    //----------------------------------------------------------------------------

    public static DataPoi[] getPoiListEx(String key, long ilId, long ilceId, long mahalleId, String poiAdi, String category, String orderName, String orderType, String attOrderName, String attOrderType,
                                         String attWhereClause, boolean isFakeCoorNeeded, String regionId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";
        Connection cnn = null;

        ArrayList array = new ArrayList();

        if (poiAdi != null)
            poiAdi = Utils.convToUpperEnglishChars(poiAdi);

        int poiCount = 100;
        try {
            poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        } catch (Exception e) {
            ;
        }

        if (ilId == 0 && ilceId == 0 && mahalleId == 0 && regionId == null)
            return null;

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        // ordering..
        if (orderName != null && orderName.equalsIgnoreCase("DATE")) {
            orderName = "GEOCODE_DATE";
            if (orderType == null || !orderType.equalsIgnoreCase("DESC"))
                orderType = "ASC";
        }

        // attribute ordering..
        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            cnn = DbConn.getPooledConnection();
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, null);
            if (sqlQuery == null)
                return null;

            sqlQuery =  sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                                    "P.ID,P.GEOCODE_DATE,P.STANDARD_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE," +
                                    (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC");

            sql = "SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR";
            sql += " FROM (" + sqlQuery;
            if (ilId > 0)
                sql += " AND P.IL_ID=?";
            if (ilceId > 0)
                sql += " AND P.ILCE_ID=?";
            if (mahalleId > 0)
                sql += " AND P.MAHALLE_ID=?";
            if (poiAdi != null && poiAdi.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            if (regionId != null) {
                sql += " AND SDO_ANYINTERACT(P.GEOLOC, (SELECT GEOLOC FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=? ))='TRUE' ";
            }
            sql += ") IQ";

            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";

                }
                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";


                if (dpaList != null && dpaList.length > 0 && attOrderName != null) {
                    sql += " AND L.ATTRIBUTE=?";
                    if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.STANDARD_NAME ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.STANDARD_NAME ASC ";
                }

                else {
                    if (orderName != null)
                        sql += " ORDER BY A." + orderName + " " + orderType;
                    else
                        sql += " ORDER BY  NLSSORT(A.STANDARD_NAME, 'NLS_SORT=XTURKISH') ";
                }

                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                if (poiAdi != null && poiAdi.length() > 0) {
                    if (orderName != null)
                        sql += " ORDER BY IQ." + orderName + " " + orderType;
                    else
                        sql += " ORDER BY NLSSORT(IQ.STANDARD_NAME, 'NLS_SORT=XTURKISH') ";
                }
                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            }
            Utils.showText("SQL: " + sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            if (ilId > 0)
                pstmt.setLong(colno++, ilId);
            if (ilceId > 0)
                pstmt.setLong(colno++, ilceId);
            if (mahalleId > 0)
                pstmt.setLong(colno++, mahalleId);
            if (poiAdi != null && poiAdi.length() > 0)
                pstmt.setString(colno++, "%" + poiAdi + "%");
            if (regionId != null) {
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, regionId);
            }
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataPoi dp = DataPoi.getInstance(rset);
                if (dpaList != null && dpaList.length > 0) {
                    DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                    dp.setPoiAttributes(dpal);
                }
                array.add(dp);
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);
        return dps;
    } // getPoiListEx()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiSearch(String key, double latitude, double longitude, int radius, String keyword, String category, String brand, String attOrderName, String attOrderType,
                                         String attWhereClause, boolean isFakeCoorNeeded, int markerName, int poiCount, int brandGroup) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (keyword != null)
            keyword = Utils.convToUpperEnglishChars(keyword);

        try {
            cnn = DbConn.getPooledConnection();
          //  System.out.println(cnn.getMetaData());
        } catch (Exception e) {
            ;
        }

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            String sqlQuery = null;
            if( category != null && category.length()>0 && category.contains(",")){
                sqlQuery = DataCategory.getCategoriesSqlQuery(key, category, brand);  
            }else{
              sqlQuery = DataCategory.getCategorySqlQuery(key, category, brand);
            }
            if (sqlQuery == null)
                return null;
            String sqlSelect="P.ID,P.STANDARD_NAME,P.BRAND_NAME1 BRAND_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE,P.XCOOR XCOOR_8307,P.YCOOR YCOOR_8307," +
                                    (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC,((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE";
            
            sqlQuery = sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",markerName==1 ? " P.BRAND_STYLE || P.SUB_TYPE_STYLE MARKER_NAME, "+ sqlSelect : sqlSelect);
            sql = "SELECT * FROM (SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR_8307,IQ.YCOOR_8307,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (" + sqlQuery;
            if (keyword != null && keyword.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            sql += " ORDER BY REF_DISTANCE) IQ WHERE REF_DISTANCE < ?) WHERE ROWNUM <= ?";
            sql += " ORDER BY DISTANCE";

            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";

                }
                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";

                // ordering..
                if (attOrderName != null) {
                    sql += " AND L.ATTRIBUTE=?";
                    if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.DISTANCE ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.DISTANCE ASC ";
                }

                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                sql = "SELECT * FROM (" + sql + ") X WHERE ROWNUM <= " + poiCount;
            }

            Utils.showText("POI SQL: " + sql);
            Utils.showText("LON/LAT: " + longitude + "," + latitude + ", RADIUS: " +  (radius / 100000.0) * (radius / 100000.0) );
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            if (keyword != null && keyword.length() > 0) {
                pstmt.setString(colno++, "%" + keyword + "%");
            }
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            pstmt.setInt(colno++, poiCount * 3);
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            rset = pstmt.executeQuery();
            long poiId = 0;
            if(brandGroup == 1){
                while (rset.next()) {
                   DataPoi dp = DataPoi.getInstance(rset, brandGroup);
                   array.add(dp);
                } // while()
            }else{
                while (rset.next()) {
                    DataPoi dp = DataPoi.getInstance(rset);
                    dp.setBrandName(rset.getString("BRAND_NAME"));
                    if( markerName==1 )
                      dp.setMarkerName(rset.getString("MARKER_NAME"));
                    if (poiId != dp.getId()) {
                        if (dpaList != null && dpaList.length > 0) {
                            DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                            dp.setPoiAttributes(dpal);
                        }
                        array.add(dp);
                    }
                    poiId = dp.getId();
                } // while()   
            }
           
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataPoi[] dps = new DataPoi[array.size()];
        array.toArray(dps);
      /*  for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);*/
        return dps;
    } // getPoiSearch()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiSearchEx(String key, double latitude, double longitude, int radius, int zone, String keyword, String category, String attOrderName, String attOrderType, String attWhereClause,
                                           boolean isFakeCoorNeeded) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";
        Connection cnn = null;

        ArrayList array = new ArrayList();

        if (keyword != null)
            keyword = Utils.convToUpperEnglishChars(keyword);

        int poiCount = 100;
        try {
            poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        } catch (Exception e) {
            ;
        }

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, null);
            if (sqlQuery == null)
                return null;
            
            cnn = DbConn.getPooledConnection();

            sqlQuery = sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                                    "P.ID,P.STANDARD_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE,P.XCOOR XCOOR_8307,P.YCOOR YCOOR_8307," +
                                    (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC,((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE");

            sql =
                "SELECT * FROM (SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR_8307,IQ.YCOOR_8307,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (" + sqlQuery + " AND P.ZONE IN (0,?)";
            if (keyword != null && keyword.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            sql += " ORDER BY REF_DISTANCE) IQ WHERE REF_DISTANCE < ?) WHERE ROWNUM <= ?";
            sql += " ORDER BY DISTANCE";

            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";
                }
                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";

                // ordering..
                if (attOrderName != null) {
                    sql += " AND L.ATTRIBUTE=?";
                    if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.DISTANCE ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.DISTANCE ASC ";
                }

                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                sql = "SELECT * FROM (" + sql + ") X WHERE ROWNUM <= " + poiCount;
            }

            Utils.showText("POI SQL: " + sql);
            Utils.showText("LON/LAT: " + longitude + "," + latitude + ", RADIUS: " + radius + ", ZONE: " + zone);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setInt(colno++, zone);
            if (keyword != null && keyword.length() > 0) {
                pstmt.setString(colno++, "%" + keyword + "%");
            }
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            pstmt.setInt(colno++, poiCount * 3);
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataPoi dp = DataPoi.getInstance(rset);
                if (dpaList != null && dpaList.length > 0) {
                    DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                    dp.setPoiAttributes(dpal);
                }
                array.add(dp);
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);
        return dps;
    } // getPoiSearchEx()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiSearchWithExtent(String key, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, String keyword, String category, String brand, String attOrderName,
                                                   String attOrderType, String attWhereClause, boolean isFakeCoorNeeded) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";
        Connection cnn = null;

        ArrayList array = new ArrayList();

        if (keyword != null)
            keyword = Utils.convToUpperEnglishChars(keyword);

        int poiCount = 100;
        try {
            poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        } catch (Exception e) {
            ;
        }

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, brand);
            if (sqlQuery == null)
                return null;
            
            cnn = DbConn.getPooledConnection();

            sqlQuery = sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                            "P.ID,P.STANDARD_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE,P.XCOOR XCOOR_8307,P.YCOOR YCOOR_8307," +
                            (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC,((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE");

            double latitude = (minLatitude + maxLatitude) / 2.0;
            double longitude = (minLongitude + maxLongitude) / 2.0;

            sql =
                "SELECT * FROM (SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR_8307,IQ.YCOOR_8307,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (" + sqlQuery + " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ?";
            if (keyword != null && keyword.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            sql += " ORDER BY REF_DISTANCE) IQ) WHERE ROWNUM <= ?";
            sql += " ORDER BY DISTANCE";

            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";
                }
                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";

                // ordering..
                if (attOrderType != null) {
                    sql += " AND L.ATTRIBUTE=?";
                    if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.DISTANCE ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.DISTANCE ASC ";
                }

                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                sql = "SELECT * FROM (" + sql + ") X WHERE ROWNUM <= " + poiCount;
            }
            Utils.showText("SQL: " + sql);
            Utils.showText("EXT: " + minLongitude + "," + minLatitude + " - " + maxLongitude + "," + maxLatitude);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, minLongitude);
            pstmt.setDouble(colno++, maxLongitude);
            pstmt.setDouble(colno++, minLatitude);
            pstmt.setDouble(colno++, maxLatitude);
            if (keyword != null && keyword.length() > 0) {
                pstmt.setString(colno++, "%" + keyword + "%");
            }
            pstmt.setInt(colno++, poiCount * 3);
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataPoi dp = DataPoi.getInstance(rset);
                if (dpaList != null && dpaList.length > 0) {
                    DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                    dp.setPoiAttributes(dpal);
                }
                array.add(dp);
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);
        return dps;
    } // getPoiSearchWithExtent()

    //-----------------------------------------------------------------------------

    public static DataPoi[] getPoiSearchExWithExtent(String key, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, String keyword, String category, String attOrderName,
                                                     String attOrderType, String attWhereClause, boolean isFakeCoorNeeded) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String decodeSql = "";

        Connection cnn = null;

        ArrayList array = new ArrayList();

        if (keyword != null)
            keyword = Utils.toCompareCase(keyword);

        int poiCount = 100;
        try {
            poiCount = Integer.parseInt(Utils.getParameter("poi_count"));
        } catch (Exception e) {
            ;
        }

        DataPoiAttribute[] dpaList = getPoiAttributes(key);

        int attOrderColumnType = 0;
        String columnName = null;
        String attColumnName = null;

        if (dpaList != null && dpaList.length > 0 && attOrderName != null) {

            attOrderColumnType = checkAttributeType(key, attOrderName);
            if (attOrderColumnType == 1)
                columnName = "NUMBER_VALUE";
            if (attOrderColumnType == 2)
                columnName = "STRING_VALUE";
            if (attOrderColumnType == 3)
                columnName = "DATE_VALUE";

            if (attOrderType == null || !attOrderType.equalsIgnoreCase("DESC"))
                attOrderType = "ASC";
        }

        try {
            String sqlQuery = DataCategory.getCategorySqlQuery(key, category, null);
            if (sqlQuery == null)
                return null;
            
            cnn = DbConn.getPooledConnection();
            
            sqlQuery =  sqlQuery.replaceAll("\\<STANDARD_COLUMN_LIST\\>",
                            "P.ID,P.STANDARD_NAME,P.IL_ID,P.ILCE_ID,P.MAHALLE_ID,P.STREET_NAME,P.STREET_TYPE,P.MAHALLE_ADI,P.HSN,P.POSTAL_CODE,P.ILCE_ADI,P.IL_ADI,P.COUNTRY_CODE,P.AREA_CODE,P.TELEPHONE,P.SUB_TYPE,P.XCOOR XCOOR_8307,P.YCOOR YCOOR_8307," +
                            (isFakeCoorNeeded ? "SDO_CS.TRANSFORM(P.GEOLOC,?) " : "P.") + "GEOLOC,((?-P.XCOOR)*(?-P.XCOOR)+(?-P.YCOOR)*(?-P.YCOOR)) REF_DISTANCE");

            double latitude = (minLatitude + maxLatitude) / 2.0;
            double longitude = (minLongitude + maxLongitude) / 2.0;

            sql =  "SELECT * FROM (SELECT IQ.*,IQ.GEOLOC.SDO_POINT.X XCOOR,IQ.GEOLOC.SDO_POINT.Y YCOOR,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR_8307,IQ.YCOOR_8307,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (" + sqlQuery + " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ?";
            if (keyword != null && keyword.length() > 0) {
                sql += " AND UPPERENG_DETERMINISTIC(P.KEYWORD) LIKE ?";
            }
            sql += " ORDER BY REF_DISTANCE) IQ) WHERE ROWNUM <= ?";
            sql += " ORDER BY DISTANCE";

            if (dpaList != null && dpaList.length > 0) {

                decodeSql += ",";

                for (int i = 0; i < dpaList.length; i++) {

                    if (dpaList[i].type == 1)
                        attColumnName = "NUMBER_VALUE";
                    if (dpaList[i].type == 2)
                        attColumnName = "STRING_VALUE";
                    if (dpaList[i].type == 3)
                        attColumnName = "DATE_VALUE";

                    if ((dpaList.length - 1) == i)
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\"";
                    else
                        decodeSql += " DECODE(L.ATTRIBUTE, '" + dpaList[i].attribute + "' , L." + attColumnName + ") AS \"" + dpaList[i].attribute + "\",";
                }
                sql = " SELECT A.* " + decodeSql + " FROM (" + sql + ") A";
                sql += " LEFT JOIN LBS_POI_ATTRIBUTE L ON  L.POI_ID = A.ID AND L.KEY=?";

                // ordering..
                if (attOrderName != null) {
                    sql += " AND L.ATTRIBUTE=?";
                    if (attOrderColumnType == 1)
                        sql += "  ORDER BY NVL(L." + columnName + ", 0) " + attOrderType + ", A.DISTANCE ASC ";
                    else
                        sql += " ORDER BY " + columnName + " " + attOrderType + " NULLS LAST, A.DISTANCE ASC ";
                }

                // filtering..
                if (attWhereClause != null && attWhereClause.length() > 0) {
                    sql = "SELECT * FROM (" + sql + ") B WHERE " + attWhereClause;
                }

                sql = " SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + poiCount;
            } else {
                sql = "SELECT * FROM (" + sql + ") X WHERE ROWNUM <= " + poiCount;
            }
            Utils.showText("SQL: " + sql);
            Utils.showText("EXT: " + minLongitude + "," + minLatitude + " - " + maxLongitude + "," + maxLatitude);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            if (isFakeCoorNeeded)
                pstmt.setInt(colno++, fakeCoorSrid);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, minLongitude);
            pstmt.setDouble(colno++, maxLongitude);
            pstmt.setDouble(colno++, minLatitude);
            pstmt.setDouble(colno++, maxLatitude);
            if (keyword != null && keyword.length() > 0) {
                pstmt.setString(colno++, "%" + keyword + "%");
            }
            pstmt.setInt(colno++, poiCount * 3);
            if (dpaList != null && dpaList.length > 0) {
                pstmt.setString(colno++, key);
            }
            if (attOrderName != null) {
                pstmt.setString(colno++, attOrderName);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataPoi dp = DataPoi.getInstance(rset);
                if (dpaList != null && dpaList.length > 0) {
                    DataPoiAttribute[] dpal = getPoiAttributes(key, dp.id);
                    dp.setPoiAttributes(dpal);
                }
                array.add(dp);
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

        DataPoi[] dps = new DataPoi[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoi) array.get(i);
        return dps;
    } // getPoiSearchExWithExtent()

    //-----------------------------------------------------------------------------

    public static double[] getCoordinate(long ilId, long ilceId, long mahalleId, long yolId, long kapiId, long poiId) {
        double[] coors = new double[2];
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (poiId > 0)
                sql = "SELECT XCOOR,YCOOR FROM POI WHERE ID=?";
            else if (kapiId > 0)
                sql = "SELECT XCOOR,YCOOR FROM KAPI WHERE KAPI_ID=?";
            else if (yolId > 0)
                sql = "SELECT XCOOR,YCOOR FROM YOL WHERE YOL_ID=?";
            else if (mahalleId > 0)
                sql = "SELECT XCOOR,YCOOR FROM MAHALLE WHERE MAHALLE_ID=?";
            else if (ilceId > 0)
                sql = "SELECT XCOOR,YCOOR FROM ILCE WHERE ILCE_ID=?";
            else if (ilId > 0)
                sql = "SELECT XCOOR,YCOOR FROM IL WHERE IL_ID=?";
            else
                return null;

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            if (poiId > 0)
                pstmt.setLong(1, poiId);
            else if (kapiId > 0)
                pstmt.setLong(1, kapiId);
            else if (yolId > 0)
                pstmt.setLong(1, yolId);
            else if (mahalleId > 0)
                pstmt.setLong(1, mahalleId);
            else if (ilceId > 0)
                pstmt.setLong(1, ilceId);
            else if (ilId > 0)
                pstmt.setLong(1, ilId);
            else
                ;
            rset = pstmt.executeQuery();
            if (rset.next()) {
                coors[0] = rset.getDouble(1);
                coors[1] = rset.getDouble(2);
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return coors;
    } // getCoordinate()

    //-----------------------------------------------------------------------------

    public static double getElevation(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT CONTOUR FROM YUKSEKLIK_EGRI_TURKIYE WHERE SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL),'SDO_NUM_RES=1',1) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                double altitude = rset.getDouble(1);
                return altitude;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return -1999.99;
    } // getElevation()

   public static DataSuggestion[] getSuggestionsServlet(String suggServletUrl,String keyword, int count, boolean isCalculateDistance) {
        String addressUrl = null;
        Connection cnn = null;
        URL lurl=null;
        HttpURLConnection connection = null;
        String inputLine = null;
        BufferedReader in = null;
        StringBuilder response = null;
        ArrayList array = new ArrayList();
        DataSuggestion[] dsgs = null;
        try {
            response = new StringBuilder();
            addressUrl = suggServletUrl + "?Keyword=" + URLEncoder.encode(keyword, "UTF-8")+ "&Count="+count; 
            Utils.showText("addressUrl: " + addressUrl);
            lurl = new URL(addressUrl);
            connection = (HttpURLConnection) lurl.openConnection();
            // optional default is GET
            connection.setRequestMethod("GET");
            //add request header
            connection.setRequestProperty("User-Agent", Operations.USER_AGENT);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            connection.setConnectTimeout(30000);
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8));

            while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine.toString());
            }
            if(response!=null){
                JSONArray jsonA = new JSONArray(response.toString());
                if(isCalculateDistance){
                    cnn = DbConn.getPooledConnection();
                }
                for (int i = 0; i < jsonA.length(); i++) {
                    String name = null;
                    JSONObject  jsonObj = jsonA.getJSONObject(i);
                    if(jsonObj!=null){
                        try{
                          name = jsonObj.getString("name");
                        }catch (Exception e){
                           Utils.showError("name" + e.getMessage() );
                         }
                        
                        String fromTable = null;
                        try{
                            fromTable = jsonObj.getString("ft");
                        }catch (Exception e){
                           Utils.showError("fromTable" + e.getMessage() );
                        }
                        
                        Long id = 0L;
                        try{
                            id = jsonObj.getLong("id");
                        }catch (Exception e){
                           Utils.showError("id" + e.getMessage() );
                        }
                        
                       double longitude = 0.00;
                        try{
                            longitude = jsonObj.getDouble("x");
                        }catch (Exception e){
                           Utils.showError("longitude" + e.getMessage() );
                        }
                        
                        double latitude = 0.00;
                         try{
                             latitude = jsonObj.getDouble("y");
                         }catch (Exception e){
                            Utils.showError("latitude" + e.getMessage() );
                         }
                         
                        double distance = 0.00;
                        if( isCalculateDistance && id>0 && latitude != 0.00 && longitude != 0.00 ){
                            distance = Utils.getDistanceFromIdariSinirYol(cnn, id,  latitude, longitude);
                        }
                        DataSuggestion dsg = new DataSuggestion(id, fromTable,name, latitude, longitude, distance);
                        array.add(dsg);
                    }
                  
                }
            }
            if (array.size() <= 0){
                return null;
            }
            
            if(isCalculateDistance){
                Collections.sort(array, new Comparator<DataSuggestion>() {
                    @Override
                    public int compare(DataSuggestion ds1, DataSuggestion ds2) {
                        return Double.compare(ds1.getDistance(), ds2.getDistance());
                    }
                });
            }
            dsgs = new DataSuggestion[array.size()];
        } catch (Exception e) {
            e.printStackTrace();
            Utils.showError("getSuggestionsServlet" + addressUrl );
            return null;
        }finally{
            DbConn.closeHttpConn(connection, null, null, in);
            DbConn.closeConnection(cnn);
        }
        
        for (int i = 0; i < dsgs.length; i++){
            dsgs[i] = (DataSuggestion) array.get(i);
        }
        
        return dsgs;             
    } 
    //-----------------------------------------------------------------------------

    public static DataSuggestion[] getSuggestions(String keyword, int count, double fromLatitute, double fromLongitude, int sxt, String dataset) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();
        String[] keywords = Utils.getSplitKeywords(keyword.replaceAll("%", "").replaceAll("'", ""));

        String sqlDistance = "0.00 DISTANCE";
        
        boolean isExistCoors = (fromLatitute != 0.00 && fromLongitude != 0.00);
        boolean isReCalculateDistance = false;
        if (isExistCoors) {
            sqlDistance = " SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(XCOOR,YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001, 'unit=M') DISTANCE ";
        }

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * \n" + " FROM (SELECT IQ.*, " + sqlDistance + " FROM (SELECT /*+index(INX_SUGGESTION_3)*/ ID, FROM_TABLE,TEXT,SUFFIX,XCOOR,YCOOR, PRIORITY FROM SUGGESTION WHERE TEXT_KEYWORD LIKE ? ";
            sql +=  (keywords.length > 1 ? "AND SUFFIX_KEYWORD LIKE ? " : "") + (dataset != null ? "AND FROM_TABLE = ? " : "");
            sql += " ORDER BY PRIORITY) IQ ORDER BY DISTANCE, IQ.PRIORITY) WHERE ROWNUM <= ?"; 

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            Utils.showText("SQL: " + sql);
            int colno = 1;
            if (isExistCoors) {
                pstmt.setDouble(colno++, fromLongitude);
                pstmt.setDouble(colno++, fromLatitute);
            }

            pstmt.setString(colno++, (sxt > 0 ? "" : "%") + keywords[0] + "%");
            if (keywords.length > 1)
                pstmt.setString(colno++, "%" + keywords[1] + "%");
            if (dataset != null)
                pstmt.setString(colno++, dataset.toUpperCase());
            pstmt.setInt(colno++, count);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                long id = rset.getLong(1);
                String fromTable = rset.getString(2);
                String name = rset.getString(3);
                if ("KAPI".equals(fromTable))
                    name = "NO: " + name;
                String suffix = rset.getString(4);
                double longitude = rset.getDouble(5);
                double latitude = rset.getDouble(6);
                double distance = rset.getDouble(8);
                if(isExistCoors && "YOL".equals(fromTable)){
                   distance = Utils.getDistanceFromIdariSinirYol(cnn, id,  fromLatitute, fromLongitude);
                   if(!isReCalculateDistance){
                    isReCalculateDistance = true;
                   }
                }
                DataSuggestion dsg = new DataSuggestion(id, fromTable, Utils.toFineCase(name + (suffix != null ? ", " + suffix : "")), latitude, longitude, distance);
                array.add(dsg);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            Utils.showError("getSuggestions" + ex.getMessage() );
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;
        
        if(isReCalculateDistance && isExistCoors){
            Collections.sort(array, new Comparator<DataSuggestion>() {
                @Override
                public int compare(DataSuggestion ds1, DataSuggestion ds2) {
                    return Double.compare(ds1.getDistance(), ds2.getDistance());
                }
            });
        }

        DataSuggestion[] dsgs = new DataSuggestion[array.size()];
        for (int i = 0; i < dsgs.length; i++)
            dsgs[i] = (DataSuggestion) array.get(i);
       
        return dsgs;
    } 

    //-----------------------------------------------------------------------------

    public static DataPoint[] getOrderByDistance(double latitude, double longitude, DataPoint[] dps, boolean routeAnalysis, String consList, String criteria) {
        DataPoint dp = null;
        try {
            for (int i = 0; i < dps.length; i++) {
                dp = dps[i];
                double distance = 0.00;
                if (routeAnalysis)
                    distance = calculateShortestDistance(latitude, longitude, dp.getLatitude(), dp.getLongitude(), consList, criteria);
                else
                    distance = calculateDistance(latitude, longitude, dp.getLatitude(), dp.getLongitude());
                dp.setDistance(distance);
            } // for()

            for (int i = 0; i < dps.length - 1; i++) {
                for (int j = i + 1; j < dps.length; j++) {
                    if (dps[i].getDistance() > dps[j].getDistance()) {
                        dp = dps[i];
                        dps[i] = dps[j];
                        dps[j] = dp;
                    }
                } // for(j)
            } // for(i)
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return dps;
    } // getOrderByDistance()

    //-----------------------------------------------------------------------------

    public static DataDistance[] getDistanceMatrix(DataPoint[] dps, boolean routeAnalysis, String consList, String criteria) {
        ArrayList array = new ArrayList();

        try {
            for (int i = 0; i < dps.length - 1; i++) {
                for (int j = i + 1; j < dps.length; j++) {
                    double distance = 0.00;
                    if (routeAnalysis)
                        distance = calculateShortestDistance(dps[i].getLatitude(), dps[i].getLongitude(), dps[j].getLatitude(), dps[j].getLongitude(), consList, criteria);
                    else
                        distance = calculateDistance(dps[i].getLatitude(), dps[i].getLongitude(), dps[j].getLatitude(), dps[j].getLongitude());
                    array.add(new DataDistance(dps[i], dps[j], distance));
                } // for(j)
            } // for(i)
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (array.size() <= 0)
            return null;

        DataDistance[] dms = new DataDistance[array.size()];
        for (int i = 0; i < dms.length; i++)
            dms[i] = (DataDistance) array.get(i);
        return dms;
    } // getDistanceMatrix()

    //-----------------------------------------------------------------------------

    public static double getDistance(double fromLatitude, double fromLongitude, double toLatitude, double toLongitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),0.001,'unit=M') FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, fromLongitude);
            pstmt.setDouble(colno++, fromLatitude);
            pstmt.setDouble(colno++, toLongitude);
            pstmt.setDouble(colno++, toLatitude);
            rset = pstmt.executeQuery();
            rset.next();
            double distance = rset.getDouble(1);
            return distance;

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return 0.00;
    } // getDistance()
    //----------------------------------------------------------------------------
            public static  DataIl[] getIlList(long pathId) {
            PreparedStatement pstmt = null;
            ResultSet rset = null;
            String sql = null;
            Connection cnn = null;

            ArrayList array = new ArrayList();

            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT IL_ID, IL_ADI FROM IL WHERE SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM NET_PATHS WHERE PATH_ID=?))='TRUE'";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setLong(1, pathId);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    DataIl ilData = DataIl.getInstance(rset);
                    array.add(ilData);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }

            if (array.size() <= 0)
                return null;

            DataIl[] dups = new DataIl[array.size()];
            for (int i = 0; i < dups.length; i++)
                dups[i] = (DataIl) array.get(i);
            return dups;
        } // getIlList()
       //----------------------------------------------------------------------------
        public static boolean isSameCoors(DataPoint[] dps ) {
           boolean sameCoors = true; 
           
           if( dps.length<2 ) sameCoors = false; 
           
           for(int i=0;i<dps.length-1;i++){
                if( (dps[i].getLatitude()!= dps[i+1].getLatitude()) || (  dps[i].getLongitude()!=  dps[i+1].getLongitude())){
                    sameCoors = false;
                    break;
                }
            }
           return sameCoors;
        }
   //----------------------------------------------------------------------------
   public static DataGlobalRoute getGlobalRoute(String addressUrl, int type, DataPoint[] dps, boolean dirProduce, boolean geometry, boolean encode) {
       DataGlobalRoute dataRota=new DataGlobalRoute();
       dataRota.setEncode(encode);
       if( type == 2){
          addressUrl = addressUrl +"&origin="+dps[0].getLatitude()+","+dps[0].getLongitude();
          String via = "";
           for(int i=1; i<dps.length-1; i++){
               via +="&via="+dps[i].getLatitude()+","+dps[i].getLongitude();
           }
           addressUrl = addressUrl + via;
           addressUrl = addressUrl +"&destination="+dps[dps.length-1].getLatitude()+","+dps[dps.length-1].getLongitude();
       }
       
       String res = Utils.commonGetRequest(addressUrl);
       if(Utils.isStringDataNull(res) ){
        return dataRota;
       }
       try {
           JSONObject jsonres = new JSONObject(res);
           JSONArray routes = Utils.getJSONArrayValueFromJSONObject(jsonres, "routes", false);
           if (routes != null && routes.length() > 0) {
               JSONObject routesRes = routes.getJSONObject(0);
               
               if(routesRes!=null){
                   List<Double> coors=new ArrayList();
                   JSONArray sections = Utils.getJSONArrayValueFromJSONObject(routesRes, "sections", false);
                   double pathDistance = 0, pathDuration = 0, pathDurationWithTmcFlow =0;
                   if (sections != null && sections.length() > 0) {
                       List<DataGlobalDirection> dataDirections=new ArrayList();

                       for(int i=0; i<sections.length(); i++){
                           JSONObject contentObject=sections.getJSONObject(i);
                              if(contentObject!=null){
                               DataGlobalDirection dataDirection=new DataGlobalDirection();  
                               JSONObject summary = Utils.getJSONObjectValueFromJSONObject(contentObject, "summary", false);
                               String id = Utils.getStringValueFromJSONObject(contentObject, "id", false);
                               dataDirection.setId(id);
                               String polyline = Utils.getStringValueFromJSONObject(contentObject, "polyline", false);
                               dataDirection.setPolyline(polyline);
                               if(summary!=null){
                                    dataDirection.setLinkId(i);
                                  // dataDirection.setDirection(instruction);
                                   double length = Utils.getDoubleValueFromJSONObject(summary, "length", false);
                                   pathDistance += length;
                                   dataDirection.setDistance(length);
                                   double duration = Utils.getDoubleValueFromJSONObject(summary, "baseDuration", false);
                                   duration = duration/60; // sn-->dk
                                   dataDirection.setDuration(duration);
                                   pathDuration += duration;
                                   double typicalDuration = Utils.getDoubleValueFromJSONObject(summary, "typicalDuration", false);
                                   pathDurationWithTmcFlow += typicalDuration;    
                                   dataDirections.add(dataDirection);
                                   if(dirProduce)
                                   dataRota.setDirections(dataDirections.toArray(new DataGlobalDirection[dataDirections.size()]));
                                 }   
                               
                               if(geometry){
                                   JSONObject departure = Utils.getJSONObjectValueFromJSONObject(contentObject, "departure", true);
                                   if(departure!=null){
                                     JSONObject place = Utils.getJSONObjectValueFromJSONObject(departure, "place", true);
                                      if(place!=null){
                                          JSONObject originalLocation = Utils.getJSONObjectValueFromJSONObject(place, "originalLocation", true);
                                          if(originalLocation!=null)
                                          coors.add(Utils.getDoubleValueFromJSONObject(originalLocation, "lng", true));
                                          coors.add(Utils.getDoubleValueFromJSONObject(originalLocation, "lat", true));
                                      }
                                   }
                               }
                               
                           }
                       }
               }
               double[] target = new double[coors.size()];
                for (int i = 0; i < target.length; i++) {
                   target[i] = coors.get(i);
                }  
               dataRota.setCoors(target);                   
               dataRota.setPathDistance(pathDistance);
               dataRota.setPathDuration(pathDuration);
               dataRota.setPathDurationWithTmcFlow(pathDurationWithTmcFlow);
               }

           }
       } catch (Exception ex) {
           Utils.showError("getGlobalRoute "+ ex.getMessage());
       }  
   return dataRota;
   }
        public static DataRota getRotaWithHere(String appId,String appCode,DataPoint[] dps,String criteria,boolean dirProduce,boolean geometry, boolean encode) {
            DataRota dataRota=new DataRota();
            dataRota.setEncode(encode);
            if(dps==null){
                return null;
            }
            
            if(dps.length<2){
                return null;
            }
            
            String waypoint="";
            for(int i=0;i<dps.length;i++){
                waypoint+="&waypoint"+i+"="+dps[i].getLatitude()+","+dps[i].getLongitude();
            }
            
            String url = Utils.getParameter("here_url");
            
            if(criteria!=null){
                if(criteria.equalsIgnoreCase("short")){
                    criteria="shortest;car";
                }else{
                    criteria="fastest;car;";
                    }
            }
            url+="?app_id="+appId+"&app_code="+appCode+waypoint+"&mode="+criteria+"traffic:disabled&language=tr-tr";
                    
            URL obj=null;
            try {
                obj = new URL(url);
                
                HttpURLConnection con = (HttpURLConnection) obj.openConnection();

               // optional default is GET
               con.setRequestMethod("GET");

               //add request header
               con.setRequestProperty("User-Agent", Operations.USER_AGENT);

               int responseCode = con.getResponseCode();
               System.out.println("\nSending 'GET' request to URL : " + url);
               System.out.println("Response Code : " + responseCode);

               BufferedReader in = new BufferedReader(
                       new InputStreamReader(con.getInputStream(),"UTF-8"));
               String inputLine;
               StringBuffer response = new StringBuffer();
               List<Double> coors=new ArrayList();
               while ((inputLine = in.readLine()) != null) {
                       response.append(inputLine);
               }
               in.close();

                if(response!=null){
                    JSONObject jsonres=new JSONObject(response.toString());
                    String iterKey = null;
                    Iterator iter = jsonres.keys();
                    while (iter.hasNext()) {
                        iterKey = (String) iter.next();
                        if (iterKey.equals("response")){
                            JSONObject hereJsonObjectResponse=jsonres.getJSONObject(iterKey);
                            if(hereJsonObjectResponse!=null){
                                JSONArray hereJsonArrayRoute=hereJsonObjectResponse.getJSONArray("route");
                                if(hereJsonArrayRoute!=null && hereJsonArrayRoute.length()>0){
                                        JSONObject eachJsonObject=hereJsonArrayRoute.getJSONObject(0);
                                        if(eachJsonObject!=null){
                                            JSONArray hereRouteLegArray=eachJsonObject.getJSONArray("leg");
                                            if(hereRouteLegArray!=null){
                                                List<DataDirection> dataDirections=new ArrayList();
                                                int id=0;

                                                for(int y=0;y<hereRouteLegArray.length();y++){
                                                    JSONObject legJsonObject=hereRouteLegArray.getJSONObject(y);
                                                        if(legJsonObject!=null){
                                                        JSONArray legManevuerJsonArray= legJsonObject.getJSONArray("maneuver");

                                                        if(legManevuerJsonArray!=null){
                                                        for(int j=0;j<legManevuerJsonArray.length();j++){
                                                            JSONObject object=legManevuerJsonArray.getJSONObject(j);
                                                            String instruction=null;
                                                            if(object.getString("instruction")!=null){
                                                                instruction=object.getString("instruction").replaceAll("</span>", "");
                                                                instruction=instruction.replaceAll("<span class=\"toward_street\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"next-street\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"direction\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"exit\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"sign\">", "");
                                                                instruction=instruction.replaceAll("<span lang=\"tr\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"number\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"street\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"distance-description\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"length\">", "");
                                                                instruction=instruction.replaceAll("<span class=\"heading\">", "");
                                                                instruction=instruction.replaceAll("'"," ");

                                                            }
                                                            DataDirection dataDirection=new DataDirection();
                                                            
                                                            id++;
                                                            
                                                            if(geometry){
                                                               JSONObject geomObject= object.getJSONObject("position");
                                                               
                                                               if(geomObject!=null){
                                                                   coors.add(geomObject.getDouble("longitude"));
                                                                   coors.add(geomObject.getDouble("latitude"));
                                                               }
                                                            }
                                                            dataDirection.setLinkId(id);
                                                            dataDirection.setDirection(instruction);
                                                            dataDirection.setDistance(object.getDouble("length"));
                                                            dataDirection.setDuration(object.getDouble("travelTime"));
        
                                                            dataDirections.add(dataDirection);
                                                            }
                                                            if(dirProduce)
                                                            dataRota.setDirections(dataDirections.toArray(new DataDirection[dataDirections.size()]));
                                                          }                                            
                                                        
                                                    }
                                                }
                                            }
                                            
                                            double[] target = new double[coors.size()];
                                             for (int i = 0; i < target.length; i++) {
                                                target[i] = coors.get(i).doubleValue();  // java 1.4 style
                                                // or:
                                                target[i] = coors.get(i);                // java 1.5+ style (outboxing)
                                             }
                                             
                                            dataRota.setCoors(target);
                                           JSONArray jsonWayPoint=eachJsonObject.getJSONArray("waypoint");
                                           if(jsonWayPoint!=null){
                                               List<DataTspPoint> dataTspPointList=new ArrayList();
                                               for(int k=0;k<jsonWayPoint.length();k++){
                                                   DataTspPoint dataTspPoint=new DataTspPoint();
                                                   JSONObject objec =  jsonWayPoint.getJSONObject(k);
                                             if(objec!=null){
                                                   String tspName=objec.getString("mappedRoadName");
                                                   dataTspPoint.setName(tspName!=null?tspName:"");
                                                   dataTspPointList.add(dataTspPoint);
                                                   }
                                               }
                                               dataRota.setTspPoints(dataTspPointList.toArray(new DataTspPoint[dataTspPointList.size()]));
                                           }
                                            
                                            JSONObject legSummary= eachJsonObject.getJSONObject("summary");
                                            if(legSummary!=null){
                                                dataRota.setPathDistance(legSummary.getDouble("distance"));
                                                dataRota.setPathDuration(legSummary.getDouble("baseTime"));
                                                dataRota.setPathDurationWithTmcFlow(legSummary.getDouble("trafficTime"));
                                                
                                            }
                                        }
                                        
                                    }
                            }
                        }
                     }
                }
                
           } catch (MalformedURLException  e) {
                return null;
           } catch(IOException ex){
                return null;    
           } catch (JSONException e) {
                return null;           
          }
        return dataRota;
        }

    //-----------------------------------------------------------------------------

    public static DataRota getRota(DataPoint[] dps, String criteria, String consList, boolean ordered, String tspName, boolean dirProduce, boolean mapProduce, int width, int height, boolean geometry, boolean encode, int ilList) {
        DataNetwork dn = DataNetwork.getNext(criteria);
        DataRota dr = null;
        if (dn == null) {
            Utils.logInfo("Property file content criteria error !");
            return null;
        }

        String networkName = dn.network;
        String host = dn.host;
        int port = dn.port;
        if (host == null || port == 0) {
            Utils.logInfo("Property file content (host, port) error !");
            return null;
        }

        boolean ptsProduce = false;
        String lang = "TR";

        SrvConnect sc = new SrvConnect(host, port);
        if (!sc.getLineWithErrorCheck()) {
            Utils.logInfo("Could not connect to network server (Host: " + host + ", Port: " + port + ") !");
            return null;
        }

        try {
            sc.sendLine("INIT LOCATIONBOX");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Bad response from server !");
                return null;
            }

            sc.sendLine("NETW " + networkName);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Network problem !");
                return null;
            }

            if (consList != null && consList.length() > 0) {
                sc.sendLine("CONS " + consList);
                if (!sc.getLineWithErrorCheck()) {
                    Utils.logInfo("Constaint problem !");
                    return null;
                }
            }

            sc.sendLine("LANG " + lang);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Language is not allowed to set !");
                return null;
            }

            sc.sendLine("TMC YES");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("TMC calculation setting is not allowed !");
                return null;
            }
            
            sc.sendLine("PATH WRITE " + (dirProduce ? "DIR" : "NODIR") + " " + (ptsProduce ? "PTS" : "NOPTS") + " " + "TRD");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Path paramater setting problem !");
                return null;
            }

            if (!mapProduce)
                sc.sendLine("MAP NO");
            else
                sc.sendLine("MAP YES " + width + " " + height);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Map command setting error !");
                return null;
            }

            String line = null;
            if (dps.length == 2) {
                double[] coorsFrom = Utils.getTransformedCoors(dps[0]);
                double[] coorsTo = Utils.getTransformedCoors(dps[1]);
                line = "SHORTEST COORS " + coorsFrom[1] + " " + coorsFrom[0] + " " + coorsTo[1] + " " + coorsTo[0];
            } else {
                if (ordered)
                    line = "MSP COORS";
                else
                    line = "TSP " + tspName + " COORS";
                for (int i = 0; i < dps.length; i++) {
                    if (i == 0)
                        line += " NS ";
                    else
                        line += " N" + i + " ";
                    double[] coors = Utils.getTransformedCoors(dps[i]);
                    line += coors[1] + " " + coors[0];
                } // for()
            }
            sc.sendLine(line);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Bad response from server!");
                return null;
            }

            String mapUrl = sc.getMapUrl();
            if (mapUrl != null && mapUrl.length() > 0) {
                //----- Download to local (WebService computer)
                String filePath = Utils.getParameter("mapimg_path");
                String urlPrefix = Utils.getParameter("mapurl_prefix");
                if (filePath != null && filePath.length() > 0 && urlPrefix != null && urlPrefix.length() > 0) {
                    String fileName = Utils.createFileName(filePath);
                    Utils.downloadAndSaveFile(mapUrl, filePath + "/" + fileName);
                    mapUrl = urlPrefix + "/" + fileName;
                }
            }
            
            dr = new DataRota(sc.getPathId(), sc.getPathDistance(), sc.getPathDuration(), sc.getPathDurationWithTmcFlow(), sc.getOpType(), mapUrl, sc.getTspPoints(), sc.getDirections(), sc.getTollRoads(), geometry,
                             encode);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sc.sendLine("QUIT");
            sc.close();
        }

        return dr;
    } // getRota()

    //-----------------------------------------------------------------------------

    public static boolean isOnRoute(double latitude, double longitude, long pathId, int distance) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SDO_GEOM.SDO_DISTANCE(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL), 0.001) FROM NET_PATHS WHERE PATH_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setLong(colno++, pathId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                double dist = rset.getDouble(1);
                if (dist <= distance)
                    return true;

                return false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return false;
    } // isOnRoute()

    //-----------------------------------------------------------------------------

    public static DataMap getDataMap(DataMap dataMap, boolean downloadToLocalFile) {

        double latitude = dataMap.getLatitude();
        double longitude = dataMap.getLongitude();

        DataPoint[] dps = dataMap.getPoints();

        int zoomLevel = dataMap.getZoomLevel();
        int height = dataMap.getHeight();
        int width = dataMap.getWidth();

        int pan = dataMap.getPan();
        int basemap = dataMap.getBaseMap();

        if (latitude == 0.00 && longitude == 0.00)
            return null;

        String mvUrl = Utils.getParameter("mapviewer_url") + "/omserver";
        String dataSource = Utils.getParameter("mapviewer_map_datasource");
        //    int srid = 8307;
        int srid = 3857;

        String baseMapTemp = "";

        if (basemap != 0 && basemap != 100) {
            String pname = "mapviewer_basemap_" + (basemap > 100 ? basemap - 100 : basemap);
            baseMapTemp = Utils.getParameter(pname);
            if (baseMapTemp != null && baseMapTemp.length() > 0) {
                srid = Integer.parseInt(Utils.getParameter(pname + "_srid"));
            }
        }
        Utils.showText("BASEMAP: " + baseMapTemp);
        Utils.showText("SRID: " + srid);


        Extent extent = null;
        double size = 0.00;
        if (srid == 8307) {
            size = getSizeFromLevel_8307(zoomLevel, height);
            extent = new Extent(latitude, longitude, size, width, height);
            extent = extent.moveExtent(pan);
        } else if (srid == 2321) {
            size = getSizeFromLevel_2321(zoomLevel, height);
            double[] coors = Utils.transformCoors(latitude, longitude, srid);
            extent = new Extent(coors[1], coors[0], size, width, height);
            extent = extent.moveExtent(pan);
        } else if (srid == 3857) {
            size = getSizeFromLevel_3857(zoomLevel, height);
            double[] coors = Utils.transformCoors(latitude, longitude, srid);
            extent = new Extent(coors[1], coors[0], size, width, height);
            extent = extent.moveExtent(pan);
        } else
            return null;

        Utils.showText("SIZE: " + size);
        Utils.showText("GIVEN: " + extent);

        try {
            MapViewer mv = new MapViewer(mvUrl);
            mv.setDataSourceName(dataSource);
            mv.setMapTitle(null);
            mv.setAntiAliasing(true);
            mv.setMapRequestSRID(srid);
            if (baseMapTemp != null && baseMapTemp.length() > 0) {
                mv.addMapTileTheme(baseMapTemp, Utils.getDataSourceFromBaseMapName(dataSource, baseMapTemp), baseMapTemp, (basemap > 100));
                mv.setSnapToCachedZoomLevel((basemap > 100));
            }
            mv.setImageFormat(MapViewer.FORMAT_PNG8_URL);

            if (dataMap != null && dataMap.getPoints().length > 0) {

                for (int i = 0; i < dps.length; i++) {
                    DataPoint dp = dps[i];
                    if (dp == null)
                        continue;

                    mv.addPointFeature(dp.getLongitude(), dp.getLatitude(), 8307, Utils.getStyleName(dp.getType()), null, null, null, false);
                } // for()
            } // if()

            if ((dataMap.getUserCoors() != null && dataMap.getUserCoors().length > 1) || dataMap.getGeo() != null) {
                if (dataMap.getUserData().equalsIgnoreCase("LINE")) {
                    mv.addLinearFeature(dataMap.getUserCoors(), 8307, (dataMap.getUserDataStyle() > 0 ? Utils.getStyleName(dataMap.getUserDataStyle()) : "L.UM_ROTA_1"), null, null, false);
                }
                if (dataMap.getUserData().equalsIgnoreCase("REGION")) {
                    mv.addPolygonFeature(Utils.getPolygonBoundary(dataMap.getGeo()), 8307, (dataMap.getUserDataStyle() > 0 ? Utils.getStyleName(dataMap.getUserDataStyle()) : "C.ROUTE_MAVI"), null, null, false);
                }
            }
            mv.setCenterAndSize(extent.getCenterX(), extent.getCenterY(), size);
            mv.setDeviceSize(new Dimension(width, height));
            mv.run();

            double[] mbr = mv.getMapMBR();
            if (srid == 8307) {
                extent = new Extent(mbr[1], mbr[0], mbr[3], mbr[2]);
            } else {
                double[] minCoors = Utils.getTransformedCoors(mbr[1], mbr[0], srid);
                double[] maxCoors = Utils.getTransformedCoors(mbr[3], mbr[2], srid);
                extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
            }
            Utils.showText("RESULT: " + extent);

            String url = mv.getGeneratedMapImageURL();
            if (url != null && downloadToLocalFile) {
                String filePath = Utils.getParameter("mapimg_path");
                String fileName = Utils.createFileName(filePath);
                Utils.downloadAndSaveFile(url, filePath + "/" + fileName);
                url = Utils.getParameter("mapurl_prefix") + "/" + fileName;
            }

            return (new DataMap(extent.getCenterY(), extent.getCenterX(), zoomLevel, extent, width, height, url, dps));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    } // getDataMap()

    //-----------------------------------------------------------------------------

    public static DataMap getDataMap(DataMap dataMap, boolean downloadToLocalFile, boolean toLocal) {
        double minLatitude = dataMap.getMinLatitude();
        double minLongitude = dataMap.getMinLongitude();
        double maxLatitude = dataMap.getMaxLatitude();
        double maxLongitude = dataMap.getMaxLongitude();

        DataPoint[] dps = dataMap.getPoints();

        int height = dataMap.getHeight();
        int width = dataMap.getWidth();
        int basemap = dataMap.getBaseMap();

        if (minLatitude == 0.00 && minLongitude == 0.00 && maxLatitude == 0.00 && maxLongitude == 0.00)
            return null;

        String mvUrl = Utils.getParameter("mapviewer_url") + "/omserver";
        String dataSource = Utils.getParameter("mapviewer_map_datasource");
        //    int srid = 8307;
        int srid = 3857;

        String baseMapTemp = "";


        if (basemap != 0 && basemap != 100) {
            String pname = "mapviewer_basemap_" + (basemap > 100 ? basemap - 100 : basemap);
            baseMapTemp = Utils.getParameter(pname);
            if (baseMapTemp != null && baseMapTemp.length() > 0) {
                srid = Integer.parseInt(Utils.getParameter(pname + "_srid"));
            }
        }

        Extent extent = null;
        if (srid == 8307) {
            extent = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
        } else {
            double[] minCoors = Utils.transformCoors(minLatitude, minLongitude, srid);
            double[] maxCoors = Utils.transformCoors(maxLatitude, maxLongitude, srid);
            extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
        }

        try {
            MapViewer mv = new MapViewer(mvUrl);
            mv.setDataSourceName(dataSource);
            mv.setMapTitle(null);
            mv.setAntiAliasing(true);
            mv.setMapRequestSRID(srid);
            if (baseMapTemp != null && baseMapTemp.length() > 0) {
                mv.addMapTileTheme(baseMapTemp, Utils.getDataSourceFromBaseMapName(dataSource, baseMapTemp), baseMapTemp, (basemap > 100));
                mv.setSnapToCachedZoomLevel((basemap > 100));
            }
            mv.setImageFormat(MapViewer.FORMAT_PNG8_URL);

            if (dataMap.getPoints() != null && dataMap.getPoints().length > 0) {

                for (int i = 0; i < dps.length; i++) {
                    DataPoint dp = dps[i];
                    if (dp == null)
                        continue;

                    mv.addPointFeature(dp.getLongitude(), dp.getLatitude(), 8307, Utils.getStyleName(dp.getType()), null, null, null, false);
                } // for()
            } // if()

            if ((dataMap.getUserCoors() != null && dataMap.getUserCoors().length > 1) || dataMap.getGeo() != null) {
                if (dataMap.getUserData().equalsIgnoreCase("LINE")) {
                    mv.addLinearFeature(dataMap.getUserCoors(), 8307, (dataMap.getUserDataStyle() > 0 ? Utils.getStyleName(dataMap.getUserDataStyle()) : "L.UM_ROTA_1"), null, null, false);
                }
                if (dataMap.getUserData().equalsIgnoreCase("REGION")) {
                    mv.addPolygonFeature(Utils.getPolygonBoundary(dataMap.getGeo()), 8307, (dataMap.getUserDataStyle() > 0 ? Utils.getStyleName(dataMap.getUserDataStyle()) : "C.ROUTE_MAVI"), null, null, false);
                }
            }
            Utils.showText("SET BOX: " + extent);

            double size = extent.scaleExtent(1.4).getSize(width, height);
            mv.setCenterAndSize(extent.getCenterX(), extent.getCenterY(), size);
            //mv.setBox(extent.getMinLongitude(), extent.getMinLatitude(), extent.getMaxLongitude(), extent.getMaxLatitude());
            mv.setDeviceSize(new Dimension(width, height));
            mv.run();

            double[] mbr = mv.getMapMBR();
            if (srid == 8307) {
                extent = new Extent(mbr[1], mbr[0], mbr[3], mbr[2]);
            } else {
                double[] minCoors = Utils.getTransformedCoors(mbr[1], mbr[0], srid);
                double[] maxCoors = Utils.getTransformedCoors(mbr[3], mbr[2], srid);
                extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
            }
            Utils.showText("RESULT: " + extent);

            String url = mv.getGeneratedMapImageURL();
            if (url != null && downloadToLocalFile) {
                String filePath = Utils.getParameter("mapimg_path");
                String fileName = Utils.createFileName(filePath);
                Utils.downloadAndSaveFile(url, filePath + "/" + fileName);
                url = Utils.getParameter("mapurl_prefix") + "/" + fileName;
            }

            return (new DataMap(extent.getCenterY(), extent.getCenterX(), -1, extent, width, height, url, dps));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    } // getDataMap()

    //-----------------------------------------------------------------------------

    public static DataMap getDataMap_Traffic(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, int width, int height, String pathIds, boolean flow, boolean tmc, DataTrafficEvent[] dtes,
                                             int basemap, boolean downloadToLocalFile) {
        if (minLatitude == 0.00 && minLongitude == 0.00 && maxLatitude == 0.00 && maxLongitude == 0.00)
            return null;

        String mvUrl = Utils.getParameter("mapviewer_url") + "/omserver";
        String dataSource = Utils.getParameter("mapviewer_map_datasource");
        String appDataSource = Utils.getParameter("mapviewer_app_datasource");
        int srid = 3857;
        String baseMap = null;
        if (basemap != 0 && basemap != 100) {
            String pname = "mapviewer_basemap_" + (basemap > 100 ? basemap - 100 : basemap);
            baseMap = Utils.getParameter(pname);
            if (baseMap != null && baseMap.length() > 0) {
                srid = Integer.parseInt(Utils.getParameter(pname + "_srid"));
            }
        }

        Extent extent = null;
        double size = 0.00;
        if (srid == 8307) {
            extent = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
            size = extent.getSize();
        } else {
            double[] minCoors = Utils.transformCoors(minLatitude, minLongitude, srid);
            double[] maxCoors = Utils.transformCoors(maxLatitude, maxLongitude, srid);
            extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
            size = extent.getSize();
        }

        int zoomLevel = 0;
        if (srid == 8307)
            zoomLevel = getLevelFromSize_8307(size);
        else if (srid == 2321)
            zoomLevel = getLevelFromSize_2321(size);
        else if (srid == 3857)
            zoomLevel = getLevelFromSize_3857(size);
        else
            return null;

        Utils.showText("ZOOM LEVEL: " + zoomLevel);

        try {
            MapViewer mv = new MapViewer(mvUrl);
            mv.setDataSourceName(appDataSource);
            mv.setMapTitle(null);
            mv.setAntiAliasing(true);
            mv.setMapRequestSRID(srid);
            mv.setBackgroundTransparent(true);
            if (baseMap != null && baseMap.length() > 0) {
                mv.addMapTileTheme(baseMap, Utils.getDataSourceFromBaseMapName(dataSource, baseMap), baseMap, (basemap > 100));
                mv.setSnapToCachedZoomLevel((basemap > 100));
            }
            mv.setImageFormat(MapViewer.FORMAT_PNG8_URL);

            if (pathIds != null && pathIds.length() > 0) {
                String themeName = "MSNTRAFFIC_ROUTE";
                mv.addPredefinedTheme(appDataSource, themeName);
                String[] params = new String[1]; // Array containing all binding variable values
                params[0] = pathIds;
                mv.setThemeBindingParameters(themeName, params);
            }

            if (flow) {
                if (tmc) {
                    String themeName = "TMC_HAT_" + srid + "_" + zoomLevel;
                    mv.addPredefinedTheme(appDataSource, themeName);
                } else {
                    //* WMS theme
                    String serviceUrl = Utils.getParameter("mapviewer_wms_serviceurl");
                    String version = "1.1.1";
                    String[] layers = { "turkey_links" };
                    String[] styles = { "default" };
                    String srs = "EPSG:4326";
                    mv.addWMSMapTheme("trafik_flow", serviceUrl, "FALSE", version, layers, styles, srs, "image/png", "TRUE", null, null, null);
                    /* WMTS theme
          String serviceUrl = Utils.getParameter("mapviewer_wmts_serviceurl");
          mv.addWMTSTheme("deneme", "trafik_flow", serviceUrl, "1.0.0", "default", "BETRAFFIC", "image/png", "TMS:BETRAFFIC", -20037508, 20037508);
//*/
                }
            }

            if (dtes != null) {
                for (int i = 0; i < dtes.length; i++) {
                    DataTrafficEvent dte = dtes[i];
                    if (dte == null)
                        continue;

                    mv.addPointFeature(dte.getLongitude(), dte.getLatitude(), 8307, "M.TRAFFIC_1" + dte.getTyp(), null, null, null, false);
                } // for()
            } // if()

            Utils.showText("SET BOX: " + extent);
            //size = extent.scaleExtent(1.4).getSize(width, height);
            //mv.setCenterAndSize(extent.getCenterX(), extent.getCenterY(), size);
            mv.setBox(extent.getMinLongitude(), extent.getMinLatitude(), extent.getMaxLongitude(), extent.getMaxLatitude());
            mv.setDeviceSize(new Dimension(width, height));
            mv.run();

            double[] mbr = mv.getMapMBR();
            if (srid == 8307) {
                extent = new Extent(mbr[1], mbr[0], mbr[3], mbr[2]);
            } else {
                double[] minCoors = Utils.getTransformedCoors(mbr[1], mbr[0], srid);
                double[] maxCoors = Utils.getTransformedCoors(mbr[3], mbr[2], srid);
                extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
            }
            Utils.showText("RESULT: " + extent);

            String url = mv.getGeneratedMapImageURL();
            if (url != null && downloadToLocalFile) {
                String filePath = Utils.getParameter("mapimg_path");
                String fileName = Utils.createFileName(filePath);
                Utils.downloadAndSaveFile(url, filePath + "/" + fileName);
                url = Utils.getParameter("mapurl_prefix") + "/" + fileName;
            }

            return (new DataMap(extent.getCenterY(), extent.getCenterX(), -1, extent, width, height, url, null));
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return null;
    } // getDataMap_Traffic()

    //-----------------------------------------------------------------------------

    public static int addUserPoint(String key, DataUserPoint dup) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn =null;

        DataAdminArea daa = DataAdminArea.getInstance(dup.latitude, dup.longitude);

        try {
            cnn = DbConn.getPooledConnection();
            String geometry = null;
            if (dup.angle == 0)
                geometry = "SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL)";
            else
                geometry = "SDO_GEOMETRY(2001,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1,1,3,1,0),SDO_ORDINATE_ARRAY(?,?,COS(?/180*3.141592653589793),SIN(?/180*3.141592653589793)))";

            sql = "UPDATE LBS_USER_POINT SET POINT_NAME=?,TYP=?,ADDRESS=?,TELNO=?,FAXNO=?,IL_ID=?,IL_ADI=?,ILCE_ID=?,ILCE_ADI=?,MAHALLE_ID=?,MAHALLE_ADI=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=?,YCOOR=?,ANGLE=?,GEOLOC=(" +
                geometry + ") WHERE KEY=? AND POINT_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, dup.name);
            pstmt.setInt(colno++, dup.type);
            if (dup.address == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.address);
            if (dup.telNo == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.telNo);
            if (dup.faxNo == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.faxNo);
            pstmt.setLong(colno++, daa.ilId);
            pstmt.setString(colno++, daa.ilAdi);
            pstmt.setLong(colno++, daa.ilceId);
            pstmt.setString(colno++, daa.ilceAdi);
            pstmt.setLong(colno++, daa.mahalleId);
            pstmt.setString(colno++, daa.mahalleAdi);
            if (dup.string_1 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_1);
            if (dup.string_2 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_2);
            if (dup.string_3 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_3);
            if (dup.string_4 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_4);
            if (dup.string_5 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_5);
            if (dup.string_6 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_6);
            if (dup.string_7 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_7);
            if (dup.string_8 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_8);
            if (dup.string_9 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dup.string_9);
            pstmt.setDouble(colno++, dup.number_1);
            pstmt.setDouble(colno++, dup.number_2);
            pstmt.setDouble(colno++, dup.number_3);
            pstmt.setDouble(colno++, dup.number_4);
            pstmt.setDouble(colno++, dup.number_5);
            pstmt.setDouble(colno++, dup.number_6);
            pstmt.setDouble(colno++, dup.number_7);
            pstmt.setDouble(colno++, dup.number_8);
            pstmt.setDouble(colno++, dup.number_9);
            pstmt.setDouble(colno++, dup.longitude);
            pstmt.setDouble(colno++, dup.latitude);
            pstmt.setDouble(colno++, dup.angle);
            pstmt.setDouble(colno++, dup.longitude);
            pstmt.setDouble(colno++, dup.latitude);
            if (dup.angle != 0) {
                pstmt.setDouble(colno++, dup.angle);
                pstmt.setDouble(colno++, dup.angle);
            }
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dup.id);
            int count = pstmt.executeUpdate();
            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);

                if (dup.id == null || dup.id.length() <= 0)
                    dup.id = Utils.getUniquePointId();

                sql = "INSERT INTO LBS_USER_POINT (KEY,POINT_ID,POINT_NAME,TYP,ADDRESS,TELNO,FAXNO,IL_ID,IL_ADI,ILCE_ID,ILCE_ADI,MAHALLE_ID,MAHALLE_ADI,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,ANGLE,GEOLOC)";
                sql += " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,?,?,?,(" + geometry + "))";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dup.id);
                pstmt.setString(colno++, dup.name);
                pstmt.setInt(colno++, dup.type);
                if (dup.address == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.address);
                if (dup.telNo == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.telNo);
                if (dup.faxNo == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.faxNo);
                pstmt.setLong(colno++, daa.ilId);
                pstmt.setString(colno++, daa.ilAdi);
                pstmt.setLong(colno++, daa.ilceId);
                pstmt.setString(colno++, daa.ilceAdi);
                pstmt.setLong(colno++, daa.mahalleId);
                pstmt.setString(colno++, daa.mahalleAdi);
                if (dup.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_1);
                if (dup.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_2);
                if (dup.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_3);
                if (dup.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_4);
                if (dup.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_5);
                if (dup.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_6);
                if (dup.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_7);
                if (dup.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_8);
                if (dup.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dup.string_9);
                pstmt.setDouble(colno++, dup.number_1);
                pstmt.setDouble(colno++, dup.number_2);
                pstmt.setDouble(colno++, dup.number_3);
                pstmt.setDouble(colno++, dup.number_4);
                pstmt.setDouble(colno++, dup.number_5);
                pstmt.setDouble(colno++, dup.number_6);
                pstmt.setDouble(colno++, dup.number_7);
                pstmt.setDouble(colno++, dup.number_8);
                pstmt.setDouble(colno++, dup.number_9);
                pstmt.setDouble(colno++, dup.longitude);
                pstmt.setDouble(colno++, dup.latitude);
                pstmt.setDouble(colno++, dup.angle);
                pstmt.setDouble(colno++, dup.longitude);
                pstmt.setDouble(colno++, dup.latitude);
                if (dup.angle != 0) {
                    pstmt.setDouble(colno++, dup.angle);
                    pstmt.setDouble(colno++, dup.angle);
                }
                pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addUserPoint()

    //-----------------------------------------------------------------------------

    public static int addUserPoints(String key, DataUserPoint dupl[]) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        
        cnn = DbConn.getPooledConnection();
        
        for (int i = 0; i < dupl.length; i++) {
            DataAdminArea daa = DataAdminArea.getInstance(dupl[i].latitude, dupl[i].longitude);

            try {
                String geometry = null;
                if (dupl[i].angle == 0)
                    geometry = "SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL)";
                else
                    geometry = "SDO_GEOMETRY(2001,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1,1,3,1,0),SDO_ORDINATE_ARRAY(?,?,COS(?/180*3.141592653589793),SIN(?/180*3.141592653589793)))";

                sql = "UPDATE LBS_USER_POINT SET POINT_NAME=?,TYP=?,ADDRESS=?,TELNO=?,FAXNO=?,IL_ID=?,IL_ADI=?,ILCE_ID=?,ILCE_ADI=?,MAHALLE_ID=?,MAHALLE_ADI=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=?,YCOOR=?,ANGLE=?,GEOLOC=(" +
                    geometry + ") WHERE KEY=? AND POINT_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setString(colno++, dupl[i].name);
                pstmt.setInt(colno++, dupl[i].type);
                if (dupl[i].address == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].address);
                if (dupl[i].telNo == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].telNo);
                if (dupl[i].faxNo == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].faxNo);
                pstmt.setLong(colno++, daa.ilId);
                pstmt.setString(colno++, daa.ilAdi);
                pstmt.setLong(colno++, daa.ilceId);
                pstmt.setString(colno++, daa.ilceAdi);
                pstmt.setLong(colno++, daa.mahalleId);
                pstmt.setString(colno++, daa.mahalleAdi);
                if (dupl[i].string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_1);
                if (dupl[i].string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_2);
                if (dupl[i].string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_3);
                if (dupl[i].string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_4);
                if (dupl[i].string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_5);
                if (dupl[i].string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_6);
                if (dupl[i].string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_7);
                if (dupl[i].string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_8);
                if (dupl[i].string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dupl[i].string_9);
                pstmt.setDouble(colno++, dupl[i].number_1);
                pstmt.setDouble(colno++, dupl[i].number_2);
                pstmt.setDouble(colno++, dupl[i].number_3);
                pstmt.setDouble(colno++, dupl[i].number_4);
                pstmt.setDouble(colno++, dupl[i].number_5);
                pstmt.setDouble(colno++, dupl[i].number_6);
                pstmt.setDouble(colno++, dupl[i].number_7);
                pstmt.setDouble(colno++, dupl[i].number_8);
                pstmt.setDouble(colno++, dupl[i].number_9);
                pstmt.setDouble(colno++, dupl[i].longitude);
                pstmt.setDouble(colno++, dupl[i].latitude);
                pstmt.setDouble(colno++, dupl[i].angle);
                pstmt.setDouble(colno++, dupl[i].longitude);
                pstmt.setDouble(colno++, dupl[i].latitude);
                if (dupl[i].angle != 0) {
                    pstmt.setDouble(colno++, dupl[i].angle);
                    pstmt.setDouble(colno++, dupl[i].angle);
                }
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dupl[i].id);
                int count = pstmt.executeUpdate();
                if (count <= 0) {
                    DbConn.closeDBConnection(pstmt, null);

                    if (dupl[i].id == null || dupl[i].id.length() <= 0)
                        dupl[i].id = Utils.getUniquePointId();

                    sql = "INSERT INTO LBS_USER_POINT (KEY,POINT_ID,POINT_NAME,TYP,ADDRESS,TELNO,FAXNO,IL_ID,IL_ADI,ILCE_ID,ILCE_ADI,MAHALLE_ID,MAHALLE_ADI,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,ANGLE,GEOLOC)";
                    sql += " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,?,?,?,(" + geometry + "))";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dupl[i].id);
                    pstmt.setString(colno++, dupl[i].name);
                    pstmt.setInt(colno++, dupl[i].type);
                    if (dupl[i].address == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].address);
                    if (dupl[i].telNo == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].telNo);
                    if (dupl[i].faxNo == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].faxNo);
                    pstmt.setLong(colno++, daa.ilId);
                    pstmt.setString(colno++, daa.ilAdi);
                    pstmt.setLong(colno++, daa.ilceId);
                    pstmt.setString(colno++, daa.ilceAdi);
                    pstmt.setLong(colno++, daa.mahalleId);
                    pstmt.setString(colno++, daa.mahalleAdi);
                    if (dupl[i].string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_1);
                    if (dupl[i].string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_2);
                    if (dupl[i].string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_3);
                    if (dupl[i].string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_4);
                    if (dupl[i].string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_5);
                    if (dupl[i].string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_6);
                    if (dupl[i].string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_7);
                    if (dupl[i].string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_8);
                    if (dupl[i].string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dupl[i].string_9);
                    pstmt.setDouble(colno++, dupl[i].number_1);
                    pstmt.setDouble(colno++, dupl[i].number_2);
                    pstmt.setDouble(colno++, dupl[i].number_3);
                    pstmt.setDouble(colno++, dupl[i].number_4);
                    pstmt.setDouble(colno++, dupl[i].number_5);
                    pstmt.setDouble(colno++, dupl[i].number_6);
                    pstmt.setDouble(colno++, dupl[i].number_7);
                    pstmt.setDouble(colno++, dupl[i].number_8);
                    pstmt.setDouble(colno++, dupl[i].number_9);
                    pstmt.setDouble(colno++, dupl[i].longitude);
                    pstmt.setDouble(colno++, dupl[i].latitude);
                    pstmt.setDouble(colno++, dupl[i].angle);
                    pstmt.setDouble(colno++, dupl[i].longitude);
                    pstmt.setDouble(colno++, dupl[i].latitude);
                    if (dupl[i].angle != 0) {
                        pstmt.setDouble(colno++, dupl[i].angle);
                        pstmt.setDouble(colno++, dupl[i].angle);
                    }
                    pstmt.executeUpdate();
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                DbConn.closeConnection(cnn);
                return -2;
            } finally {
                DbConn.closeDBConnection(pstmt, null);
            }
        }
        
        DbConn.closeConnection(cnn);
        
        return 0;
    } // addUserPoints()

    //-----------------------------------------------------------------------------

    public static DataUserPoint[] getUserPoint(String key, String id) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataUserPoint dup = DataUserPoint.getInstance(rset);
                array.add(dup);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserPoint[] dups = new DataUserPoint[array.size()];
        for (int i = 0; i < dups.length; i++)
            dups[i] = (DataUserPoint) array.get(i);
        return dups;
    } // getUserPoint()

    //-----------------------------------------------------------------------------

    public static int removeUserPoint(String key, String id) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "DELETE FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            int count = pstmt.executeUpdate();
            if (count > 0)
                return 0;

            return -1;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return -2;
    } // removeUserPoint()

    //-----------------------------------------------------------------------------

    public static DataList[] getUserPointList(String key, int count, double latitude, double longitude, int radius, String whereClause, String subKeys, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        String[] keyValues = null;
        String[] pointIdList = null;
        
        if (idList != null) {
            pointIdList = idList.split(",");
        }
        ArrayList array = new ArrayList();

        double tolerance = ((double) (radius + 1000) / 100000.0) * ((double) (radius + 1000) / 100000.0);
        
        if (subKeys != null) {
            subKeys = subKeys + "," + key;
            subKeys.split(",");
            keyValues = subKeys.split(",");

            try {
                cnn = DbConn.getPooledConnection();
                for (int i = 0; i < keyValues.length; i++) {
                    if (latitude == 0.00 && longitude == 0.00)
                        sql = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO FROM LBS_USER_POINT WHERE KEY = ? " + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY NLSSORT(POINT_NAME, 'NLS_SORT=XTURKISH'),POINT_ID ";
                    else {
                        String sqlQuery = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO, ((?-XCOOR)*(?-XCOOR)+(?-YCOOR)*(?-YCOOR)) REF_DISTANCE FROM LBS_USER_POINT WHERE KEY IN (?) " +
                            (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY REF_DISTANCE";
                        sql = "SELECT * FROM (SELECT IQ.*,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR,IQ.YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
                        sql += " FROM (" + sqlQuery + ") IQ " + (radius != -1 ? " WHERE REF_DISTANCE <= ?" : "") + ") " + (radius != -1 ? " WHERE DISTANCE <= ?" : "") + " ORDER BY DISTANCE";
                        //sql += " FROM (" + sqlQuery + ") IQ WHERE REF_DISTANCE <= ?) WHERE DISTANCE <= ? ORDER BY DISTANCE";
                    }
                    sql = "SELECT * FROM (" + sql + ") WHERE ";
                    if (pointIdList != null) {
                        sql += "POINT_ID IN (";
                        for (int j = 0; j < pointIdList.length; j++) {
                            if (j > 0)
                                sql += ",";
                            sql += "'" + pointIdList[j] + "'";
                        }
                        sql += ") AND ";
                    }
                    sql += "ROWNUM <= " + count;
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    int colno = 1;
                    if (latitude == 0.00 && longitude == 0.00) {
                        pstmt.setString(colno++, keyValues[i]);
                    } else {
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setString(colno++, keyValues[i]);
                        if (radius != -1) {
                            pstmt.setDouble(colno++, tolerance);
                            pstmt.setInt(colno++, radius);
                        }
                    }
                    rset = pstmt.executeQuery();
                    while (rset.next()) {
                        String id = rset.getString(1);
                        String name = rset.getString(2);
                        int type = rset.getInt(3);
                        double xcoor = rset.getDouble(4);
                        double ycoor = rset.getDouble(5);
                        double angle = rset.getDouble(6);
                        String address = rset.getString(7);
                        String telephone = rset.getString(8);
                        int distance = -1;
                        if (latitude != 0.00 || longitude != 0.00)
                            distance = (int) (rset.getDouble("DISTANCE") + 0.5);

                        DataList dl = new DataList(id, name, type, ycoor, xcoor, angle, distance, address, telephone);
                        dl.setAddress(address);

                        array.add(dl);
                    } // while()
                    
                    DbConn.closeDBConnection(pstmt, rset);
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        } else {

            try {
                cnn = DbConn.getPooledConnection();
                if (latitude == 0.00 && longitude == 0.00)
                    sql = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO FROM LBS_USER_POINT WHERE KEY = ? " + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY  NLSSORT(POINT_NAME, 'NLS_SORT=XTURKISH'),POINT_ID ";
                else {
                    String sqlQuery = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR, ANGLE,ADDRESS,TELNO, ((?-XCOOR)*(?-XCOOR)+(?-YCOOR)*(?-YCOOR)) REF_DISTANCE FROM LBS_USER_POINT WHERE KEY IN (?) " +
                        (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY REF_DISTANCE";
                    sql = "SELECT * FROM (SELECT IQ.*,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR,IQ.YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
                    sql += " FROM (" + sqlQuery + ") IQ " + (radius != -1 ? " WHERE REF_DISTANCE <= ?" : "") + ") " + (radius != -1 ? " WHERE DISTANCE <= ?" : "") + " ORDER BY DISTANCE";
                    //sql += " FROM (" + sqlQuery + ") IQ WHERE REF_DISTANCE <= ?) WHERE DISTANCE <= ? ORDER BY DISTANCE";
                }
                sql = "SELECT * FROM (" + sql + ") WHERE ";
                if (pointIdList != null) {
                    sql += "POINT_ID IN (";
                    for (int j = 0; j < pointIdList.length; j++) {
                        if (j > 0)
                            sql += ",";
                        sql += "'" + pointIdList[j] + "'";
                    }
                    sql += ") AND ";
                }
                sql += "ROWNUM <= " + count;
                Utils.showText("SQL: " + sql);
                Utils.showText("LON/LAT: " + longitude + "," + latitude);
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                if (latitude == 0.00 && longitude == 0.00) {
                    pstmt.setString(colno++, key);
                } else {
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setString(colno++, key);
                    if (radius != -1) {
                        pstmt.setDouble(colno++, tolerance);
                        pstmt.setInt(colno++, radius);
                    }
                }
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    String id = rset.getString(1);
                    String name = rset.getString(2);
                    int type = rset.getInt(3);
                    double xcoor = rset.getDouble(4);
                    double ycoor = rset.getDouble(5);
                    double angle = rset.getDouble(6);
                    String address = rset.getString(7);
                    String telephone = rset.getString(8);
                    int distance = -1;
                    if (latitude != 0.00 || longitude != 0.00)
                        distance = (int) (rset.getDouble("DISTANCE") + 0.5);
                    DataList dl = new DataList(id, name, type, ycoor, xcoor, angle, distance, address, telephone);
                    dl.setAddress(address);
                    array.add(dl);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataList[] dints = new DataList[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataList) array.get(i);
        return dints;
    } // getUserPointList()

    //-----------------------------------------------------------------------------

    public static DataUserPoint[] getUserPointListDetailed(String key, int count, double latitude, double longitude, int radius, String whereClause, String subKeys, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        String[] keyValues = null;
        String[] pointIdList = null;
        
        if (idList != null) {
            pointIdList = idList.split(",");
        }
        ArrayList array = new ArrayList();

        double tolerance = ((double) (radius + 1000) / 100000.0) * ((double) (radius + 1000) / 100000.0);

        if (subKeys != null) {
            subKeys = subKeys + "," + key;
            subKeys.split(",");
            keyValues = subKeys.split(",");

            try {
                cnn = DbConn.getPooledConnection();
                for (int i = 0; i < keyValues.length; i++) {
                    if (latitude == 0.00 && longitude == 0.00)
                        sql = "SELECT * FROM LBS_USER_POINT WHERE KEY = ? " + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY POINT_NAME,POINT_ID";
                    else {
                        String sqlQuery = "SELECT P.*, ((?-XCOOR)*(?-XCOOR)+(?-YCOOR)*(?-YCOOR)) REF_DISTANCE FROM LBS_USER_POINT P WHERE KEY IN (?) " + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                            " ORDER BY REF_DISTANCE";
                        sql = "SELECT * FROM (SELECT IQ.*,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR,IQ.YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
                        sql += " FROM (" + sqlQuery + ") IQ " + (radius != -1 ? " WHERE REF_DISTANCE <= ?" : "") + ") " + (radius != -1 ? " WHERE DISTANCE <= ?" : "") + " ORDER BY DISTANCE";
                        //sql += " FROM (" + sqlQuery + ") IQ WHERE REF_DISTANCE <= ?) WHERE DISTANCE <= ? ORDER BY DISTANCE";
                    }
                    sql = "SELECT * FROM (" + sql + ") WHERE ";
                    if (pointIdList != null) {
                        sql += "POINT_ID IN (";
                        for (int j = 0; j < pointIdList.length; j++) {
                            if (j > 0)
                                sql += ",";
                            sql += "'" + pointIdList[j] + "'";
                        }
                        sql += ") AND ";
                    }
                    sql += "ROWNUM <= " + count;
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    int colno = 1;
                    if (latitude == 0.00 && longitude == 0.00) {
                        pstmt.setString(colno++, keyValues[i]);
                    } else {
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, longitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setDouble(colno++, latitude);
                        pstmt.setString(colno++, keyValues[i]);
                        if (radius != -1) {
                            pstmt.setDouble(colno++, tolerance);
                            pstmt.setInt(colno++, radius);
                        }
                    }
                    rset = pstmt.executeQuery();
                    while (rset.next()) {
                        int distance = -1;
                        if (latitude != 0.00 || longitude != 0.00)
                            distance = (int) (rset.getDouble("DISTANCE") + 0.5);
                        DataUserPoint dur = DataUserPoint.getInstance(rset);
                        dur.setDistance(distance);
                        array.add(dur);
                    } // while()
                    
                    DbConn.closeDBConnection(pstmt, rset);
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        } else {
            try {
                cnn = DbConn.getPooledConnection();
                if (latitude == 0.00 && longitude == 0.00)
                    sql = "SELECT * FROM LBS_USER_POINT WHERE KEY = ? " + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY POINT_NAME,POINT_ID";
                else {
                    String sqlQuery = "SELECT P.*, ((?-XCOOR)*(?-XCOOR)+(?-YCOOR)*(?-YCOOR)) REF_DISTANCE FROM LBS_USER_POINT P WHERE KEY IN (?) " + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY REF_DISTANCE";
                    sql = "SELECT * FROM (SELECT IQ.*,SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(IQ.XCOOR,IQ.YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
                    sql += " FROM (" + sqlQuery + ") IQ " + (radius != -1 ? " WHERE REF_DISTANCE <= ?" : "") + ") " + (radius != -1 ? " WHERE DISTANCE <= ?" : "") + " ORDER BY DISTANCE";
                    //sql += " FROM (" + sqlQuery + ") IQ WHERE REF_DISTANCE <= ?) WHERE DISTANCE <= ? ORDER BY DISTANCE";
                }
                sql = "SELECT * FROM (" + sql + ") WHERE ";
                if (pointIdList != null) {
                    sql += "POINT_ID IN (";
                    for (int j = 0; j < pointIdList.length; j++) {
                        if (j > 0)
                            sql += ",";
                        sql += "'" + pointIdList[j] + "'";
                    }
                    sql += ") AND ";
                }
                sql += "ROWNUM <= " + count;
                Utils.showText("SQL: " + sql);
                Utils.showText("LON/LAT: " + longitude + "," + latitude);
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                if (latitude == 0.00 && longitude == 0.00) {
                    pstmt.setString(colno++, key);
                } else {
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, longitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setDouble(colno++, latitude);
                    pstmt.setString(colno++, key);
                    if (radius != -1) {
                        pstmt.setDouble(colno++, tolerance);
                        pstmt.setInt(colno++, radius);
                    }
                }
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    int distance = -1;
                    if (latitude != 0.00 || longitude != 0.00)
                        distance = (int) (rset.getDouble("DISTANCE") + 0.5);
                    DataUserPoint dur = DataUserPoint.getInstance(rset);
                    dur.setDistance(distance);
                    array.add(dur);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataUserPoint[] dints = new DataUserPoint[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataUserPoint) array.get(i);
        return dints;
    } // getUserPointListDetailed()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointListCount(String key, int type, long reqId, String[] pointList, String whereClause) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (SELECT P.IL_ID, P.IL_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, IL Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                if (reqId != 99)
                    sql += "AND Z.IL_ID=" + reqId + " ";
                sql += "AND Z.IL_ID=P.IL_ID GROUP BY P.IL_ADI, P.IL_ID, Z.XCOOR, Z.YCOOR ORDER BY CNT DESC) M, IL I WHERE M.IL_ID = I.IL_ID AND ROWNUM <=" + maxCount;

                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (SELECT P.ILCE_ID, P.ILCE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, ILCE Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND Z.IL_ID=P.IL_ID AND Z.ILCE_ID=P.ILCE_ID ";
                if (reqId > 0)
                    sql += "AND Z.IL_ID=" + reqId + " ";
                sql += "GROUP BY P.ILCE_ADI, P.ILCE_ID, Z.XCOOR, Z.YCOOR ORDER BY CNT DESC ) M, ILCE I WHERE M.ILCE_ID = I.ILCE_ID AND ROWNUM <=" + maxCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (SELECT P.MAHALLE_ID, P.MAHALLE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, MAHALLE Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND Z.ILCE_ID=P.ILCE_ID AND Z.MAHALLE_ID=P.MAHALLE_ID ";
                if (reqId > 0)
                    sql += "AND Z.ILCE_ID=" + reqId + " ";
                sql += "GROUP BY P.MAHALLE_ADI, P.MAHALLE_ID, Z.XCOOR, Z.YCOOR ORDER BY CNT DESC ) M, MAHALLE I WHERE M.MAHALLE_ID = I.MAHALLE_ID AND ROWNUM <=" + maxCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            }
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                long count = rset.getLong(5);
                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);

                if (type == 1)
                    array.add(new DataIl(id, name, latitude, longitude, extent, count));
                else if (type == 2)
                    array.add(new DataIlce(id, name, reqId, latitude, longitude, extent, count));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, reqId, latitude, longitude, extent, count));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointListCount()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointListCount(String key, int type, long reqId, String[] pointList, String whereClause, String groupByCol) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (SELECT P.IL_ID, P.IL_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol +
                    " FROM LBS_USER_POINT P, IL Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                if (reqId != 99)
                    sql += "AND Z.IL_ID=" + reqId + " ";
                sql += "AND Z.IL_ID=P.IL_ID GROUP BY P.IL_ADI, P.IL_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol + " ORDER BY CNT DESC) M, IL I WHERE M.IL_ID = I.IL_ID AND ROWNUM <=" + maxCount;

                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (SELECT P.ILCE_ID, P.ILCE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol +
                    " FROM LBS_USER_POINT P, ILCE Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND Z.IL_ID=P.IL_ID AND Z.ILCE_ID=P.ILCE_ID ";
                if (reqId > 0)
                    sql += "AND Z.IL_ID=" + reqId + " ";
                sql += "GROUP BY P.ILCE_ADI, P.ILCE_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol + " ORDER BY CNT DESC ) M, ILCE I WHERE M.ILCE_ID = I.ILCE_ID AND ROWNUM <=" + maxCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (SELECT P.MAHALLE_ID, P.MAHALLE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol +
                    " FROM LBS_USER_POINT P, MAHALLE Z WHERE P.KEY=? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                if (pointList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND Z.ILCE_ID=P.ILCE_ID AND Z.MAHALLE_ID=P.MAHALLE_ID ";
                if (reqId > 0)
                    sql += "AND Z.ILCE_ID=" + reqId + " ";
                sql += "GROUP BY P.MAHALLE_ADI, P.MAHALLE_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol + " ORDER BY CNT DESC ) M, MAHALLE I WHERE M.MAHALLE_ID = I.MAHALLE_ID AND ROWNUM <=" + maxCount;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
            }
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                long count = rset.getLong(5);
                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);
                String groupValue = rset.getString(7);
                if (groupValue == null) {
                    groupValue = "-";
                }
                ;

                if (type == 1)
                    array.add(new DataIl(id, name, latitude, longitude, extent, count, groupValue));
                else if (type == 2)
                    array.add(new DataIlce(id, name, reqId, latitude, longitude, extent, count, groupValue));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, reqId, latitude, longitude, extent, count, groupValue));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointListCountGV()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointCountSearch(String key, int type, double latitude, double longitude, int radius, String whereClause, String[] pointIdList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));        
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR FROM(SELECT IL_ID, IL_ADI, COUNT(1) CNT FROM LBS_USER_POINT WHERE KEY = ? ";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                    "GROUP BY IL_ID, IL_ADI) M, IL I WHERE M.IL_ID = I.IL_ID AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR FROM(SELECT ILCE_ID, ILCE_ADI, COUNT(1) CNT FROM LBS_USER_POINT WHERE KEY = ? ";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                    "GROUP BY ILCE_ID, ILCE_ADI) M, ILCE I WHERE M.ILCE_ID = I.ILCE_ID  AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR FROM(SELECT MAHALLE_ID, MAHALLE_ADI, COUNT(1) CNT FROM LBS_USER_POINT WHERE KEY = ?";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                    "GROUP BY MAHALLE_ID, MAHALLE_ADI) M, MAHALLE I WHERE M.MAHALLE_ID = I.MAHALLE_ID  AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            }

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            String requestStr = "distance=" + radius + " unit=m";
            pstmt.setString(colno++, key);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setString(colno++, requestStr);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                int count = rset.getInt(3);
                double lon = rset.getDouble(4);
                double lat = rset.getDouble(5);

                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);
                if (type == 1)
                    array.add(new DataIl(id, name, lat, lon, extent, count));
                else if (type == 2)
                    array.add(new DataIlce(id, name, 0, lat, lon, extent, count));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, 0, lat, lon, extent, count));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointCountSearch()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointCountSearch(String key, int type, double latitude, double longitude, int radius, String whereClause, String[] pointIdList, String groupByCol) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR, M." + groupByCol + " FROM(SELECT IL_ID, IL_ADI, COUNT(1) CNT, " + groupByCol + " FROM LBS_USER_POINT WHERE KEY = ? ";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") + "GROUP BY IL_ID, IL_ADI, " +
                    groupByCol + ") M, IL I WHERE M.IL_ID = I.IL_ID AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR, M." + groupByCol + " FROM(SELECT ILCE_ID, ILCE_ADI, COUNT(1) CNT, " + groupByCol + " FROM LBS_USER_POINT WHERE KEY = ? ";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") + "GROUP BY ILCE_ID, ILCE_ADI, " +
                    groupByCol + ") M, ILCE I WHERE M.ILCE_ID = I.ILCE_ID  AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.CNT, I.XCOOR, I.YCOOR, I.GEOMBR, M." + groupByCol + " FROM(SELECT MAHALLE_ID, MAHALLE_ADI, COUNT(1) CNT, " + groupByCol + " FROM LBS_USER_POINT WHERE KEY = ?";
                if (pointIdList != null) {
                    sql += "AND POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += "AND SDO_WITHIN_DISTANCE (GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), ?) = 'TRUE' " + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                    "GROUP BY MAHALLE_ID, MAHALLE_ADI, " + groupByCol + ") M, MAHALLE I WHERE M.MAHALLE_ID = I.MAHALLE_ID  AND ROWNUM<=" + maxCount + " ORDER BY CNT DESC";
            }

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            String requestStr = "distance=" + radius + " unit=m";
            pstmt.setString(colno++, key);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setString(colno++, requestStr);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                int count = rset.getInt(3);
                double lon = rset.getDouble(4);
                double lat = rset.getDouble(5);
                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);
                String groupValue = rset.getString(7);
                if (groupValue == null) {
                    groupValue = "-";
                }
                ;

                if (type == 1)
                    array.add(new DataIl(id, name, lat, lon, extent, count, groupValue));
                else if (type == 2)
                    array.add(new DataIlce(id, name, 0, lat, lon, extent, count, groupValue));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, 0, lat, lon, extent, count, groupValue));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointCountSearchgv()

    //-----------------------------------------------------------------------------

    public static DataList[] getUserPointListWithExtent(String key, int count, double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, String whereClause, String subKeys, String pointIdList[]) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String[] keyValues = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();
    
        if (subKeys != null) {
            subKeys = subKeys + "," + key;
            subKeys.split(",");
            keyValues = subKeys.split(",");

            try {
                cnn = DbConn.getPooledConnection();
                for (int i = 0; i < keyValues.length; i++) {
                    sql = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO FROM LBS_USER_POINT WHERE KEY = ? AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? " +
                        (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY NLSSORT(POINT_NAME, 'NLS_SORT=XTURKISH'),POINT_ID ";
                    sql = "SELECT * FROM (" + sql + ") WHERE ";
                    if (pointIdList != null) {
                        sql += "POINT_ID IN (";
                        for (int j = 0; j < pointIdList.length; j++) {
                            if (j > 0)
                                sql += ",";
                            sql += "'" + pointIdList[j] + "'";
                        }
                        sql += ") AND ";
                    }
                    sql += "ROWNUM <= " + count;
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    int colno = 1;
                    pstmt.setString(colno++, keyValues[i]);
                    pstmt.setDouble(colno++, minLongitude);
                    pstmt.setDouble(colno++, maxLongitude);
                    pstmt.setDouble(colno++, minLatitude);
                    pstmt.setDouble(colno++, maxLatitude);

                    rset = pstmt.executeQuery();
                    while (rset.next()) {
                        String id = rset.getString(1);
                        String name = rset.getString(2);
                        int type = rset.getInt(3);
                        double xcoor = rset.getDouble(4);
                        double ycoor = rset.getDouble(5);
                        double angle = rset.getDouble(6);
                        String address = rset.getString(7);
                        String telephone = rset.getString(8);
                        int distance = -1;
                        array.add(new DataList(id, name, type, ycoor, xcoor, angle, distance, address, telephone));
                    } // while()
                    
                    DbConn.closeDBConnection(pstmt, rset);
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        } else {
            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO FROM LBS_USER_POINT WHERE KEY = ? AND XCOOR >= ? AND XCOOR <= ? AND YCOOR >= ? AND YCOOR <= ? " +
                    (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY NLSSORT(POINT_NAME, 'NLS_SORT=XTURKISH'),POINT_ID ";
                sql = "SELECT * FROM (" + sql + ") WHERE ";
                if (pointIdList != null) {
                    sql += "POINT_ID IN (";
                    for (int j = 0; j < pointIdList.length; j++) {
                        if (j > 0)
                            sql += ",";
                        sql += "'" + pointIdList[j] + "'";
                    }
                    sql += ") AND ";
                }
                sql += "ROWNUM <= " + count;
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                int colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setDouble(colno++, minLongitude);
                pstmt.setDouble(colno++, maxLongitude);
                pstmt.setDouble(colno++, minLatitude);
                pstmt.setDouble(colno++, maxLatitude);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    String id = rset.getString(1);
                    String name = rset.getString(2);
                    int type = rset.getInt(3);
                    double xcoor = rset.getDouble(4);
                    double ycoor = rset.getDouble(5);
                    double angle = rset.getDouble(6);
                    String address = rset.getString(7);
                    String telephone = rset.getString(8);
                    int distance = -1;
                    array.add(new DataList(id, name, type, ycoor, xcoor, angle, distance, address, telephone));
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return null;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        if (array.size() <= 0)
            return null;

        DataList[] dints = new DataList[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataList) array.get(i);
        return dints;
    } // getUserPointListWithExtent()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointListCountWithExtent(String key, int type, Extent ext, String[] pointIdList, String whereClause) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));

        Connection cnn = null;
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (";
                sql += " SELECT P.IL_ID, P.IL_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, IL Z WHERE P.KEY=? ";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.IL_ID=P.IL_ID";
                sql += " GROUP BY P.IL_ADI, P.IL_ID, Z.XCOOR, Z.YCOOR";
                sql += " ORDER BY CNT DESC) M, IL I ";
                sql += " WHERE M.IL_ID = I.IL_ID AND ROWNUM<=" + maxCount;
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (";
                sql += " SELECT P.ILCE_ID, P.ILCE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, ILCE Z WHERE P.KEY=? ";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.IL_ID=P.IL_ID AND Z.ILCE_ID=P.ILCE_ID";
                sql += " GROUP BY P.ILCE_ADI, P.ILCE_ID, Z.XCOOR, Z.YCOOR";
                sql += " ORDER BY CNT DESC) M, ILCE I ";
                sql += " WHERE M.ILCE_ID = I.ILCE_ID AND ROWNUM<=" + maxCount;
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR FROM (";
                sql += " SELECT P.MAHALLE_ID, P.MAHALLE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT FROM LBS_USER_POINT P, MAHALLE Z WHERE P.KEY=?";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.ILCE_ID=P.ILCE_ID AND Z.MAHALLE_ID=P.MAHALLE_ID";
                sql += " GROUP BY P.MAHALLE_ADI, P.MAHALLE_ID, Z.XCOOR, Z.YCOOR";
                sql += " ORDER BY CNT DESC) M, MAHALLE I";
                sql += " WHERE M.MAHALLE_ID = I.MAHALLE_ID AND ROWNUM<=" + maxCount;
            }

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                int count = rset.getInt(5);
                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);
                if (type == 1)
                    array.add(new DataIl(id, name, latitude, longitude, extent, count));
                else if (type == 2)
                    array.add(new DataIlce(id, name, 0, latitude, longitude, extent, count));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, 0, latitude, longitude, extent, count));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointCountListWithExtent()

    //-----------------------------------------------------------------------------

    public static ArrayList getUserPointListCountWithExtent(String key, int type, Extent ext, String[] pointIdList, String whereClause, String groupByCol) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        int maxCount = Integer.parseInt(Utils.getParameter("userpoint_count"));
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (type == 1) {
                sql = "SELECT M.IL_ID, M.IL_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (";
                sql += " SELECT P.IL_ID, P.IL_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol + " FROM LBS_USER_POINT P, IL Z WHERE P.KEY=? ";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.IL_ID=P.IL_ID";
                sql += " GROUP BY P.IL_ADI, P.IL_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol;
                sql += " ORDER BY CNT DESC) M, IL I ";
                sql += " WHERE M.IL_ID = I.IL_ID AND ROWNUM<=" + maxCount;
            } else if (type == 2) {
                sql = "SELECT M.ILCE_ID, M.ILCE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (";
                sql += " SELECT P.ILCE_ID, P.ILCE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol + " FROM LBS_USER_POINT P, ILCE Z WHERE P.KEY=? ";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.IL_ID=P.IL_ID AND Z.ILCE_ID=P.ILCE_ID";
                sql += " GROUP BY P.ILCE_ADI, P.ILCE_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol;
                sql += " ORDER BY CNT DESC) M, ILCE I ";
                sql += " WHERE M.ILCE_ID = I.ILCE_ID AND ROWNUM<=" + maxCount;
            } else if (type == 3) {
                sql = "SELECT M.MAHALLE_ID, M.MAHALLE_ADI, M.XCOOR, M.YCOOR, M.CNT, I.GEOMBR, M." + groupByCol + " FROM (";
                sql += " SELECT P.MAHALLE_ID, P.MAHALLE_ADI, Z.XCOOR, Z.YCOOR, COUNT(1) CNT, P." + groupByCol + " FROM LBS_USER_POINT P, MAHALLE Z WHERE P.KEY=?";
                if (pointIdList != null) {
                    sql += "AND P.POINT_ID IN (";
                    for (int i = 0; i < pointIdList.length; i++) {
                        if (i > 0)
                            sql += ",";
                        sql += "'" + pointIdList[i] + "'";
                    }
                    sql += ") ";
                }
                sql += " AND P.XCOOR >= ? AND P.XCOOR <= ? AND P.YCOOR >= ? AND P.YCOOR <= ? ";
                sql += (whereClause.length() > 0 ? " AND " + whereClause : "");
                sql += " AND Z.ILCE_ID=P.ILCE_ID AND Z.MAHALLE_ID=P.MAHALLE_ID";
                sql += " GROUP BY P.MAHALLE_ADI, P.MAHALLE_ID, Z.XCOOR, Z.YCOOR, P." + groupByCol;
                sql += " ORDER BY CNT DESC) M, MAHALLE I";
                sql += " WHERE M.MAHALLE_ID = I.MAHALLE_ID AND ROWNUM<=" + maxCount;
            }

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                long id = rset.getLong(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                int count = rset.getInt(5);
                Extent extent = null;
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(6));
                if (obj != null)
                    extent = Utils.getExtent(obj);
                String groupValue = rset.getString(7);
                if (groupValue == null) {
                    groupValue = "-";
                }
                ;

                if (type == 1)
                    array.add(new DataIl(id, name, latitude, longitude, extent, count, groupValue));
                else if (type == 2)
                    array.add(new DataIlce(id, name, 0, latitude, longitude, extent, count, groupValue));
                else if (type == 3)
                    array.add(new DataMahalle(id, name, 0, 0, latitude, longitude, extent, count, groupValue));
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return array;
    } // getUserPointCountListWithExtentgv()

    //-----------------------------------------------------------------------------

    public static DataList[] userPointList(String key, int count, String regionId, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT POINT_ID,POINT_NAME,TYP,XCOOR,YCOOR,ANGLE,ADDRESS,TELNO FROM LBS_USER_POINT WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                " AND SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=?)) = 'TRUE' ORDER BY POINT_ID,POINT_NAME";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " POINT_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, regionId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString(1);
                String name = rset.getString(2);
                int type = rset.getInt(3);
                double xcoor = rset.getDouble(4);
                double ycoor = rset.getDouble(5);
                double angle = rset.getDouble(6);
                String address = rset.getString(7);
                String telephone = rset.getString(8);
                int distance = -1;
                DataList dl = new DataList(id, name, type, ycoor, xcoor, angle, distance, address, telephone);
                dl.setAddress(address);
                array.add(dl);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataList[] dints = new DataList[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataList) array.get(i);
        return dints;
    } // userPointList()

    //-----------------------------------------------------------------------------
    public static DataUserPoint[] userPointListDetailed(String key, int count, String regionId, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_POINT WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                " AND SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=?)) = 'TRUE' ORDER BY POINT_ID,POINT_NAME";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " POINT_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, regionId);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                int distance = -1;
                DataUserPoint dur = DataUserPoint.getInstance(rset);
                dur.setDistance(distance);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserPoint[] dints = new DataUserPoint[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataUserPoint) array.get(i);
        return dints;
    } // userPointListDetailed()

    //-----------------------------------------------------------------------------

    public static int addUserRegionByUnion(String key, DataUserRegion dur, String fromType, String idList) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        
        if (!fromType.equals("IL") && !fromType.equals("ILCE") && !fromType.equals("MAHALLE") && !fromType.equals("BOLGE"))
            return -1;

        String sqlGeoloc = "SELECT GEOLOC FROM " + fromType + " WHERE " + fromType + "_ID=?";

        String[] info = Utils.splitString(idList, ",");
        if (info.length <= 0)
            return -2;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=0,YCOOR=0,GEOLOC=(" +
                sqlGeoloc + "),GEOMBR=NULL, COOR_ACCESS=0 WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, dur.name);
            pstmt.setInt(colno++, dur.type);
            if (dur.string_1 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_1);
            if (dur.string_2 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_2);
            if (dur.string_3 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_3);
            if (dur.string_4 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_4);
            if (dur.string_5 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_5);
            if (dur.string_6 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_6);
            if (dur.string_7 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_7);
            if (dur.string_8 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_8);
            if (dur.string_9 == null)
                pstmt.setNull(colno++, Types.VARCHAR);
            else
                pstmt.setString(colno++, dur.string_9);
            pstmt.setDouble(colno++, dur.number_1);
            pstmt.setDouble(colno++, dur.number_2);
            pstmt.setDouble(colno++, dur.number_3);
            pstmt.setDouble(colno++, dur.number_4);
            pstmt.setDouble(colno++, dur.number_5);
            pstmt.setDouble(colno++, dur.number_6);
            pstmt.setDouble(colno++, dur.number_7);
            pstmt.setDouble(colno++, dur.number_8);
            pstmt.setDouble(colno++, dur.number_9);
            pstmt.setLong(colno++, Long.parseLong(info[0]));
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dur.id);
            int count = pstmt.executeUpdate();
            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);

                if (dur.id == null || dur.id.length() <= 0)
                    dur.id = Utils.getUniqueRegionId();

                sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR,COOR_ACCESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0,(" +
                    sqlGeoloc + "),NULL, 0)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                pstmt.setLong(colno++, Long.parseLong(info[0]));
                pstmt.executeUpdate();
            }
            
            DbConn.closeDBConnection(pstmt, null);

            for (int i = 1; i < info.length; i++) {
                sql = "UPDATE LBS_USER_REGION SET GEOLOC=(SDO_GEOM.SDO_UNION(GEOLOC, (" + sqlGeoloc + "), 0.001)),GEOMBR=NULL WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setLong(colno++, Long.parseLong(info[i]));
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.executeUpdate();
                DbConn.closeDBConnection(pstmt, null);
            } // for()

            sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dur.id);
            pstmt.executeUpdate();

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -3;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }
        return 0;
    } // addUserRegionByUnion()

    //-----------------------------------------------------------------------------

    public static int addUserRegion(String key, DataUserRegion dur, double[] coors) {
        PreparedStatement pstmt = null;
        String sql = null;
        int count = 0;
        int colno = 0;
        Connection cnn = null;
        
        if (coors != null && coors.length < 8)
            return -1;

        JGeometry geo = null;
        STRUCT obj = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (dur.id != null) {
                sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE" +
                    (coors == null ? "" : ",XCOOR=0,YCOOR=0,GEOLOC=?,GEOMBR=NULL") + " WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                if (coors != null) {
                    geo = JGeometry.createLinearPolygon(coors, 2, 8307);
                    obj = JGeometry.store(geo, cnn);
                    pstmt.setObject(colno++, obj);
                }
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                count = pstmt.executeUpdate();
            }

            if (count <= 0) {
                
                DbConn.closeDBConnection(pstmt, null);

                if (dur.id == null || dur.id.length() <= 0)
                    dur.id = Utils.getUniqueRegionId();

                sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR,COOR_ACCESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0," +
                    (coors == null ? "NULL" : "?") + ",NULL,1)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                if (coors != null) {
                    if (obj == null) {
                        geo = JGeometry.createLinearPolygon(coors, 2, 8307);
                        obj = JGeometry.store(geo, cnn);
                    }
                    pstmt.setObject(colno++, obj);
                }
                pstmt.executeUpdate();
            }
            
            DbConn.closeDBConnection(pstmt, null);

            if (coors != null) {
                sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addUserRegion()

    //-----------------------------------------------------------------------------

    public static int addUserRegions(String key, DataUserRegion[] durl, double[][] coorList) {
        PreparedStatement pstmt = null;
        String sql = null;
        int count = 0;
        int colno = 0;

        Connection cnn = DbConn.getPooledConnection();

        for (int i = 0; i < durl.length; i++) {

            JGeometry geo = null;
            STRUCT obj = null;

            try {
                if (durl[i].id != null) {
                    sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE" +
                        (coorList == null ? "" : ",XCOOR=0,YCOOR=0,GEOLOC=?,GEOMBR=NULL") + " WHERE KEY=? AND REGION_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, durl[i].name);
                    pstmt.setInt(colno++, durl[i].type);
                    if (durl[i].string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_1);
                    if (durl[i].string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_2);
                    if (durl[i].string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_3);
                    if (durl[i].string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_4);
                    if (durl[i].string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_5);
                    if (durl[i].string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_6);
                    if (durl[i].string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_7);
                    if (durl[i].string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_8);
                    if (durl[i].string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_9);
                    pstmt.setDouble(colno++, durl[i].number_1);
                    pstmt.setDouble(colno++, durl[i].number_2);
                    pstmt.setDouble(colno++, durl[i].number_3);
                    pstmt.setDouble(colno++, durl[i].number_4);
                    pstmt.setDouble(colno++, durl[i].number_5);
                    pstmt.setDouble(colno++, durl[i].number_6);
                    pstmt.setDouble(colno++, durl[i].number_7);
                    pstmt.setDouble(colno++, durl[i].number_8);
                    pstmt.setDouble(colno++, durl[i].number_9);
                    if (coorList[i] != null && coorList[i].length >= 8) {
                        geo = JGeometry.createLinearPolygon(coorList[i], 2, 8307);
                        obj = JGeometry.store(geo, cnn);
                        pstmt.setObject(colno++, obj);
                    }
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, durl[i].id);
                    count = pstmt.executeUpdate();
                }
                
                DbConn.closeDBConnection(pstmt, null);
                
                if (count <= 0) {
                    if (durl[i].id == null || durl[i].id.length() <= 0)
                        durl[i].id = Utils.getUniqueRegionId();

                    sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0," +
                        (coorList == null ? "NULL" : "?") + ",NULL)";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, durl[i].id);
                    pstmt.setString(colno++, durl[i].name);
                    pstmt.setInt(colno++, durl[i].type);
                    if (durl[i].string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_1);
                    if (durl[i].string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_2);
                    if (durl[i].string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_3);
                    if (durl[i].string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_4);
                    if (durl[i].string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_5);
                    if (durl[i].string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_6);
                    if (durl[i].string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_7);
                    if (durl[i].string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_8);
                    if (durl[i].string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, durl[i].string_9);
                    pstmt.setDouble(colno++, durl[i].number_1);
                    pstmt.setDouble(colno++, durl[i].number_2);
                    pstmt.setDouble(colno++, durl[i].number_3);
                    pstmt.setDouble(colno++, durl[i].number_4);
                    pstmt.setDouble(colno++, durl[i].number_5);
                    pstmt.setDouble(colno++, durl[i].number_6);
                    pstmt.setDouble(colno++, durl[i].number_7);
                    pstmt.setDouble(colno++, durl[i].number_8);
                    pstmt.setDouble(colno++, durl[i].number_9);
                    if (coorList[i] != null && coorList[i].length >= 8) {
                        if (obj == null) {
                            geo = JGeometry.createLinearPolygon(coorList[i], 2, 8307);
                            obj = JGeometry.store(geo, cnn);
                        }
                        pstmt.setObject(colno++, obj);
                    }
                    pstmt.executeUpdate();
                }
                DbConn.closeDBConnection(pstmt, null);

                if (coorList[i] != null) {
                    sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, durl[i].id);
                    pstmt.executeUpdate();
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                DbConn.closeDBConnection(pstmt, null);
                DbConn.closeConnection(cnn);
                return -2;
            } finally {
                DbConn.closeDBConnection(pstmt, null);
            }
        }
        DbConn.closeDBConnection(pstmt, null);
        DbConn.closeConnection(cnn);
        return 0;
    } // addUserRegions()

    //-----------------------------------------------------------------------------

    public static int addBufferedLineToUserRegion(String key, DataUserRegion dur, double[] coors, String lineId, int bufferDist) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int count = 0;
        int colno = 0;
        Connection cnn = null;
        if ((coors != null && coors.length < 8) && lineId == null)
            return -1;

        double bufferTol = 0.0005 * bufferDist;

        JGeometry geo = null;
        STRUCT obj = null;

        if (lineId != null) {
            cnn = DbConn.getPooledConnection();
            try {
                sql = "SELECT GEOLOC FROM LBS_USER_LINE WHERE KEY=? AND LINE_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setString(1, key);
                pstmt.setString(2, lineId);
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    obj = DbConn.convToSTRUCT(rset.getObject(1));
                } else {
                    DbConn.closeDBConnection(pstmt, null);
                    DbConn.closeConnection(cnn);
                    return -2;
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            try {
                if (dur.id != null) {
                    DbConn.closeDBConnection(pstmt, null);
                    sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=0,YCOOR=0,GEOLOC=SDO_GEOM.SDO_BUFFER(?,?,?),GEOMBR=NULL WHERE KEY=? AND REGION_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, dur.name);
                    pstmt.setInt(colno++, dur.type);
                    if (dur.string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_1);
                    if (dur.string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_2);
                    if (dur.string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_3);
                    if (dur.string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_4);
                    if (dur.string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_5);
                    if (dur.string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_6);
                    if (dur.string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_7);
                    if (dur.string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_8);
                    if (dur.string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_9);
                    pstmt.setDouble(colno++, dur.number_1);
                    pstmt.setDouble(colno++, dur.number_2);
                    pstmt.setDouble(colno++, dur.number_3);
                    pstmt.setDouble(colno++, dur.number_4);
                    pstmt.setDouble(colno++, dur.number_5);
                    pstmt.setDouble(colno++, dur.number_6);
                    pstmt.setDouble(colno++, dur.number_7);
                    pstmt.setDouble(colno++, dur.number_8);
                    pstmt.setDouble(colno++, dur.number_9);
                    pstmt.setObject(colno++, obj);
                    pstmt.setInt(colno++, bufferDist);
                    pstmt.setDouble(colno++, bufferTol);
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dur.id);
                    count = pstmt.executeUpdate();
                }

                if (count <= 0) {
                    DbConn.closeDBConnection(pstmt, null);

                    if (dur.id == null || dur.id.length() <= 0)
                        dur.id = Utils.getUniqueRegionId();

                    sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0,SDO_GEOM.SDO_BUFFER(?,?,?),NULL)";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dur.id);
                    pstmt.setString(colno++, dur.name);
                    pstmt.setInt(colno++, dur.type);
                    if (dur.string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_1);
                    if (dur.string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_2);
                    if (dur.string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_3);
                    if (dur.string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_4);
                    if (dur.string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_5);
                    if (dur.string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_6);
                    if (dur.string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_7);
                    if (dur.string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_8);
                    if (dur.string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_9);
                    pstmt.setDouble(colno++, dur.number_1);
                    pstmt.setDouble(colno++, dur.number_2);
                    pstmt.setDouble(colno++, dur.number_3);
                    pstmt.setDouble(colno++, dur.number_4);
                    pstmt.setDouble(colno++, dur.number_5);
                    pstmt.setDouble(colno++, dur.number_6);
                    pstmt.setDouble(colno++, dur.number_7);
                    pstmt.setDouble(colno++, dur.number_8);
                    pstmt.setDouble(colno++, dur.number_9);
                    pstmt.setObject(colno++, obj);
                    pstmt.setInt(colno++, bufferDist);
                    pstmt.setDouble(colno++, bufferTol);
                    pstmt.executeUpdate();
                }
                DbConn.closeDBConnection(pstmt, null);

                sql ="UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.executeUpdate();

            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return -2;
            } finally {
                DbConn.closeDBConnection(pstmt, null);
                DbConn.closeConnection(cnn);
            }

        }  else {

            try {
                cnn = DbConn.getPooledConnection();
                if (dur.id != null) {
                    sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE" +
                        (coors == null ? "" : ",XCOOR=0,YCOOR=0,GEOLOC=SDO_GEOM.SDO_BUFFER(?,?,?),GEOMBR=NULL") + " WHERE KEY=? AND REGION_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, dur.name);
                    pstmt.setInt(colno++, dur.type);
                    if (dur.string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_1);
                    if (dur.string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_2);
                    if (dur.string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_3);
                    if (dur.string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_4);
                    if (dur.string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_5);
                    if (dur.string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_6);
                    if (dur.string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_7);
                    if (dur.string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_8);
                    if (dur.string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_9);
                    pstmt.setDouble(colno++, dur.number_1);
                    pstmt.setDouble(colno++, dur.number_2);
                    pstmt.setDouble(colno++, dur.number_3);
                    pstmt.setDouble(colno++, dur.number_4);
                    pstmt.setDouble(colno++, dur.number_5);
                    pstmt.setDouble(colno++, dur.number_6);
                    pstmt.setDouble(colno++, dur.number_7);
                    pstmt.setDouble(colno++, dur.number_8);
                    pstmt.setDouble(colno++, dur.number_9);
                    if (coors != null) {
                        geo = JGeometry.createLinearPolygon(coors, 2, 8307);
                        obj = JGeometry.store(geo, cnn);
                        pstmt.setObject(colno++, obj);
                        pstmt.setInt(colno++, bufferDist);
                        pstmt.setDouble(colno++, bufferTol);
                    }
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dur.id);
                    count = pstmt.executeUpdate();
                }

                if (count <= 0) {
                    DbConn.closeDBConnection(pstmt, null);

                    if (dur.id == null || dur.id.length() <= 0)
                        dur.id = Utils.getUniqueRegionId();

                    sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0," +
                        (coors == null ? "NULL" : "SDO_GEOM.SDO_BUFFER(?,?,?)") + ",NULL)";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dur.id);
                    pstmt.setString(colno++, dur.name);
                    pstmt.setInt(colno++, dur.type);
                    if (dur.string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_1);
                    if (dur.string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_2);
                    if (dur.string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_3);
                    if (dur.string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_4);
                    if (dur.string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_5);
                    if (dur.string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_6);
                    if (dur.string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_7);
                    if (dur.string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_8);
                    if (dur.string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dur.string_9);
                    pstmt.setDouble(colno++, dur.number_1);
                    pstmt.setDouble(colno++, dur.number_2);
                    pstmt.setDouble(colno++, dur.number_3);
                    pstmt.setDouble(colno++, dur.number_4);
                    pstmt.setDouble(colno++, dur.number_5);
                    pstmt.setDouble(colno++, dur.number_6);
                    pstmt.setDouble(colno++, dur.number_7);
                    pstmt.setDouble(colno++, dur.number_8);
                    pstmt.setDouble(colno++, dur.number_9);
                    if (coors != null) {
                        if (obj == null) {
                            geo = JGeometry.createLinearPolygon(coors, 2, 8307);
                            obj = JGeometry.store(geo, cnn);
                        }
                        pstmt.setObject(colno++, obj);
                        pstmt.setInt(colno++, bufferDist);
                        pstmt.setDouble(colno++, bufferTol);
                    }
                    pstmt.executeUpdate();
                }
                
                DbConn.closeDBConnection(pstmt, null);

                if (coors != null) {
                    sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dur.id);
                    pstmt.executeUpdate();
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                return -2;
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }
        }

        return 0;
    } // addBufferedLineToUserRegion()

    //-----------------------------------------------------------------------------

    public static int addBufferedRouteToUserRegion(String key, DataUserRegion dur, long pathId, int bufferDist) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Connection cnn = null;
        String sql = null;
        int count = 0;
        int colno = 0;

        STRUCT obj = null;

        double bufferTol = 0.0005 * bufferDist;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT GEOLOC FROM NET_PATHS WHERE PATH_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, pathId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                obj = DbConn.convToSTRUCT(rset.getObject(1));
            } else {
                DbConn.closeDBConnection(pstmt, null);
                DbConn.closeConnection(cnn);
                return -2;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        try {
            if (dur.id != null) {
                DbConn.closeDBConnection(pstmt, null);
                sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=0,YCOOR=0,GEOLOC=SDO_GEOM.SDO_BUFFER(?,?,?),GEOMBR=NULL,COOR_ACCESS=0 WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                pstmt.setObject(colno++, obj);
                pstmt.setInt(colno++, bufferDist);
                pstmt.setDouble(colno++, bufferTol);
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                count = pstmt.executeUpdate();
            }

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);

                if (dur.id == null || dur.id.length() <= 0)
                    dur.id = Utils.getUniqueRegionId();

                sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR,COOR_ACCESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0,SDO_GEOM.SDO_BUFFER(?,?,?),NULL,0)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                pstmt.setObject(colno++, obj);
                pstmt.setInt(colno++, bufferDist);
                pstmt.setDouble(colno++, bufferTol);
                pstmt.executeUpdate();
            }
            DbConn.closeDBConnection(pstmt, null);

            sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dur.id);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addBufferedRouteToUserRegion()

    //-----------------------------------------------------------------------------

    public static int addRouteToUserLine(String key, DataUserLine dul, long pathId, boolean routeGeometry) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int count = 0;
        int colno = 0;
        Connection cnn = null;
        STRUCT obj = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT GEOLOC FROM NET_PATHS WHERE PATH_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, pathId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                obj = DbConn.convToSTRUCT(rset.getObject(1));
            } else {
                return -2;
            }
            if (dul.id != null) {
                DbConn.closeDBConnection(pstmt, rset);
                sql = "UPDATE LBS_USER_LINE SET LINE_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=0,YCOOR=0,GEOLOC=?,GEOMBR=NULL,COOR_ACCESS=? WHERE KEY=? AND LINE_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, dul.name);
                pstmt.setInt(colno++, dul.type);
                if (dul.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_1);
                if (dul.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_2);
                if (dul.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_3);
                if (dul.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_4);
                if (dul.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_5);
                if (dul.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_6);
                if (dul.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_7);
                if (dul.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_8);
                if (dul.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_9);
                pstmt.setDouble(colno++, dul.number_1);
                pstmt.setDouble(colno++, dul.number_2);
                pstmt.setDouble(colno++, dul.number_3);
                pstmt.setDouble(colno++, dul.number_4);
                pstmt.setDouble(colno++, dul.number_5);
                pstmt.setDouble(colno++, dul.number_6);
                pstmt.setDouble(colno++, dul.number_7);
                pstmt.setDouble(colno++, dul.number_8);
                pstmt.setDouble(colno++, dul.number_9);
                pstmt.setObject(colno++, obj);
                if (routeGeometry)
                    pstmt.setInt(colno++, 1);
                else
                    pstmt.setInt(colno++, 0);
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dul.id);
                count = pstmt.executeUpdate();
            }

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, rset);

                if (dul.id == null || dul.id.length() <= 0)
                    dul.id = Utils.getUniqueRegionId();

                sql = "INSERT INTO LBS_USER_LINE (KEY,LINE_ID,LINE_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR,COOR_ACCESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0,?,NULL,?)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dul.id);
                pstmt.setString(colno++, dul.name);
                pstmt.setInt(colno++, dul.type);
                if (dul.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_1);
                if (dul.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_2);
                if (dul.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_3);
                if (dul.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_4);
                if (dul.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_5);
                if (dul.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_6);
                if (dul.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_7);
                if (dul.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_8);
                if (dul.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_9);
                pstmt.setDouble(colno++, dul.number_1);
                pstmt.setDouble(colno++, dul.number_2);
                pstmt.setDouble(colno++, dul.number_3);
                pstmt.setDouble(colno++, dul.number_4);
                pstmt.setDouble(colno++, dul.number_5);
                pstmt.setDouble(colno++, dul.number_6);
                pstmt.setDouble(colno++, dul.number_7);
                pstmt.setDouble(colno++, dul.number_8);
                pstmt.setDouble(colno++, dul.number_9);
                pstmt.setObject(colno++, obj);
                if (routeGeometry)
                    pstmt.setInt(colno++, 1);
                else
                    pstmt.setInt(colno++, 0);
                pstmt.executeUpdate();
            }
            DbConn.closeDBConnection(pstmt, rset);

            sql = "UPDATE LBS_USER_LINE SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND LINE_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dul.id);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addRouteToUserLine()

    //-----------------------------------------------------------------------------

    public static DataUserRegion[] getUserRegion(String key, String id, boolean withCoors) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                int coorAccess = rset.getInt("COOR_ACCESS");
                if (coorAccess <= 0)
                    withCoors = false;
                DataUserRegion dur = DataUserRegion.getInstance(rset, withCoors);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserRegion[] durs = new DataUserRegion[array.size()];
        for (int i = 0; i < durs.length; i++)
            durs[i] = (DataUserRegion) array.get(i);
        return durs;
    } // getUserRegion()

    //-----------------------------------------------------------------------------

    public static DataUserRegion[] userRegionSearch(String key, int count, double latitude, double longitude, String whereClause) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_REGION WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                " AND SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE' ORDER BY REGION_ID,REGION_NAME";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataUserRegion dur = DataUserRegion.getInstance(rset, false);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserRegion[] durs = new DataUserRegion[array.size()];
        for (int i = 0; i < durs.length; i++)
            durs[i] = (DataUserRegion) array.get(i);
        return durs;
    } // userRegionSearch()

    //-----------------------------------------------------------------------------

    public static DataUserRegion[] userRegionSearch(String key, int count, String pointId, String whereClause) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_REGION WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") +
                " AND SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID=?)) = 'TRUE' ORDER BY REGION_ID,REGION_NAME";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, pointId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataUserRegion dur = DataUserRegion.getInstance(rset, false);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserRegion[] durs = new DataUserRegion[array.size()];
        for (int i = 0; i < durs.length; i++)
            durs[i] = (DataUserRegion) array.get(i);
        return durs;
    } // userRegionSearch()

    //-----------------------------------------------------------------------------

    public static int removeUserRegion(String key, String id) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "DELETE FROM LBS_USER_REGION WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            int count = pstmt.executeUpdate();
            if (count > 0)
                return 0;

            return -1;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return -2;
    } // removeUserRegion()

    //-----------------------------------------------------------------------------

    public static DataList[] getUserRegionList(String key, int count, String pointId, double latitude, double longitude, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT REGION_ID,REGION_NAME,TYP,GEOMBR FROM LBS_USER_REGION WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "");
            if (pointId != null)
                sql += "AND SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID=?)) = 'TRUE' ";
            else if (latitude != 0.00 && longitude != 0.00)
                sql += " AND SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE' ";
            sql += " ORDER BY NLSSORT(REGION_NAME, 'NLS_SORT=XTURKISH'),REGION_ID";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " REGION_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            System.out.println("Sql->"+sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            if (pointId != null) {
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, pointId);
            } else if (latitude != 0.00 && longitude != 0.00) {
                pstmt.setDouble(colno++, longitude);
                pstmt.setDouble(colno++, latitude);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString(1);
                String name = rset.getString(2);
                int type = rset.getInt(3);
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(4));
                Extent extent = null;
                if (obj != null)
                    extent = Utils.getExtent(obj);
                array.add(new DataList(id, name, type, extent));
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataList[] dints = new DataList[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataList) array.get(i);
        return dints;
    } // getUserRegionList()


    //-----------------------------------------------------------------------------

    public static DataUserRegion[] getUserRegionListDetailed(String key, int count, String pointId, double latitude, double longitude, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_REGION WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "");
            if (pointId != null)
                sql += "AND SDO_ANYINTERACT(GEOLOC, (SELECT GEOLOC FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID=?)) = 'TRUE' ";
            else if (latitude != 0.00 && longitude != 0.00)
                sql += " AND SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE' ";
            sql += " ORDER BY NLSSORT(REGION_NAME, 'NLS_SORT=XTURKISH'),REGION_ID";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " REGION_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            if (pointId != null) {
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, pointId);
            } else if (latitude != 0.00 && longitude != 0.00) {
                pstmt.setDouble(colno++, longitude);
                pstmt.setDouble(colno++, latitude);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataUserRegion dur = DataUserRegion.getInstance(rset, false);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserRegion[] durs = new DataUserRegion[array.size()];
        for (int i = 0; i < durs.length; i++)
            durs[i] = (DataUserRegion) array.get(i);
        return durs;
    } // getUserRegionList()

    //-----------------------------------------------------------------------------

    public static int addUserRegionFromFile(String key, DataUserRegion dur, File savedFile) {
        PreparedStatement pstmt = null;
        String sql = null;

        return 0;
    } // addUserRegionFromFile()

    //-----------------------------------------------------------------------------

    public static int addUserLine(String key, DataUserLine dul, double[] coors) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        int count = 0;
        int colno = 0;

        if (coors != null && coors.length < 4)
            return -1;

        JGeometry geo = null;
        STRUCT obj = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (dul.id != null) {
                sql = "UPDATE LBS_USER_LINE SET LINE_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE" +
                    (coors == null ? "" : ",XCOOR=0,YCOOR=0,GEOLOC=?,GEOMBR=NULL,COOR_ACCESS=1") + " WHERE KEY=? AND LINE_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, dul.name);
                pstmt.setInt(colno++, dul.type);
                if (dul.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_1);
                if (dul.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_2);
                if (dul.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_3);
                if (dul.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_4);
                if (dul.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_5);
                if (dul.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_6);
                if (dul.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_7);
                if (dul.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_8);
                if (dul.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_9);
                pstmt.setDouble(colno++, dul.number_1);
                pstmt.setDouble(colno++, dul.number_2);
                pstmt.setDouble(colno++, dul.number_3);
                pstmt.setDouble(colno++, dul.number_4);
                pstmt.setDouble(colno++, dul.number_5);
                pstmt.setDouble(colno++, dul.number_6);
                pstmt.setDouble(colno++, dul.number_7);
                pstmt.setDouble(colno++, dul.number_8);
                pstmt.setDouble(colno++, dul.number_9);
                if (coors != null) {
                    geo = JGeometry.createLinearLineString(coors, 2, 8307);
                    obj = JGeometry.store(geo, cnn);
                    pstmt.setObject(colno++, obj);
                }
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dul.id);
                count = pstmt.executeUpdate();
            }

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);

                if (dul.id == null || dul.id.length() <= 0)
                    dul.id = Utils.getUniqueLineId();

                sql = "INSERT INTO LBS_USER_LINE (KEY,LINE_ID,LINE_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR,COOR_ACCESS) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0," +
                    (coors == null ? "NULL" : "?") + ",NULL,1)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dul.id);
                pstmt.setString(colno++, dul.name);
                pstmt.setInt(colno++, dul.type);
                if (dul.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_1);
                if (dul.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_2);
                if (dul.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_3);
                if (dul.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_4);
                if (dul.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_5);
                if (dul.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_6);
                if (dul.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_7);
                if (dul.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_8);
                if (dul.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dul.string_9);
                pstmt.setDouble(colno++, dul.number_1);
                pstmt.setDouble(colno++, dul.number_2);
                pstmt.setDouble(colno++, dul.number_3);
                pstmt.setDouble(colno++, dul.number_4);
                pstmt.setDouble(colno++, dul.number_5);
                pstmt.setDouble(colno++, dul.number_6);
                pstmt.setDouble(colno++, dul.number_7);
                pstmt.setDouble(colno++, dul.number_8);
                pstmt.setDouble(colno++, dul.number_9);
                if (coors != null) {
                    if (obj == null) {
                        geo = JGeometry.createLinearLineString(coors, 2, 8307);
                        obj = JGeometry.store(geo, cnn);
                    }
                    pstmt.setObject(colno++, obj);
                }
                pstmt.executeUpdate();
            }
            DbConn.closeDBConnection(pstmt, null);

            if (coors != null) {
                sql = "UPDATE LBS_USER_LINE SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND LINE_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dul.id);
                pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addUserLine()

    //-----------------------------------------------------------------------------

    public static int addUserLines(String key, DataUserLine[] dull, double[][] coorList) {
        PreparedStatement pstmt = null;
        String sql = null;
        int count = 0;
        int colno = 0;
        Connection cnn = null;
        
        cnn = DbConn.getPooledConnection();
        
        for (int i = 0; i < dull.length; i++) {

            JGeometry geo = null;
            STRUCT obj = null;

            try {
                if (dull[i].id != null) {
                    sql = "UPDATE LBS_USER_LINE SET LINE_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE" +
                        (coorList == null ? "" : ",XCOOR=0,YCOOR=0,GEOLOC=?,GEOMBR=NULL,COOR_ACCESS=1") + " WHERE KEY=? AND LINE_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, dull[i].name);
                    pstmt.setInt(colno++, dull[i].type);
                    if (dull[i].string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_1);
                    if (dull[i].string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_2);
                    if (dull[i].string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_3);
                    if (dull[i].string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_4);
                    if (dull[i].string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_5);
                    if (dull[i].string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_6);
                    if (dull[i].string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_7);
                    if (dull[i].string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_8);
                    if (dull[i].string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_9);
                    pstmt.setDouble(colno++, dull[i].number_1);
                    pstmt.setDouble(colno++, dull[i].number_2);
                    pstmt.setDouble(colno++, dull[i].number_3);
                    pstmt.setDouble(colno++, dull[i].number_4);
                    pstmt.setDouble(colno++, dull[i].number_5);
                    pstmt.setDouble(colno++, dull[i].number_6);
                    pstmt.setDouble(colno++, dull[i].number_7);
                    pstmt.setDouble(colno++, dull[i].number_8);
                    pstmt.setDouble(colno++, dull[i].number_9);
                    if (coorList[i] != null && coorList[i].length >= 2) {
                        geo = JGeometry.createLinearLineString(coorList[i], 2, 8307);
                        obj = JGeometry.store(geo, cnn);
                        pstmt.setObject(colno++, obj);
                    }
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dull[i].id);
                    count = pstmt.executeUpdate();
                }

                if (count <= 0) {
                   DbConn.closeDBConnection(pstmt, null);

                    if (dull[i].id == null || dull[i].id.length() <= 0)
                        dull[i].id = Utils.getUniqueLineId();

                    sql = "INSERT INTO LBS_USER_LINE (KEY,LINE_ID,LINE_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0," +
                        (coorList == null ? "NULL" : "?") + ",NULL)";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dull[i].id);
                    pstmt.setString(colno++, dull[i].name);
                    pstmt.setInt(colno++, dull[i].type);
                    if (dull[i].string_1 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_1);
                    if (dull[i].string_2 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_2);
                    if (dull[i].string_3 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_3);
                    if (dull[i].string_4 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_4);
                    if (dull[i].string_5 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_5);
                    if (dull[i].string_6 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_6);
                    if (dull[i].string_7 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_7);
                    if (dull[i].string_8 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_8);
                    if (dull[i].string_9 == null)
                        pstmt.setNull(colno++, Types.VARCHAR);
                    else
                        pstmt.setString(colno++, dull[i].string_9);
                    pstmt.setDouble(colno++, dull[i].number_1);
                    pstmt.setDouble(colno++, dull[i].number_2);
                    pstmt.setDouble(colno++, dull[i].number_3);
                    pstmt.setDouble(colno++, dull[i].number_4);
                    pstmt.setDouble(colno++, dull[i].number_5);
                    pstmt.setDouble(colno++, dull[i].number_6);
                    pstmt.setDouble(colno++, dull[i].number_7);
                    pstmt.setDouble(colno++, dull[i].number_8);
                    pstmt.setDouble(colno++, dull[i].number_9);
                    if (coorList[i] != null && coorList[i].length >= 2) {
                        if (obj == null) {
                            geo = JGeometry.createLinearLineString(coorList[i], 2, 8307);
                            obj = JGeometry.store(geo, cnn);
                        }
                        pstmt.setObject(colno++, obj);
                    }
                    pstmt.executeUpdate();
                }
                
                DbConn.closeDBConnection(pstmt, null);
                
                if (coorList[i] != null) {
                    sql = "UPDATE LBS_USER_LINE SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)) WHERE KEY=? AND LINE_ID=?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.clearParameters();
                    colno = 1;
                    pstmt.setString(colno++, key);
                    pstmt.setString(colno++, dull[i].id);
                    pstmt.executeUpdate();
                }
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
                DbConn.closeDBConnection(pstmt, null);
                DbConn.closeConnection(cnn);
                return -2;
            } finally {
                DbConn.closeDBConnection(pstmt, null);
            }
        }
        DbConn.closeDBConnection(pstmt, null);
        DbConn.closeConnection(cnn);
        return 0;
    } // addUserLines()

    //-----------------------------------------------------------------------------

    public static DataUserLine[] getUserLine(String key, String id, boolean withCoors) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_LINE WHERE KEY=? AND LINE_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                int coorAccess = rset.getInt("COOR_ACCESS");
                if (coorAccess <= 0)
                    withCoors = false;
                DataUserLine dur = DataUserLine.getInstance(rset, withCoors);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserLine[] durs = new DataUserLine[array.size()];
        for (int i = 0; i < durs.length; i++)
            durs[i] = (DataUserLine) array.get(i);
        return durs;
    } // getUserLine()

    //-----------------------------------------------------------------------------

    public static int removeUserLine(String key, String id) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "DELETE FROM LBS_USER_LINE WHERE KEY=? AND LINE_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            int count = pstmt.executeUpdate();
            if (count > 0)
                return 0;

            return -1;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return -2;
    } // removeUserLine()

    //-----------------------------------------------------------------------------

    public static DataList[] getUserLineList(String key, int count, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT LINE_ID,LINE_NAME,TYP,GEOMBR FROM LBS_USER_LINE WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY LINE_NAME,LINE_ID";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " LINE_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString(1);
                String name = rset.getString(2);
                int type = rset.getInt(3);
                STRUCT obj = DbConn.convToSTRUCT(rset.getObject(4));
                Extent extent = Utils.getExtent(obj);
                array.add(new DataList(id, name, type, extent));
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataList[] dints = new DataList[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataList) array.get(i);
        return dints;
    } // getUserLineList()

    //-----------------------------------------------------------------------------

    public static DataUserLine[] getUserLineListDetailed(String key, int count, String whereClause, String idList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();

        if (idList == null)
            idList = "";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM LBS_USER_LINE WHERE KEY=?" + (whereClause.length() > 0 ? " AND " + whereClause : "") + " ORDER BY NLSSORT(LINE_NAME, 'NLS_SORT=XTURKISH'),LINE_ID";
            sql = "SELECT * FROM (" + sql + ") WHERE " + (idList.length() > 0 ? " LINE_ID IN (" + idList + ") AND " : "") + "ROWNUM <= " + count;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataUserLine dur = DataUserLine.getInstance(rset, false);
                array.add(dur);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataUserLine[] dints = new DataUserLine[array.size()];
        for (int i = 0; i < dints.length; i++)
            dints[i] = (DataUserLine) array.get(i);
        return dints;
    } // getUserLineListDetailed()

    //-----------------------------------------------------------------------------

    public static DataTrafficEvent[] getTrafficEventSearch(long pathId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT ID,EVENT_DESC,EVENT_CODE,ICON_CODE,PRIMARY_LATITUDE,PRIMARY_LONGITUDE,DELAY,SPEED,TMC_ROAD_NAME,TMC_START_POINT_NAME,TMC_END_POINT_NAME,TO_NUMBER('0') DISTANCE FROM TRAFFIC_EVENT";
            sql += " WHERE TMC_LOCATION_PRIMARY IN (SELECT ABS(TMC_KOD) FROM TMC_LINKS WHERE ID IN (SELECT LINK_ID FROM MSNTRAFIK_PATHLINKS WHERE PATH_ID = ? ))";
            sql += " OR TMC_LOCATION_SECONDARY IN (SELECT ABS(TMC_KOD) FROM TMC_LINKS WHERE ID IN (SELECT LINK_ID FROM MSNTRAFIK_PATHLINKS WHERE PATH_ID = ? ))";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setLong(colno++, pathId);
            pstmt.setLong(colno++, pathId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTrafficEvent dte = DataTrafficEvent.getInstance(rset);
                array.add(dte);
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

        DataTrafficEvent[] dtes = new DataTrafficEvent[array.size()];
        for (int i = 0; i < dtes.length; i++)
            dtes[i] = (DataTrafficEvent) array.get(i);
        return dtes;
    } // getTrafficEventSearch()

    //-----------------------------------------------------------------------------

    public static DataTrafficEvent[] getTrafficEventSearch(double latitude, double longitude, int radius, int zone) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM (SELECT INNER_QUERY.*, SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(PRIMARY_LONGITUDE,PRIMARY_LATITUDE,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (SELECT ID,EVENT_DESC,EVENT_CODE,ICON_CODE,PRIMARY_LATITUDE,PRIMARY_LONGITUDE,DELAY,SPEED,TMC_ROAD_NAME,TMC_START_POINT_NAME,TMC_END_POINT_NAME,((?-PRIMARY_LONGITUDE)*(?-PRIMARY_LONGITUDE)+(?-PRIMARY_LATITUDE)*(?-PRIMARY_LATITUDE)) REF_DISTANCE FROM TRAFFIC_EVENT";
            sql += " ORDER BY REF_DISTANCE) INNER_QUERY WHERE REF_DISTANCE < ?)";
            sql += " ORDER BY DISTANCE";
            Utils.showText("EVENT SQL: " + sql);
            Utils.showText("LON/LAT: " + longitude + "," + latitude + ", RADIUS: " + radius + ", ZONE: " + zone);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTrafficEvent dte = DataTrafficEvent.getInstance(rset);
                array.add(dte);
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

        DataTrafficEvent[] dtes = new DataTrafficEvent[array.size()];
        for (int i = 0; i < dtes.length; i++)
            dtes[i] = (DataTrafficEvent) array.get(i);
        return dtes;
    } // getTrafficEventSearch()

    //-----------------------------------------------------------------------------

    public static DataTrafficInfo getTrafficInfo(double latitude, double longitude) {
        DataTrafficInfo dti = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT ID,SPEED,SPEED_LIMIT FROM TMC_LINKS WHERE ID = (SELECT LINK_ID FROM NET_LINKS WHERE SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE')";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                dti = DataTrafficInfo.getInstance(rset);
                return dti;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dti;
    } // getTrafficInfo()

    //-----------------------------------------------------------------------------

    public static DataTmcFlow[] getTrafficTmcFlow(String tmcKodList) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        
        ArrayList array = new ArrayList();
        String[] info = Utils.splitString(tmcKodList, ",");

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT TMC_KOD,TRAVEL_TIME,SPEED,DELAY,OPTIMAL_TRAVEL_TIME,OPTIMAL_SPEED,LENGTH_METER,LINK_IN_JAM FROM TMC_FLOW_LAST WHERE TMC_KOD IN (";
            for (int i = 0; i < info.length; i++) {
                if (i > 0)
                    sql += ",";
                sql += "?";
            } // for()
            sql += ")";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            for (int i = 0; i < info.length; i++)
                pstmt.setString(colno++, info[i]);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTmcFlow dtf = DataTmcFlow.getInstance(rset);
                array.add(dtf);
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

        DataTmcFlow[] dtfs = new DataTmcFlow[array.size()];
        for (int i = 0; i < dtfs.length; i++)
            dtfs[i] = (DataTmcFlow) array.get(i);
        return dtfs;
    } // getTrafficTmcFlow()

    //-----------------------------------------------------------------------------

    public static String[] getTrafficStartList(String key, long ilId, String endName, double latitude, double longitude, int detail) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (latitude != 0.00 || longitude != 0.00)
                sql = "SELECT FROM_NAME FROM (SELECT MP.FROM_NAME,SDO_GEOM.SDO_DISTANCE(NN.GEOLOC,SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),0.001) DISTANCE FROM MSNTRAFIK_PATHS MP, NET_NODES NN WHERE (NN.NODE_ID=MP.START_NODE_ID) AND MP.IL=?" +
                    (endName != null ? " AND TO_NAME=?" : "") + " AND DETAIL <= ?) WHERE DISTANCE IS NOT NULL ORDER BY DISTANCE";
            else
                sql = "SELECT DISTINCT FROM_NAME FROM MSNTRAFIK_PATHS WHERE IL=?" + (endName != null ? " AND TO_NAME=?" : "") + " AND DETAIL <= ? ORDER BY NLSSORT(FROM_NAME, 'NLS_SORT=XTURKISH') ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (latitude != 0.00 || longitude != 0.00) {
                pstmt.setDouble(colno++, longitude);
                pstmt.setDouble(colno++, latitude);
            }
            pstmt.setLong(colno++, ilId);
            if (endName != null)
                pstmt.setString(colno++, endName);
            pstmt.setInt(colno++, detail);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String name = rset.getString(1);
                if (latitude != 0.00 || longitude != 0.00) {
                    boolean found = false;
                    for (int j = 0; j < array.size() && !found; j++) {
                        String nm = (String) array.get(j);
                        if (nm.equalsIgnoreCase(name))
                            found = true;
                    } // for()
                    if (found)
                        continue;
                }

                array.add(name);
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

        String[] names = new String[array.size()];
        for (int i = 0; i < names.length; i++)
            names[i] = (String) array.get(i);
        return names;
    } // getTrafficStartList()

    //-----------------------------------------------------------------------------

    public static String[] getTrafficEndList(String key, long ilId, String startName, double latitude, double longitude, int detail) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            if (latitude != 0.00 || longitude != 0.00)
                sql = "SELECT TO_NAME FROM (SELECT MP.TO_NAME,SDO_GEOM.SDO_DISTANCE(NN.GEOLOC,SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),0.001) DISTANCE FROM MSNTRAFIK_PATHS MP, NET_NODES NN WHERE (NN.NODE_ID=MP.END_NODE_ID) AND MP.IL=?" +
                    (startName != null ? " AND FROM_NAME=?" : "") + " AND DETAIL <= ?) WHERE DISTANCE IS NOT NULL ORDER BY DISTANCE";
            else
                sql = "SELECT DISTINCT TO_NAME FROM MSNTRAFIK_PATHS WHERE IL=?" + (startName != null ? " AND FROM_NAME=?" : "") + " AND DETAIL <= ? ORDER BY NLSSORT(TO_NAME, 'NLS_SORT=XTURKISH')";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (latitude != 0.00 || longitude != 0.00) {
                pstmt.setDouble(colno++, longitude);
                pstmt.setDouble(colno++, latitude);
            }
            pstmt.setLong(colno++, ilId);
            if (startName != null)
                pstmt.setString(colno++, startName);
            pstmt.setInt(colno++, detail);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String name = rset.getString(1);
                if (latitude != 0.00 || longitude != 0.00) {
                    boolean found = false;
                    for (int j = 0; j < array.size() && !found; j++) {
                        String nm = (String) array.get(j);
                        if (nm.equalsIgnoreCase(name))
                            found = true;
                    } // for()
                    if (found)
                        continue;
                }

                array.add(name);
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

        String[] names = new String[array.size()];
        for (int i = 0; i < names.length; i++)
            names[i] = (String) array.get(i);
        return names;
    } // getTrafficEndList()

    //-----------------------------------------------------------------------------

    public static DataTrafficRoute[] getTrafficRoute(String key, long ilId, String fromName, String toName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT PATH_ID,FROM_NAME,TO_NAME,EXP,DISTANCE,DURATION,DELAY,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR, FROM_XCOOR, FROM_YCOOR, TO_XCOOR, TO_YCOOR FROM MSNTRAFIK_PATHS WHERE IL=? AND FROM_NAME=? AND TO_NAME=? ORDER BY USE_COUNT DESC";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, ilId);
            pstmt.setString(2, fromName);
            pstmt.setString(3, toName);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTrafficRoute dtr = DataTrafficRoute.getInstance(rset);
                array.add(dtr);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataTrafficRoute[] dtrs = new DataTrafficRoute[array.size()];
        for (int i = 0; i < dtrs.length; i++)
            dtrs[i] = (DataTrafficRoute) array.get(i);
        return dtrs;
    } // getTrafficRoute()

    //-----------------------------------------------------------------------------

    public static DataTrafficRoute[] getTrafficRouteList(String key, long ilId, int detail) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT PATH_ID,FROM_NAME,TO_NAME,EXP,DISTANCE,DURATION,DELAY,SDO_GEOM.SDO_MBR(GEOLOC) GEOMBR, FROM_XCOOR, FROM_YCOOR, TO_XCOOR, TO_YCOOR FROM MSNTRAFIK_PATHS WHERE IL=? AND DETAIL <= ? ORDER BY  NLSSORT(FROM_NAME, 'NLS_SORT=XTURKISH'), NLSSORT(TO_NAME, 'NLS_SORT=XTURKISH') ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, ilId);
            pstmt.setInt(2, detail);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTrafficRoute dtr = DataTrafficRoute.getInstance(rset);
                array.add(dtr);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        DataTrafficRoute[] dtrs = new DataTrafficRoute[array.size()];
        for (int i = 0; i < dtrs.length; i++)
            dtrs[i] = (DataTrafficRoute) array.get(i);
        return dtrs;
    } // getTrafficRouteList()

    //-----------------------------------------------------------------------------

    public static DataDemographic getDemographicInfo(String key, long ilId, long ilceId, long mahalleId, String infoType) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        String columnList = null;
        String tableName = "";

        try {
            cnn = DbConn.getPooledConnection();
            if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_NUFUS)) {
                columnList = "NUFUS";
                if (mahalleId > 0) {
                    tableName = "DEMOGRAFIK_MAHALLE";
                } else if (ilceId > 0) {
                    tableName = "DEMOGRAFIK_ILCE";
                } else if (ilId > 0) {
                    tableName = "DEMOGRAFIK_IL";
                }
            } else if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_OKUMAYAZMA)) {
                columnList = "nvl(ERKEK_OKURYAZAR_FAKAT_TERK,0)+nvl(ERKEK_ILKOGRETIM_MEZUNU,0)+nvl(ERKEK_ILKOKUL_MEZUNU,0)+nvl(ERKEK_LISE_VE_DENGI_MEZUN,0)+nvl(ERKEK_ORTAOKUL_DENGI_MEZUN,0)+nvl(ERKEK_YUKSEKOKUL_FAKULTE_M,0)+nvl(ERKEK_YUKSEK_LISANS_MEZUNU,0)+nvl(ERKEK_DOKTORA_MEZUNU,0)," +
                    "nvl(KADIN_OKURYAZAR_FAKAT_TERK,0)+nvl(KADIN_ILKOGRETIM_MEZUNU,0)+nvl(KADIN_ILKOKUL_MEZUNU,0)+nvl(KADIN_LISE_VE_DENGI_MEZUN,0)+nvl(KADIN_ORTAOKUL_DENGI_MEZUN,0)+nvl(KADIN_YUKSEKOKUL_FAKULTE_M,0)+nvl(KADIN_YUKSEK_LISANS_MEZUNU,0)+nvl(KADIN_DOKTORA_MEZUNU,0)," +
                    "ERKEK_OKURYAZAR_DEGIL,KADIN_OKURYAZAR_DEGIL,ERKEK_BILINMEYEN,KADIN_BILINMEYEN";
                if (mahalleId > 0)
                    tableName = "DEMOGRAFIK_MAHALLE";
                else if (ilceId > 0)
                    tableName = "DEMOGRAFIK_ILCE";
                else if (ilId > 0)
                    tableName = "DEMOGRAFIK_IL";
            } else if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_KONUT)) {
                columnList = "KONUT_SAYISI,YAZLIK_SAYISI";
                if (mahalleId > 0)
                    tableName = "DEMOGRAFIK_MAHALLE";
                else if (ilceId > 0)
                    tableName = "DEMOGRAFIK_ILCE";
                else if (ilId > 0)
                    tableName = "DEMOGRAFIK_IL";
            } else if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_EGITIM)) {
                columnList = "ERKEK_BILINMEYEN, KADIN_BILINMEYEN, ERKEK_ILKOKUL_MEZUNU, KADIN_ILKOKUL_MEZUNU, ERKEK_ILKOGRETIM_MEZUNU, KADIN_ILKOGRETIM_MEZUNU, ERKEK_LISE_VE_DENGI_MEZUN, KADIN_LISE_VE_DENGI_MEZUN, ERKEK_ORTAOKUL_DENGI_MEZUN, KADIN_ORTAOKUL_DENGI_MEZUN, ERKEK_YUKSEKOKUL_FAKULTE_M, KADIN_YUKSEKOKUL_FAKULTE_M";
                if (mahalleId > 0)
                    tableName = "DEMOGRAFIK_MAHALLE";
                else if (ilceId > 0)
                    tableName = "DEMOGRAFIK_ILCE";
                else if (ilId > 0)
                    tableName = "DEMOGRAFIK_IL";
            } else if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_YASGRUP)) {
                columnList =
                    "YAS_GRUBU_0_4, YAS_GRUBU_5_9, YAS_GRUBU_10_14, YAS_GRUBU_15_19, YAS_GRUBU_20_24, YAS_GRUBU_25_29, YAS_GRUBU_30_34, YAS_GRUBU_35_39, YAS_GRUBU_40_44, YAS_GRUBU_45_49, YAS_GRUBU_50_54, YAS_GRUBU_55_59, YAS_GRUBU_60_64, YAS_GRUBU_65_VE_UZERI";
                if (mahalleId > 0)
                    tableName = "DEMOGRAFIK_MAHALLE";
                else if (ilceId > 0)
                    tableName = "DEMOGRAFIK_ILCE";
                else if (ilId > 0)
                    tableName = "DEMOGRAFIK_IL";
            } else if (infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_ARAC)) {
                columnList = "KAMYON, KAMYONET, MINIBUS, MOTORSIKLET, OTOBUS, OTOMOBIL, OZEL_AMACLI, TRAKTOR";
                if (ilceId > 0)
                    tableName = "DEMOGRAFIK_ILCE";
                else if (ilId > 0)
                    tableName = "DEMOGRAFIK_IL";
            }

            sql = "SELECT " + columnList + " FROM " + tableName + " WHERE ";
            if (mahalleId > 0 && !infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_ARAC))
                sql += "MAHALLE_ID=?";
            else if (ilceId > 0)
                sql += "ILCE_ID=?";
            else if (ilId > 0)
                sql += "IL_ID=?";
            Utils.showText("DEMOGRAPHIC SQL: " + sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (mahalleId > 0 && !infoType.equalsIgnoreCase(DataDemographic.DEMOGRAPHIC_ARAC))
                pstmt.setLong(colno++, mahalleId);
            else if (ilceId > 0)
                pstmt.setLong(colno++, ilceId);
            else if (ilId > 0)
                pstmt.setLong(colno++, ilId);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                DataDemographic dd = DataDemographic.getInstance(rset, infoType);
                return dd;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getDemographicInfo()

    //-----------------------------------------------------------------------------

    public static int addUserImage(String key, String id, int seqNo, int typ, HttpServletRequest request) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart)
            return -1;

        byte[] bytes = null;

        try {
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List items = upload.parseRequest(request);
            Utils.showText("Items: " + items);

            Iterator itr = items.iterator();
            while (itr.hasNext()) {
                FileItem item = (FileItem) itr.next();
                if (!item.isFormField()) {
                    bytes = item.get();
                    break;
                }

            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            return -2;
        }

        try {
            cnn = DbConn.getPooledConnection();
            cnn.setAutoCommit(false);

            sql = "SELECT COUNT(*) FROM LBS_USER_IMAGE WHERE KEY=? AND ID=? AND SEQNO=? AND TYP=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            pstmt.setInt(colno++, seqNo);
            pstmt.setInt(colno++, typ);
            rset = pstmt.executeQuery();
            rset.next();
            int count = rset.getInt(1);

            DbConn.closeDBConnection(pstmt, rset);

            if (count <= 0) {
                sql = "INSERT INTO LBS_USER_IMAGE (KEY,ID,SEQNO,TYP,TIME_STAMP,IMAGE) VALUES (?,?,?,?,SYSDATE,EMPTY_BLOB())";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, id);
                pstmt.setInt(colno++, seqNo);
                pstmt.setInt(colno++, typ);
                pstmt.executeUpdate();
                DbConn.closeDBConnection(pstmt, null);
            }

            sql = "SELECT IMAGE FROM LBS_USER_IMAGE WHERE KEY=? AND ID=? AND SEQNO=? AND TYP=? FOR UPDATE";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            pstmt.setInt(colno++, seqNo);
            pstmt.setInt(colno++, typ);
            rset = pstmt.executeQuery();
            rset.next();
            BLOB blob = DbConn.convToBLOB(rset.getObject(1));
            //      BLOB blob = ((OracleResultSet)rset).getBLOB(1);
            OutputStream out = blob.setBinaryStream(0); //get the output stream from the Blob to insert it

            int pos = 0;
            while (true) {
                int remaining = (bytes.length - pos);
                if (remaining <= 0)
                    break;

                if (remaining < 2048) {
                    out.write(bytes, pos, remaining);
                    pos += remaining;
                } else {
                    out.write(bytes, pos, 2048);
                    pos += 2048;
                }
            } // while()
            
            DbConn.closeStreamConn(null,  out);
            cnn.commit();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -3;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            try {
                cnn.setAutoCommit(true);
            } catch (Exception e) {
                ;
            }
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addUserImage()

    //-----------------------------------------------------------------------------
    
    public static DataMap getMapTheme(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude, int width, int height,String theme,String datasource,int basemap, boolean downloadToLocalFile) {
          if (minLatitude == 0.00 && minLongitude == 0.00 && maxLatitude == 0.00 && maxLongitude == 0.00)
              return null;

          String mvUrl = Utils.getParameter("mapviewer_url") + "/omserver";
          String dataSource = Utils.getParameter("mapviewer_map_datasource");
          int srid = 3857;
          String baseMap = null;
          if (basemap != 0 && basemap != 100) {
              String pname = "mapviewer_basemap_" + (basemap > 100 ? basemap - 100 : basemap);
              baseMap = Utils.getParameter(pname);
              if (baseMap != null && baseMap.length() > 0) {
                  srid = Integer.parseInt(Utils.getParameter(pname + "_srid"));
              }
          }

          Extent extent = null;
          double size = 0.00;
          if (srid == 8307) {
              extent = new Extent(minLatitude, minLongitude, maxLatitude, maxLongitude);
              size = extent.getSize();
          } else {
              double[] minCoors = Utils.transformCoors(minLatitude, minLongitude, srid);
              double[] maxCoors = Utils.transformCoors(maxLatitude, maxLongitude, srid);
              extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
              size = extent.getSize();
          }
          
          int zoomLevel = 0;
          if (srid == 8307)
              zoomLevel = getLevelFromSize_8307(size);
          else if (srid == 2321)
              zoomLevel = getLevelFromSize_2321(size);
          else if (srid == 3857)
              zoomLevel = getLevelFromSize_3857(size);
          else
              return null;

          Utils.showText("ZOOM LEVEL: " + zoomLevel);

          try {
              MapViewer mv = new MapViewer(mvUrl);
              mv.setDataSourceName(datasource);
              mv.setMapTitle(null);
              mv.setAntiAliasing(true);
              mv.setMapRequestSRID(srid);
              mv.setBackgroundTransparent(true);
              if (baseMap != null && baseMap.length() > 0) {
                  mv.addMapTileTheme(baseMap, Utils.getDataSourceFromBaseMapName(dataSource, baseMap), baseMap, (basemap > 100));
                  mv.setSnapToCachedZoomLevel((basemap > 100));
              }
              mv.setImageFormat(MapViewer.FORMAT_PNG8_URL);


              if (theme!=null || theme=="") {
                      String themeName = theme;
                      mv.addPredefinedTheme(datasource, themeName);
              }
              Utils.showText("SET BOX: " + extent);
              mv.setBox(extent.getMinLongitude(), extent.getMinLatitude(), extent.getMaxLongitude(), extent.getMaxLatitude());
              mv.setDeviceSize(new Dimension(width, height));
              mv.run();

              double[] mbr = mv.getMapMBR();
              if (srid == 8307) {
                  extent = new Extent(mbr[1], mbr[0], mbr[3], mbr[2]);
              } else {
                  double[] minCoors = Utils.getTransformedCoors(mbr[1], mbr[0], srid);
                  double[] maxCoors = Utils.getTransformedCoors(mbr[3], mbr[2], srid);
                  extent = new Extent(minCoors[1], minCoors[0], maxCoors[1], maxCoors[0]);
              }
              Utils.showText("RESULT: " + extent);
              String url = mv.getGeneratedMapImageURL();
              if( url != null && !( url.contains("http://") || url.contains("https://")))
                  url = "https:" + url;
              if (url != null && downloadToLocalFile) {
                  String filePath = Utils.getParameter("mapimg_path");
                  String fileName = Utils.createFileName(filePath);
                  Utils.downloadAndSaveFile(url, filePath + "/" + fileName);
                  url = Utils.getParameter("mapurl_prefix") + "/" + fileName;
              }

              return (new DataMap(extent.getCenterY(), extent.getCenterX(), -1, extent, width, height, url, null));
          } catch (Exception ex) {
              Utils.showError("getMapTheme: " + ex.getMessage());
              ex.printStackTrace();
          }
          
          return null;
      } // getMapTheme()
    
    //----------------------------------------------------------------------------

    public static int removeUserImage(String key, String id, int seqNo, int typ) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "DELETE FROM LBS_USER_IMAGE WHERE KEY=? AND ID=? AND SEQNO=? AND TYP=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, id);
            pstmt.setInt(colno++, seqNo);
            pstmt.setInt(colno++, typ);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
            return -3;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // removeUserImage()

    //-----------------------------------------------------------------------------

    public static int addServiceAreaToUserRegion(String key, DataUserRegion dur, double latitude, double longitude, double cost, int networkType) {
        PreparedStatement pstmt = null;
        String sql = null;
        int count = 0;
        int colno = 0;
        String criteria = null;
        
        switch (networkType) {
        case 1:
            criteria = "yaya";
            cost = cost * 60.0 / 5000.0;
            break;
        case 2:
            criteria = "yaya";
            break;
        case 3:
            criteria = "short";
            break;
        case 4:
            criteria = "fast";
            break;
        } // switch()

        if (criteria == null)
            return -2;

        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            if (dur.id != null) {
                sql = "UPDATE LBS_USER_REGION SET REGION_NAME=?,TYP=?,STRING_1=?,STRING_2=?,STRING_3=?,STRING_4=?,STRING_5=?,STRING_6=?,STRING_7=?,STRING_8=?,STRING_9=?,NUMBER_1=?,NUMBER_2=?,NUMBER_3=?,NUMBER_4=?,NUMBER_5=?,NUMBER_6=?,NUMBER_7=?,NUMBER_8=?,NUMBER_9=?,UPDATE_DATE=SYSDATE,XCOOR=0,YCOOR=0,GEOLOC=NULL,GEOMBR=NULL WHERE KEY=? AND REGION_ID=?";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                count = pstmt.executeUpdate();
            }

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);

                if (dur.id == null || dur.id.length() <= 0)
                    dur.id = Utils.getUniqueRegionId();

                sql = "INSERT INTO LBS_USER_REGION (KEY,REGION_ID,REGION_NAME,TYP,STRING_1,STRING_2,STRING_3,STRING_4,STRING_5,STRING_6,STRING_7,STRING_8,STRING_9,NUMBER_1,NUMBER_2,NUMBER_3,NUMBER_4,NUMBER_5,NUMBER_6,NUMBER_7,NUMBER_8,NUMBER_9,CREATE_DATE,UPDATE_DATE,XCOOR,YCOOR,GEOLOC,GEOMBR) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,SYSDATE,NULL,0,0,NULL,NULL)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setString(colno++, dur.id);
                pstmt.setString(colno++, dur.name);
                pstmt.setInt(colno++, dur.type);
                if (dur.string_1 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_1);
                if (dur.string_2 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_2);
                if (dur.string_3 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_3);
                if (dur.string_4 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_4);
                if (dur.string_5 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_5);
                if (dur.string_6 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_6);
                if (dur.string_7 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_7);
                if (dur.string_8 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_8);
                if (dur.string_9 == null)
                    pstmt.setNull(colno++, Types.VARCHAR);
                else
                    pstmt.setString(colno++, dur.string_9);
                pstmt.setDouble(colno++, dur.number_1);
                pstmt.setDouble(colno++, dur.number_2);
                pstmt.setDouble(colno++, dur.number_3);
                pstmt.setDouble(colno++, dur.number_4);
                pstmt.setDouble(colno++, dur.number_5);
                pstmt.setDouble(colno++, dur.number_6);
                pstmt.setDouble(colno++, dur.number_7);
                pstmt.setDouble(colno++, dur.number_8);
                pstmt.setDouble(colno++, dur.number_9);
                pstmt.executeUpdate();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        DataNetwork dn = DataNetwork.getNext(criteria);
        if (dn == null) {
            Utils.logInfo("Property file content criteria error !");
            return -2;
        }

        String networkName = dn.network;
        String host = dn.host;
        int port = dn.port;
        if (host == null || port == 0) {
            Utils.logInfo("Property file content (host, port) error !");
            return -2;
        }

        SrvConnect sc = new SrvConnect(host, port);
        if (!sc.getLineWithErrorCheck()) {
            Utils.logInfo("Could not connect to network server (Host: " + host + ", Port: " + port + ") !");
            return -2;
        }

        try {
            sc.sendLine("INIT LOCATIONBOX");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Bad response from server !");
                return -2;
            }

            sc.sendLine("NETW " + networkName);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Network problem !");
                return -2;
            }

            sc.sendLine("MANEUVER NO");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Maneuver setting problem !");
                return -2;
            }

            sc.sendLine("AREA COOR " + latitude + " " + longitude + " " + cost + " LBS_USER_REGION GEOLOC REGION_ID " + dur.id + " KEY " + key);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Area response problem !");
                return -2;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return -2;
        } finally {
            sc.sendLine("QUIT");
            sc.close();
        }

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_USER_REGION SET XCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.X),YCOOR=(SDO_GEOM.SDO_CENTROID(GEOLOC, 0.5).SDO_POINT.Y),GEOMBR=(SDO_GEOM.SDO_MBR(GEOLOC)), COOR_ACCESS=0 WHERE KEY=? AND REGION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setString(colno++, dur.id);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -2;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // addServiceAreaToUserRegion()

    //-----------------------------------------------------------------------------

    public static DataSocialEvent[] getSocialEventList(String key, long id) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT ID,EVENT_CATEGORY_ID,NAME,DESCRIPTION,LOCATION,START_DATE,END_DATE,TIMEZONE,XCOOR,YCOOR FROM DAKICK_EVENTS WHERE EVENT_CATEGORY_ID IN (SELECT ID FROM DAKICK_EVENT_CATEGORIES) AND TIMEZONE='Istanbul' ";
            if (id > 0)
                sql += "AND ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (id > 0)
                pstmt.setLong(colno++, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataSocialEvent dse = DataSocialEvent.getInstance(rset);
                array.add(dse);
            } // while()
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

        DataSocialEvent[] dses = new DataSocialEvent[array.size()];
        for (int i = 0; i < dses.length; i++)
            dses[i] = (DataSocialEvent) array.get(i);
        return dses;
    } // getCampaignList()

    //-----------------------------------------------------------------------------

    public static DataSocialEvent[] getSocialEventSearch(double latitude, double longitude, int radius, int zone) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM (SELECT INNER_QUERY.*, SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(XCOOR,YCOOR,NULL),NULL,NULL), SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001) DISTANCE";
            sql += " FROM (SELECT ID,EVENT_CATEGORY_ID,NAME,DESCRIPTION,LOCATION,START_DATE,END_DATE,TIMEZONE,XCOOR,YCOOR,((?-XCOOR)*(?-XCOOR)+(?-YCOOR)*(?-YCOOR)) REF_DISTANCE FROM DAKICK_EVENTS";
            sql += " WHERE EVENT_CATEGORY_ID IN (SELECT ID FROM DAKICK_EVENT_CATEGORIES) AND TIMEZONE='Istanbul' ";
            sql += " ORDER BY REF_DISTANCE) INNER_QUERY WHERE REF_DISTANCE < ?)";
            sql += " ORDER BY DISTANCE";
            Utils.showText("EVENT SQL: " + sql);
            Utils.showText("LON/LAT: " + longitude + "," + latitude + ", RADIUS: " + radius + ", ZONE: " + zone);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setDouble(colno++, (radius / 100000.0) * (radius / 100000.0));
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataSocialEvent dse = DataSocialEvent.getInstance(rset);
                array.add(dse);
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

        DataSocialEvent[] dses = new DataSocialEvent[array.size()];
        for (int i = 0; i < dses.length; i++)
            dses[i] = (DataSocialEvent) array.get(i);
        return dses;
    } // getSocialEventSearch()

    //-----------------------------------------------------------------------------

    public static DataTmcHat getTmcHatInfo(String tmcKod, int zoomLevel, boolean hatGeometry, int srid, boolean reverse, boolean encode) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        int inx = 0;
        if (zoomLevel >= 0 && zoomLevel <= 6)
            inx = 4;
        else if (zoomLevel >= 7 && zoomLevel <= 8)
            inx = 3;
        else if (zoomLevel == 9)
            inx = 2;
        else
            inx = 1;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT TH.TMC_KOD,TH.FRC_A,TH.PCT_SPEED_LIMIT,SDO_CS.TRANSFORM(TH.GEOLRS,?) FROM TMC_HAT_" + inx + " TH WHERE TH.TMC_KOD=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setInt(1, srid);
            pstmt.setString(2, tmcKod);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                DataTmcHat dth = DataTmcHat.getInstance(rset, hatGeometry, reverse, encode);
                return dth;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getTmcHatInfo()

    //-----------------------------------------------------------------------------

    public static DataTmcHat[] getTmcHatListWithExtent(Extent ext) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        ArrayList array = new ArrayList();

        int tmcHatCount = 100;
        try {
            tmcHatCount = Integer.parseInt(Utils.getParameter("tmc_hat_count"));
        } catch (Exception e) {
            ;
        }

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT TMC_KOD,FRC_A,PCT_SPEED_LIMIT FROM TMC_HAT_1 WHERE SDO_ANYINTERACT(GEOLOC, SDO_GEOMETRY(2003,8307, NULL, SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?))) = 'TRUE'";
            sql = "SELECT * FROM (" + sql + ") WHERE ROWNUM <= " + tmcHatCount;
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, ext.getMinLongitude());
            pstmt.setDouble(colno++, ext.getMinLatitude());
            pstmt.setDouble(colno++, ext.getMaxLongitude());
            pstmt.setDouble(colno++, ext.getMaxLatitude());
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTmcHat dth = DataTmcHat.getInstance(rset, false, false, false);
                array.add(dth);
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

        DataTmcHat[] dths = new DataTmcHat[array.size()];
        for (int i = 0; i < dths.length; i++)
            dths[i] = (DataTmcHat) array.get(i);
        return dths;
    } // getTmcHatListWithExtent()

    //-----------------------------------------------------------------------------

    public static DataTmcHat[] getTmcHatSearch(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT TMC_KOD,FRC_A,PCT_SPEED_LIMIT FROM TMC_HAT_1 WHERE SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL),'SDO_NUM_RES=1') = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataTmcHat dth = DataTmcHat.getInstance(rset, false, false, false);
                array.add(dth);
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

        DataTmcHat[] dths = new DataTmcHat[array.size()];
        for (int i = 0; i < dths.length; i++)
            dths[i] = (DataTmcHat) array.get(i);
        return dths;
    } // getTmcHatSearch()

    //-----------------------------------------------------------------------------

    public static DataSpatialAnalysis spatialAnalysis(String key, String type, long ilId, String targetLayer, String targetBrand, String targetCategory, String targetWhereClause, String searchLayer, String searchBrand,
                                                      String searchCategory, String searchWhereClause, int searchDistance) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        String sqlTTable = null;
        String sqlTWhere = null;
        String sqlSTable = null;
        String sqlSWhere = null;

        boolean ilUsed = false;
        boolean isPoi = false;

        try {
            cnn = DbConn.getPooledConnection();
            if (targetLayer.equalsIgnoreCase("POI")) {
                ilUsed = true;
                isPoi = true;
                sqlTTable = "POI";
                String sqlTQuery = DataCategory.getCategorySqlQuery(key, targetCategory, targetBrand);
                sqlTWhere = Utils.extractWhereClauseAndAddPrefix(sqlTQuery, "P");
                if (sqlTWhere == null || sqlTWhere.length() <= 0)
                    return new DataSpatialAnalysis("44001", "Generated target WHERE clause from CATEGORY and BRAND is empty.");

                String tWhere = Utils.extractWhereClauseAndAddPrefix(targetWhereClause, "P");
                if (tWhere != null && tWhere.length() > 0)
                    sqlTWhere += " AND " + tWhere;

                if (searchLayer.equalsIgnoreCase("POI")) {
                    sqlSTable = "POI_REGIONS";
                    String sqlSQuery = DataCategory.getCategorySqlQuery(key, searchCategory, searchBrand);
                    sqlSWhere = Utils.extractWhereClauseAndAddPrefix(sqlSQuery, "R");
                    if (sqlSWhere == null || sqlSWhere.length() <= 0)
                        return new DataSpatialAnalysis("44002", "Generated search WHERE clause from CATEGORY and BRAND is empty.");

                    String sWhere = Utils.extractWhereClauseAndAddPrefix(searchWhereClause, "R");
                    if (sWhere != null && sWhere.length() > 0)
                        sqlSWhere += " AND " + sWhere;
                } else if (searchLayer.equalsIgnoreCase("UserRegion")) {
                    sqlSTable = "LBS_USER_REGION";
                    sqlSWhere = "KEY='" + key + "'";
                    if (searchWhereClause != null && searchWhereClause.length() > 0)
                        sqlSWhere = " AND " + searchWhereClause;
                    searchDistance = 0;
                }
            } else if (targetLayer.equalsIgnoreCase("UserPoint")) {
                sqlTTable = "LBS_USER_POINT";
                sqlTWhere = "KEY='" + key + "'";
                if (targetWhereClause != null && targetWhereClause.length() > 0)
                    sqlTWhere = " AND " + targetWhereClause;

                if (searchLayer.equalsIgnoreCase("POI")) {
                    sqlSTable = "POI_REGIONS";
                    String sqlSQuery = DataCategory.getCategorySqlQuery(key, searchCategory, searchBrand);
                    sqlSWhere = Utils.extractWhereClauseAndAddPrefix(sqlSQuery, "R");
                    if (sqlSWhere == null || sqlSWhere.length() <= 0)
                        return new DataSpatialAnalysis("44003", "Generated search WHERE clause from CATEGORY and BRAND is empty.");

                    String sWhere = Utils.extractWhereClauseAndAddPrefix(searchWhereClause, "R");
                    if (sWhere != null && sWhere.length() > 0)
                        sqlSWhere += " AND " + sWhere;
                } else if (searchLayer.equalsIgnoreCase("UserRegion")) {
                    sqlSTable = "LBS_USER_REGION";
                    if (!sqlTTable.equals("LBS_USER_POINT"))
                        sqlSWhere = "KEY='" + key + "'";
                    if (searchWhereClause != null && searchWhereClause.length() > 0)
                        sqlSWhere = " AND " + searchWhereClause;
                    searchDistance = 0;
                }
            }

            String sqlWhere = sqlTWhere;
            if (sqlWhere == null || sqlWhere.length() <= 0)
                sqlWhere = sqlSWhere;
            else {
                if (sqlSWhere != null && sqlSWhere.length() > 0)
                    sqlWhere += " AND " + sqlSWhere;
            }

            String relateType = null;
            if (type.equalsIgnoreCase("INTERACT"))
                relateType = "ANYINTERACT";
            else if (type.equalsIgnoreCase("DISJOINT"))
                relateType = "DISJOINT";
            else
                return new DataSpatialAnalysis("44004", "Given TYPE is unknown.");

            sql = "SELECT  /*+ ordered */ * FROM " + sqlTTable + " P, " + sqlSTable + " R WHERE " + (ilUsed ? "P.IL_ID=? AND R.IL_ID=? AND " : "") + (sqlWhere == null || sqlWhere.length() <= 0 ? "" : sqlWhere + " AND ") +
                "SDO_RELATE(R.GEOLOC" + (searchDistance == 0 ? "" : "_" + searchDistance) + ", P.GEOLOC, 'mask=" + relateType + "') = 'TRUE'";
            Utils.showText("SQL: " + sql);
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            if (ilUsed) {
                pstmt.setLong(colno++, ilId);
                pstmt.setLong(colno++, ilId);
            }
            rset = pstmt.executeQuery();
            while (rset.next()) {
                if (isPoi) {
                    DataPoi dp = DataPoi.getInstance(rset);
                    array.add(dp);
                } else {
                    DataUserPoint dup = DataUserPoint.getInstance(rset);
                    array.add(dup);
                }
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

        DataSpatialAnalysis dsa = new DataSpatialAnalysis();
        if (isPoi) {
            DataPoi[] dps = new DataPoi[array.size()];
            for (int i = 0; i < dps.length; i++)
                dps[i] = (DataPoi) array.get(i);
            dsa.setPois(dps);
        } else {
            DataUserPoint[] dups = new DataUserPoint[array.size()];
            for (int i = 0; i < dups.length; i++)
                dups[i] = (DataUserPoint) array.get(i);
            dsa.setUserPoints(dups);
        }
        return dsa;
    } // spatialAnalysis()

    //-----------------------------------------------------------------------------

    public static DataGlobalGeocode getGlobalGeocode(String res) {
        double latitude = 0.00, longitude = 0.00, score = 0.00, confidence = 0.00;
        String houseNumber = null, roadType = null, streetName = null, city = null, state = null, country = null, postCode = null, formattedAddress = null;
        String countryCode = null, municipality = null, countryCodeISO3 = null ;

        try {
            JSONObject jsonres = new JSONObject(res);
            JSONArray geoResult = Utils.getJSONArrayValueFromJSONObject(jsonres, "results", false);
            if (geoResult != null && geoResult.length() > 0) {
                JSONObject geoRes = geoResult.getJSONObject(0);
                roadType = Utils.getStringValueFromJSONObject(geoRes, "type", true);
                score = Utils.getDoubleValueFromJSONObject(geoRes, "score", true);
                state = Utils.getStringValueFromJSONObject(geoRes, "id", true);

                JSONObject position = Utils.getJSONObjectValueFromJSONObject(geoRes, "position", true);
                if(position != null){
                    longitude = Utils.getDoubleValueFromJSONObject(position, "lon", true);
                    latitude = Utils.getDoubleValueFromJSONObject(position, "lat", true);
                }

                JSONObject address = Utils.getJSONObjectValueFromJSONObject(geoRes, "address", false);
                if(address != null){
                    formattedAddress = Utils.getStringValueFromJSONObject(address, "freeformAddress", false);
                    streetName = Utils.getStringValueFromJSONObject(address, "streetName", true);
                    country = Utils.getStringValueFromJSONObject(address, "country", true);
                    city = Utils.getStringValueFromJSONObject(address, "localName", true);
                    postCode = Utils.getStringValueFromJSONObject(address, "extendedPostalCode", true);
                    if(Utils.isStringDataNull(postCode)){
                      postCode = Utils.getStringValueFromJSONObject(address, "postalCode", true);
                    }
                    
                    municipality = Utils.getStringValueFromJSONObject(address, "municipality", true);
                    countryCode = Utils.getStringValueFromJSONObject(address, "countryCode", true);
                    countryCodeISO3 = Utils.getStringValueFromJSONObject(address, "countryCodeISO3", true);                    
                }
                   
            } else {
                return null;
            }
        } catch (Exception ex) {
            Utils.showError("getGlobalGeocode "+ ex.getMessage());
        }
        
        DataGlobalGeocode dgg = new  DataGlobalGeocode(countryCode, municipality, countryCodeISO3, latitude, longitude, houseNumber, roadType, streetName, city, state, country, postCode, formattedAddress, score, confidence);
        return dgg;
    } 
    
    public static DataReverseGlobalGeocode getReverseGlobalGeocode(String res) {
        double latitude = 0.00, longitude = 0.00;
        String city = null, country = null, postCode = null, streetName = null, formattedAddress = null, municipality = null, countryCode = null, countrySubdivision = null, countrySecondarySubdivision =null, countryCodeISO3 =null;

        try {
            JSONObject jsonres = new JSONObject(res);
            JSONArray geoResult = Utils.getJSONArrayValueFromJSONObject(jsonres, "addresses", false);
            if (geoResult != null && geoResult.length() > 0) {
                JSONObject geoRes = geoResult.getJSONObject(0);

                String position = Utils.getStringValueFromJSONObject(geoRes, "position", false);
                if(position != null){
                    String [] coors = Utils.splitString(position, ",");
                    if(coors!=null && coors.length==2){
                        longitude = Utils.convertStringToDoubleValue("DataReverseGlobalGeocode Split Coors", coors[1]);
                        latitude = Utils.convertStringToDoubleValue("DataReverseGlobalGeocode Split Coors", coors[0]);
                    }
                }

                JSONObject address = Utils.getJSONObjectValueFromJSONObject(geoRes, "address", false);
                if(address != null){
                    countryCode = Utils.getStringValueFromJSONObject(address, "countryCode", true);
                    countrySubdivision = Utils.getStringValueFromJSONObject(address, "countrySubdivision", true);
                    countrySecondarySubdivision = Utils.getStringValueFromJSONObject(address, "countrySecondarySubdivision", true);
                    municipality = Utils.getStringValueFromJSONObject(address, "municipality", true);
                    postCode = Utils.getStringValueFromJSONObject(address, "extendedPostalCode", true);
                    if(Utils.isStringDataNull(postCode)){
                      postCode = Utils.getStringValueFromJSONObject(address, "postalCode", true);
                    }
                    country = Utils.getStringValueFromJSONObject(address, "country", false);
                    countryCodeISO3 = Utils.getStringValueFromJSONObject(address, "countryCodeISO3", true);
                    formattedAddress = Utils.getStringValueFromJSONObject(address, "freeformAddress", true);
                    city = Utils.getStringValueFromJSONObject(address, "localName", true);
                    streetName = Utils.getStringValueFromJSONObject(address, "streetName", true);
                }
                   
            } else {
                return null;
            }
        } catch (Exception ex) {
            Utils.showError("getReverseGlobalGeocode "+ ex.getMessage());
        }

        DataReverseGlobalGeocode drgg = new  DataReverseGlobalGeocode(city, streetName, countryCode, municipality, countryCodeISO3, countrySubdivision, countrySecondarySubdivision,  country, formattedAddress, postCode, latitude, longitude);
        return drgg;
    } 

    public static LbsGlobalGeocode getGlobalGeocodeKeyDetails(Connection cnn, String key) {
        LbsGlobalGeocode gg = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        try {
            sql = "SELECT KEY, TYP, URL, API_KEY, REVERSE_URL FROM LBS_GLOBAL_GEOCODE WHERE KEY = ? ORDER BY TYP";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                gg = LbsGlobalGeocode.getInstance(rset);
            }else{
                sql = "SELECT KEY, TYP, URL, API_KEY, REVERSE_URL FROM LBS_GLOBAL_GEOCODE WHERE KEY = 'DEFAULT'";
                pstmt.clearParameters();
                pstmt.setString(1, key);
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    gg = LbsGlobalGeocode.getInstance(rset);
                    gg.setKey(key);
                }
            }
        } catch (Exception ex) {
            Utils.logInfo("getGlobalGeocodeKeyDetails Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
        }
        return gg;

    }
    public static LbsGlobalRoute getGlobalRouteKeyDetails(Connection cnn, String key) {
        LbsGlobalRoute gr = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        try {
            sql = "SELECT KEY, TYP, URL, API_KEY FROM LBS_GLOBAL_ROUTE WHERE KEY = ? ORDER BY TYP";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                gr = LbsGlobalRoute.getInstance(rset);
            }else{
                sql = "SELECT KEY, TYP, URL, API_KEY, REVERSE_URL FROM LBS_GLOBAL_ROUTE WHERE KEY = 'DEFAULT'";
                pstmt.clearParameters();
                pstmt.setString(1, key);
                rset = pstmt.executeQuery();
                if (rset.next()) {
                    gr = LbsGlobalRoute.getInstance(rset);
                    gr.setKey(key);
                }
            }
        } catch (Exception ex) {
            Utils.logInfo("getGlobalRouteKeyDetails Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
        }
        return gr;

    }

    //-----------------------------------------------------------------------------

    public static DataWeatherReport getWeatherReport(long ilId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        DataWeatherReport dwr = null;
        
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT IL_ID, " + "IL_ADI, " + "C.DESC_TUR AS CURRENT_CONDITION, " + "CURRENT_TEMP AS CURRENT_TEMP , " + "CURRENT_HUMIDITY AS CURRENT_HUMIDITY, " + "CURRENT_PRESSURE AS CURRENT_PRESSURE, " +
                "CURRENT_WINDSPEED AS CURRENT_WINDSPEED, " + "CURRENT_WINDFROM AS CURRENT_WINDFROM, " + "CURRENT_VISIBILITY AS CURRENT_VISIBILITY, " + "TO_CHAR(WEATHER_DATE, 'DD-MM-YYYY') AS DAY, " +
                "TO_CHAR(WEATHER_DATE_2, 'DD-MM-YYYY') AS DAY1, " + "TO_CHAR(WEATHER_DATE_3, 'DD-MM-YYYY') AS DAY2, " + "TO_CHAR(WEATHER_DATE_4, 'DD-MM-YYYY') AS DAY3, " +
                "TO_CHAR(WEATHER_DATE_5, 'DD-MM-YYYY') AS DAY4, " + "C1.DESC_TUR AS FORECAST, " + "FORECAST_TEMP_LOW, " + "FORECAST_TEMP_HIGH, " + "C2.DESC_TUR AS DAYFORECAST_1, " + "FORECAST_TEMP_LOW_2, " +
                "FORECAST_TEMP_HIGH_2, " + "C3.DESC_TUR AS DAYFORECAST_2, " + "FORECAST_TEMP_LOW_2, " + "FORECAST_TEMP_HIGH_2, " + "C4.DESC_TUR AS DAYFORECAST_3, " + "FORECAST_TEMP_LOW_3, " + "FORECAST_TEMP_HIGH_3, " +
                "C5.DESC_TUR AS DAYFORECAST_4, " + "FORECAST_TEMP_LOW_4, " + "FORECAST_TEMP_HIGH_4, " + "FORECAST_TEMP_LOW_5, " + "FORECAST_TEMP_HIGH_5 " + "FROM WEATHER_REPORT W " +
                "LEFT JOIN CONDITIONS C ON (C.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_CURRENT) " + "LEFT JOIN CONDITIONS C1 ON (C1.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_FORECAST) " +
                "LEFT JOIN CONDITIONS C2 ON (C2.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_FORECAST_2) " + "LEFT JOIN CONDITIONS C3 ON (C3.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_FORECAST_3) " +
                "LEFT JOIN CONDITIONS C4 ON (C4.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_FORECAST_4) " + "LEFT JOIN CONDITIONS C5 ON (C5.CONDITIONS_CONDITION_CODE = W.CONDITION_CODE_FORECAST_5) " +
                "WHERE IL_ID = ? " + "ORDER BY IL_ID ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, ilId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                dwr = DataWeatherReport.getInstance(rset);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dwr;
    } // getWeatherReport()

    //-----------------------------------------------------------------------------

    public static int setPoiAttribute(String key, DataPoiAttribute dpa) {
        PreparedStatement pstmt = null;
        Connection cnn = null;

        String sql = null;
        String columnName = null;

        int colno = 0;
        int count = 0;

        if (dpa.type == 1)
            columnName = "NUMBER_VALUE";
        if (dpa.type == 2)
            columnName = "STRING_VALUE";
        if (dpa.type == 3)
            columnName = "DATE_VALUE";

        try {
            cnn = DbConn.getPooledConnection();
            if (dpa.type == 3)
                sql = "UPDATE LBS_POI_ATTRIBUTE SET " + columnName + "=" + (dpa.value.equalsIgnoreCase("SYSDATE") ? "SYSDATE " : " TO_DATE(?,'YYYYMMDDHH24MISS')") +
                    ", UPDATE_DATE=SYSDATE WHERE KEY=? AND POI_ID=? AND ATTRIBUTE=? ";
            else
                sql = "UPDATE LBS_POI_ATTRIBUTE SET " + columnName + "=?, UPDATE_DATE=SYSDATE WHERE KEY=? AND POI_ID=? AND ATTRIBUTE=? ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            if (!dpa.value.equalsIgnoreCase("SYSDATE"))
                pstmt.setString(colno++, dpa.value);
            pstmt.setString(colno++, key);
            pstmt.setLong(colno++, dpa.poiId);
            pstmt.setString(colno++, dpa.attribute);
            count = pstmt.executeUpdate();

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);
                if (dpa.type == 3)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?," + (dpa.value.equalsIgnoreCase("SYSDATE") ? "SYSDATE" : " TO_DATE(?,'YYYYMMDDHH24MISS'))") +
                        ")";
                else
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,?)";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setLong(colno++, dpa.poiId);
                pstmt.setString(colno++, dpa.attribute);
                pstmt.setInt(colno++, dpa.type);
                if (!dpa.value.equalsIgnoreCase("SYSDATE"))
                    pstmt.setString(colno++, dpa.value);
                pstmt.execute();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -1;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // setPoiAttribute()

    //-----------------------------------------------------------------------------

    public static int removePoiAttribute(String key, DataPoiAttribute dpa) {
        PreparedStatement pstmt = null;
        Connection cnn = null;
        String sql = null;
        String columnName = null;

        int colno = 0;
        int count = 0;

        if (dpa.type == 1)
            columnName = "NUMBER_VALUE";
        if (dpa.type == 2)
            columnName = "STRING_VALUE";
        if (dpa.type == 3)
            columnName = "DATE_VALUE";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "DELETE FROM LBS_POI_ATTRIBUTE WHERE KEY=? AND POI_ID=? AND ATTRIBUTE=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setLong(colno++, dpa.poiId);
            pstmt.setString(colno++, dpa.attribute);
            count = pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -1;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        if (count == 0)
            return 1;

        return 0;
    } // removePoiAttribute()

    //-----------------------------------------------------------------------------

    public static int incrementPoiAttribute(String key, DataPoiAttribute dpa) {
        PreparedStatement pstmt = null;
        Connection cnn = null;
        
        String sql = null;
        String columnName = null;
        int colno = 0;
        int count = 0;

        if (dpa.type == 1)
            columnName = "NUMBER_VALUE";
        if (dpa.type == 2)
            columnName = "STRING_VALUE";
        if (dpa.type == 3)
            columnName = "DATE_VALUE";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_POI_ATTRIBUTE SET " + columnName + "= " + columnName + " + ?, UPDATE_DATE=SYSDATE WHERE KEY=? AND POI_ID=? AND ATTRIBUTE=? ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, dpa.value);
            pstmt.setString(colno++, key);
            pstmt.setLong(colno++, dpa.poiId);
            pstmt.setString(colno++, dpa.attribute);
            count = pstmt.executeUpdate();

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);
                if (dpa.type == 1)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,0 + ?)";
                if (dpa.type == 2)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,?)";
                if (dpa.type == 3)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,TO_DATE(SYSDATE + ?,'YYYYMMDDHH24MISS'))";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setLong(colno++, dpa.poiId);
                pstmt.setString(colno++, dpa.attribute);
                pstmt.setInt(colno++, dpa.type);
                pstmt.setString(colno++, dpa.value);
                pstmt.execute();
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -1;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // incrementPoiAttribute()

    //-----------------------------------------------------------------------------

    public static int decrementPoiAttribute(String key, DataPoiAttribute dpa) {
        PreparedStatement pstmt = null;
        Connection cnn = null;
        
        String sql = null;
        String columnName = null;
        int colno = 0;
        int count = 0;

        if (dpa.type == 1)
            columnName = "NUMBER_VALUE";
        if (dpa.type == 2)
            columnName = "STRING_VALUE";
        if (dpa.type == 3)
            columnName = "DATE_VALUE";

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_POI_ATTRIBUTE SET " + columnName + "= " + columnName + " - ?, UPDATE_DATE=SYSDATE WHERE KEY=? AND POI_ID=? AND ATTRIBUTE=? ";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, dpa.value);
            pstmt.setString(colno++, key);
            pstmt.setLong(colno++, dpa.poiId);
            pstmt.setString(colno++, dpa.attribute);
            count = pstmt.executeUpdate();

            if (count <= 0) {
                DbConn.closeDBConnection(pstmt, null);
                if (dpa.type == 1)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,0 - ?)";
                if (dpa.type == 2)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,?)";
                if (dpa.type == 3)
                    sql = "INSERT INTO LBS_POI_ATTRIBUTE (KEY,POI_ID,ATTRIBUTE,DATA_TYPE," + columnName + ") VALUES (?,?,?,?,TO_DATE(SYSDATE - ?,'YYYYMMDDHH24MISS'))";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setLong(colno++, dpa.poiId);
                pstmt.setString(colno++, dpa.attribute);
                pstmt.setInt(colno++, dpa.type);
                pstmt.setString(colno++, dpa.value);
                pstmt.execute();
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return -1;
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // decrementPoiAttribute()

    //-----------------------------------------------------------------------------

    public static DataClusterPoint getClusterPoints(DataPoint[] dps, int clusterNum) {

        List<Clusterable> points = new ArrayList<Clusterable>();
        DataClusterPoint dcp = new DataClusterPoint();
        Clusterable point = null;

        try {
            for (int i = 0; i < dps.length; i++) {
                point = new Point((float) dps[i].getLatitude(), (float) dps[i].getLongitude(), dps[i].getName());
                points.add(point);
            }

            KClusterer clusterer = new KMeansClusterer();
            dcp.setClusters(clusterer.cluster(points, clusterNum));


        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return dcp;
    } // getClusterPoints()

    //-----------------------------------------------------------------------------

    public static DataPhonebook[] getGetPhoneAddressScore(String key, String serviceKey, long id, String firstName, String lastName, String address) {
        DataGeocode dg = null;
        DataGeocode dgService = null;

        JSONArray resultsArray = null;

        String gsm = null;
        String customer = null;
        String[] operator = { "TELEKOM", "AVEA", "VODAFONE", "TURKCELL" };

        DataPhonebook[] dpbl = null;
        ArrayList array = new ArrayList();

        try {
            int res = GeocodeOperations.checkGeocodeAddress(address);
            if (res != 0) {
                return null;
            }

            dg = GeocodeOperations.getGeocode(address, 0.0, 0.0, 0);
            if (dg == null) {
                return null;
            }

            long cityCode = dg.getDa().getIlId();
            String district = dg.getDa().getAdrIlceAdi();
            for (int i = 0; operator.length > i; i++) {
                if (cityCode != 0) {
                    String resultStr = getGlobalService(firstName, lastName, cityCode, district, operator[i], null, serviceKey);
                    JSONObject jsonres = new JSONObject(resultStr);
                    int status = jsonres.getInt("res");
                    if (status == 1) {
                        resultsArray = jsonres.getJSONArray("data");
                        if ((resultsArray == null || resultsArray.length() <= 0) && (district != null && district.length() > 0)) {
                            resultStr = getGlobalService(firstName, lastName, cityCode, "", operator[i], null, serviceKey);
                            jsonres = new JSONObject(resultStr);
                            resultsArray = jsonres.getJSONArray("data");
                        }

                        for (int j = 0; j < resultsArray.length(); j++) {
                            try {
                                JSONObject subs = resultsArray.getJSONObject(j);
                                customer = subs.getString("customer");
                                gsm = subs.getString("gsmno");
                                address = subs.getString("address");
                                address = address.replaceAll("\"", "");
                                address = address.replaceAll("\\*", "");

                                // globalbilgi servisinden donen adres formati geocode icin daha uygun hale getiriliyor.
                                // orn: istanbul umraniye serifali mah. -> serifali mah. umraniye istanbul
                                String arr[] = address.split(" ", 3);
                                if (arr.length >= 2) {
                                    String firstWord = arr[0];
                                    String secondWord = arr[1];
                                    address = address.replaceFirst(firstWord + " " + secondWord, "");
                                    address += " " + secondWord + " " + firstWord;
                                }
                                address = address.trim();

                                cityCode = subs.getInt("cityid");
                                dgService = GeocodeOperations.getGeocode(address, 0.0, 0.0, 0);
                                if (dgService != null && dg != null) {
                                    int score = decidePhoneScore(dg.getDa(), dgService.getDa());
                                    array.add(new DataPhonebook(id, firstName, lastName, customer, gsm, operator[i], cityCode, address, dgService.getDa().getAdrIlAdi(), dgService.getDa().getAdrIlceAdi(), score));
                                }
                            } catch (Exception e) {
                                Utils.showError("Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            Utils.showError("Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        dpbl = new DataPhonebook[array.size()];

        for (int i = 0; i < array.size(); i++) {
            dpbl[i] = (DataPhonebook) array.get(i);
        }

        Arrays.sort(dpbl, Collections.reverseOrder());

        getPhoneAddressScoreData(key, serviceKey, dpbl);

        return dpbl;
    } // getGetPhoneAddressScore()

    //-----------------------------------------------------------------------------

    private static int decidePhoneScore(DataAddress daFromClient, DataAddress daFromService) {
        int score = 0;
        if (daFromClient != null && daFromService != null) {
            if (daFromClient.getIlId() != 0 && daFromClient.getIlId() == daFromService.getIlId())
                score = 100;
            if (daFromClient.getIlceId() != 0 && daFromClient.getIlceId() == daFromService.getIlceId())
                score = 200;
            if (daFromClient.getMahalleId() != 0 && daFromClient.getMahalleId() == daFromService.getMahalleId())
                score = 300;
            if (daFromClient.getKoyId() != 0 && daFromClient.getKoyId() == daFromService.getKoyId())
                score = 400;
            if (daFromClient.getSokakId() != 0 && daFromClient.getSokakId() == daFromService.getSokakId() || daFromClient.getCaddeId() != 0 && daFromClient.getCaddeId() == daFromService.getCaddeId())
                score = 700;
            if (daFromClient.getSiteId() != 0 && daFromClient.getSiteId() == daFromService.getSiteId())
                score = 850;
            if (daFromClient.getKapiId() != 0 && daFromClient.getKapiId() == daFromService.getKapiId())
                score = 1000;
        }
        return score;
    }

    //-----------------------------------------------------------------------------

    public static String getGlobalService(String name, String surname, long cityCode, String district, String operator, String gsm, String key) {
        xmlParserPosStart = 0;
        boolean gsmSearch = false;
        String resultStr = "";

        name = Utils.convUtf8ToEnglish(Utils.toUpperCase(name));
        surname = Utils.convUtf8ToEnglish(Utils.toUpperCase(surname));
        district = Utils.convUtf8ToEnglish(Utils.toUpperCase(district));

        if (gsm != null)
            gsmSearch = true;

        String endPoint = "http://11850.global-bilgi.com.tr/PhoneBookService.svc";

        String req = "";
        req += "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n";
        req += "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\r\n";
        req += "  <soap:Body>\r\n";
        req += "    <Search xmlns=\"http://11850.global-bilgi.com.tr\" xmlns:prm=\"http://schemas.datacontract.org/2004/07/CCA.Rehberlik.PhoneBookWs\">\r\n";
        req += "      <searchParams>\r\n";
        req += "        <prm:cityId>" + cityCode + "</prm:cityId>\r\n";
        req += "        <prm:district>" + district + "</prm:district>\r\n";
        //req += "        <prm:district xsi:nil=\"true\" />\r\n";
        if (gsmSearch)
            req += "        <prm:gsm>" + gsm + "</prm:gsm>\r\n";
        else
            req += "        <prm:gsm xsi:nil=\"true\" />\r\n";
        req += "        <prm:gsmOperator>" + operator + "</prm:gsmOperator>\r\n";
        if (gsmSearch)
            req += "        <prm:name xsi:nil=\"true\" />\r\n";
        else
            req += "        <prm:name>" + name + "</prm:name>\r\n";
        req += "        <prm:searchOption>Individual</prm:searchOption>\r\n";
        if (gsmSearch)
            req += "        <prm:searchType>SearchByGsm</prm:searchType>\r\n";
        else
            req += "        <prm:searchType>SearchByName</prm:searchType>\r\n";
        if (gsmSearch)
            req += "        <prm:surName xsi:nil=\"true\" />\r\n";
        else
            req += "        <prm:surName>" + surname + "</prm:surName>\r\n";
        req += "        <prm:userKey>" + key + "</prm:userKey>\r\n";
        req += "      </searchParams>\r\n";
        req += "    </Search>\r\n";
        req += "  </soap:Body>\r\n";
        req += "</soap:Envelope>\r\n";
        String soapAction = "http://11850.global-bilgi.com.tr/IPhoneBookService/Search";
        String res = sendRequest(endPoint, soapAction, req);
        if (res == null)
            resultStr += "{ res: 0, data: [] }";
        else {
            int count = 0;
            resultStr += "{ res: 1, data: [";

            while (true) {
                String data = getNextNodeData(res, "a:PhoneBookDetail");
                if (data == null)
                    break;

                String address = Utils.convUtf8ToTurkish(getNodeData(data, "a:address")).replace("'", "");
                String cityId = getNodeData(data, "a:cityId");
                String customer = Utils.convUtf8ToTurkish(getNodeData(data, "a:customer"));
                String gsmNo = getNodeData(data, "a:gsm");
                String gsmOperator = getNodeData(data, "a:gsmOperator");

                if (count > 0)
                    resultStr += ",";
                resultStr += "  { address: '" + address + "', cityid: '" + cityId + "', customer: '" + customer + "', gsmno: '" + gsmNo + "', gsmoperator: '" + gsmOperator + "' }";
                count++;
            } // while()

            resultStr += "";
            resultStr += "] }";
        }

        return resultStr;

    } // getGlobalService()

    //-----------------------------------------------------------------------------

    private static String getNextNodeData(String data, String nodeName) {
        int posBeg = data.indexOf("<" + nodeName, xmlParserPosStart);
        if (posBeg < 0)
            return null;

        posBeg += (1 + nodeName.length() + 1);
        int posEnd = data.indexOf("</" + nodeName, posBeg);
        if (posEnd < 0)
            return null;

        xmlParserPosStart = posEnd + nodeName.length() + 2;
        return (data.substring(posBeg, posEnd));

    } // getNextNodeData()


    //-----------------------------------------------------------------------------

    private static String getNodeData(String data, String nodeName) {
        int posBeg = data.indexOf("<" + nodeName + ">");
        if (posBeg < 0)
            return "0";

        posBeg += (1 + nodeName.length() + 1);
        int posEnd = data.indexOf("</" + nodeName + ">", posBeg);
        if (posEnd < 0)
            return "0";

        return (data.substring(posBeg, posEnd));

    } // getNodeData()

    //-----------------------------------------------------------------------------

    private static String sendRequest(String endPoint, String soapAction, String query) {
        URL url = null;
        HttpURLConnection http = null;
        OutputStream out = null;
        InputStream inp = null;

        Utils.logInfo("ENDPOINT: " + endPoint);
        Utils.logInfo("SOAPACTION: " + soapAction);

        try {
            url = new URL(endPoint);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("POST");
            http.setRequestProperty("Content-type", "text/xml");
            http.setRequestProperty("SOAPAction", "\"" + soapAction + "\"");
            http.setReadTimeout(30000);
            http.setConnectTimeout(30000);
            http.setDoOutput(true);
            http.setDoInput(true);
            http.connect();

            out = http.getOutputStream();
            out.write(query.getBytes());

            inp = http.getInputStream();
            int code = http.getResponseCode();
            if (code != 200) {
                Utils.logInfo("Response Code: " + code);
                return null;
            }

            String result = "";
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                result += new String(b, 0, length, "UTF-8");
            } // while()
            result = result.trim();
            return result;
        } catch (Exception e) {
            Utils.showError("Exception: " + e.getMessage());
            e.printStackTrace();
        } finally {
            DbConn.closeStreamConn(inp, out);
            DbConn.closeHttpConn(http, null, null, null);
        }

        return null;
    } // sendRequest()

    //-----------------------------------------------------------------------------

    public static void getPhoneAddressScoreData(String key, String serviceKey, DataPhonebook[] dpbl) {

        String sql = null;
        PreparedStatement pstmt = null;
        Connection cnn = null;
        try {
            cnn = DbConn.getPooledConnection();
            for (int i = 0; dpbl.length > i; i++) {
    
                sql = "INSERT INTO LBS_PHONE_ADDRESS_SCORE_LOG(KEY, SERVICE_KEY, ROWNO, ID, CUSTOMER, TELNO, OPERATOR, CITY_CODE, ADDRESS, TIME_STAMP) ";
                sql += "VALUES (?,?,SEQ_PHONE_ADD_SCR_LOG_ROWNO.NEXTVAL, ? , ? , ? , ?, ?, ?, SYSDATE)";
    
                try {
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.setQueryTimeout(360);
                    pstmt.setString(1, key);
                    pstmt.setString(2, serviceKey);
                    pstmt.setLong(3, dpbl[i].getId());
                    pstmt.setString(4, dpbl[i].getCustomer());
                    pstmt.setString(5, dpbl[i].getTelNo());
                    pstmt.setString(6, dpbl[i].getOperator());
                    pstmt.setLong(7, dpbl[i].getCityCode());
                    pstmt.setString(8, dpbl[i].getAddress());
                    pstmt.execute();
                } catch (SQLException e) {
                    Utils.showError("SQL: " + sql);
                    Utils.showError("Exception: " + e.getMessage());
                    e.printStackTrace();
                } finally {
                    try {
                       DbConn.closeDBConnection(pstmt, null);
                    } catch (Exception e) {
                        Utils.showError("SQL: " + sql);
                        Utils.showError("Exception: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
         }
    } // getPhoneAddressScoreData()

    //-----------------------------------------------------------------------------


    public static DataPoint getNearestPointOnYol(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        DataPoint dp = null;
        double[] minData = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SDO_NN_DISTANCE(1),GEOLOC, YOL_ID, YOL_ADI FROM YOL WHERE SDO_NN(GEOLOC, SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL),1) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                double dist = rset.getDouble(1);
                if (dist <= 2000) {
                    STRUCT obj = DbConn.convToSTRUCT(rset.getObject(2));
                    //STRUCT obj = (STRUCT) rset.getObject(2); //changed with above line ekn
                    if (obj != null) {
                        JGeometry geo = JGeometry.load(obj);
                        Object[] objs = geo.getOrdinatesOfElements();


                        for (int i = 0; i < objs.length; i++) {
                            Vertex[] vertices = makeArray((double[]) objs[i]);
                            double[] data = getCorrectedCoordinate(vertices, latitude, longitude);
                            if (minData == null || data[3] < minData[3])
                                minData = data;
                        } // for()
                        //return minData;
                    }
                }

                long yolId = rset.getLong(3);
                String yolAdi = rset.getString(4);

                DecimalFormat df = new DecimalFormat("#.#####");

                dp = new DataPoint();
                if (minData != null) {

                    dp.setDistance(dist);
                    dp.setName(yolAdi);
                    dp.setId(Long.toString(yolId));
                    dp.setLatitude(Double.parseDouble(df.format(minData[0])));
                    dp.setLongitude(Double.parseDouble(df.format(minData[1])));

                }
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dp;
    } // getCorrectedCoordinateOnNearestLine()

    //-----------------------------------------------------------------------------


    public static boolean getKeyControl(String key) {
        Connection cnn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT EXPIRE_DATE, REQUEST_LIMIT, REQUEST_COUNT, ACTIVE  FROM LBS_KEYS WHERE KEY = ? AND ACTIVE=1 AND EXPIRE_DATE > SYSDATE AND REQUEST_COUNT < REQUEST_LIMIT";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                return true;
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return false;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return false;

    } // getKeyStatus()
    
   
    //-----------------------------------------------------------------------------

    public static DataKeyStatus getKeyDetail(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        DataKeyStatus dataKeyStatus = null;
        
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT KEY,EXPIRE_DATE, REQUEST_LIMIT, REQUEST_COUNT, ACTIVE FROM LBS_KEYS WHERE KEY = ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                dataKeyStatus=new DataKeyStatus();
                dataKeyStatus.setKey(rset.getString("KEY"));
                dataKeyStatus.setExpireDate(rset.getDate("EXPIRE_DATE"));
                dataKeyStatus.setRequestLimit(rset.getInt("REQUEST_LIMIT"));
                dataKeyStatus.setRequestCount(rset.getInt("REQUEST_COUNT"));
                dataKeyStatus.setStatus(rset.getInt("ACTIVE"));
             }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
            return dataKeyStatus;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dataKeyStatus;

    } // getKeyStatus()
    
    //-----------------------------------------------------------------------------

    public static DataKeyStatus getKeyStatus(String key) {
        DataKeyStatus dks = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT EXPIRE_DATE, REQUEST_LIMIT, REQUEST_COUNT, ACTIVE  FROM LBS_KEYS WHERE KEY = ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                Date exp_date = rset.getDate(1);
                int req_lmt = rset.getInt(2);
                int req_cnt = rset.getInt(3);
                int stat = rset.getInt(4);

                dks = new DataKeyStatus();

                dks.setExpireDate(exp_date);
                dks.setRequestLimit(req_lmt);
                dks.setRequestCount(req_cnt);
                dks.setStatus(stat);
                dks.setKey(key);

            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dks;

    } // getKeyStatus()


    //-----------------------------------------------------------------------------

    public static DataGeocode getBagimsizBirim(long adresKodu) {
        DataAddress da = new DataAddress();
        DataGeocode dg = null;
        DataKapi dk = null;

        String address = null;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql ="SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, YOL_ID, YOL_ADI, ZONE, NULL, UAVT_KAPI_ID, ";
            sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID FROM KAPI ";
            sql += "WHERE UAVT_KAPI_ID = (SELECT UAVT_KAPI_ID FROM KAPI_ICKAPI WHERE UAVT_ICKAPI_ID = ?)  ORDER BY NLSSORT(KAPI_NO, 'NLS_SORT=XTURKISH'), NLSSORT(KAPI_ADI, 'NLS_SORT=XTURKISH')";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setLong(1, adresKodu);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                // setting dk object
                dk = DataKapi.getInstance(rset, false, false);
                da.setIlId(dk.getIlId());
                da.setIlAdi(dk.getIlAdi());
                da.setAdrIlAdi(dk.getIlAdi());
                da.setIlceId(dk.getIlceId());
                da.setIlceAdi(dk.getIlceAdi());
                da.setAdrIlceAdi(dk.getIlceAdi());
                da.setMahalleId(dk.getMahalleId());
                da.setMahalleAdi(dk.getMahalleAdi());
                da.setAdrMahalleAdi(dk.getMahalleAdi());
                da.setKoyId(dk.getKoyId());
                da.setKoyAdi(da.getKoyAdi());
                da.setAdrKoyAdi(da.getKoyAdi());
                da.setCaddeId(dk.getYolId());
                da.setCaddeAdi(dk.getYolAdi());
                da.setAdrCaddeAdi(dk.getYolAdi());
                da.setZone(dk.getZone());
                da.setKapiId(dk.getId());
                da.setKapiNo(dk.getNo());
                da.setAdrKapiNo(dk.getNo());
                da.setKapiAdi(dk.getName());
                da.setAdrKapiAdi(dk.getName());
                da.setKapiUavt(dk.getAdresKodu());
                da.setCaddeUavt(dk.getYolAdresKodu());
                da.setKoyUavt(dk.getKoyAdresKodu());
                da.setMahalleUavt(dk.getMahalleAdresKodu());
                da.setIlceUavt(dk.getIlceAdresKodu());
                da.setIlUavt(dk.getIlAdresKodu());
                da.setPostaKodu(dk.getPostaKodu());
                da.setAdrPostaKodu(dk.getPostaKodu());
                try {
                    DbConn.closeDBConnection(pstmt, rset);
                    sql = "SELECT ICKAPI_NO FROM KAPI_ICKAPI WHERE UAVT_ICKAPI_ID = ?";
                    pstmt = cnn.prepareStatement(sql);
                    pstmt.clearParameters();
                    pstmt.setLong(1, adresKodu);
                    rset = pstmt.executeQuery();

                    if (rset.next()) {
                        da.setDaireNo(rset.getString(1));
                        da.setAdrDaireNo(rset.getString(1));
                    }
                } catch (Exception ex) {
                    Utils.logInfo("Exception: " + ex.getMessage());
                    ex.printStackTrace();
                } finally {
                    DbConn.closeDBConnection(pstmt, rset);
                    DbConn.closeConnection(cnn);
                }

                address = ((da.getMahalleAdi() == null || da.getMahalleAdi().equals("")) ? "" : da.getMahalleAdi() + " MAH. ");

                address += ((da.getKoyAdi() == null || da.getKoyAdi().equals("")) ? "" : da.getKoyAdi() + " KÃ–YÃœ");

                address += (dk.getYolAdi() == null ? "" : dk.getYolAdi()) + (dk.getName() == null ? "" : " " + dk.getName()) + (dk.getNo() == null ? "" : " NO: " + dk.getNo());
                address = address.trim();


                address += (da.getDaireNo() == null ? "" : " D: " + da.getDaireNo());
                address = address.trim();

                address += " " + (da.getPostaKodu() == null || da.getPostaKodu().length() < 3 ? "" : " " + da.getPostaKodu());
                address += (da.getIlceAdi() == null ? "" : " " + da.getIlceAdi()) + (da.getIlAdi() == null ? "" : " " + da.getIlAdi());
                address = address.trim();

                address = Utils.toUpperCase(address);

                if (dk.getLatitude() > 0 && dk.getLongitude() > 0) {
                    da.setXCoor(dk.getLongitude());
                    da.setYCoor(dk.getLatitude());
                    da.setGeolocSeviyesi(6);
                    da.setAdrAdres(address);
                    da.setAdrResAdres(address);
                } else {
                    address = DataAddress.preprocess(address);
                    DataAddress gc = DataAddress.parseAddress(address);
                    da.setGeolocSeviyesi(gc.getGeolocSeviyesi());
                    da.setXCoor(gc.getXCoor());
                    da.setYCoor(gc.getYCoor());
                    da.setAdrAdres(gc.getAdrAdres());
                    da.setAdrResAdres(gc.getAdrAdres());
                }
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        //da.setKapiUavt(adresKodu);
        da.setDaireUavt(adresKodu);

        dg = new DataGeocode(da);
        dg.setGeolocLevel(da.getGeolocSeviyesi());
        dg.setAddress(address);
        dg.setLatitude(da.getYCoor());
        dg.setLongitude(da.getXCoor());
        dg.setZone(da.getZone());
        return dg;
    } // getBagimsizBirim()
    //-----------------------------------------------------------------------------

    public static DataBagimsizBirim [] getBagimsizBirimList(long kapiId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql ="SELECT UAVT_KAPI_ID, UAVT_ICKAPI_ID, MAHALLE_ID,ICKAPI_NO,ICKAPI_TURU FROM KAPI_ICKAPI WHERE UAVT_KAPI_ID IN(SELECT UAVT_KAPI_ID FROM KAPI WHERE KAPI_ID= ? )";
            sql += "ORDER BY NLSSORT(ICKAPI_NO, 'NLS_SORT=XTURKISH')";
         
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setLong(1, kapiId);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                DataBagimsizBirim dbb = DataBagimsizBirim.getInstance(rset);   
                array.add(dbb);
            }
               
          
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }
        if (array.size() <= 0)
              return null;

       DataBagimsizBirim[] dbbk = new DataBagimsizBirim[array.size()];
       for (int i = 0; i < dbbk.length; i++)
           dbbk[i] = (DataBagimsizBirim) array.get(i);
        return dbbk;
    } // getBagimsizBirim()

    //-----------------------------------------------------------------------------
    
   public static DataAdresCadde getCaddeAdresKodu(long caddeAdresKodu) {
        DataAdresCadde dac = new DataAdresCadde();

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT XCOOR, YCOOR, IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, YOL_ID, YOL_ADI, ZONE, ";
            sql += "UAVT_CSBM_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID FROM KAPI ";
            sql += "WHERE UAVT_CSBM_ID =? AND YSK=0";

            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setLong(1, caddeAdresKodu);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                // setting dk object
                dac.setIlId(rset.getLong("IL_ID"));
                dac.setIlUavt(rset.getLong("UAVT_IL_ID"));
                dac.setIlAdi(rset.getString("IL_ADI")!=null?rset.getString("IL_ADI"):"");
                
                dac.setIlceId(rset.getLong("ILCE_ID"));
                dac.setIlceUavt(rset.getLong("UAVT_ILCE_ID"));
                dac.setIlceAdi(rset.getString("ILCE_ADI")!=null?rset.getString("ILCE_ADI"):"");
                
                dac.setMahalleId(rset.getLong("MAHALLE_ID"));
                dac.setMahalleUavt(rset.getLong("UAVT_MAHALLE_ID"));
                dac.setMahalleAdi(rset.getString("MAHALLE_ADI")!=null?rset.getString("MAHALLE_ADI"):"");
                
                
                dac.setCaddeId(rset.getLong("YOL_ID"));
                dac.setCaddeUavt(rset.getLong("UAVT_CSBM_ID"));
                dac.setCaddeAdi(rset.getString("YOL_ADI")!=null?rset.getString("YOL_ADI"):"");
                
                dac.setZone(rset.getInt("ZONE"));
                dac.setLongitude(rset.getDouble("XCOOR"));
                dac.setLatitude(rset.getDouble("YCOOR"));

            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dac;
    } // getCaddeAdresKodu()
    
    //-----------------------------------------------------------------------------
    public static DataAdresMahalle getMahalleAdresKodu(long mahalleAdresKodu) {
         DataAdresMahalle dam = new DataAdresMahalle();

         PreparedStatement pstmt = null;
         ResultSet rset = null;
         String sql = null;
         Connection cnn = null;

         try {
             cnn = DbConn.getPooledConnection();
             sql = "SELECT XCOOR, YCOOR, IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, ZONE, ";
             sql += " UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID FROM KAPI ";
             sql += "WHERE UAVT_MAHALLE_ID =? AND YSK=0";

             pstmt = cnn.prepareStatement(sql);
             pstmt.clearParameters();
             pstmt.setLong(1, mahalleAdresKodu);
             rset = pstmt.executeQuery();

             if (rset.next()) {
                 // setting dk object
                 dam.setIlId(rset.getLong("IL_ID"));
                 dam.setIlUavt(rset.getLong("UAVT_IL_ID"));
                 dam.setIlAdi(rset.getString("IL_ADI")!=null?rset.getString("IL_ADI"):"");
                 
                 dam.setIlceId(rset.getLong("ILCE_ID"));
                 dam.setIlceUavt(rset.getLong("UAVT_ILCE_ID"));
                 dam.setIlceAdi(rset.getString("ILCE_ADI")!=null?rset.getString("ILCE_ADI"):"");
                 
                 dam.setMahalleId(rset.getLong("MAHALLE_ID"));
                 dam.setMahalleUavt(rset.getLong("UAVT_MAHALLE_ID"));
                 dam.setMahalleAdi(rset.getString("MAHALLE_ADI")!=null?rset.getString("MAHALLE_ADI"):"");
                                 
                 dam.setZone(rset.getInt("ZONE"));
                 dam.setLongitude(rset.getDouble("XCOOR"));
                 dam.setLatitude(rset.getDouble("YCOOR"));

                
             }
         } catch (Exception ex) {
             Utils.logInfo("Exception: " + ex.getMessage());
             ex.printStackTrace();
         } finally {
             DbConn.closeDBConnection(pstmt, rset);
             DbConn.closeConnection(cnn);
         }

         return dam;
     }

//getMahalleAdresKodu
//------------------------------------------------------------------------------
    public static DataMaxYolSpeed getMaxYolSpeed(double latitude, double longitude) {
        DataMaxYolSpeed dmys = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM YOL_LINKS YL ";
            sql += "WHERE SDO_NN(YL.GEOLOC, SDO_GEOMETRY(2001, 8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL) ,";
            sql += " 'sdo_num_res=1 distance=50 unit=meter') = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                int carLimit = rset.getInt("SPEED_LIMIT_OTOMOBIL");
                int minibusLimit = rset.getInt("SPEED_LIMIT_MINIBUS");
                int busLimit = rset.getInt("SPEED_LIMIT_OTOBUS");
                int truckLimit = rset.getInt("SPEED_LIMIT_KAMYON");
                int motorcycleLimit = rset.getInt("SPEED_LIMIT_MOTOSIKLET");
                int dangerLimit = rset.getInt("SPEED_LIMIT_TEHLIKELI");
                int tractorLimit = rset.getInt("SPEED_LIMIT_TRAKTOR");

                dmys = new DataMaxYolSpeed();

                dmys.setCarLimit(carLimit);
                dmys.setMinibusLimit(minibusLimit);
                dmys.setBusLimit(busLimit);
                dmys.setTruckLimit(truckLimit);
                dmys.setMotorcycleLimit(motorcycleLimit);
                dmys.setDangerousVehicleLimit(dangerLimit);
                dmys.setTractorLimit(tractorLimit);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dmys;
    } // getMaxYolSpeed()


    //-----------------------------------------------------------------------------

    public static DataEarthQuake getEarthQuakeInfo(double latitude, double longitude, long radius) {
        DataEarthQuake deq = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT TRUNC(AVG(D.BUYUKLUK), 2) AVG_MAGNITUDE, TRUNC(AVG(D.DERINLIK), 2) AVG_DEPTH, ";
            sql += "MIN(D.BUYUKLUK) AS MIN_MAGNITUDE, MAX(D.BUYUKLUK) MAX_MAGNITUDE, MIN(DERINLIK) AS MIN_DEPTH, ";
            sql += "MAX(D.DERINLIK) AS MAX_DEPTH , COUNT(*) AS CNT ";
            sql += "FROM DEPREM_GOV D ";
            sql += "WHERE TO_DATE(REPLACE(TARIH,'Z',''),'YYYY-MM-DD HH24:MI:SS') BETWEEN ADD_MONTHS(SYSDATE, -180) ";
            sql += "AND SYSDATE AND D.BUYUKLUK >= 2 AND ";
            sql += "SDO_WITHIN_DISTANCE(D.GEOLOC, SDO_GEOMETRY(2001, 8307, SDO_POINT_TYPE(?,?, NULL), NULL, NULL),'distance='||?||'') = 'TRUE' ";
            sql += "HAVING COUNT(*) > 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            pstmt.setString(3, String.valueOf(radius));
            rset = pstmt.executeQuery();

            if (rset.next()) {
                double avgMagnitude = rset.getDouble("AVG_MAGNITUDE");
                double avgDepth = rset.getDouble("AVG_DEPTH");
                double minMagnitude = rset.getDouble("MIN_MAGNITUDE");
                double maxMagnitude = rset.getDouble("MAX_MAGNITUDE");
                double minDepth = rset.getDouble("MIN_DEPTH");
                double maxDepth = rset.getDouble("MAX_DEPTH");
                long earthquakeCount = rset.getLong("CNT");

                deq = new DataEarthQuake();

                deq.setAvgMagnitude(avgMagnitude);
                deq.setAvgDepth(avgDepth);
                deq.setMinMagnitude(minMagnitude);
                deq.setMaxMagnitude(maxMagnitude);
                deq.setMinDepth(minDepth);
                deq.setMaxDepth(maxDepth);
                deq.setEarthquakeCount(earthquakeCount);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return deq;
    } // getEarthQuakeInfo()


    //-----------------------------------------------------------------------------

    public static DataIndoorVenue[] getIndoorVenueList() {
        DataIndoorVenue div = null;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        // Venue List..
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT VENUE_ID, VENUE_NAME, XCOOR, YCOOR, GEOMBR ";
            sql += "FROM INDOOR_VENUE ";
            sql += "ORDER BY VENUE_ID";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();

            while (rset.next()) {
                div = DataIndoorVenue.getInstance(rset);
                div = getIndoorVenueFloorList(div);

                array.add(div);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }  finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        DataIndoorVenue[] divs = new DataIndoorVenue[array.size()];
        for (int i = 0; i < divs.length; i++)
            divs[i] = (DataIndoorVenue) array.get(i);

        return divs;
    } // getIndoorVenueList()

    //-----------------------------------------------------------------------------

    public static DataIndoorVenue getIndoorVenueFloorList(DataIndoorVenue div) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        Map<Integer, String> floors = new HashMap<Integer, String>();
        Map<Integer, String> entrances = new HashMap<Integer, String>();

        // Venue Floor List..
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT F.FLOOR, F.FLOOR_NAME, E.NAME AS ENTRANCE_NAME ";
            sql += "FROM INDOOR_VENUE_FLOOR F ";
            sql += "LEFT OUTER JOIN INDOOR_VENUE_ENTRANCE E ON (E.VENUE_ID = F.VENUE_ID AND E.FLOOR = F.FLOOR) ";
            sql += "WHERE F.VENUE_ID = ? ";
            sql += "ORDER BY F.FLOOR";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            pstmt.setLong(1, div.getId());
            rset = pstmt.executeQuery();

            while (rset.next()) {
                floors.put(rset.getInt("FLOOR_LEVEL"), rset.getString("FLOOR_NAME"));

                // eger katta bir giris varsa, bunun listeye alinmasi..
                String entranceName = rset.getString("ENTRANCE_NAME");
                if (entranceName != null && entranceName.length() > 0)
                    entrances.put(rset.getInt("FLOOR_LEVEL"), entranceName);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        div.setFloors(floors);
        div.setEntrances(entrances);

        return div;
    } // getIndoorVenueFloorList()

    //-----------------------------------------------------------------------------

    public static DataIndoorPoi[] getIndoorPoiList(long venueId, int floorLevel, String category) {
        DataIndoorPoi dip = null;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        // IndoorPoi List..
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT V.VENUE_NAME AS VENUE_NAME, P.AREA_ID, P.AREA_NAME, P.FLOOR, F.FLOOR_NAME, P.BRAND_NAME,  ";
            sql += "P.BRAND_ID, P.SUB_TYPE, ";
            sql += "P.XCOOR_CENTER, P.YCOOR_CENTER, P.XCOOR_ENTRANCE, P.YCOOR_ENTRANCE, 0 AS DISTANCE ";
            sql += "FROM INDOOR_AREA P, ";
            sql += "INDOOR_VENUE_FLOOR F, ";
            sql += "INDOOR_VENUE V ";
            sql += "WHERE F.FLOOR = P.FLOOR AND V.VENUE_ID = P.VENUE_ID ";
            sql += "AND F.VENUE_ID = P.VENUE_ID AND P.VENUE_ID = ? ";
            if (floorLevel > -100)
                sql += "AND P.FLOOR = ? ";
            //if( category != null && category.length() > 0 ) sql += " AND CATEGORY = ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setLong(colno++, venueId);
            if (floorLevel > -100)
                pstmt.setInt(colno++, floorLevel);
            if (category != null && category.length() > 0)
                pstmt.setString(colno++, Utils.convToUpperEnglishChars(category));
            rset = pstmt.executeQuery();

            while (rset.next()) {
                dip = DataIndoorPoi.getInstance(rset);
                array.add(dip);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        DataIndoorPoi[] dips = new DataIndoorPoi[array.size()];
        for (int i = 0; i < dips.length; i++)
            dips[i] = (DataIndoorPoi) array.get(i);

        return dips;
    } // getIndoorPoiList()

    //-----------------------------------------------------------------------------

    public static DataIndoorPoi[] getIndoorPoiSearch(long venueId, int floorLevel, double latitude, double longitude, long radius, String category) {
        DataIndoorPoi dip = null;

        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        // IndoorPoi List..
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT K.VENUE_NAME, K.AREA_ID, K.AREA_NAME, K.FLOOR, K.FLOOR_NAME, K.BRAND_NAME, ";
            sql += "K.BRAND_ID, K.SUB_TYPE, ";
            sql += "K.XCOOR_CENTER, K.YCOOR_CENTER, K.XCOOR_ENTRANCE, K.YCOOR_ENTRANCE, K.DISTANCE FROM ( ";
            sql += "SELECT ";
            sql += "SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001, 8307, SDO_POINT_TYPE(P.XCOOR_ENTRANCE, P.YCOOR_ENTRANCE,NULL), NULL, NULL), ";
            sql += "SDO_GEOMETRY(2001, 8307, SDO_POINT_TYPE(?, ?, NULL), NULL, NULL), 0.001) DISTANCE, ";
            sql += "V.VENUE_NAME AS VENUE_NAME, P.AREA_ID, P.AREA_NAME, P.FLOOR, F.FLOOR_NAME, P.BRAND_NAME, ";
            sql += "P.BRAND_ID, P.SUB_TYPE, ";
            sql += "P.XCOOR_CENTER, P.YCOOR_CENTER, P.XCOOR_ENTRANCE, P.YCOOR_ENTRANCE  FROM INDOOR_AREA P, ";
            sql += "INDOOR_VENUE_FLOOR F, ";
            sql += "INDOOR_VENUE V ";
            sql += "WHERE F.FLOOR = P.FLOOR AND V.VENUE_ID = P.VENUE_ID ";
            sql += "AND F.VENUE_ID = P.VENUE_ID AND P.VENUE_ID = ? ";
            if (floorLevel > -100)
                sql += "AND P.FLOOR = ? ";
            //if( category != null && category.length() > 0 ) sql += " AND CATEGORY = ?";
            sql += ") K WHERE DISTANCE < ? ORDER BY DISTANCE ";

            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, longitude);
            pstmt.setDouble(colno++, latitude);
            pstmt.setLong(colno++, venueId);
            if (floorLevel > -100)
                pstmt.setInt(colno++, floorLevel);
            if (category != null && category.length() > 0)
                pstmt.setString(colno++, Utils.convToUpperEnglishChars(category));
            pstmt.setLong(colno++, radius);
            rset = pstmt.executeQuery();

            while (rset.next()) {
                dip = DataIndoorPoi.getInstance(rset);
                array.add(dip);
            }
        } catch (Exception ex) {
            Utils.logInfo("Exception: " + ex.getMessage());
            ex.printStackTrace();
        }finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        DataIndoorPoi[] dips = new DataIndoorPoi[array.size()];
        for (int i = 0; i < dips.length; i++)
            dips[i] = (DataIndoorPoi) array.get(i);

        return dips;
    } // getIndoorPoiSearch()

    //-----------------------------------------------------------------------------

    public static What3Words getWhat3Words(String res) {
        String wordResult = null;
        double latitude = 0.00;
        double longitude = 0.00;
        String lang = "en";

        What3Words what3Words = null;

        try {

            JSONObject jsonres = new JSONObject(res);

            wordResult = jsonres.getString("words");

            JSONObject jsongeo = jsonres.getJSONObject("geometry");
            latitude = jsongeo.getDouble("lat");
            longitude = jsongeo.getDouble("lng");

            lang = jsonres.getString("language");
            what3Words = new What3Words(wordResult, latitude, longitude, lang);
            return what3Words;


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return what3Words;
    } // getWhat3Words()
    //-----------------------------------------------------------------------------

    public static DataBBolumFeedback addBBolumFeedback(String key, long id, long bolumUavt, String address, String emailAddress, String adbVersion) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        ResultSet rset = null;
        int colno = 1;
        DataBBolumFeedback dbf = null;
       
        try {
           cnn = DbConn.getPooledConnection();
           String company = Utils.getKeyCompany(cnn,  key);
           long pk_id = Utils.getBBolumFeedbackId(cnn);
           sql = "INSERT INTO LBS_USER_FEEDBACK (KEY, FEEDBACK_ID, COMPANY, UAVT_ID, TEXT, PROCESS_STATUS, TIME_STAMP, ADB_VERSION )";
           sql += " VALUES (?,?,?,?,?,0,SYSDATE,?)";
           pstmt = cnn.prepareStatement(sql);
           pstmt.setQueryTimeout(360);
           pstmt.clearParameters();

           pstmt.setString(colno++, key);
           pstmt.setLong(colno++, pk_id);
           pstmt.setString(colno++, company);
           pstmt.setLong(colno++, bolumUavt);
           pstmt.setString(colno++, address);
           pstmt.setString(colno++, adbVersion);
           pstmt.executeUpdate();
              
           dbf = new DataBBolumFeedback(pk_id);
           DbConn.closeDBConnection(pstmt, rset);
            
           String emailSubj = "Locationbox Feedback Kaydedildi";
           String emailContent = company+ " firmasindan " + pk_id + " numaralı Feedback kaydı yapılmıştır";
           String emailData = "|" + emailSubj.replace("|"," ") + "|?" + emailContent;
            
           sendEmail( cnn, emailAddress, emailData);
            
          } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeConnection(cnn);
                DbConn.closeDBConnection(pstmt, rset);
            }
        
        return dbf;
    } // addBBolumFeedback()
    //-----------------------------------------------------------------------------
    
    public static void sendEmail(Connection cnn, String emailAddress, String emailData){                         
        PreparedStatement pstmt = null;
        String sql = null;
        try {                            
            sql = "INSERT INTO OUTBOX (ROWNO, GATEWAY, DESTINATION, DATA , TIME_STAMP, PROCESS_STATUS, GENERATOR, GENERATOR_ROWNO)";
            sql += " VALUES (SEQ_OUTBOX_ROWNO.NEXTVAL, ?, ?, ?, SYSDATE, ?, ?, ?)";                  
            pstmt = cnn.prepareStatement(sql);
            pstmt.clearParameters();
            
            int colno = 1;
            pstmt.setString(colno++, "EMAIL_1");              
            pstmt.setString(colno++, emailAddress); 
            pstmt.setString(colno++, emailData);    
            pstmt.setString(colno++, "0");  
            pstmt.setString(colno++, "doBBolumFeedback");    
            pstmt.setString(colno++, "0");  
        
            pstmt.setQueryTimeout(30);
            
            int sqlReturn = pstmt.executeUpdate();
            if( sqlReturn > 0 ) {
               Utils.logInfo("Kayit OUTBOX tablosuna islendi.");
            } else {
               Utils.logInfo("Kayit OUTBOX tablosuna islenemedi.");
            }
         }catch (Exception ex) {
            ex.printStackTrace();
            Utils.showError("sendEmail catch hatası: " + ex.getMessage());
        }finally {
            DbConn.closeDBConnection(pstmt, null);   
         }
     }       
            
    //-----------------------------------------------------------------------------

    public static DataBBolumFeedback getBBolumFeedback(String key, long id) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        ResultSet rset = null;
        int colno = 1;
        DataBBolumFeedback dbf = null;
       
       try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT FEEDBACK_ID, TIME_STAMP, COMPANY, UAVT_ID, TEXT, PROCESS_STATUS, PROCESS_TIME_STAMP, ADB_VERSION FROM LBS_USER_FEEDBACK WHERE FEEDBACK_ID=? AND KEY=?";          
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(colno++,id);
            pstmt.setString(colno++, key);
            rset = pstmt.executeQuery();
           
            if (rset.next()) {
              dbf = DataBBolumFeedback.getInstance(rset);
            } // 
           
           if( dbf!=null && dbf.getProcessStatus()>0){
                colno = 1;
                DbConn.closeDBConnection(pstmt, rset);
                sql = "SELECT TEXT FROM LBS_USER_FEEDBACK_RESULT WHERE FEEDBACK_ID=? ORDER BY SEQUENCE_NO ";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                pstmt.setLong(colno++,id);
                rset = pstmt.executeQuery();
              
                List feedbackResultList = new ArrayList();
                while (rset.next()) {
                 colno = 1;
                 String readdress = rset.getString(colno++);
                 feedbackResultList.add(readdress);
                }
                 
                dbf.setFeedbackResultList(feedbackResultList);
                 updateFeedBackRespTimeStamp(cnn, key, id, dbf.getProcessStatus());
                
           }
            
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeConnection(cnn);
            DbConn.closeDBConnection(pstmt, rset);
        }
        
        return dbf;
    } // addBBolumFeedback()
    //-----------------------------------------------------------------------------
    
    public static void updateFeedBackRespTimeStamp(Connection cnn, String key, long id, int processStatus){
        PreparedStatement pstmt = null;
        int colno = 1;
        String sql = null;
        
        try {
            if( 1 == processStatus)
              sql = "UPDATE LBS_USER_FEEDBACK SET RESPONSE_TIMESTAMP = SYSDATE, PROCESS_STATUS = 2 WHERE FEEDBACK_ID=? AND KEY=?";
            else
              sql = "UPDATE LBS_USER_FEEDBACK SET RESPONSE_TIMESTAMP = SYSDATE WHERE FEEDBACK_ID=? AND KEY=?";
            
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(colno++, id);
            pstmt.setString(colno++, key);
            pstmt.executeUpdate();
           
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        }finally{
           DbConn.closeDBConnection(pstmt, null);  
        }
       
    }

    public static List<DataBBolumFeedback> getListFeedback(String key, int processed) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;
        ResultSet rset = null;
      
        List<DataBBolumFeedback> list = null;
        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT FEEDBACK_ID, UAVT_ID, PROCESS_STATUS ";
            sql += "FROM LBS_USER_FEEDBACK  ";
            sql += "WHERE KEY = ? AND TIME_STAMP -30 < TRUNC(SYSDATE) ";
            if(processed > 0)
                sql += " AND PROCESS_STATUS = ? ";
            
            sql += " ORDER BY FEEDBACK_ID";
            
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setString(colno++, key);
            if(processed > 0)
                pstmt.setInt(colno++, processed);

            rset = pstmt.executeQuery();
            list = new ArrayList<DataBBolumFeedback>();
            while (rset.next()) {
              DataBBolumFeedback dbf = DataBBolumFeedback.getInstanceList(rset);
              list.add(dbf);
            }

            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                ex.printStackTrace();
            } finally {
                DbConn.closeConnection(cnn);
                DbConn.closeDBConnection(pstmt, rset);
            }
        
        return list;
    } // getListFeedback()
    //------------------------------------------------------------------------------    

    public static DataPoiAttribute[] getPoiAttributes(String key, long poiId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        Connection cnn = null;
        String sql = null;
        String columnName = null;

        int colno = 0;

        DataPoiAttribute dpa = null;
        DataPoiAttribute[] dpal = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DISTINCT ATTRIBUTE, DATA_TYPE FROM LBS_POI_ATTRIBUTE WHERE KEY=? AND POI_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            pstmt.setLong(colno++, poiId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String attribute = rset.getString("ATTRIBUTE");
                int dataType = rset.getInt("DATA_TYPE");
                array.add(new DataPoiAttribute(attribute, dataType));
            } // while()

            if (array.size() > 0) {
                
                DbConn.closeDBConnection(pstmt, rset);
                
                dpal = new DataPoiAttribute[array.size()];

                for (int i = 0; i < array.size(); i++) {
                    dpa = (DataPoiAttribute) array.get(i);
                    if (dpa.type == 1)
                        columnName = "NUMBER_VALUE";
                    if (dpa.type == 2)
                        columnName = "STRING_VALUE";
                    if (dpa.type == 3)
                        columnName = "DATE_VALUE";

                    if (i == 0)
                        sql = "SELECT ";

                    if ((array.size() - 1) == i)
                        sql += " MAX(DECODE(ATTRIBUTE, '" + dpa.attribute + "' , " + columnName + ")) AS \"" + dpa.attribute + "\"";
                    else
                        sql += " MAX(DECODE(ATTRIBUTE, '" + dpa.attribute + "' , " + columnName + ")) AS \"" + dpa.attribute + "\",";
                }
                sql += " FROM LBS_POI_ATTRIBUTE WHERE KEY=? AND POI_ID=? GROUP BY KEY, POI_ID";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                colno = 1;
                pstmt.setString(colno++, key);
                pstmt.setLong(colno++, poiId);
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    colno = 1;
                    for (int i = 0; i < array.size(); i++) {
                        dpa = (DataPoiAttribute) array.get(i);
                        String attribute = dpa.attribute;
                        int dataType = dpa.type;
                        String value = rset.getString(colno++);
                        dpal[i] = new DataPoiAttribute(attribute, dataType, value);
                    }
                } // while()
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dpal;
    } // getPoiAttributes()

    //-----------------------------------------------------------------------------

    public static DataPoiAttribute[] getPoiAttributes(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        String sql = null;
        int colno = 0;

        DataPoiAttribute dpa = null;
        DataPoiAttribute[] dpal = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DISTINCT ATTRIBUTE, DATA_TYPE FROM LBS_POI_ATTRIBUTE WHERE KEY=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String attribute = rset.getString("ATTRIBUTE");
                int dataType = rset.getInt("DATA_TYPE");
                array.add(new DataPoiAttribute(attribute, dataType));
            } // while()
            dpal = new DataPoiAttribute[array.size()];

            for (int i = 0; i < array.size(); i++) {
                dpa = (DataPoiAttribute) array.get(i);
                dpal[i] = dpa;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dpal;
    } // getPoiAttributes()

    //-----------------------------------------------------------------------------

    private static boolean isNobetciEczane(long id) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int count = 0;

        Connection cnn = DbConn.getPooledConnection();

        String nobetEndtime = Utils.getParameter("nobet_endtime");
        if (nobetEndtime == null)
            nobetEndtime = "09:00";

        GregorianCalendar gc = new GregorianCalendar();
        String dt = Utils.formatNumber(gc.get(Calendar.YEAR), 4) + "-" + Utils.formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" + Utils.formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " 00:00:00";
        String hhmm = Utils.formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" + Utils.formatNumber(gc.get(Calendar.MINUTE), 2);
        if (hhmm.compareTo(nobetEndtime) < 0) {
            gc.add(Calendar.DAY_OF_MONTH, -1);
            dt = Utils.formatNumber(gc.get(Calendar.YEAR), 4) + "-" + Utils.formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" + Utils.formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " 00:00:00";
        }

        try {
            /*
      sql = "SELECT COUNT(*) FROM HOLIDAYS WHERE TRUNC(H_DATE) = TRUNC(?)";
      pstmt = cnn.prepareStatement(sql);
 pstmt.setQueryTimeout(360);
      pstmt.clearParameters();
      pstmt.setLong(1, id);
      pstmt.setTimestamp(2, Utils.toTimestamp(dt));
      rset = pstmt.executeQuery();
      rset.next();
      count = rset.getInt(1);
      if( count > 0 ) return true;

      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;
*/
            sql = "SELECT COUNT(*) FROM POI_NOBET WHERE POI_ID=? AND NOBET_TARIHI=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, id);
            pstmt.setTimestamp(2, Utils.toTimestamp(dt));
            rset = pstmt.executeQuery();
            rset.next();
            count = rset.getInt(1);
            if (count > 0)
                return true;

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }
        return false;
    } // isNobetciEczane()

    //-----------------------------------------------------------------------------

    public static int getPackageCounter(String key, String packageName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT LP.COUNT FROM LBS_KEY_PACKAGE LKP, LBS_PACKAGE LP WHERE (LKP.PACKAGE_NAME = LP.PACKAGE_NAME) AND LKP.ACTIVE=1 AND LKP.EXPIRE_DATE > TRUNC(SYSDATE) AND LKP.KEY=? AND LP.PACKAGE_NAME=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            pstmt.setString(2, packageName);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int count = rset.getInt(1);
                DbConn.closeDBConnection(pstmt, rset);
                return count;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return -1;
    } 
    
    public static int getPackageCounter(Connection cnn, String key, String packageName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        try {
            sql = "SELECT LP.COUNT FROM LBS_KEY_PACKAGE LKP, LBS_PACKAGE LP WHERE (LKP.PACKAGE_NAME = LP.PACKAGE_NAME) AND LKP.ACTIVE=1 AND LKP.EXPIRE_DATE > TRUNC(SYSDATE) AND LKP.KEY=? AND LP.PACKAGE_NAME=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            pstmt.setString(2, packageName);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int count = rset.getInt(1);
                return count;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
        }

        return -1;
    }

    //-----------------------------------------------------------------------------

    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        double distance = -1.00;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SDO_GEOM.SDO_DISTANCE(SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL),0.001,'unit=M') FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            int colno = 1;
            pstmt.setDouble(colno++, lon1);
            pstmt.setDouble(colno++, lat1);
            pstmt.setDouble(colno++, lon2);
            pstmt.setDouble(colno++, lat2);
            rset = pstmt.executeQuery();
            rset.next();
            distance = rset.getDouble(1);
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return distance;
    } // calculateDistance()

    //-----------------------------------------------------------------------------

    private static double calculateShortestDistance(double lat1, double lon1, double lat2, double lon2, String consList, String criteria) {
        DataNetwork dn = DataNetwork.getNext(criteria);
        if (dn == null) {
            Utils.logInfo("Property file content criteria error !");
            return -1.00;
        }

        String networkName = dn.network;
        String host = dn.host;
        int port = dn.port;
        if (host == null || port == 0) {
            Utils.logInfo("Property file content (host, port) error !");
            return -1.00;
        }

        SrvConnect sc = new SrvConnect(host, port);
        if (!sc.getLineWithErrorCheck()) {
            Utils.logInfo("Could not connect to network server (Host: " + host + ", Port: " + port + ") !");
            return -1.00;
        }

        double distance = -1.00;

        try {
            sc.sendLine("INIT LOCATIONBOX");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Bad response from server !");
                return -1.00;
            }

            sc.sendLine("NETW " + networkName);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Network problem !");
                return -1.00;
            }

            if (consList != null && consList.length() > 0) {
                sc.sendLine("CONS " + consList);
                if (!sc.getLineWithErrorCheck()) {
                    Utils.logInfo("Constaint problem !");
                    return -1.00;
                }
            }

            sc.sendLine("PATH NOWRITE NODIR NOPTS");
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Path paramater setting problem !");
                return -1.00;
            }

            String line = "SHORTEST COORS " + lat1 + " " + lon1 + " " + lat2 + " " + lon2;
            sc.sendLine(line);
            if (!sc.getLineWithErrorCheck()) {
                Utils.logInfo("Bad response from server !");
                return -1.00;
            }

            distance = sc.getPathDistance();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            sc.sendLine("QUIT");
            sc.close();
        }

        return distance;
    } // calculateShortestDistance()

    //-----------------------------------------------------------------------------

    public static int checkAttributeType(String key, String attName) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int dataType = 0;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DATA_TYPE FROM LBS_POI_ATTRIBUTE WHERE KEY=? AND ATTRIBUTE=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            pstmt.setString(2, attName);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                dataType = rset.getInt(1);
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dataType;
    } // checkAttributeType()

    //-----------------------------------------------------------------------------

    private static double getSizeFromLevel_8307(int level, int height) {
        double mult = 0.0;
        double size = 0.0;

        mult = (double) height / 256.0;
        switch (level) {
        case 0:
            size = 18.3432337897295;
            break;
        case 1:
            size = 8.02792903033586;
            break;
        case 2:
            size = 2.97133606272284;
            break;
        case 3:
            size = 1.4856680313614292;
            break;
        case 4:
            size = 0.7323273170865205;
            break;
        case 5:
            size = 0.36098444648273015;
            break;
        case 6:
            size = 0.17793915081839787;
            break;
        case 7:
            size = 0.0877112295712235;
            break;
        case 8:
            size = 0.04323500204239257;
            break;
        case 9:
            size = 0.021311780458034123;
            break;
        case 10:
            size = 0.010505169991378118;
            break;
        case 11:
            size = 0.005178294898840654;
            break;
        case 12:
            size = 0.002552155261277706;
            break;
        case 13:
            size = 0.0012577343968491232;
            break;
        case 14:
            size = 4.57663683053751E-4;
            break;
        default:
            size = 18.3432337897295;
            break;
        } // switch()
        size *= mult;
        return size;
    } // getSizeFromLevel_8307()

    //-----------------------------------------------------------------------------

    private static double getSizeFromLevel_2321(int level, int height) {
        double mult = 0.0;
        double size = 0.0;

        mult = (double) height / 256.0;
        switch (level) {
        case 0:
            size = 400000.0;
            break;
        case 1:
            size = 400000.0;
            break;
        case 2:
            size = 200000.0;
            break;
        case 3:
            size = 133333.33333333334;
            break;
        case 4:
            size = 66666.66666666667;
            break;
        case 5:
            size = 36363.63636363636;
            break;
        case 6:
            size = 19047.619047619046;
            break;
        case 7:
            size = 9756.09756097561;
            break;
        case 8:
            size = 4878.048780487805;
            break;
        case 9:
            size = 2439.0243902439024;
            break;
        case 10:
            size = 1219.5121951219512;
            break;
        case 11:
            size = 610.6870229007634;
            break;
        case 12:
            size = 305.57677616501144;
            break;
        case 13:
            size = 152.78838808250572;
            break;
        case 14:
            size = 76.3213127265789;
            break;
        default:
            size = 400000.0;
            break;
        } // switch()
        size *= mult;
        return size;
    } // getSizeFromLevel_2321()

    //-----------------------------------------------------------------------------

    private static double getSizeFromLevel_3857(int level, int height) {
        double mult = 0.0;
        double size = 0.0;

        mult = (double) height / 256.0;
        switch (level) {
        case 0:
            size = 2.0037508E7;
            break;
        case 1:
            size = 1.0018754E7;
            break;
        case 2:
            size = 5009377.0;
            break;
        case 3:
            size = 2504688.5;
            break;
        case 4:
            size = 1252344.25;
            break;
        case 5:
            size = 626172.125;
            break;
        case 6:
            size = 313086.0625;
            break;
        case 7:
            size = 156543.03125;
            break;
        case 8:
            size = 78271.515625;
            break;
        case 9:
            size = 39135.7578125;
            break;
        case 10:
            size = 19567.87890625;
            break;
        case 11:
            size = 9783.939453125;
            break;
        case 12:
            size = 4891.9697265625;
            break;
        case 13:
            size = 2445.98486328125;
            break;
        case 14:
            size = 1222.992431640625;
            break;
        case 15:
            size = 611.4962158203125;
            break;
        case 16:
            size = 305.74810791015625;
            break;
        case 17:
            size = 152.87405395507812;
            break;
        case 18:
            size = 76.43702697753906;
            break;
        default:
            size = 2.0037508E7;
            break;
        } // switch()
        size *= mult;
        return size;
    } // getSizeFromLevel_3857()

    //-----------------------------------------------------------------------------

    private static int getLevelFromSize_8307(double size) {
        int level = 0;
        if (size <= 4.57663683053751E-4)
            level = 14;
        else if (size <= 0.0012577343968491232)
            level = 13;
        else if (size <= 0.002552155261277706)
            level = 12;
        else if (size <= 0.005178294898840654)
            level = 11;
        else if (size <= 0.010505169991378118)
            level = 10;
        else if (size <= 0.021311780458034123)
            level = 9;
        else if (size <= 0.04323500204239257)
            level = 8;
        else if (size <= 0.0877112295712235)
            level = 7;
        else if (size <= 0.17793915081839787)
            level = 6;
        else if (size <= 0.36098444648273015)
            level = 5;
        else if (size <= 0.7323273170865205)
            level = 4;
        else if (size <= 1.4856680313614292)
            level = 3;
        else if (size <= 2.97133606272284)
            level = 2;
        else if (size <= 8.02792903033586)
            level = 1;
        else if (size <= 18.3432337897295)
            level = 0;
        else
            level = 0;
        return level;
    } // getLevelFromSize_8307()

    //-----------------------------------------------------------------------------

    private static int getLevelFromSize_2321(double size) {
        int level = 0;
        if (size <= 76.3213127265789)
            level = 14;
        else if (size <= 152.78838808250572)
            level = 13;
        else if (size <= 305.57677616501144)
            level = 12;
        else if (size <= 610.6870229007634)
            level = 11;
        else if (size <= 1219.5121951219512)
            level = 10;
        else if (size <= 2439.0243902439024)
            level = 9;
        else if (size <= 4878.048780487805)
            level = 8;
        else if (size <= 9756.09756097561)
            level = 7;
        else if (size <= 19047.619047619046)
            level = 6;
        else if (size <= 36363.63636363636)
            level = 5;
        else if (size <= 66666.66666666667)
            level = 4;
        else if (size <= 133333.33333333334)
            level = 3;
        else if (size <= 200000.0)
            level = 2;
        else if (size <= 400000.0)
            level = 1;
        else if (size <= 400000.0)
            level = 0;
        else
            level = 0;
        return level;
    } // getLevelFromSize_2321()

    //-----------------------------------------------------------------------------

    private static int getLevelFromSize_3857(double size) {
        int level = 0;
        if (size <= 76.43702697753906)
            level = 18;
        else if (size <= 152.87405395507812)
            level = 17;
        else if (size <= 305.74810791015625)
            level = 16;
        else if (size <= 611.4962158203125)
            level = 15;
        else if (size <= 1222.992431640625)
            level = 14;
        else if (size <= 2445.98486328125)
            level = 13;
        else if (size <= 4891.9697265625)
            level = 12;
        else if (size <= 9783.939453125)
            level = 11;
        else if (size <= 19567.87890625)
            level = 10;
        else if (size <= 39135.7578125)
            level = 9;
        else if (size <= 78271.515625)
            level = 8;
        else if (size <= 156543.03125)
            level = 7;
        else if (size <= 313086.0625)
            level = 6;
        else if (size <= 626172.125)
            level = 5;
        else if (size <= 1252344.25)
            level = 4;
        else if (size <= 2504688.5)
            level = 3;
        else if (size <= 5009377.0)
            level = 2;
        else if (size <= 1.0018754E7)
            level = 1;
        else if (size <= 2.0037508E7)
            level = 0;
        else
            level = 0;
        return level;
    } // getLevelFromSize_3857()

    //---------------------------------------------------------------------------ekn

    /*
    private static double calculateDistance2(double lat1, double lon1, double lat2, double lon2) {
        double dx = (lon1 - lon2) * 100000.0;
        double dy = (lat1 - lat2) * 100000.0;
        double distance = Math.sqrt(dx * dx + dy * dy);
        return (distance);
    } // calculateDistance2()
*/
    //-----------------------------------------------------------------------------

    private static Vertex[] makeArray(double[] oarray) {
        Vertex[] vertices = new Vertex[oarray.length / 2];
        for (int i = 0; i < vertices.length; i++) {
            double dist = 0.00;
            if (i > 0)
                dist = calculateDistance(oarray[2 * (i - 1) + 1], oarray[2 * (i - 1)], oarray[2 * i + 1], oarray[2 * i]);
            vertices[i] = new Vertex(oarray[2 * i + 1], oarray[2 * i], dist);
        } // for()
        return vertices;
    } // makeArray()

    //-----------------------------------------------------------------------------


    private static double[] getCorrectedCoordinate(Vertex[] vertices, double latitude, double longitude) {
        if (vertices == null)
            return null;

        double x0 = longitude;
        double y0 = latitude;

        double x00 = 0.00;
        double y00 = 0.00;
        double xx = 0.00;
        double yy = 0.00;

        double len = 0.00;

        // Find minimum distance to the vertices
        double dist_inx = 0.00;
        double dist_xx = 0.00;
        double dist_yy = 0.00;
        double min_dist = 9999999.99;
        for (int i = 0; i < vertices.length; i++) {
            Vertex vertex = vertices[i];
            double dist = calculateDistance(y0, x0, vertex.getLatitude(), vertex.getLongitude());
            if (dist < min_dist) {
                min_dist = dist;
                dist_inx = i;
                dist_xx = vertex.getLongitude();
                dist_yy = vertex.getLatitude();
                len = 0;
                for (int j = 0; j < dist_inx; j++)
                    len += vertices[j].getLength();
                //        Utils.logInfo("VERTEX DIST: " + min_dist + ", INX: " + dist_inx + ", XX: " + dist_xx + ", YY: " + dist_yy + ", LEN: " + len);
            }
        } // for()

        // Calculate perpendicular intersection point for all line segments
        for (int i = 0; i < vertices.length - 1; i++) {
            double x1 = vertices[i].getLongitude();
            double y1 = vertices[i].getLatitude();
            double x2 = vertices[i + 1].getLongitude();
            double y2 = vertices[i + 1].getLatitude();

            if (x1 == x2) {
                x00 = x1;
                y00 = y0;
            } else {
                if (y1 == y2) {
                    x00 = x0;
                    y00 = y1;
                } else {
                    double m1 = (y2 - y1) / (x2 - x1);
                    double c1 = y1 - m1 * x1;

                    double m2 = (x1 - x2) / (y2 - y1);
                    double c2 = y0 - m2 * x0;

                    x00 = (c2 - c1) / (m1 - m2);
                    y00 = m1 * x00 + c1;
                }
            } // else

            // Check this instersection point lies with in line segment range
            double xmin = 0.00;
            double ymin = 0.00;
            double xmax = 0.00;
            double ymax = 0.00;
            if (x1 < x2) {
                xmin = x1;
                xmax = x2;
            } else {
                xmin = x2;
                xmax = x1;
            }
            if (y1 < y2) {
                ymin = y1;
                ymax = y2;
            } else {
                ymin = y2;
                ymax = y1;
            }

            // Save real intersection point if the distance is minimum
            if (x00 >= xmin && x00 <= xmax && y00 >= ymin && y00 <= ymax) {
                double dist = calculateDistance(y0, x0, y00, x00);
                if (dist < min_dist) {
                    min_dist = dist;
                    dist_inx = i;
                    dist_xx = x00;
                    dist_yy = y00;
                    len = 0;
                    for (int j = 0; j < i; j++)
                        len += vertices[j].getLength();
                    len += (vertices[i].getLength() * (dist_xx - vertices[i + 1].getLongitude()) / (vertices[i].getLongitude() - vertices[i + 1].getLongitude()));
                    //          Utils.logInfo("SEGMENT DIST: " + min_dist + ", INX: " + dist_inx + ", XX: " + dist_xx + ", YY: " + dist_yy + ", LEN: " + len);
                }
            }

        } // for()

        double[] res = new double[4];
        res[0] = dist_yy;
        res[1] = dist_xx;
        res[2] = len;
        res[3] = calculateDistance(x0, y0, dist_xx, dist_yy);
        return res;
    } // getCorrectedCoordinate()

    public static String escapeInvalidXml(String xmlStr) {

        xmlStr = xmlStr.replaceAll("&", "&amp;");
        xmlStr = xmlStr.replaceAll("\"", "&quot;");
        xmlStr = xmlStr.replaceAll("'", "&apos;");

        return xmlStr;
    }


}
