package com.infotech.locationbox.servlet;

import java.sql.ResultSet;

public class DataWeatherReport {
    private long ilId = 0;
    private String ilAdi = null;
    private String weatherDate = null;
    private String condition = null;
    private double temp = 0.0;
    private double humidity = 0.0;
    private double pressure = 0.0;
    private double windSpeed = 0.0;
    private String windFrom = null;
    private double visibility = 0.0;
    private double lowTemp = 0.0;
    private double highTemp = 0.0;
    private String conditionForecast = null;
    private String day1WeatherDate = null;
    private String day1Condition = null;
    private double day1LowTemp = 0.0;
    private double day1HighTemp = 0.0;
    private String day2WeatherDate = null;
    private String day2Condition = null;
    private double day2LowTemp = 0.0;
    private double day2HighTemp = 0.0;
    private String day3WeatherDate = null;
    private String day3Condition = null;
    private double day3LowTemp = 0.0;
    private double day3HighTemp = 0.0;
    private String day4WeatherDate = null;
    private String day4Condition = null;
    private double day4LowTemp = 0.0;
    private double day4HighTemp = 0.0;

    public DataWeatherReport() {
    }

    public static DataWeatherReport getInstance(ResultSet rset) {
        DataWeatherReport dwr = new DataWeatherReport();

        try {
            dwr.ilId = rset.getLong("IL_ID");
            dwr.ilAdi = rset.getString("IL_ADI");
            dwr.condition = rset.getString("CURRENT_CONDITION");
            dwr.temp = rset.getDouble("CURRENT_TEMP");
            dwr.lowTemp = rset.getDouble("FORECAST_TEMP_LOW");
            dwr.highTemp = rset.getDouble("FORECAST_TEMP_HIGH");
            dwr.humidity = rset.getDouble("CURRENT_HUMIDITY");
            dwr.windSpeed = rset.getDouble("CURRENT_WINDSPEED");
            dwr.windFrom = rset.getString("CURRENT_WINDFROM");
            dwr.visibility = rset.getDouble("CURRENT_VISIBILITY");
            dwr.weatherDate = rset.getString("DAY");
            dwr.conditionForecast = rset.getString("FORECAST");
            dwr.day1WeatherDate = rset.getString("DAY1");
            dwr.day2WeatherDate = rset.getString("DAY2");
            dwr.day3WeatherDate = rset.getString("DAY3");
            dwr.day4WeatherDate = rset.getString("DAY4");
            dwr.day1Condition = rset.getString("DAYFORECAST_1");
            dwr.day2Condition = rset.getString("DAYFORECAST_2");
            dwr.day3Condition = rset.getString("DAYFORECAST_3");
            dwr.day4Condition = rset.getString("DAYFORECAST_4");
            dwr.day1LowTemp = rset.getDouble("FORECAST_TEMP_LOW_2");
            dwr.day2LowTemp = rset.getDouble("FORECAST_TEMP_LOW_3");
            dwr.day3LowTemp = rset.getDouble("FORECAST_TEMP_LOW_4");
            dwr.day4LowTemp = rset.getDouble("FORECAST_TEMP_LOW_5");
            dwr.day1HighTemp = rset.getDouble("FORECAST_TEMP_HIGH_2");
            dwr.day2HighTemp = rset.getDouble("FORECAST_TEMP_HIGH_3");
            dwr.day3HighTemp = rset.getDouble("FORECAST_TEMP_HIGH_4");
            dwr.day4HighTemp = rset.getDouble("FORECAST_TEMP_HIGH_5");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

        return dwr;
    } // getInstance()

    //-----------------------------------------------------------------------------

    public String toJson(String indent) {
        String s = "";
        s += indent + "{\n";
        s += indent + "  \"ilid\": " + ilId + ",\n";
        s += indent + "  \"iladi\": \"" + Utils.convStr2Json(ilAdi) + "\",\n";
        s += indent + "  \"weatherdate\": \"" + Utils.convStr2Json(weatherDate) + "\",\n";
        s += indent + "  \"condition\": \"" + Utils.convStr2Json(condition) + "\",\n";
        s += indent + "  \"conditionforecast\": \"" + Utils.convStr2Json(conditionForecast) + "\",\n";
        s += indent + "  \"temp\": " + temp + ",\n";
        s += indent + "  \"humidity\": " + humidity + ",\n";
        s += indent + "  \"pressure\": " + pressure + ",\n";
        s += indent + "  \"windspeed\": " + windSpeed + ",\n";
        s += indent + "  \"windfrom\": \"" + Utils.convStr2Json(windFrom) + "\",\n";
        s += indent + "  \"forecasts\": { \n";
        s +=
            indent + "    \"day1\": { \"condition\": \"" + Utils.convStr2Json(day1Condition) + "\", \"lowtemp\": " +
            day1LowTemp + ", \"hightemp\": " + day1HighTemp + " }, \n";
        s +=
            indent + "    \"day2\": { \"condition\": \"" + Utils.convStr2Json(day2Condition) + "\", \"lowtemp\": " +
            day2LowTemp + ", \"hightemp\": " + day2HighTemp + " }, \n";
        s +=
            indent + "    \"day3\": { \"condition\": \"" + Utils.convStr2Json(day3Condition) + "\", \"lowtemp\": " +
            day3LowTemp + ", \"hightemp\": " + day3HighTemp + " }, \n";
        s +=
            indent + "    \"day4\": { \"condition\": \"" + Utils.convStr2Json(day4Condition) + "\", \"lowtemp\": " +
            day4LowTemp + ", \"hightemp\": " + day4HighTemp + " } \n";
        s += "\n";
        s += indent + "  }";
        s += "\n";
        s += indent + "}";
        s += "\n";
        return s;
    } // toJson()

    //-----------------------------------------------------------------------------

    public String toXml() {
        String s = "";
        s += "    <weatherreport>\n";
        s += "      <ilid>" + ilId + "</ilid>\n";
        s += "      <iladi>" + Utils.convStr2Xml(ilAdi) + "</iladi>\n";
        s += "      <weatherdate>" + Utils.convStr2Xml(weatherDate) + "</weatherdate>\n";
        s += "      <condition>" + condition + "</condition>\n";
        s += "      <conditionforecast>" + Utils.convStr2Xml(conditionForecast) + "</conditionforecast>\n";
        s += "      <temp>" + temp + "</temp>\n";
        s += "      <humidity>" + humidity + "</humidity>\n";
        s += "      <pressure>" + pressure + "</pressure>\n";
        s += "      <windspeed>" + windSpeed + "</windspeed>\n";
        s += "      <windfrom>" + Utils.convStr2Xml(windFrom) + "</windfrom>\n";
        s += "      <visibility>" + visibility + "</visibility>\n";
        s += "      <forescasts>\n";
        s += "        <day1>\n";
        s += "          <condition>" + day1Condition + "</condition>\n";
        s += "          <lowtemp>" + day1LowTemp + "</lowtemp>\n";
        s += "          <hightemp>" + day1HighTemp + "</hightemp>\n";
        s += "        </day1>\n";
        s += "        <day2>\n";
        s += "          <condition>" + day2Condition + "</condition>\n";
        s += "          <lowtemp>" + day2LowTemp + "</lowtemp>\n";
        s += "          <hightemp>" + day2HighTemp + "</hightemp>\n";
        s += "        </day2>\n";
        s += "        <day3>\n";
        s += "          <condition>" + day3Condition + "</condition>\n";
        s += "          <lowtemp>" + day3LowTemp + "</lowtemp>\n";
        s += "          <hightemp>" + day3HighTemp + "</hightemp>\n";
        s += "        </day3>\n";
        s += "        <day4>\n";
        s += "          <condition>" + day4Condition + "</condition>\n";
        s += "          <lowtemp>" + day4LowTemp + "</lowtemp>\n";
        s += "          <hightemp>" + day4HighTemp + "</hightemp>\n";
        s += "        </day4>\n";
        s += "      </forescasts>\n";
        s += "    </weatherreport>\n";
        return s;
    } // toXml()

    //-----------------------------------------------------------------------------

    public void setIlId(long ilId) {
        this.ilId = ilId;
    }

    public long getIlId() {
        return ilId;
    }

    public void setIlAdi(String ilAdi) {
        this.ilAdi = ilAdi;
    }

    public String getIlAdi() {
        return ilAdi;
    }

    public void setCondition(String currentCondition) {
        this.condition = currentCondition;
    }

    public String getCondition() {
        return condition;
    }

    public void setTemp(double currentTemp) {
        this.temp = currentTemp;
    }

    public double getTemp() {
        return temp;
    }

    public void setHumidity(double currentHumidity) {
        this.humidity = currentHumidity;
    }

    public double getHumidity() {
        return humidity;
    }

    public void setPressure(double currentPressure) {
        this.pressure = currentPressure;
    }

    public double getPressure() {
        return pressure;
    }

    public void setWindSpeed(double currentWindSpeed) {
        this.windSpeed = currentWindSpeed;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindFrom(String currentWindFrom) {
        this.windFrom = currentWindFrom;
    }

    public String getWindFrom() {
        return windFrom;
    }

    public void setDay1WeatherDate(String day1WeatherDate) {
        this.day1WeatherDate = day1WeatherDate;
    }

    public String getDay1WeatherDate() {
        return day1WeatherDate;
    }

    public void setDay1Condition(String day1Condition) {
        this.day1Condition = day1Condition;
    }

    public String getDay1Condition() {
        return day1Condition;
    }

    public void setDay1LowTemp(double day1LowTemp) {
        this.day1LowTemp = day1LowTemp;
    }

    public double getDay1LowTemp() {
        return day1LowTemp;
    }

    public void setDay1HighTemp(double day1HighTemp) {
        this.day1HighTemp = day1HighTemp;
    }

    public double getDay1HighTemp() {
        return day1HighTemp;
    }

    public void setDay2WeatherDate(String day2WeatherDate) {
        this.day2WeatherDate = day2WeatherDate;
    }

    public String getDay2WeatherDate() {
        return day2WeatherDate;
    }

    public void setDay2Condition(String day2Condition) {
        this.day2Condition = day2Condition;
    }

    public String getDay2Condition() {
        return day2Condition;
    }

    public void setDay2LowTemp(double day2LowTemp) {
        this.day2LowTemp = day2LowTemp;
    }

    public double getDay2LowTemp() {
        return day2LowTemp;
    }

    public void setDay2HighTemp(double day2HighTemp) {
        this.day2HighTemp = day2HighTemp;
    }

    public double getDay2HighTemp() {
        return day2HighTemp;
    }

    public void setDay3WeatherDate(String day3WeatherDate) {
        this.day3WeatherDate = day3WeatherDate;
    }

    public String getDay3WeatherDate() {
        return day3WeatherDate;
    }

    public void setDay3Condition(String day3Condition) {
        this.day3Condition = day3Condition;
    }

    public String getDay3Condition() {
        return day3Condition;
    }

    public void setDay3LowTemp(double day3LowTemp) {
        this.day3LowTemp = day3LowTemp;
    }

    public double getDay3LowTemp() {
        return day3LowTemp;
    }

    public void setDay3HighTemp(double day3HighTemp) {
        this.day3HighTemp = day3HighTemp;
    }

    public double getDay3HighTemp() {
        return day3HighTemp;
    }

    public void setDay4WeatherDate(String day4WeatherDate) {
        this.day4WeatherDate = day4WeatherDate;
    }

    public String getDay4WeatherDate() {
        return day4WeatherDate;
    }

    public void setDay4Condition(String day4Condition) {
        this.day4Condition = day4Condition;
    }

    public String getDay4Condition() {
        return day4Condition;
    }

    public void setDay4LowTemp(double day4LowTemp) {
        this.day4LowTemp = day4LowTemp;
    }

    public double getDay4LowTemp() {
        return day4LowTemp;
    }

    public void setDay4HighTemp(double day4HighTemp) {
        this.day4HighTemp = day4HighTemp;
    }

    public double getDay4HighTemp() {
        return day4HighTemp;
    }

    public void setWeatherDate(String weatherDate) {
        this.weatherDate = weatherDate;
    }

    public String getWeatherDate() {
        return weatherDate;
    }

    public void setLowTemp(double lowTemp) {
        this.lowTemp = lowTemp;
    }

    public double getLowTemp() {
        return lowTemp;
    }

    public void setHighTemp(double highTemp) {
        this.highTemp = highTemp;
    }

    public double getHighTemp() {
        return highTemp;
    }

    public void setConditionForecast(String forecastCondition) {
        this.conditionForecast = forecastCondition;
    }

    public String getConditionForecast() {
        return conditionForecast;
    }

    public void setVisibility(double visibility) {
        this.visibility = visibility;
    }

    public double getVisibility() {
        return visibility;
    }
}
