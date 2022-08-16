package com.infotech.locationbox.servlet;

import com.infotech.address.cleaner.DataAddress;

import java.awt.geom.Point2D;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;

import java.net.HttpURLConnection;
import java.net.URL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import java.text.DecimalFormat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import oracle.spatial.geometry.JGeometry;

import oracle.sql.STRUCT;

import org.json.JSONArray;
import org.json.JSONObject;

public class Utils {
    private static final String xlatTable = "0123456789AaBbCcÇçDdEeFfGgĞğHhIıİiJjKkLlMmNnOoÖöPpQqRrSsŞşTtUuÜüVvWwXxYyZz";
    private static String paramFile = "locationboxservlet_params";
    private static Properties paramValues = null;
    private static Hashtable htable = null;

    public static final int BUFFER_SIZE = 2048;

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


    public static String getPropertiesFilePath(String file) {
        String path = null;
        try {
            // Loads a ResourceBundle and creates Properties from it
            ResourceBundle bundle = ResourceBundle.getBundle(file);
            // Retrieve the keys and populate the properties object
            Enumeration enu = bundle.getKeys();
            String key = null;
            while (enu.hasMoreElements()) {
                key = (String) enu.nextElement();
                Object value = bundle.getObject(key);
                if (key.equals("properties_path_file") && !(value == null || value.equals(""))) {
                    path = value.toString();
                    break;
                }
            } // while()
        } catch (Exception ex) {
            System.out.println(ex.getStackTrace());
        }

        return path;
    } // getPropertiesFilePath()
    
    //-----------------------------------------------------------------------------

