package com.infotech.locationbox.tracking.platform.base;

import com.infotech.locationbox.tracking.platform.service.*;

import java.io.*;
import java.sql.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class SystemObject extends Object {
  public String moduleName = null;
  public int moduleInstance = 1;
  public String swVersion = null;

  private String dbString = null;
  private String username = null;
  private String password = null;

  private long heartbeatInterval = 60; // in seconds
  private Thread heartbeat = null;

  public SrvListenerThread srvListener = null;
  private int srvPort;

  public SystemObject() {
  }

//------------------------------------------------------------------------------

  public void startUp(String[] args, String moduleName, String swVersion) {
    this.moduleName = moduleName;
    this.moduleInstance   = 1; // Default instance number
    this.swVersion = swVersion;

    if( !processArgs(args) ) return;
    
    DesEncrypter.initialize();

    Log.showText("Module name: " + moduleName + ", instance: " + moduleInstance + ", version: " + swVersion);
    Log.showError("Module name: " + moduleName + ", instance: " + moduleInstance + ", version: " + swVersion);

    if( dbString == null || username == null ) {
      Log.showText("DB parameters are incorrect !");
      return;
    }

    if( moduleName.equals("MsgProcessor") ) DbConnection.isConnectionPool = true;
    if( moduleName.equals("LocationProcessor") ) DbConnection.isConnectionPool = true;
    
    DbConnection.dbString = dbString;
    DbConnection.username = username;
    DbConnection.password = password;
    DbConnection.progName = "IND_" + moduleName + "_" + moduleInstance;

    String logPath = getSysModuleParamString("MODULE_LOG_PATH");
    if( logPath == null || logPath.length() <= 0 )
      logPath = null;
    else
      logPath += "/" + moduleName + "_" + moduleInstance;
    Log.initialize(this, logPath);

    try {
      srvPort = getSysModuleParamInt("MODULE_SERVICE_PORT");
      if( srvPort > 0 ) {
        srvListener = new SrvListenerThread(srvPort, this);
        srvListener.start();
      }
    }
    catch(IOException ex) {
      Log.showTextForService("*** Service Listener start failure: " + ex.getMessage());
      ex.printStackTrace();
      srvListener = null;
    }

    // Read heartbeat interval from database and start heartbeat timer
    heartbeatInterval = getSysParamLong("HEARTBEAT_INTERVAL");
    if( heartbeatInterval == 0 ) heartbeatInterval = 60;
    heartbeat = new Thread(new Heartbeat(this, heartbeatInterval));
    heartbeat.start();
    Log.showText("Heartbeat timer started with an interval " + heartbeatInterval);

    // Read module specific parameters from database
    if( !readModuleParams() ) return;

    // Start module specific threads
    runThreads();

    return;
  } // startUp()

//------------------------------------------------------------------------------

  class Heartbeat implements Runnable {
    protected boolean running = false;
    SystemObject so = null;
    long heartbeatInterval = 60;

    public Heartbeat(SystemObject so, long heartbeatInterval) {
      this.so = so;
      this.heartbeatInterval = heartbeatInterval;
    }

    public void run() {
      PreparedStatement pstmt = null;
      String sql = null;

      running = true;
      
      while( running ) {
        DbConnection dbCnn = DbConnection.getPooledConnection();

        try {
          sql = "UPDATE SYS_MODULE SET HEARTBEAT = SYSDATE WHERE MODULE_NAME=? AND MODULE_INSTANCE=?";
          pstmt = dbCnn.prepareStatement(sql);
          pstmt.clearParameters();
          pstmt.setString(1, moduleName);
          pstmt.setInt(2, moduleInstance);
          pstmt.executeUpdate();
        }
        catch (Exception ex) {
          ex.printStackTrace();
          dbCnn.checkSQLException(ex);
          Log.logException(1, sql, "Heartbeat.TimerTask");
        }
        finally {
          try { if( pstmt != null ) pstmt.close(); pstmt = null; } catch (Exception e) {;}
          try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
        }

        try { Thread.sleep(heartbeatInterval * 1000); } catch (Exception e) {;}
      } // while()
      return;
    }
  } // Heartbeat()

//------------------------------------------------------------------------------

  public boolean readModuleParams() {
    return  true;
  } // readModuleParams()

  public void runThreads() {
    return;
  } // runThreads()

//------------------------------------------------------------------------------

  public boolean processArgs(String[] args) {
    for( int  i = 0; i < args.length; i++ ) {
      String arg = args[i];
      if( arg.equals("-help") ) {
        printUsage();
        return false;
      }

      //----------------------------------------------
      if( arg.equals("-instance") ) {
        i++;
        if( i >= args.length ) {
          printUsage();
          return false;
        }

        moduleInstance = Integer.parseInt(args[i]);
      }
      else
      //----------------------------------------------
      if( arg.equals("-dbstring") ) {
        i++;
        if( i >= args.length ) {
          printUsage();
          return false;
        }

        dbString = args[i];
      }
      else
      //----------------------------------------------
      if( arg.equals("-username") ) {
        i++;
        if( i >= args.length ) {
          printUsage();
          return false;
        }

        username = args[i];
      }
      else
      //----------------------------------------------
      if( arg.equals("-password") ) {
        i++;
        if( i >= args.length ) {
          printUsage();
          return false;
        }

        password = args[i];
      }
      else
      if( arg.startsWith("-svc=") ) {
        // OK (Service Name)
      }
      else {
        printUsage();
        return false;
      }

    } // for()

    return  true;
  } // processArgs()

//------------------------------------------------------------------------------

  public static void printUsage() {
    Log.showText("Usage: java infomobil.<processorname> [-help] -dbtype {ORACLE | ORADB} -dbstring <db string> -username <username> -password <password>");
    Log.showText("");
  } // printUsage()

//------------------------------------------------------------------------------

  public long getSysModuleParamLong(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    long value = 0;
    
    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT PARAM_VALUE FROM SYS_MODULE_PARAM WHERE MODULE_NAME = ? AND MODULE_INSTANCE = ? AND PARAM_NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, moduleName);
      pstmt.setInt(2, moduleInstance);
      pstmt.setString(3, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() )
        value = Long.parseLong(rset.getString(1));
      else {
        rset.close();
        rset = null;
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_MODULE_PARAM (MODULE_NAME,MODULE_INSTANCE,PARAM_NAME,PARAM_VALUE) VALUES (?,?,?,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, moduleName);
        pstmt.setInt(2, moduleInstance);
        pstmt.setString(3, paramName);
        pstmt.setString(4, String.valueOf(value));
        pstmt.executeUpdate();

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysModuleParamLong");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysModuleParamLong()

//------------------------------------------------------------------------------

  public int getSysModuleParamInt(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int value = 0;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT PARAM_VALUE FROM SYS_MODULE_PARAM WHERE MODULE_NAME = ? AND MODULE_INSTANCE = ? AND PARAM_NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, moduleName);
      pstmt.setInt(2, moduleInstance);
      pstmt.setString(3, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() )
        value = Integer.parseInt(rset.getString(1));
      else {
        rset.close();
        rset = null;
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_MODULE_PARAM (MODULE_NAME,MODULE_INSTANCE,PARAM_NAME,PARAM_VALUE) VALUES (?,?,?,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, moduleName);
        pstmt.setInt(2, moduleInstance);
        pstmt.setString(3, paramName);
        pstmt.setString(4, String.valueOf(value));
        pstmt.executeUpdate();
        
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysModuleParamInt");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysModuleParamInt()

//------------------------------------------------------------------------------

  public void setSysModuleParamInt(String paramName, int value) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_MODULE_PARAM SET PARAM_VALUE=? WHERE MODULE_NAME=? AND MODULE_INSTANCE=? AND PARAM_NAME=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, String.valueOf(value));
      pstmt.setString(2, moduleName);
      pstmt.setInt(3, moduleInstance);
      pstmt.setString(4, paramName);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysModuleParamInt");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysModuleParamInt()

//------------------------------------------------------------------------------

  public void setSysModuleParamLong(String paramName, long value) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_MODULE_PARAM SET PARAM_VALUE=? WHERE MODULE_NAME=? AND MODULE_INSTANCE=? AND PARAM_NAME=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, String.valueOf(value));
      pstmt.setString(2, moduleName);
      pstmt.setInt(3, moduleInstance);
      pstmt.setString(4, paramName);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysModuleParamLong");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysModuleParamLong()

//------------------------------------------------------------------------------

  public void setSysModuleParamString(String paramName, String value) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_MODULE_PARAM SET PARAM_VALUE=? WHERE MODULE_NAME=? AND MODULE_INSTANCE=? AND PARAM_NAME=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, value);
      pstmt.setString(2, moduleName);
      pstmt.setInt(3, moduleInstance);
      pstmt.setString(4, paramName);
      pstmt.executeUpdate();
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysModuleParamString");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysModuleParamString()

