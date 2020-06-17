package com.zbdihd.projectnosql.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "Artist")
public class Artist {
    @Id
    private String id;

    private String name; //Name and Surname or band name
    private String country;
    private String birthDate; //format dd/MM/yyyy
    private String dateOfDeath; //format dd/MM/yyyy

    private Date lastModifiedAt; //or created


    public Artist(String name, String country, String birthDate, String dateOfDeath, Date lastModifiedAt) {
        this.name = name;
        this.country = country;
        this.birthDate = birthDate;
        this.dateOfDeath = dateOfDeath;
        this.lastModifiedAt = lastModifiedAt;
    }
}
