package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@Data
@Document(collection = "MusicLabel")
public class MusicLabel {
    @Id
    private String id;

    @NonNull private String name;
    @NonNull private String dateOfCreation; //format dd/MM/yyyy
    @NonNull private String countryOfResidence;
    @NonNull private String chairmanOfTheBoard; //prezes zarządu

    @NonNull private Date lastModifiedAt; //or created
}