//------------------------------------------------------------------------------

  public String getSysModuleParamString(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    String value = "";

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT PARAM_VALUE FROM SYS_MODULE_PARAM WHERE MODULE_NAME=? AND MODULE_INSTANCE=? AND PARAM_NAME=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, moduleName);
      pstmt.setInt(2, moduleInstance);
      pstmt.setString(3, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() )
        value = rset.getString(1);
      else {
        rset.close();
        rset = null;
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_MODULE_PARAM (MODULE_NAME,MODULE_INSTANCE,PARAM_NAME,PARAM_VALUE) VALUES (?,?,?,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, moduleName);
        pstmt.setInt(2, moduleInstance);
        pstmt.setString(3, paramName);
        pstmt.setString(4, value);
        pstmt.executeUpdate();

      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysModuleParamString");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysModuleParamString()

//------------------------------------------------------------------------------

  public String getSysModuleParamString_NoCreate(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    String value = "";

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT PARAM_VALUE FROM SYS_MODULE_PARAM WHERE MODULE_NAME = ? AND MODULE_INSTANCE = ? AND PARAM_NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, moduleName);
      pstmt.setInt(2, moduleInstance);
      pstmt.setString(3, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() ) value = rset.getString(1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysModuleParamString");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysModuleParamString()

//------------------------------------------------------------------------------

  public void setSysParamString(String paramName, String paramValue) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_PARAM SET DATA = ? WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, paramValue);
      pstmt.setString(2, paramName);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_PARAM(NAME,DESCRIPTION,DATA) VALUES (?,NULL,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, paramName);
        pstmt.setString(2, paramValue);
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysParamString");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysParamString()

//------------------------------------------------------------------------------

  public String getSysParamString(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    String value = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT DATA FROM SYS_PARAM WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() ) value = rset.getString(1);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysParamString");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysParamString()

//------------------------------------------------------------------------------

  public void setSysParamInt(String paramName, int paramValue) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_PARAM SET DATA = ? WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, "" + paramValue);
      pstmt.setString(2, paramName);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_PARAM(NAME,DESCRIPTION,DATA) VALUES (?,NULL,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, paramName);
        pstmt.setString(2, "" + paramValue);
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysParamInt");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysParamInt()

