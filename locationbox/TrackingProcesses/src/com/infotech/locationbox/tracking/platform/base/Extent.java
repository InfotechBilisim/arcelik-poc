package com.infotech.locationbox.tracking.platform.base;

public class Extent {
  private double minLatitude  = 0.00;
  private double minLongitude = 0.00;
  private double maxLatitude  = 0.00;
  private double maxLongitude = 0.00;

  public static final int N  = 1;
  public static final int NW = 2;
  public static final int NE = 3;
  public static final int S  = 4;
  public static final int SW = 5;
  public static final int SE = 6;
  public static final int E  = 7;
  public static final int W  = 8;

  public Extent() {
  }
  
  public Extent(double minLatitude, double minLongitude, double maxLatitude, double maxLongitude) {
    if( minLatitude > maxLatitude || minLongitude > maxLongitude ) {
      Log.showText("Invalid Extent !");
      return;
    }
    
    this.minLatitude  = minLatitude;
    this.minLongitude = minLongitude;
    this.maxLatitude  = maxLatitude;
    this.maxLongitude = maxLongitude;
  }

  public Extent(double midLatitude, double midLongitude, double size, int width, int height) {
    this.minLatitude  = midLatitude - (size / 2.00);
    this.minLongitude = midLongitude - (size / 2.00) * ((double)width/(double)height);
    this.maxLatitude  = midLatitude + (size / 2.00);
    this.maxLongitude = midLongitude + (size / 2.00) * ((double)width/(double)height);
  }

//-----------------------------------------------------------------------------
  
  public Extent translateToVirtual() {
    return translateToVirtual(this);
  } // translateToVirtual()

