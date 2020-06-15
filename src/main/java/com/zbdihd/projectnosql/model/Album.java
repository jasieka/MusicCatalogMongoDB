package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Album")
public class Album {
    @Id
    private String id;

    @NonNull private String name;
    @NonNull private String artistId;
    @NonNull private List<String> tracks;

    @NonNull private Map<String, String> ratings; // "userName;rating"
    @NonNull private int averageRating; //calculated after each operation on ratings

    @NonNull private String imageURL;
    @NonNull private String musicLabelId;
    @NonNull private int releaseYear;
    @NonNull private int numberOfCDs;
    @NonNull private String description;

    @NonNull private Date lastModifiedAt; //or created
}
