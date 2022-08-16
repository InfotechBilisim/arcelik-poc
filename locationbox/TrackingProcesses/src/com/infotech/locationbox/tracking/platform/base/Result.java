package com.infotech.locationbox.tracking.platform.base;

import java.sql.*;

public class Result extends Object {

  public Result() {
  }

  public static String getString(ResultSet rset, String fieldName) {
    String str = null;

    try {
      str = rset.getString(fieldName);
    }
    catch(Exception ex) {
    }
    return( str );
  } // getString()

  public static long getLong(ResultSet rset, String fieldName) {
    long val = 0;

    try {
      val = rset.getLong(fieldName);
    }
    catch(Exception ex) {
    }
    return( val );
  } // getLong()

  public static int getInt(ResultSet rset, String fieldName) {
    int val = 0;

    try {
      val = rset.getInt(fieldName);
    }
    catch(Exception ex) {
    }
    return( val );
  } // getInt()

  public static double getDouble(ResultSet rset, String fieldName) {
    double val = 0;

    try {
      val = rset.getDouble(fieldName);
    }
    catch(Exception ex) {
    }
    return( val );
  } // getDouble()

  public static boolean getBoolean(ResultSet rset, String fieldName) {
    boolean val = false;

    try {
      val = rset.getBoolean(fieldName);
    }
    catch(Exception ex) {
    }
    return( val );
  } // getBoolean()

}
