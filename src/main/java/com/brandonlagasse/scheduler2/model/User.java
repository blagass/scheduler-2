package com.brandonlagasse.scheduler2.model;

/**
 * The User is the the one logging into the application, with assigned appointments, as well as associated reporting.
 */
public class User {

    //PARAMETERS
    private int id;
    private String name;
    private String password;

    //CONSTRUCTOR
    public User(int id, String name, String password){
        this.id = id;
        this.name = name;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
