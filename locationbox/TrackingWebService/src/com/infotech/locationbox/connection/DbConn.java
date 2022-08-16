package com.infotech.locationbox.connection;

import com.infotech.locationbox.utils.Utils;

import java.sql.*;
import javax.naming.*;
import javax.sql.*;

public class DbConn {

  public DbConn() {
  }
  
//-----------------------------------------------------------------------------

  public static Connection getPooledConnection() {
    Connection cnn = null;

    String datasource = Utils.getParameter("datasource");

    if( datasource == null || datasource.length() <= 0 ) return getPooledConnection_JDBC();
    
    try {
      Context initial = new InitialContext(); 
      DataSource ds = (DataSource)initial.lookup(datasource);
      cnn = ds.getConnection();
    }
    catch (Exception ex) {
      ex.printStackTrace();
            Utils.logInfo("Connection Error : " + ex.getMessage());
      return null;
    }
    
    return( cnn );
  } // getPooledConnection()

//-----------------------------------------------------------------------------

  public static Connection getPooledConnection_JDBC() {
    Connection cnn = null;
    
    String driver = Utils.getParameter("driver");
    String url = Utils.getParameter("url");
    String userid = Utils.getParameter("userid");
    String password = Utils.getParameter("password");
//    Utils.showText("URL: " + url + ", USERID: " + userid + ", PASSWORD: " + password);
  
    try {
      java.sql.Driver IfmxDrv = (java.sql.Driver) Class.forName(driver).newInstance();
      cnn = java.sql.DriverManager.getConnection(url, userid, password);
    }
    catch (Exception ex) {
            Utils.logInfo("Connection Error : " + ex.getMessage());
      ex.printStackTrace();
      return null;
    }
    
    return( cnn );
  } // getPooledConnection_JDBC()

}
