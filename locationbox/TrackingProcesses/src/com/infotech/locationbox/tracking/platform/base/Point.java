package com.infotech.locationbox.tracking.platform.base;

public class Point extends Object implements Cloneable {
  private double latitude = 0.00;
  private double longitude = 0.00;

  public Point() {
  }

  public Point(double lat, double lon) {
    this.latitude = lat;
    this.longitude = lon;
  }

//-----------------------------------------------------------------------------

  public double getLatitude() {
    return latitude;
  }

  public void setLatitude(double newLatitude) {
    latitude = newLatitude;
    return;
  }

  public double getLongitude() {
    return longitude;
  }

  public void setLongitude(double newLongitude) {
    longitude = newLongitude;
    return;
  }

  public Object clone() {
    Point c = new Point();
    c.latitude = this.latitude;
    c.longitude = this.longitude;
    return( c );
  }

}


