package com.zbdihd.projectnosql.model;

import org.springframework.data.annotation.Id;

public class Artist {
    @Id
    public String id;

    public String firstName;
    public String lastName;

    public Artist() {
    }

    public Artist(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
