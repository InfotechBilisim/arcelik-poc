package com.infotech.locationbox.tracking.platform.base;

import oracle.jdbc.OracleConnection;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;
import oracle.ucp.UniversalConnectionPoolAdapter;
import oracle.ucp.admin.UniversalConnectionPoolManager;
import oracle.ucp.admin.UniversalConnectionPoolManagerImpl;
import oracle.ucp.jdbc.PoolDataSourceFactory;
import oracle.ucp.jdbc.PoolDataSource;


public class DbConnection {
  public static String dbString = null;
  public static String username = null;
  public static String password = null;
  
  public static String progName = null;
  
  public static boolean isConnectionPool = false;

  public static UniversalConnectionPoolManager mgr = null;
  public static PoolDataSource pds = null;

  private static OracleConnection conn = null;
  private static boolean connected = false;

  private OracleConnection cnn = null;
  
  public DbConnection() {
  }

//-----------------------------------------------------------------------------
  
  public static DbConnection getPooledConnection() {
    if( !isConnectionPool ) return getPooledConnection_JDBC();

    DbConnection dbCnn = null;
    while( true ) {
      dbCnn = getPooledConnection_CP();
      if( dbCnn != null ) break;
      
      sleep();
    } // while()
    
    return dbCnn;
  } // getPooledConnection()

//-----------------------------------------------------------------------------
  
  public static DbConnection getPooledConnection_JDBC() {
    DbConnection dbCnn = new DbConnection();

    try {
      if( conn != null ) {
        if( conn.isClosed() ) {
          try { conn.close(); } catch (Exception e) {;}
          conn = null;
        }
        else {
          dbCnn.cnn = conn;
          return dbCnn;
        }
      }
    }
    catch (Exception ex) {
      ;
    }

    Log.showText("*** Establishing connection...");

    connected = false;
    while( true ) {
      try {
        Properties props = new Properties();
        props.setProperty("password", DesEncrypter.decrypt(password));
        props.setProperty("user", username);
        props.put("v$session.program", (progName == null ? "LBX" : progName));
  
        java.sql.Driver IfmxDrv = (java.sql.Driver) Class.forName("oracle.jdbc.driver.OracleDriver").newInstance();
        conn = (OracleConnection)java.sql.DriverManager.getConnection("jdbc:oracle:thin:" + dbString, props);
        dbCnn.cnn = conn;
        dbCnn.cnn.setAutoCommit(true);
        dbCnn.cnn.setStatementCacheSize(100);
        dbCnn.cnn.setImplicitCachingEnabled(true);
        connected = true;
      }
      catch (Exception ex) {
        ;
      }
      
      if( connected ) break;
      
      sleep();
    } // while()

    Log.showText("*** Database connection established !");
    return dbCnn;
  } // getPooledConnection_JDBC()

//-----------------------------------------------------------------------------
  
  public static DbConnection getPooledConnection_CP() {
    if( pds == null ) {
      boolean firstTime = true;
      while( true ) {
        if( initPoolParameters(firstTime) ) break;
        
        firstTime = false;
        try { Thread.sleep(1000); } catch (Exception e) {;}
      } // while()
    }

    DbConnection dbCnn = new DbConnection();

    try {
      dbCnn.cnn = (OracleConnection)pds.getConnection();
      dbCnn.cnn.setAutoCommit(true);
      dbCnn.cnn.setStatementCacheSize(100);
      dbCnn.cnn.setImplicitCachingEnabled(true);
    }
    catch (Exception ex) {
      if( ex.getMessage().indexOf("All connections in the Universal Connection Pool are in use") >= 0 ) {
        Log.showText("All connections are in use. Will retry...");
        return null;
      }
      
      ex.printStackTrace();
      return null;
    }
    
    return dbCnn;
  } // getPooledConnection_CP()

//-----------------------------------------------------------------------------
  
  public void close() {
    if( !isConnectionPool ) return;

    try {
      cnn.close();
      cnn = null;
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }

    return;
  } // close()

//-----------------------------------------------------------------------------
  
