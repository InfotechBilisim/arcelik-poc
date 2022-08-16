package com.infotech.locationbox.tracking.platform.base;

import java.io.*;
import java.util.*;
import java.text.*;
import java.sql.*;
import java.net.*;

public class Utils extends Object {
  static final String xlatHexTable = "0123456789ABCDEF";
  
  public Utils() {
  }

//-----------------------------------------------------------------------------

  public static String getStringFromBytes(byte[] bytes, int length) {
    String str = "";
    for( int i = 0; i < length; i++ ) {
      str += (char)bytes[i];
    } // for()
    return( str );
  } // getStringFromBytes()

//-----------------------------------------------------------------------------

  public static String stripEscapedString(String txt) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch == '%' ) {
        ch = (char)(convHexStringToNumber(txt.substring(i + 1, i + 1 + 2), 2));
        i += 2;
      }
      else
      if( ch == '+' ) {
        ch = ' ';
      }
      s += ch;
    } // for()
    return s;
  } // stringEscapedString()

//-----------------------------------------------------------------------------

  public static String removeSpaces(String txt) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch == ' ' ) continue;

      s += ch;
    } // for()
    return s;
  } // removeSpaces()

//------------------------------------------------------------------------------

  public static boolean isAllDigits(String txt) {
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch < '0' || ch > '9' ) return false;

    } // for()

    return true;
  } // isAllDigits()

//-----------------------------------------------------------------------------

  public static boolean isAllAscii(String txt) {
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch < ' ' || ch > '~' ) return false;

    } // for()

    return true;
  } // isAllAscii()

//------------------------------------------------------------------------------

  public static String checkNullString(String s) {
    if( s == null ) return( "" ); else return( s );
  } // checkNullString()

//------------------------------------------------------------------------------

  public static String formatNumber(int  num, int  len) {
    String s = "";
    for( int i = 0; i < len; i++ ) s += "0";
    s += num;
    s = s.substring(s.length() - len);
    return( s );
  } // formatNumber()

//------------------------------------------------------------------------------

  public static String formatNumber(long  num, int  len) {
    String s = "";
    for( int i = 0; i < len; i++ ) s += "0";
    s += num;
    s = s.substring(s.length() - len);
    return( s );
  } // formatNumber()

//------------------------------------------------------------------------------

  public static String formatNumber(double num, int  totlen, int len) {
    String s = "";
    for( int i = 0; i < totlen - len; i++ ) s += "0";
    s += num;
    for( int i = 0; i < len; i++ ) s += "0";
    int  pos = s.indexOf('.');
    s = s.substring(pos - (totlen - len), pos + len);
    return( s );
  } // formatNumber()

//------------------------------------------------------------------------------

  public static String formatCoordinate(double coor, int len) {
    int coorNum = (int)(coor * 1000000.0 + 0.5);
    String s = Utils.formatNumber(coorNum, len);
    return( s );
  } // formatCoordinate()

//-----------------------------------------------------------------------------

  public static String formatDateTime(String dt) {
    String s = dt.substring(8, 10) + "." + dt.substring(5, 7) + "." + dt.substring(0, 4) +
               " " + dt.substring(11, 16);
    return( s );
  } // formatDateTime()

//-----------------------------------------------------------------------------

  public static String formatDateTimeForLog(java.util.Date dateTime, String dateDelimiter, String timeDelimiter, String dateTimeSeparator) {
    GregorianCalendar gc = new GregorianCalendar();
    gc.setTime(dateTime);
    String dt = formatNumber(gc.get(Calendar.YEAR), 4) + dateDelimiter +
                formatNumber(gc.get(Calendar.MONTH) + 1, 2) + dateDelimiter +
                formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + dateTimeSeparator +
                formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + timeDelimiter +
                formatNumber(gc.get(Calendar.MINUTE), 2) + timeDelimiter +
                formatNumber(gc.get(Calendar.SECOND), 2);
    return( dt );
  } // formatDateTime()

//------------------------------------------------------------------------------

  public static String padLeftString(String txt, int len, char ch) {
    if( txt == null ) txt = ""; // Just in case
    String s = "";
    for( int i = 0; i < len; i++ ) s += String.valueOf(ch);
    s += txt;
    s = s.substring(s.length() - len);
    return( s );
  } // padLeftString()

//------------------------------------------------------------------------------

  public static String padRightString(String txt, int len, char ch) {
    if( txt == null ) txt = ""; // Just in case
    String s = txt;
    for( int i = 0; i < len - txt.length(); i++ ) s += String.valueOf(ch);
    return( s );
  } // padRightString()

//-----------------------------------------------------------------------------

  public static String getCurrentDate() {
    Calendar c = new GregorianCalendar();
    String dt;

    dt = formatNumber(c.get(Calendar.YEAR), 4) +
         formatNumber(c.get(Calendar.MONTH) + 1, 2) +
         formatNumber(c.get(Calendar.DAY_OF_MONTH), 2);
    return( dt );
  }

//------------------------------------------------------------------------------

  public static String getCurrentTime() {
    GregorianCalendar gc = new GregorianCalendar();
    String dt = formatNumber(gc.get(Calendar.YEAR), 4) +
                formatNumber(gc.get(Calendar.MONTH) + 1, 2) +
                formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) +
                formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) +
                formatNumber(gc.get(Calendar.MINUTE), 2) +
                formatNumber(gc.get(Calendar.SECOND), 2);
    return( dt );
  } // getCurrentTime()

//------------------------------------------------------------------------------

  public static String getCurrentDateTime() {
    GregorianCalendar gc = new GregorianCalendar();
    String dt = formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
                formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
                formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
                formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
                formatNumber(gc.get(Calendar.MINUTE), 2) + ":" +
                formatNumber(gc.get(Calendar.SECOND), 2);
    return( dt );
  } // getCurrentDateTime()

