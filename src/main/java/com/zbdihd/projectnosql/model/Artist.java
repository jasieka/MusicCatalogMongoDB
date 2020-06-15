package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "Artist")
public class Artist {
    @Id
    private String id;

    @NonNull private String name; //Name and Surname or band name
    @NonNull private String country;
    @NonNull private String birthDate; //format dd/MM/yyyy
    @NonNull private String dateOfDeath; //format dd/MM/yyyy

    @NonNull private Date lastModifiedAt; //or created
}
