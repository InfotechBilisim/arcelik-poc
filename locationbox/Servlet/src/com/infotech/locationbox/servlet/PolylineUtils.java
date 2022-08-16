package com.infotech.locationbox.servlet;

import java.util.ArrayList;

public class PolylineUtils {

  public PolylineUtils() {
  }

  //-----------------------------------------------------------------------------

  private static int floor1e5(double coordinate) {
    return (int) (Math.round(coordinate * 1e5));
  } // floor1e5()

  //-----------------------------------------------------------------------------

  private static String encodeSignedNumber(int num) {
    int snum = (num << 1);
    if (num < 0)
      snum = ~(snum);
    return (encodeNumber(snum));
  } // encodeSignedNumber()

  //-----------------------------------------------------------------------------

  private static String encodeNumber(int num) {
    StringBuffer encodeString = new StringBuffer();
    while (num >= 0x20) {
      int nextValue = (0x20 | (num & 0x1f)) + 63;
      if (nextValue == 92)
        encodeString.append((char) (nextValue));
      encodeString.append((char) (nextValue));
      num >>= 5;
    } // while()

    num += 63;
    if (num == 92)
      encodeString.append((char) (num));
    encodeString.append((char) (num));
    return encodeString.toString();
  } // encodeNumber()

  //-----------------------------------------------------------------------------

  public static String encodePolyline(double[] coors) {
    StringBuffer encodedPoints = new StringBuffer();

    int prevLat = 0;
    int prevLon = 0;
    for (int i = 0; i < coors.length / 2; i++) {
      int lat = floor1e5(coors[2 * i + 1]);
      int lon = floor1e5(coors[2 * i + 0]);
      int deltaLat = lat - prevLat;
      int deltaLon = lon - prevLon;
      prevLat = lat;
      prevLon = lon;

      encodedPoints.append(encodeSignedNumber(deltaLat));
      encodedPoints.append(encodeSignedNumber(deltaLon));
    } // for()

    return encodedPoints.toString();
  } // encodePolyline()

  //-----------------------------------------------------------------------------

  public static double[] decodePolyline(String encodedCoors) {
    int len = encodedCoors.length();
    int inx = 0;
    int lat = 0;
    int lon = 0;

    ArrayList array = new ArrayList();

    try {

      while (inx < len) {
        int b = 0;
        int shift = 0;
        int result = 0;
        do {
          b = encodedCoors.charAt(inx++) - 63;
          result |= (b & 0x1f) << shift;
          shift += 5;
        } while (b >= 0x20);

        int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lat += dlat;

        shift = 0;
        result = 0;
        do {
          b = encodedCoors.charAt(inx++) - 63;
          result |= (b & 0x1f) << shift;
          shift += 5;
        } while (b >= 0x20);
        int dlon = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
        lon += dlon;

        array.add(new Double((double) lon / 1E5));
        array.add(new Double((double) lat / 1E5));
      } // while()

    }
    catch (Exception ex) {
      ex.printStackTrace();
      Utils.showError("EXCEPTION: " + ex.getMessage());
      return null;
    }

    double[] coors = new double[array.size()];
    for (int i = 0; i < coors.length; i++)
      coors[i] = ((Double) array.get(i)).doubleValue();
    return coors;
  } // decodePolyline()

  //-----------------------------------------------------------------------------

  public static String encodeLevel(int level) {
    return encodeNumber(level);
  } // encodeLevel()

  //-----------------------------------------------------------------------------

  public static int decodeLevel(String encodedLevels) {
    int b = 0;
    int shift = 0;
    int result = 0;
    int inx = 0;
    do {
      b = encodedLevels.charAt(inx++) - 63;
      result |= (b & 0x1f) << shift;
      shift += 5;
    } while (b >= 0x20);

    return result;
  } // decodeLevel()

}