//------------------------------------------------------------------------------

  public static String getCurrentDateTimeWithMinutes(int minutes) {
    GregorianCalendar gc = new GregorianCalendar();
    String dt = Utils.formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
                Utils.formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
                Utils.formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
                Utils.formatNumber((minutes / 60), 2) + ":" +
                Utils.formatNumber((minutes % 60), 2) + ":00";
    return( dt );
  } // getCurrentDateTime()

//------------------------------------------------------------------------------

  public static int getCurrentHour() {
    GregorianCalendar gc = new GregorianCalendar();
    return( gc.get(Calendar.HOUR_OF_DAY) * 100 + gc.get(Calendar.MINUTE) );
  } // getCurrentHour()

//------------------------------------------------------------------------------

  public static int getCurrentMinute() {
    GregorianCalendar gc = new GregorianCalendar();
    return( gc.get(Calendar.HOUR_OF_DAY) * 60 + gc.get(Calendar.MINUTE) );
  } // getCurrentMinute()

//------------------------------------------------------------------------------

  public static int convToHour(String tm) {
    if( tm == null || tm.length() <= 0 ) return -1;
    
    int hh = -1;
    int mm = -1;

    try {
      int pos = tm.indexOf(':');
      if( pos < 0 ) pos = tm.indexOf('.');
      if( pos < 0 && tm.length() == 4 ) {
        hh = Integer.parseInt(tm.substring(0, 2));
        mm = Integer.parseInt(tm.substring(2, 4));
      }
      else {
        if( pos == 1 && tm.length() == 4 ) {
          hh = Integer.parseInt(tm.substring(0, 1));
          mm = Integer.parseInt(tm.substring(2, 4));
        }
        else
        if( pos == 2 && tm.length() == 5 ) {
          hh = Integer.parseInt(tm.substring(0, 2));
          mm = Integer.parseInt(tm.substring(3, 5));
        }
      }
    }
    catch (Exception ex) {
//      ex.printStackTrace();
      return -2;
    }

    if( hh < 0 || mm < 0 ) return -3;
    
    if( hh >= 24 || mm >= 60 ) return -4;
    
    return (hh * 100 + mm);
  } // convToHour()

//------------------------------------------------------------------------------

  public static String convToTime(int hour) {
    String tm = formatNumber((hour / 100), 2) + ":" + formatNumber((hour % 100), 2);
    return tm;
  } // convToHour()

//------------------------------------------------------------------------------

  public static int addMinutesToHour(int hour, int minutes) {
    int hh = hour / 100;
    int mm = hour % 100;
    mm += minutes;
    if( mm >= 60 ) {
      hh++;
      mm -= 60;
    }
    return hh * 100 + mm;
  } // addMinutesToHour()

