package com.infotech.locationbox.trackingws;


import com.infotech.locationbox.utils.Utils;

import java.util.Date;

public class DataRegister {
  public String username = null;
  public String key = null;
  public long loginId = 0;
  public int resultCode = 0;
  public String resultDesc = null;
  public String remoteIp = null;
  
  public static int timeOut = 0; // No timeout -- 1800; // In seconds
  
  public DataRegister() {
  }

  public DataRegister(String remoteIp) {
    this.remoteIp = remoteIp;
  }

  public DataRegister(String username, String key, int resultCode, String resultDesc) {
    this.username = username;
    this.key = key;
    this.resultCode = resultCode;
    this.resultDesc = resultDesc;
  }

//-----------------------------------------------------------------------------

  public String toString() {
    long time = ((new Date()).getTime() / 1000);
    return "(DR: " + username + ";" + key + ";" + time + ";" + loginId + ";" + resultCode + ";" + (remoteIp == null ? "0.0.0.0" : remoteIp) + ")";
  } // toString()

//-----------------------------------------------------------------------------

  public String getToken() {
    return DesEncrypter.encryptSimple(this.toString());
  }

//-----------------------------------------------------------------------------

  public static DataRegister getInstance(String token, String remoteIp) {
    if( token == null ) return null;
    
    try {
      String strToken = DesEncrypter.decryptSimple(Utils.decodeEscape(token));
      if( strToken != null && !strToken.startsWith("(DR: ") ) return null;
      
      if( strToken == null || !strToken.endsWith(")") ) return null;
      
      String[] info = strToken.substring(5, strToken.length() - 1).split(";");
      DataRegister dr = new DataRegister();
      dr.username = info[0];
      dr.key = info[1];

      if( timeOut > 0 ) {
        long time = Long.parseLong(info[2]);
        long curr = ((new Date()).getTime() / 1000);
        long delta = curr - time;
      //      Utils.logInfo("CURR: " + curr + ", TIME: " + time + ",DELTA: " + delta);
        if( delta < 0 || delta > timeOut ) return null;

      }
        
      dr.loginId = Long.parseLong(info[3]);
      if( !Utils.isUserExists(dr) ) return null;
        
      dr.resultCode = Integer.parseInt(info[4]);
      dr.remoteIp = (info[5].equals("0.0.0.0") ? null : info[5]);
      if( dr.remoteIp == null ) dr.remoteIp = remoteIp;
      else {
        if( !dr.remoteIp.equals(remoteIp) ) return null;
      }
        
      return dr;
    }
    
    catch(Exception ex) {
      ex.printStackTrace();
    }
    
    return null;
  }
  
//-----------------------------------------------------------------------------
  
}
