package com.recept_kezelo_mobil.models;

//userhez szukseges
public class Name{
    private String firstname;
    private String lastname;

    public Name() {
    }

    public Name(String firstname, String lastname) {
        this.firstname = firstname;
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }
}