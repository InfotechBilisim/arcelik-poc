package com.infotech.locationbox.servlet;

public class GlobalGeocode {
    private String countryCode = null;
    private String street = null;
    private String city = null;
    private String municipality = null;
    private String country = null;
    private String countryCodeISO3 = null;
    private String address = null;
    private String postCode = null;
    private double latitude = 0.00;
    private double longitude = 0.00;
   

    public GlobalGeocode(String city, String streetName, String countryCode, String municipality, String country, String countryCodeISO3, String postCode, double latitude, double longitude) {
        setConstructor(city, streetName, countryCode, municipality, country, countryCodeISO3, postCode, latitude, longitude);
    }
    
    private void setConstructor(String city, String streetName, String countryCode, String municipality, String country, String countryCodeISO3, String postCode, double latitude, double longitude) {
        this.countryCode = countryCode;
        this.municipality = municipality;
        this.country = country;
        this.countryCodeISO3 = countryCodeISO3;
        this.postCode = postCode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.street = streetName;
        this.city = city;
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

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCountry() {
        return country;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setMunicipality(String municipality) {
        this.municipality = municipality;
    }

    public String getMunicipality() {
        return municipality;
    }

    public void setCountryCodeISO3(String countryCodeISO3) {
        this.countryCodeISO3 = countryCodeISO3;
    }

    public String getCountryCodeISO3() {
        return countryCodeISO3;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

}
