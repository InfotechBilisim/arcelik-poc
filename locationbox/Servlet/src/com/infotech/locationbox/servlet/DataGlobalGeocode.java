package com.infotech.locationbox.servlet;


public class DataGlobalGeocode extends GlobalGeocode{
    private String houseNumber = null;
    private String roadType = null;
    private String state = null;
    private String formattedAddress = null;
    private double score = 0.0;
    private double confidence = 0.0;


    public DataGlobalGeocode(String countryCode, String municipality, String countryCodeISO3, double latitude, double longitude, String houseNumber, String roadType, String street, String city, String state, String country, String postCode, String formattedAddress, double score, double confidence) {
        super(city, street, countryCode, municipality, country, countryCodeISO3, postCode, latitude, longitude);
        this.houseNumber = houseNumber;
        this.roadType = roadType;
        this.state = state;
        this.formattedAddress = formattedAddress;
        this.score = score;
        this.confidence = confidence;
    }

    //----------------------------------------------------------------------------

    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        s.append("  \"countryCode\": \"" +  Utils.getStringData(getCountryCode()) + "\",\n");
        s.append("  \"municipality\": \"" +  Utils.getStringData(getMunicipality()) + "\",\n");
        s.append("  \"countryCodeISO3\": \"" +  Utils.getStringData(getCountryCodeISO3()) + "\",\n");
        s.append("  \"address\": \"" +  Utils.getStringData(getAddress()) + "\",\n");
        s.append("  \"latitude\": " + getLatitude() + ",\n");
        s.append("  \"longitude\": " + getLongitude() + ",\n");
        s.append("  \"formattedAddress\": \"" +  Utils.getStringData(formattedAddress) + "\",\n");
        s.append("  \"type\": \"" +  Utils.getStringData(roadType) + "\",\n");
        s.append("  \"street\": \"" +  Utils.getStringData(getStreet())  + "\",\n");
        s.append("  \"city\": \"" +  Utils.getStringData(getCity()) + "\",\n");
        s.append("  \"state\": \"" +  Utils.getStringData(state) + "\",\n");
        s.append("  \"country\": \"" +  Utils.getStringData(getCountry()) + "\",\n");
        s.append("  \"postcode\": \"" +  Utils.getStringData(getPostCode()) + "\",\n");
        s.append("  \"score\": \"" + score + "\",\n");
        s.append("  \"confidence\": \"" + confidence + "\"\n");
        s.append("}\n");
        return s.toString();
    }

    //----------------------------------------------------------------------------

    public String toXml() {
        StringBuilder s = new StringBuilder();
        s.append("    <countryCode>" +  Utils.getStringData(getCountryCode()) + "</countryCode>\n");
        s.append("    <municipality>" +  Utils.getStringData(getMunicipality()) + "</municipality>\n");
        s.append("    <countryCodeISO3>" +  Utils.getStringData(getCountryCodeISO3()) + "</countryCodeISO3>\n");
        s.append("    <address>" +  Utils.getStringData(getAddress()) + "</address>\n");
        s.append("    <latitude>" + getLatitude() + "</latitude>\n");
        s.append("    <longitude>" + getLongitude() + "</longitude>\n");
        s.append("    <formattedaddress>" +  Utils.getStringData(formattedAddress) + "</formattedaddress>\n");
        s.append("    <type>" +  Utils.getStringData(roadType) + "</type>\n");
        s.append("    <street>" +  Utils.getStringData(getStreet()) + "</street>\n");
        s.append("    <city>" +  Utils.getStringData(getCity()) + "</city>\n");
        s.append("    <state>" +  Utils.getStringData(state) + "</state>\n");
        s.append("    <country>" +  Utils.getStringData(getCountry()) + "</country>\n");
        s.append("    <postcode>" +  Utils.getStringData(getPostCode()) + "</postcode>\n");
        s.append("    <score>" + score + "</score>\n");
        s.append("    <confidence>" + confidence + "</confidence>\n");
        return s.toString();
    } 

}
