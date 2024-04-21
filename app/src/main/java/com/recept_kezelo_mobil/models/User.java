package com.recept_kezelo_mobil.models;

import java.util.Map;

public class User {
    private String id;
    private  String email;
    private Name name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public User(String id, String email, Name name) {
        this.id = id;
        this.email = email;
        this.name = name;
    }

    public User() {
    }
}
