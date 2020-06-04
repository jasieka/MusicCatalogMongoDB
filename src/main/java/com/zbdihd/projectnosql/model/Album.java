package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "Album")
public class Album {
    @Id
    public String id;

    @NonNull public String name;
    @NonNull public String artistId;
    @NonNull public List<String> tracks;

    @NonNull public Map<String, String> ratings; // "userName;rating"
    @NonNull public int averageRating; //calculated after each operation on ratings

    @NonNull public String imageURL;
    @NonNull public String musicLabelId;
    @NonNull public int releaseYear;
    @NonNull public int numberOfCDs;
    @NonNull public String description;
}