//------------------------------------------------------------------------------

  public static int getDateTimeHour(String dt) {
    try {
      int hour = Integer.parseInt(dt.substring(11, 13));
      int minute = Integer.parseInt(dt.substring(14, 16));
      return( hour * 100 + minute );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  } // getDateTimeHour()

//------------------------------------------------------------------------------

  public static int getDateTimeMinute(String dt) {
    try {
      int hour = Integer.parseInt(dt.substring(11, 13));
      int minute = Integer.parseInt(dt.substring(14, 16));
      return( hour * 60 + minute );
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return 0;
  } // getDateTimeMinute()

//------------------------------------------------------------------------------

  public static String getCurrentTimeStamp() {
    Timestamp ts = new Timestamp(System.currentTimeMillis());
    return( ts.toString() );
  } // getCurrentTimeStamp()

//-----------------------------------------------------------------------------

  public static String getTimeStampDate(Timestamp ts) {
    String tss = ts.toString();
    String dt = tss.substring(0, 4) +
                tss.substring(5, 7) +
                tss.substring(8, 10) +
                tss.substring(11, 13) +
                tss.substring(14, 16) +
                tss.substring(17, 19);
    return( dt );
  } // getTimeStampDate()

//------------------------------------------------------------------------------

  public static String toDatetime(java.util.Date dt) {
    String tarih = null;
    if( dt == null )
      tarih = "";
    else {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      tarih = sdf.format(dt);
    }
    return tarih;
  } // toDatetime()

//------------------------------------------------------------------------------

  public static int timeDifference(String endDate, String startDate) {
    try {
      if( endDate.length() > 19 ) endDate = endDate.substring(0, 19);
      if( startDate.length() > 19 ) startDate = startDate.substring(0, 19);
  
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      java.util.Date dtEnd = sdf.parse(endDate);
      java.util.Date dtStart = sdf.parse(startDate);
      return (int)((dtEnd.getTime() - dtStart.getTime()) / 1000);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return 0;
  } // timeDifference()

//------------------------------------------------------------------------------

  public static String calculateDateTime(String datetime, int minuteOffset) {
    int year   = Integer.parseInt(datetime.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(datetime.substring(5, 7));
    if( month == 0 ) month = 1;
    int day    = Integer.parseInt(datetime.substring(8, 10));
    if( day == 0 ) day = 1;
    int hour   = 0;
    int minute = 0;
    if( datetime.length() >= 16 ) {
      hour   = Integer.parseInt(datetime.substring(11, 13));
      minute = Integer.parseInt(datetime.substring(14, 16));
    }
    int second = 0;
    if( datetime.length() >= 19 ) {
      second = Integer.parseInt(datetime.substring(17, 19));
    }
    GregorianCalendar gc = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
    gc.add(Calendar.MINUTE, minuteOffset);
    String dt =  formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
                 formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
                 formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
                 formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
                 formatNumber(gc.get(Calendar.MINUTE), 2) +  ":" +
                 formatNumber(gc.get(Calendar.SECOND), 2);
    return dt;
  } // calculateDateTime()

//------------------------------------------------------------------------------

  public static int getMinutes(String dt) {
    int hh  = Integer.parseInt(dt.substring(11, 13));
    int mm  = Integer.parseInt(dt.substring(14, 16));
    return (hh * 60) + mm;
  } // getMinutes()
  
//------------------------------------------------------------------------------

  public static int[] getDayInfo(String dt) {
    int year   = Integer.parseInt(dt.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(dt.substring(5, 7));
    if( month == 0 ) month = 1;
    int day    = Integer.parseInt(dt.substring(8, 10));
    if( day == 0 ) day = 1;
    Calendar c = new GregorianCalendar(year, (month - 1), day);
    int dow = c.get(Calendar.DAY_OF_WEEK);

    int[] dayInfo = new int[4];
    dayInfo[0] = (dow >= Calendar.MONDAY && dow <= Calendar.FRIDAY ? 1 : 0);
    dayInfo[1] = (dow == Calendar.SATURDAY ? 1 : 0);
    dayInfo[2] = (dow == Calendar.SUNDAY ? 1 : 0);
    dayInfo[3] = 0; // Bayram
    return dayInfo;
  } // getDayInfo()

//------------------------------------------------------------------------------

  public static int getCurrentWeekDay() {
    Calendar c = new GregorianCalendar();
    int weekDay = c.get(Calendar.DAY_OF_WEEK);
    return weekDay;
  } // getCurrentWeekDay()

//------------------------------------------------------------------------------

  public static int getCurrentMonthDay() {
    Calendar c = new GregorianCalendar();
    int monthDay = c.get(Calendar.DAY_OF_MONTH);
    return monthDay;
  } // getCurrentMonthDay()

//------------------------------------------------------------------------------

  public static Timestamp toTimestamp(String datetime) throws Exception {
    int year   = Integer.parseInt(datetime.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(datetime.substring(5, 7));
    if( month == 0 ) month = 1;
    int day    = Integer.parseInt(datetime.substring(8, 10));
    if( day == 0 ) day = 1;
    int hour   = Integer.parseInt(datetime.substring(11, 13));
    int minute = Integer.parseInt(datetime.substring(14, 16));
    int second = 0;
    if( datetime.length() >= 19 ) second = Integer.parseInt(datetime.substring(17, 19));
    Calendar c = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
    return( new Timestamp( c.getTime().getTime() ) );
  } // toTimestamp()

//------------------------------------------------------------------------------

  public static java.sql.Date toSqlDate(String datetime) throws Exception {
    int year   = Integer.parseInt(datetime.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(datetime.substring(5, 7));
    if( month == 0 ) month = 1;
    int day    = Integer.parseInt(datetime.substring(8, 10));
    if( day == 0 ) day = 1;
    int hour   = Integer.parseInt(datetime.substring(11, 13));
    int minute = Integer.parseInt(datetime.substring(14, 16));
    int second = 0;
    if( datetime.length() >= 19 ) second = Integer.parseInt(datetime.substring(17, 19));
    Calendar c = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
    return( new java.sql.Date( c.getTime().getTime() ) );
  } // toSqlDate()

//-----------------------------------------------------------------------------

  public static String toDatetime(String dt) {
    String s = "";
    s += dt.substring(0, 4) + "-" + dt.substring(4, 6) + "-" + dt.substring(6, 8);
    s += " ";
    s += dt.substring(8, 10) + ":" + dt.substring(10, 12) + ":" + dt.substring(12, 14);
    return s;
  } // toDatetime()

//------------------------------------------------------------------------------

  public static String toDate_YYYYMMDD(String datetime) {
    return( datetime.substring(0, 4) + datetime.substring(5, 7) + datetime.substring(8, 10) );
  } // toDate_YYYYMMDD()

//------------------------------------------------------------------------------

  public static String timeCorrection(String datetime, int hourOffset) throws Exception {
    int year   = Integer.parseInt(datetime.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(datetime.substring(5, 7));
    if( month == 0 ) month = 1;
    int day    = Integer.parseInt(datetime.substring(8, 10));
    if( day == 0 ) day = 1;
    int hour   = Integer.parseInt(datetime.substring(11, 13));
    int minute = Integer.parseInt(datetime.substring(14, 16));
    int second = 0;
    if( datetime.length() >= 19 ) second = Integer.parseInt(datetime.substring(17, 19));
    GregorianCalendar gc = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
    gc.add(Calendar.HOUR_OF_DAY, hourOffset);
    String dt = formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
                formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
                formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
                formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
                formatNumber(gc.get(Calendar.MINUTE), 2) + ":" +
                formatNumber(gc.get(Calendar.SECOND), 2);
    return( dt );
  } // timeCorrection()

//------------------------------------------------------------------------------

  public static String timeAddDeltaDurationInMinutes(String datetime, int deltaDuration) {
    String dt = null;

    try {
      int year   = Integer.parseInt(datetime.substring(0, 4));
      if( year == 0 ) year = 2000;
      int month  = Integer.parseInt(datetime.substring(5, 7));
      if( month == 0 ) month = 1;
      int day    = Integer.parseInt(datetime.substring(8, 10));
      if( day == 0 ) day = 1;
      int hour   = Integer.parseInt(datetime.substring(11, 13));
      int minute = Integer.parseInt(datetime.substring(14, 16));
      GregorianCalendar gc = new GregorianCalendar(year, (month - 1), day, hour, minute);
      gc.add(Calendar.MINUTE, deltaDuration);
      dt = formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
           formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
           formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
           formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
           formatNumber(gc.get(Calendar.MINUTE), 2) + ":" +
           formatNumber(gc.get(Calendar.SECOND), 2);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return( dt );
  } // timeAddDeltaDurationInMinutes()

//------------------------------------------------------------------------------

  public static String timeAddDeltaDurationInSeconds(String datetime, int deltaDuration) {
    String dt = null;

    try {
      int year   = Integer.parseInt(datetime.substring(0, 4));
      if( year == 0 ) year = 2000;
      int month  = Integer.parseInt(datetime.substring(5, 7));
      if( month == 0 ) month = 1;
      int day    = Integer.parseInt(datetime.substring(8, 10));
      if( day == 0 ) day = 1;
      int hour   = Integer.parseInt(datetime.substring(11, 13));
      int minute = Integer.parseInt(datetime.substring(14, 16));
      int second = Integer.parseInt(datetime.substring(17, 19));
      GregorianCalendar gc = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
      gc.add(Calendar.SECOND, deltaDuration);
      dt = formatNumber(gc.get(Calendar.YEAR), 4) + "-" +
           formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" +
           formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " +
           formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
           formatNumber(gc.get(Calendar.MINUTE), 2) + ":" +
           formatNumber(gc.get(Calendar.SECOND), 2);
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    return( dt );
  } // timeAddDeltaDurationInSeconds()

//------------------------------------------------------------------------------

  public static GregorianCalendar getGregorianCalendarFromString(String datetime) throws Exception {
    int year   = Integer.parseInt(datetime.substring(0, 4));
    if( year == 0 ) year = 2000;
    int month  = Integer.parseInt(datetime.substring(5, 7));
    int day    = Integer.parseInt(datetime.substring(8, 10));
    if( day == 0 ) day = 1;
    int hour   = Integer.parseInt(datetime.substring(11, 13));
    int minute = Integer.parseInt(datetime.substring(14, 16));
    int second = Integer.parseInt(datetime.substring(17, 19));
    GregorianCalendar gc = new GregorianCalendar(year, month, day, hour, minute, second);
    return( gc );
  } // getGregorianCalendarFromString()

//------------------------------------------------------------------------------

  public static long calculateTimeDifference(String datetime1, String datetime2) throws Exception {
    GregorianCalendar gc1 = Utils.getGregorianCalendarFromString(datetime1);
    GregorianCalendar gc2 = Utils.getGregorianCalendarFromString(datetime2);
    long difference = gc1.getTime().getTime() - gc2.getTime().getTime();
    difference /= 1000L;
    return( difference );
  } // calculateTimeDifference()

//------------------------------------------------------------------------------

  public static String mergeDateTime(String dat, String tim) {
    if( tim.indexOf('-') >= 0 )
      return dat.substring(0, 11) + tim.substring(11, 19);
    else
      return dat.substring(0, 11) + tim.substring(0, 8);
  } // mergeDateTime()

//------------------------------------------------------------------------------

  public static int checkTimes(int checkType, String startTime, String endTime, String currTime) {
    if( checkType == 0 || startTime == null || endTime == null ) return 1;
    
    try {
      int start = Integer.parseInt(startTime.substring(11, 13) + startTime.substring(14, 16));
      int end = Integer.parseInt(endTime.substring(11, 13) + endTime.substring(14, 16));
      int curr = Integer.parseInt(currTime.substring(11, 13) + currTime.substring(14, 16));
  
      boolean inbetween = false;
      if( start <= end ) {
        if( start <= curr && curr <= end ) inbetween = true;
      }
      else {
        if( start <= curr || curr <= end ) inbetween = true;
      }
      
      if( checkType == 1 && inbetween ) return 2;
  
      if( checkType == 2 && !inbetween ) return 2;
  
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
    
    return 1;
  } // checkTimes()

//------------------------------------------------------------------------------

  public static char xlatHexFromTable(int inx) {
    char ch;

    if( inx >= xlatHexTable.length() ) inx = 0;
    ch = xlatHexTable.charAt(inx);
    return( ch );
  } // xlatHexFromTable()

  public static int getXlatHexIndex(char ch) {
    int inx;

    inx = xlatHexTable.indexOf(ch);
    if( inx < 0 ) inx = 0;
    return( inx );
  } // getXlatHexIndex()

//------------------------------------------------------------------------------

  public static String convNumberToHexString(long number, int length) {
    String str = "";

    for( ; length > 0; length-- ) {
      int inx = (int)((number % 16) & 0x0F);
      str = xlatHexFromTable(inx) + str;
      number /= 16;
    } // for()
    return( str );
  } // convNumberToHexString()

  public static long convHexStringToNumber(String str, int length) {
    long number = 0;
    if( str == null || str.length() < length ) return number;

    for( int i = 0; i < length; i++ ) {
      char ch = str.charAt(i);
      if( ch == 0 ) break;

      number = number * 16 + getXlatHexIndex(ch);
    } // for()
    
    return number;
  } // convHexStringToNumber()

//-----------------------------------------------------------------------------

  public static String downloadResponse_WGet(String mapUrl) {
    InputStream inp = null;
    OutputStream out = null;
    String strResult = null;

    Log.showText("Request : " + mapUrl);

    try {
      URL url = new URL(mapUrl);
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("GET");
      http.setRequestProperty("Content-type", "text/xml");
      http.setDoOutput(false);
      http.setDoInput(true);
      http.connect();

      inp = http.getInputStream();
      int code = http.getResponseCode();

      strResult = "";
      byte b[] = new byte[10240];
      while( true ) {
        int length = inp.read(b, 0, b.length);
        if( length < 0 ) break;

        strResult += new String(b, 0, length);
      } // while()
      strResult = strResult.trim();
      inp.close();
      http.disconnect();
    }
    catch (Exception ex) {
      Log.showError("URL: " + mapUrl);
      ex.printStackTrace();
      try { inp.close(); } catch (Exception e) {;}
    }

    return strResult;
  } // downloadResponse_WGet()

//-----------------------------------------------------------------------------

  public static String downloadResponse_WPost(String postUrl, String postData) {
    InputStream inp = null;
    OutputStream out = null;
    String strResult = null;

    Log.showText("Request : " + postUrl);
    Log.showText("Request Data : " + postData);

    try {
      URL url = new URL(postUrl);
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("POST");
//      http.setRequestProperty("Content-type", "text/xml");
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
      while( true ) {
        int length = inp.read(b, 0, b.length);
        if( length < 0 ) break;

        strResult += new String(b, 0, length);
      } // while()
      strResult = strResult.trim();
      inp.close();
      http.disconnect();
    }
    catch (Exception ex) {
      Log.showError("Exception Post Data : " + postData);
      ex.printStackTrace();
      try { out.close(); } catch (Exception e) {;}
      try { inp.close(); } catch (Exception e) {;}
    }

    return strResult;
  } // downloadResponse_WPost()

//-----------------------------------------------------------------------------

  public static String downloadResponse(String mapUrl) {
    InputStream inp = null;
    OutputStream out = null;
    String strResult = null;

    Log.showText("Request : " + mapUrl);

    String urlHead = mapUrl;
    String urlData = "";

    int pos = mapUrl.indexOf('?');
    if( pos >= 0 ) {
      urlHead = mapUrl.substring(0, pos);
      urlData = mapUrl.substring(pos + 1);
    }

    try {
      URL url = new URL(urlHead);
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("POST");
//      http.setRequestProperty("Content-type", "text/xml");
      http.setDoOutput(true);
      http.setDoInput(true);
      http.connect();

      out = http.getOutputStream();
      out.write(urlData.getBytes());
      out.flush();
      out.close();

      inp = http.getInputStream();
      int code = http.getResponseCode();

      strResult = "";
      byte b[] = new byte[10240];
      while( true ) {
        int length = inp.read(b, 0, b.length);
        if( length < 0 ) break;

        strResult += new String(b, 0, length);
      } // while()
      strResult = strResult.trim();
      inp.close();
      http.disconnect();
    }
    catch (Exception ex) {
      Log.showError("URL: " + mapUrl);
      ex.printStackTrace();
      try { out.close(); } catch (Exception e) {;}
      try { inp.close(); } catch (Exception e) {;}
    }

    return strResult;
  } // downloadResponse()

//-----------------------------------------------------------------------------

  public static boolean downloadAndSaveFile(String fileUrl, String fileName) {
    Log.showText("Download from : " + fileUrl);
    Log.showText("Save into : " + fileName);
    try {
      URL url = new URL(fileUrl);
      HttpURLConnection http = (HttpURLConnection)url.openConnection();
      http.setRequestMethod("GET");
      http.setRequestProperty("Content-type", "image/gif");
      http.setDoOutput(false);
      http.setDoInput(true);
      http.setConnectTimeout(120000);
      http.setReadTimeout(600000);
      http.connect();

      FileOutputStream out = new FileOutputStream( new File(fileName) );
      InputStream inp = http.getInputStream();
      int code = http.getResponseCode();
      byte b[] = new byte[10240];
      while( true ) {
        int length = inp.read(b, 0, b.length);
        if( length < 0 ) break;

        out.write(b, 0, length);
      } // while()
      http.disconnect();
      out.close();
    }
    catch (Exception ex) {
      Log.showError("Failed download from : " + fileUrl);
      Log.showError("Failed save into : " + fileName);
      ex.printStackTrace();
      return false;
    }

    return true;
  } // downloadAndSaveFile()

//-----------------------------------------------------------------------------

  public static String convStr2Xml(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x03C : s += "&lt;"; break;
      case 0x03E : s += "&gt;"; break;
      case 0x026 : s += "&amp;"; break;
      case 0x027 : s += "&apos;"; break;
      case 0x022 : s += "&quot;"; break;
      default :
        s += ch;
        break;
      } // switch()
    } // for()
    return( s );
  } // convStr2Xml()

//-----------------------------------------------------------------------------

  public static String convStr2Json(String txt) {
    if( txt == null ) return "";
    
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      if( ch == 9 ) s += "\t";
      else
      if( ch == '\\' ) s += "\\\\";
      else
      if( ch == '"' ) s += "\\\"";
      else
      if( ch >= 0x21 && ch <= 0x2f ) s += "\\u" + Utils.convNumberToHexString((long)ch, 4);
      else
      if( ch >= 0x3a && ch <= 0x3f ) s += "\\u" + Utils.convNumberToHexString((long)ch, 4);
      else
      if( ch >= 0x5b && ch <= 0x5f ) s += "\\u" + Utils.convNumberToHexString((long)ch, 4);
      else
      if( ch >= 0x7b && ch <= 0x7f ) s += "\\u" + Utils.convNumberToHexString((long)ch, 4);
      else
        s += (char)ch;
    } // for()
    return s.replaceAll("\n", "\\\\n");
  } // convStr2Json()

//-----------------------------------------------------------------------------

  public static String convToAscii(String txt) {
    if( txt == null ) return null;

    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x07E : s += "~~"; break;
      case 0x020 : s += "~b"; break;
      case 0x03A : s += "~e"; break;
      case 0x0C7 : s += "~C"; break;
      case 0x0E7 : s += "~c"; break;
      case 0x11E : s += "~G"; break;
      case 0x11F : s += "~g"; break;
      case 0x0D0 : s += "~G"; break;
      case 0x0F0 : s += "~g"; break;
      case 0x15E : s += "~S"; break;
      case 0x15F : s += "~s"; break;
      case 0x0DE : s += "~S"; break;
      case 0x0FE : s += "~s"; break;
      case 0x0EE : s += "~s"; break;
      case 0x130 : s += "~I"; break;
      case 0x131 : s += "~i"; break;
      case 0x0DD : s += "~I"; break;
      case 0x0FD : s += "~i"; break;
      case 0x0DC : s += "~U"; break;
      case 0x0FC : s += "~u"; break;
      case 0x0D6 : s += "~O"; break;
      case 0x0F6 : s += "~o"; break;
      default :
        s += ch;
        break;
      } // switch()
    } // for()
    return( s );
  } // convToAscii()

//-----------------------------------------------------------------------------

  public static String getTurkishString(String txt) {
    if( txt == null ) return null;

    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch == '~' ) {
        i++;
        ch = txt.charAt(i);
        switch( ch ) {
        case '~' : ch = '~'; break;
        case 'b' : ch = ' '; break;
        case 'e' : ch = ':'; break;
        case 'C' : ch = (char)0x0C7; break;
        case 'c' : ch = (char)0x0E7; break;
        case 'G' : ch = (char)0x11E; break;
        case 'g' : ch = (char)0x11F; break;
        case 'S' : ch = (char)0x15E; break;
        case 's' : ch = (char)0x15F; break;
        case 'I' : ch = (char)0x130; break;
        case 'i' : ch = (char)0x131; break;
        case 'U' : ch = (char)0x0DC; break;
        case 'u' : ch = (char)0x0FC; break;
        case 'O' : ch = (char)0x0D6; break;
        case 'o' : ch = (char)0x0F6; break;
        } // switch()
      } // if()
      s += ch;
    } // for()
    return( s );
  } // getTurkishString()

//-----------------------------------------------------------------------------

  public static String convTrk2Eng(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x0C7 : ch = 'C'; break;
      case 0x0E7 : ch = 'c'; break;
      case 0x11E : ch = 'G'; break;
      case 0x11F : ch = 'g'; break;
      case 0x15E : ch = 'S'; break;
      case 0x15F : ch = 's'; break;
      case 0x130 : ch = 'I'; break;
      case 0x131 : ch = 'i'; break;
      case 0x0DC : ch = 'U'; break;
      case 0x0FC : ch = 'u'; break;
      case 0x0D6 : ch = 'O'; break;
      case 0x0F6 : ch = 'o'; break;
      default :
        break;
      } // switch()
      s += ch;
    } // for()
    return( s );
  } // convTrk2Eng()

//-----------------------------------------------------------------------------

  public static String convToUpperEnglishChars(String txt, boolean removeSpaces) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      switch( ch ) {
      case 0x0C7 : ch  = 'C'; break;
      case 0x0E7 : ch  = 'C'; break;
      case 0x15E : ch  = 'S'; break;
      case 0x15F : ch  = 'S'; break;
      case 0x0DE : ch  = 'S'; break;
      case 0x0FE : ch  = 'S'; break;
      case 0x11E : ch  = 'G'; break;
      case 0x11F : ch  = 'G'; break;
      case 0x0D0 : ch  = 'G'; break;
      case 0x0F0 : ch  = 'G'; break;
      case 0x130 : ch  = 'I'; break;
      case 0x131 : ch  = 'I'; break;
      case 0x0DD : ch  = 'I'; break;
      case 0x0FD : ch  = 'I'; break;
      case 0x0DC : ch  = 'U'; break;
      case 0x0FC : ch  = 'U'; break;
      case 0x0D6 : ch  = 'O'; break;
      case 0x0F6 : ch  = 'O'; break;
      default :
        if( ch >= 'a' && ch <= 'z' )  ch &= 0xDF;
        break;
      } // switch()

      if( removeSpaces && ch == ' ' ) continue;
      if( removeSpaces && ch == '.' ) continue;
      if( removeSpaces && ch == ',' ) continue;

      s += (char)ch;
    } // for()
    return s;
  } // convToUpperEnglishChars()

