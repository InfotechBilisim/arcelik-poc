package com.infotech.locationbox.servlet;

public class DataReverseGlobalGeocode extends GlobalGeocode {
    private String countrySubdivision = null;
    private String countrySecondarySubdivision = null;
    public DataReverseGlobalGeocode(String city, String streetName, String countryCode, String municipality, String countryCodeISO3, String countrySubdivision, String countrySecondarySubdivision,  String country, String address, String postCode, double latitude, double longitude) {
        super(city, streetName, countryCode, municipality, country, countryCodeISO3, postCode, latitude, longitude);
        setAddress(address);
        setCountrySubdivision(countrySubdivision);
        setCountrySecondarySubdivision(countrySecondarySubdivision);
    }

    public String toJson() {
        StringBuilder s = new StringBuilder();
        s.append("{\n");
        s.append("  \"countryCode\": \"" + Utils.getStringData(getCountryCode()) + "\",\n");
        s.append("  \"municipality\": \"" + Utils.getStringData(getMunicipality()) + "\",\n");
        s.append("  \"city\": \"" + Utils.getStringData(getCity()) + "\",\n");
        s.append("  \"street\": \"" + Utils.getStringData(getStreet()) + "\",\n");
        s.append("  \"countryCodeISO3\": \"" + Utils.getStringData(getCountryCodeISO3()) + "\",\n");
        s.append("  \"address\": \"" +  Utils.getStringData(getAddress()) + "\",\n");
        s.append("  \"latitude\": " + getLatitude() + ",\n");
        s.append("  \"longitude\": " + getLongitude() + ",\n");
        s.append("  \"country\": \"" +  Utils.getStringData(getCountry()) + "\",\n");
        s.append("  \"postcode\": \"" +  Utils.getStringData(getPostCode()) + "\",\n");
        s.append("  \"countrySubdivision\": \"" +  Utils.getStringData(getCountrySubdivision()) + "\",\n");
        s.append("  \"countrySecondarySubdivision\": \"" +  Utils.getStringData(getCountrySecondarySubdivision()) + "\"\n");
        s.append("}\n");
        return s.toString();
    }

    //----------------------------------------------------------------------------

    public String toXml() {
        StringBuilder s = new StringBuilder();
        s.append("    <countryCode>" +  Utils.getStringData(getCountryCode()) + "</countryCode>\n");
        s.append("    <municipality>" +  Utils.getStringData(getMunicipality()) + "</municipality>\n");
        s.append("    <city>" +  Utils.getStringData(getCity()) + "</city>\n");
        s.append("    <street>" +  Utils.getStringData(getStreet()) + "</street>\n");
        s.append("    <countryCodeISO3>" +  Utils.getStringData(getCountryCodeISO3()) + "</countryCodeISO3>\n");
        s.append("    <address>" +  Utils.getStringData(getAddress()) + "</address>\n");
        s.append("    <latitude>" + getLatitude() + "</latitude>\n");
        s.append("    <longitude>" + getLongitude() + "</longitude>\n");
        s.append("    <country>" +  Utils.getStringData(getCountry()) + "</country>\n");
        s.append("    <postcode>" +  Utils.getStringData(getPostCode()) + "</postcode>\n");
        s.append("    <countrySubdivision>" +  Utils.getStringData(getCountrySubdivision()) + "</countrySubdivision>\n");
        s.append("    <countrySecondarySubdivision>" +  Utils.getStringData(getCountrySecondarySubdivision()) + "</countrySecondarySubdivision>\n");
        return s.toString();
    } 


    public void setCountrySubdivision(String countrySubdivision) {
        this.countrySubdivision = countrySubdivision;
    }

    public String getCountrySubdivision() {
        return countrySubdivision;
    }

    public void setCountrySecondarySubdivision(String countrySecondarySubdivision) {
        this.countrySecondarySubdivision = countrySecondarySubdivision;
    }

    public String getCountrySecondarySubdivision() {
        return countrySecondarySubdivision;
    }
}
