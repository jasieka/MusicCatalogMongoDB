package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "MusicLabel")
public class MusicLabel {
    @Id
    public String id;

    @NonNull public String name;
    @NonNull public String dateOfCreation; //format dd/MM/yyyy
    @NonNull public String countryOfResidence;
    @NonNull public String chairmanOfTheBoard; //prezes zarządu
}