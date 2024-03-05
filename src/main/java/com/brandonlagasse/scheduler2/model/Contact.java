package com.brandonlagasse.scheduler2.model;

public class Contact {

    //PROPERTIES
    private int id;
    private String name;
    private String email;

    //CONSTRUCTOR
    public Contact(int id,
                   String name,
                   String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }

    //GETTERS/SETTERS
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }



}