  public static Extent translateToVirtual(Extent ext) {
    double minLat = (long)(ext.getMinLatitude()  * 10000.0 + 0.5) - 400000.0;
    double maxLat = (long)(ext.getMaxLatitude()  * 10000.0 + 0.5) - 400000.0;
    double minLon = (long)(ext.getMinLongitude() * 10000.0 + 0.5) - 280000.0;
    double maxLon = (long)(ext.getMaxLongitude() * 10000.0 + 0.5) - 280000.0;
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // translateToVirtual()

//-----------------------------------------------------------------------------
  
  public Extent translateToReal() {
    return translateToReal(this);
  } // translateToReal()

  public static Extent translateToReal(Extent ext) {
    double minLat = (ext.getMinLatitude()  + 400000.0) / 10000.0;
    double maxLat = (ext.getMaxLatitude()  + 400000.0) / 10000.0;
    double minLon = (ext.getMinLongitude() + 280000.0) / 10000.0;
    double maxLon = (ext.getMaxLongitude() + 280000.0) / 10000.0;
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // translateToReal()

//-----------------------------------------------------------------------------

  public boolean equals(Extent ext) {
    if( Math.abs(minLatitude - ext.getMinLatitude()) > 0.00001 ) return false;
    if( Math.abs(minLongitude - ext.getMinLongitude()) > 0.00001 ) return false;
    if( Math.abs(maxLatitude - ext.getMaxLatitude()) > 0.00001 ) return false;
    if( Math.abs(maxLongitude - ext.getMaxLongitude()) > 0.00001 ) return false;
    return true;
  } // equals()

//-----------------------------------------------------------------------------

  public Extent scaleExtent(double scale) {
    return scaleExtent(this, scale);
  } // scaleExtent()

  public static Extent scaleExtent(Extent ext, double scale) {
    double midLat = (ext.getMinLatitude() + ext.getMaxLatitude()) / 2.0;
    double midLon = (ext.getMinLongitude() + ext.getMaxLongitude()) / 2.0;
    double offLat = Math.abs((ext.getMaxLatitude() - ext.getMinLatitude()) * scale) / 2.0;
    double offLon = Math.abs((ext.getMaxLongitude() - ext.getMinLongitude()) * scale) / 2.0;
    return( new Extent(midLat - offLat, midLon - offLon, midLat + offLat, midLon + offLon) );
  } // scaleExtent()
  
//-----------------------------------------------------------------------------

  public Extent widenExtent(double offset) {
    return widenExtent(this, offset);
  } // widenExtent()

  public static Extent widenExtent(Extent ext, double offset) {
    double midLat = (ext.getMinLatitude() + ext.getMaxLatitude()) / 2.0;
    double midLon = (ext.getMinLongitude() + ext.getMaxLongitude()) / 2.0;
    double offLat = Math.abs(ext.getMaxLatitude() - ext.getMinLatitude()) / 2.0 + offset;
    double offLon = Math.abs(ext.getMaxLongitude() - ext.getMinLongitude()) / 2.0 + offset;
    return( new Extent(midLat - offLat, midLon - offLon, midLat + offLat, midLon + offLon) );
  } // widenExtent()
  
//-----------------------------------------------------------------------------

  public Extent extrapolate(int x1, int y1, int x2, int y2, int w, int h) {
    double mulLat = (maxLatitude - minLatitude) / h;
    double mulLon = (maxLongitude - minLongitude) / w;
    double minLat = minLatitude + (h - (y1 > y2 ? y1 : y2)) * mulLat; // Since in screen coordinates 0,0 is top-left.
    double minLon = minLongitude + (x1 < x2 ? x1 : x2) * mulLon;
    double maxLat = minLatitude + (h - (y1 < y2 ? y1 : y2)) * mulLat; // Since in screen coordinates 0,0 is top-left.
    double maxLon = minLongitude + (x1 > x2 ? x1 : x2) * mulLon;
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // extrapolate()

//-----------------------------------------------------------------------------

  public Extent extrapolate(int x, int y, int w, int h) {
    double mulLat = (maxLatitude - minLatitude) / h;
    double mulLon = (maxLongitude - minLongitude) / w;
    double midLat = minLatitude + (h - y) * mulLat;
    double midLon = minLongitude + x * mulLon;
    double offLat = (maxLatitude - minLatitude) / 2.0;
    double offLon = (maxLongitude - minLongitude) / 2.0;
    return( new Extent(midLat - offLat, midLon - offLon, midLat + offLat, midLon + offLon) );
  } // extrapolate()

//-----------------------------------------------------------------------------

  public Extent expandWithPoint(double latitude, double longitude) {
    double minLat = minLatitude;
    double minLon = minLongitude;
    double maxLat = maxLatitude;
    double maxLon = maxLongitude;
    if( latitude > maxLat ) maxLat = latitude;
    if( latitude < minLat ) minLat = latitude;
    if( longitude > maxLon ) maxLon = longitude;
    if( longitude < minLon ) minLon = longitude;
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // expandWithPoint()

//-----------------------------------------------------------------------------

  public Extent Union(Extent ext) {
    if( ext == null ) return this;
    
    double minLat = minLatitude;
    double minLon = minLongitude;
    double maxLat = maxLatitude;
    double maxLon = maxLongitude;
    if( ext.maxLatitude > maxLat ) maxLat = ext.maxLatitude;
    if( ext.minLatitude < minLat ) minLat = ext.minLatitude;
    if( ext.maxLongitude > maxLon ) maxLon = ext.maxLongitude;
    if( ext.minLongitude < minLon ) minLon = ext.minLongitude;
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // Union()

//-----------------------------------------------------------------------------

  public Extent moveExtent(int way) {
    double offLat = ((maxLatitude - minLatitude) * 1.0) / 2.0;
    double offLon = ((maxLongitude - minLongitude) * 1.0) / 2.0;

    double minLat = minLatitude;
    double minLon = minLongitude;
    double maxLat = maxLatitude;
    double maxLon = maxLongitude;

    switch( way ) {
    case N  : minLat += offLat; maxLat += offLat; break;
    case NW : minLat += offLat; maxLat += offLat; minLon -= offLon; maxLon -= offLon; break;
    case NE : minLat += offLat; maxLat += offLat; minLon += offLon; maxLon += offLon; break;
    case S  : minLat -= offLat; maxLat -= offLat; break;
    case SW : minLat -= offLat; maxLat -= offLat; minLon -= offLon; maxLon -= offLon; break;
    case SE : minLat -= offLat; maxLat -= offLat; minLon += offLon; maxLon += offLon; break;
    case E  : minLon += offLon; maxLon += offLon; break;
    case W  : minLon -= offLon; maxLon -= offLon; break;
    } // switch()
    return( new Extent(minLat, minLon, maxLat, maxLon) );
  } // moveExtent()

//-----------------------------------------------------------------------------

  public Extent zoomLevelExtent(int zoomLevel, int height, int width) {
    double xoffs = 0.00;

    switch( zoomLevel ) {
    case 1 :  xoffs = 0.001; break;
    case 2 :  xoffs = 0.003; break;
    case 3 :  xoffs = 0.007; break;
    case 4 :  xoffs = 0.014; break;
    case 5 :  xoffs = 0.028; break;
    case 6 :  xoffs = 0.056; break;
    case 7 :  xoffs = 0.113; break;
    case 8 :  xoffs = 0.226; break;
    case 9 :  xoffs = 0.453; break;
    case 10 : xoffs = 0.906; break;
    default :
      xoffs = 0.028;
      break;
    } // switch()

    double yoffs = xoffs * ((double)height / (double)width);

    double lat = (minLatitude + maxLatitude) / 2.0;
    double lon = (minLongitude + maxLongitude) / 2.0;

    double xul = lon - xoffs;
    double xlr = lon + xoffs;
    double ylr = lat - yoffs;
    double yul = lat + yoffs;
    
    return( new Extent(ylr, xul, yul, xlr) );
  } // zoomLevelExtent()

//-----------------------------------------------------------------------------

  public boolean isNull() {
    return (minLatitude == 0.00) && (minLongitude == 0.00) && (maxLatitude == 0.00) && (maxLongitude == 0.00);
  } // isNull()
  
//-----------------------------------------------------------------------------

  public void setNull() {
    minLatitude  = 0.00;
    minLongitude = 0.00;
    maxLatitude  = 0.00;
    maxLongitude = 0.00;
    return;
  } // setNull()
  
//-----------------------------------------------------------------------------

  public String toString() {
    return "(EXTENT: " + minLatitude + "/" + minLongitude + " - " + maxLatitude + "/" + maxLongitude + ")";
  } // toString()
  
//-----------------------------------------------------------------------------

  public double getCenterX() {
    return (minLongitude + maxLongitude) / 2.0;
  }
  
  public double getCenterY() {
    return (minLatitude + maxLatitude) / 2.0;
  }
  
  public double getSize() {
    double deltaLon = Math.abs(maxLongitude - minLongitude);
    double deltaLat = Math.abs(maxLatitude - minLatitude);
    if( deltaLat > deltaLon )
      return deltaLat;
    else
      return deltaLon;
  } // getSize()
  
  public double getSize(int width, int height) {
    double deltaLon = Math.abs(maxLongitude - minLongitude);
    double deltaLat = Math.abs(maxLatitude - minLatitude);
    if( deltaLon > deltaLat ) {
      if( height < width )
        return( deltaLon );
      else 
        return( deltaLon * ((double)height / (double)width));
    }
    else {
      if( height < width )
        return( deltaLat );
      else
        return( deltaLat * ((double)height / (double)width));
    }
  } // getSize()
  
//-----------------------------------------------------------------------------

  public Extent makeSquare() {
    double deltaLat = Math.abs(maxLatitude - minLatitude);
    double offsLon = (deltaLat / 2);
    double centerLon = (maxLongitude + minLongitude) / 2;
    double minLon = centerLon - offsLon;
    double maxLon = centerLon + offsLon;
    return( new Extent(minLatitude, minLon, maxLatitude, maxLon) );
  } // makeSquare()
  
//-----------------------------------------------------------------------------
  
  public double getMinLatitude() {
    return minLatitude;
  }
  
  public void setMinLatitude(double minLatitude) {
    this.minLatitude = minLatitude;
  }
  
  public double getMinLongitude() {
    return minLongitude;
  }
  
  public void setMinLongitude(double minLongitude) {
    this.minLongitude = minLongitude;
  }
  
  public double getMaxLatitude() {
    return maxLatitude;
  }
  
  public void setMaxLatitude(double maxLatitude) {
    this.maxLatitude = maxLatitude;
  }
  
  public double getMaxLongitude() {
    return maxLongitude;
  }
  
  public void setMaxLongitude(double maxLongitude) {
    this.maxLongitude = maxLongitude;
  }
  
}