//-----------------------------------------------------------------------------

  public static String convToUpperEng(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x0C7 : ch = 'C'; break;
      case 0x0E7 : ch = 'C'; break;
      case 0x11E : ch = 'G'; break;
      case 0x11F : ch = 'G'; break;
      case 0x15E : ch = 'S'; break;
      case 0x15F : ch = 'S'; break;
      case 0x130 : ch = 'I'; break;
      case 0x131 : ch = 'I'; break;
      case 0x069 : ch = 'I'; break;
      case 0x0DC : ch = 'U'; break;
      case 0x0FC : ch = 'U'; break;
      case 0x0D6 : ch = 'O'; break;
      case 0x0F6 : ch = 'O'; break;
      case   '&' :
        int pos = txt.indexOf(';', i);
        if( pos >= 0 ) {
          String sx = txt.substring(i, pos + 1);
          if( sx.equalsIgnoreCase("&#xC7;") || sx.equalsIgnoreCase("&#xE7;") ) ch = 'C';
          else
          if( sx.equalsIgnoreCase("&#x11E;") || sx.equalsIgnoreCase("&#x11F;") ) ch = 'G';
          else
          if( sx.equalsIgnoreCase("&#x15E;") || sx.equalsIgnoreCase("&#x15F;") ) ch = 'S';
          else
          if( sx.equalsIgnoreCase("&#x130;") || sx.equalsIgnoreCase("&#x131;") ) ch = 'I';
          else
          if( sx.equalsIgnoreCase("&#xDC;") || sx.equalsIgnoreCase("&#xFC;") ) ch = 'U';
          else
          if( sx.equalsIgnoreCase("&#xD6;") || sx.equalsIgnoreCase("&#xF6;") ) ch = 'O';
          else
            ;
          i = pos; // Correct the index
        }
        break;
      default :
        ch = Character.toUpperCase(ch);
        break;
      } // switch()
      s += ch;
    } // for()

    return( s );
  } // convToUpperEng()

