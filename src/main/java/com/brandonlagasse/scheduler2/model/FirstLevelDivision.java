package com.brandonlagasse.scheduler2.model;

public class FirstLevelDivision {

    //PARAMETERS
    private int id;
    private String name;
    private int countryId;

    //CONSTRUCTOR
    FirstLevelDivision(int id, String name, int countryId){
        this.id = id;
        this.name = name;
        this.countryId = countryId;
    }

    //METHODS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }
}
