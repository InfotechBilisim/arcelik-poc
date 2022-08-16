package com.infotech.locationbox.tracking.platform.base;

public class Position extends Object {
  String dateTime = null;
  char source = ' ';
  double latitude = 0.0;
  double longitude = 0.0;
  int altitude = 0;
  int cellId = 0;
  boolean gpsStatus = false;
  double distanceTravelled = 0.0;
  double totalDistanceTravelled = 0.0;
  int speed = 0;
  int direction = 0;
  int digital = 0;
  double analog1 = 0.00;
  double analog2 = 0.00;
  double analog3 = 0.00;
  double analog4 = 0.00;
  double analog5 = 0.00;
  double analog6 = 0.00;
  int deltaDuration = 0;

  public Position() {
  }

  public Position(double lat, double lon, int speed, int direction, boolean gpsStatus, int digital, double analog1, double analog2) {
    this.latitude = lat;
    this.longitude = lon;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.analog1 = analog1;
    this.analog2 = analog2;
    this.deltaDuration = 0;
  }

  public Position(double latitude, double longitude, int altitude, int cellId, int speed, int direction, boolean gpsStatus, int digital, double analog1, double analog2) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.cellId = cellId;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.analog1 = analog1;
    this.analog2 = analog2;
    this.deltaDuration = 0;
  }

  public Position(double lat, double lon, int speed, int direction, boolean gpsStatus, int digital, double analog1, double analog2, double analog3, double analog4, double analog5, double analog6) {
    this.latitude = lat;
    this.longitude = lon;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.analog1 = analog1;
    this.analog2 = analog2;
    this.analog3 = analog3;
    this.analog4 = analog4;
    this.analog5 = analog5;
    this.analog6 = analog6;
    this.deltaDuration = 0;
  }

  public Position(double latitude, double longitude, int altitude, int cellId, int speed, int direction, boolean gpsStatus, int digital, double analog1, double analog2, double analog3, double analog4, double analog5, double analog6) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.cellId = cellId;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.analog1 = analog1;
    this.analog2 = analog2;
    this.analog3 = analog3;
    this.analog4 = analog4;
    this.analog5 = analog5;
    this.analog6 = analog6;
    this.deltaDuration = 0;
  }

  public Position(double lat, double lon, int speed, int direction, boolean gpsStatus, int digital, char posSource, int deltaDuration) {
    this.latitude = lat;
    this.longitude = lon;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.source = posSource;
    this.deltaDuration = deltaDuration;
  }

  public Position(double latitude, double longitude, int altitude, int cellId, int speed, int direction, boolean gpsStatus, int digital, char posSource, int deltaDuration) {
    this.latitude = latitude;
    this.longitude = longitude;
    this.altitude = altitude;
    this.cellId = cellId;
    this.speed = speed;
    this.direction = direction;
    this.gpsStatus = gpsStatus;
    this.digital = digital;
    this.source = posSource;
    this.deltaDuration = deltaDuration;
  }

  public String getDateTime() { return( dateTime ); }
  public char getSource() { return( source ); }
  public double getLatitude() { return( latitude ); }
  public double getLongitude() { return( longitude ); }
  public int getAltitude() { return( altitude ); }
  public int getCellId() { return( cellId ); }
  public boolean getGpsStatus() { return( gpsStatus ); }
  public double getDistanceTravelled() { return distanceTravelled; }
  public double getTotalDistanceTravelled() { return totalDistanceTravelled; }
  public int getSpeed() { return( speed ); }
  public int getDirection() { return( direction ); }
  public int getDigital() { return( digital ); }
  public double getAnalog1() { return( analog1 ); }
  public double getAnalog2() { return( analog2 ); }
  public double getAnalog3() { return( analog3 ); }
  public double getAnalog4() { return( analog4 ); }
  public double getAnalog5() { return( analog5 ); }
  public double getAnalog6() { return( analog6 ); }
  public int getDeltaDuration() { return( deltaDuration ); }

  public void setDateTime(String dateTime) { this.dateTime = dateTime; }
  public void setSource(char source) { this.source = source; }
  public void setLatitude(double latitude) { this.latitude = latitude; }
  public void setLongitude(double longitude) { this.longitude = longitude; }
  public void setAltitude(int altitude) { this.altitude = altitude; }
  public void setCellId(int cellId) { this.cellId = cellId; }
  public void setGpsStatus(boolean gpsStatus) { this.gpsStatus = gpsStatus; }
  public void setDistanceTravelled(double distanceTravelled) { this.distanceTravelled = distanceTravelled; }
  public void setTotalDistanceTravelled(double totalDistanceTravelled) { this.totalDistanceTravelled = totalDistanceTravelled; }
  public void setSpeed(int speed) { this.speed = speed; }
  public void setDirection(int direction) { this.direction = direction; }
  public void setDigital(int digital) { this.digital = digital; }
  public void setAnalog1(int analog1) { this.analog1 = analog1; }
  public void setAnalog2(int analog2) { this.analog2 = analog2; }
  public void setDeltaDuration(int deltaDuration) { this.deltaDuration = deltaDuration; }

}