//------------------------------------------------------------------------------

  public int getSysParamInt(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    int value = 0;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT DATA FROM SYS_PARAM WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() ) value = Integer.parseInt(rset.getString(1));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysParamInt");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysParamInt()

//------------------------------------------------------------------------------

  public void setSysParamLong(String paramName, long paramValue) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_PARAM SET DATA = ? WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, "" + paramValue);
      pstmt.setString(2, paramName);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_PARAM(NAME,DESCRIPTION,DATA) VALUES (?,NULL,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, paramName);
        pstmt.setString(2, "" + paramValue);
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysParamLong");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // setSysParamLong()

//------------------------------------------------------------------------------

  public long getSysParamLong(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    long value = 0;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT DATA FROM SYS_PARAM WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() ) value = Long.parseLong(rset.getString(1));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysParamLong");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return( value );
  } // getSysParamLong()

//------------------------------------------------------------------------------

  public void setSysParamDouble(String paramName, double paramValue) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE SYS_PARAM SET DATA = ? WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, "" + paramValue);
      pstmt.setString(2, paramName);
      int count = pstmt.executeUpdate();
      if( count <= 0 ) {
        pstmt.close();
        pstmt = null;
        
        sql = "INSERT INTO SYS_PARAM(NAME,DESCRIPTION,DATA) VALUES (?,NULL,?)";
        pstmt = dbCnn.prepareStatement(sql);
        pstmt.clearParameters();
        pstmt.setString(1, paramName);
        pstmt.setString(2, "" + paramValue);
        pstmt.executeUpdate();
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "setSysParamDouble");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    
    return;
  } // setSysParamDouble()

