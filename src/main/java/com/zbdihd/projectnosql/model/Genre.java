package com.zbdihd.projectnosql.model;

import lombok.Data;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Genre")
public class Genre {
    @Id
    private String id;

    @NonNull private String name;
    @NonNull private List<String> albumIDs;

    public int numberOfAlbums() {
        return albumIDs.size();
    }


}