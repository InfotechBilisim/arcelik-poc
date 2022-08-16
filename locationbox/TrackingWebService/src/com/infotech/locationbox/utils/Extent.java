package com.infotech.locationbox.utils;

public class Extent {
  protected double xmin = 0.00;
  protected double ymin = 0.00;
  protected double xmax = 0.00;
  protected double ymax = 0.00;

  public Extent() {
  }

  public Extent(double xmin, double ymin, double xmax, double ymax) {
    this.xmin = xmin;
    this.ymin = ymin;
    this.xmax = xmax;
    this.ymax = ymax;
  }
  
//-----------------------------------------------------------------------------

  public String toString() {
    return "{ \"xmin\": " + xmin + ", \"ymin\": " + ymin +  ", \"xmax\": " + xmax + ", \"ymax\": " + ymax + " }";
  }

//-----------------------------------------------------------------------------

  public double getCenterX() {
    return (xmin + xmax) / 2.0;
  }

//-----------------------------------------------------------------------------

  public double getCenterY() {
    return (ymin + ymax) / 2.0;
  }

}
