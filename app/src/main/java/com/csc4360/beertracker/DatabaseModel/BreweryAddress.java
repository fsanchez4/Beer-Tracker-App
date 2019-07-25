package com.csc4360.beertracker.DatabaseModel;

public class BreweryAddress {

    private String streetNumber;
    private String streetName;
    private String city;
    private String state;
    private String zipCode;

    public BreweryAddress(String streetNumber, String streetName, String city, String state,
                          String zipCode) {
        this.streetNumber = streetNumber;
        this.streetName = streetName;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public String getStreetNumber() {
        return streetNumber;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }


    // Method to return address
    public String returnAddress() {
        return streetNumber + " " + streetName + "\n" + city + ", " + state + ", " + zipCode;
    }
}