//------------------------------------------------------------------------------

  public double getSysParamDouble(String paramName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;
    double value = 0;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT DATA FROM SYS_PARAM WHERE NAME = ?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, paramName);
      rset = pstmt.executeQuery();
      if( rset.next() ) value = Double.parseDouble(rset.getString(1));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "getSysParamDouble");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    
    return( value );
  } // getSysParamDouble()

//------------------------------------------------------------------------------

  public boolean isDbConnectionDead() {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();
    if( dbCnn == null || dbCnn.getCnn() == null ) return true;

    try {
      if( dbCnn.getCnn().isClosed() ) return true;

      sql = "SELECT SYSDATE FROM DUAL";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      return false;
      
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return true;
  } // isDbConnectionDead()

//------------------------------------------------------------------------------

  public synchronized long dbGetUniqueRowNo(String tableName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT SEQ_" + tableName + "_ROWNO.NEXTVAL FROM DUAL";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      long rowNo = rset.getLong(1);
      return rowNo;
      
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "dbGetUniqueRowNo");
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return 0;
  } // dbGetUniqueRowNo()

//------------------------------------------------------------------------------

  public void dbMarkProcessed(String tableName, int processDayCount, long rowNo) {
    dbMarkProcessed(tableName, processDayCount, rowNo, 1);
    return;
  } // dbMarkProcessed()
  
//------------------------------------------------------------------------------

  public void dbMarkProcessed(String tableName, int processDayCount, long rowNo, int status) {
    PreparedStatement pstmt = null;
    String sql = null;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "UPDATE " + tableName + " SET PROCESS_STATUS=?, PROCESS_TIME_STAMP=SYSDATE, PROCESS_MODULE_NAME=?, PROCESS_MODULE_INSTANCE=? WHERE TIME_STAMP > SYSDATE - ? AND ROWNO=?";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setInt(colno++, status);
      pstmt.setString(colno++, moduleName);
      pstmt.setInt(colno++, moduleInstance);
      pstmt.setInt(colno++, processDayCount);
      pstmt.setLong(colno++, rowNo);
      pstmt.executeUpdate();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
      Log.logException(1, sql, "dbMarkProcessed");
    }
    finally {
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }
    return;
  } // dbMarkProcessed()

//------------------------------------------------------------------------------

  public String dbToDateFunctionString(String dt) {
    return( "TO_DATE('" + dt + "', 'YYYY-MM-DD HH24:MI:SS')" );
  } // dbToDateFunctionString()

