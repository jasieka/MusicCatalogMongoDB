package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "Artist")
public class Artist {
    @Id
    public String id;

    @NonNull public String name; //Name and Surname or band name
    @NonNull public String country;
    @NonNull public String birthDate; //format dd/MM/yyyy
    @NonNull public String dateOfDeath; //format dd/MM/yyyy
}