    public static Properties loadParams(String file) {
        Properties prop = null;
        try {
            String path = getPropertiesFilePath(file);
            prop = new Properties();
            if (path != null && path.length() > 2) {//properties dosyasını dışardan okumak
                file = path + "/"+ file + ".properties";
                prop.load(new FileInputStream(file));
            } else {
                // Loads a ResourceBundle and creates Properties from it
                ResourceBundle bundle = ResourceBundle.getBundle(file);

                // Retrieve the keys and populate the properties object
                Enumeration enu = bundle.getKeys();
                String key = null;
                while (enu.hasMoreElements()) {
                    key = (String) enu.nextElement();
                    prop.put(key, bundle.getObject(key));
                } // while()
            }
          
        } catch (Exception ex) {
            com.infotech.address.cleaner.Utils.showError("Utils.loadParams Error : " + ex.getMessage());
            ex.printStackTrace();
            return null;
        }

      return prop;
    } // loadParams()
    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, String value) {
        return txt.replaceAll("\\$\\{1\\}", value);
    } // replaceParameter()

    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, String value1, String value2) {
        return txt.replaceAll("\\$\\{1\\}", value1).replaceAll("\\$\\{2\\}", value2);
    } // replaceParameter()

    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, String value1, String value2, String value3) {
        return txt.replaceAll("\\$\\{1\\}", value1)
                  .replaceAll("\\$\\{2\\}", value2)
                  .replaceAll("\\$\\{3\\}", value3);
    } 

    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, String value1, String value2, String value3, String value4) {
        return txt.replaceAll("\\$\\{1\\}", value1)
                  .replaceAll("\\$\\{2\\}", value2)
                  .replaceAll("\\$\\{3\\}", value3)
                  .replaceAll("\\$\\{4\\}", value4);
    } 

    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, String value1, String value2, String value3, String value4, String value5) {
        return txt.replaceAll("\\$\\{1\\}", value1)
                  .replaceAll("\\$\\{2\\}", value2)
                  .replaceAll("\\$\\{3\\}", value3)
                  .replaceAll("\\$\\{4\\}", value4)
                  .replaceAll("\\$\\{5\\}", value5);
    }

    //-----------------------------------------------------------------------------

    public static String replaceParameter(String txt, DataNamedValue[] nvp) {
        String res = txt;
        for (int i = 0; i < nvp.length; i++) {
            DataNamedValue nv = nvp[i];
            res = res.replaceAll("\\$\\{" + nv.getName() + "\\}", nv.getValue());
        } // for()
        return res;
    }

    //-----------------------------------------------------------------------------

    public static String makeCoorFormat(double coor) {
        if (coor > 180.0) {
            String s = formatNumber((long) (coor + 0.5), 10);

            int pos = 0;
            for (; pos < s.length(); pos++)
                if (s.charAt(pos) != '0')
                    break;

            return s.substring(pos);
        }

        return "" + coor;
    } // makeCoorFormat()

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
        String s = "";
        for (int i = 0; i < totlen - len; i++)
            s += "0";
        s += num;
        for (int i = 0; i < len; i++)
            s += "0";
        int pos = s.indexOf('.');
        s = s.substring(pos - (totlen - len), pos + len);
        return (s);
    } // formatNumber()

    //------------------------------------------------------------------------------

    public static String formatDate(String dt) {
        String s = dt.substring(8, 10) + "." + dt.substring(5, 7) + "." + dt.substring(0, 4) + " " + dt.substring(11, 19);
        return (s);
    } // formatDate()

    //-----------------------------------------------------------------------------

    public static String getCurrentDate() {
        Calendar c = new GregorianCalendar();
        String dt = formatNumber(c.get(Calendar.YEAR), 4) + formatNumber(c.get(Calendar.MONTH) + 1, 2) + formatNumber(c.get(Calendar.DAY_OF_MONTH), 2);
        return dt;
    } // getCurrentDate()

    //-----------------------------------------------------------------------------

    public static String getCurrentDateTime() {
        GregorianCalendar gc = new GregorianCalendar();
        String dt =
            formatNumber(gc.get(Calendar.YEAR), 4) + "-" + formatNumber(gc.get(Calendar.MONTH) + 1, 2) + "-" + formatNumber(gc.get(Calendar.DAY_OF_MONTH), 2) + " " + formatNumber(gc.get(Calendar.HOUR_OF_DAY), 2) + ":" +
            formatNumber(gc.get(Calendar.MINUTE), 2) + ":" + formatNumber(gc.get(Calendar.SECOND), 2);
        return (dt);
    } // getCurrentDateTime()

    //-----------------------------------------------------------------------------

    public static int getDayOfWeek(String datetime) {
        try {
            int year = Integer.parseInt(datetime.substring(0, 4));
            if (year == 0)
                year = 2000;
            int month = Integer.parseInt(datetime.substring(5, 7));
            if (month == 0)
                month = 1;
            int day = Integer.parseInt(datetime.substring(8, 10));
            if (day == 0)
                day = 1;
            int hour = Integer.parseInt(datetime.substring(11, 13));
            int minute = Integer.parseInt(datetime.substring(14, 16));
            int second = 0;
            if (datetime.length() >= 19)
                second = Integer.parseInt(datetime.substring(17, 19));
            GregorianCalendar gc = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
            int dayOfWeek = gc.get(Calendar.DAY_OF_WEEK);
            return (dayOfWeek);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (0);
    } // getDayOfWeek()

    //------------------------------------------------------------------------------

    public static Timestamp toTimestamp(String datetime) {
        Calendar c = null;

        try {
            int year = Integer.parseInt(datetime.substring(0, 4));
            if (year == 0)
                year = 2000;
            int month = Integer.parseInt(datetime.substring(5, 7));
            if (month == 0)
                month = 1;
            int day = Integer.parseInt(datetime.substring(8, 10));
            if (day == 0)
                day = 1;
            int hour = Integer.parseInt(datetime.substring(11, 13));
            int minute = Integer.parseInt(datetime.substring(14, 16));
            int second = 0;
            if (datetime.length() >= 19)
                second = Integer.parseInt(datetime.substring(17, 19));
            c = new GregorianCalendar(year, (month - 1), day, hour, minute, second);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return (new Timestamp(c.getTime().getTime()));
    } // toTimestamp()

    //-----------------------------------------------------------------------------

    public static void showText(String text) {
        System.out.println(text);
        Utils.logInfo(text);
        return;
    } // showText()

    //-----------------------------------------------------------------------------

    public static void showError(String text) {
        System.err.println(text);
        Utils.logInfo(text);
        return;
    } // showError()

    //-----------------------------------------------------------------------------

    public static String convStr2Url(String txt) {
        String s = "";

        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            switch (ch) {
            case 0x020:
                s += "%20";
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

    public static String convStr2Xml(String txt) {
        String s = "";
        if (txt == null)
            return s;

        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            switch (ch) {
            case 0x03C:
                s += "&lt;";
                break;
            case 0x03E:
                s += "&gt;";
                break;
            case 0x026:
                s += "&amp;";
                break;
            case 0x027:
                s += "&apos;";
                break;
            case 0x022:
                s += "&quot;";
                break;
            default:
                s += ch;
                break;
            } // switch()
        } // for()
        return (s);
    } // convStr2Xml()

    //-----------------------------------------------------------------------------

    public static String convStr2Json(String txt) {
        String s = "";
        if (txt == null)
            return s;

        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            switch (ch) {
            case 0x027:
                s += "`";
                break;
            case 0x022:
                s += "`";
                break;
            default:
                s += ch;
                break;
            } // switch()
        } // for()
        return (s);
    } // convStr2Json()

    //-----------------------------------------------------------------------------

    public static String convStrIfNull(String txt) {
        if (txt == null)
            return "";

        return txt;
    } // convStrIfNull()

    //-----------------------------------------------------------------------------

    public static String convUtf8ToEnglish(String txt) {
        if (txt == null)
            return null;

        String s = "";

        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0x0C7:
                ch = 'C';
                break;
            case 0x0E7:
                ch = 'c';
                break;
            case 0x11E:
                ch = 'G';
                break;
            case 0x11F:
                ch = 'g';
                break;
            case 0x15E:
                ch = 'S';
                break;
            case 0x15F:
                ch = 's';
                break;
            case 0x130:
                ch = 'I';
                break;
            case 0x131:
                ch = 'i';
                break;
            case 0x0DC:
                ch = 'U';
                break;
            case 0x0FC:
                ch = 'u';
                break;
            case 0x0D6:
                ch = 'O';
                break;
            case 0x0F6:
                ch = 'o';
                break;

            case 0xDE:
                ch = 'S';
                break;
            case 0xFE:
                ch = 's';
                break;
            case 0xDD:
                ch = 'I';
                break;
            case 0xFD:
                ch = 'i';
                break;
            case 0xD0:
                ch = 'G';
                break;
            case 0xF0:
                ch = 'g';
                break;
            case 0xC3:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x87:
                    ch = 'C';
                    break;
                case 0xA7:
                    ch = 'c';
                    break;
                case 0x9C:
                    ch = 'U';
                    break;
                case 0xBC:
                    ch = 'u';
                    break;
                case 0x96:
                    ch = 'O';
                    break;
                case 0xB6:
                    ch = 'o';
                    break;
                } // switch()
                break;
            case 0xC4:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x9E:
                    ch = 'G';
                    break;
                case 0x9F:
                    ch = 'g';
                    break;
                case 0xB0:
                    ch = 'I';
                    break;
                case 0xB1:
                    ch = 'i';
                    break;
                } // switch()
                break;
            case 0xC5:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x9E:
                    ch = 'S';
                    break;
                case 0x9F:
                    ch = 's';
                    break;
                } // switch()
                break;
            default:
                break;
            } // switch()
            s += (char) ch;
        } // for()
        return (s);
    } // convUtf8ToEnglish()

    //-----------------------------------------------------------------------------

    public static String convUtf8ToTurkish(String txt) {
        if (txt == null)
            return null;

        String s = "";

        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0xDE:
                ch = 0x15E;
                break;
            case 0xFE:
                ch = 0x15F;
                break;
            case 0xEE:
                ch = 0x15F;
                break;
            case 0xDD:
                ch = 0x130;
                break;
            case 0xFD:
                ch = 0x131;
                break;
            case 0xD0:
                ch = 0x11E;
                break;
            case 0xF0:
                ch = 0x11F;
                break;
            case 0xC3:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x0087:
                    ch = 0x0C7;
                    break;
                case 0x00A7:
                    ch = 0x0E7;
                    break;
                case 0x009C:
                    ch = 0x0DC;
                    break;
                case 0x00BC:
                    ch = 0x0FC;
                    break;
                case 0x0096:
                    ch = 0x0D6;
                    break;
                case 0x00B6:
                    ch = 0x0F6;
                    break;
                case 0x2021:
                    ch = 0x0C7;
                    break;
                case 0x2013:
                    ch = 0x0D6;
                    break;
                case 0x0153:
                    ch = 0x0DC;
                    break;
                } // switch()
                break;
            case 0xC4:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x017E:
                    ch = 0x11E;
                    break;
                case 0x017D:
                    ch = 0x11F;
                    break;
                case 0x009E:
                    ch = 0x11E;
                    break;
                case 0x009F:
                    ch = 0x11F;
                    break;
                case 0x00B0:
                    ch = 0x130;
                    break;
                case 0x00B1:
                    ch = 0x131;
                    break;
                case 0x0178:
                    ch = 0x11F;
                    break;
                } // switch()
                break;
            case 0xC5:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x009E:
                    ch = 0x15E;
                    break;
                case 0x009F:
                    ch = 0x15F;
                    break;
                case 0x017E:
                    ch = 0x15E;
                    break;
                case 0x0178:
                    ch = 0x15F;
                    break;
                } // switch()
                break;
            default:
                break;
            } // switch()
            s += (char) ch;
        } // for()
        return (s);
    } // convUtf8ToTurkish()

    //-----------------------------------------------------------------------------

    public static String convToUpperEnglishChars(String txt) {
        String s = "";
        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0x0C7:
                ch = 'C';
                break;
            case 0x0E7:
                ch = 'C';
                break;
            case 0x15E:
                ch = 'S';
                break;
            case 0x15F:
                ch = 'S';
                break;
            case 0x0DE:
                ch = 'S';
                break;
            case 0x0FE:
                ch = 'S';
                break;
            case 0x11E:
                ch = 'G';
                break;
            case 0x11F:
                ch = 'G';
                break;
            case 0x0D0:
                ch = 'G';
                break;
            case 0x0F0:
                ch = 'G';
                break;
            case 0x130:
                ch = 'I';
                break;
            case 0x131:
                ch = 'I';
                break;
            case 0x0DD:
                ch = 'I';
                break;
            case 0x0FD:
                ch = 'I';
                break;
            case 0x0DC:
                ch = 'U';
                break;
            case 0x0FC:
                ch = 'U';
                break;
            case 0x0D6:
                ch = 'O';
                break;
            case 0x0F6:
                ch = 'O';
                break;
            default:
                if (ch >= 'a' && ch <= 'z')
                    ch &= 0xDF;
                break;
            } // switch()

            if (ch == ' ')
                continue;

            /*
       * KEYWORD alaninda nokta kullaniminin farkedilmesi ve yapilan aramalarin basarili olmasi icin nokta kullanimi gecerli sayildi.
      */
            //      if (ch == '.')
            //        continue;

            if (ch == ',')
                continue;

            s += (char) ch;
        } // for()
        return s;
    } // convToUpperEnglishChars()
    //-----------------------------------------------------------------------------

    public static String convToUpperEnglishChar(String txt) {
        String s = "";
        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0x0C7:
                ch = 'C';
                break;
            case 0x0E7:
                ch = 'C';
                break;
            case 0x15E:
                ch = 'S';
                break;
            case 0x15F:
                ch = 'S';
                break;
            case 0x0DE:
                ch = 'S';
                break;
            case 0x0FE:
                ch = 'S';
                break;
            case 0x11E:
                ch = 'G';
                break;
            case 0x11F:
                ch = 'G';
                break;
            case 0x0D0:
                ch = 'G';
                break;
            case 0x0F0:
                ch = 'G';
                break;
            case 0x130:
                ch = 'I';
                break;
            case 0x131:
                ch = 'I';
                break;
            case 0x0DD:
                ch = 'I';
                break;
            case 0x0FD:
                ch = 'I';
                break;
            case 0x0DC:
                ch = 'U';
                break;
            case 0x0FC:
                ch = 'U';
                break;
            case 0x0D6:
                ch = 'O';
                break;
            case 0x0F6:
                ch = 'O';
                break;
            default:
                if (ch >= 'a' && ch <= 'z')
                    ch &= 0xDF;
                break;
            } // switch()

            if (ch == ' ')
                continue;

            s += (char) ch;
        } // for()
        return s;
    } // convToUpperEnglishChars()
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
            if (chk.equals("%C7")) {
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

                    chk = txt.substring(i, i + 6);
                    if (chk.equals("%u015E")) {
                        ch = 0x15E;
                        i += 5;
                    } else if (chk.equals("%u015F")) {
                        ch = 0x15F;
                        i += 5;
                    } else if (chk.equals("%u011E")) {
                        ch = 0x11E;
                        i += 5;
                    } else if (chk.equals("%u011F")) {
                        ch = 0x11F;
                        i += 5;
                    } else if (chk.equals("%u0130")) {
                        ch = 0x130;
                        i += 5;
                    } else if (chk.equals("%u0131")) {
                        ch = 0x131;
                        i += 5;
                    } else
                        ;

                } // else
            } // else

            s += (char) ch;
        } // for()
        return s;
    } // decodeEscape()

    //-----------------------------------------------------------------------------

    public static String toCompareCase(String txt) {
        if (txt == null)
            return "";

        String s = "";

        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0x0C7:
                ch = 'C';
                break;
            case 0x0E7:
                ch = 'C';
                break;
            case 0x11E:
                ch = 'G';
                break;
            case 0x11F:
                ch = 'G';
                break;
            case 0x15E:
                ch = 'S';
                break;
            case 0x15F:
                ch = 'S';
                break;
            case 0x130:
                ch = 'I';
                break;
            case 0x131:
                ch = 'I';
                break;
            case 0x0DC:
                ch = 'U';
                break;
            case 0x0FC:
                ch = 'U';
                break;
            case 0x0D6:
                ch = 'O';
                break;
            case 0x0F6:
                ch = 'O';
                break;

            case 0xDE:
                ch = 'S';
                break;
            case 0xFE:
                ch = 'S';
                break;
            case 0xDD:
                ch = 'I';
                break;
            case 0xFD:
                ch = 'I';
                break;
            case 0xD0:
                ch = 'G';
                break;
            case 0xF0:
                ch = 'G';
                break;
            case 0xC3:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x87:
                    ch = 'C';
                    break;
                case 0xA7:
                    ch = 'C';
                    break;
                case 0x9C:
                    ch = 'U';
                    break;
                case 0xBC:
                    ch = 'U';
                    break;
                case 0x96:
                    ch = 'O';
                    break;
                case 0xB6:
                    ch = 'O';
                    break;
                } // switch()
                break;
            case 0xC4:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x9E:
                    ch = 'G';
                    break;
                case 0x9F:
                    ch = 'G';
                    break;
                case 0xB0:
                    ch = 'I';
                    break;
                case 0xB1:
                    ch = 'I';
                    break;
                } // switch()
                break;
            case 0xC5:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x9E:
                    ch = 'S';
                    break;
                case 0x9F:
                    ch = 'S';
                    break;
                } // switch()
                break;
            default:
                break;
            } // switch()
            s += Character.toUpperCase((char) ch);
        } // for()
        return (s);
    } // toCompareCase()

    //------------------------------------------------------------------------------

    public static boolean isAllDigits(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            if (ch < '0' || ch > '9')
                return false;

        } // for()

        return true;
    } // isAllDigits()

    //-----------------------------------------------------------------------------

    public static boolean isAllAscii(String txt) {
        for (int i = 0; i < txt.length(); i++) {
            char ch = txt.charAt(i);
            if (ch < ' ' || ch > '~')
                return false;

        } // for()

        return true;
    } // isAllAscii()

    //------------------------------------------------------------------------------

    public static int getTurkishCharIndex(char ch) {
        int inx;

        inx = xlatTable.indexOf(ch);
        if (inx < 0)
            inx = 0;
        return (inx);
    } // getTurkishCharIndex()

    //------------------------------------------------------------------------------

    public static int compareTurkish(String s1, String s2) {
        if (s1 == null)
            return -1;

        if (s2 == null)
            return 1;

        StringBuilder sb1 = new StringBuilder(s1);
        StringBuilder sb2 = new StringBuilder(s2);

        int len1 = sb1.length();
        int len2 = sb2.length();
        int len = len1;
        if (len1 > len2)
            len = len2;
        for (int i = 0; i < len; i++) {
            char ch1 = sb1.charAt(i);
            char ch2 = sb2.charAt(i);
            if (getTurkishCharIndex(ch1) < getTurkishCharIndex(ch2))
                return -1;

            if (getTurkishCharIndex(ch1) > getTurkishCharIndex(ch2))
                return 1;

        } // for()

        if (len1 == len2)
            return 0;

        if (len1 < len2)
            return -1;

        return 1;
    } // compareTurkish()

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

    //------------------------------------------------------------------------------

    public synchronized static void logInfo(String txt) {
        String fullPath = Utils.getParameter("logfileprefix") + "_" + Utils.getCurrentDate() + ".log";

        try {
            BufferedWriter out = new BufferedWriter(new FileWriter(fullPath, true));
            out.write(Utils.getCurrentDateTime() + " --> " + txt);
            out.newLine();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return;
    } // logInfo()

    //-----------------------------------------------------------------------------

    public static double makeDegree(String coor) throws Exception {
        int posOne = coor.indexOf(' ');
        if (posOne >= 0) {
            int posTwo = coor.indexOf(' ', posOne + 1);
            if (posTwo < 0)
                return 0.00;

            double deg = Double.parseDouble(coor.substring(0, posOne));
            double min = Double.parseDouble(coor.substring(posOne + 1, posTwo));
            double sec = Double.parseDouble(coor.substring(posTwo + 1, posTwo + 1 + 2));
            char way = coor.charAt(posTwo + 3);
            min += sec / 60.0;
            deg += min / 60.0;
            if (way == 'S' || way == 'W')
                deg = -deg;
            return deg;
        }

        return Double.parseDouble(coor);
    } // makeDegree()

    //-----------------------------------------------------------------------------

    public synchronized static String createFileName(String path) {
        String fname = null;
        int inx = 0;
        while (true) {
            fname = new java.util.Date().getTime() + "_" + inx + ".png";
            File f = new File(path + "/" + fname);
            if (!f.exists())
                break;

            inx++;
        } // while()
        return fname;
    } // createFileName()

    //-----------------------------------------------------------------------------

    public static void xlogLbsServiceRequest(String key, String transactionId, String requestName, String txt) {
        logLbsServiceRequest(key, transactionId, requestName, txt, null);
        return;
    } // logLbsServiceRequest()

    //-----------------------------------------------------------------------------

    public static void logLbsServiceRequest(String key, String transactionId, String requestName, String txt, String requestIp) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "INSERT INTO LBS_SERVICE_LOG (KEY,TRANSACTION_ID,TIME_STAMP,TIME_STAMP_TS, REQUEST_NAME,REQUEST_TEXT,REQUEST_IP) VALUES (?,?,SYSDATE, SYSTIMESTAMP, ?,?,?)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            if (key == null)
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            else
                pstmt.setString(1, key);
            pstmt.setString(2, transactionId);
            if (requestName == null)
                pstmt.setNull(3,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(3, requestName);
            if (txt == null || txt.length() <= 0)
                pstmt.setNull(4,  java.sql.Types.VARCHAR);
            else {
                if (txt.length() <= 3500)
                    pstmt.setString(4, txt);
                else
                    pstmt.setString(4, txt.substring(0, 3500));
            }
            if (requestIp == null)
                pstmt.setNull(5,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(5, requestIp);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // logWebServiceRequest()
    
    public static void logLbsServiceRequest(Connection cnn, String key, String transactionId, String requestName, String txt, String requestIp) {
        PreparedStatement pstmt = null;
        String sql = null;
        try {
            sql = "INSERT INTO LBS_SERVICE_LOG (KEY,TRANSACTION_ID,TIME_STAMP,TIME_STAMP_TS, REQUEST_NAME,REQUEST_TEXT,REQUEST_IP) VALUES (?,?,SYSDATE, SYSTIMESTAMP, ?,?,?)";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            if (key == null)
                pstmt.setNull(1, java.sql.Types.VARCHAR);
            else
                pstmt.setString(1, key);
            pstmt.setString(2, transactionId);
            if (requestName == null)
                pstmt.setNull(3,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(3, requestName);
            if (txt == null || txt.length() <= 0)
                pstmt.setNull(4,  java.sql.Types.VARCHAR);
            else {
                if (txt.length() <= 3500)
                    pstmt.setString(4, txt);
                else
                    pstmt.setString(4, txt.substring(0, 3500));
            }
            if (requestIp == null)
                pstmt.setNull(5,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(5, requestIp);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
        }

        return;
    } 

    //-----------------------------------------------------------------------------

    public static void updateLbsServiceRequest(String transactionId, String resultCode, String resultDesc, String txt) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_SERVICE_LOG SET RESULT_CODE=?,RESULT_DESC=?,RESPONSE_TIME_STAMP=SYSDATE,RESPONSE_TIME_STAMP_TS=SYSTIMESTAMP,RESPONSE_TEXT=? WHERE TRANSACTION_ID=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            if (resultCode == null)
                pstmt.setNull(1,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(1, resultCode);
            if (resultDesc == null)
                pstmt.setNull(2,  java.sql.Types.VARCHAR);
            else
                pstmt.setString(2, resultDesc);
            if (txt == null || txt.length() <= 0)
                pstmt.setNull(3,  java.sql.Types.VARCHAR);
            else {
                if (txt.length() <= 3500)
                    pstmt.setString(3, txt);
                else
                    pstmt.setString(3, txt.substring(0, 3500));
            }
            pstmt.setString(4, transactionId);
            pstmt.executeUpdate();
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // updateLbsServiceRequest()

    //-----------------------------------------------------------------------------

    public static boolean isFileExists(String fileName) {
        File f = new File(fileName);
        if (f.isFile() && f.exists())
            return true;

        return false;
    } // isFileExists()

    //-----------------------------------------------------------------------------

    public static String getDomainFromReferer(String referer) {
        String domain = "";
        if (referer == null)
            return domain;

        try {
            if (referer.startsWith("http://") || referer.startsWith("https://"))
                domain = referer.replace("^(http|https)://", "");
            else
                domain = referer;

            int pos = domain.indexOf("/");
            if (pos >= 0)
                domain = domain.substring(0, pos);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return domain;
    } // getDomainFromReferer()

    //-----------------------------------------------------------------------------

    public static boolean isKeyValid(String key) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_KEYS SET LAST_ACCESS=SYSDATE WHERE KEY=? AND ACTIVE=1 AND EXPIRE_DATE > SYSDATE AND REQUEST_COUNT < REQUEST_LIMIT";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            int count = pstmt.executeUpdate();
            if (count > 0)
                return true;

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return false;
    } // isKeyValid()

    //-----------------------------------------------------------------------------

    public static boolean isKeyDomainValid(String key, String domain) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT DOMAIN FROM LBS_KEY_DOMAIN WHERE KEY=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            int count = 0;
            while (rset.next()) {
                count++;
                String dbDomain = rset.getString(1);
                if (dbDomain.startsWith(domain) && domain.length() > 0)
                    return true;

            } // while()
            if (count <= 0)
                return true;

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return false;
    } // isKeyDomainValid()

    //-----------------------------------------------------------------------------

    public static void incrementAccessCount(String key, String transactionId, int count) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_KEYS SET REQUEST_COUNT=REQUEST_COUNT+? WHERE KEY=? AND ACTIVE=1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setInt(1, count);
            pstmt.setString(2, key);
            int res = pstmt.executeUpdate();
            if(res == 1){
                reducedKeyLimit(cnn, key, transactionId, count);
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // incrementAccessCount()

    //-----------------------------------------------------------------------------

    public static void incrementAccessCount(String key, String transactionId) {
        PreparedStatement pstmt = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "UPDATE LBS_KEYS SET REQUEST_COUNT=REQUEST_COUNT+1 WHERE KEY=? AND ACTIVE=1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            int res = pstmt.executeUpdate();
            DbConn.closeDBConnection(pstmt, null);
            if(res == 1){
                reducedKeyLimit(cnn, key, transactionId, 1);
            }
            
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
            DbConn.closeConnection(cnn);
        }

        return;
    } // incrementAccessCount()
    
    public static void incrementAccessCount(Connection cnn, String key, String transactionId) {
        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "UPDATE LBS_KEYS SET REQUEST_COUNT=REQUEST_COUNT+1 WHERE KEY=? AND ACTIVE=1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            int res = pstmt.executeUpdate();
            DbConn.closeDBConnection(pstmt, null);
            if(res == 1){
                reducedKeyLimit(cnn, key, transactionId, 1);
            }
            
        } catch (Exception ex) {
            Utils.showError("incrementAccessCount SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
        }

        return;
    }
   //-----------------------------------------------------------------------------
    public static void reducedKeyLimit( Connection cnn, String key, String transactionId, int count){
        PreparedStatement pstmt = null;
        String sql = null;

        try {
            sql = "UPDATE LBS_SERVICE_LOG SET REDUCED_KEY_LIMIT =  NVL(REDUCED_KEY_LIMIT,0) + ? WHERE KEY=? AND TRANSACTION_ID = ?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setInt(1, count);
            pstmt.setString(2, key);
            pstmt.setString(3, transactionId);
            pstmt.executeUpdate();
            
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, null);
        }

    }
    
    //-----------------------------------------------------------------------------

    public static boolean isRequestLimitEnough(String key, int count) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT REQUEST_COUNT FROM LBS_KEYS WHERE KEY=? AND ACTIVE=1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int requestCount = rset.getInt(1);
                if (requestCount >= count)
                    return true;

            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return false;
    } // isRequestLimitEnough()

    //-----------------------------------------------------------------------------

    public static String getKeyEMail(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT EMAIL FROM LBS_KEYS WHERE KEY=? AND ACTIVE=1 AND EXPIRE_DATE > SYSDATE AND REQUEST_COUNT < REQUEST_LIMIT";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                String email = rset.getString(1);
                return email;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getKeyEMail()
    
    //-----------------------------------------------------------------------------

    public static String getKeyCompany(Connection cnn, String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String company = null;
        try {
            sql = "SELECT COMPANY FROM LBS_KEYS WHERE KEY=? AND ROWNUM=1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                company = rset.getString(1);
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
        }

        return company;
    } // getKeyCompany()
    //-----------------------------------------------------------------------------
    
    public static long getBBolumFeedbackId(Connection cnn) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        long id = 0L;

        try {
            sql = "SELECT SEQ_BBOLUM_FEEDBACK.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                id = rset.getLong(1);
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
        }

        return id;
    } // getBBolumFeedbackId()
    
    //-----------------------------------------------------------------------------
    public static String getStyleName(int type) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;

        if (htable == null) {
            htable = new Hashtable();

            Connection cnn = null;

            try {
                cnn = DbConn.getPooledConnection();
                sql = "SELECT NO,SYMBOL FROM LBS_SYMBOL ORDER BY NO";
                pstmt = cnn.prepareStatement(sql);
                pstmt.setQueryTimeout(360);
                pstmt.clearParameters();
                rset = pstmt.executeQuery();
                while (rset.next()) {
                    int no = rset.getInt(1);
                    String symbol = rset.getString(2);
                    htable.put("" + no, symbol);
                } // while()
            } catch (Exception ex) {
                Utils.showError("SQL: " + sql);
                Utils.showError("Exception occured : " + ex.getMessage());
                ex.printStackTrace();
            } finally {
                DbConn.closeDBConnection(pstmt, rset);
                DbConn.closeConnection(cnn);
            }

        } // if(htable)

        String symbol = (String) htable.get("" + type);
        if (symbol != null)
            return symbol;

        return "M.RED PIN";
    } // getStyleName()

    //-----------------------------------------------------------------------------

    public static double[] getTransformedCoors(DataPoint dp) {
        return getTransformedCoors(dp.getLatitude(), dp.getLongitude());
    } // getTransformedCoors()

    //-----------------------------------------------------------------------------

    public static double[] getTransformedCoors(double latitude, double longitude) {
        double[] coors = new double[2];
        if (latitude <= 90.0 && longitude <= 180.0) {
            coors[0] = longitude;
            coors[1] = latitude;
            return coors;
        }

        int srid = 81989006;
        try {
            srid = Integer.parseInt(Utils.getParameter("fakecoor_srid"));
        } catch (Exception e) {
            ;
        }
        return getTransformedCoors(latitude, longitude, srid);
    } // getTransformedCoors()

    //-----------------------------------------------------------------------------

    public static double[] getTransformedCoors(double latitude, double longitude, int srid) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT P.GEOLOC.SDO_POINT.X,P.GEOLOC.SDO_POINT.Y FROM (SELECT SDO_CS.TRANSFORM(SDO_GEOMETRY(2001,?, SDO_POINT_TYPE(?,?,NULL), NULL, NULL), 8307) GEOLOC FROM DUAL) P";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setInt(1, srid);
            pstmt.setDouble(2, longitude);
            pstmt.setDouble(3, latitude);
            rset = pstmt.executeQuery();
            rset.next();
            double[] coors = new double[2];
            coors[0] = rset.getDouble(1);
            coors[1] = rset.getDouble(2);
            return coors;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getTransformedCoors()

    //-----------------------------------------------------------------------------

    public static double[] transformCoors(double latitude, double longitude, int srid) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT P.GEOLOC.SDO_POINT.X,P.GEOLOC.SDO_POINT.Y FROM (SELECT SDO_CS.TRANSFORM(SDO_GEOMETRY(2001,8307, SDO_POINT_TYPE(?,?,NULL), NULL, NULL), ?) GEOLOC FROM DUAL) P";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            pstmt.setInt(3, srid);
            rset = pstmt.executeQuery();
            rset.next();
            double[] coors = new double[2];
            coors[0] = rset.getDouble(1);
            coors[1] = rset.getDouble(2);
            return coors;
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // transformCoors()

    //-----------------------------------------------------------------------------

    public static DataAddress getIlIlceMahalle(double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM ADMIN_AREA WHERE SDO_ANYINTERACT(GEOLOC,SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL), NULL, NULL)) = 'TRUE'";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            rset = pstmt.executeQuery();
            if (rset.next()) {
                DataAddress da = new DataAddress();
                long ilId = rset.getLong("IL_ID");
                String ilAdi = rset.getString("IL_ADI");
                long ilceId = rset.getLong("ILCE_ID");
                String ilceAdi = rset.getString("ILCE_ADI");
                long mahalleId = rset.getLong("MAHALLE_ID");
                String mahalleAdi = rset.getString("MAHALLE_ADI");
                int zone = rset.getInt("ZONE");
                da.setIlId(ilId);
                da.setIlAdi(ilAdi);
                da.setAdrIlAdi(ilAdi);
                da.setIlceId(ilceId);
                da.setIlceAdi(ilceAdi);
                da.setAdrIlceAdi(ilceAdi);
                da.setMahalleId(mahalleId);
                da.setMahalleAdi(mahalleAdi);
                da.setAdrMahalleAdi(mahalleAdi);
                da.setZone(zone);
                return da;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getIlIlceMahalle()

    //-----------------------------------------------------------------------------

    public static DataKapi getKapiInfo(double latitude, double longitude, double distance) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        DataKapi dk = null; //ekn
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT KAPI_ID, KAPI_ADI, KAPI_NO, ZOOMLEVEL, POSTA_KODU, XCOOR, YCOOR, ";
            sql += "IL_ID, IL_ADI, ILCE_ID, ILCE_ADI, MAHALLE_ID, MAHALLE_ADI, MAHALLE_ID AS KOY_ID, MAHALLE_ADI AS KOY_ADI, YOL_ID, ";
            sql += "YOL_ADI,ZONE,SDO_GEOM.SDO_DISTANCE(GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL), NULL, NULL),0.001,'unit=M') DISTANCE, UAVT_KAPI_ID, ";
            sql += "UAVT_CSBM_ID, UAVT_KOY_ID, UAVT_MAHALLE_ID, UAVT_ILCE_ID, UAVT_IL_ID ";
            sql += "FROM KAPI WHERE SDO_WITHIN_DISTANCE(GEOLOC, MDSYS.SDO_GEOMETRY (2001,8307, MDSYS.SDO_POINT_TYPE(?,?,NULL), NULL, NULL), ?) = 'TRUE' AND KAPI_ID > 0 ORDER BY DISTANCE";

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setDouble(4, latitude);
            pstmt.setString(5, "distance = " + distance + " UNIT=M");
            rset = pstmt.executeQuery();
            if (rset.next()) {
                dk = DataKapi.getInstance(rset, false, false);
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dk; // ekn
    } // getKapiInfo()

    //-----------------------------------------------------------------------------

    public static DataYol getYolInfo(double latitude, double longitude, double distance) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        DataYol dy = null; //ekn
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT Y.YOL_ID,Y.YOL_ADI,Y.YOL_SINIFI,Y.YOL_TURU,Y.ZOOMLEVEL,Y.POSTA_KODU,Y.XCOOR,Y.YCOOR,SDO_GEOM.SDO_DISTANCE(ISY.GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL), NULL, NULL),0.001,'unit=M') DISTANCE, ISY.UAVT_CSBM_ID ";
            sql += "FROM YOL Y, IDARI_SINIR_YOL ISY WHERE SDO_WITHIN_DISTANCE(ISY.GEOLOC, MDSYS.SDO_GEOMETRY (2001,8307, MDSYS.SDO_POINT_TYPE(?,?,NULL), NULL, NULL), ?) = 'TRUE' AND Y.YOL_ID > 0 AND Y.YOL_ADI IS NOT NULL AND Y.YOL_ID = ISY.YOL_ID ORDER BY DISTANCE";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setDouble(4, latitude);
            pstmt.setString(5, "distance = " + distance + " UNIT=M");

            rset = pstmt.executeQuery();
            if (rset.next()) {
                long yolId = rset.getLong(1);
                String yolAdi = rset.getString(2);
                int yolSinifi = rset.getInt(3);
                String yolTuru = rset.getString(4);
                int zoomLevel = rset.getInt(5);
                String postaKodu = rset.getString(6);
                double xcoor = rset.getDouble(7);
                double ycoor = rset.getDouble(8);
                long adresKodu = rset.getLong(10);
                dy = new DataYol(yolId, yolAdi, ycoor, xcoor, yolSinifi, zoomLevel, postaKodu, 0, "", 0, "", 0, "", adresKodu, yolTuru);
                //return dy;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return dy;
    } // getYolInfo()
    
    //-----------------------------------------------------------------------------

    public static String getYolFRCInfo(double latitude, double longitude, int buffer) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;
        String frc = null;

        try {
            cnn = DbConn.getPooledConnection();
            String geolocBuffer =null;
            if( buffer == 10 ){
                geolocBuffer = "GEOLOC10";
            }else if( buffer == 50 ){
                geolocBuffer = "GEOLOC50";
            }else{
                geolocBuffer = "GEOLOC100";  
            }
            sql = "SELECT FRC_A FRC FROM YOL_FRC WHERE SDO_ANYINTERACT(" + geolocBuffer + ", SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL)) = 'TRUE'";

            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);

            rset = pstmt.executeQuery();
            if (rset.next()) {
                frc = rset.getString(1);
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return frc;
    } // getYolInfo()

    //-----------------------------------------------------------------------------

    public static DataMahalle getKoyInfo(double latitude, double longitude, double distance) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT * FROM (SELECT MAHALLE_ID, MAHALLE_ADI, TYPE, ILCE_ID, XCOOR, YCOOR, SDO_GEOM.SDO_DISTANCE(GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL), NULL, NULL),0.001,'unit=M') DISTANCE FROM MAHALLE WHERE TYPE = 4 AND SDO_WITHIN_DISTANCE(GEOLOC, MDSYS.SDO_GEOMETRY (2001,8307, MDSYS.SDO_POINT_TYPE(?,?,NULL), NULL, NULL), ?) = 'TRUE'  ORDER BY DISTANCE) WHERE ROWNUM = 1";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setDouble(4, latitude);
            pstmt.setString(5, "distance = " + distance + " UNIT=M");
            rset = pstmt.executeQuery();
            if (rset.next()) {
                long mahalleId = rset.getLong(1);
                String mahalleAdi = rset.getString(2);
                int type = rset.getInt(3);
                long ilceId = rset.getLong(4);
                double xcoor = rset.getDouble(5);
                double ycoor = rset.getDouble(6);
                DataMahalle dm = new DataMahalle(mahalleId, mahalleAdi, type, ilceId, xcoor, ycoor, null);
                return dm;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getKoyInfo()

    //-----------------------------------------------------------------------------

    public static String getCategoryList(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String res = "";
        int count = 0;
        Connection cnn = null;
        
        Hashtable htable = new Hashtable();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT CATEGORY,SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND BRAND_ID=0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                String sqlQuery = rset.getString(2);
                int pos = sqlQuery.indexOf(" WHERE ");
                if (pos < 0)
                    continue;

                if (htable.containsKey(category))
                    continue;

                if (count > 0)
                    res += ",";
                res += "{id:\"" + category + "\",wc:\"" + sqlQuery.substring(pos + 7) + "\",tn:\"" + sqlQuery.substring(sqlQuery.lastIndexOf("FROM ") + 5, sqlQuery.indexOf("WHERE")) + "\"}";
                htable.put(category, "+");
                count++;
            } // while()
            DbConn.closeDBConnection(pstmt, rset);
            sql = "SELECT CATEGORY,SQL_QUERY FROM LBS_POI_CATEGORY WHERE BRAND_ID=0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                String sqlQuery = rset.getString(2);
                int pos = sqlQuery.indexOf(" WHERE ");
                if (pos < 0)
                    continue;

                if (htable.containsKey(category))
                    continue;

                if (count > 0)
                    res += ",";
                res += "{id:\"" + category + "\",wc:\"" + sqlQuery.substring(pos + 7) + "\",tn:\"" + sqlQuery.substring((sqlQuery.indexOf("FROM") + 5), (sqlQuery.indexOf("WHERE") - 1)) + "\"}";
                htable.put(category, "+");
                count++;
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return res;
    } // getCategoryList()

    //-----------------------------------------------------------------------------

    public static String getBrandList(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String res = "";
        int count = 0;
        Connection cnn = null;
        Hashtable htable = new Hashtable();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT CATEGORY,SQL_QUERY FROM LBS_POI_CATEGORY_KEY WHERE KEY=? AND BRAND_ID <> 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                String sqlQuery = rset.getString(2);
                int pos = sqlQuery.indexOf(" WHERE ");
                if (pos < 0)
                    continue;

                if (htable.containsKey(category))
                    continue;

                if (count > 0)
                    res += ",";
                res += "{id:\"" + category + "\",wc:\"" + sqlQuery.substring(pos + 7) + "\"}";
                htable.put(category, "+");
                count++;
            } // while()
            DbConn.closeDBConnection(pstmt, rset);
            sql = "SELECT CATEGORY,SQL_QUERY FROM LBS_POI_CATEGORY WHERE BRAND_ID <> 0";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String category = rset.getString(1);
                String sqlQuery = rset.getString(2);
                int pos = sqlQuery.indexOf(" WHERE ");
                if (pos < 0)
                    continue;

                if (htable.containsKey(category))
                    continue;

                if (count > 0)
                    res += ",";
                res += "{id:\"" + category + "\",wc:\"" + sqlQuery.substring(pos + 7) + "\"}";
                htable.put(category, "+");
                count++;
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return res;
    } // getBrandList()

    //-----------------------------------------------------------------------------

    public static String getPackages(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String res = "";
        int count = 0;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT PACKAGE_NAME FROM LBS_KEY_PACKAGE WHERE ACTIVE=1 AND KEY=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                String packageName = rset.getString(1);
                if (count > 0)
                    res += ",";
                res += "\"" + packageName + "\"";
                count++;
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        res += "";
        return res;
    } // getPackages()

    //-----------------------------------------------------------------------------

    public static String getUniquePointId() {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SEQ_LBS_USER_POINT_ID.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                String id = rset.getString(1);
                return id;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getUniquePointId()

    //-----------------------------------------------------------------------------

    public static String getUniqueRegionId() {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SEQ_LBS_USER_REGION_ID.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int id = rset.getInt(1);
                return "" + id;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getUniqueRegionId()

    //-----------------------------------------------------------------------------

    public static String getUniqueLineId() {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SEQ_LBS_USER_LINE_ID.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                int id = rset.getInt(1);
                return "" + id;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return null;
    } // getUniqueLineId()

    //-----------------------------------------------------------------------------

    public static long getUniqueRequestId() {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SEQ_OPT_REQUEST_REQUEST_ID.NEXTVAL FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            rset = pstmt.executeQuery();
            if (rset.next()) {
                long id = rset.getLong(1);
                return id;
            }

        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return 0;
    } // getUniqueRequestId()

    //-----------------------------------------------------------------------------

    public static double getExtentArea(Extent ext) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        double area = 0.00;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT SDO_GEOM.SDO_AREA((SDO_GEOMETRY(2003,8307,NULL,SDO_ELEM_INFO_ARRAY(1,1003,3),SDO_ORDINATE_ARRAY(?,?,?,?))), 0.001) FROM DUAL";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, ext.getMinLongitude());
            pstmt.setDouble(2, ext.getMinLatitude());
            pstmt.setDouble(3, ext.getMaxLongitude());
            pstmt.setDouble(4, ext.getMaxLatitude());
            rset = pstmt.executeQuery();
            rset.next();
            area = rset.getDouble(1);
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return area;
    } // getExtentArea()

    //-----------------------------------------------------------------------------

    public static double[] getGeometryVertices(String tableName, String idColumn, String geolocColumn, long id) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        Connection cnn = null;

        ArrayList array = new ArrayList();

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT VX.X,VX.Y FROM " + tableName + " TBL, TABLE(SDO_UTIL.GETVERTICES(TBL." + geolocColumn + ")) VX WHERE TBL." + idColumn + "=? ORDER BY VX.ID";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setLong(1, id);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                double[] coors = new double[2];
                coors[0] = rset.getDouble(1);
                coors[1] = rset.getDouble(2);
                array.add(coors);
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        if (array.size() <= 0)
            return null;

        double[] oarray = new double[array.size() * 2];
        for (int i = 0; i < oarray.length / 2; i++) {
            double[] coors = (double[]) array.get(i);
            oarray[2 * i] = coors[0];
            oarray[2 * i + 1] = coors[1];
        } // for()
        return oarray;
    } // getGeometryVertices()

    

    public static double getDistanceFromIdariSinirYol(Connection cnn, long yolId, double latitude, double longitude) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        double distance = 0.00;

        try {
            sql= "SELECT SDO_GEOM.SDO_DISTANCE(GEOLOC, SDO_GEOMETRY(2001,8307,SDO_POINT_TYPE(?,?,NULL),NULL,NULL), 0.001, 'unit=M') DISTANCE FROM IDARI_SINIR_YOL Y WHERE YOL_ID=? ORDER BY DISTANCE";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setDouble(1, longitude  );
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, yolId);
            rset = pstmt.executeQuery();
            if(rset.next()){
              distance = rset.getDouble(1);
            }
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);;
        }

        return distance;
    } 
    //-----------------------------------------------------------------------------


    public static double[][] getPolygonBoundary(JGeometry geoelem) {
        double[][] boundary = null;

        if (geoelem == null)
            return (double[][]) null;

        int gtype = geoelem.getType();
        if (gtype != JGeometry.GTYPE_POLYGON)
            return (double[][]) null;

        int dim = geoelem.getDimensions();
        int[] elemInfo = geoelem.getElemInfo();
        double[] oords = geoelem.getOrdinatesArray();

        int etype = elemInfo[1];
        if (etype == 1005 || etype == 2005) // compound polygon
        {
            // add code to get boundary for compound polygon
        } else //1003 or 2003
        {
            int einterp = elemInfo[2];
            if (einterp == 2) // connected sequence of circular arcs
            {
                // add code to get boundary from polygon with arcs
            } else // 1 - simple polygon (it may have voids) or rectangle with voids
            {
                int nelems = elemInfo.length / 3;
                boundary = new double[nelems][];
                for (int i = 0; i < nelems; i++) {
                    int interp = elemInfo[3 * i + 2];
                    int startIndex = elemInfo[3 * i] - 1;
                    int endIndex = oords.length - 1;
                    if (i < (nelems - 1))
                        endIndex = elemInfo[3 * (i + 1)] - 2;
                    if (interp == 3) {
                        //boundary[i] = add code to get boundary from rectangle ordinates.

                    } else
                        boundary[i] = getBoundaryFromOordinatesRange(oords, dim, startIndex, endIndex);
                }
            }
        }

        return boundary;
    }

    //-----------------------------------------------------------------------------

    public static double[] getBoundaryFromOordinatesRange(double[] oords, int dim, int startIndex, int endIndex) {
        if (oords == null)
            return (double[]) null;

        if (startIndex < 0 || endIndex < 0 || startIndex >= oords.length || endIndex >= oords.length || endIndex <= startIndex)
            return (double[]) null;

        ArrayList<Point2D> points = new ArrayList<Point2D>();

        for (int i = startIndex; i <= endIndex; i += dim)
            points.add(new Point2D.Double(oords[i], oords[i + 1]));

        if (points.size() == 0)
            return (double[]) null;
        else // convert into single double array to return
        {
            double[] outArray = new double[points.size() * 2];
            int pos = 0;
            for (int i = 0; i < points.size(); i++) {
                outArray[pos++] = points.get(i).getX();
                outArray[pos++] = points.get(i).getY();
            }

            return outArray;
        }
    }

    //-----------------------------------------------------------------------------

    public static String getPhonebookKey(String key) {
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        String sql = null;
        String phonebookKey = null;
        int count = 0;
        Connection cnn = null;

        try {
            cnn = DbConn.getPooledConnection();
            sql = "SELECT PHONEBOOKKEY FROM LBS_PHONEBOOK_KEY WHERE KEY=?";
            pstmt = cnn.prepareStatement(sql);
            pstmt.setQueryTimeout(360);
            pstmt.clearParameters();
            pstmt.setString(1, key);
            rset = pstmt.executeQuery();
            while (rset.next()) {
                phonebookKey = rset.getString(1);
                count++;
            } // while()
        } catch (Exception ex) {
            Utils.showError("SQL: " + sql);
            Utils.showError("Exception occured : " + ex.getMessage());
            ex.printStackTrace();
        } finally {
            DbConn.closeDBConnection(pstmt, rset);
            DbConn.closeConnection(cnn);
        }

        return phonebookKey;
    } // getPhonebookKey()

    //-----------------------------------------------------------------------------

    public static double[] getOrdinatesArray(JGeometry geo) {
        JGeometry[] objs = geo.getElements();
        if (objs == null || objs.length <= 0)
            return null;

        double[] oarray = objs[0].getOrdinatesArray();
        objs[0] = null;

        boolean merged = false;

        do {
            merged = false;
            for (int i = 1; i < objs.length; i++) {
                if (objs[i] == null)
                    continue;

                double[] narray = checkAndMerge(oarray, objs[i].getOrdinatesArray());
                if (narray != null) {
                    oarray = narray;
                    objs[i] = null;
                    merged = true;
                }
            } // for(i)
        } while (merged);

        return oarray;
    } // getOrdinatesArray()

    //-----------------------------------------------------------------------------

    private static double[] checkAndMerge(double[] oarray, double[] narray) {
        double[] array = null;

        if (isCoorsEqual(oarray[0], oarray[1], narray[0], narray[1])) {
            array = new double[oarray.length + narray.length - 2];
            int offs = 0;
            for (int i = narray.length - 2; i >= 0; i -= 2) {
                array[offs++] = narray[i];
                array[offs++] = narray[i + 1];
            } // for()
            for (int i = 2; i < oarray.length; i += 2) {
                array[offs++] = oarray[i];
                array[offs++] = oarray[i + 1];
            } // for()
        } else if (isCoorsEqual(oarray[0], oarray[1], narray[narray.length - 2], narray[narray.length - 1])) {
            array = new double[oarray.length + narray.length - 2];
            int offs = 0;
            for (int i = 0; i < narray.length; i += 2) {
                array[offs++] = narray[i];
                array[offs++] = narray[i + 1];
            } // for()
            for (int i = 2; i < oarray.length; i += 2) {
                array[offs++] = oarray[i];
                array[offs++] = oarray[i + 1];
            } // for()
        } else if (isCoorsEqual(oarray[oarray.length - 2], oarray[oarray.length - 1], narray[0], narray[1])) {
            array = new double[oarray.length + narray.length - 2];
            int offs = 0;
            for (int i = 0; i < oarray.length; i += 2) {
                array[offs++] = oarray[i];
                array[offs++] = oarray[i + 1];
            } // for()
            for (int i = 2; i < narray.length; i += 2) {
                array[offs++] = narray[i];
                array[offs++] = narray[i + 1];
            } // for()
        } else if (isCoorsEqual(oarray[oarray.length - 2], oarray[oarray.length - 1], narray[narray.length - 2], narray[narray.length - 1])) {
            array = new double[oarray.length + narray.length - 2];
            int offs = 0;
            for (int i = 0; i < oarray.length - 2; i += 2) {
                array[offs++] = oarray[i];
                array[offs++] = oarray[i + 1];
            } // for()
            for (int i = narray.length - 2; i >= 0; i -= 2) {
                array[offs++] = narray[i];
                array[offs++] = narray[i + 1];
            } // for()
        }

        return (array);
    } // checkAndMerge()

    //-----------------------------------------------------------------------------

    private static boolean isCoorsEqual(double x1, double y1, double x2, double y2) {
        // System.out.println("X1: " + x1 + ", Y1: " + y1 + "X2: " + x2 + ", Y2: " + y2 + "   DX: " + Math.abs(x1 - x2) + "   " + Math.abs(y1 - y2));
        if (Math.abs(x1 - x2) < 0.00001 && Math.abs(y1 - y2) < 0.00001)
            return true; // 1 meter tolerance

        return false;
    } // isCoorsEqual()

    //-----------------------------------------------------------------------------

    public static boolean downloadAndSaveFile(String fileUrl, String fileName) {
        Utils.logInfo("Download from : " + fileUrl);
        Utils.logInfo("Save into : " + fileName);
        InputStream inp = null;
        HttpURLConnection http = null;
        FileOutputStream out = null;
        try {
            URL url = new URL(fileUrl);
            http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.setRequestProperty("Content-type", "image/gif");
            http.setDoOutput(false);
            http.setDoInput(true);
            http.connect();

            File file = new File(fileName);
            file.setReadable(true, false);
            out = new FileOutputStream(file);
            inp = http.getInputStream();
            byte b[] = new byte[10240];
            while (true) {
                int length = inp.read(b, 0, b.length);
                if (length < 0)
                    break;

                out.write(b, 0, length);
            } // while()
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }finally{
            DbConn.closeStreamConn(inp, null);
            DbConn.closeHttpConn( http,null, null, null);
            DbConn.closeFileConn(null, out);
        }

        return true;
    } // downloadAndSaveFile()

    //-----------------------------------------------------------------------------

    public static Extent getExtent(STRUCT obj) {
        try {
            JGeometry geo = JGeometry.load(obj);
            double[] arr = geo.getOrdinatesArray();
            Extent extent = new Extent(arr[1], arr[0], arr[3], arr[2]);
            return extent;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    } // getExtent()

    //-----------------------------------------------------------------------------

    public static String toUpperCase(String txt) {
        String s = "";
        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            switch (ch) {
            case 0x0C7:
                break;
            case 0x0E7:
                ch = 0x0C7;
                break; // �

            case 0x0DE:
                ch = 0x15E;
                break; // �
            case 0x0FE:
                ch = 0x15E;
                break; // �
            case 0x15E:
                break; // �
            case 0x15F:
                ch = 0x15E;
                break; // �

            case 0x0D0:
                ch = 0x11E;
                break; // �
            case 0x0F0:
                ch = 0x11E;
                break; // �
            case 0x11E:
                break; // �
            case 0x11F:
                ch = 0x11E;
                break; // �

            case 0x0DD:
                ch = 0x130;
                break; // �
            case 0x0FD:
                ch = 0x049;
                break; // I
            case 0x130:
                break; // �
            case 0x131:
                ch = 0x049;
                break; // I
            case 0x069:
                ch = 0x130;
                break; // �

            case 0x0D6:
                break;
            case 0x0F6:
                ch = 0x0D6;
                break; // �

            case 0x0DC:
                break;
            case 0x0FC:
                ch = 0x0DC;
                break; // �

            default:
                if (Character.isLowerCase(ch))
                    ch = Character.toUpperCase(ch);
                break;
            } // switch()
            s += (char) ch;
        } // for()
        return s;
    } // toUpperCase()

    //-----------------------------------------------------------------------------

    public static String toFineCase(String txt) {
        boolean firstLetter = true;
        String s = "";
        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            if (ch == ' ') {
                firstLetter = true;
                s += (char) ch;
                continue;
            }

            if (firstLetter) {
                firstLetter = false;
                switch (ch) {
                case 0x0C7:
                    break;
                case 0x0E7:
                    ch = 0x0C7;
                    break; // �

                case 0x0DE:
                    ch = 0x15E;
                    break; // �
                case 0x0FE:
                    ch = 0x15E;
                    break; // �
                case 0x0EE:
                    ch = 0x15E;
                    break; // �
                case 0x15E:
                    break; // �
                case 0x15F:
                    ch = 0x15E;
                    break; // �

                case 0x0D0:
                    ch = 0x11E;
                    break; // �
                case 0x0F0:
                    ch = 0x11E;
                    break; // �
                case 0x11E:
                    break; // �
                case 0x11F:
                    ch = 0x11E;
                    break; // �

                case 0x0DD:
                    ch = 0x130;
                    break; // �
                case 0x0FD:
                    ch = 0x049;
                    break; // I
                case 0x130:
                    break; // �
                case 0x131:
                    ch = 0x049;
                    break; // I
                case 0x069:
                    ch = 0x130;
                    break; // �

                case 0x0D6:
                    break;
                case 0x0F6:
                    ch = 0x0D6;
                    break; // �

                case 0x0DC:
                    break;
                case 0x0FC:
                    ch = 0x0DC;
                    break; // �

                default:
                    if (Character.isLowerCase(ch))
                        ch = Character.toUpperCase(ch);
                    break;
                } // switch()
            } else {
                switch (ch) {
                case 0x0C7:
                    ch = 0x0E7;
                    break; // �
                case 0x0E7:
                    break;

                case 0x0DE:
                    ch = 0x15F;
                    break; // �
                case 0x0FE:
                    ch = 0x15F;
                    break; // �
                case 0x0EE:
                    ch = 0x15F;
                    break; // �
                case 0x15E:
                    ch = 0x15F;
                    break; // �
                case 0x15F:
                    break;

                case 0x0D0:
                    ch = 0x11F;
                    break; // �
                case 0x0F0:
                    ch = 0x11F;
                    break; // �
                case 0x11E:
                    ch = 0x11F;
                    break; // �
                case 0x11F:
                    break;

                case 0x0DD:
                    ch = 0x069;
                    break; // i
                case 0x0FD:
                    ch = 0x131;
                    break; // �
                case 0x130:
                    ch = 0x069;
                    break; // i
                case 0x131:
                    break;
                case 0x049:
                    ch = 0x131;
                    break; // �
                case 0x069:
                    break;

                case 0x0D6:
                    ch = 0x0F6;
                    break; // �
                case 0x0F6:
                    break;

                case 0x0DC:
                    ch = 0x0FC;
                    break; // �
                case 0x0FC:
                    break;

                default:
                    if (Character.isUpperCase(ch))
                        ch = Character.toLowerCase(ch);
                    break;
                } // switch()
            }
            s += (char) ch;
        } // for()
        return s;
    } // toFineCase()

    //-----------------------------------------------------------------------------

    public static String toSuggestKeyword(String txt) {
        if (txt == null)
            return null;

        String s = "";

        for (int i = 0; i < txt.length(); i++) {
            int ch = txt.charAt(i);
            if (ch == ' ')
                continue;

            switch (ch) {
            case 0xC7:
                ch = 'C';
                break;
            case 0xE7:
                ch = 'C';
                break;
            case 0xDE:
                ch = 'S';
                break;
            case 0xFE:
                ch = 'S';
                break;
            case 0xEE:
                ch = 'S';
                break;
            case 0x15E:
                ch = 'S';
                break;
            case 0x15F:
                ch = 'S';
                break;
            case 0xDD:
                ch = 'I';
                break;
            case 0xFD:
                ch = 'I';
                break;
            case 0x130:
                ch = 'I';
                break;
            case 0x131:
                ch = 'I';
                break;
            case 0xD0:
                ch = 'G';
                break;
            case 0xF0:
                ch = 'G';
                break;
            case 0x11E:
                ch = 'G';
                break;
            case 0x11F:
                ch = 'G';
                break;
            case 0xD6:
                ch = 'O';
                break;
            case 0xF6:
                ch = 'O';
                break;
            case 0xDC:
                ch = 'U';
                break;
            case 0xFC:
                ch = 'U';
                break;
            case 0xC3:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x0087:
                    ch = 'C';
                    break;
                case 0x00A7:
                    ch = 'C';
                    break;
                case 0x009C:
                    ch = 'U';
                    break;
                case 0x00BC:
                    ch = 'U';
                    break;
                case 0x0096:
                    ch = 'O';
                    break;
                case 0x00B6:
                    ch = 'O';
                    break;
                case 0x2021:
                    ch = 'C';
                    break;
                case 0x2013:
                    ch = 'O';
                    break;
                case 0x0153:
                    ch = 'U';
                    break;
                } // switch()
                break;
            case 0xC4:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x009E:
                    ch = 'G';
                    break;
                case 0x009F:
                    ch = 'G';
                    break;
                case 0x00B0:
                    ch = 'I';
                    break;
                case 0x00B1:
                    ch = 'I';
                    break;
                case 0x0178:
                    ch = 'G';
                    break;
                } // switch()
                break;
            case 0xC5:
                i++;
                ch = txt.charAt(i);
                switch (ch) {
                case 0x009E:
                    ch = 'S';
                    break;
                case 0x009F:
                    ch = 'S';
                    break;
                case 0x017E:
                    ch = 'S';
                    break;
                case 0x0178:
                    ch = 'S';
                    break;
                } // switch()
                break;
            default:
                if (ch >= 'a' && ch <= 'z')
                    ch &= 0xDF;
                break;
            } // switch()
            s += (char) ch;
        } // for()
        return (s);
    } // toSuggestKeyword()

    //-----------------------------------------------------------------------------

    public static boolean zipFile(String fileName, String zipFileName) {
        BufferedInputStream origin = null;
        ZipOutputStream out = null;
        byte data[] = new byte[BUFFER_SIZE];
        int count;

        try {
            File file = new File(fileName);
            origin = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            //out.setMethod(ZipOutputStream.DEFLATED);

            ZipEntry entry = new ZipEntry(file.getName());
            out.putNextEntry(entry);
            while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                out.write(data, 0, count);
            } // while()
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        } finally {
            DbConn.closeBStreamConn(origin, out);
        }

        return true;
    } // zipFile()

    //-----------------------------------------------------------------------------

    public static boolean zipFiles(String[] fileNames, String zipFileName) {
        BufferedInputStream origin = null;
        ZipOutputStream out = null;
        byte data[] = new byte[BUFFER_SIZE];
        int count;

        try {
            out = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFileName)));
            //out.setMethod(ZipOutputStream.DEFLATED);

            for (int i = 0; i < fileNames.length; i++) {
                File file = new File(fileNames[i]);
                origin = new BufferedInputStream(new FileInputStream(file), BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(file.getName());
                out.putNextEntry(entry);
                while ((count = origin.read(data, 0, BUFFER_SIZE)) != -1) {
                    out.write(data, 0, count);
                } // while()
            } // for()
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        } finally {
            DbConn.closeBStreamConn(origin, out);
        }

        return true;
    } // zipFile()

    //-----------------------------------------------------------------------------

    public static String getDataSourceFromBaseMapName(String dataSource, String baseMap) {
        int pos = baseMap.indexOf('.');
        if (pos <= 0)
            return dataSource;

        return baseMap.substring(0, pos);
    } // getDataSourceFromBaseMapName()

    //-----------------------------------------------------------------------------

    public static String extractWhereClauseAndAddPrefix(String sql, String prefix) {
        if (sql == null)
            return null;

        int pos = sql.indexOf("WHERE");
        if (pos < 0)
            pos = 0;
        else
            pos += 5;
        return sql.substring(pos)
                  .trim()
                  .replaceAll("TUR", prefix + ".TUR")
                  .replaceAll("BRAND_ID", prefix + ".BRAND_ID")
                  .replaceAll("TYPE", prefix + ".TYPE")
                  .replaceAll("SUB_TYPE", prefix + ".SUB_TYPE");
    } // extractWhereClauseAndAddPrefix()

    //-----------------------------------------------------------------------------

    public static String[] getSplitKeywords(String keyword) {
        String[] info = Utils.splitString(keyword, ",");
        if (info.length <= 2)
            return info;

        String txt = "";
        for (int i = 1; i < info.length; i++) {
            if (i > 1)
                txt += ",";
            txt += info[i];
        } // for()

        String[] res = new String[2];
        res[0] = info[0];
        res[1] = txt;
        return res;
    } // getSplitKeywords()

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

    //-----------------------------------------------------------------------------

    public static String getTruncatedString(String txt, int length) {
        if (txt == null)
            return null;

        if (txt.length() > length)
            return txt.substring(0, length);

        return txt;
    } // getTruncatedString()

    //-----------------------------------------------------------------------------
    public static Double truncCoords(Double coord) {
        Double retCoord = coord;
        String s = coord.toString();
        
        try {
            if (s.length() > 0 && s.indexOf('.') != -1) {
                int pos = s.indexOf('.');
                s = s.substring(pos+1, s.length());        
                if (s.length() > 5) {
                    DecimalFormat df = new DecimalFormat("##.######");
                    retCoord = Double.valueOf(df.format(coord));        
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return retCoord;
    } // truncCoords()

    //-----------------------------------------------------------------------------

    public static String  stringToSqlString( String keyword) {
        if (keyword == null || keyword.trim().equalsIgnoreCase("") || keyword.trim().length()<1)
            return null;
        
        String [] array = null;
        String s = "";
        
        try{  
          array = keyword.split(",");
          
            for (int i = 0; i < array.length; i++) {
                if (i > 0)  s += ",";
                
                if(i == 0)
                    s = "'" + array[i] + "'";
                else
                s += "'" + array[i] + "'";
            } // for()
        }catch (Exception e){
        ;        
        }
       return s;
    } // getTruncatedString()
    
    public static boolean isStringDataNull(String txt){
        if (txt == null  || txt.trim().length()<1 || txt.trim().equalsIgnoreCase(""))
            return true;
        return false;
    }
    
   public static String getStringData(String txt){
       if (txt == null) return "";
       return txt;
    }
    
    public static long convertStringToLongValue(String functionName, String variable){
      long value = 0;
      try {
          if(!isStringDataNull(variable)){
            value = Long.parseLong(variable);
          }
      }catch(Exception e){
        Utils.showError(functionName+ " getStringToLongValue variable("+variable+"): "+ e.getMessage());  
      }
     return value;
     }
     
     public static int convertStringToIntValue(String functionName, String variable, int defaultValue){
       int value = defaultValue;
       try {
           if(!isStringDataNull(variable)){
             value = Integer.parseInt(variable);
           }
       }catch(Exception e){
         Utils.showError(functionName+ " getStringToIntValue variable("+variable+"): "+ e.getMessage());  
       }
      return value;
      }
     
     public static int convertStringToIntValue(String functionName, String variable){
       int value = 0;
       try {
           if(!isStringDataNull(variable)){
             value = Integer.parseInt(variable);
           }
       }catch(Exception e){
         Utils.showError(functionName+ " getStringToIntValue variable("+variable+"): "+ e.getMessage());  
       }
      return value;
      }
      
     public static double convertStringToDoubleValue(String functionName, String variable){
       double value = 0.00;
       try {
           if(!isStringDataNull(variable)){
             value = Double.parseDouble(variable);
           }
       }catch(Exception e){
         Utils.showError(functionName+ " getStringToDoubleValue variable("+variable+"): "+ e.getMessage());  
       }
      return value;
      }
     
   public static JSONArray getJSONArrayValueFromJSONObject(JSONObject object, String variable, boolean option) {
       JSONArray array = null;
        try {
          array = object.getJSONArray(variable);
        } catch (Exception e) {
            if(!option){
              Utils.showError("getJSONArrayValueFromJSONObject.getJSONObject("+variable+"): "+ object + " " +e.getMessage());
            }
        }
        return array;
     }
     
     public static JSONObject getJSONObjectValueFromJSONObject(JSONObject object, String variable, boolean option) {
       JSONObject obj = null;
        try {
          obj = object.getJSONObject(variable);
        } catch (Exception e) {
            if(!option){
              Utils.showError("getJSONObjectValueFromJSONObject.getJSONObject("+variable+"): "+ object + " " +e.getMessage());
            }
        }
        return obj;
     }
     public static String getStringValueFromJSONObject(JSONObject object, String variable, boolean option){
      String value = "";
      try {
       value = object.getString(variable);
      }catch(Exception e){
         if(!option){
           Utils.showError("getStringValueFromJSONObject variable("+variable+"): "+ object + " " +e.getMessage());
         }
      }
      
     return value;
     }
     
    public static double getDoubleValueFromJSONObject(JSONObject object, String variable, boolean option){
     double value = 0.00;
     try {
      value = object.getDouble(variable);
     }catch(Exception e){
        if(!option){
           Utils.showError("getDoubleValueFromJSONObject variable("+variable+"): "+ object + " " +e.getMessage());
         }
     }
     
    return value;
    }
     
     public static boolean getBooleanValueFromJSONObject(JSONObject object, String variable, boolean option){
      boolean value = false;
      try {
       value = object.getBoolean(variable);
      }catch(Exception e){
          if(!option){
            Utils.showError("getBooleanValueFromJSONObject variable: " + variable + " " +e.getMessage());
          }
      }
      
     return value;
     }
     public static String commonGetRequest(String requestUrl){
         HttpURLConnection connection = null;
         String inputLine = null, res = "";
         StringBuffer response = new StringBuffer();
         BufferedReader in = null;
         try {
             URL lurl = new URL(requestUrl);
             connection = (HttpURLConnection) lurl.openConnection();
             connection.setDoOutput(false);
             connection.setDoInput(true);
             connection.setInstanceFollowRedirects(false);
             connection.setRequestMethod("GET");
             connection.setRequestProperty("Content-Type", "application/json");
             connection.setRequestProperty("charset", "UTF-8");
             connection.setRequestProperty("Content-Length", "" + Integer.toString("".getBytes().length));
             connection.setUseCaches(false);
             in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

             while ((inputLine = in.readLine()) != null) {
                 response.append(inputLine);
             }
             if(response != null){
               res = Utils.convUtf8ToTurkish(response.toString());
             }

         } catch (Exception e) {
             Utils.showError("commonGetRequest: " + requestUrl + " " +e.getMessage());
             return null;
         }finally{
             DbConn.closeHttpConn(connection, null, null, in);
         }
         return res;
     }


}