//-----------------------------------------------------------------------------

  public static String convToLowerEng(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x0C7 : ch = 'c'; break;
      case 0x0E7 : ch = 'c'; break;
      case 0x11E : ch = 'g'; break;
      case 0x11F : ch = 'g'; break;
      case 0x15E : ch = 's'; break;
      case 0x15F : ch = 's'; break;
      case 0x130 : ch = 'i'; break;
      case 0x131 : ch = 'i'; break;
      case 0x069 : ch = 'i'; break;
      case 0x0DC : ch = 'u'; break;
      case 0x0FC : ch = 'u'; break;
      case 0x0D6 : ch = 'o'; break;
      case 0x0F6 : ch = 'o'; break;
      case   '&' :
        int pos = txt.indexOf(';', i);
        if( pos >= 0 ) {
          String sx = txt.substring(i, pos + 1);
          if( sx.equalsIgnoreCase("&#xC7;") || sx.equalsIgnoreCase("&#xE7;") ) ch = 'c';
          else
          if( sx.equalsIgnoreCase("&#x11E;") || sx.equalsIgnoreCase("&#x11F;") ) ch = 'g';
          else
          if( sx.equalsIgnoreCase("&#x15E;") || sx.equalsIgnoreCase("&#x15F;") ) ch = 's';
          else
          if( sx.equalsIgnoreCase("&#x130;") || sx.equalsIgnoreCase("&#x131;") ) ch = 'i';
          else
          if( sx.equalsIgnoreCase("&#xDC;") || sx.equalsIgnoreCase("&#xFC;") ) ch = 'u';
          else
          if( sx.equalsIgnoreCase("&#xD6;") || sx.equalsIgnoreCase("&#xF6;") ) ch = 'o';
          else
            ;
          i = pos; // Correct the index
        }
        break;
      default :
        ch = Character.toLowerCase(ch);
        break;
      } // switch()
      s += ch;
    } // for()

    return( s );
  } // convToLowerEng()

