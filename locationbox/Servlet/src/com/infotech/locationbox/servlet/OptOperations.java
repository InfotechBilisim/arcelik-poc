package com.infotech.locationbox.servlet;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class OptOperations {

    public OptOperations() {
    }

    //-----------------------------------------------------------------------------

    public static DataPoint[] optMatrix(String key, DataNamedValue[] pids) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int colno = 0;
        int count = 0;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT POINT_ID,POINT_NAME,XCOOR,YCOOR FROM LBS_USER_POINT WHERE KEY=? AND POINT_ID IN (";
            count = 0;
            for (int i = 0; i < pids.length; i++) {
                if (!pids[i].getName().equals("u"))
                    continue;

                if (count > 0)
                    sql += ",";
                sql += "?";
                count++;
            } // for()
            sql += ")";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, key);
            for (int i = 0; i < pids.length; i++) {
                if (!pids[i].getName().equals("u"))
                    continue;

                pstmt.setString(colno++, pids[i].getValue());
            } // for()
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                array.add(new DataPoint(id, name, latitude, longitude));
            } // while()
            
            DbConn.closeDBConnection(pstmt, rset);

            sql = "SELECT ID,STANDARD_NAME,XCOOR,YCOOR FROM POI WHERE ID IN (";
            count = 0;
            for (int i = 0; i < pids.length; i++) {
                if (!pids[i].getName().equals("p"))
                    continue;

                if (count > 0)
                    sql += ",";
                sql += "?";
                count++;
            } // for()
            sql += ")";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            for (int i = 0; i < pids.length; i++) {
                if (!pids[i].getName().equals("p"))
                    continue;

                pstmt.setString(colno++, pids[i].getValue());
            } // for()
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String id = rset.getString(1);
                String name = rset.getString(2);
                double longitude = rset.getDouble(3);
                double latitude = rset.getDouble(4);
                array.add(new DataPoint(id, name, latitude, longitude));
            } // while()

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
            return null;
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        DataPoint[] dps = new DataPoint[array.size()];
        for (int i = 0; i < dps.length; i++)
            dps[i] = (DataPoint) array.get(i);

        for (int i = 0; i < array.size() - 1; i++) {
            for (int j = i + 1; j < array.size(); j++) {
                insertOptMatrixP2P("LOCATIONBOX", dps[i], dps[j]);
                insertOptMatrixP2P("LOCATIONBOX", dps[j], dps[i]);
            } // for(j)
        } // for(i)
        return dps;
    } // optMatrix()

    //-----------------------------------------------------------------------------

    public static long optRequest(String key, String name, DataNamedValue[] pids, DataVehicle[] vehicles,
                                  String email) {
        long requestId = 0;

        DataPoint[] dps = optMatrix(key, pids);
        if (dps == null)
            return -1;

        try {
            requestId = Utils.getUniqueRequestId();
            insertOptRequest(requestId, name, 0, email);
            for (int i = 0; i < vehicles.length; i++)
                insertOptMobile(requestId, i, vehicles[i].getId(), vehicles[i].getAlias(), vehicles[i].getCapacity());
            for (int i = 0; i < dps.length; i++)
                insertOptStopPoint(requestId, i, dps[i].getId(), dps[i].getName(), dps[i].getLatitude(),
                                   dps[i].getLongitude());
            for (int i = 0; i < dps.length; i++) {
                for (int j = 0; j < dps.length; j++) {
                    insertOptMatrix(requestId, i, j, dps[i], dps[j]);
                } // for(j)
            } // for(i)

        } catch (Exception ex) {
            ex.printStackTrace();
            return -2;
        }

        return requestId;
    } // optRequest()

    //-----------------------------------------------------------------------------

    public static DataOptResult[] optResult(long requestId) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT STATUS FROM OPT_REQUEST WHERE REQUEST_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            rset = pstmt.executeQuery();
            if (!rset.next())
                return null;

            int status = rset.getInt(1);
            if (status != 1) {
                DataOptResult[] dors = new DataOptResult[1];
                dors[0] = new DataOptResult();
                return dors;
            }

            DbConn.closeDBConnection(pstmt, rset);

            sql = "SELECT * FROM OPT_RESULT WHERE REQUEST_ID=? ORDER BY MOBILE,JOB_ORDER";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                DataOptResult dor = DataOptResult.getInstance(rset);
                array.add(dor);
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

        DataOptResult[] dors = new DataOptResult[array.size()];
        for (int i = 0; i < dors.length; i++)
            dors[i] = (DataOptResult) array.get(i);
        return dors;
    } // optResult()

    //-----------------------------------------------------------------------------

    private static void insertOptRequest(long requestId, String name, int status, String email) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "INSERT INTO OPT_REQUEST (REQUEST_ID,TIME_STAMP,FINISH_TIME_STAMP,DESCRIPTION,STATUS,OPTIMO_ID,EMAIL) VALUES (?,SYSDATE,NULL,?,?,(SELECT SYS_GUID() FROM DUAL),?)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            pstmt.setString(2, name);
            pstmt.setInt(3, status);
            pstmt.setString(4, email);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // insertOptRequest()

    //-----------------------------------------------------------------------------

    private static void insertOptMobile(long requestId, int seqno, long mobile, String alias, int capacity) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "INSERT INTO OPT_MOBILE (REQUEST_ID,SEQNO,MOBILE,ALIAS,CAPACITY) VALUES (?,?,?,?,?)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            pstmt.setInt(2, seqno);
            pstmt.setLong(3, mobile);
            pstmt.setString(4, alias);
            pstmt.setInt(5, capacity);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // insertOptMobile()

    //-----------------------------------------------------------------------------

    private static void insertOptStopPoint(long requestId, int seqno, String pointId, String pointName, double latitude,
                                           double longitude) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "INSERT INTO OPT_STOP_POINT (REQUEST_ID,SEQNO,POINTID,POINTNAME,XCOOR,YCOOR) VALUES (?,?,?,?,?,?)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            pstmt.setInt(2, seqno);
            pstmt.setString(3, pointId);
            pstmt.setString(4, pointName);
            pstmt.setDouble(5, longitude);
            pstmt.setDouble(6, latitude);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // insertOptStopPoint()

    //-----------------------------------------------------------------------------

    private static void insertOptMatrix(long requestId, int fromPointSeqno, int toPointSeqno, DataPoint fromDp,
                                        DataPoint toDp) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "INSERT INTO OPT_MATRIX (REQUEST_ID,FROM_POINT_SEQNO,TO_POINT_SEQNO,DISTANCE,DURATION) VALUES (?,?,?,NULL,NULL)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, requestId);
            pstmt.setInt(2, fromPointSeqno);
            pstmt.setInt(3, toPointSeqno);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // insertOptMatrix()

    //-----------------------------------------------------------------------------

    private static void insertOptMatrixP2P(String origin, DataPoint dpFrom, DataPoint dpTo) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        int colno = 0;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT COUNT(*) FROM OPT_MATRIX_P2P WHERE ORIGIN=? AND FROM_POINTID=? AND TO_POINTID=? AND TIME_STAMP > SYSDATE - 200";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, origin);
            pstmt.setString(colno++, dpFrom.getId());
            pstmt.setString(colno++, dpTo.getId());
            rset = pstmt.executeQuery();
            rset.next();
            int count = rset.getInt(1);
            if (count > 0)
                return;

            DbConn.closeDBConnection(pstmt, rset);

            sql = "DELETE FROM OPT_MATRIX_P2P WHERE ORIGIN=? AND FROM_POINTID=? AND TO_POINTID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, origin);
            pstmt.setString(colno++, dpFrom.getId());
            pstmt.setString(colno++, dpTo.getId());
            pstmt.executeUpdate();

            DbConn.closeDBConnection(pstmt, null);

            sql = "INSERT INTO OPT_MATRIX_P2P (ORIGIN,FROM_POINTID,FROM_POINTNAME,FROM_XCOOR,FROM_YCOOR,TO_POINTID,TO_POINTNAME,TO_XCOOR,TO_YCOOR,DISTANCE,DURATION,TIME_STAMP,OPTYPE,GEOLOC) VALUES (?,?,?,?,?,?,?,?,?,NULL,NULL,SYSDATE,NULL,NULL)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            colno = 1;
            pstmt.setString(colno++, origin);
            pstmt.setString(colno++, dpFrom.getId());
            pstmt.setString(colno++, dpFrom.getName());
            pstmt.setDouble(colno++, dpFrom.getLongitude());
            pstmt.setDouble(colno++, dpFrom.getLatitude());
            pstmt.setString(colno++, dpTo.getId());
            pstmt.setString(colno++, dpTo.getName());
            pstmt.setDouble(colno++, dpTo.getLongitude());
            pstmt.setDouble(colno++, dpTo.getLatitude());
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return;
    } // insertOptMatrixP2P()

}
