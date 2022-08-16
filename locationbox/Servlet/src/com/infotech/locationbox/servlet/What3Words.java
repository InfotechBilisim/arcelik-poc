package com.infotech.locationbox.servlet;

public class What3Words {
    private String words = null;
    private double latitude = 0.00;
    private double longitude = 0.00;
    private String lang = null;
    private String type = null;
    private Extent extent = new Extent();
    
  public What3Words() {
      super();
  }

  public What3Words(String words, double latitude, double longitude, String lang, String type) {
      this.words = words;
      this.latitude = latitude;
      this.longitude = longitude;
      this.lang = lang;
      this.type = type;
  }

  public What3Words(String words, double latitude, double longitude, String lang) {
    super();
    this.words = words;
    this.latitude = latitude;
    this.longitude = longitude;
    this.lang = lang;
  }
  
  public String toJson() {
    String result = "";
    
    if (type != null && !type.isEmpty()) result += "\"type\": \"" + type + "\", ";
    if (words != null && words.length() > 0)  result += "\"words\": \"" + Utils.convStr2Json(words) + "\" , ";
    
    
    result +="\"latitude\": " + Utils.makeCoorFormat(latitude) + ", \"longitude\": " + Utils.makeCoorFormat(longitude) + ",";
    result +="\"language\": \"" + lang + "\" ";
    
    return result;
  } // toJson()

  //-----------------------------------------------------------------------------

  public String toXml() {
    String result = "";
    
    if(type != null && !type.isEmpty()) result = "<type>" + type + "</type>\n";
    if (words != null && words.length() > 0)  result += "<words>" + words + "</words>\n";
    
    result += "<latitude>" + Utils.makeCoorFormat(latitude) + "</latitude>\n";
    result += "<longitude>" + Utils.makeCoorFormat(longitude) + "</longitude>\n";
    result += "<language>" + lang + "</language>\n";
    
    return result;
  } // toXml()
  public void setWords(String words) {
        this.words = words;
    }

  public String getWords() {
      return words;
  }

  public void setLatitude(double latitude) {
      this.latitude = latitude;
  }

  public double getLatitude() {
      return latitude;
  }

  public void setLongitude(double longitude) {
      this.longitude = longitude;
  }

  public double getLongitude() {
      return longitude;
  }

  public void setLang(String lang) {
      this.lang = lang;
  }

  public String getLang() {
      return lang;
  }

  public void setType(String type) {
      this.type = type;
  }

  public String getType() {
      return type;
  }

  public void setExtent(Extent extent) {
    this.extent = extent;
  }

  public Extent getExtent() {
    return extent;
  }
}