//-----------------------------------------------------------------------------

  public static String convTrk2Eng_Url(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x0C7 : s += 'C'; break;
      case 0x0E7 : s += 'c'; break;
      case 0x11E : s += 'G'; break;
      case 0x11F : s += 'g'; break;
      case 0x15E : s += 'S'; break;
      case 0x15F : s += 's'; break;
      case 0x130 : s += 'I'; break;
      case 0x131 : s += 'i'; break;
      case 0x0DC : s += 'U'; break;
      case 0x0FC : s += 'u'; break;
      case 0x0D6 : s += 'O'; break;
      case 0x0F6 : s += 'o'; break;
      case 0x020 : s += "%20"; break;
      case 0x025 : s += "%25"; break;
      case 0x07C : s += "%7C"; break;
      case 0x023 : s += "%23"; break;
      case 0x026 : s += "%26"; break;
      case 0x03F : s += "%3F"; break;
      case 0x040 : s += "%40"; break;
      case 0x05F : s += "%5F"; break;
      case 0x02B : s += "%2B"; break;
      case 0x009 : s += "%09"; break;
      case 0x027 : s += "%27"; break;
      default :
        s += ch;
        break;
      } // switch()
    } // for()
    return( s );
  } // convTrk2Eng_Url()

//-----------------------------------------------------------------------------

  public static String convStr2Url(String txt) {
    String s = "";

    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      switch( ch ) {
      case 0x020 : s += "%20"; break;
      case 0x025 : s += "%25"; break;
      case 0x07C : s += "%7C"; break;
      case 0x023 : s += "%23"; break;
      case 0x026 : s += "%26"; break;
      case 0x03F : s += "%3F"; break;
      case 0x040 : s += "%40"; break;
      case 0x05F : s += "%5F"; break;
      case 0x02B : s += "%2B"; break;
      case 0x009 : s += "%09"; break;
      case 0x027 : s += "%27"; break;
      default :
        s += ch;
        break;
      } // switch()
    } // for()
    return( s );
  } // convStr2Url()

