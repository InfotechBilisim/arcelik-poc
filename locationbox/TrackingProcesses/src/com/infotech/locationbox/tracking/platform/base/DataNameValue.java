package com.infotech.locationbox.tracking.platform.base;

public class DataNameValue {
  private String name = null;
  private String value = null;
  
  public DataNameValue() {
  }
  
  public DataNameValue(String name, String value) {
    this.name = name;
    this.value = value;
  }

//-----------------------------------------------------------------------------

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }
  
}