//------------------------------------------------------------------------------

  public void dbInsertMobileEvent(Mobile mobile, MobileLocation mloc, String eventCode, String eventDesc, int locTyp, long locId, String locName) {
    CallableStatement cstmt = null;
    String sql = null;
    int colno = 1;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "{ call SP_INSERT_MOBILE_EVENT(?,?,?,?,?,?,?,?) }";
      cstmt = dbCnn.callableStatement(sql);
      cstmt.setLong(colno++, mobile.getId());
      cstmt.setLong(colno++, mloc.getRowno());
      cstmt.setTimestamp(colno++, Utils.toTimestamp(mloc.getTimeStamp()));
      cstmt.setString(colno++, eventCode);
      cstmt.setString(colno++, eventDesc);
      cstmt.setInt(colno++, locTyp);
      cstmt.setLong(colno++, locId);
      cstmt.setString(colno++, locName);
      cstmt.executeUpdate();
    }
    catch (Exception ex) {
      Log.showError("MID: " + mobile.getId() + ", ROWNO: " + mloc.getRowno() + ", TS: " + mloc.getTimeStamp());
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( cstmt != null ) cstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // dbInsertMobileEvent()

//------------------------------------------------------------------------------

  public void dbInsertMobileAlarm(Mobile mobile, MobileLocation mloc, String eventCode, String eventDesc, int locTyp, long locId, String locName) {
    CallableStatement cstmt = null;
    String sql = null;
    int colno = 1;

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "{ call SP_INSERT_MOBILE_ALARM(?,?,?,?,?,?,?,?) }";
      cstmt = dbCnn.callableStatement(sql);
      cstmt.setLong(colno++, mobile.getId());
      cstmt.setLong(colno++, mloc.getRowno());
      cstmt.setTimestamp(colno++, Utils.toTimestamp(mloc.getTimeStamp()));
      cstmt.setString(colno++, eventCode);
      cstmt.setString(colno++, eventDesc);
      cstmt.setInt(colno++, locTyp);
      cstmt.setLong(colno++, locId);
      cstmt.setString(colno++, locName);
      cstmt.executeUpdate();
    }
    catch (Exception ex) {
      Log.showError("MID: " + mobile.getId() + ", ROWNO: " + mloc.getRowno() + ", TS: " + mloc.getTimeStamp());
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( cstmt != null ) cstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return;
  } // dbInsertMobileAlarm()

//------------------------------------------------------------------------------

  public boolean isAlarmGenerationRequired(long mobileId, String eventCode, int locTyp, long locId, String timeStamp) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    int currentHour = Utils.getDateTimeHour(timeStamp);

    DbConnection dbCnn = DbConnection.getPooledConnection();

    try {
      sql = "SELECT * FROM MOBILE_SCHEDULE WHERE MOBILE_ID=? AND EVENT_CODE=? AND LOC_TYP=? AND LOC_ID=? AND ACTIVE_FLAG=1 AND (ALWAYS=1 OR (START_DATE <= TRUNC(SYSDATE) AND END_DATE >= TRUNC(SYSDATE)))";
      pstmt = dbCnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, mobileId);
      pstmt.setString(colno++, eventCode);
      pstmt.setInt(colno++, locTyp);
      pstmt.setLong(colno++, locId);
      rset = pstmt.executeQuery();
      if( rset.next() ) {
        boolean always = rset.getLong("ALWAYS") != 0;
        if( always ) return true;

        int startHour = Utils.getDateTimeHour(rset.getString("START_TIME"));
        int endHour = Utils.getDateTimeHour(rset.getString("END_TIME"));
        if( startHour < endHour ) {
          if( startHour > currentHour || currentHour > endHour ) return false;

        }
        else {
          if( startHour > currentHour && currentHour > endHour ) return false;

        }

        boolean ok = false;
        GregorianCalendar gc = new GregorianCalendar();
        int dow = gc.get(Calendar.DAY_OF_WEEK);
        switch( dow ) {
        case Calendar.SUNDAY    : ok = rset.getInt("SUNDAY") != 0; break;
        case Calendar.MONDAY    : ok = rset.getInt("MONDAY") != 0; break;
        case Calendar.TUESDAY   : ok = rset.getInt("TUESDAY") != 0; break;
        case Calendar.WEDNESDAY : ok = rset.getInt("WEDNESDAY") != 0; break;
        case Calendar.THURSDAY  : ok = rset.getInt("THURSDAY") != 0; break;
        case Calendar.FRIDAY    : ok = rset.getInt("FRIDAY") != 0; break;
        case Calendar.SATURDAY  : ok = rset.getInt("SATURDAY") != 0; break;
        } // switch()
        
        return ok;
      }
    }
    catch (Exception ex) {
      ex.printStackTrace();
      dbCnn.checkSQLException(ex);
    }
    finally {
      try { if( rset != null ) rset.close(); } catch (Exception e) {;}
      try { if( pstmt != null ) pstmt.close(); } catch (Exception e) {;}
      try { if( dbCnn != null ) dbCnn.close(); } catch (Exception e) {;}
    }

    return false;
  } // isAlarmGenerationRequired()

//-----------------------------------------------------------------------------

  public String mobileRangeClause() {
    return mobileRangeClause(false);
  } // mobileRangeClause()
  
  public String mobileRangeClause(boolean isInbox) {
    return " ";
  } // mobileRangeClause()

//-----------------------------------------------------------------------------

  public String mobileRangeClause(String prefix) {
    return " ";
  } // mobileRangeClause()

//-----------------------------------------------------------------------------

  public int mobileRangeParams(PreparedStatement pstmt, int colno) {
    return colno;
  } // mobileRangeParams()

}