//-----------------------------------------------------------------------------

  public static String convStr2UrlEncoded(String txt) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      int ch1 = ((ch >> 8) & 0xff);
      int ch2 = ((ch) & 0xff);
      s += "%" + Utils.convNumberToHexString((long)ch1, 2);
      s += "%" + Utils.convNumberToHexString((long)ch2, 2);
    } // for()
    return s;
  } // convStr2UrlEncoded()

//-----------------------------------------------------------------------------

  public static String convName2Keyword(String name) {
    String s = "";
    for( int i = 0; i < name.length(); i++ ) {
      int ch = name.charAt(i);
      switch( ch ) {
      case 0x0C7 : ch  = 'C'; break;
      case 0x0E7 : ch  = 'C'; break;
      case 0x15E : ch  = 'S'; break;
      case 0x15F : ch  = 'S'; break;
      case 0x0DE : ch  = 'S'; break;
      case 0x0FE : ch  = 'S'; break;
      case 0x11E : ch  = 'G'; break;
      case 0x11F : ch  = 'G'; break;
      case 0x0D0 : ch  = 'G'; break;
      case 0x0F0 : ch  = 'G'; break;
      case 0x130 : ch  = 'I'; break;
      case 0x131 : ch  = 'I'; break;
      case 0x0DD : ch  = 'I'; break;
      case 0x0FD : ch  = 'I'; break;
      case 0x0DC : ch  = 'U'; break;
      case 0x0FC : ch  = 'U'; break;
      case 0x0D6 : ch  = 'O'; break;
      case 0x0F6 : ch  = 'O'; break;
      default :
        if( ch >= 'a' && ch <= 'z' )  ch &= 0xDF;
        break;
      } // switch()

      if( ch == ' ' ) continue;
      if( ch == '.' ) continue;
      if( ch == ',' ) continue;

      s += (char)ch;
    } // for()
    return s;
  } // convName2Keyword()