  public void closeAll() {
    if( !isConnectionPool ) {
      try { if( conn != null ) conn.close(); conn = null; cnn = null; } catch (Exception e) {;}
      return;
    }

    try {
      mgr.stopConnectionPool("INFOAPP");
      mgr.destroyConnectionPool("INFOAPP");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return;
  } // closeAll()

//-----------------------------------------------------------------------------

  public PreparedStatement prepareStatement(String sql) {
//    Log.showText("*** PREP - SQL: " + sql);
    try {
      PreparedStatement pstmt = cnn.prepareStatement(sql);
      pstmt.setMaxFieldSize(2048);
      pstmt.setFetchSize(1000);
      return pstmt;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  } // prepareStatement()

//-----------------------------------------------------------------------------

  public CallableStatement callableStatement(String sql) {
//    Log.showText("*** CALL - SQL: " + sql);
    try {
      CallableStatement cstmt = cnn.prepareCall(sql);
      cstmt.setMaxFieldSize(2048);
      cstmt.setFetchSize(1000);
      return cstmt;
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  } // callableStatement()

//-----------------------------------------------------------------------------

  public void setAutoCommit(boolean autoCommit) {
    try {
      cnn.setAutoCommit(autoCommit);
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return;
  } // setAutoCommit()

//-----------------------------------------------------------------------------

  public void commit() {
    try {
      cnn.commit();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return;
  } // commit()

//-----------------------------------------------------------------------------

  public void rollback() {
    try {
      cnn.rollback();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    return;
  } // rollback()

//-----------------------------------------------------------------------------

  public Connection getCnn() {
    return cnn;
  } // rollback()

//-----------------------------------------------------------------------------

  public void checkSQLException(Exception ex) {
    if( ex.getClass().getName().indexOf("java.sql.SQL") < 0 ) return;

    String msg = ex.getMessage();
    if( msg == null ) msg = "";
    Log.showError("EXCEPTION MESSAGE: " + msg);
    
    boolean err = false;
    if( msg.indexOf("ORA-01000") >= 0 ) err = true;
    if( msg.indexOf("OALL8") >= 0 ) err = true;
    if( msg.indexOf("Protocol violation:") >= 0 ) err = true;
    if( msg.indexOf("Closed Connection") >= 0 ) err = true;
    if( msg.indexOf("No more data to read from socket") >= 0 ) err = true;
    
    if( err ) {
      Log.showError("*** Disconnect database and exit program !");
      try { closeAll(); } catch (Exception e) {;}
      System.exit(1);
    }
    
    return;
  } // dbCheckSQLException()

//-----------------------------------------------------------------------------

  private static boolean initPoolParameters(boolean firstTime) {
    try {
      if( firstTime ) {
        mgr = UniversalConnectionPoolManagerImpl.getUniversalConnectionPoolManager();
        pds = PoolDataSourceFactory.getPoolDataSource();
        pds.setConnectionFactoryClassName("oracle.jdbc.pool.OracleDataSource");
        pds.setURL("jdbc:oracle:thin:" + dbString);
        pds.setUser(username);
        pds.setPassword(DesEncrypter.decrypt(password));     
        pds.setConnectionPoolName("INFOAPP");
        pds.setMaxConnectionReuseTime(100000);
        pds.setAbandonedConnectionTimeout(600); // 10 minutes
        pds.setTimeToLiveConnectionTimeout(0);
        pds.setConnectionWaitTimeout(120);      // 2 minutes
        pds.setInactiveConnectionTimeout(300);  // 5 minutes
        pds.setTimeoutCheckInterval(60);
        pds.setInitialPoolSize(1);
        pds.setMaxPoolSize(11);
        pds.setMinPoolSize(1);
        pds.setValidateConnectionOnBorrow(true);
        pds.setConnectionProperty("v$session.program", (progName == null ? "AVL" : progName));
        mgr.createConnectionPool((UniversalConnectionPoolAdapter)pds);
      }
      mgr.startConnectionPool("INFOAPP");
    }
    catch (Exception ex) {
      Log.showText("Database Connection Failed. Will retry...");
      return false;
    }
    
    return true;
  } // initPoolParameters()

//-----------------------------------------------------------------------------

  private static void sleep() {
    try {
      Log.showText("sleeping...");
      Thread.sleep(1000);
      Log.showText("awake.");
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return;
  } // sleep()

}
