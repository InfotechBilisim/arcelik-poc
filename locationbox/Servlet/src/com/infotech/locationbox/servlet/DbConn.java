package com.infotech.locationbox.servlet;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import java.io.PrintWriter;

import java.net.HttpURLConnection;

import java.sql.Connection;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.zip.ZipOutputStream;

import javax.naming.Context;
import javax.naming.InitialContext;

import javax.sql.DataSource;

import oracle.jdbc.OracleConnection;
import oracle.jdbc.pool.OracleDataSource;

import oracle.sql.BLOB;
import oracle.sql.STRUCT;

public class DbConn {
  private static String datasource = null;
  private static Connection cnn = null;

  private static OracleDataSource ods = null;
  private static OracleConnection ocnn = null;

  public DbConn() {
  }

  //-----------------------------------------------------------------------------

  public static OracleConnection getOracleConnection() {
    try {
      if (ocnn != null && !ocnn.isClosed())
        return ocnn;

     // String driver = Utils.getParameter("driver");
      String url = Utils.getParameter("url");
      String userid = Utils.getParameter("userid");
      String password = Utils.getParameter("password");
      int encrypt = 0;
      String tmp = Utils.getParameter("encrypted");
      try { encrypt = Integer.parseInt(tmp); } catch (Exception e) {;}
        if(encrypt>0){
          userid = DesEncrypter.decrypt(userid);
          password = DesEncrypter.decrypt(password);
      }

      ods = new OracleDataSource();
      ods.setURL(url);
      ocnn = (OracleConnection) ods.getConnection(userid, password);
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return (ocnn);
  } // getOracleConnection()

  //-----------------------------------------------------------------------------

  public static Connection getPooledConnection() {
    datasource = Utils.getParameter("datasource");
    if (datasource == null || datasource.length() <= 0)
      return getPooledConnection_JDBC();

    try {
      Context initial = new InitialContext();
      DataSource ds = (DataSource) initial.lookup(datasource);
      Connection dsCnn = ds.getConnection();

      return (dsCnn);

    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.showError("Connection Error : " + ex.getMessage());
    }

    return null;
  } // getPooledConnection()

  //-----------------------------------------------------------------------------

  public static Connection getPooledConnection_JDBC() {
    try {
      if (cnn != null && !cnn.isClosed())
        return cnn;

      String driver = Utils.getParameter("driver");
      String url = Utils.getParameter("url");
      String userid = Utils.getParameter("userid");
      String password = Utils.getParameter("password");
      int encrypt = 0;
      String tmp = Utils.getParameter("encrypted");
      try { encrypt = Integer.parseInt(tmp); } catch (Exception e) {;}
        if(encrypt>0){
            userid = DesEncrypter.decrypt(userid);
            password = DesEncrypter.decrypt(password);
        }
      //    Log.showText("URL: " + url + ", USERID: " + userid + ", PASSWORD: " + password);

      java.sql.Driver IfmxDrv = (java.sql.Driver) Class.forName(driver).newInstance();
      cnn = java.sql.DriverManager.getConnection(url, userid, password);
    } catch (Exception ex) {
      Utils.showError("Connection Error : " + ex.getMessage());
      ex.printStackTrace();
      cnn = null;
    }

    return cnn;
  } // getPooledConnection_JDBC()

  //-----------------------------------------------------------------------------

  public static void waitTillConnect() {
    Connection test = null;

    while (test == null) {
      try {
        Thread.currentThread().sleep(10000);
        test = getPooledConnection();
      } catch (Exception ex) {
        ;
      }
    } // while()
    return;
  } // waitTillConnect()

  //-----------------------------------------------------------------------------

  public static void closeConnection(Connection dsCnn) {
    try {
      // if (datasource == null || datasource.length() <= 0)
     //   return;
        
        if (dsCnn == cnn)  return;
        
        if (dsCnn != null)
            dsCnn.close();
        dsCnn = null;
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return;
  } // closeConnection()
  //-----------------------------------------------------------------------------

    public static void closeDBConnection(PreparedStatement pstmt, ResultSet rset) {
          try {
              if (rset != null)
                  rset.close();
              rset = null;
          } catch (Exception e) {
              ;
          }
          try {
              if (pstmt != null)
                  pstmt.close();
              pstmt = null;
          } catch (Exception e) {
              ;
          }
    } // closeConnection()
  //-----------------------------------------------------------------------------
    public static void closeStreamConn(InputStream inp, OutputStream out) {
          try {
              if (out != null)
                  out.close();
              out = null;
          } catch (Exception e) {
              ;
          }
          try {
              if (inp != null)
                  inp.close();
              inp = null;
          } catch (Exception e) {
              ;
          }
    } // closeConnection()
    
    //-----------------------------------------------------------------------------
      public static void closeBStreamConn(BufferedInputStream inp, ZipOutputStream out) {
            try {
                if (out != null)
                    out.close();
                out = null;
            } catch (Exception e) {
                ;
            }
            try {
                if (inp != null)
                    inp.close();
                inp = null;
            } catch (Exception e) {
                ;
            }
      } // closeConnection()
      
    //-----------------------------------------------------------------------------
      public static void closeHttpConn(HttpURLConnection connection,InputStreamReader inp, PrintWriter out, BufferedReader br) {
          try {
              if (connection != null)
                connection.disconnect();
              connection = null;
          } catch (Exception e) {
              ;
          }
            try {
                if (out != null)
                    out.close();
                out = null;
            } catch (Exception e) {
                ;
            }
            try {
                if (inp != null)
                    inp.close();
                inp = null;
            } catch (Exception e) {
                ;
            }
          try {
              if (br != null)
                  br.close();
              br = null;
          } catch (Exception e) {
              ;
          }
      } // closeConnection()
      //-----------------------------------------------------------------------------
     public static void closeFileConn(FileReader fr, FileOutputStream fos) {
          try {
              if (fr != null)
                  fr.close();
              fr = null;
          } catch (Exception e) {
              ;
          }
        try {
            if (fos != null)
                fos.close();
            fos = null;
        } catch (Exception e) {
            ;
        }
    } // closeConnection()
  //-----------------------------------------------------------------------------

  public static STRUCT convToSTRUCT(Object obj) {
    if (obj == null)
      return null;

    try {
      if (!obj.getClass().getName().startsWith("weblogic.jdbc.wrapper.Struct"))
        return (STRUCT) obj;

      return (STRUCT) ((weblogic.jdbc.wrapper.Struct) obj).getVendorObj();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  } // convToSTRUCT()

  //-----------------------------------------------------------------------------

  public static BLOB convToBLOB(Object obj) {
    if (obj == null)
      return null;

    try {
      if (!obj.getClass().getName().startsWith("weblogic.jdbc.wrapper.Blob"))
        return (BLOB) obj;

      return (BLOB) ((weblogic.jdbc.wrapper.Blob) obj).getVendorObj();
    } catch (Exception ex) {
      ex.printStackTrace();
    }

    return null;
  } // convToBLOB()

}
