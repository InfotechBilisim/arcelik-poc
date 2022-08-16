package com.infotech.locationbox.utils;

import com.infotech.locationbox.connection.DbConn;

import com.infotech.locationbox.trackingws.DataRegister;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import java.nio.charset.StandardCharsets;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Utils extends Object {
  private static String paramFile = "trackingws";
  private static Properties paramValues = null;

  private static final String xlatHexTable = "0123456789ABCDEF";

  public Utils() {
  }

  //-----------------------------------------------------------------------------

  public static String getParameter(String key) {
    if (paramValues == null)
      paramValues = loadParams(paramFile);
    if (paramValues == null)
      return null;

    return (paramValues.getProperty(key));
  } // getParameter()

  //-----------------------------------------------------------------------------

  public static void clearParams() {
    paramValues = null;
    return;
  } // clearParams()

  //-----------------------------------------------------------------------------

  public static Properties loadParams(String file) {
    Properties prop = null;

    try {
      prop = new Properties();
      ResourceBundle bundle = ResourceBundle.getBundle(file);
      Enumeration enu = bundle.getKeys();
      String key = null;
      while (enu.hasMoreElements()) {
        key = (String) enu.nextElement();
        prop.put(key, bundle.getObject(key));
      } // while()
    } catch (Exception ex) {
      ex.printStackTrace();
      return null;
    }

    return (prop);
  } // loadParams()

  //------------------------------------------------------------------------------

  public static char xlatHexFromTable(int inx) {
    char ch;

    if (inx >= xlatHexTable.length())
      inx = 0;
    ch = xlatHexTable.charAt(inx);
    return (ch);
  } // xlatHexFromTable()

  public static int getXlatHexIndex(char ch) {
    int inx;

    inx = xlatHexTable.indexOf(ch);
    if (inx < 0)
      inx = 0;
    return (inx);
  } // getXlatHexIndex()

  public static String convNumberToHexString(long number, int length) {
    String str = "";

    for (; length > 0; length--) {
      int inx = (int) (number % 16);
      str = xlatHexFromTable(inx) + str;
      number /= 16;
    } // for()
    return (str);
  } // convNumberToHexString()

  public static long convHexStringToNumber(String str, int length) {
    long number = 0;

    for (int i = 0; i < length; i++) {
      char ch = str.charAt(i);
      if (ch == 0)
        break;

      number = number * 16 + getXlatHexIndex(ch);
    } // for()
    return (number);
  } // convHexStringToNumber()

  //------------------------------------------------------------------------------

  public static String formatNumber(int num, int len) {
    String s = "";
    for (int i = 0; i < len; i++)
      s += "0";
    s += num;
    s = s.substring(s.length() - len);
    return (s);
  } // formatNumber()

  //------------------------------------------------------------------------------

  public static String formatNumber(long num, int len) {
    String s = "";
    for (int i = 0; i < len; i++)
      s += "0";
    s += num;
    s = s.substring(s.length() - len);
    return (s);
  } // formatNumber()

  //------------------------------------------------------------------------------

  public static String formatNumber(double num, int totlen, int len) {
    String n = "";
    if (num >= 1)
      n += num;
    else {
      num += 1;
      n += num;
      n = "0" + n.substring(1);
    }
    String s = "";
    for (int i = 0; i < totlen - len; i++)
      s += "0";
    s += n;
    for (int i = 0; i < len; i++)
      s += "0";
    int pos = s.indexOf('.');
    if (len == 0)
      s = s.substring(pos - (totlen - 1), pos);
    else
      s = s.substring(pos - (totlen - 1 - len), pos + 1 + len);
    return (s);
  } // formatNumber()

  //------------------------------------------------------------------------------

  public static String formatNumeric(double num, int len) {
    String s = "";
    String f = "0.";
    for (int i = 0; i < len; i++) {
      s += "0";
      f += "0";
    } // for()
    f += "5";
    num += Double.parseDouble(f);
    s = num + s;
    int pos = s.indexOf('.');
    if (len == 0)
      s = s.substring(0, pos);
    else
      s = s.substring(0, pos + 1 + len);
    return (s);
  } // formatNumeric()

  //-----------------------------------------------------------------------------

  public static String convToTurkish(String txt) {
    if (txt == null)
      return null;

    String s = "";

    for (int i = 0; i < txt.length(); i++) {
      char ch = txt.charAt(i);
      if (ch == '~') {
        i++;
        ch = txt.charAt(i);
        switch (ch) {
        case '~':
          ch = '~';
          break;
        case 'b':
          ch = ' ';
          break;
        case 'C':
          ch = (char) 0x0C7;
          break;
        case 'c':
          ch = (char) 0x0E7;
          break;
        case 'G':
          ch = (char) 0x11E;
          break;
        case 'g':
          ch = (char) 0x11F;
          break;
        case 'S':
          ch = (char) 0x15E;
          break;
        case 's':
          ch = (char) 0x15F;
          break;
        case 'I':
          ch = (char) 0x130;
          break;
        case 'i':
          ch = (char) 0x131;
          break;
        case 'U':
          ch = (char) 0x0DC;
          break;
        case 'u':
          ch = (char) 0x0FC;
          break;
        case 'O':
          ch = (char) 0x0D6;
          break;
        case 'o':
          ch = (char) 0x0F6;
          break;
        } // switch()
      } // if()
      s += ch;
    } // for()
    return (s);
  } // convToTurkish()

  //-----------------------------------------------------------------------------

  public static String convToAscii(String txt) {
    String s = "";

    for (int i = 0; i < txt.length(); i++) {
      char ch = txt.charAt(i);
      switch (ch) {
      case 0x07E:
        s += "~~";
        break;
      case 0x020:
        s += "~b";
        break;
      case 0x0C7:
        s += "~C";
        break;
      case 0x0E7:
        s += "~c";
        break;
      case 0x11E:
        s += "~G";
        break;
      case 0x11F:
        s += "~g";
        break;
      case 0x15E:
        s += "~S";
        break;
      case 0x15F:
        s += "~s";
        break;
      case 0x130:
        s += "~I";
        break;
      case 0x131:
        s += "~i";
        break;
      case 0x0DC:
        s += "~U";
        break;
      case 0x0FC:
        s += "~u";
        break;
      case 0x0D6:
        s += "~O";
        break;
      case 0x0F6:
        s += "~o";
        break;
      default:
        s += ch;
        break;
      } // switch()
    } // for()
    return (s);
  } // convToAscii()

  //-----------------------------------------------------------------------------

  public static String htmlFilter(String txt) {
    String s = "";
    for (int i = 0; i < txt.length(); i++) {
      char ch = txt.charAt(i);
      switch (ch) {
      case '<':
        s += "&lt;";
        break;
      case '>':
        s += "&gt;";
        break;
      case '|':
        s += "&brvbar;";
        break;
      default:
        s += ch;
        break;
      } // switch()
    } // for()
    return (s);
  } // htmlFilter()

  //-----------------------------------------------------------------------------

  public static String convStr2Url(String txt) {
    String s = "";

    for (int i = 0; i < txt.length(); i++) {
      char ch = txt.charAt(i);
      switch (ch) {
      case 0x020:
        s += "+";
        break;
      case 0x025:
        s += "%25";
        break;
      case 0x07C:
        s += "%7C";
        break;
      case 0x023:
        s += "%23";
        break;
      case 0x026:
        s += "%26";
        break;
      case 0x03F:
        s += "%3F";
        break;
      case 0x040:
        s += "%40";
        break;
      case 0x02B:
        s += "%2B";
        break;
      case 0x009:
        s += "%09";
        break;
      case 0x027:
        s += "%27";
        break;
      default:
        s += ch;
        break;
      } // switch()
    } // for()
    return (s);
  } // convStr2Url()

  //-----------------------------------------------------------------------------

  public static String encodeEscape(String txt) {
    if (txt == null)
      return null;

    String s = "";
    for (int i = 0; i < txt.length(); i++) {
      int ch = txt.charAt(i);
      switch (ch) {
      case 0xDE:
        s += "%u015E";
        break;
      case 0x15E:
        s += "%u015E";
        break;
      case 0xFE:
        s += "%u015F";
        break;
      case 0x15F:
        s += "%u015F";
        break;
      case 0xD0:
        s += "%u011E";
        break;
      case 0x11E:
        s += "%u011E";
        break;
      case 0xF0:
        s += "%u011F";
        break;
      case 0x11F:
        s += "%u011F";
        break;
      case 0xDD:
        s += "%u0130";
        break;
      case 0x130:
        s += "%u0130";
        break;
      case 0xFD:
        s += "%u0131";
        break;
      case 0x131:
        s += "%u0131";
        break;
      default:
        s += (char) ch;
        break;
      } // switch()
    } // for()
    return s;
  } // encodeEscape()

  //-----------------------------------------------------------------------------

  public static String decodeEscape(String txt) {
    if (txt == null)
      return null;

    String s = "";
    for (int i = 0; i < txt.length(); i++) {
      int ch = txt.charAt(i);
      if (ch != '%') {
        s += (char) ch;
        continue;
      }

      if (i + 3 > txt.length()) {
        s += (char) ch;
        continue;
      }

      String chk = txt.substring(i, i + 3);
      if (chk.equals("%20")) {
        ch = 0x020;
        i += 2;
      } else if (chk.equals("%21")) {
        ch = 0x021;
        i += 2;
      } else if (chk.equals("%22")) {
        ch = 0x022;
        i += 2;
      } else if (chk.equals("%23")) {
        ch = 0x023;
        i += 2;
      } else if (chk.equals("%24")) {
        ch = 0x024;
        i += 2;
      } else if (chk.equals("%25")) {
        ch = 0x025;
        i += 2;
      } else if (chk.equals("%26")) {
        ch = 0x026;
        i += 2;
      } else if (chk.equals("%27")) {
        ch = 0x027;
        i += 2;
      } else if (chk.equals("%28")) {
        ch = 0x028;
        i += 2;
      } else if (chk.equals("%29")) {
        ch = 0x029;
        i += 2;
      } else if (chk.equals("%2C")) {
        ch = 0x02C;
        i += 2;
      } else if (chk.equals("%3C")) {
        ch = 0x03C;
        i += 2;
      } else if (chk.equals("%3F")) {
        ch = 0x03F;
        i += 2;
      } else if (chk.equals("%C7")) {
        ch = 0x0C7;
        i += 2;
      } else if (chk.equals("%E7")) {
        ch = 0x0E7;
        i += 2;
      } else if (chk.equals("%DC")) {
        ch = 0x0DC;
        i += 2;
      } else if (chk.equals("%FC")) {
        ch = 0x0FC;
        i += 2;
      } else if (chk.equals("%D6")) {
        ch = 0x0D6;
        i += 2;
      } else if (chk.equals("%F6")) {
        ch = 0x0F6;
        i += 2;
      } else {
        if (i + 4 > txt.length()) {
          s += (char) ch;
          continue;
        }

        chk = txt.substring(i, i + 4);
        if (chk.equals("%xDD")) {
          ch = 0x130;
          i += 3;
        } else {
          if (i + 6 > txt.length()) {
            s += (char) ch;
            continue;
          }

          chk = txt.substring(i, i + 2);
          if (chk.equals("%u")) {
            chk = txt.substring(i + 2, i + 6);
            ch = (int) Utils.convHexStringToNumber(chk, 4);
            i += 5;
          }

        } // else
      } // else

      s += (char) ch;
    } // for()
    return s;
  } // decodeEscape()

  //-----------------------------------------------------------------------------

  public static String downloadResponse(String postUrl, String postData) {
    InputStream inp = null;
    OutputStream out = null;
    String strResult = null;

    Utils.showText("Request Url: " + postUrl);

    try {
      URL url = new URL(postUrl);
      HttpURLConnection http = (HttpURLConnection) url.openConnection();
      http.setRequestMethod("POST");
      http.setDoOutput(true);
      http.setDoInput(true);
      http.connect();

      out = http.getOutputStream();
      out.write(postData.getBytes());
      out.flush();
      out.close();

      inp = http.getInputStream();
      int code = http.getResponseCode();

      strResult = "";
      byte b[] = new byte[10240];
      while (true) {
        int length = inp.read(b, 0, b.length);
        if (length < 0)
          break;

        strResult += new String(b, 0, length);
      } // while()
      strResult = strResult.trim();
      inp.close();
      http.disconnect();
    } catch (Exception ex) {
      ex.printStackTrace();
      try {
        out.close();
      } catch (Exception e) {
        ;
      }
      try {
        inp.close();
      } catch (Exception e) {
        ;
      }
    }

    return strResult;
  } // downloadResponse()

  //-----------------------------------------------------------------------------

  public static String getCurrentDate() {
    Calendar c = new GregorianCalendar();
    String dt;

    dt = formatNumber(c.get(Calendar.YEAR), 4) + formatNumber(c.get(Calendar.MONTH) + 1, 2) + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2);
    return (dt);
  }

  //-----------------------------------------------------------------------------

  public static String getCurrentDateTime() {
    Calendar c = new GregorianCalendar();
    String dt;

    dt =
      formatNumber(c.get(Calendar.YEAR), 4) + "-" + formatNumber(c.get(Calendar.MONTH) + 1, 2) + "-" + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2) + " " +
      formatNumber(c.get(Calendar.HOUR_OF_DAY), 2) + ":" + formatNumber(c.get(Calendar.MINUTE), 2) + ":" + formatNumber(c.get(Calendar.SECOND), 2);
    return (dt);
  }

  //-----------------------------------------------------------------------------

  public static String getDateTime(long ts) {
    Calendar c = new GregorianCalendar();
    c.setTimeInMillis(ts);
    String dt;

    dt =
      formatNumber(c.get(Calendar.YEAR), 4) + "-" + formatNumber(c.get(Calendar.MONTH) + 1, 2) + "-" + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2) + " " +
      formatNumber(c.get(Calendar.HOUR_OF_DAY), 2) + ":" + formatNumber(c.get(Calendar.MINUTE), 2) + ":" + formatNumber(c.get(Calendar.SECOND), 2);
    return (dt);
  }

  //------------------------------------------------------------------------------

  public static Timestamp toTimestamp(String datetime) throws Exception {
    int year = 2000;
    int month = 1;
    int day = 1;
    int hour = 0;
    int minute = 0;
    int second = 0;

    if (datetime.length() == 5) {
      hour = Integer.parseInt(datetime.substring(0, 2));
      minute = Integer.parseInt(datetime.substring(3, 5));
    } else {
      year = Integer.parseInt(datetime.substring(0, 4));
      if (year == 0)
        year = 2000;
      month = Integer.parseInt(datetime.substring(4, 6));
      if (month == 0)
        month = 1;
      day = Integer.parseInt(datetime.substring(6, 8));
      if (day == 0)
        day = 1;
      if (datetime.length() >= 12) {
        hour = Integer.parseInt(datetime.substring(8, 10));
        minute = Integer.parseInt(datetime.substring(10, 12));
        if (datetime.length() >= 14)
          second = Integer.parseInt(datetime.substring(12, 14));
      }
    }
    Calendar c = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
    return (new Timestamp(c.getTime().getTime()));
  } // toTimestamp()

  //------------------------------------------------------------------------------

  public synchronized static void logInfo(String txt) {
    String fullPath = Utils.getParameter("logfileprefix") + "_" + getCurrentDate() + ".log";
    try {
      BufferedWriter out = new BufferedWriter(new FileWriter(fullPath, true));
      out.write(getCurrentDateTime() + ": " + txt);
      out.newLine();
      out.close();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
    return;
  } // logInfo()

  //------------------------------------------------------------------------------

  public static String getStringFromBytes(byte[] buf, int len) {
    String s = "";
    for (int i = 0; i < len; i++) {
      int ch = (int) buf[i];
      switch (ch) {
      case '%':
        String hex = "" + (char) buf[i + 1] + (char) buf[i + 2];
        i += 2;
        ch = (int) convHexStringToNumber(hex, 2);
        s += (char) ch;
        break;
      default:
        if (ch == '+')
          s += ' ';
        else
          s += (char) ch;
        break;
      } // switch()
    } // for()
    return (s);
  } // getStringFromBytes()

  //-----------------------------------------------------------------------------

  public static void showText(String txt) {
    System.out.println(getCurrentDateTime() + ": " + txt);
    Utils.logInfo(txt);
    return;
  } // showText()

  //-----------------------------------------------------------------------------

  public static void showError(String txt) {
    System.err.println(getCurrentDateTime() + ": " + txt);
    Utils.logInfo(txt);
    return;
  } // showError()

  //-----------------------------------------------------------------------------

  public static byte[] getJsonStringBytes(String txt) {
    int posBeg = txt.indexOf('{');
    if (posBeg < 0)
      return null;

    int posEnd = txt.lastIndexOf('}');
    if (posEnd < 0)
      return null;

    posEnd++;
    byte[] bytes = txt.substring(posBeg, posEnd).getBytes(StandardCharsets.UTF_8);
    return bytes;
  } // getJsonStringBytes()

  //-----------------------------------------------------------------------------

  public static long getUniqueId(String tableName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT SEQ_" + tableName + "_ID.NEXTVAL FROM DUAL";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      long id = rset.getLong(1);
      return id;
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.showError("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return 0;
  } // getUniqueId()

  //-----------------------------------------------------------------------------

  public static long getUniqueRowno(String tableName) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT SEQ_" + tableName + "_ROWNO.NEXTVAL FROM DUAL";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      long id = rset.getLong(1);
      return id;
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.showError("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return 0;
  } // getUniqueRowno()

  //-----------------------------------------------------------------------------

  public static DataRegister getUserDetails(String username, String password, String key) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    Utils.showText("USERNAME: " + username + ", PASSWORD: " + password);

    try {
      /*
      java.util.Date dt = new java.util.Date();
      long loginId = dt.getTime();
      loginId %= 1000000000L;
      loginId *= 117;
      loginId %= 1000000000L;
*/
      long loginId = 123456789L;
      sql =
        "UPDATE USERS SET LOG_ID=?,LAST_LOGIN_TIME_STAMP=SYSDATE WHERE USERNAME=? AND PASSWORD=(SELECT ENCRYPTION.ENCRYPT(?,'A1B390F12D243680','132FD66F5009895C','06F58436588321FF','0123456789ABCDEF') FROM DUAL) AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, loginId);
      pstmt.setString(colno++, username);
      pstmt.setString(colno++, password);
      pstmt.setString(colno++, key);
      if (pstmt.executeUpdate() != 1)
        return null;

      pstmt.close();
      pstmt = null;

      sql = "SELECT LOG_ID FROM USERS WHERE USERNAME=? AND PASSWORD=(SELECT ENCRYPTION.ENCRYPT(?,'A1B390F12D243680','132FD66F5009895C','06F58436588321FF','0123456789ABCDEF') FROM DUAL) AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      colno = 1;
      pstmt.setString(colno++, username);
      pstmt.setString(colno++, password);
      pstmt.setString(colno++, key);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        DataRegister dr = new DataRegister(username, key, 0, null);
        dr.loginId = rset.getInt("LOG_ID");
        return (dr);
      }
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.showError("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return (null);
  } // getUserDetails()

  //-----------------------------------------------------------------------------

  public static boolean isUserExists(DataRegister dr) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    if (dr == null)
      return false;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT COUNT(*) FROM USERS WHERE USERNAME=? AND LOG_ID=? AND KEY=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setString(colno++, dr.username);
      pstmt.setLong(colno++, dr.loginId);
      pstmt.setString(colno++, dr.key);
      rset = pstmt.executeQuery();
      rset.next();
      int count = rset.getInt(1);
      if (count > 0)
        return true;

    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.logInfo("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return false;
  } // isUserExists()

  //-----------------------------------------------------------------------------

  public static long getDeviceId(String thing, String deviceInfo) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT DEVICE_ID FROM DEVICE WHERE THING_ID = (SELECT THING_ID FROM THING WHERE OWNER=?) AND INFO=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      pstmt.setString(1, thing);
      pstmt.setString(2, deviceInfo);
      rset = pstmt.executeQuery();
      if (rset.next()) {
        long deviceId = rset.getLong(1);
        return deviceId;
      }

    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.logInfo("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return -1;
  } // getDeviceId()

  //-----------------------------------------------------------------------------

  public static long addToInbox(String source, String gateway, String data, long mobile) {
    PreparedStatement pstmt = null;
    String sql = null;
    long rowNo = 0;

    Connection cnn = DbConn.getPooledConnection();

    try {
      rowNo = getUniqueRowno("INBOX");

      sql = "INSERT INTO INBOX (ROWNO,GATEWAY,SOURCE,DATA,TIME_STAMP,PROCESS_STATUS,TRANSACTION_ROWNO,MOBILE) VALUES (?,?,?,?,SYSDATE,0,0,?)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, rowNo);
      pstmt.setString(colno++, gateway);
      pstmt.setString(colno++, source);
      pstmt.setString(colno++, data);
      pstmt.setLong(colno++, mobile);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.logInfo("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return rowNo;
  } // addToInbox()

  //-----------------------------------------------------------------------------

  public static long logWebServiceRequest(DataRegister dr, String requestName, String txt) {
    PreparedStatement pstmt = null;
    ResultSet rset = null;
    String sql = null;

    String username = null;
    long rowNo = 0;

    if (dr != null) {
      username = dr.username;
    }

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "SELECT SEQ_WEB_SERVICE_LOG_ROWNO.NEXTVAL FROM DUAL";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      rset = pstmt.executeQuery();
      rset.next();
      rowNo = rset.getLong(1);
      rset.close();
      rset = null;
      pstmt.close();
      pstmt = null;

      sql =
        "INSERT INTO WEB_SERVICE_LOG (ROWNO,TIME_STAMP,USERNAME,REQUEST_NAME,TXT,REQUEST_IP,RESULT_CODE,RESULT_DESC,RESPONSE_TIME_STAMP,RESPONSE_TXT) VALUES (?,SYSDATE,?,?,?,?,NULL,NULL,NULL,NULL)";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      pstmt.setLong(colno++, rowNo);
      if (username == null)
        pstmt.setNull(colno++, java.sql
                                   .Types
                                   .VARCHAR);
      else
        pstmt.setString(colno++, username);
      pstmt.setString(colno++, requestName);
      if (txt.length() > 3500)
        pstmt.setString(colno++, txt.substring(0, 3500));
      else
        pstmt.setString(colno++, txt);
      if (dr == null || dr.remoteIp == null)
        pstmt.setNull(colno++, java.sql
                                   .Types
                                   .VARCHAR);
      else
        pstmt.setString(colno++, dr.remoteIp);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.logInfo("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (rset != null)
          rset.close();
      } catch (Exception e) {
        ;
      }
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }

    return rowNo;
  } // logWebServiceRequest()

  //-----------------------------------------------------------------------------

  public static void logWebServiceResponse(long rowNo, String resultCode, String resultDesc, String responseText) {
    PreparedStatement pstmt = null;
    String sql = null;

    Connection cnn = DbConn.getPooledConnection();

    try {
      sql = "UPDATE WEB_SERVICE_LOG SET RESULT_CODE=?,RESULT_DESC=?,RESPONSE_TIME_STAMP=SYSDATE,RESPONSE_TXT=? WHERE ROWNO=?";
      pstmt = cnn.prepareStatement(sql);
      pstmt.clearParameters();
      int colno = 1;
      if (resultCode == null)
        pstmt.setNull(colno++, java.sql
                                   .Types
                                   .VARCHAR);
      else
        pstmt.setString(colno++, resultCode);
      if (resultDesc == null)
        pstmt.setNull(colno++, java.sql
                                   .Types
                                   .VARCHAR);
      else
        pstmt.setString(colno++, resultDesc);
      if (responseText == null)
        pstmt.setNull(colno++, java.sql
                                   .Types
                                   .VARCHAR);
      else {
        if (responseText.length() > 3500)
          responseText = responseText.substring(0, 3500);
        pstmt.setString(colno++, responseText);
      }
      pstmt.setLong(colno++, rowNo);
      pstmt.executeUpdate();
    } catch (Exception ex) {
      ex.printStackTrace();
      Utils.logInfo("Exception occured : " + ex.getMessage());
    } finally {
      try {
        if (pstmt != null)
          pstmt.close();
      } catch (Exception e) {
        ;
      }
      try {
        cnn.close();
      } catch (Exception e) {
        ;
      }
    }
    return;
  } // logWebServiceResponse()

  //-----------------------------------------------------------------------------

  public static String[] splitString(String splitStr, String delim) {
    StringTokenizer toker;
    String[] result;
    int count, i;

    toker = new StringTokenizer(splitStr, delim);
    count = toker.countTokens();
    result = new String[count];
    for (i = 0; i < count; ++i) {
      try {
        result[i] = toker.nextToken();
      } catch (NoSuchElementException ex) {
        result = null;
        break;
      }

    } // for()

    return result;
  } // splitString()

  public static String timeStampControl(Date timeStamp) {
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");

    if (timeStamp != null) {
      Calendar cal = Calendar.getInstance();
      cal.setTime(new Date());
      cal.add(Calendar.DATE, -120);
      Date beforeThreeDay = cal.getTime();
      cal.setTime(new Date());
      cal.add(Calendar.HOUR, 3);
      Date afterOneHour = cal.getTime();
      if (afterOneHour.after(timeStamp) && beforeThreeDay.before(timeStamp)) {
        return df.format(timeStamp);
      } else {
        return null;
      }
    }
    return null;
  } // timeStampControl()

  public static String timeStampToDate(Long timeStamp) {
    if (timeStamp != null) {
      Timestamp stamp = new Timestamp((timeStamp * 1000));
      Date date = new Date(stamp.getTime());
      return timeStampControl(date);
    }
    return null;
  } // timeStampToDate()
  
  
}
