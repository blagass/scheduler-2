package com.brandonlagasse.scheduler2.model;

public class Country {

    //PROPERTIES
    private int id;
    private String name;

    //CONSTRUCTOR
    public Country(int id,
                   String name){
        this.id = id;
        this.name = name;
    }


    //GETTERS/SETTERS
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String geName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
