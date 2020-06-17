package com.zbdihd.projectnosql.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "Genre")
public class Genre {
    @Id
    private String id;

    private String name;
    @DBRef
    private List<Album> albumList;

    public int numberOfAlbums() {
        return albumList.size();
    }


    public Genre(String name, List<Album> albumList) {
        this.name = name;
        this.albumList = albumList;
    }
}