//-----------------------------------------------------------------------------

  public static boolean isDateTimeValid(String dt) {
    try {
      int yyyy = Integer.parseInt(dt.substring(0, 4));
      if( yyyy > 2099 ) return false;
      
      int mm = Integer.parseInt(dt.substring(5, 7));
      if( mm > 12 ) return false;
      
      int dd = Integer.parseInt(dt.substring(8, 10));
      if( dd > 31 ) return false;
      
      int hh = Integer.parseInt(dt.substring(11, 13));
      if( hh > 23 ) return false;
      
      int mi = Integer.parseInt(dt.substring(14, 16));
      if( mi > 59 ) return false;
      
      if( dt.length() >= 19 ) {
        int ss = Integer.parseInt(dt.substring(17, 19));
        if( ss > 59 ) return false;
        
      }
    }
    catch (Exception ex) {
      return false;
    }

    return true;
  } // isDateTimeValid()
  
//-----------------------------------------------------------------------------

  public static boolean isLatitudeValid(double latitude) {
    if( latitude < -90.0 || latitude > 90.0 ) return false;
    
    return true;
  } // isLatitudeValid()
  
//-----------------------------------------------------------------------------

  public static boolean isLongitudeValid(double longitude) {
    if( longitude < -180.0 || longitude > 180.0 ) return false;
    
    return true;
  } // isLongitudeValid()
  
//-----------------------------------------------------------------------------

  public static boolean isPrintable(String str) {
    for( int i = 0; i < str.length(); i++ ) {
      char ch = str.charAt(i);
      if( ch < ' ' ) return false;

    } // for()

    return true;
  } // isPrintable()

//-----------------------------------------------------------------------------

  public static String filterNonPrintables(String txt) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      char ch = txt.charAt(i);
      if( ch < ' ' ) ch = '|';
      s += ch;
    } // for()
    return s;
  } // filterNonPrintables()

//-----------------------------------------------------------------------------

  public static long convBinStringToNumber(String str, int length) {
    long number = 0;

    for( int i = 0; i < length; i++ ) {
      int ch = (int)str.charAt(i);
      number = number * 256 + (ch & 0xff);
    } // for()
    return( number );
  } // convBinStringToNumber()

//-----------------------------------------------------------------------------

  public static String replaceParameter(String txt, String value) {
    return txt.replaceAll("\\$\\{1\\}", escapeDollar(value));
  } // replaceParameter()

//-----------------------------------------------------------------------------

  public static String replaceParameter(String txt, String value1, String value2) {
    return txt.replaceAll("\\$\\{1\\}", escapeDollar(value1)).replaceAll("\\$\\{2\\}", escapeDollar(value2));
  } // replaceParameter()

//-----------------------------------------------------------------------------

  public static String replaceParameter(String txt, String value1, String value2, String value3) {
    return txt.replaceAll("\\$\\{1\\}", escapeDollar(value1)).replaceAll("\\$\\{2\\}", escapeDollar(value2)).replaceAll("\\$\\{3\\}", escapeDollar(value3));
  } // replaceParameter()

//-----------------------------------------------------------------------------

  public static String replaceParameter(String txt, String value1, String value2, String value3, String value4) {
    return txt.replaceAll("\\$\\{1\\}", escapeDollar(value1)).replaceAll("\\$\\{2\\}", escapeDollar(value2)).replaceAll("\\$\\{3\\}", escapeDollar(value3)).replaceAll("\\$\\{4\\}", escapeDollar(value4));
  } // replaceParameter()

//-----------------------------------------------------------------------------

  public static String replaceParameter(String txt, String value1, String value2, String value3, String value4, String value5) {
    return txt.replaceAll("\\$\\{1\\}", escapeDollar(value1)).replaceAll("\\$\\{2\\}", escapeDollar(value2)).replaceAll("\\$\\{3\\}", escapeDollar(value3)).replaceAll("\\$\\{4\\}", escapeDollar(value4)).replaceAll("\\$\\{5\\}", escapeDollar(value5));
  } // replaceParameter()

//-----------------------------------------------------------------------------

  private static String escapeDollar(String txt) {
    String s = "";
    for( int i = 0; i < txt.length(); i++ ) {
      int ch = txt.charAt(i);
      if( ch == '$' ) s += '\\';
       s += (char)ch;
    } // for()
    return s;
  } // escapeDollar()

//-----------------------------------------------------------------------------

  public static String[] splitString(String splitStr, String delim) {
    StringTokenizer toker;
    String[] result;
    int count, i;

    toker = new StringTokenizer(splitStr, delim);
    count = toker.countTokens();
    result = new String[count];
    for( i = 0 ; i < count ; ++i ) {
      try {
        result[i] = toker.nextToken();
      }
      catch (NoSuchElementException ex) {
        result = null;
        break;
      }

    } // for()

    return result;
  } // splitString()

}
